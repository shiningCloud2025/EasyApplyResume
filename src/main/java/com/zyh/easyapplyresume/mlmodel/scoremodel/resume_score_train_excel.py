# -*- coding: utf-8 -*-
"""
简历评分模型训练脚本（Excel 输入单文件版）

依赖安装：
pip install pandas openpyxl numpy scikit-learn xgboost requests

运行示例：
python resume_score_train_excel.py --excel-path "D:/train_data.xlsx"

说明：
除训练数据路径外，其余训练参数默认固定在脚本中。
运行前需通过环境变量提供 DASHSCOPE_API_KEY。

流程:
树₁: 预测 → 算残差(实际分-预测分)
树₂: 专门学习残差 → 修正 → 新残差变小
树₃: 继续学剩下的残差 → 再修正 → 残差更小
...
树₃₀₀: 残差已经微乎其微
最终预测 = 树₁ + 树₂ + 树₃ + ... + 树₃₀₀


"""

import argparse
import json
import logging
import os
import re
import time
from datetime import datetime
from pathlib import Path
from typing import Any, Dict, List, Union

import numpy as np
import pandas as pd
import requests
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
from sklearn.model_selection import train_test_split
from xgboost import XGBRegressor


LOGGER = logging.getLogger("resume_score_train_excel")
SCRIPT_PATH = Path(__file__).resolve()
DEFAULT_OUTPUT_ROOT = SCRIPT_PATH.parent / "upload"

COL_INDUSTRY_NAME = "行业名称"
COL_RESUME_NAME = "简历名称"
COL_RESUME_CONTENT = "简历内容"
COL_LABEL_SCORE = "训练标签分数"

# 固定训练配置：平时只需要传训练 Excel 路径，其余参数默认从这里读取。
FIXED_SHEET_NAME: Union[int, str] = 0  # 读取第几个 sheet；0 表示第一个 sheet
FIXED_EMBEDDING_MODEL_NAME = "text-embedding-v4"  # 固定使用 DashScope embedding 模型
FIXED_DASHSCOPE_EMBEDDING_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/embeddings"
FIXED_DASHSCOPE_TIMEOUT_SECONDS = 120  # 单次 embedding 请求超时时间
FIXED_EMBEDDING_DIMENSIONS = 512  # 固定使用 512 维；训练和推理必须保持一致
FIXED_MODEL_NAME = "resume-score-model"  # 模型名称；会写入 summary 和输出文件名
FIXED_MODEL_TYPE = "xgboost"  # 模型类型标识；当前脚本固定为 xgboost
FIXED_OUTPUT_ROOT = DEFAULT_OUTPUT_ROOT  # 模型输出根目录
FIXED_BATCH_SIZE = 8  # 一次送入 embedding 接口的文本条数
FIXED_MAX_LENGTH = 8192  # 该参数在 DashScope 方案下不再参与截断，先保留占位
FIXED_MIN_CONTENT_LENGTH = 20  # 简历内容少于该长度时直接过滤
FIXED_TEST_SIZE = 0.2  # 验证集比例；0.2 表示 20% 样本用于验证
FIXED_RANDOM_STATE = 42  # 随机种子；用于保证切分和训练结果可复现
FIXED_N_ESTIMATORS = 300  # XGBoost 总树数量
FIXED_LEARNING_RATE = 0.05  # 学习率；每棵树修正前面结果的力度
FIXED_MAX_DEPTH = 6  # 单棵树最多长多深
FIXED_SUBSAMPLE = 0.9  # 每棵树随机使用多少比例的样本
FIXED_COLSAMPLE_BYTREE = 0.9  # 每棵树随机使用多少比例的特征列
FIXED_REG_LAMBDA = 1.0  # L2 正则强度；用于抑制过拟合
FIXED_N_JOBS = -1  # 并行线程数；-1 表示尽量使用全部 CPU 线程
FIXED_USE_FP16 = False  # DashScope 方案下该参数不再生效，先保留占位


def init_logger() -> None:
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s [%(levelname)s] %(message)s",
    )


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="简历评分模型训练脚本（Excel 输入版，固定参数版）")
    parser.add_argument("--excel-path", required=True, help="训练 Excel 路径")
    return parser.parse_args()


def normalize_text(value: Any) -> str:
    if value is None:
        return ""
    return str(value).strip()


def build_sample_text(resume_name: str, industry_name: str, resume_content: str) -> str:
    # 把多列字段拼成 embedding 模型真正要吃的一段完整文本。
    return (
        f"[简历名称]: {resume_name.strip()}\n"
        f"[行业名称]: {industry_name.strip()}\n"
        f"[简历内容]: {resume_content.strip()}"
    )


def load_excel_samples(excel_path: str, sheet_name: Union[int, str], min_content_length: int) -> List[Dict[str, Any]]:
    df = pd.read_excel(excel_path, sheet_name=sheet_name, engine="openpyxl")

    required_columns = [
        COL_INDUSTRY_NAME,
        COL_RESUME_NAME,
        COL_RESUME_CONTENT,
        COL_LABEL_SCORE,
    ]
    missing_columns = [col for col in required_columns if col not in df.columns]
    if missing_columns:
        raise ValueError(f"Excel 缺少必需列: {missing_columns}")

    df = df[required_columns].copy()
    df = df.rename(
        columns={
            COL_INDUSTRY_NAME: "industry_name",
            COL_RESUME_NAME: "resume_name",
            COL_RESUME_CONTENT: "resume_content",
            COL_LABEL_SCORE: "label_score",
        }
    )

    # 统一把文本列转成干净字符串，避免空值、空格、非字符串类型影响训练。
    df["resume_name"] = df["resume_name"].fillna("").astype(str).str.strip()
    df["industry_name"] = df["industry_name"].fillna("").astype(str).str.strip()
    df["resume_content"] = df["resume_content"].fillna("").astype(str).str.strip()
    df["label_score"] = pd.to_numeric(df["label_score"], errors="coerce")  # 非数字标签会被转成 NaN

    # 过滤掉简历名称、行业名称、简历内容为空，或标签分数无效的样本。
    df = df[
        (df["resume_name"] != "")
        & (df["industry_name"] != "")
        & (df["resume_content"] != "")
        & df["label_score"].notna()
    ].copy()

    # 过滤过短简历，并按关键字段去重，避免重复样本反复参与训练。
    df = df[df["resume_content"].str.len() >= min_content_length].copy()
    df = df.drop_duplicates(subset=["resume_name", "industry_name", "resume_content"]).copy()

    if df.empty:
        raise ValueError("清洗后没有可用训练样本")

    sample_texts = []
    for _, row in df.iterrows():
        sample_texts.append(
            build_sample_text(
                resume_name=row["resume_name"],
                industry_name=row["industry_name"],
                resume_content=row["resume_content"],
            )
        )
    df["sample_text"] = sample_texts

    LOGGER.info("Excel 样本读取并清洗完成，剩余 %s 条", len(df))

    result: List[Dict[str, Any]] = []
    for _, row in df.iterrows():
        result.append(
            {
                "resume_name": row["resume_name"],
                "industry_name": row["industry_name"],
                "resume_content": row["resume_content"],
                "label_score": float(row["label_score"]),
                "sample_text": row["sample_text"],
            }
        )
    return result


def build_embeddings(
    texts: List[str],
    embedding_model_name: str,
    batch_size: int,
    max_length: int,
    use_fp16: bool,
) -> np.ndarray:
    # DashScope API 方案下，这两个参数保留只是为了不改主流程调用。
    _ = max_length
    _ = use_fp16

    api_key = os.getenv("DASHSCOPE_API_KEY")
    if not api_key:
        raise ValueError("缺少环境变量 DASHSCOPE_API_KEY")

    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json",
    }

    all_embeddings: List[List[float]] = []

    LOGGER.info("开始调用 DashScope embedding 模型: %s", embedding_model_name)
    LOGGER.info("开始生成 embedding，样本数: %s", len(texts))

    for start in range(0, len(texts), batch_size):
        batch_texts = texts[start:start + batch_size]

        payload = {
            "model": embedding_model_name,
            "input": batch_texts,
        }

        if FIXED_EMBEDDING_DIMENSIONS is not None:
            payload["dimensions"] = FIXED_EMBEDDING_DIMENSIONS

        response = requests.post(
            FIXED_DASHSCOPE_EMBEDDING_URL,
            headers=headers,
            json=payload,
            timeout=FIXED_DASHSCOPE_TIMEOUT_SECONDS,
        )

        if response.status_code != 200:
            raise ValueError(f"DashScope embedding 调用失败: {response.status_code} {response.text}")

        result = response.json()
        data = result.get("data")
        if not isinstance(data, list):
            raise ValueError(f"DashScope embedding 返回格式异常: {result}")

        data = sorted(data, key=lambda item: item.get("index", 0))
        batch_embeddings = [item["embedding"] for item in data]

        if len(batch_embeddings) != len(batch_texts):
            raise ValueError("embedding 返回数量和输入数量不一致")

        all_embeddings.extend(batch_embeddings)
        LOGGER.info("embedding 进度: %s/%s", min(start + batch_size, len(texts)), len(texts))

    embeddings = np.asarray(all_embeddings, dtype=np.float32)
    if embeddings.ndim != 2:
        raise ValueError("embedding 结果维度不正确")

    LOGGER.info("embedding 完成，向量维度: %s", embeddings.shape[1])
    return embeddings


def compute_metric_block(y_true: np.ndarray, y_pred: np.ndarray, prefix: str) -> Dict[str, Any]:
    return {
        f"{prefix}_rmse": round(float(np.sqrt(mean_squared_error(y_true, y_pred))), 6),
        f"{prefix}_mae": round(float(mean_absolute_error(y_true, y_pred)), 6),
        f"{prefix}_r2": round(float(r2_score(y_true, y_pred)), 6),
    }


def train_xgboost_model(X: np.ndarray, y: np.ndarray):
    model = XGBRegressor(
        objective="reg:squarederror",  # 回归任务：预测连续分数
        n_estimators=FIXED_N_ESTIMATORS,  # 总共训练多少棵树
        learning_rate=FIXED_LEARNING_RATE,  # 每棵树修正前面结果的力度
        max_depth=FIXED_MAX_DEPTH,  # 单棵树最多长多深
        subsample=FIXED_SUBSAMPLE,  # 每棵树随机使用多少比例的样本
        colsample_bytree=FIXED_COLSAMPLE_BYTREE,  # 每棵树随机使用多少比例的特征列
        reg_lambda=FIXED_REG_LAMBDA,  # L2 正则，防止模型过拟合
        random_state=FIXED_RANDOM_STATE,  # 固定随机性，方便复现
        n_jobs=FIXED_N_JOBS,  # 并行线程数
        tree_method="hist",  # 直方图加速建树，通常更快更省资源
    )

    if len(X) >= 10:
        X_train, X_valid, y_train, y_valid = train_test_split(
            X,
            y,
            test_size=FIXED_TEST_SIZE,
            random_state=FIXED_RANDOM_STATE,
        )

        LOGGER.info("开始训练 XGBoost，训练集=%s，验证集=%s", len(X_train), len(X_valid))
        model.fit(X_train, y_train)

        train_pred = model.predict(X_train)
        valid_pred = model.predict(X_valid)

        metrics = {
            "eval_mode": "train_valid_split",
            "train_size": int(len(X_train)),
            "valid_size": int(len(X_valid)),
        }
        metrics.update(compute_metric_block(y_train, train_pred, "train"))
        metrics.update(compute_metric_block(y_valid, valid_pred, "valid"))
        return model, metrics

    LOGGER.warning("样本数量小于10，改为全量训练并在训练集上评估")
    model.fit(X, y)

    pred = model.predict(X)
    metrics = {
        "eval_mode": "train_only",
        "train_size": int(len(X)),
        "valid_size": 0,
    }
    metrics.update(compute_metric_block(y, pred, "train"))
    return model, metrics


def sanitize_file_name(file_name: str) -> str:
    file_name = normalize_text(file_name)
    file_name = re.sub(r'[<>:"/\\|?*\x00-\x1f]+', "_", file_name)
    file_name = re.sub(r"\s+", "_", file_name)
    file_name = re.sub(r"_+", "_", file_name)
    file_name = file_name.strip("._-")
    return file_name or "resume_score_model"


def sanitize_path_segment(value: str) -> str:
    value = normalize_text(value)
    value = re.sub(r"[^A-Za-z0-9._-]+", "_", value)
    value = re.sub(r"_+", "_", value)
    value = value.strip("._-")
    return value or "unknown"


def build_version() -> str:
    return datetime.now().strftime("v%Y.%m.%d-%H%M%S")


def resolve_unique_path(target_path: Path) -> Path:
    if not target_path.exists():
        return target_path

    suffix_index = 1
    stem = target_path.stem
    suffix = target_path.suffix
    parent = target_path.parent

    while True:
        candidate = parent / f"{stem}_{suffix_index}{suffix}"
        if not candidate.exists():
            return candidate
        suffix_index += 1


def save_outputs(
    model: XGBRegressor,
    metrics: Dict[str, Any],
    sample_count: int,
    embedding_dimension: int,
    train_cost_ms: int,
) -> Dict[str, Any]:
    version = build_version()
    date_str = datetime.now().strftime("%Y%m%d")

    safe_model_type = sanitize_path_segment(FIXED_MODEL_TYPE)
    safe_model_name_for_file = sanitize_file_name(FIXED_MODEL_NAME)
    safe_version_for_file = sanitize_file_name(version)

    relative_dir = Path("mlmodel") / safe_model_type / date_str
    full_dir = Path(FIXED_OUTPUT_ROOT).resolve() / relative_dir
    full_dir.mkdir(parents=True, exist_ok=True)

    model_file_name = f"{safe_model_name_for_file}_{safe_version_for_file}.json"
    model_path = resolve_unique_path(full_dir / model_file_name)

    metrics["embedding_dimension"] = int(embedding_dimension)
    metrics["sample_count"] = int(sample_count)
    metrics["embedding_model"] = FIXED_EMBEDDING_MODEL_NAME
    metrics["model_type"] = FIXED_MODEL_TYPE

    model.save_model(str(model_path))

    metrics_path = model_path.with_suffix(".metrics.json")
    metrics_path.write_text(
        json.dumps(metrics, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )

    relative_model_url = str((relative_dir / model_path.name).as_posix())

    summary = {
        "scoreModelVersionModelName": FIXED_MODEL_NAME,
        "scoreModelVersionVersion": version,
        "scoreModelVersionModelType": FIXED_MODEL_TYPE,
        "scoreModelVersionModelUrl": relative_model_url,
        "scoreModelVersionEmbeddingModel": FIXED_EMBEDDING_MODEL_NAME,
        "scoreModelVersionSampleCount": int(sample_count),
        "scoreModelVersionTrainCostMs": int(train_cost_ms),
        "scoreModelVersionMetricJson": json.dumps(metrics, ensure_ascii=False),
        "scoreModelVersionIsActive": 0,
        "localModelAbsolutePath": str(model_path),
        "localMetricAbsolutePath": str(metrics_path),
    }

    summary_path = model_path.with_suffix(".summary.json")
    summary_path.write_text(
        json.dumps(summary, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )

    return summary


def main() -> None:
    init_logger()
    args = parse_args()

    start_time = time.time()

    samples = load_excel_samples(
        excel_path=args.excel_path,
        sheet_name=FIXED_SHEET_NAME,
        min_content_length=FIXED_MIN_CONTENT_LENGTH,
    )

    # texts 是模型输入文本，labels 是人工打分标签。
    texts = [item["sample_text"] for item in samples]
    labels = np.asarray([item["label_score"] for item in samples], dtype=np.float32)

    # embedding 阶段通过 DashScope 接口把文本转成数值向量，后面 XGBoost 只吃这个向量结果。
    embeddings = build_embeddings(
        texts=texts,
        embedding_model_name=FIXED_EMBEDDING_MODEL_NAME,
        batch_size=FIXED_BATCH_SIZE,
        max_length=FIXED_MAX_LENGTH,
        use_fp16=FIXED_USE_FP16,
    )

    model, metrics = train_xgboost_model(
        X=embeddings,
        y=labels,
    )

    train_cost_ms = int((time.time() - start_time) * 1000)

    summary = save_outputs(
        model=model,
        metrics=metrics,
        sample_count=len(samples),
        embedding_dimension=int(embeddings.shape[1]),
        train_cost_ms=train_cost_ms,
    )

    LOGGER.info("训练完成")
    print(json.dumps(summary, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

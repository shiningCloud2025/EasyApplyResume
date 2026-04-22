# -*- coding: utf-8 -*-
"""
简历评分模型训练脚本（Excel 输入单文件版）

依赖安装：
pip install pandas openpyxl numpy scikit-learn xgboost FlagEmbedding

运行示例：
python resume_score_train_excel.py --excel-path "D:/train_data.xlsx"

可选示例：
python resume_score_train_excel.py \
  --excel-path "D:/train_data.xlsx" \
  --sheet-name 0 \
  --embedding-model-name "BAAI/bge-m3" \
  --model-name "resume-score-model" \
  --model-type "xgboost"
"""

import argparse
import json
import logging
import re
import time
from datetime import datetime
from pathlib import Path
from typing import Any, Dict, List, Union

import numpy as np
import pandas as pd
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
from sklearn.model_selection import train_test_split
from xgboost import XGBRegressor


LOGGER = logging.getLogger("resume_score_train_excel")
SCRIPT_PATH = Path(__file__).resolve()
PROJECT_ROOT = SCRIPT_PATH.parents[8]
DEFAULT_OUTPUT_ROOT = PROJECT_ROOT / "upload"

COL_ID = "训练数据ID"
COL_INDUSTRY_NAME = "行业名称"
COL_RESUME_NAME = "简历名称"
COL_RESUME_CONTENT = "简历内容"
COL_LABEL_SCORE = "训练标签分数"


def init_logger() -> None:
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s [%(levelname)s] %(message)s",
    )


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="简历评分模型训练脚本（Excel 输入版）")
    parser.add_argument("--excel-path", required=True, help="训练 Excel 路径")
    parser.add_argument("--sheet-name", default="0", help="sheet 名称或索引，默认 0")
    parser.add_argument("--embedding-model-name", default="BAAI/bge-m3", help="embedding 模型名")
    parser.add_argument("--model-name", default="resume-score-model", help="模型名称")
    parser.add_argument("--model-type", default="xgboost", help="模型类型")
    parser.add_argument("--output-root", default=str(DEFAULT_OUTPUT_ROOT), help="模型输出根目录")
    parser.add_argument("--batch-size", type=int, default=8, help="embedding 批大小")
    parser.add_argument("--max-length", type=int, default=8192, help="embedding 最大长度")
    parser.add_argument("--min-content-length", type=int, default=20, help="简历内容最小长度")
    parser.add_argument("--test-size", type=float, default=0.2, help="验证集比例")
    parser.add_argument("--random-state", type=int, default=42, help="随机种子")
    parser.add_argument("--n-estimators", type=int, default=300, help="XGBoost 树数量")
    parser.add_argument("--learning-rate", type=float, default=0.05, help="学习率")
    parser.add_argument("--max-depth", type=int, default=6, help="树深度")
    parser.add_argument("--subsample", type=float, default=0.9, help="subsample")
    parser.add_argument("--colsample-bytree", type=float, default=0.9, help="colsample_bytree")
    parser.add_argument("--reg-lambda", type=float, default=1.0, help="L2 正则")
    parser.add_argument("--n-jobs", type=int, default=-1, help="并行线程数")
    parser.add_argument("--use-fp16", action="store_true", help="如果有 GPU，可以打开 fp16")
    parser.add_argument("--version", default="", help="手动指定版本号，不传则自动生成")
    return parser.parse_args()


def resolve_sheet_name(sheet_name_arg: str) -> Union[int, str]:
    sheet_name_arg = str(sheet_name_arg).strip()
    if sheet_name_arg.isdigit():
        return int(sheet_name_arg)
    return sheet_name_arg


def normalize_text(value: Any) -> str:
    if value is None:
        return ""
    return str(value).strip()


def build_sample_text(resume_name: str, industry_name: str, resume_content: str) -> str:
    return (
        f"[简历名称] {resume_name.strip()}\n"
        f"[行业名称] {industry_name.strip()}\n"
        f"[简历内容] {resume_content.strip()}"
    )


def load_excel_samples(excel_path: str, sheet_name: Union[int, str], min_content_length: int) -> List[Dict[str, Any]]:
    df = pd.read_excel(excel_path, sheet_name=sheet_name, engine="openpyxl")

    required_columns = [
        COL_ID,
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
            COL_ID: "sample_id",
            COL_INDUSTRY_NAME: "industry_name",
            COL_RESUME_NAME: "resume_name",
            COL_RESUME_CONTENT: "resume_content",
            COL_LABEL_SCORE: "label_score",
        }
    )

    df["resume_name"] = df["resume_name"].fillna("").astype(str).str.strip()
    df["industry_name"] = df["industry_name"].fillna("").astype(str).str.strip()
    df["resume_content"] = df["resume_content"].fillna("").astype(str).str.strip()
    df["label_score"] = pd.to_numeric(df["label_score"], errors="coerce")

    df = df[
        (df["resume_name"] != "")
        & (df["industry_name"] != "")
        & (df["resume_content"] != "")
        & df["label_score"].notna()
    ].copy()

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
                "sample_id": int(row["sample_id"]) if pd.notna(row["sample_id"]) else None,
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
    from FlagEmbedding import BGEM3FlagModel

    LOGGER.info("开始加载 embedding 模型: %s", embedding_model_name)
    embedding_model = BGEM3FlagModel(embedding_model_name, use_fp16=use_fp16)

    LOGGER.info("开始生成 embedding，样本数: %s", len(texts))
    result = embedding_model.encode(
        texts,
        batch_size=batch_size,
        max_length=max_length,
    )

    embeddings = np.asarray(result["dense_vecs"], dtype=np.float32)
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


def train_xgboost_model(X: np.ndarray, y: np.ndarray, args: argparse.Namespace):
    model = XGBRegressor(
        objective="reg:squarederror",
        n_estimators=args.n_estimators,
        learning_rate=args.learning_rate,
        max_depth=args.max_depth,
        subsample=args.subsample,
        colsample_bytree=args.colsample_bytree,
        reg_lambda=args.reg_lambda,
        random_state=args.random_state,
        n_jobs=args.n_jobs,
        tree_method="hist",
    )

    if len(X) >= 10:
        X_train, X_valid, y_train, y_valid = train_test_split(
            X,
            y,
            test_size=args.test_size,
            random_state=args.random_state,
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


def build_version(args: argparse.Namespace) -> str:
    if normalize_text(args.version):
        return normalize_text(args.version)
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
    args: argparse.Namespace,
    sample_count: int,
    embedding_dimension: int,
    train_cost_ms: int,
) -> Dict[str, Any]:
    version = build_version(args)
    date_str = datetime.now().strftime("%Y%m%d")

    safe_model_type = sanitize_path_segment(args.model_type)
    safe_model_name_for_file = sanitize_file_name(args.model_name)
    safe_version_for_file = sanitize_file_name(version)

    relative_dir = Path("mlmodel") / safe_model_type / date_str
    full_dir = Path(args.output_root).resolve() / relative_dir
    full_dir.mkdir(parents=True, exist_ok=True)

    model_file_name = f"{safe_model_name_for_file}_{safe_version_for_file}.json"
    model_path = resolve_unique_path(full_dir / model_file_name)

    metrics["embedding_dimension"] = int(embedding_dimension)
    metrics["sample_count"] = int(sample_count)
    metrics["embedding_model"] = args.embedding_model_name
    metrics["model_type"] = args.model_type

    model.save_model(str(model_path))

    metrics_path = model_path.with_suffix(".metrics.json")
    metrics_path.write_text(
        json.dumps(metrics, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )

    relative_model_url = str((relative_dir / model_path.name).as_posix())

    summary = {
        "scoreModelVersionModelName": args.model_name,
        "scoreModelVersionVersion": version,
        "scoreModelVersionModelType": args.model_type,
        "scoreModelVersionModelUrl": relative_model_url,
        "scoreModelVersionEmbeddingModel": args.embedding_model_name,
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
        sheet_name=resolve_sheet_name(args.sheet_name),
        min_content_length=args.min_content_length,
    )

    texts = [item["sample_text"] for item in samples]
    labels = np.asarray([item["label_score"] for item in samples], dtype=np.float32)

    embeddings = build_embeddings(
        texts=texts,
        embedding_model_name=args.embedding_model_name,
        batch_size=args.batch_size,
        max_length=args.max_length,
        use_fp16=args.use_fp16,
    )

    model, metrics = train_xgboost_model(
        X=embeddings,
        y=labels,
        args=args,
    )

    train_cost_ms = int((time.time() - start_time) * 1000)

    summary = save_outputs(
        model=model,
        metrics=metrics,
        args=args,
        sample_count=len(samples),
        embedding_dimension=int(embeddings.shape[1]),
        train_cost_ms=train_cost_ms,
    )

    LOGGER.info("训练完成")
    print(json.dumps(summary, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

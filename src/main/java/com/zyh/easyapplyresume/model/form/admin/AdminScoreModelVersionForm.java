package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 简历评分模型版本表单
 *
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分模型版本表单")
public class AdminScoreModelVersionForm {

    /**
     * 模型版本id，修改时必填
     */
    @Schema(description = "模型版本id，修改时必填")
    private Integer scoreModelVersionId;

    /**
     * 模型名称，最长64字符
     */
    @Schema(description = "模型名称，最长64字符")
    private String scoreModelVersionModelName;

    /**
     * 模型版本号，最长64字符
     */
    @Schema(description = "模型版本号，最长64字符")
    private String scoreModelVersionVersion;

    /**
     * 模型类型，如xgboost，最长32字符
     */
    @Schema(description = "模型类型，如xgboost，最长32字符")
    private String scoreModelVersionModelType;

    /**
     * 模型访问地址，最长2056字符
     */
    @Schema(description = "模型访问地址，最长2056字符")
    private String scoreModelVersionModelUrl;

    /**
     * 使用的embedding模型，如bge-m3，最长64字符
     */
    @Schema(description = "使用的embedding模型，如bge-m3，最长64字符")
    private String scoreModelVersionEmbeddingModel;

    /**
     * 训练样本数量
     */
    @Schema(description = "训练样本数量")
    private Integer scoreModelVersionSampleCount;

    /**
     * 训练耗时毫秒
     */
    @Schema(description = "训练耗时毫秒")
    private Long scoreModelVersionTrainCostMs;

    /**
     * 评估指标JSON
     */
    @Schema(description = "评估指标JSON")
    private String scoreModelVersionMetricJson;

    /**
     * 是否当前启用版本，0否1是
     */
    @Schema(description = "是否当前启用版本，0否1是")
    private Integer scoreModelVersionIsActive;
}

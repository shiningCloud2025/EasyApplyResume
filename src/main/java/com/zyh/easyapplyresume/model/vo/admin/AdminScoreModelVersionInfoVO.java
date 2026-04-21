package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 简历评分模型版本详情VO
 *
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分模型版本详情VO")
public class AdminScoreModelVersionInfoVO {

    /**
     * 模型版本id
     */
    @Schema(description = "模型版本id")
    private Integer scoreModelVersionId;

    /**
     * 模型名称
     */
    @Schema(description = "模型名称")
    private String scoreModelVersionModelName;

    /**
     * 模型版本号
     */
    @Schema(description = "模型版本号")
    private String scoreModelVersionVersion;

    /**
     * 模型类型
     */
    @Schema(description = "模型类型")
    private String scoreModelVersionModelType;

    /**
     * 模型访问地址
     */
    @Schema(description = "模型访问地址")
    private String scoreModelVersionModelUrl;

    /**
     * 使用的embedding模型
     */
    @Schema(description = "使用的embedding模型")
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
     * 是否当前启用版本
     */
    @Schema(description = "是否当前启用版本")
    private Integer scoreModelVersionIsActive;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime scoreModelVersionCreateTime;
}

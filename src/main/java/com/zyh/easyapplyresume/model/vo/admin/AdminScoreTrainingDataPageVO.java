package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 简历评分训练数据分页VO
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分训练数据分页VO")
public class AdminScoreTrainingDataPageVO {
    /**
     * 训练数据id
     */
    @Schema(description = "训练数据id")
    private Integer scoreTrainingDataId;
    /**
     * 简历名称
     */
    @Schema(description = "简历名称")
    private String scoreTrainingDataResumeName;
    /**
     * 行业名称
     */
    @Schema(description = "行业名称")
    private String scoreTrainingDataIndustryName;
    /**
     * 训练标签分数
     */
    @Schema(description = "训练标签分数")
    private Double scoreTrainingDataLabelScore;
    /**
     * 数据来源：0人工，1使用模型
     */
    @Schema(description = "数据来源：0人工，1使用模型")
    private Integer scoreTrainingDataDataSource;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime scoreTrainingDataCreateTime;
}

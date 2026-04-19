package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 简历评分训练数据表单
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分训练数据表单")
public class AdminScoreTrainingDataForm {
    /**
     * 训练数据id，修改时必填
     */
    @Schema(description = "训练数据id，修改时必填")
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
     * 简历内容（React代码/文本）
     */
    @Schema(description = "简历内容（React代码/文本）")
    private String scoreTrainingDataResumeContent;
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
}

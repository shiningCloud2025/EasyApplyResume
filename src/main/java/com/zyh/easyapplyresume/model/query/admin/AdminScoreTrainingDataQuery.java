package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 简历评分训练数据分页查询条件
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分训练数据分页查询条件")
public class AdminScoreTrainingDataQuery {
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
     * 数据来源：0人工，1使用模型
     */
    @Schema(description = "数据来源：0人工，1使用模型")
    private Integer scoreTrainingDataDataSource;
}

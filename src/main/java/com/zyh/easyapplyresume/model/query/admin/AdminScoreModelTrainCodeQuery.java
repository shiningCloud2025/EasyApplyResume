package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 简历评分模型训练代码分页查询条件
 *
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分模型训练代码分页查询条件")
public class AdminScoreModelTrainCodeQuery {

    /**
     * 训练代码名称
     */
    @Schema(description = "训练代码名称")
    private String scoreModelTrainCodeName;

    /**
     * 训练代码语言
     */
    @Schema(description = "训练代码语言")
    private String scoreModelTrainCodeLanguage;
}

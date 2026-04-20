package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 简历评分模型训练代码表单
 *
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分模型训练代码表单")
public class AdminScoreModelTrainCodeForm {

    /**
     * 训练代码id，修改时必填
     */
    @Schema(description = "训练代码id，修改时必填")
    private Integer scoreModelTrainCodeId;

    /**
     * 训练代码名称，最长64字符
     */
    @Schema(description = "训练代码名称，最长64字符")
    private String scoreModelTrainCodeName;

    /**
     * 训练代码版本号，最长64字符
     */
    @Schema(description = "训练代码版本号，最长64字符")
    private String scoreModelTrainCodeVersion;

    /**
     * 训练代码语言，如java、python，最长32字符
     */
    @Schema(description = "训练代码语言，如java、python，最长32字符")
    private String scoreModelTrainCodeLanguage;

    /**
     * 训练代码内容
     */
    @Schema(description = "训练代码内容")
    private String scoreModelTrainCodeContent;

    /**
     * 训练代码描述，最长500字符
     */
    @Schema(description = "训练代码描述，最长500字符")
    private String scoreModelTrainCodeDesc;
}

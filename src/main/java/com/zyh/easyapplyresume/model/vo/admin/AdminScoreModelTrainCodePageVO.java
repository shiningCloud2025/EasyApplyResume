package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 简历评分模型训练代码分页VO
 *
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分模型训练代码分页VO")
public class AdminScoreModelTrainCodePageVO {

    /**
     * 训练代码id
     */
    @Schema(description = "训练代码id")
    private Integer scoreModelTrainCodeId;

    /**
     * 训练代码名称
     */
    @Schema(description = "训练代码名称")
    private String scoreModelTrainCodeName;

    /**
     * 训练代码版本号
     */
    @Schema(description = "训练代码版本号")
    private String scoreModelTrainCodeVersion;

    /**
     * 训练代码语言
     */
    @Schema(description = "训练代码语言")
    private String scoreModelTrainCodeLanguage;

    /**
     * 训练代码描述
     */
    @Schema(description = "训练代码描述")
    private String scoreModelTrainCodeDesc;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime scoreModelTrainCodeCreateTime;
}

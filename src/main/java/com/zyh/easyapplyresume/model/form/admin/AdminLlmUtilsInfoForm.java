package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "LLM工具类调用日志提交表单")
public class AdminLlmUtilsInfoForm {
    @Schema(description = "工具类名")
    private String llmUtilsInfoToolClass;

    @Schema(description = "工具描述/功能分类")
    private String llmUtilsInfoToolDescription;

    @Schema(description = "模型厂商")
    private String llmUtilsInfoModelProvider;

    @Schema(description = "模型名称")
    private String llmUtilsInfoModelName;

    @Schema(description = "输入内容")
    private String llmUtilsInfoInputContent;

    @Schema(description = "输出结果（JSON）")
    private String llmUtilsInfoOutputResult;

    @Schema(description = "响应延迟（毫秒）")
    private Integer llmUtilsInfoLatencyMs;

    @Schema(description = "调用状态（SUCCESS/FAILED）")
    private String llmUtilsInfoStatus;

    @Schema(description = "错误信息")
    private String llmUtilsInfoErrorMessage;
}

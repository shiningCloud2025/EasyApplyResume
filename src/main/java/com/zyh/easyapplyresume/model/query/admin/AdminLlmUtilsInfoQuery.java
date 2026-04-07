package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "LLM工具类调用日志查询参数")
public class AdminLlmUtilsInfoQuery {

    @Schema(description = "工具描述/功能分类")
    private String llmUtilsInfoToolDescription;

    @Schema(description = "模型厂商")
    private String llmUtilsInfoModelProvider;

    @Schema(description = "模型名称")
    private String llmUtilsInfoModelName;

    @Schema(description = "调用状态（SUCCESS/FAILED）")
    private String llmUtilsInfoStatus;



}

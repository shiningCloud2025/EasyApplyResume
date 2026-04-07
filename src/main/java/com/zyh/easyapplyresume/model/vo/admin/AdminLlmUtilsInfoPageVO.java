package com.zyh.easyapplyresume.model.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "LLM工具类调用日志分页信息")
public class AdminLlmUtilsInfoPageVO {

    @Schema(description = "LLM工具信息ID")
    private Long llmUtilsInfoId;

    @Schema(description = "工具类名")
    private String llmUtilsInfoToolClass;

    @Schema(description = "工具描述/功能分类")
    private String llmUtilsInfoToolDescription;

    @Schema(description = "模型厂商")
    private String llmUtilsInfoModelProvider;

    @Schema(description = "模型名称")
    private String llmUtilsInfoModelName;

    @Schema(description = "响应延迟（毫秒）")
    private Integer llmUtilsInfoLatencyMs;

    @Schema(description = "调用状态（SUCCESS/FAILED）")
    private String llmUtilsInfoStatus;

    @Schema(description = "错误信息")
    private String llmUtilsInfoErrorMessage;

    @Schema(description = "调用时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime llmUtilsInfoCreatedTime;


}

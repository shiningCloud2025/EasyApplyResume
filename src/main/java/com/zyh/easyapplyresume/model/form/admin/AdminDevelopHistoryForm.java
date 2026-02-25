package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "发展历程表单")
public class AdminDevelopHistoryForm {
    @Schema(description = "发展历程id")
    private Integer developHistoryId;

    @Schema(description = "发展历程标题")
    private String developHistoryTitle;

    @Schema(description = "发展历程内容")
    private String developHistoryContent;
}

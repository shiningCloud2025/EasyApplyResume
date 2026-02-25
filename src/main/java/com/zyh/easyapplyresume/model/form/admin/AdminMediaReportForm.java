package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "媒体报道表单")
public class AdminMediaReportForm {
    @Schema(description = "媒体报道id")
    private Integer mediaReportId;

    @Schema(description = "媒体报道标题")
    private String mediaReportTitle;

    @Schema(description = "媒体报道内容")
    private String mediaReportContent;
}

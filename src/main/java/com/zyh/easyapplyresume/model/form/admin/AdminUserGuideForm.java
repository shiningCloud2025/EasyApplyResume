package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "使用指南表单")
public class AdminUserGuideForm {
    @Schema(description = "使用指南id")
    private Integer userGuideId;

    @Schema(description = "使用指南标题")
    private String userGuideTitle;

    @Schema(description = "使用指南内容")
    private String userGuideContent;
}

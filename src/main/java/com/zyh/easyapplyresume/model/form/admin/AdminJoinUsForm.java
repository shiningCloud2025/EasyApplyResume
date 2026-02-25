package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "加入我们表单")
public class AdminJoinUsForm {
    @Schema(description = "加入我们id")
    private Integer joinUsId;

    @Schema(description = "加入我们标题")
    private String joinUsTitle;

    @Schema(description = "加入我们内容")
    private String joinUsContent;
}

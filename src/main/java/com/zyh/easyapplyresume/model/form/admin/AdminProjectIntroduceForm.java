package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "项目介绍表单")
public class AdminProjectIntroduceForm {
    @Schema(description = "项目介绍id")
    private Integer projectIntroduceId;

    @Schema(description = "项目介绍标题")
    @NotNull(message = "项目介绍标题不能为空")
    private String projectIntroduceTitle;

    @Schema(description = "项目介绍内容")
    @NotNull(message = "项目介绍内容不能为空")
    private String projectIntroduceContent;
}

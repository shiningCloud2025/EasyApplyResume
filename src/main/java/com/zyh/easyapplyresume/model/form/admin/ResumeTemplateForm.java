package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历模版表单")
public class ResumeTemplateForm {
    @Schema(description = "简历模版id")
    private Integer resumeTemplateId;
    @NotNull(message = "简历模版名称不能为空")
    @Schema(description = "简历模版名称")
    private String resumeTemplateName;
    @NotNull(message = "简历模版代码不能为空")
    @Schema(description = "简历模版代码")
    private String resumeTemplateReactCode;
}

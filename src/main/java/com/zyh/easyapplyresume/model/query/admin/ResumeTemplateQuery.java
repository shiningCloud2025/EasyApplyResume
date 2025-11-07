package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历模版查询")
public class ResumeTemplateQuery {
    @Schema(description = "模版名称")
    private String resumeTemplateName;
    @NotNull(message = "简历模版行业不能为空")
    @Schema(description = "简历模版行业")
    private Integer resumeTemplateIndustry;
}

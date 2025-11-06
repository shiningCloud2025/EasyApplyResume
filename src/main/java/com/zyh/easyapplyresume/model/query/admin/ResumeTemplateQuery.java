package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历模版查询")
public class ResumeTemplateQuery {
    @Schema(description = "模版名称")
    private String resumeTemplateName;
}

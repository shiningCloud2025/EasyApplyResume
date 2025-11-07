package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历模板分页信息")
public class ResumeTemplatePageVO {
    @Schema(description = "简历模板id")
    private Integer resumeTemplateId;
    @Schema(description = "简历模板名称")
    private String resumeTemplateName;
    @Schema(description = "简历模板代码")
    private String resumeTemplateReactCode;
    @Schema(description = "简历模版行业")
    private String industryMapIndustryName;
    @Schema(description = "是否启用")
    private Integer resumeTemplateIsActive;
    @Schema(description = "简历创建时间")
    private String resumeTemplateCreatedTime;
    @Schema(description = "简历修改时间")
    private String resumeTemplateUpdatedTime;
}

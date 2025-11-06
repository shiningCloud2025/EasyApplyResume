package com.zyh.easyapplyresume.model.vo.admin;

import cn.hutool.core.date.DateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历模版信息")
public class ResumeTemplateInfoVO {
    @Schema(description = "简历模版id")
    private Integer resumeTemplateId;
    @Schema(description = "简历模版名称")
    private String resumeTemplateName;
    @Schema(description = "简历模版代码")
    private String resumeTemplateReactCode;
    @Schema(description = "是否启用")
    private Integer isEnable;
    @Schema(description = "简历创建时间")
    private DateTime createTime;
    @Schema(description = "简历更新时间")
    private DateTime updateTime;
}

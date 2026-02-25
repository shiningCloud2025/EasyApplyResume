package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "团队介绍表单")
public class AdminTeamIntroduceForm {
    @Schema(description = "团队介绍id")
    private Integer teamIntroduceId;

    @Schema(description = "团队介绍标题")
    private String teamIntroduceTitle;

    @Schema(description = "团队介绍内容")
    private String teamIntroduceContent;
}

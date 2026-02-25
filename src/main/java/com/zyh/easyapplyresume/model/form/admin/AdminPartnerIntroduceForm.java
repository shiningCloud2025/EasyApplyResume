package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "合作伙伴表单")
public class AdminPartnerIntroduceForm {
    @Schema(description = "合作伙伴id")
    private Integer partnerIntroduceId;

    @Schema(description = "合作伙伴标题")
    @NotNull(message = "合作伙伴标题不能为空")
    private String partnerIntroduceTitle;

    @Schema(description = "合作伙伴内容")
    @NotNull(message = "合作伙伴内容不能为空")
    private String partnerIntroduceContent;
}

package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "常见问题表单")
public class AdminFaqForm {
    @Schema(description = "常见问题id")
    private Integer faqId;

    @Schema(description = "常见问题标题")
    private String faqTitle;

    @Schema(description = "常见问题内容")
    private String faqContent;
}

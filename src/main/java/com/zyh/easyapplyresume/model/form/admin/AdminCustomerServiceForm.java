package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "人工客服表单")
public class AdminCustomerServiceForm {
    @Schema(description = "人工客服id")
    private Integer customerServiceId;

    @Schema(description = "人工客服标题")
    private String customerServiceTitle;

    @Schema(description = "人工客服内容")
    private String customerServiceContent;
}

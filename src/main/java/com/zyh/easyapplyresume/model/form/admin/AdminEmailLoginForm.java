package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "邮箱登录表单")
public class AdminEmailLoginForm {
    @Schema(description = "管理员邮箱")
    private String email;

    @Schema(description = "邮箱验证码")
    private String messageCode;
}

package com.zyh.easyapplyresume.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "邮箱登录表单")
public class EmailLoginForm {
    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "邮箱验证码")
    private String messageCode;
}

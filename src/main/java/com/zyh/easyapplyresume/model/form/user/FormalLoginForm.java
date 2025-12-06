package com.zyh.easyapplyresume.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "普通登录表单")
public class FormalLoginForm {

    @Schema(description = "用户账号/手机号/邮箱号")
    private String accountOrPhoneOrEmail;

    @Schema(description = "用户密码")
    private String password;
}

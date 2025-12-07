package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "普通登录表单")
public class AdminFormalLoginForm {

    @Schema(description = "管理员账号/手机号/邮箱号")
    private String accountOrPhoneOrEmail;

    @Schema(description = "管理员密码")
    private String password;
}

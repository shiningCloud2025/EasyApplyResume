package com.zyh.easyapplyresume.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "手机登录表单")
public class PhoneLoginForm {

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "手机验证码")
    private String messageCode;
}

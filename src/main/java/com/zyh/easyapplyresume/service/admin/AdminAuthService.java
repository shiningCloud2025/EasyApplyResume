package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminFormalLoginForm;
import com.zyh.easyapplyresume.model.form.admin.AdminPhoneLoginForm;
import com.zyh.easyapplyresume.model.form.user.EmailLoginForm;

/**
 * @author shiningCloud2025
 */
public interface AdminAuthService {
    /**
     * 普通登录(账号/手机号/邮箱号+密码)
     */
    public String formalLogin(AdminFormalLoginForm formalLoginForm);

    /**
     * 手机登录(手机短信+密码)
     */
    public String phoneLogin(AdminPhoneLoginForm phoneLoginForm);

    /**
     * 邮箱登录(邮箱验证码+密码)
     */
    public String emailLogin(EmailLoginForm emailLoginForm);


;
}

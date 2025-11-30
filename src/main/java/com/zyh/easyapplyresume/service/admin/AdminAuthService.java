package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.user.EmailRegisterForm;
import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;
import com.zyh.easyapplyresume.model.form.user.PhoneRegisterForm;

/**
 * @author shiningCloud2025
 */
public interface AdminAuthService {
    /**
     * 普通登录(账号/手机号/邮箱号+密码)
     */
    public boolean formalLogin(String accountOrPhoneOrEmail, String password);

    /**
     * 手机登录(手机短信+密码)
     */
    public boolean phoneLogin(String phone, String password);

    /**
     * 邮箱登录(邮箱验证码+密码)
     */
    public boolean emailLogin(String email, String password);


    /**
     * 退出登录
     * @return
     */
    public boolean logout();
}

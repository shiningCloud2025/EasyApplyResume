package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;

/**
 * @author shiningCloud2025
 */
public interface UserAuthService {
    /**
     * 普通登录(账号/手机号/邮箱号+密码)
     */
    public boolean formalLogin(String accountOrPhoneOrEmail, String password);

    /**
     * 手机登录(手机短信+密码)
     */
    public boolean phoneLogin(String phone, String messageCode);

    /**
     * 邮箱登录(邮箱验证码+密码)
     */
    public boolean emailLogin(String email, String messageCode);

    /**
     * 普通注册(用户名+用户账号+邮箱+手机+密码)
     */
    public boolean formalRegister(FormalRegisterForm formalRegisterForm);

    /**
     * 生成随机账号(7-10位)
     */
    public String generateRandomAccount();

    /**
     * 退出登录
     * @return
     */
    public boolean logout();






}

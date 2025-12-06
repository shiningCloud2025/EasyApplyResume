package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.form.user.EmailLoginForm;
import com.zyh.easyapplyresume.model.form.user.FormalLoginForm;
import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;
import com.zyh.easyapplyresume.model.form.user.PhoneLoginForm;

/**
 * @author shiningCloud2025
 */
public interface UserAuthService {
    /**
     * 普通登录(账号/手机号/邮箱号+密码)
     */
    public String formalLogin(FormalLoginForm formalLoginForm);

    /**
     * 手机登录(手机短信+密码)
     */
    public String phoneLogin(PhoneLoginForm phoneLoginForm);

    /**
     * 邮箱登录(邮箱验证码+密码)
     */
    public String emailLogin(EmailLoginForm emailLoginForm);

    /**
     * 普通注册(用户名+用户账号+邮箱+手机+密码)
     */
    public String formalRegister(FormalRegisterForm formalRegisterForm);

    /**
     * 生成随机账号(7-10位)
     */
    public String generateRandomAccount();

    /**
     * 退出登录
     * @return
     */
    public void logout(Integer userId);






}

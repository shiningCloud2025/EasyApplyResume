package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.form.user.EmailRegisterForm;
import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;
import com.zyh.easyapplyresume.model.form.user.PhoneRegisterForm;
import com.zyh.easyapplyresume.model.form.user.UserUpdateForm;
import com.zyh.easyapplyresume.model.vo.user.UserInfoVO;

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
     * 普通注册(用户账号+用户名+账号+密码)
     */
    public boolean formalRegister(FormalRegisterForm formalRegisterForm);

    /**
     * 手机注册(用户账号+用户名+账号手机短信+密码)
     */
    public boolean phoneRegister(PhoneRegisterForm phoneRegisterForm);

    /**
     * 邮箱注册(用户账号+用户名+邮箱验证码+密码)
     */
    public boolean emailRegister(EmailRegisterForm emailRegisterForm);

    /**
     * 退出登录
     * @return
     */
    public boolean logout();






}

package com.zyh.easyapplyresume.service.user;
/**
 * @author shiningCloud2025
 */
public interface LoginAndRegisterEmailVerifyService {
    /**
     * 发送邮箱验证码
     * @param toEmail
     */
    public void sendVerifyCode(String toEmail);

    /**
     * 验证邮箱验证码
     * @param email
     * @param inputCode
     */
    public void verifyCode(String email, String inputCode);
}

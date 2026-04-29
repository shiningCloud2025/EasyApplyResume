package com.zyh.easyapplyresume.service.user;
/**
 * @author shiningCloud2025
 */
public interface UserLoginAndRegisterEmailVerifyService {
    /**
     * 发送邮箱验证码
     * @param toEmail
     */
    public void sendVerifyCode(String toEmail);

    /**
     * 验证邮箱验证码（校验成功后删除）
     * @param email
     * @param inputCode
     */
    public void verifyCode(String email, String inputCode);

    /**
     * 验证邮箱验证码（只校验，不删除）
     * @param email
     * @param inputCode
     */
    public void checkCode(String email, String inputCode);

    /**
     * 删除邮箱验证码
     * @param email
     */
    public void deleteCode(String email);
}

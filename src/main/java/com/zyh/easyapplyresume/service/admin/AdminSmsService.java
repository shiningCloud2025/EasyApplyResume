package com.zyh.easyapplyresume.service.admin;
/**
 * @author shiningCloud2025
 */
public interface AdminSmsService {
    /**
     * 发送短信验证码
     * @param phoneNumber
     */
    public void sendVerifyCode(String phoneNumber);

    /**
     * 校验短信验证码
     * @param phoneNumber
     * @param inputCode
     */
    public void verifyCode(String phoneNumber, String inputCode);
}

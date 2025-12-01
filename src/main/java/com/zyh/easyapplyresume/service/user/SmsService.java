package com.zyh.easyapplyresume.service.user;
/**
 * @author shiningCloud2025
 */
public interface SmsService {
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

package com.zyh.easyapplyresume.service.user;
/**
 * @author shiningCloud2025
 */
public interface UserSmsService {
    /**
     * 发送短信验证码
     * @param phoneNumber
     */
    public void sendVerifyCode(String phoneNumber);

    /**
     * 校验短信验证码（校验成功后删除）
     * @param phoneNumber
     * @param inputCode
     */
    public void verifyCode(String phoneNumber, String inputCode);

    /**
     * 校验短信验证码（只校验，不删除）
     * @param phoneNumber
     * @param inputCode
     */
    public void checkCode(String phoneNumber, String inputCode);

    /**
     * 删除短信验证码
     * @param phoneNumber
     */
    public void deleteCode(String phoneNumber);
}

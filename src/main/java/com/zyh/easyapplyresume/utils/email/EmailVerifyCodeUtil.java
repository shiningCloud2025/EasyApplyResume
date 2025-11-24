package com.zyh.easyapplyresume.utils.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * 验证码生成工具类
 */
@Component
public class EmailVerifyCodeUtil {

    // 从配置文件读取验证码长度
    @Value("${custom.verify.code.length}")
    private int codeLength;

    /**
     * 验证码字符池
     * - 大写字母 (A-Z)
     * - 小写字母 (a-z)
     * - 数字 (0-9)
     */
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // 安全随机数生成器
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 生成指定长度的验证码 (大小写字母 + 数字)
     * @return 混合字符验证码
     */
    public String generateCode() {
        // 检查字符池是否为空，避免异常
        if (CODE_CHARACTERS.isEmpty()) {
            throw new IllegalStateException("验证码字符池不能为空");
        }

        StringBuilder codeBuilder = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            // 1. 生成一个随机索引，范围在 [0, CODE_CHARACTERS.length() - 1]
            int randomIndex = SECURE_RANDOM.nextInt(CODE_CHARACTERS.length());

            // 2. 根据索引从字符池中获取一个字符
            char randomChar = CODE_CHARACTERS.charAt(randomIndex);

            // 3. 将字符追加到验证码字符串中
            codeBuilder.append(randomChar);
        }

        return codeBuilder.toString();
    }
}
package com.zyh.easyapplyresume.utils.security;

import cn.hutool.core.util.StrUtil;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdMonitorCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 服务器密码加解密工具类
 *
 * 说明：
 * 1. 用于服务器 SSH 密码这类“需要还原原文”的场景
 * 2. 不要用于用户登录密码，登录密码仍然使用 PasswordEncoder
 * 3. 当前先用固定 KEY 跑通，后续建议把 KEY 挪到配置文件或环境变量
 */
public class ServiceMachinePasswordCryptoUtil {
    /**
     * AES 密钥长度必须是 16 / 24 / 32
     * 当前先使用 16 位，便于快速落地
     */
    private static final String KEY = "zyh2026easyapply";

    private static final String ALGORITHM = "AES";

    private ServiceMachinePasswordCryptoUtil() {}

    /**
     * 加密：
     * 1. 先校验明文密码不能为空
     * 2. 用固定 KEY 构造 AES 密钥
     * 3. 初始化 Cipher 为加密模式
     * 4. 把原始密码字节加密
     * 5. 最后转成 Base64 字符串，方便存数据库
     */
    public static String encrypt(String plainText) {
        try{
            if(StrUtil.isBlank(plainText)){
                throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_PASSWORD_EMPTY);
            }
            // 获取 AES 加密器实例
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 用固定密钥生成 AES key 对象
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            // 初始化为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            // 对明文密码进行加密，得到二进制密文字节
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            // 二进制密文不适合直接存库，这里转成 Base64 字符串后返回
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }catch (BusException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("服务器密码加密失败", e);
        }
    }

    /**
     * 解密：
     * 1. 先校验密文不能为空
     * 2. 用固定 KEY 构造 AES 密钥
     * 3. 初始化 Cipher 为解密模式
     * 4. 先把 Base64 字符串还原成密文字节
     * 5. 再把密文字节解密回原始明文密码
     */
    public static String decrypt(String cipherText) {
        try {
            if (StrUtil.isBlank(cipherText)) {
                throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_PASSWORD_EMPTY);
            }
            // 获取 AES 解密器实例
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 用固定密钥生成 AES key 对象
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            // 初始化为解密模式
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            // 先把 Base64 字符串解码为原始密文字节
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            // 再把密文字节解密成原始明文密码
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            // 最后把字节数组转回字符串
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("服务器密码解密失败", e);
        }
    }

}

package com.zyh.easyapplyresume.service.impl.admin;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.CodeEnum;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

/**
 * 阿里云短信服务
 * 基于 Spring Boot 实现，配置从 application.yml 注入，支持异步发送
 */
@Slf4j
@Service
public class SmsService {

    // ==================== 配置注入 ====================
    @Value("${ali.sms.access-key-id}")
    private String accessKeyId;

    @Value("${ali.sms.access-key-secret}")
    private String accessKeySecret;

    @Value("${ali.sms.sign-name}")
    private String signName;

    @Value("${ali.sms.template-code}")
    private String templateCode;

    @Value("${ali.sms.region-id}")
    private String regionId;

    @Value("${ali.sms.endpoint}")
    private String endpoint;

    @Value("${ali.sms.length}")
    private int codeLength;

    @Value("${ali.sms.valid-time}")
    private long validTime;

    // ==================== 内部成员 ====================
    private AsyncClient asyncClient;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String CODE_CHARACTERS = "0123456789";

    /**
     * 初始化客户端
     */
    @PostConstruct
    public void initClient() {
        log.info("开始初始化阿里云短信客户端...");
        try {
            if (accessKeyId == null || accessKeySecret == null || signName == null || templateCode == null) {
                log.error("阿里云短信核心配置缺失！");
                throw new BusException(CodeEnum.SMS_CONFIG_ERROR);
            }

            StaticCredentialProvider credentialProvider = StaticCredentialProvider.create(
                    Credential.builder()
                            .accessKeyId(accessKeyId)
                            .accessKeySecret(accessKeySecret)
                            .build()
            );

            this.asyncClient = AsyncClient.builder()
                    .region(regionId)
                    .credentialsProvider(credentialProvider)
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create().setEndpointOverride(endpoint)
                    )
                    .build();
            log.info("阿里云短信客户端初始化完成");
        } catch (BusException e) {
            throw e; // 直接向上抛出已知的业务异常
        } catch (Exception e) {
            log.error("初始化阿里云短信客户端时发生未知异常: ", e);
            // 将其他异常包装成系统错误
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号
     * @return CompletableFuture<Void> 只关心成功或失败，不关心返回内容
     */
    public CompletableFuture<Void> sendVerificationCode(String phoneNumber) {
        log.info("准备向手机号 [{}] 发送短信验证码", phoneNumber);

        // 1. 参数校验 (同步抛出异常)
        validatePhoneNumber(phoneNumber);

        // 2. 生成验证码
        String verificationCode = generateCode();
        log.info("为手机号 [{}] 生成短信验证码：{}", phoneNumber, verificationCode);

        // 3. 构建请求参数
        String templateParam = String.format("{\"code\":\"%s\",\"min\":\"%d\"}", verificationCode, validTime / 60);
        SendSmsVerifyCodeRequest sendRequest = SendSmsVerifyCodeRequest.builder()
                .signName(signName)
                .templateCode(templateCode)
                .phoneNumber(phoneNumber)
                .templateParam(templateParam)
                .codeLength((long) codeLength)
                .validTime(validTime)
                .build();

        // 4. 异步发送短信
        CompletableFuture<SendSmsVerifyCodeResponse> responseFuture = asyncClient.sendSmsVerifyCode(sendRequest);

        // 5. 监听异步结果，并在失败时抛出异常
        return responseFuture.thenAccept(response -> {
            // 业务逻辑判断：如果阿里云返回的状态不是OK，也视为失败
            if (response.getBody() == null || !"OK".equals(response.getBody().getCode())) {
                String errorMsg = response.getBody() != null ? response.getBody().getMessage() : "未知错误";
                log.error("手机号 [{}] 短信发送失败（阿里云返回失败）: {}", phoneNumber, errorMsg);
                // 这里不能直接 throw new BusException()，因为 thenAccept 不允许抛出 checked exception
                // 但我们可以抛出一个 unchecked exception，或者使用 handle/exceptionally
                // 为了统一，我们在下面的 exceptionally 中处理
                throw new RuntimeException(new BusException(CodeEnum.SMS_SEND_FAILED.getCode(), errorMsg));
            }
            log.info("手机号 [{}] 短信发送成功", phoneNumber);
        }).exceptionally(throwable -> {
            // 捕获所有异常（包括网络异常、上面抛出的RuntimeException包装的BusException等）
            log.error("手机号 [{}] 短信发送失败: {}", phoneNumber, throwable.getMessage());

            // 解包异常，如果是我们自己抛出的BusException，则直接抛出
            Throwable cause = throwable.getCause();
            if (cause instanceof BusException) {
                throw (BusException) cause;
            }

            // 其他未知异常，统一包装成系统错误
            throw new BusException(CodeEnum.SYSTEM_ERROR);
        });
    }

    /**
     * 手机号格式校验
     */
    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().length() != 11 || !phoneNumber.matches("\\d+")) {
            log.warn("手机号 [{}] 格式不正确", phoneNumber);
            throw new BusException(CodeEnum.SMS_PHONE_FORMAT_ERROR);
        }
    }

    /**
     * 生成验证码
     */
    private String generateCode() {
        if (codeLength <= 0 || codeLength > 10) {
            log.warn("短信验证码长度配置异常（{}），使用默认长度6", codeLength);
            codeLength = 6;
        }
        StringBuilder codeBuilder = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            codeBuilder.append(CODE_CHARACTERS.charAt(SECURE_RANDOM.nextInt(CODE_CHARACTERS.length())));
        }
        return codeBuilder.toString();
    }

    /**
     * 销毁客户端
     */
    @PreDestroy
    public void destroyClient() {
        if (asyncClient != null) {
            log.info("开始关闭阿里云短信客户端...");
            asyncClient.close();
            log.info("阿里云短信客户端已关闭");
        }
    }
}
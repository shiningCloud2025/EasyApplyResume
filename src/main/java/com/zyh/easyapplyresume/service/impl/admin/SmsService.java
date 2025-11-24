package com.zyh.easyapplyresume.service.impl.admin;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
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
 * @author shiningCloud2025
 */
@Slf4j
@Service
public class SmsService {

    // ==================== 配置注入（与 application.yml 对应）====================
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
    // 阿里云短信异步客户端（全局唯一，避免重复创建连接）
    private AsyncClient asyncClient;
    // 安全随机数生成器（用于生成验证码，比 Math.random() 更安全）
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    // 短信验证码字符池（纯数字，用户易识别）
    private static final String CODE_CHARACTERS = "0123456789";

    /**
     * 初始化：创建阿里云短信客户端（应用启动时执行）
     * @PostConstruct 确保配置注入完成后再初始化客户端
     */
    @PostConstruct
    public void initClient() {
        log.info("开始初始化阿里云短信客户端...");

        // 校验核心配置（避免配置缺失导致客户端创建失败）
        if (accessKeyId == null || accessKeySecret == null || signName == null || templateCode == null) {
            log.error("阿里云短信核心配置缺失！请检查 application.yml 中 ali.sms 相关配置");
            throw new IllegalArgumentException("阿里云短信配置不能为空");
        }

        // 1. 配置凭证（AccessKeyId + AccessKeySecret）
        StaticCredentialProvider credentialProvider = StaticCredentialProvider.create(
                Credential.builder()
                        .accessKeyId(accessKeyId)
                        .accessKeySecret(accessKeySecret)
                        .build()
        );

        // 2. 构建短信客户端
        this.asyncClient = AsyncClient.builder()
                .region(regionId) // 指定服务区域（必须与配置一致）
                .credentialsProvider(credentialProvider) // 注入凭证
                .overrideConfiguration( // 覆盖客户端配置（指定 Endpoint）
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(endpoint)
                )
                .build();

        log.info("阿里云短信客户端初始化完成");
    }

    /**
     * 发送短信验证码
     * @param phoneNumber 接收手机号（格式：13800138000，无需加国家码）
     * @return 异步响应结果（包含发送状态、验证码等信息）
     */
    public CompletableFuture<SendSmsVerifyCodeResponse> sendVerificationCode(String phoneNumber) {
        log.info("准备向手机号 [{}] 发送短信验证码", phoneNumber);

        // 1. 生成随机验证码（纯数字，长度从配置读取）
        String verificationCode = generateCode();
        log.info("生成短信验证码：{}（有效期：{}分钟）", verificationCode, validTime / 60);

        // 2. 构建模板参数（需与短信模板中的变量对应，示例模板："您的验证码是${code}，${min}分钟内有效"）
        String templateParam = String.format("{\"code\":\"%s\",\"min\":\"%d\"}", verificationCode, validTime / 60);

        // 3. 构建短信发送请求
        SendSmsVerifyCodeRequest sendRequest = SendSmsVerifyCodeRequest.builder()
                .signName(signName)         // 短信签名（需阿里云审核通过）
                .templateCode(templateCode) // 短信模板Code（需阿里云审核通过）
                .phoneNumber(phoneNumber)   // 接收手机号
                .templateParam(templateParam) // 模板参数（JSON格式）
                .codeLength((long) codeLength) // 验证码长度（与配置一致）
                .validTime(validTime)       // 验证码有效期（秒，与配置一致）
                .build();

        // 4. 异步发送短信并返回结果
        CompletableFuture<SendSmsVerifyCodeResponse> responseFuture = asyncClient.sendSmsVerifyCode(sendRequest);

        // 5. 异步处理发送结果（日志记录，不阻塞主线程）
        responseFuture.whenComplete((response, throwable) -> {
            if (throwable != null) {
                log.error("手机号 [{}] 短信发送失败：{}", phoneNumber, throwable.getMessage());
            } else {
                if (response.getBody() != null && "OK".equals(response.getBody().getCode())) {
                    log.info("手机号 [{}] 短信发送成功，请求ID：{}", phoneNumber, response.getBody().getRequestId());
                } else {
                    String errorMsg = response.getBody() != null ? response.getBody().getMessage() : "发送结果未知";
                    log.error("手机号 [{}] 短信发送失败：{}", phoneNumber, errorMsg);
                }
            }
        });

        return responseFuture;
    }

    /**
     * 内部方法：生成指定长度的纯数字验证码
     * @return 验证码字符串
     */
    private String generateCode() {
        // 校验验证码长度配置（避免无效长度）
        if (codeLength <= 0 || codeLength > 10) {
            log.warn("短信验证码长度配置异常（{}），使用默认长度6", codeLength);
            codeLength = 6;
        }

        StringBuilder codeBuilder = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            // 从字符池随机选取一个数字
            int randomIndex = SECURE_RANDOM.nextInt(CODE_CHARACTERS.length());
            codeBuilder.append(CODE_CHARACTERS.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    /**
     * 销毁：关闭短信客户端（应用关闭时执行）
     * @PreDestroy 确保应用退出前释放资源
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
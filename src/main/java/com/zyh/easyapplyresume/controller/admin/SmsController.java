package com.zyh.easyapplyresume.controller.admin;

import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.zyh.easyapplyresume.service.impl.admin.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * 手机短信验证控制器-管理端
 * 提供发送短信验证码的API接口
 * @author shiningCloud2025
 */
@Slf4j
@RestController
@RequestMapping("/admin/sms") // 统一的API前缀
@RequiredArgsConstructor // Lombok注解，自动注入构造函数（替代@Autowired）
public class SmsController {

    // 注入短信服务
    private final SmsService smsService;

    /**
     * 发送短信验证码
     *
     * @param phone 接收短信的手机号，通过URL路径参数传递
     * @return 异步响应（泛型明确为String类型的ResponseEntity）
     */
    @GetMapping("/send/{phone}")
    public CompletableFuture<ResponseEntity<String>> sendVerificationCode(@PathVariable String phone) {
        log.info("收到发送短信验证码的请求，手机号：{}", phone);

        // 1. 简单的手机号格式校验
        if (phone == null || phone.trim().length() != 11) {
            log.warn("手机号格式不正确：{}", phone);
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body("手机号格式不正确，请输入11位有效手机号")
            );
        }

        // 2. 调用SmsService发送短信
        CompletableFuture<SendSmsVerifyCodeResponse> sendSmsFuture = smsService.sendVerificationCode(phone);

        // 3. 处理异步结果，转换为String类型的ResponseEntity
        return sendSmsFuture.thenApply(response -> {
            if (response.getBody() != null && "OK".equals(response.getBody().getCode())) {
                log.info("手机号 [{}] 短信发送成功", phone);
                return ResponseEntity.ok().body("短信验证码已成功发送至 " + phone + "，请注意查收");
            } else {
                String errorMessage = response.getBody() != null ? response.getBody().getMessage() : "短信发送失败，原因未知";
                log.error("手机号 [{}] 短信发送失败：{}", phone, errorMessage);
                return ResponseEntity.badRequest().body("短信发送失败：" + errorMessage);
            }
        }).exceptionally(throwable -> {
            log.error("发送短信时发生未知异常，手机号：{}", phone, throwable);
            return ResponseEntity.internalServerError().body("系统异常，短信发送失败，请稍后重试");
        });
    }
}
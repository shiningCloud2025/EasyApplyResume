package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.impl.admin.SmsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * 手机短信验证控制器-管理端
 * Controller层只负责调用服务和返回成功，不处理任何异常
 */
@Slf4j
@RestController
@RequestMapping("/admin/sms")
@RequiredArgsConstructor
@Tag(name = "短信验证码接口-管理端")
public class SmsController {

    private final SmsService smsService;

    /**
     * 发送短信验证码接口
     * @param phone 收件人手机号
     * @return BaseResult
     */
    @GetMapping("/send/{phone}")
    public CompletableFuture<BaseResult<?>> sendVerifyCode(@PathVariable String phone) {
        log.info("收到发送短信验证码的请求，手机号：{}", phone);

        // 调用Service方法。
        // 如果方法执行过程中抛出异常，会被全局异常处理器捕获。
        // 如果没有异常，则继续执行 thenApply，返回成功。
        return smsService.sendVerificationCode(phone)
                .thenApply(v -> {
                    // v 是 Void 类型，我们不关心它的值
                    log.info("手机号 [{}] 短信发送流程已成功触发", phone);
                    return BaseResult.ok();
                });
    }
}
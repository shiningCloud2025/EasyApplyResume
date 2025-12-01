package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.impl.user.UserSmsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手机短信验证控制器-用户端
 * Controller层只负责调用服务和返回成功，不处理任何异常
 *  @author shiningCloud2025
 */
@Slf4j
@RestController
@RequestMapping("/user/sms")
@RequiredArgsConstructor
@Tag(name = "短信验证码接口-用户端")
public class SmsController {

    private final UserSmsServiceImpl smsService;

    /**
     * 发送短信验证码接口（模仿邮箱：POST 请求）
     * @param phone 收件人手机号
     * @return BaseResult
     */
    @PostMapping("/send")
    public BaseResult<?> sendVerifyCode(@RequestParam(required = true, value = "phone") String phone) {
        log.info("收到发送短信验证码请求，手机号：{}", phone);
        // 调用同步服务方法（异常由全局处理器捕获）
        smsService.sendVerifyCode(phone);
        return BaseResult.ok();
    }

    /**
     * 校验短信验证码接口（模仿邮箱：新增 check 接口）
     * @param phone 收件人手机号
     * @param code 用户输入的验证码
     * @return BaseResult
     */
    @PostMapping("/check")
    public BaseResult<?> checkVerifyCode(
            @RequestParam(required = true, value = "phone") String phone,
            @RequestParam(required = true, value = "code") String code) {
        log.info("收到短信验证码校验请求，手机号：{}，输入验证码：{}", phone, code);
        // 调用服务层校验方法（异常由全局处理器捕获）
        smsService.verifyCode(phone, code);
        return BaseResult.ok();
    }
}
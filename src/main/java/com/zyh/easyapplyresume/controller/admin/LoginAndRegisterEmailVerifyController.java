package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.admin.LoginAndRegisterEmailVerifyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录注册邮箱验证码控制器-管理端
 * Controller层只负责调用服务和返回成功，不处理任何异常
 */
@RestController
@RequestMapping("/admin/email/loginandregister")
@Tag(name = "登录注册邮箱验证码接口-管理端")
public class LoginAndRegisterEmailVerifyController {

    @Autowired
    private LoginAndRegisterEmailVerifyService emailVerifyService;

    /**
     * 发送邮箱验证码接口
     * @param email 收件人邮箱
     * @return BaseResult
     */
    @PostMapping("/send")
    public BaseResult<?> sendVerifyCode(@RequestParam(required = true,value = "email") String email) {
        // 调用Service方法。如果方法执行完毕没有抛出异常，说明发送成功。
        // 如果抛出异常，会被全局异常处理器捕获，不会执行到下面的 return 语句。
        emailVerifyService.sendVerifyCode(email);

        // 只有成功时才会执行到这里
        return BaseResult.ok();
    }

    /**
     * 验证邮箱验证码接口
     * @param email 收件人邮箱
     * @param code 用户输入的验证码
     * @return BaseResult
     */
    @PostMapping("/check")
    public BaseResult<?> checkVerifyCode(@RequestParam(required = true,value = "email") String email, @RequestParam(required = true,value = "code") String code) {
        // 调用Service方法。如果方法执行完毕没有抛出异常，说明验证成功。
        emailVerifyService.verifyCode(email, code);

        // 只有成功时才会执行到这里
        return BaseResult.ok();
    }
}
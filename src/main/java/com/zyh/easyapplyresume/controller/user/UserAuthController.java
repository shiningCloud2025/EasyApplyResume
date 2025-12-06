package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.user.EmailLoginForm;
import com.zyh.easyapplyresume.model.form.user.FormalLoginForm;
import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;
import com.zyh.easyapplyresume.model.form.user.PhoneLoginForm;
import com.zyh.easyapplyresume.security.SecurityUser;
import com.zyh.easyapplyresume.service.user.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 用户认证控制器-用户端
 *  @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/auth")
@Tag(name = "用户认证接口-用户端")
public class UserAuthController {
    @Autowired
    private UserAuthService userAuthService;


    @Operation(summary = "普通登录")
    @PostMapping("/formalLogin")
    public BaseResult<String> formalLogin(@RequestBody FormalLoginForm formalLoginForm) {
        return BaseResult.ok(userAuthService.formalLogin(formalLoginForm));
    }

    @Operation(summary = "手机登录")
    @PostMapping("/phoneLogin")
    public BaseResult<String> phoneLogin(@RequestBody PhoneLoginForm phoneLoginForm) {
        return BaseResult.ok(userAuthService.phoneLogin(phoneLoginForm));
    }

    @Operation(summary = "邮箱登录")
    @PostMapping("/emailLogin")
    public BaseResult<String> emailLogin(@RequestBody EmailLoginForm emailLoginForm) {
        return BaseResult.ok(userAuthService.emailLogin(emailLoginForm));
    }

    @Operation(summary = "普通注册")
    @PostMapping("/formalRegister")
    public BaseResult<String> formalRegister(@RequestBody FormalRegisterForm formalRegisterForm) {
        return BaseResult.ok(userAuthService.formalRegister(formalRegisterForm));
    }

    @Operation(summary = "生成随机账号")
    @PostMapping("/generateRandomAccount")
    public BaseResult<String> generateRandomAccount() {
        return BaseResult.ok(userAuthService.generateRandomAccount());
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public BaseResult<?> logout(@RequestBody Integer userId) {
        userAuthService.logout(userId);
        return BaseResult.ok();
    }

    @Operation(summary = "获取用户信息")
    @PostMapping("/getUserInfo")
    public BaseResult<SecurityUser> getUserInfo(@AuthenticationPrincipal SecurityUser securityUser){
        return BaseResult.ok(securityUser);
    }

}

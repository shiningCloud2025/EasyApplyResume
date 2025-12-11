package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminFormalLoginForm;
import com.zyh.easyapplyresume.model.form.admin.AdminPhoneLoginForm;
import com.zyh.easyapplyresume.model.form.user.EmailLoginForm;
import com.zyh.easyapplyresume.security.SecurityUser;
import com.zyh.easyapplyresume.service.admin.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 管理员登录接口-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/auth")
@Tag(name = "管理员登录接口-管理端")
public class AdminAuthController {
    @Autowired
    private AdminAuthService adminAuthService;

    @PostMapping("/formalLogin")
    public BaseResult<String> formalLogin(@RequestBody AdminFormalLoginForm formalLoginForm) {
        return BaseResult.ok(adminAuthService.formalLogin(formalLoginForm));
    }

    @PostMapping("/phoneLogin")
    public BaseResult<String> phoneLogin(@RequestBody AdminPhoneLoginForm phoneLoginForm) {
        return BaseResult.ok(adminAuthService.phoneLogin(phoneLoginForm));
    }

    @PostMapping("/emailLogin")
    public BaseResult<String> emailLogin(@RequestBody EmailLoginForm emailLoginForm) {
        return BaseResult.ok(adminAuthService.emailLogin(emailLoginForm));
    }

    @Operation(summary = "获取管理员信息")
    @PostMapping("/getAdminInfo")
    public BaseResult<SecurityUser> getAdminInfo(@AuthenticationPrincipal SecurityUser securityUser){
        return BaseResult.ok(securityUser);
    }
}

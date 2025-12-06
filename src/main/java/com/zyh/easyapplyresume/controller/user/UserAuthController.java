package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.service.user.UserAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    public String formalLogin() {
    }


}

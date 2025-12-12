package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.user.UserUpdateForm;
import com.zyh.easyapplyresume.model.vo.user.UserInfoVO;
import com.zyh.easyapplyresume.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/user")
@Tag(name="用户管理控制器-用户端")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/updateUser")
    @Operation(summary = "用户更新信息")
    public BaseResult<?> updateUser(@RequestBody UserUpdateForm userUpdateForm) {
        userService.updateUser(userUpdateForm);
        return BaseResult.ok();
    }

    @GetMapping("/getUserByUserId")
    @Operation(summary = "根据用户id查询用户信息")
    public BaseResult<UserInfoVO> getUserByUserId(@RequestParam(required = true,value = "userId") String userId) {
        return BaseResult.ok(userService.getUserByUserId(userId));
    }

}

package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.user.UserSendResumeToHrForm;
import com.zyh.easyapplyresume.service.user.UserSendResumeToHrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 用户发送简历给HR控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/sendResumeToHr")
@Tag(name = "用户发送简历给HR")
public class UserSendResumeToHrController {

    @Autowired
    private UserSendResumeToHrService userSendResumeToHrService;

    @PostMapping("/sendResumeToHr")
    @Operation(summary = "用户发送简历给HR")
    public BaseResult<?> sendResumeToHr(@RequestBody UserSendResumeToHrForm userSendResumeToHrForm) {
        userSendResumeToHrService.sendResumeToHr(userSendResumeToHrForm);
        return BaseResult.ok();
    }

}

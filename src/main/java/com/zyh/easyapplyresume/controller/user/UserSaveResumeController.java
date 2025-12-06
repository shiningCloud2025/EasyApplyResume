package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.service.user.UserSaveResumeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/saveResume")
@Tag(name="用户保存简历控制器-用户端")
public class UserSaveResumeController {

    @Autowired
    private UserSaveResumeService userSaveResumeService;



}

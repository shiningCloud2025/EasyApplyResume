package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.mapper.mysql.user.UserDeleteResumeBySystemMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeBySystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统回收用户删除的简历控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/userDeleteResumeBySystemService")
@Tag(name = "系统回收用户删除的简历控制器-用户端")
public class UserDeleteResumeBySystemServiceController {
    @Autowired
    private UserDeleteResumeBySystemService userDeleteResumeBySystemService;

    @PostMapping("/addExpiredUserDeleteResume")
    @Operation(summary = "添加系统回收用户删除的简历")
    public BaseResult<?> addExpiredUserDeleteResume(@RequestBody List<UserDeleteResume> userDeleteResumes) {
        userDeleteResumeBySystemService.addExpiredUserDeleteResume(userDeleteResumes);
        return BaseResult.ok();
    }

    @PostMapping("/clearExpiredUserDeleteResumeEveryThreeMonth")
    @Operation(summary = "主动清理系统回收用户删除的简历")
    public BaseResult<?> clearExpiredUserDeleteResumeEveryThreeMonth() {
        userDeleteResumeBySystemService.clearExpiredUserDeleteResumeEveryThreeMonth();
        return BaseResult.ok();
    }

}

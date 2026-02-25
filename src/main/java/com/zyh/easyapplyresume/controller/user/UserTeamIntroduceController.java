package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminTeamIntroduce;
import com.zyh.easyapplyresume.service.admin.AdminTeamIntroduceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 团队介绍控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/teamIntroduce")
@Tag(name = "团队介绍控制器-用户端")
public class UserTeamIntroduceController {
    @Autowired
    private AdminTeamIntroduceService teamIntroduceService;

    @Operation(summary = "获取团队介绍信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminTeamIntroduce> getTeamIntroduceInfo() {
        return BaseResult.ok(teamIntroduceService.getTeamIntroduceInfo());
    }
}

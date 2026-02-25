package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminTeamIntroduce;
import com.zyh.easyapplyresume.service.admin.AdminTeamIntroduceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 团队介绍控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/teamIntroduce")
@Tag(name = "团队介绍控制器-管理端")
public class AdminTeamIntroduceController {
    @Autowired
    private AdminTeamIntroduceService teamIntroduceService;

    @Operation(summary = "添加团队介绍")
    @RequestMapping("/add")
    public BaseResult<Integer> addTeamIntroduce(@RequestBody AdminTeamIntroduce teamIntroduce) {
        return BaseResult.ok(teamIntroduceService.addTeamIntroduce(teamIntroduce));
    }

    @Operation(summary = "修改团队介绍")
    @RequestMapping("/update")
    public BaseResult<Integer> updateTeamIntroduce(@RequestBody AdminTeamIntroduce teamIntroduce) {
        return BaseResult.ok(teamIntroduceService.updateTeamIntroduce(teamIntroduce));
    }

    @Operation(summary = "获取团队介绍信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminTeamIntroduce> getTeamIntroduceInfo() {
        return BaseResult.ok(teamIntroduceService.getTeamIntroduceInfo());
    }
}

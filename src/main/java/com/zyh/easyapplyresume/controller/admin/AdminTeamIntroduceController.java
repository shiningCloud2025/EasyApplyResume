package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminTeamIntroduceForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminTeamIntroduceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminTeamIntroduceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('/admin/teamIntroduce/add')")
    public BaseResult<Integer> addTeamIntroduce(@RequestBody AdminTeamIntroduceForm teamIntroduceForm) {
        return BaseResult.ok(teamIntroduceService.addTeamIntroduce(teamIntroduceForm));
    }

    @Operation(summary = "修改团队介绍")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/admin/teamIntroduce/update')")
    public BaseResult<Integer> updateTeamIntroduce(@RequestBody AdminTeamIntroduceForm teamIntroduceForm) {
        return BaseResult.ok(teamIntroduceService.updateTeamIntroduce(teamIntroduceForm));
    }

    @Operation(summary = "获取团队介绍信息")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admin/teamIntroduce/getInfo')")
    public BaseResult<AdminTeamIntroduceInfoVO> getTeamIntroduceInfo() {
        return BaseResult.ok(teamIntroduceService.getTeamIntroduceInfo());
    }
}

package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminProjectIntroduceForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminProjectIntroduceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminProjectIntroduceService;
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
 * 项目介绍控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/projectIntroduce")
@Tag(name = "项目介绍控制器-管理端")
public class AdminProjectIntroduceController {
    @Autowired
    private AdminProjectIntroduceService projectIntroduceService;

    @Operation(summary = "添加项目介绍")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('/admin/projectIntroduce/add')")
    public BaseResult<Integer> addProjectIntroduce(@RequestBody AdminProjectIntroduceForm projectIntroduceForm) {
        return BaseResult.ok(projectIntroduceService.addProjectIntroduce(projectIntroduceForm));
    }

    @Operation(summary = "修改项目介绍")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/admin/projectIntroduce/update')")
    public BaseResult<Integer> updateProjectIntroduce(@RequestBody AdminProjectIntroduceForm projectIntroduceForm) {
        return BaseResult.ok(projectIntroduceService.updateProjectIntroduce(projectIntroduceForm));
    }

    @Operation(summary = "获取项目介绍信息")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admin/projectIntroduce/getInfo')")
    public BaseResult<AdminProjectIntroduceInfoVO> getProjectIntroduceInfo() {
        return BaseResult.ok(projectIntroduceService.getProjectIntroduceInfo());
    }
}

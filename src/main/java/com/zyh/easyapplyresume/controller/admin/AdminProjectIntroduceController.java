package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminProjectIntroduce;
import com.zyh.easyapplyresume.service.admin.AdminProjectIntroduceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping("/add")
    public BaseResult<Integer> addProjectIntroduce(@RequestBody AdminProjectIntroduce projectIntroduce) {
        return BaseResult.ok(projectIntroduceService.addProjectIntroduce(projectIntroduce));
    }

    @Operation(summary = "修改项目介绍")
    @RequestMapping("/update")
    public BaseResult<Integer> updateProjectIntroduce(@RequestBody AdminProjectIntroduce projectIntroduce) {
        return BaseResult.ok(projectIntroduceService.updateProjectIntroduce(projectIntroduce));
    }

    @Operation(summary = "获取项目介绍信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminProjectIntroduce> getProjectIntroduceInfo() {
        return BaseResult.ok(projectIntroduceService.getProjectIntroduceInfo());
    }
}

package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminJoinUs;
import com.zyh.easyapplyresume.service.admin.AdminJoinUsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 加入我们控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/joinUs")
@Tag(name = "加入我们控制器-管理端")
public class AdminJoinUsController {
    @Autowired
    private AdminJoinUsService joinUsService;

    @Operation(summary = "添加加入我们")
    @RequestMapping("/add")
    public BaseResult<Integer> addJoinUs(@RequestBody AdminJoinUs joinUs) {
        return BaseResult.ok(joinUsService.addJoinUs(joinUs));
    }

    @Operation(summary = "修改加入我们")
    @RequestMapping("/update")
    public BaseResult<Integer> updateJoinUs(@RequestBody AdminJoinUs joinUs) {
        return BaseResult.ok(joinUsService.updateJoinUs(joinUs));
    }

    @Operation(summary = "获取加入我们信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminJoinUs> getJoinUsInfo() {
        return BaseResult.ok(joinUsService.getJoinUsInfo());
    }
}

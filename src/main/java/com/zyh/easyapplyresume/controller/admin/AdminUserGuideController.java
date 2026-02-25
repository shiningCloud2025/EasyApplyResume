package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminUserGuide;
import com.zyh.easyapplyresume.service.admin.AdminUserGuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 使用指南控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/userGuide")
@Tag(name = "使用指南控制器-管理端")
public class AdminUserGuideController {
    @Autowired
    private AdminUserGuideService userGuideService;

    @Operation(summary = "添加使用指南")
    @PostMapping("/add")
    public BaseResult<Integer> addUserGuide(@RequestBody AdminUserGuide userGuide) {
        return BaseResult.ok(userGuideService.addUserGuide(userGuide));
    }

    @Operation(summary = "修改使用指南")
    @PostMapping("/update")
    public BaseResult<Integer> updateUserGuide(@RequestBody AdminUserGuide userGuide) {
        return BaseResult.ok(userGuideService.updateUserGuide(userGuide));
    }

    @Operation(summary = "删除使用指南")
    @DeleteMapping("/delete")
    public BaseResult<Integer> deleteUserGuide(@RequestParam(required = true, value = "userGuideId") Integer userGuideId) {
        return BaseResult.ok(userGuideService.deleteUserGuide(userGuideId));
    }

    @Operation(summary = "获取使用指南信息")
    @GetMapping("/getInfo")
    public BaseResult<AdminUserGuide> getUserGuideInfo(@RequestParam(required = true, value = "userGuideId") Integer userGuideId) {
        return BaseResult.ok(userGuideService.getUserGuideInfo(userGuideId));
    }

    @Operation(summary = "分页查询使用指南")
    @GetMapping("/getPage")
    public BaseResult<Page<AdminUserGuide>> getUserGuidePage(@RequestParam(required = false, value = "size", defaultValue = "10") int size,
                                                      @RequestParam(required = false, value = "page", defaultValue = "1") int page) {
        return BaseResult.ok(userGuideService.getUserGuidePage(size, page));
    }
}

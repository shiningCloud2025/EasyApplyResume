package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminUserGuideForm;
import com.zyh.easyapplyresume.model.query.admin.AdminUserGuideQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminUserGuideInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminUserGuidePageVO;
import com.zyh.easyapplyresume.service.admin.AdminUserGuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('/admin/userGuide/add')")
    public BaseResult<Integer> addUserGuide(@RequestBody AdminUserGuideForm userGuideForm) {
        return BaseResult.ok(userGuideService.addUserGuide(userGuideForm));
    }

    @Operation(summary = "修改使用指南")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/admin/userGuide/update')")
    public BaseResult<Integer> updateUserGuide(@RequestBody AdminUserGuideForm userGuideForm) {
        return BaseResult.ok(userGuideService.updateUserGuide(userGuideForm));
    }

    @Operation(summary = "删除使用指南")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('/admin/userGuide/delete')")
    public BaseResult<Integer> deleteUserGuide(@RequestParam(required = true, value = "userGuideId") Integer userGuideId) {
        return BaseResult.ok(userGuideService.deleteUserGuide(userGuideId));
    }

    @Operation(summary = "获取使用指南信息")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admin/userGuide/getInfo')")
    public BaseResult<AdminUserGuideInfoVO> getUserGuideInfo(@RequestParam(required = true, value = "userGuideId") Integer userGuideId) {
        return BaseResult.ok(userGuideService.getUserGuideInfo(userGuideId));
    }

    @Operation(summary = "分页查询使用指南")
    @PostMapping("/getPage")
    @PreAuthorize("hasAuthority('/admin/userGuide/getPage')")
    public BaseResult<Page<AdminUserGuidePageVO>> getUserGuidePage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") int pageSize,
            @RequestBody(required = false) AdminUserGuideQuery userGuideQuery) {
        return BaseResult.ok(userGuideService.getUserGuidePage(pageSize, pageNum, userGuideQuery));
    }
}

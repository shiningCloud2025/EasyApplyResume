package com.zyh.easyapplyresume.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.admin.AdminUserGuideQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminUserGuideInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminUserGuidePageVO;
import com.zyh.easyapplyresume.service.admin.AdminUserGuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 使用指南控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/userGuide")
@Tag(name = "使用指南控制器-用户端")
public class UserUserGuideController {
    @Autowired
    private AdminUserGuideService userGuideService;

    @Operation(summary = "获取使用指南信息")
    @GetMapping("/getInfo")
    public BaseResult<AdminUserGuideInfoVO> getUserGuideInfo(@RequestParam(required = true, value = "userGuideId") Integer userGuideId) {
        return BaseResult.ok(userGuideService.getUserGuideInfo(userGuideId));
    }

    @Operation(summary = "分页查询使用指南")
    @PostMapping("/getPage")
    public BaseResult<Page<AdminUserGuidePageVO>> getUserGuidePage(
            @RequestParam(required = false, value = "size", defaultValue = "10") int size,
            @RequestParam(required = false, value = "page", defaultValue = "1") int page,
            @RequestBody AdminUserGuideQuery query) {
        return BaseResult.ok(userGuideService.getUserGuidePage(size, page, query));
    }
}

package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.admin.UniversityMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.UniversityMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.UniversityMapPageVO;
import com.zyh.easyapplyresume.service.admin.UniversityMapAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 大学Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/universityMap")
@Tag(name = "大学Map控制器-管理端")
public class AdminUniversityMapController {
    @Autowired
    private UniversityMapAdminService universityMapAdminService;

    @Operation(summary = "查询大学Map详情")
    @GetMapping("/findUniversityMapById")
    @PreAuthorize("hasAuthority('/admin/universityMap/findUniversityMapById')")
    public BaseResult<UniversityMapInfoVO> findUniversityMapById(
            @RequestParam(required = true, value = "universityMapId") Integer universityMapId) {
        return BaseResult.ok(universityMapAdminService.findUniversityMapById(universityMapId));
    }

    @Operation(summary = "分页查询大学Map")
    @PostMapping("/findUniversityMapByPage")
    @PreAuthorize("hasAuthority('/admin/universityMap/findUniversityMapByPage')")
    public BaseResult<Page<UniversityMapPageVO>> findUniversityMapByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody UniversityMapQuery universityMapQuery) {
        return BaseResult.ok(universityMapAdminService.findUniversityMapByPage(pageNum, pageSize, universityMapQuery));
    }
}

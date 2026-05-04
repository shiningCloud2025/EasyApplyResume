package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminLlmUtilsInfoForm;
import com.zyh.easyapplyresume.model.query.admin.AdminLlmUtilsInfoQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminLlmUtilsInfoPageVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminLlmUtilsInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminLlmUtilsInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * LLM工具类调用日志控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/llmUtilsInfo")
@Tag(name = "LLM工具类调用日志控制器-管理端")
public class AdminLlmUtilsInfoController {

    @Autowired
    private AdminLlmUtilsInfoService adminLlmUtilsInfoService;

    @Operation(summary = "获取LLM工具类调用日志详情")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admin/llmUtilsInfo/getInfo')")
    public BaseResult<AdminLlmUtilsInfoVO> findAdminLlmUtilsInfoById(@RequestParam(required = true, value = "llmUtilsInfoId") Long llmUtilsInfoId) {
        return BaseResult.ok(adminLlmUtilsInfoService.findAdminLlmUtilsInfoById(llmUtilsInfoId));
    }

    @Operation(summary = "分页查询LLM工具类调用日志")
    @PostMapping("/getPage")
    @PreAuthorize("hasAuthority('/admin/llmUtilsInfo/getPage')")
    public BaseResult<Page<AdminLlmUtilsInfoPageVO>> findAdminLlmUtilsInfoByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody(required = false) AdminLlmUtilsInfoQuery adminLlmUtilsInfoQuery) {
        return BaseResult.ok(adminLlmUtilsInfoService.findAdminLlmUtilsInfoByPage(pageNum, pageSize, adminLlmUtilsInfoQuery));
    }


}

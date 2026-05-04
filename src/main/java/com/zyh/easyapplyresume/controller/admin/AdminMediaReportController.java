package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminMediaReportForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminMediaReportInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminMediaReportService;
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
 * 媒体报道控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/mediaReport")
@Tag(name = "媒体报道控制器-管理端")
public class AdminMediaReportController {
    @Autowired
    private AdminMediaReportService mediaReportService;

    @Operation(summary = "添加媒体报道")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('/admin/mediaReport/add')")
    public BaseResult<Integer> addMediaReport(@RequestBody AdminMediaReportForm mediaReportForm) {
        return BaseResult.ok(mediaReportService.addMediaReport(mediaReportForm));
    }

    @Operation(summary = "修改媒体报道")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/admin/mediaReport/update')")
    public BaseResult<Integer> updateMediaReport(@RequestBody AdminMediaReportForm mediaReportForm) {
        return BaseResult.ok(mediaReportService.updateMediaReport(mediaReportForm));
    }

    @Operation(summary = "获取媒体报道信息")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admin/mediaReport/getInfo')")
    public BaseResult<AdminMediaReportInfoVO> getMediaReportInfo() {
        return BaseResult.ok(mediaReportService.getMediaReportInfo());
    }
}

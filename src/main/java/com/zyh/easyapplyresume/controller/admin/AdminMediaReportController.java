package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminMediaReport;
import com.zyh.easyapplyresume.service.admin.AdminMediaReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping("/add")
    public BaseResult<Integer> addMediaReport(@RequestBody AdminMediaReport mediaReport) {
        return BaseResult.ok(mediaReportService.addMediaReport(mediaReport));
    }

    @Operation(summary = "修改媒体报道")
    @RequestMapping("/update")
    public BaseResult<Integer> updateMediaReport(@RequestBody AdminMediaReport mediaReport) {
        return BaseResult.ok(mediaReportService.updateMediaReport(mediaReport));
    }

    @Operation(summary = "获取媒体报道信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminMediaReport> getMediaReportInfo() {
        return BaseResult.ok(mediaReportService.getMediaReportInfo());
    }
}

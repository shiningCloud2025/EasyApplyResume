package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminMediaReport;
import com.zyh.easyapplyresume.service.admin.AdminMediaReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 媒体报道控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/mediaReport")
@Tag(name = "媒体报道控制器-用户端")
public class UserMediaReportController {
    @Autowired
    private AdminMediaReportService mediaReportService;

    @Operation(summary = "获取媒体报道信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminMediaReport> getMediaReportInfo() {
        return BaseResult.ok(mediaReportService.getMediaReportInfo());
    }
}

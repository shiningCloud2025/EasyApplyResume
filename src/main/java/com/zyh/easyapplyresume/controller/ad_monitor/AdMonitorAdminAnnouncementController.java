package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorAdminAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorAdminAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdMonitorAdminAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 公告管理控制器-管理端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/admin/announcement")
@Tag(name = "公告管理控制器-管理端")
public class AdMonitorAdminAnnouncementController {
    @Autowired
    private AdMonitorAdminAnnouncementService adminAnnouncementService;

    @Operation(summary = "添加公告")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('/admonitor/admin/announcement/add')")
    public BaseResult<Integer> addAnnouncement(@RequestBody AdMonitorAdminAnnouncementForm adMonitorAdminAnnouncementForm) {
        return BaseResult.ok(adminAnnouncementService.addAnnouncement(adMonitorAdminAnnouncementForm));
    }

    @Operation(summary = "修改公告")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('/admonitor/admin/announcement/update')")
    public BaseResult<Integer> updateAnnouncement(@RequestBody AdMonitorAdminAnnouncementForm adMonitorAdminAnnouncementForm) {
        return BaseResult.ok(adminAnnouncementService.updateAnnouncement(adMonitorAdminAnnouncementForm));
    }

    @Operation(summary = "获取公告信息")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admonitor/admin/announcement/getInfo')")
    public BaseResult<AdMonitorAdminAnnouncementInfoVO> getAnnouncementInfo() {
        return BaseResult.ok(adminAnnouncementService.getAnnouncementInfo());
    }

}

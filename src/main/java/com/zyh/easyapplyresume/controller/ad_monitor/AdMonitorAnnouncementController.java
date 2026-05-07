package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdMonitorAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公告管理控制器-监测端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/admonitor/announcement")
@Tag(name = "公告管理控制器-监测端")
public class AdMonitorAnnouncementController {
    @Autowired
    private AdMonitorAnnouncementService adMonitorAnnouncementService;

    @Operation(summary = "添加公告")
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('/admonitor/admonitor/announcement/add')")
    public Integer addAnnouncement(@RequestBody AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        return adMonitorAnnouncementService.addAnnouncement(adMonitorAnnouncementForm);
    }

    @Operation(summary = "修改公告")
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('/admonitor/admonitor/announcement/update')")
    public Integer updateAnnouncement(@RequestBody AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        return adMonitorAnnouncementService.updateAnnouncement(adMonitorAnnouncementForm);
    }

    @Operation(summary = "获取公告信息")
    @RequestMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admonitor/admonitor/announcement/getInfo')")
    public AdMonitorAnnouncementInfoVO getAnnouncementInfo() {
        return adMonitorAnnouncementService.getAnnouncementInfo();
    }
}

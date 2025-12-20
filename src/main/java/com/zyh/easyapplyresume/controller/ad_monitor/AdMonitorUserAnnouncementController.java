package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorUserAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorUserAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdMonitorUserAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 公告管理控制器-用户端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/user/announcement")
@Tag(name = "公告管理控制器-用户端")
public class AdMonitorUserAnnouncementController {
    @Autowired
    private AdMonitorUserAnnouncementService userAnnouncementService;

    @RequestMapping("/add")
    @Operation(summary = "添加公告")
    public Integer addAnnouncement(@RequestBody AdMonitorUserAnnouncementForm userAnnouncementForm) {
        return userAnnouncementService.addAnnouncement(userAnnouncementForm);
    }

    @RequestMapping("/update")
    @Operation(summary = "修改公告")
    public Integer updateAnnouncement(@RequestBody  AdMonitorUserAnnouncementForm userAnnouncementForm) {
        return userAnnouncementService.updateAnnouncement(userAnnouncementForm);
    }

    @RequestMapping("/getInfo")
    @Operation(summary = "获取公告信息")
    public AdMonitorUserAnnouncementInfoVO getAnnouncementInfo() {
        return userAnnouncementService.getAnnouncementInfo();
    }
}

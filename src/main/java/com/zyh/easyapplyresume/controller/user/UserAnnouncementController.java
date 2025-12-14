package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.model.form.user.UserAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.user.UserAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.user.UserAnnouncementService;
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
@RequestMapping("/user/announcement")
@Tag(name = "公告管理控制器-用户端")
public class UserAnnouncementController {
    @Autowired
    private UserAnnouncementService userAnnouncementService;

    @RequestMapping("/add")
    @Operation(summary = "添加公告")
    public Integer addAnnouncement(@RequestBody UserAnnouncementForm userAnnouncementForm) {
        return userAnnouncementService.addAnnouncement(userAnnouncementForm);
    }

    @RequestMapping("/update")
    @Operation(summary = "修改公告")
    public Integer updateAnnouncement(@RequestBody UserAnnouncementForm userAnnouncementForm) {
        return userAnnouncementService.updateAnnouncement(userAnnouncementForm);
    }

    @RequestMapping("/getInfo")
    @Operation(summary = "获取公告信息")
    public UserAnnouncementInfoVO getAnnouncementInfo() {
        return userAnnouncementService.getAnnouncementInfo();
    }
}

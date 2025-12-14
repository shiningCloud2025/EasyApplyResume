package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公告管理控制器-管理端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admin/announcement")
@Tag(name = "公告管理控制器-管理端")
public class AdminAnnouncementController {
    @Autowired
    private AdminAnnouncementService adminAnnouncementService;

    @Operation(summary = "添加公告")
    @PostMapping("/add")
    public BaseResult<Integer> addAnnouncement(@RequestBody AdminAnnouncementForm adminAnnouncementForm) {
        return BaseResult.ok(adminAnnouncementService.addAnnouncement(adminAnnouncementForm));
    }

    @Operation(summary = "修改公告")
    @PutMapping("/update")
    public BaseResult<Integer> updateAnnouncement(@RequestBody AdminAnnouncementForm adminAnnouncementForm) {
        return BaseResult.ok(adminAnnouncementService.updateAnnouncement(adminAnnouncementForm));
    }

    @Operation(summary = "获取公告信息")
    @GetMapping("/getInfo")
    public BaseResult<AdminAnnouncementInfoVO> getAnnouncementInfo() {
        return BaseResult.ok(adminAnnouncementService.getAnnouncementInfo());
    }

}

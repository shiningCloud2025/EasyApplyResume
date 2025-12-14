package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminAnnouncementInfoVO;

/**
 * @author shiningCloud2025
 */
public interface AdminAnnouncementService {
    /**
     * 添加公告(仅能添加一个)
     * @param adminAnnouncementForm
     * @return
     */
    public Integer addAnnouncement(AdminAnnouncementForm adminAnnouncementForm);

    /**
     * 修改公告
     * @param adminAnnouncementForm
     * @return
     */
    public Integer updateAnnouncement(AdminAnnouncementForm adminAnnouncementForm);

    /**
     * 获取公告信息
     * @return
     */
    public AdminAnnouncementInfoVO getAnnouncementInfo();
}

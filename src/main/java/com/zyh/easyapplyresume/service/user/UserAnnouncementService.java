package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.form.admin.AdminAnnouncementForm;
import com.zyh.easyapplyresume.model.form.user.UserAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminAnnouncementInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserAnnouncementInfoVO;

/**
 * @author shiningCloud2025
 */
public interface UserAnnouncementService {

    /**
     * 添加公告(仅能添加一个)
     * @param userAnnouncementForm
     * @return
     */
    public Integer addAnnouncement(UserAnnouncementForm userAnnouncementForm);

    /**
     * 修改公告
     * @param userAnnouncementForm
     * @return
     */
    public Integer updateAnnouncement( UserAnnouncementForm userAnnouncementForm);

    /**
     * 获取公告信息
     * @param announcementId
     * @return
     */
    public UserAnnouncementInfoVO getAnnouncementInfo(Integer announcementId);


}

package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorAnnouncementForm;
import com.zyh.easyapplyresume.model.form.admin.AdminAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorAnnouncementInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminAnnouncementInfoVO;

/**
 * @author shiningCloud2025
 */
public interface AdMonitorAnnouncementService {

    /**
     * 添加公告(仅能添加一个)
     * @param adMonitorAnnouncementForm
     * @return
     */
    public Integer addAnnouncement(AdMonitorAnnouncementForm adMonitorAnnouncementForm);

    /**
     * 修改公告
     * @param adMonitorAnnouncementForm
     * @return
     */
    public Integer updateAnnouncement(AdMonitorAnnouncementForm adMonitorAnnouncementForm);

    /**
     * 获取公告信息
     * @param announcementId
     * @return
     */
    public AdMonitorAnnouncementInfoVO getAnnouncementInfo(Integer announcementId);
}

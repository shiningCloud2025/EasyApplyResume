package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorAdminAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorAdminAnnouncementInfoVO;

/**
 * @author shiningCloud2025
 */
public interface AdMonitorAdminAnnouncementService {
    /**
     * 添加公告(仅能添加一个)
     * @param adminAnnouncementForm
     * @return
     */
    public Integer addAnnouncement(AdMonitorAdminAnnouncementForm adminAnnouncementForm);

    /**
     * 修改公告
     * @param adminAnnouncementForm
     * @return
     */
    public Integer updateAnnouncement(AdMonitorAdminAnnouncementForm adminAnnouncementForm);

    /**
     * 获取公告信息
     * @return
     */
    public AdMonitorAdminAnnouncementInfoVO getAnnouncementInfo();
}

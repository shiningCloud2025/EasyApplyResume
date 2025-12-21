package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorUserAnnouncementForm;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorUserAnnouncementInfoVO;

/**
 * @author shiningCloud2025
 */
public interface AdMonitorUserAnnouncementService {

    /**
     * 添加公告(仅能添加一个)
     * @param userAnnouncementForm
     * @return
     */
    public Integer addAnnouncement(AdMonitorUserAnnouncementForm userAnnouncementForm);

    /**
     * 修改公告
     * @param userAnnouncementForm
     * @return
     */
    public Integer updateAnnouncement(AdMonitorUserAnnouncementForm userAnnouncementForm);

    /**
     * 获取公告信息
     * @return
     */
    public AdMonitorUserAnnouncementInfoVO getAnnouncementInfo();


}

package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdMonitorAdminAnnouncementMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorAdminAnnouncementForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdMonitorAdminAnnouncement;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorAdminAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdMonitorAdminAnnouncementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdMonitorAdminAnnouncementValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@Slf4j
public class AdMonitorAdminAnnouncementServiceImpl implements AdMonitorAdminAnnouncementService {
    @Autowired
    AdMonitorAdminAnnouncementMapper adminAnnouncementMapper;
    @Override
    public Integer addAnnouncement(AdMonitorAdminAnnouncementForm adminAnnouncementForm) {
        try{
            log.info("管理员添加公告");
            AdMonitorAdminAnnouncementValidator.validateForAdd(adminAnnouncementForm);
            List<AdMonitorAdminAnnouncement> adminAnnouncements = adminAnnouncementMapper.selectList(null);
            if (adminAnnouncements.size() > 0) {
                log.error("管理员已添加过公告");
                throw new BusException(AdminCodeEnum.ADMIN_ALREADY_ADD_ANNOUNCEMENT);
            }
            AdMonitorAdminAnnouncement adminAnnouncement = new  AdMonitorAdminAnnouncement();
            BeanUtil.copyProperties(adminAnnouncementForm, adminAnnouncement);
            adminAnnouncement.setAnnouncementUpdatedTime(new Date());
            log.info("管理员添加公告成功");
            return adminAnnouncementMapper.insert(adminAnnouncement);
        }catch (Exception e){
            log.error("管理员添加公告失败");
            throw new BusException(AdminCodeEnum.ADMIN_ADD_ANNOUNCEMENT_FAIL);
        }
    }

    @Override
    public Integer updateAnnouncement(AdMonitorAdminAnnouncementForm adminAnnouncementForm) {
        try{
            log.info("管理员修改公告");
            AdMonitorAdminAnnouncementValidator.validateForUpdate(adminAnnouncementForm);
            AdMonitorAdminAnnouncement adminAnnouncement = new AdMonitorAdminAnnouncement();
            BeanUtil.copyProperties(adminAnnouncementForm, adminAnnouncement);
            adminAnnouncement.setAnnouncementUpdatedTime(new Date());
            log.info("管理员修改公告成功");
            return adminAnnouncementMapper.updateById(adminAnnouncement);
        }catch (Exception e){
            log.error("管理员修改公告失败");
            throw new BusException(AdminCodeEnum.ADMIN_UPDATE_ANNOUNCEMENT_FAIL);
        }
    }

    @Override
    public AdMonitorAdminAnnouncementInfoVO getAnnouncementInfo() {
        try{
            log.info("管理员获取公告信息");
            AdMonitorAdminAnnouncement adminAnnouncement = adminAnnouncementMapper.selectById(1);
            AdMonitorAdminAnnouncementInfoVO adminAnnouncementInfoVO = new AdMonitorAdminAnnouncementInfoVO();
            BeanUtil.copyProperties(adminAnnouncement, adminAnnouncementInfoVO);
            log.info("管理员获取公告信息成功");
            return adminAnnouncementInfoVO;
        }catch (Exception e){
            log.error("管理员获取公告信息失败");
            throw new BusException(AdminCodeEnum.ADMIN_GET_ANNOUNCEMENT_INFO_FAIL);
        }
    }
}

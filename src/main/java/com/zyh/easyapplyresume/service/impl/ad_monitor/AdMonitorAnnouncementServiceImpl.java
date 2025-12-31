package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdMonitorCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdMonitorAnnouncementMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorAnnouncementForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdMonitorAnnouncement;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdMonitorAnnouncementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdMonitorAnnouncementValidator;
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
public class AdMonitorAnnouncementServiceImpl implements AdMonitorAnnouncementService {
    @Autowired
    AdMonitorAnnouncementMapper adMonitorAnnouncementMapper;
    @Override
    public Integer addAnnouncement(AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        try{
            log.info("监测端添加公告");
            AdMonitorAnnouncementValidator.validateForAdd(adMonitorAnnouncementForm);
            List<AdMonitorAnnouncement> adMonitorAnnouncements = adMonitorAnnouncementMapper.selectList(null);
            if (adMonitorAnnouncements.size() > 0) {
                log.error("监测端已添加过公告");
                throw new BusException(AdMonitorCodeEnum.ADMONITOR_ALREADY_ADD_ANNOUNCEMENT);
            }
            AdMonitorAnnouncement adMonitorAnnouncement = new AdMonitorAnnouncement();
            BeanUtil.copyProperties(adMonitorAnnouncementForm, adMonitorAnnouncement);
            adMonitorAnnouncement.setAnnouncementUpdatedTime(new Date());
            log.info("检测端添加成功");
            return adMonitorAnnouncementMapper.insert(adMonitorAnnouncement);
        }catch (BusException e){
            throw e;
        } catch (Exception  e){
            log.error("监测端添加公告失败");
            throw new BusException(AdMonitorCodeEnum.ADMONITOR_ADD_ANNOUNCEMENT_FAILED);
        }
    }

    @Override
    public Integer updateAnnouncement(AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        try{
            log.info("监测端修改公告");
            AdMonitorAnnouncementValidator.validateForUpdate(adMonitorAnnouncementForm);
            AdMonitorAnnouncement adMonitorAnnouncement = new AdMonitorAnnouncement();
            BeanUtil.copyProperties(adMonitorAnnouncementForm, adMonitorAnnouncement);
            adMonitorAnnouncement.setAnnouncementUpdatedTime(new Date());
            log.info("监测端修改公告成功");
            return adMonitorAnnouncementMapper.updateById(adMonitorAnnouncement);
        }catch (BusException e){
            throw e;
        } catch (Exception e){
            log.error("监测端修改公告失败");
            throw new BusException(AdMonitorCodeEnum.ADMONITOR_UPDATE_ANNOUNCEMENT_FAILED);
        }

    }

    @Override
    public AdMonitorAnnouncementInfoVO getAnnouncementInfo() {
        try{
            log.info("监测端获取公告信息");
            AdMonitorAnnouncement adMonitorAnnouncement = adMonitorAnnouncementMapper.selectById(1);
            AdMonitorAnnouncementInfoVO adMonitorAnnouncementInfoVO = new AdMonitorAnnouncementInfoVO();
            BeanUtil.copyProperties(adMonitorAnnouncement, adMonitorAnnouncementInfoVO);
            log.info("监测端获取公告信息成功");
            return adMonitorAnnouncementInfoVO;
        }catch (BusException e){
            throw e;
        } catch (Exception e){
            log.error("监测端获取公告信息失败");
            throw new BusException(AdMonitorCodeEnum.ADMONITOR_GET_ANNOUNCEMENT_INFO_FAILED);
        }
    }
}

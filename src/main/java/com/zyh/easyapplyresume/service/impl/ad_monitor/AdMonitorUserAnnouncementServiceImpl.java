package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdMonitorUserAnnouncementMapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.UserAnnouncementMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorUserAnnouncementForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.UserAnnouncementForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdMonitorUserAnnouncement;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.UserAnnouncement;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorUserAnnouncementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.UserAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdMonitorUserAnnouncementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdMonitorUserAnnouncementValidator;
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
public class AdMonitorUserAnnouncementServiceImpl implements AdMonitorUserAnnouncementService {
    @Autowired
    AdMonitorUserAnnouncementMapper userAnnouncementMapper;
    @Override
    public Integer addAnnouncement( AdMonitorUserAnnouncementForm userAnnouncementForm) {
        try{
            log.info("用户添加公告");
            AdMonitorUserAnnouncementValidator.validateForAdd(userAnnouncementForm);
            List<AdMonitorUserAnnouncement> userAnnouncements = userAnnouncementMapper.selectList(null);
            if (userAnnouncements.size() > 0) {
                log.error("用户已添加过公告");
                throw new BusException(UserCodeEnum.USER_ALREADY_ADD_ANNOUNCEMENT);
            }
            AdMonitorUserAnnouncement userAnnouncement = new  AdMonitorUserAnnouncement();
            BeanUtil.copyProperties(userAnnouncementForm, userAnnouncement);
            userAnnouncement.setAnnouncementUpdatedTime(new Date());
            log.info("用户添加公告成功");
            return userAnnouncementMapper.insert(userAnnouncement);
        }catch (Exception e){
            log.error("用户添加公告失败");
            throw new BusException(UserCodeEnum.USER_ADD_ANNOUNCEMENT_FAIL);
        }
    }

    @Override
    public Integer updateAnnouncement( AdMonitorUserAnnouncementForm userAnnouncementForm) {
        try{
            log.info("用户修改公告");
            AdMonitorUserAnnouncementValidator.validateForUpdate(userAnnouncementForm);
            AdMonitorUserAnnouncement userAnnouncement = new  AdMonitorUserAnnouncement();
            BeanUtil.copyProperties(userAnnouncementForm, userAnnouncement);
            userAnnouncement.setAnnouncementUpdatedTime(new Date());
            log.info("用户修改公告成功");
            return userAnnouncementMapper.updateById(userAnnouncement);
        } catch (Exception  e){
            log.info("用户修改公告失败");
            throw new BusException(UserCodeEnum.USER_UPDATE_ANNOUNCEMENT_FAIL);
        }
    }

    @Override
    public AdMonitorUserAnnouncementInfoVO getAnnouncementInfo() {
        try{
            log.info("用户获取公告信息");
            AdMonitorUserAnnouncement userAnnouncement = userAnnouncementMapper.selectById(1);
            AdMonitorUserAnnouncementInfoVO userAnnouncementInfoVO = new  AdMonitorUserAnnouncementInfoVO();
            BeanUtil.copyProperties(userAnnouncement, userAnnouncementInfoVO);
            log.info("用户获取公告信息成功");
            return userAnnouncementInfoVO;
        }catch (Exception e){
            log.error("用户获取公告信息失败");
            throw new BusException(UserCodeEnum.USER_GET_ANNOUNCEMENT_INFO_FAIL);
        }
    }
}

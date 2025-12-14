package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.user.UserAnnouncementMapper;
import com.zyh.easyapplyresume.model.form.user.UserAnnouncementForm;
import com.zyh.easyapplyresume.model.pojo.user.UserAnnouncement;
import com.zyh.easyapplyresume.model.vo.user.UserAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.user.UserAnnouncementService;
import com.zyh.easyapplyresume.utils.uservalidator.UserAnnouncementValidator;
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
public class UserAnnouncementServiceImpl implements UserAnnouncementService {
    @Autowired
    UserAnnouncementMapper userAnnouncementMapper;
    @Override
    public Integer addAnnouncement(UserAnnouncementForm userAnnouncementForm) {
        try{
            log.info("用户添加公告");
            UserAnnouncementValidator.validateForAdd(userAnnouncementForm);
            List<UserAnnouncement> userAnnouncements = userAnnouncementMapper.selectList(null);
            if (userAnnouncements.size() > 0) {
                log.error("用户已添加过公告");
                throw new BusException(UserCodeEnum.USER_ALREADY_ADD_ANNOUNCEMENT);
            }
            UserAnnouncement userAnnouncement = new UserAnnouncement();
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
    public Integer updateAnnouncement(UserAnnouncementForm userAnnouncementForm) {
        try{
            log.info("用户修改公告");
            UserAnnouncementValidator.validateForUpdate(userAnnouncementForm);
            UserAnnouncement userAnnouncement = new UserAnnouncement();
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
    public UserAnnouncementInfoVO getAnnouncementInfo() {
        try{
            log.info("用户获取公告信息");
            UserAnnouncement userAnnouncement = userAnnouncementMapper.selectById(1);
            UserAnnouncementInfoVO userAnnouncementInfoVO = new UserAnnouncementInfoVO();
            BeanUtil.copyProperties(userAnnouncement, userAnnouncementInfoVO);
            log.info("用户获取公告信息成功");
            return userAnnouncementInfoVO;
        }catch (Exception e){
            log.error("用户获取公告信息失败");
            throw new BusException(UserCodeEnum.USER_GET_ANNOUNCEMENT_INFO_FAIL);
        }
    }
}

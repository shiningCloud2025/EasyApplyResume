package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminAnnouncementMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminAnnouncementForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminAnnouncement;
import com.zyh.easyapplyresume.model.vo.admin.AdminAnnouncementInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminAnnouncementService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminAnnouncementValidator;
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
public class AdminAnnouncementServiceImpl implements AdminAnnouncementService {
    @Autowired
    AdminAnnouncementMapper adminAnnouncementMapper;
    @Override
    public Integer addAnnouncement(AdminAnnouncementForm adminAnnouncementForm) {
        try{
            log.info("管理员添加公告");
            AdminAnnouncementValidator.validateForAdd(adminAnnouncementForm);
            List<AdminAnnouncement> adminAnnouncements = adminAnnouncementMapper.selectList(null);
            if (adminAnnouncements.size() > 0) {
                log.error("管理员已添加过公告");
                throw new BusException(AdminCodeEnum.ADMIN_ALREADY_ADD_ANNOUNCEMENT);
            }
            AdminAnnouncement adminAnnouncement = new AdminAnnouncement();
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
    public Integer updateAnnouncement(AdminAnnouncementForm adminAnnouncementForm) {
        try{
            log.info("管理员修改公告");
            AdminAnnouncementValidator.validateForUpdate(adminAnnouncementForm);
            AdminAnnouncement adminAnnouncement = new AdminAnnouncement();
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
    public AdminAnnouncementInfoVO getAnnouncementInfo() {
        try{
            log.info("管理员获取公告信息");
            AdminAnnouncement adminAnnouncement = adminAnnouncementMapper.selectById(1);
            AdminAnnouncementInfoVO adminAnnouncementInfoVO = new AdminAnnouncementInfoVO();
            BeanUtil.copyProperties(adminAnnouncement, adminAnnouncementInfoVO);
            log.info("管理员获取公告信息成功");
            return adminAnnouncementInfoVO;
        }catch (Exception e){
            log.error("管理员获取公告信息失败");
            throw new BusException(AdminCodeEnum.ADMIN_GET_ANNOUNCEMENT_INFO_FAIL);
        }
    }
}

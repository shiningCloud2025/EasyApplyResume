package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminJoinUsMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminJoinUsForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminJoinUs;
import com.zyh.easyapplyresume.model.vo.admin.AdminJoinUsInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminJoinUsService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminJoinUsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 加入我们Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminJoinUsServiceImpl implements AdminJoinUsService {
    @Autowired
    private AdminJoinUsMapper joinUsMapper;

    @Override
    public Integer addJoinUs(AdminJoinUsForm joinUsForm) {
        try {
            log.info("添加加入我们");
            AdminJoinUsValidator.validateForAdd(joinUsForm);
            List<AdminJoinUs> joinUsList = joinUsMapper.selectList(null);
            if (joinUsList.size() > 0) {
                log.error("已添加过加入我们");
                throw new BusException(AdminCodeEnum.JOIN_US_ALREADY_ADD);
            }
            AdminJoinUs joinUs = new AdminJoinUs();
            BeanUtils.copyProperties(joinUsForm, joinUs);
            joinUs.setJoinUsUpdatedTime(new Date());
            int result = joinUsMapper.insert(joinUs);
            log.info("添加加入我们成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加加入我们失败");
            throw new BusException(AdminCodeEnum.JOIN_US_ADD_FAIL);
        }
    }

    @Override
    public Integer updateJoinUs(AdminJoinUsForm joinUsForm) {
        try {
            log.info("修改加入我们");
            AdminJoinUsValidator.validateForUpdate(joinUsForm);
            AdminJoinUs joinUs = new AdminJoinUs();
            BeanUtils.copyProperties(joinUsForm, joinUs);
            joinUs.setJoinUsUpdatedTime(new Date());
            int result = joinUsMapper.updateById(joinUs);
            log.info("修改加入我们成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改加入我们失败");
            throw new BusException(AdminCodeEnum.JOIN_US_UPDATE_FAIL);
        }
    }

    @Override
    public AdminJoinUsInfoVO getJoinUsInfo() {
        try {
            log.info("获取加入我们信息");
            List<AdminJoinUs> joinUsList = joinUsMapper.selectList(null);
            if (joinUsList.size() > 0) {
                AdminJoinUsInfoVO infoVO = new AdminJoinUsInfoVO();
                BeanUtils.copyProperties(joinUsList.get(0), infoVO);
                return infoVO;
            }
            return null;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取加入我们信息失败");
            throw new BusException(AdminCodeEnum.JOIN_US_GET_INFO_FAIL);
        }
    }
}


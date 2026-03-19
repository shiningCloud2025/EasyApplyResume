package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminProjectIntroduceMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminProjectIntroduceForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminProjectIntroduce;
import com.zyh.easyapplyresume.model.vo.admin.AdminProjectIntroduceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminProjectIntroduceService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminProjectIntroduceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 项目介绍Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminProjectIntroduceServiceImpl implements AdminProjectIntroduceService {
    @Autowired
    private AdminProjectIntroduceMapper projectIntroduceMapper;

    @Override
    public Integer addProjectIntroduce(AdminProjectIntroduceForm projectIntroduceForm) {
        try {
            log.info("添加项目介绍");
            AdminProjectIntroduceValidator.validateForAdd(projectIntroduceForm);
            List<AdminProjectIntroduce> projectIntroduces = projectIntroduceMapper.selectList(null);
            if (projectIntroduces.size() > 0) {
                log.error("已添加过项目介绍");
                throw new BusException(AdminCodeEnum.PROJECT_INTRODUCE_ALREADY_ADD);
            }
            AdminProjectIntroduce projectIntroduce = new AdminProjectIntroduce();
            BeanUtils.copyProperties(projectIntroduceForm, projectIntroduce);
            projectIntroduce.setProjectIntroduceUpdatedTime(LocalDateTime.now());
            int result = projectIntroduceMapper.insert(projectIntroduce);
            log.info("添加项目介绍成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加项目介绍失败");
            throw new BusException(AdminCodeEnum.PROJECT_INTRODUCE_ADD_FAIL);
        }
    }

    @Override
    public Integer updateProjectIntroduce(AdminProjectIntroduceForm projectIntroduceForm) {
        try {
            log.info("修改项目介绍");
            AdminProjectIntroduceValidator.validateForUpdate(projectIntroduceForm);
            AdminProjectIntroduce projectIntroduce = new AdminProjectIntroduce();
            BeanUtils.copyProperties(projectIntroduceForm, projectIntroduce);
            projectIntroduce.setProjectIntroduceUpdatedTime(LocalDateTime.now());
            int result = projectIntroduceMapper.updateById(projectIntroduce);
            log.info("修改项目介绍成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改项目介绍失败");
            throw new BusException(AdminCodeEnum.PROJECT_INTRODUCE_UPDATE_FAIL);
        }
    }

    @Override
    public AdminProjectIntroduceInfoVO getProjectIntroduceInfo() {
        try {
            log.info("获取项目介绍信息");
            List<AdminProjectIntroduce> projectIntroduces = projectIntroduceMapper.selectList(null);
            if (projectIntroduces.size() > 0) {
                AdminProjectIntroduceInfoVO infoVO = new AdminProjectIntroduceInfoVO();
                BeanUtils.copyProperties(projectIntroduces.get(0), infoVO);
                return infoVO;
            }
            return null;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取项目介绍信息失败");
            throw new BusException(AdminCodeEnum.PROJECT_INTRODUCE_GET_INFO_FAIL);
        }
    }
}


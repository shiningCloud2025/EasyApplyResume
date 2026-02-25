package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminUserGuideMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminUserGuideForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminUserGuide;
import com.zyh.easyapplyresume.model.vo.admin.AdminUserGuideInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminUserGuidePageVO;
import com.zyh.easyapplyresume.service.admin.AdminUserGuideService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminUserGuideValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 使用指南Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminUserGuideServiceImpl implements AdminUserGuideService {
    @Autowired
    private AdminUserGuideMapper userGuideMapper;

    @Override
    public Integer addUserGuide(AdminUserGuideForm userGuideForm) {
        try {
            log.info("添加使用指南");
            // 校验表单字段
            AdminUserGuideValidator.validateForAdd(userGuideForm);
            AdminUserGuide userGuide = new AdminUserGuide();
            BeanUtils.copyProperties(userGuideForm, userGuide);
            userGuide.setUserGuideCreatedTime(new Date());
            userGuide.setUserGuideUpdatedTime(new Date());
            userGuide.setDeleted(0);
            int result = userGuideMapper.insert(userGuide);
            log.info("添加使用指南成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加使用指南失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer updateUserGuide(AdminUserGuideForm userGuideForm) {
        try {
            log.info("修改使用指南");
            // 校验表单字段
            AdminUserGuideValidator.validateForUpdate(userGuideForm);
            AdminUserGuide userGuide = new AdminUserGuide();
            BeanUtils.copyProperties(userGuideForm, userGuide);
            userGuide.setUserGuideUpdatedTime(new Date());
            int result = userGuideMapper.updateById(userGuide);
            log.info("修改使用指南成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改使用指南失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer deleteUserGuide(Integer userGuideId) {
        try {
            log.info("删除使用指南");
            AdminUserGuide userGuide = new AdminUserGuide();
            userGuide.setUserGuideId(userGuideId);
            userGuide.setDeleted(1);
            userGuide.setUserGuideUpdatedTime(new Date());
            int result = userGuideMapper.updateById(userGuide);
            log.info("删除使用指南成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除使用指南失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public AdminUserGuideInfoVO getUserGuideInfo(Integer userGuideId) {
        try {
            log.info("获取使用指南信息");
            AdminUserGuide userGuide = userGuideMapper.selectById(userGuideId);
            if (userGuide == null || userGuide.getDeleted() == 1) {
                log.error("使用指南不存在");
                throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
            }
            AdminUserGuideInfoVO userGuideInfoVO = new AdminUserGuideInfoVO();
            BeanUtils.copyProperties(userGuide, userGuideInfoVO);
            log.info("获取使用指南信息成功");
            return userGuideInfoVO;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取使用指南信息失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Page<AdminUserGuidePageVO> getUserGuidePage(int size, int page) {
        try {
            log.info("分页查询使用指南");
            Page<AdminUserGuide> userGuidePage = new Page<>(page, size);
            Page<AdminUserGuide> result = userGuideMapper.selectPage(userGuidePage, null);
            // 转换为Page<AdminUserGuidePageVO>
            Page<AdminUserGuidePageVO> pageVO = new Page<>(page, size);
            pageVO.setTotal(result.getTotal());
            pageVO.setRecords(result.getRecords().stream().map(userGuide -> {
                AdminUserGuidePageVO vo = new AdminUserGuidePageVO();
                BeanUtils.copyProperties(userGuide, vo);
                return vo;
            }).toList());
            log.info("分页查询使用指南成功");
            return pageVO;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("分页查询使用指南失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }
}


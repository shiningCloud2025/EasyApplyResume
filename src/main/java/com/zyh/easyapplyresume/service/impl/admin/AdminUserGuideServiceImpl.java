package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminUserGuideMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminUserGuideForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminUserGuide;
import com.zyh.easyapplyresume.model.query.admin.AdminUserGuideQuery;
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

import java.time.LocalDateTime;
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
            userGuide.setUserGuideCreatedTime(LocalDateTime.now());
            userGuide.setUserGuideUpdatedTime(LocalDateTime.now());
            userGuide.setDeleted(0);
            int result = userGuideMapper.insert(userGuide);
            log.info("添加使用指南成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加使用指南失败");
            throw new BusException(AdminCodeEnum.USER_GUIDE_ADD_FAIL);
        }
    }

    @Override
    public Integer updateUserGuide(AdminUserGuideForm userGuideForm) {
        try {
            log.info("修改使用指南");
            AdminUserGuideValidator.validateForUpdate(userGuideForm);
            AdminUserGuide userGuide = new AdminUserGuide();
            BeanUtils.copyProperties(userGuideForm, userGuide);
            userGuide.setUserGuideUpdatedTime(LocalDateTime.now());
            // 使用 LambdaUpdateWrapper，避免 updateById 在全局逻辑删除配置下生成非法 SQL
            LambdaUpdateWrapper<AdminUserGuide> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(AdminUserGuide::getUserGuideId, userGuide.getUserGuideId());
            wrapper.eq(AdminUserGuide::getDeleted, 0);
            if (userGuide.getUserGuideTitle() != null)
                wrapper.set(AdminUserGuide::getUserGuideTitle, userGuide.getUserGuideTitle());
            if (userGuide.getUserGuideContent() != null)
                wrapper.set(AdminUserGuide::getUserGuideContent, userGuide.getUserGuideContent());
            wrapper.set(AdminUserGuide::getUserGuideUpdatedTime, userGuide.getUserGuideUpdatedTime());
            int result = userGuideMapper.update(null, wrapper);
            log.info("修改使用指南成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改使用指南失败");
            throw new BusException(AdminCodeEnum.USER_GUIDE_UPDATE_FAIL);
        }
    }

    @Override
    public Integer deleteUserGuide(Integer userGuideId) {
        try {
            log.info("删除使用指南");
            // 使用 LambdaUpdateWrapper，避免 updateById 在全局逻辑删除配置下生成非法 SQL
            LambdaUpdateWrapper<AdminUserGuide> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(AdminUserGuide::getUserGuideId, userGuideId);
            wrapper.eq(AdminUserGuide::getDeleted, 0);
            wrapper.set(AdminUserGuide::getDeleted, 1);
            wrapper.set(AdminUserGuide::getUserGuideUpdatedTime, LocalDateTime.now());
            int result = userGuideMapper.update(null, wrapper);
            log.info("删除使用指南成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除使用指南失败");
            throw new BusException(AdminCodeEnum.USER_GUIDE_DELETE_FAIL);
        }
    }

    @Override
    public AdminUserGuideInfoVO getUserGuideInfo(Integer userGuideId) {
        try {
            log.info("获取使用指南信息");
            AdminUserGuide userGuide = userGuideMapper.selectById(userGuideId);
            if (userGuide == null || userGuide.getDeleted() == 1) {
                log.error("使用指南不存在");
                throw new BusException(AdminCodeEnum.USER_GUIDE_NOT_FOUND);
            }
            AdminUserGuideInfoVO userGuideInfoVO = new AdminUserGuideInfoVO();
            BeanUtils.copyProperties(userGuide, userGuideInfoVO);
            log.info("获取使用指南信息成功");
            return userGuideInfoVO;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取使用指南信息失败");
            throw new BusException(AdminCodeEnum.USER_GUIDE_GET_INFO_FAIL);
        }
    }

    @Override
    public Page<AdminUserGuidePageVO> getUserGuidePage(int size, int page, AdminUserGuideQuery userGuideQuery) {
        try {
            log.info("分页查询使用指南");
            Page<AdminUserGuide> userGuidePage = new Page<>(page, size);
            LambdaQueryWrapper<AdminUserGuide> wrapper = new LambdaQueryWrapper<>();
            // 只查询未删除的数据
            wrapper.eq(AdminUserGuide::getDeleted, 0);
            // 按标题模糊查询
            if (userGuideQuery != null && userGuideQuery.getUserGuideTitle() != null && !userGuideQuery.getUserGuideTitle().trim().isEmpty()) {
                wrapper.like(AdminUserGuide::getUserGuideTitle, userGuideQuery.getUserGuideTitle().trim());
            }
            Page<AdminUserGuide> result = userGuideMapper.selectPage(userGuidePage, wrapper);
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
            throw new BusException(AdminCodeEnum.USER_GUIDE_GET_PAGE_FAIL);
        }
    }
}


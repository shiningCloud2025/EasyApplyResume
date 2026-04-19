package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminQuestionSecondCategoryMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionSecondCategoryForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminQuestionSecondCategory;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionSecondCategoryQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionSecondCategoryInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionSecondCategoryPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminQuestionSecondCategoryService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminQuestionSecondCategoryFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题库小类管理实现
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminQuestionSecondCategoryServiceImpl implements AdminQuestionSecondCategoryService {

    @Autowired
    private AdminQuestionSecondCategoryMapper adminQuestionSecondCategoryMapper;

    @Autowired
    private AdminQuestionSecondCategoryFormValidator adminQuestionSecondCategoryFormValidator;

    @Override
    public Integer addQuestionSecondCategory(AdminQuestionSecondCategoryForm form) {
        try {
            adminQuestionSecondCategoryFormValidator.validateForAdd(form);
            validateQuestionSecondCategoryUnique(form);

            AdminQuestionSecondCategory questionSecondCategory = new AdminQuestionSecondCategory();
            BeanUtils.copyProperties(form, questionSecondCategory);
            questionSecondCategory.setQuestionSecondCategoryCreateTime(LocalDateTime.now());
            questionSecondCategory.setDeleted(0);

            adminQuestionSecondCategoryMapper.insert(questionSecondCategory);
            return questionSecondCategory.getQuestionSecondCategoryId();
        } catch (BusException e) {
            log.info("新增题库小类业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("新增题库小类失败", e);
            throw new RuntimeException("新增题库小类失败");
        }
    }

    @Override
    public Integer updateQuestionSecondCategory(AdminQuestionSecondCategoryForm form) {
        try {
            adminQuestionSecondCategoryFormValidator.validateForUpdate(form);
            validateQuestionSecondCategoryUnique(form);

            // 占位：后续需要联动题目、用户题目映射表
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_UPDATE_FAIL);
        } catch (BusException e) {
            log.info("修改题库小类业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("修改题库小类失败", e);
            throw new RuntimeException("修改题库小类失败");
        }
    }

    @Override
    public Integer deleteQuestionSecondCategory(Integer questionSecondCategoryId) {
        try {
            // 占位：后续需要联动题目、用户题目映射表
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_DELETE_FAIL);
        } catch (BusException e) {
            log.info("删除题库小类业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("删除题库小类失败", e);
            throw new RuntimeException("删除题库小类失败");
        }
    }

    @Override
    public AdminQuestionSecondCategoryInfoVO findQuestionSecondCategoryById(Integer questionSecondCategoryId) {
        try {
            LambdaQueryWrapper<AdminQuestionSecondCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionSecondCategory::getQuestionSecondCategoryId, questionSecondCategoryId);
            lambdaQueryWrapper.eq(AdminQuestionSecondCategory::getDeleted, 0);

            AdminQuestionSecondCategory questionSecondCategory = adminQuestionSecondCategoryMapper.selectOne(lambdaQueryWrapper);
            if (questionSecondCategory == null || (questionSecondCategory.getDeleted() != null && questionSecondCategory.getDeleted() == 1)) {
                throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_NOT_FOUND);
            }

            AdminQuestionSecondCategoryInfoVO vo = new AdminQuestionSecondCategoryInfoVO();
            BeanUtils.copyProperties(questionSecondCategory, vo);
            return vo;
        } catch (BusException e) {
            log.info("查询题库小类详情业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询题库小类详情失败", e);
            throw new RuntimeException("查询题库小类详情失败");
        }
    }

    @Override
    public Page<AdminQuestionSecondCategoryPageVO> findQuestionSecondCategoryByPage(Integer pageNum, Integer pageSize, AdminQuestionSecondCategoryQuery query) {
        try {
            LambdaQueryWrapper<AdminQuestionSecondCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionSecondCategory::getDeleted, 0);

            if (query != null) {
                if (query.getQuestionFirstCategoryId() != null) {
                    lambdaQueryWrapper.eq(AdminQuestionSecondCategory::getQuestionFirstCategoryId, query.getQuestionFirstCategoryId());
                }
                if (query.getQuestionSecondCategoryName() != null && !query.getQuestionSecondCategoryName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminQuestionSecondCategory::getQuestionSecondCategoryName, query.getQuestionSecondCategoryName().trim());
                }
                if (query.getQuestionSecondCategoryIntroduce() != null && !query.getQuestionSecondCategoryIntroduce().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminQuestionSecondCategory::getQuestionSecondCategoryIntroduce, query.getQuestionSecondCategoryIntroduce().trim());
                }
            }

            lambdaQueryWrapper.orderByDesc(AdminQuestionSecondCategory::getQuestionSecondCategoryCreateTime);

            Page<AdminQuestionSecondCategory> page = adminQuestionSecondCategoryMapper.selectPage(
                    new Page<>(pageNum, pageSize),
                    lambdaQueryWrapper
            );

            List<AdminQuestionSecondCategoryPageVO> voList = page.getRecords().stream()
                    .map(item -> {
                        AdminQuestionSecondCategoryPageVO vo = new AdminQuestionSecondCategoryPageVO();
                        BeanUtils.copyProperties(item, vo);
                        return vo;
                    })
                    .collect(Collectors.toList());

            Page<AdminQuestionSecondCategoryPageVO> result = new Page<>();
            result.setRecords(voList);
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
            result.setPages(page.getPages());

            return result;
        } catch (BusException e) {
            log.info("分页查询题库小类业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("分页查询题库小类失败", e);
            throw new RuntimeException("分页查询题库小类失败");
        }
    }

    @Override
    public List<AdminQuestionSecondCategoryInfoVO> findAllQuestionSecondCategory() {
        try {
            LambdaQueryWrapper<AdminQuestionSecondCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionSecondCategory::getDeleted, 0);
            lambdaQueryWrapper.orderByDesc(AdminQuestionSecondCategory::getQuestionSecondCategoryCreateTime);

            List<AdminQuestionSecondCategory> questionSecondCategoryList = adminQuestionSecondCategoryMapper.selectList(lambdaQueryWrapper);

            return questionSecondCategoryList.stream().map(item -> {
                AdminQuestionSecondCategoryInfoVO vo = new AdminQuestionSecondCategoryInfoVO();
                BeanUtils.copyProperties(item, vo);
                return vo;
            }).collect(Collectors.toList());
        } catch (BusException e) {
            log.info("查询所有题库小类业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询所有题库小类失败", e);
            throw new RuntimeException("查询所有题库小类失败");
        }
    }

    private void validateQuestionSecondCategoryUnique(AdminQuestionSecondCategoryForm form) {
        LambdaQueryWrapper<AdminQuestionSecondCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminQuestionSecondCategory::getDeleted, 0);
        lambdaQueryWrapper.eq(AdminQuestionSecondCategory::getQuestionFirstCategoryId, form.getQuestionFirstCategoryId());
        lambdaQueryWrapper.eq(AdminQuestionSecondCategory::getQuestionSecondCategoryName, form.getQuestionSecondCategoryName().trim());

        if (form.getQuestionSecondCategoryId() != null) {
            lambdaQueryWrapper.ne(AdminQuestionSecondCategory::getQuestionSecondCategoryId, form.getQuestionSecondCategoryId());
        }

        Long count = adminQuestionSecondCategoryMapper.selectCount(lambdaQueryWrapper);
        if (count != null && count > 0) {
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_NAME_DUPLICATE);
        }
    }
}

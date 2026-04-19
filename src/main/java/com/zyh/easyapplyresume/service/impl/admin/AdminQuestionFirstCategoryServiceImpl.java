package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminQuestionFirstCategoryMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionFirstCategoryForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminQuestionFirstCategory;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionFirstCategoryQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionFirstCategoryInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionFirstCategoryPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminQuestionFirstCategoryService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminQuestionFirstCategoryFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题库大类管理实现
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminQuestionFirstCategoryServiceImpl implements AdminQuestionFirstCategoryService {

    @Autowired
    private AdminQuestionFirstCategoryMapper adminQuestionFirstCategoryMapper;

    @Autowired
    private AdminQuestionFirstCategoryFormValidator adminQuestionFirstCategoryFormValidator;

    @Override
    public Integer addQuestionFirstCategory(AdminQuestionFirstCategoryForm form) {
        try {
            adminQuestionFirstCategoryFormValidator.validateForAdd(form);
            validateQuestionFirstCategoryUnique(form);

            AdminQuestionFirstCategory questionFirstCategory = new AdminQuestionFirstCategory();
            BeanUtils.copyProperties(form, questionFirstCategory);
            questionFirstCategory.setQuestionFirstCategoryCreateTime(LocalDateTime.now());
            questionFirstCategory.setDeleted(0);

            adminQuestionFirstCategoryMapper.insert(questionFirstCategory);
            return questionFirstCategory.getQuestionFirstCategoryId();
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("新增题库大类失败", e);
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_ADD_FAIL);
        }
    }

    @Override
    public Integer updateQuestionFirstCategory(AdminQuestionFirstCategoryForm form) {
        try {
            adminQuestionFirstCategoryFormValidator.validateForUpdate(form);
            validateQuestionFirstCategoryUnique(form);

            // 占位：后续需要联动小类、题目、用户题目映射表
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_UPDATE_FAIL);
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改题库大类失败", e);
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_UPDATE_FAIL);
        }
    }

    @Override
    public Integer deleteQuestionFirstCategory(Integer questionFirstCategoryId) {
        try {
            // 占位：后续需要联动小类、题目、用户题目映射表
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_DELETE_FAIL);
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除题库大类失败", e);
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_DELETE_FAIL);
        }
    }

    @Override
    public AdminQuestionFirstCategoryInfoVO findQuestionFirstCategoryById(Integer questionFirstCategoryId) {
        try {
            LambdaQueryWrapper<AdminQuestionFirstCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionFirstCategory::getQuestionFirstCategoryId, questionFirstCategoryId);
            lambdaQueryWrapper.eq(AdminQuestionFirstCategory::getDeleted, 0);
            AdminQuestionFirstCategory questionFirstCategory = adminQuestionFirstCategoryMapper.selectOne(lambdaQueryWrapper);
            if (questionFirstCategory == null || (questionFirstCategory.getDeleted() != null && questionFirstCategory.getDeleted() == 1)) {
                throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_NOT_FOUND);
            }

            AdminQuestionFirstCategoryInfoVO vo = new AdminQuestionFirstCategoryInfoVO();
            BeanUtils.copyProperties(questionFirstCategory, vo);
            return vo;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询题库大类详情失败", e);
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_INFO_FAIL);
        }
    }

    @Override
    public Page<AdminQuestionFirstCategoryPageVO> findQuestionFirstCategoryByPage(Integer pageNum, Integer pageSize, AdminQuestionFirstCategoryQuery query) {
        try {
            LambdaQueryWrapper<AdminQuestionFirstCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionFirstCategory::getDeleted, 0);

            if (query != null) {
                if (query.getQuestionFirstCategoryName() != null && !query.getQuestionFirstCategoryName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminQuestionFirstCategory::getQuestionFirstCategoryName, query.getQuestionFirstCategoryName().trim());
                }
                if (query.getQuestionFirstCategoryIntroduce() != null && !query.getQuestionFirstCategoryIntroduce().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminQuestionFirstCategory::getQuestionFirstCategoryIntroduce, query.getQuestionFirstCategoryIntroduce().trim());
                }
            }

            lambdaQueryWrapper.orderByDesc(AdminQuestionFirstCategory::getQuestionFirstCategoryCreateTime);

            Page<AdminQuestionFirstCategory> page = adminQuestionFirstCategoryMapper.selectPage(
                    new Page<>(pageNum, pageSize),
                    lambdaQueryWrapper
            );

            List<AdminQuestionFirstCategoryPageVO> voList = page.getRecords().stream()
                    .map(item -> {
                        AdminQuestionFirstCategoryPageVO vo = new AdminQuestionFirstCategoryPageVO();
                        BeanUtils.copyProperties(item, vo);
                        return vo;
                    })
                    .collect(Collectors.toList());

            Page<AdminQuestionFirstCategoryPageVO> result = new Page<>();
            result.setRecords(voList);
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
            result.setPages(page.getPages());

            return result;
        } catch (Exception e) {
            log.error("分页查询题库大类失败", e);
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_PAGE_FAIL);
        }
    }

    private void validateQuestionFirstCategoryUnique(AdminQuestionFirstCategoryForm form) {
        LambdaQueryWrapper<AdminQuestionFirstCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminQuestionFirstCategory::getDeleted, 0);
        lambdaQueryWrapper.eq(AdminQuestionFirstCategory::getQuestionFirstCategoryName, form.getQuestionFirstCategoryName().trim());

        if (form.getQuestionFirstCategoryId() != null) {
            lambdaQueryWrapper.ne(AdminQuestionFirstCategory::getQuestionFirstCategoryId, form.getQuestionFirstCategoryId());
        }

        Long count = adminQuestionFirstCategoryMapper.selectCount(lambdaQueryWrapper);
        if (count != null && count > 0) {
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_NAME_DUPLICATE);
        }
    }
}
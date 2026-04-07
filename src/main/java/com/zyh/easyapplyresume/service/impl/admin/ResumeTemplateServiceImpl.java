package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.ResumeTemplateMapper;
import com.zyh.easyapplyresume.model.form.admin.ResumeTemplateForm;
import com.zyh.easyapplyresume.model.pojo.admin.ResumeTemplate;
import com.zyh.easyapplyresume.model.query.admin.ResumeTemplateQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplatePageVO;
import com.zyh.easyapplyresume.redis.constant.common.ResumeTemplateCacheKey;
import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.util.CacheInvalidatePublisher;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.IndustryMapService;
import com.zyh.easyapplyresume.utils.adminvalidator.ResumeTemplateFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zyh.easyapplyresume.service.admin.ResumeTemplateService;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class ResumeTemplateServiceImpl implements ResumeTemplateService {
    @Autowired
    private ResumeTemplateMapper resumeTemplateMapper;
    @Autowired
    private IndustryMapService industryMapService;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Autowired
    private CacheInvalidatePublisher cachePublisher;

    @Override
    public Integer addResumeTemplate(ResumeTemplateForm resumeTemplateForm) {
        ResumeTemplateFormValidator.validateForAdd(resumeTemplateForm);
        ResumeTemplate resumeTemplate = new ResumeTemplate();
        BeanUtils.copyProperties(resumeTemplateForm, resumeTemplate);
        resumeTemplate.setResumeTemplateIsActive(1);
        resumeTemplate.setResumeTemplateCreatedTime(new DateTime());
        resumeTemplate.setResumeTemplateUpdatedTime(new DateTime());
        resumeTemplate.setDeleted(0);
        try{
            int result = resumeTemplateMapper.insert(resumeTemplate);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            ResumeTemplateCacheKey.ALL_PATTERN,
                            CacheOperationType.ADD
                        );
                    }
                }
            );
            
            return result;
        }catch (DataAccessException e){
            throw resolveResumeDbException(e);
        }

    }

    @Override
    public Integer updateResumeTemplate(ResumeTemplateForm resumeTemplateForm) {
        ResumeTemplateFormValidator.validateForUpdate(resumeTemplateForm);
        ResumeTemplate resumeTemplate = new ResumeTemplate();
        BeanUtils.copyProperties(resumeTemplateForm, resumeTemplate);
        resumeTemplate.setResumeTemplateUpdatedTime(new DateTime());
        try{
            int result = resumeTemplateMapper.updateById(resumeTemplate);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            ResumeTemplateCacheKey.ALL_PATTERN,
                            CacheOperationType.UPDATE
                        );
                    }
                }
            );
            
            return result;
        }catch (DataAccessException e){
            throw resolveResumeDbException(e);
        }
    }


    private BusException resolveResumeDbException(Exception e) {
        String errorMsg = e.getMessage();
        // 1. 匹配唯一约束冲突异常（与原方法一致：DuplicateKeyException 或包含 "Duplicate entry" 信息）
        if (errorMsg.contains("Duplicate entry") || e instanceof DuplicateKeyException) {
            // 2. 匹配简历名称字段名 或 简历名称唯一索引名（核心逻辑）
            // 注意：请将 "uk_resume_template_name" 替换为你数据库中实际的唯一索引名！
            if (errorMsg.contains("resumeTemplate_name") || errorMsg.contains("general_resumeTemplate_pk")) {
                return new BusException(AdminCodeEnum.RESUME_TEMPLATE_NAME_DUPLICATE);
            }
        }
        // 3. 未匹配到特定异常，返回模块内通用数据库异常
        return new BusException(AdminCodeEnum.DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION);
    }

    @Override
    public Integer deleteResumeTemplate(Integer resumeTemplateId) {
        ResumeTemplate resumeTemplate = new ResumeTemplate();
        resumeTemplate.setResumeTemplateId(resumeTemplateId);
        resumeTemplate.setDeleted(1);
        int result = resumeTemplateMapper.updateById(resumeTemplate);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    cachePublisher.publishInvalidate(
                        ResumeTemplateCacheKey.ALL_PATTERN,
                        CacheOperationType.DELETE
                    );
                }
            }
        );
        
        return result;
    }

    @Override
    public ResumeTemplateInfoVO findResumeTemplateById(Integer resumeTemplateId) {
        String cacheKey = ResumeTemplateCacheKey.GET_PREFIX + "_" + resumeTemplateId;
        
        Object cached = redisCacheUtil.get(cacheKey);
        if (cached != null) {
            return (ResumeTemplateInfoVO) cached;
        }
        
        LambdaQueryWrapper<ResumeTemplate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ResumeTemplate::getResumeTemplateId, resumeTemplateId);
        lambdaQueryWrapper.eq(ResumeTemplate::getDeleted, 0);
        ResumeTemplate resumeTemplate = resumeTemplateMapper.selectOne(lambdaQueryWrapper);
        ResumeTemplateInfoVO resumeTemplateInfoVO = BeanUtil.copyProperties(resumeTemplate, ResumeTemplateInfoVO.class);
        resumeTemplateInfoVO.setCreateTime(resumeTemplate.getResumeTemplateCreatedTime());
        resumeTemplateInfoVO.setUpdateTime(resumeTemplate.getResumeTemplateUpdatedTime());
        resumeTemplateInfoVO.setIsEnable(resumeTemplate.getResumeTemplateIsActive());
        resumeTemplateInfoVO.setIndustryMapIndustryName(industryMapService.findIndustryMapById(resumeTemplate.getResumeTemplateIndustry()).getIndustryMapIndustryName());
        
        redisCacheUtil.set(cacheKey, resumeTemplateInfoVO, ResumeTemplateCacheKey.GET_TTL, TimeUnit.MINUTES);
        
        return resumeTemplateInfoVO;
    }

    @Override
    public Page<ResumeTemplatePageVO> findResumeTemplateByPage(Integer pageNum, Integer pageSize, ResumeTemplateQuery resumeTemplateQuery) {
        boolean hasQueryCondition = resumeTemplateQuery != null && (
                (resumeTemplateQuery.getResumeTemplateName() != null
                        && !resumeTemplateQuery.getResumeTemplateName().trim().isEmpty())
                        || resumeTemplateQuery.getResumeTemplateIndustry() != null
        );

        String cacheKey = ResumeTemplateCacheKey.PAGE_PREFIX
                + "_" + pageNum
                + "_" + pageSize;

        if (!hasQueryCondition) {
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                return (Page<ResumeTemplatePageVO>) cached;
            }
        }

        LambdaQueryWrapper<ResumeTemplate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ResumeTemplate::getDeleted, 0);

        if (resumeTemplateQuery != null
                && resumeTemplateQuery.getResumeTemplateName() != null
                && !resumeTemplateQuery.getResumeTemplateName().trim().isEmpty()) {
            lambdaQueryWrapper.like(ResumeTemplate::getResumeTemplateName, resumeTemplateQuery.getResumeTemplateName().trim());
        }

        if (resumeTemplateQuery != null && resumeTemplateQuery.getResumeTemplateIndustry() != null) {
            lambdaQueryWrapper.eq(ResumeTemplate::getResumeTemplateIndustry, resumeTemplateQuery.getResumeTemplateIndustry());
        }

        Page<ResumeTemplate> resumeTemplatePage = resumeTemplateMapper.selectPage(
                new Page<>(pageNum, pageSize),
                lambdaQueryWrapper
        );

        List<ResumeTemplatePageVO> voList = resumeTemplatePage.getRecords().stream()
                .map(resumeTemplate -> {
                    ResumeTemplatePageVO infoVO = new ResumeTemplatePageVO();
                    BeanUtils.copyProperties(resumeTemplate, infoVO);
                    infoVO.setIndustryMapIndustryName(
                            industryMapService.findIndustryMapById(resumeTemplate.getResumeTemplateIndustry()).getIndustryMapIndustryName()
                    );
                    return infoVO;
                })
                .collect(Collectors.toList());

        Page<ResumeTemplatePageVO> voPage = new Page<>(pageNum, pageSize);
        voPage.setRecords(voList);
        voPage.setSize(resumeTemplatePage.getSize());
        voPage.setCurrent(resumeTemplatePage.getCurrent());
        voPage.setPages(resumeTemplatePage.getPages());
        voPage.setTotal(resumeTemplatePage.getTotal());

        if (!hasQueryCondition) {
            redisCacheUtil.set(cacheKey, voPage, ResumeTemplateCacheKey.PAGE_TTL, TimeUnit.MINUTES);
        }

        return voPage;
    }
    @Override
    public List<ResumeTemplatePageVO> findAllResumeTemplate() {
        List<ResumeTemplate> resumeTemplates = resumeTemplateMapper.selectList(null);
        List<ResumeTemplatePageVO> resumeTemplatePageVOs = BeanUtil.copyToList(resumeTemplates, ResumeTemplatePageVO.class);
        return resumeTemplatePageVOs;
    }
}

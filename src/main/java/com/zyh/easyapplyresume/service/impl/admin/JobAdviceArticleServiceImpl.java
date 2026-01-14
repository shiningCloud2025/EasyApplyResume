package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.JobAdviceArticleMapper;
import com.zyh.easyapplyresume.model.form.admin.JobAdviceArticleForm;
import com.zyh.easyapplyresume.model.pojo.admin.JobAdviceArticle;
import com.zyh.easyapplyresume.model.query.admin.JobAdviceArticleQuery;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticlePageVO;
import com.zyh.easyapplyresume.model.vo.admin.PermissionPageVO;
import com.zyh.easyapplyresume.redis.constant.common.JobAdviceArticleCacheKey;
import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.util.CacheInvalidatePublisher;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.JobAdviceArticleService;
import com.zyh.easyapplyresume.utils.adminvalidator.JobAdviceArticleFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
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
public class JobAdviceArticleServiceImpl implements JobAdviceArticleService {
    @Autowired
    private JobAdviceArticleMapper jobAdviceArticleMapper;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Autowired
    private CacheInvalidatePublisher cachePublisher;

    @Override
    public Integer addJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm) {
        JobAdviceArticleFormValidator.validateForAdd(jobAdviceArticleForm);
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        BeanUtil.copyProperties(jobAdviceArticleForm, jobAdviceArticle);
        jobAdviceArticle.setJobAdviceArticlePublishedStatus(1);
        jobAdviceArticle.setJobAdviceArticlePublishedTime(new DateTime());
        jobAdviceArticle.setJobAdviceArticleUpdatedTime(new DateTime());
        int result = jobAdviceArticleMapper.insert(jobAdviceArticle);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    cachePublisher.publishInvalidate(
                        JobAdviceArticleCacheKey.ALL_PATTERN,
                        CacheOperationType.ADD
                    );
                }
            }
        );
        
        return result;
    }

    @Override
    public Integer updateJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm) {
        JobAdviceArticleFormValidator.validateForUpdate(jobAdviceArticleForm);
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        BeanUtil.copyProperties(jobAdviceArticleForm, jobAdviceArticle);
        jobAdviceArticle.setJobAdviceArticleUpdatedTime(new DateTime());
        int result = jobAdviceArticleMapper.updateById(jobAdviceArticle);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    cachePublisher.publishInvalidate(
                        JobAdviceArticleCacheKey.ALL_PATTERN,
                        CacheOperationType.UPDATE
                    );
                }
            }
        );
        
        return result;
    }

    @Override
    public Integer deleteJobAdviceArticle(Integer jobAdviceArticleId) {
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        jobAdviceArticle.setJobAdviceArticleId(jobAdviceArticleId);
        jobAdviceArticle.setDeleted(1);
        int result = jobAdviceArticleMapper.updateById(jobAdviceArticle);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    cachePublisher.publishInvalidate(
                        JobAdviceArticleCacheKey.ALL_PATTERN,
                        CacheOperationType.DELETE
                    );
                }
            }
        );
        
        return result;
    }

    @Override
    public JobAdviceArticleInfoVO getJobAdviceArticleInfo(Integer jobAdviceArticleId) {
        String cacheKey = JobAdviceArticleCacheKey.GET_PREFIX + "_" + jobAdviceArticleId;
        
        Object cached = redisCacheUtil.get(cacheKey);
        if (cached != null) {
            return (JobAdviceArticleInfoVO) cached;
        }
        
        LambdaQueryWrapper<JobAdviceArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(JobAdviceArticle::getJobAdviceArticleId, jobAdviceArticleId);
        lambdaQueryWrapper.eq(JobAdviceArticle::getDeleted, 0);
        JobAdviceArticle jobAdviceArticle = jobAdviceArticleMapper.selectOne(lambdaQueryWrapper);
        JobAdviceArticleInfoVO result = BeanUtil.copyProperties(jobAdviceArticle, JobAdviceArticleInfoVO.class);
        
        redisCacheUtil.set(cacheKey, result, JobAdviceArticleCacheKey.GET_TTL, TimeUnit.MINUTES);
        
        return result;
    }

    @Override
    public Page<JobAdviceArticlePageVO> getJobAdviceArticlePage(int size, int page, JobAdviceArticleQuery jobAdviceArticleQuery) {
        String cacheKey = JobAdviceArticleCacheKey.PAGE_PREFIX 
                        + "_" + page 
                        + "_" + size 
                        + "_" + (jobAdviceArticleQuery != null ? jobAdviceArticleQuery.hashCode() : 0);
        
        Object cached = redisCacheUtil.get(cacheKey);
        if (cached != null) {
            return (Page<JobAdviceArticlePageVO>) cached;
        }
        
        // 1. 构建 LambdaQueryWrapper（指定 JobAdviceArticle 数据库实体类）
        LambdaQueryWrapper<JobAdviceArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(JobAdviceArticle::getDeleted, 0);
        // 2. 判空过滤：查询条件不为空时，添加对应模糊查询（适配所有查询字段）
        if (jobAdviceArticleQuery != null) {
            // 标题：不为空且非空串 → 模糊查询
            if (jobAdviceArticleQuery.getJobAdviceArticleTitle() != null && !jobAdviceArticleQuery.getJobAdviceArticleTitle().trim().isEmpty()) {
                lambdaQueryWrapper.like(JobAdviceArticle::getJobAdviceArticleTitle, jobAdviceArticleQuery.getJobAdviceArticleTitle().trim());
            }
            // 分类：不为空且非空串 → 模糊查询
            if (jobAdviceArticleQuery.getJobAdviceArticleCategory() != null && !jobAdviceArticleQuery.getJobAdviceArticleCategory().trim().isEmpty()) {
                lambdaQueryWrapper.like(JobAdviceArticle::getJobAdviceArticleCategory, jobAdviceArticleQuery.getJobAdviceArticleCategory().trim());
            }
            // 标签：不为空且非空串 → 模糊查询
            if (jobAdviceArticleQuery.getJobAdviceArticleTags() != null && !jobAdviceArticleQuery.getJobAdviceArticleTags().trim().isEmpty()) {
                lambdaQueryWrapper.like(JobAdviceArticle::getJobAdviceArticleTags, jobAdviceArticleQuery.getJobAdviceArticleTags().trim());
            }
            // 作者名称：不为空且非空串 → 模糊查询
            if (jobAdviceArticleQuery.getJobAdviceArticleAuthorName() != null && !jobAdviceArticleQuery.getJobAdviceArticleAuthorName().trim().isEmpty()) {
                lambdaQueryWrapper.like(JobAdviceArticle::getJobAdviceArticleAuthorName, jobAdviceArticleQuery.getJobAdviceArticleAuthorName().trim());
            }
        }

        // 3. 调用 Mapper 分页查询（依赖 JobAdviceArticleMapper 继承 MyBatis-Plus BaseMapper）
        Page<JobAdviceArticle> jobAdviceArticlePage = jobAdviceArticleMapper.selectPage(
                new Page<>(page, size),  // 分页参数：当前页（page）、每页条数（size）
                lambdaQueryWrapper        // 多条件模糊查询组合
        );

        // 4. 实体转换：JobAdviceArticle（数据库实体）→ JobAdviceArticlePageVO（前端返回VO）
        List<JobAdviceArticlePageVO> voList = jobAdviceArticlePage.getRecords().stream()
                .map(article -> {
                    JobAdviceArticlePageVO pageVO = new JobAdviceArticlePageVO();
                    // 复制同名字段（要求：VO与实体字段名一致、数据类型一致，如 jobAdviceArticleId、jobAdviceArticleTitle 等）
                    BeanUtils.copyProperties(article, pageVO);
                    // 若 VO 与实体有字段差异，需手动补充映射（示例如下，根据实际 VO 结构调整）
                    // 示例1：日期字段格式化 → pageVO.setCreateTimeStr(DateUtil.format(article.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    // 示例2：字段名不一致映射 → pageVO.setArticleTitle(article.getJobAdviceArticleTitle());
                    return pageVO;
                })
                .collect(Collectors.toList());

        // 5. 封装 VO 分页对象（复制原始分页的所有分页参数，保证分页逻辑正确）
        Page<JobAdviceArticlePageVO> jobAdviceArticlePageVOPage = new Page<>();
        jobAdviceArticlePageVOPage.setRecords(voList);         // 核心：转换后的 VO 列表
        jobAdviceArticlePageVOPage.setCurrent(jobAdviceArticlePage.getCurrent()); // 当前页码
        jobAdviceArticlePageVOPage.setSize(jobAdviceArticlePage.getSize());       // 每页条数
        jobAdviceArticlePageVOPage.setTotal(jobAdviceArticlePage.getTotal());     // 总数据量（关键：计算总页数用）
        jobAdviceArticlePageVOPage.setPages(jobAdviceArticlePage.getPages());     // 总页数
        
        redisCacheUtil.set(cacheKey, jobAdviceArticlePageVOPage, JobAdviceArticleCacheKey.PAGE_TTL, TimeUnit.MINUTES);
        
        return jobAdviceArticlePageVOPage;
    }

    @Override
    public List<JobAdviceArticleInfoVO> getAllJobAdviceArticle() {
        List<JobAdviceArticle> jobAdviceArticleList = jobAdviceArticleMapper.selectList(null);
        List<JobAdviceArticleInfoVO> jobAdviceArticleInfoVOs = BeanUtil.copyToList(jobAdviceArticleList, JobAdviceArticleInfoVO.class);
        return jobAdviceArticleInfoVOs;
    }
}

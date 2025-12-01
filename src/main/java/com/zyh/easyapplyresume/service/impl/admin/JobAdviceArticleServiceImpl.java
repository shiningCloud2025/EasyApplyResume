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
import com.zyh.easyapplyresume.service.admin.JobAdviceArticleService;
import com.zyh.easyapplyresume.utils.adminvalidator.JobAdviceArticleFormValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class JobAdviceArticleServiceImpl implements JobAdviceArticleService {
    @Autowired
    private JobAdviceArticleMapper jobAdviceArticleMapper;

    @Override
    public Integer addJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm) {
        JobAdviceArticleFormValidator.validateForAdd(jobAdviceArticleForm);
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        BeanUtil.copyProperties(jobAdviceArticleForm, jobAdviceArticle);
        jobAdviceArticle.setJobAdviceArticlePublishedStatus(1);
        jobAdviceArticle.setJobAdviceArticlePublishedTime(new DateTime());
        jobAdviceArticle.setJobAdviceArticleUpdatedTime(new DateTime());
        return  jobAdviceArticleMapper.insert(jobAdviceArticle);
    }

    @Override
    public Integer updateJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm) {
        JobAdviceArticleFormValidator.validateForUpdate(jobAdviceArticleForm);
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        BeanUtil.copyProperties(jobAdviceArticleForm, jobAdviceArticle);
        jobAdviceArticle.setJobAdviceArticleUpdatedTime(new DateTime());
        return jobAdviceArticleMapper.updateById(jobAdviceArticle);
    }

    @Override
    public Integer deleteJobAdviceArticle(Integer jobAdviceArticleId) {
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        jobAdviceArticle.setDeleted(1);
        return jobAdviceArticleMapper.updateById(jobAdviceArticle);
    }

    @Override
    public JobAdviceArticleInfoVO getJobAdviceArticleInfo(Integer jobAdviceArticleId) {
        LambdaQueryWrapper<JobAdviceArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(JobAdviceArticle::getJobAdviceArticleId, jobAdviceArticleId);
        lambdaQueryWrapper.eq(JobAdviceArticle::getDeleted, 0);
        JobAdviceArticle jobAdviceArticle = jobAdviceArticleMapper.selectOne(lambdaQueryWrapper);
        return BeanUtil.copyProperties(jobAdviceArticle, JobAdviceArticleInfoVO.class);
    }

    @Override
    public List<JobAdviceArticlePageVO> getJobAdviceArticlePage(int size, int page, JobAdviceArticleQuery jobAdviceArticleQuery) {
        // 1. 构建 LambdaQueryWrapper（指定 JobAdviceArticle 数据库实体类）
        LambdaQueryWrapper<JobAdviceArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(JobAdviceArticle::getDeleted, 0);
        // 2. 判空过滤：查询条件不为空时，添加对应模糊查询（适配所有查询字段）
        if (jobAdviceArticleQuery != null) {
            // 标题：不为空且非空串 → 模糊查询
            if (jobAdviceArticleQuery.getJobAdviceArticleTitle() != null && !jobAdviceArticleQuery.getJobAdviceArticleTitle().trim().isEmpty()) {
                lambdaQueryWrapper.like(JobAdviceArticle::getJobAdviceArticleTitle, jobAdviceArticleQuery.getJobAdviceArticleTitle().trim());
            }
            // 正文：不为空且非空串 → 模糊查询
            if (jobAdviceArticleQuery.getJobAdviceArticleContent() != null && !jobAdviceArticleQuery.getJobAdviceArticleContent().trim().isEmpty()) {
                lambdaQueryWrapper.like(JobAdviceArticle::getJobAdviceArticleContent, jobAdviceArticleQuery.getJobAdviceArticleContent().trim());
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

        // 5. 返回分页后的 VO 列表（无数据时返回空列表，避免空指针）
        return voList != null ? voList : Collections.emptyList();
    }

    @Override
    public List<JobAdviceArticleInfoVO> getAllJobAdviceArticle() {
        List<JobAdviceArticle> jobAdviceArticleList = jobAdviceArticleMapper.selectList(null);
        List<JobAdviceArticleInfoVO> jobAdviceArticleInfoVOs = BeanUtil.copyToList(jobAdviceArticleList, JobAdviceArticleInfoVO.class);
        return jobAdviceArticleInfoVOs;
    }
}

package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.zyh.easyapplyresume.mapper.admin.JobAdviceArticleMapper;
import com.zyh.easyapplyresume.model.form.admin.JobAdviceArticleForm;
import com.zyh.easyapplyresume.model.pojo.admin.JobAdviceArticle;
import com.zyh.easyapplyresume.model.query.admin.JobAdviceArticleQuery;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticlePageVO;
import com.zyh.easyapplyresume.service.admin.JobAdviceArticleService;
import com.zyh.easyapplyresume.utils.Validator.JobAdviceArticleFormValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class JobAdviceArticleServiceImpl implements JobAdviceArticleService {
    @Autowired
    private JobAdviceArticleMapper jobAdviceArticleMapper;

    @Override
    public void addJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm) {
        JobAdviceArticleFormValidator.validateForAdd(jobAdviceArticleForm);
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        BeanUtil.copyProperties(jobAdviceArticleForm, jobAdviceArticle);
        jobAdviceArticle.setJobAdviceArticlePublishedStatus(1);
        jobAdviceArticle.setJobAdviceArticlePublishedTime(new DateTime());
        jobAdviceArticle.setJobAdviceArticleUpdatedTime(new DateTime());
        jobAdviceArticleMapper.insert(jobAdviceArticle);
    }

    @Override
    public void updateJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm) {
        JobAdviceArticleFormValidator.validateForUpdate(jobAdviceArticleForm);
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        BeanUtil.copyProperties(jobAdviceArticleForm, jobAdviceArticle);
        jobAdviceArticle.setJobAdviceArticleUpdatedTime(new DateTime());
        jobAdviceArticleMapper.updateById(jobAdviceArticle);
    }

    @Override
    public void deleteJobAdviceArticle(Integer jobAdviceArticleId) {
        JobAdviceArticle jobAdviceArticle = new JobAdviceArticle();
        jobAdviceArticle.setDeleted(1);
        jobAdviceArticleMapper.updateById(jobAdviceArticle);
    }

    @Override
    public JobAdviceArticleInfoVO getJobAdviceArticleInfo(Integer jobAdviceArticleId) {
        JobAdviceArticle jobAdviceArticle = jobAdviceArticleMapper.selectById(jobAdviceArticleId);
        return BeanUtil.copyProperties(jobAdviceArticle, JobAdviceArticleInfoVO.class);
    }

    @Override
    public List<JobAdviceArticlePageVO> getJobAdviceArticlePage(int size, int page, JobAdviceArticleQuery jobAdviceArticleQuery) {
        return Collections.emptyList();
    }

    @Override
    public List<JobAdviceArticleInfoVO> getAllJobAdviceArticle() {
        return Collections.emptyList();
    }
}

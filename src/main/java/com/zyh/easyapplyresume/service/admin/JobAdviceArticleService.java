package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.JobAdviceArticleForm;
import com.zyh.easyapplyresume.model.query.admin.JobAdviceArticleQuery;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticlePageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface JobAdviceArticleService {
    /**
     * 新增求职攻略文章
     * @param jobAdviceArticleForm
     */
    public Integer addJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm);
    /**
     * 修改求职攻略文章
     * @param jobAdviceArticleForm
     */
    public Integer updateJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm);
    /**
     * 删除求职攻略文章
     * @param jobAdviceArticleId
     */
    public Integer deleteJobAdviceArticle(Integer jobAdviceArticleId);
    /**
     * 查询求职攻略文章信息
     * @param jobAdviceArticleId
     * @return
     */
    public JobAdviceArticleInfoVO getJobAdviceArticleInfo(Integer jobAdviceArticleId);
    /**
     * 分页查询求职攻略文章信息
     * @param size
     * @param page
     * @param jobAdviceArticleQuery
     * @return
     */
    public List<JobAdviceArticlePageVO> getJobAdviceArticlePage(int size, int page, JobAdviceArticleQuery jobAdviceArticleQuery);
    /**
     * 获取所有求职攻略文章信息
     * @return
     */
    public List<JobAdviceArticleInfoVO> getAllJobAdviceArticle();

}

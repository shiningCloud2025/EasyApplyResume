package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.model.form.admin.JobAdviceArticleForm;
import com.zyh.easyapplyresume.service.admin.JobAdviceArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 求职攻略文章控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/jobAdviceArticle")
@Tag(name="求职攻略文章控制器-管理端")
public class JobAdviceArticleController {
    @Autowired
    private JobAdviceArticleService jobAdviceArticleService;

    @Operation(summary = "新增求职攻略文章")
    @RequestMapping("/addJobAdviceArticle")
    public Integer addJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm){
        jobAdviceArticleService.addJobAdviceArticle(jobAdviceArticleForm);
    }

    @Operation(summary = "修改求职攻略文章")
    @RequestMapping("/updateJobAdviceArticle")
    public Integer updateJobAdviceArticle(JobAdviceArticleForm jobAdviceArticleForm){
        jobAdviceArticleService.updateJobAdviceArticle(jobAdviceArticleForm);
    }

    @Operation(summary = "删除求职攻略文章")
    @RequestMapping("/deleteJobAdviceArticle")
    public Integer deleteJobAdviceArticle(Integer jobAdviceArticleId){
        jobAdviceArticleService.deleteJobAdviceArticle(jobAdviceArticleId);
    }

    @Operation(summary = "查询求职攻略文章")
    @RequestMapping("/getJobAdviceArticleInfo")
    public JobAdviceArticleInfoVO getJobAdviceArticleInfo(Integer jobAdviceArticleId){
        return jobAdviceArticleService.getJobAdviceArticleInfo(jobAdviceArticleId);
    }

    @Operation(summary = "分页查询")
    @RequestMapping("/getJobAdviceArticlePage")
    public List<JobAdviceArticlePageVO> getJobAdviceArticlePage(int size, int page, JobAdviceArticleQuery jobAdviceArticleQuery){
        return jobAdviceArticleService.getJobAdviceArticlePage(size,page,jobAdviceArticleQuery);
    }

    @Operation(summary = "查询所有求职攻略文章")
    @RequestMapping("/getAllJobAdviceArticle")
    public List<JobAdviceArticleInfoVO> getAllJobAdviceArticle(){
        return jobAdviceArticleService.getAllJobAdviceArticle();
    }



}

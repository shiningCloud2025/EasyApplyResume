package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.JobAdviceArticleForm;
import com.zyh.easyapplyresume.model.query.admin.JobAdviceArticleQuery;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticlePageVO;
import com.zyh.easyapplyresume.service.admin.JobAdviceArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/addJobAdviceArticle")
    public BaseResult <Integer> addJobAdviceArticle(@RequestBody JobAdviceArticleForm jobAdviceArticleForm){
        return BaseResult.ok(jobAdviceArticleService.addJobAdviceArticle(jobAdviceArticleForm));
    }

    @Operation(summary = "修改求职攻略文章")
    @PostMapping("/updateJobAdviceArticle")
    public BaseResult <Integer> updateJobAdviceArticle(@RequestBody JobAdviceArticleForm jobAdviceArticleForm){
        return BaseResult.ok(jobAdviceArticleService.updateJobAdviceArticle(jobAdviceArticleForm));
    }

    @Operation(summary = "删除求职攻略文章")
    @DeleteMapping("/deleteJobAdviceArticle")
    public Integer deleteJobAdviceArticle(@RequestParam(required = true,value = "jobAdviceArticleId") Integer jobAdviceArticleId){
        return jobAdviceArticleService.deleteJobAdviceArticle(jobAdviceArticleId);
    }

    @Operation(summary = "查询求职攻略文章")
    @GetMapping("/getJobAdviceArticleInfo")
    public JobAdviceArticleInfoVO getJobAdviceArticleInfo(@RequestParam(required = true,value = "jobAdviceArticleId") Integer jobAdviceArticleId){
        return jobAdviceArticleService.getJobAdviceArticleInfo(jobAdviceArticleId);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/getJobAdviceArticlePage")
    public BaseResult<List<JobAdviceArticlePageVO>> getJobAdviceArticlePage(@RequestParam (required = false,value = "size",defaultValue = "10") int size,
                                                                @RequestParam (required = false,value = "page",defaultValue = "1") int page,
                                                                @RequestBody JobAdviceArticleQuery jobAdviceArticleQuery){
        return BaseResult.ok(jobAdviceArticleService.getJobAdviceArticlePage(size,page,jobAdviceArticleQuery));
    }

    @Operation(summary = "查询所有求职攻略文章")
    @GetMapping("/getAllJobAdviceArticle")
    public BaseResult<List<JobAdviceArticleInfoVO>> getAllJobAdviceArticle(){
        return BaseResult.ok(jobAdviceArticleService.getAllJobAdviceArticle());
    }

}

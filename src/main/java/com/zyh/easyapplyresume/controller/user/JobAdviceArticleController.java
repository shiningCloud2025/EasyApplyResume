package com.zyh.easyapplyresume.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.admin.JobAdviceArticleQuery;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.JobAdviceArticlePageVO;
import com.zyh.easyapplyresume.service.admin.JobAdviceArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 求职攻略控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/jobAdviceArticle")
@Tag(name = "求职攻略控制器-用户端")
public class JobAdviceArticleController {
    @Autowired
    private JobAdviceArticleService jobAdviceArticleService;

    @GetMapping("/getJobAdviceArticlePage")
    @Operation(summary = "分页查询求职攻略信息")
    public BaseResult<Page<JobAdviceArticlePageVO>> getJobAdviceArticlePage(@RequestParam(required = false, defaultValue = "10") int size,
                                                                            @RequestParam(required = false, defaultValue = "1") int page,
                                                                            @RequestBody JobAdviceArticleQuery jobAdviceArticleQuery) {
        return BaseResult.ok(jobAdviceArticleService.getJobAdviceArticlePage(size, page, jobAdviceArticleQuery));
    }

    @GetMapping("/getJobAdviceArticleInfo")
    @Operation(summary = "查询求职攻略信息")
    public BaseResult<JobAdviceArticleInfoVO> getJobAdviceArticleInfo(@RequestParam(required = true,value = "jobAdviceArticleId") Integer jobAdviceArticleId) {
        return BaseResult.ok(jobAdviceArticleService.getJobAdviceArticleInfo(jobAdviceArticleId));
    }



}

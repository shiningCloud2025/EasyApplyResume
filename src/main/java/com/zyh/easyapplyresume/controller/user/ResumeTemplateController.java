package com.zyh.easyapplyresume.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.admin.ResumeTemplateQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplatePageVO;
import com.zyh.easyapplyresume.service.admin.ResumeTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 简历模板控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/resumeTemplate")
@Tag(name = "简历模板控制器-用户端")
public class ResumeTemplateController {

    @Autowired
    private ResumeTemplateService resumeTemplateService;

    @GetMapping("/findResumeTemplateByPage")
    @Operation(summary = "分页查询简历模板信息")
    public BaseResult<Page<ResumeTemplatePageVO>> findResumeTemplateByPage(@RequestParam(required = true, defaultValue = "10") Integer pageNum,
                                                                           @RequestParam(required = true, defaultValue = "1") Integer pageSize,
                                                                           @RequestBody ResumeTemplateQuery resumeTemplateQuery) {
        return BaseResult.ok(resumeTemplateService.findResumeTemplateByPage(pageNum, pageSize, resumeTemplateQuery));
    }

    @GetMapping("/findResumeTemplateById")
    @Operation(summary = "查询简历模板信息")
    public BaseResult<ResumeTemplateInfoVO> findResumeTemplateById(@RequestParam(required = true, value = "resumeTemplateId") Integer resumeTemplateId) {
        return BaseResult.ok(resumeTemplateService.findResumeTemplateById(resumeTemplateId));
    }
}

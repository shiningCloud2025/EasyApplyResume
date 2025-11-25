package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.ResumeTemplateForm;
import com.zyh.easyapplyresume.model.query.admin.ResumeTemplateQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplatePageVO;
import com.zyh.easyapplyresume.service.admin.ResumeTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 简历模版控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/resumeTemplate")
@Tag(name="简历模版控制器-管理端")
public class ResumeTemplateController {

    @Autowired
    private ResumeTemplateService resumeTemplateService;

    @Operation(summary = "新增简历模版")
    @RequestMapping("/addResumeTemplate")
    public BaseResult<Integer> addResumeTemplate(@RequestBody ResumeTemplateForm resumeTemplateForm){
        return BaseResult.ok(resumeTemplateService.addResumeTemplate(resumeTemplateForm));
    }

    @Operation(summary = "修改简历模版")
    @RequestMapping("/updateResumeTemplate")
    public BaseResult<Integer> updateResumeTemplate(@RequestBody ResumeTemplateForm resumeTemplateForm){
        return BaseResult.ok(resumeTemplateService.updateResumeTemplate(resumeTemplateForm));
    }

    @Operation(summary = "删除简历模版")
    @RequestMapping("/deleteResumeTemplate")
    public BaseResult<Integer> deleteResumeTemplate(@RequestParam(required = true,value = "resumeTemplateId") Integer resumeTemplateId){
        return BaseResult.ok(resumeTemplateService.deleteResumeTemplate(resumeTemplateId));
    }

    @Operation(summary = "根据id查询简历模版")
    @RequestMapping("/findResumeTemplateById")
    public BaseResult<ResumeTemplateInfoVO> findResumeTemplateById(@RequestParam(required = true,value = "resumeTemplateId") Integer resumeTemplateId){
        return BaseResult.ok(resumeTemplateService.findResumeTemplateById(resumeTemplateId));
    }

    @Operation(summary = "分页查询简历模版")
    @RequestMapping("/findResumeTemplateByPage")
    public BaseResult<Page<ResumeTemplatePageVO>> findResumeTemplateByPage(@RequestParam(required = true,value = "pageNum",defaultValue = "1") Integer pageNum,
                                                                           @RequestParam(required = true,value = "pageSize",defaultValue = "10") Integer pageSize,
                                                                           @RequestBody ResumeTemplateQuery resumeTemplateQuery){
        return BaseResult.ok(resumeTemplateService.findResumeTemplateByPage(pageNum,pageSize,resumeTemplateQuery));
    }

    @Operation(summary = "查询所有简历模版")
    @RequestMapping("/findAllResumeTemplate")
    public BaseResult<List<ResumeTemplatePageVO>> findAllResumeTemplate(){
        return BaseResult.ok(resumeTemplateService.findAllResumeTemplate());
    }



}

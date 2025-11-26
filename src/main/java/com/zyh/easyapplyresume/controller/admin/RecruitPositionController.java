package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.RecruitPositionForm;
import com.zyh.easyapplyresume.model.query.admin.RecruitPositionQuery;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionPageVO;
import com.zyh.easyapplyresume.service.admin.RecruitPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 招聘岗位控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/recruitPosition")
@Tag(name="招聘岗位控制器-管理端")
public class RecruitPositionController {
    @Autowired
    private RecruitPositionService recruitPositionService;

    @Operation(summary = "新增招聘岗位")
    @PostMapping("/addRecruitPosition")
    public BaseResult<?> addRecruitPosition(@RequestBody RecruitPositionForm recruitPositionForm){
        return BaseResult.ok(recruitPositionService.addRecruitPosition(recruitPositionForm));
    }
    @Operation(summary = "修改招聘岗位")
    @PostMapping("/updateRecruitPosition")
    public  BaseResult<?> updateRecruitPosition(@RequestBody RecruitPositionForm recruitPositionForm){
        return BaseResult.ok(recruitPositionService.updateRecruitPosition(recruitPositionForm));
    }

    @Operation(summary = "删除招聘岗位")
    @DeleteMapping("/deleteRecruitPosition")
    public  BaseResult<?> deleteRecruitPosition(@RequestParam(required = true,value = "recruitPositionId") Integer recruitPositionId){
        return BaseResult.ok(recruitPositionService.deleteRecruitPosition(recruitPositionId));
    }

    @Operation(summary = "查看招聘岗位")
    @GetMapping("/queryRecruitPosition")
    public BaseResult<RecruitPositionInfoVO> queryRecruitPosition(@RequestParam(required = true,value = "recruitPositionId") Integer recruitPositionId){
        return BaseResult.ok(recruitPositionService.queryRecruitPosition(recruitPositionId));
    }

    @Operation(summary = "分页查询招聘岗位")
    @PostMapping("/queryRecruitPositionPage")
    public BaseResult<Page<RecruitPositionPageVO>> queryRecruitPositionPage(@RequestParam(required = false,value = "pageNum",defaultValue = "1") Integer pageNum,
                                                                @RequestParam(required = false,value = "pageSize",defaultValue = "10") Integer pageSize,
                                                                @RequestBody RecruitPositionQuery recruitPositionQuery){
        return BaseResult.ok(recruitPositionService.queryRecruitPositionPage(pageNum,pageSize,recruitPositionQuery));
    }

    @Operation(summary = "查看所有招聘岗位")
    @GetMapping("/queryAllRecruitPositionPage")
    public BaseResult<List<RecruitPositionInfoVO>> queryAllRecruitPositionPage(){
        return BaseResult.ok(recruitPositionService.queryAllRecruitPositionPage());
    }
}


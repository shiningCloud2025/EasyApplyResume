package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionSecondCategoryForm;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionSecondCategoryQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionSecondCategoryInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionSecondCategoryPageVO;
import com.zyh.easyapplyresume.service.admin.AdminQuestionSecondCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题库小类控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/questionSecondCategory")
@Tag(name = "题库小类控制器-管理端")
public class AdminQuestionSecondCategoryController {

    @Autowired
    private AdminQuestionSecondCategoryService adminQuestionSecondCategoryService;

    @Operation(summary = "新增题库小类")
    @PostMapping("/addQuestionSecondCategory")
    @PreAuthorize("hasAuthority('/admin/questionSecondCategory/addQuestionSecondCategory')")
    public BaseResult<Integer> addQuestionSecondCategory(@RequestBody AdminQuestionSecondCategoryForm form) {
        return BaseResult.ok(adminQuestionSecondCategoryService.addQuestionSecondCategory(form));
    }

    @Operation(summary = "修改题库小类")
    @PostMapping("/updateQuestionSecondCategory")
    @PreAuthorize("hasAuthority('/admin/questionSecondCategory/updateQuestionSecondCategory')")
    public BaseResult<Integer> updateQuestionSecondCategory(@RequestBody AdminQuestionSecondCategoryForm form) {
        return BaseResult.ok(adminQuestionSecondCategoryService.updateQuestionSecondCategory(form));
    }

    @Operation(summary = "删除题库小类")
    @DeleteMapping("/deleteQuestionSecondCategory")
    @PreAuthorize("hasAuthority('/admin/questionSecondCategory/deleteQuestionSecondCategory')")
    public BaseResult<Integer> deleteQuestionSecondCategory(
            @RequestParam(required = true, value = "questionSecondCategoryId") Integer questionSecondCategoryId) {
        return BaseResult.ok(adminQuestionSecondCategoryService.deleteQuestionSecondCategory(questionSecondCategoryId));
    }

    @Operation(summary = "查询题库小类详情")
    @GetMapping("/findQuestionSecondCategoryById")
    @PreAuthorize("hasAuthority('/admin/questionSecondCategory/findQuestionSecondCategoryById')")
    public BaseResult<AdminQuestionSecondCategoryInfoVO> findQuestionSecondCategoryById(
            @RequestParam(required = true, value = "questionSecondCategoryId") Integer questionSecondCategoryId) {
        return BaseResult.ok(adminQuestionSecondCategoryService.findQuestionSecondCategoryById(questionSecondCategoryId));
    }

    @Operation(summary = "分页查询题库小类")
    @PostMapping("/findQuestionSecondCategoryByPage")
    @PreAuthorize("hasAuthority('/admin/questionSecondCategory/findQuestionSecondCategoryByPage')")
    public BaseResult<Page<AdminQuestionSecondCategoryPageVO>> findQuestionSecondCategoryByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody AdminQuestionSecondCategoryQuery query) {
        return BaseResult.ok(adminQuestionSecondCategoryService.findQuestionSecondCategoryByPage(pageNum, pageSize, query));
    }

    @Operation(summary = "根据题库大类id查询题库小类")
    @GetMapping("/findQuestionSecondCategoryByFirstCategoryId")
    @PreAuthorize("hasAuthority('/admin/questionSecondCategory/findQuestionSecondCategoryByFirstCategoryId')")
    public BaseResult<List<AdminQuestionSecondCategoryInfoVO>> findQuestionSecondCategoryByFirstCategoryId(
            @RequestParam(required = true, value = "questionFirstCategoryId") Integer questionFirstCategoryId) {
        return BaseResult.ok(adminQuestionSecondCategoryService.findQuestionSecondCategoryByFirstCategoryId(questionFirstCategoryId));
    }

    @Operation(summary = "查询所有题库小类")
    @GetMapping("/findAllQuestionSecondCategory")
    public BaseResult<List<AdminQuestionSecondCategoryInfoVO>> findAllQuestionSecondCategory() {
        return BaseResult.ok(adminQuestionSecondCategoryService.findAllQuestionSecondCategory());
    }
}

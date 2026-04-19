package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionFirstCategoryForm;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionFirstCategoryQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionFirstCategoryInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionFirstCategoryPageVO;
import com.zyh.easyapplyresume.service.admin.AdminQuestionFirstCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 题库大类控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/questionFirstCategory")
@Tag(name = "题库大类控制器-管理端")
public class AdminQuestionFirstCategoryController {

    @Autowired
    private AdminQuestionFirstCategoryService adminQuestionFirstCategoryService;

    @Operation(summary = "新增题库大类")
    @PostMapping("/addQuestionFirstCategory")
    public BaseResult<Integer> addQuestionFirstCategory(@RequestBody AdminQuestionFirstCategoryForm adminQuestionFirstCategoryForm) {
        return BaseResult.ok(adminQuestionFirstCategoryService.addQuestionFirstCategory(adminQuestionFirstCategoryForm));
    }

    @Operation(summary = "修改题库大类")
    @PostMapping("/updateQuestionFirstCategory")
    public BaseResult<Integer> updateQuestionFirstCategory(@RequestBody AdminQuestionFirstCategoryForm adminQuestionFirstCategoryForm) {
        return BaseResult.ok(adminQuestionFirstCategoryService.updateQuestionFirstCategory(adminQuestionFirstCategoryForm));
    }

    @Operation(summary = "删除题库大类")
    @DeleteMapping("/deleteQuestionFirstCategory")
    public BaseResult<Integer> deleteQuestionFirstCategory(
            @RequestParam(required = true, value = "questionFirstCategoryId") Integer questionFirstCategoryId) {
        return BaseResult.ok(adminQuestionFirstCategoryService.deleteQuestionFirstCategory(questionFirstCategoryId));
    }

    @Operation(summary = "查询题库大类详情")
    @GetMapping("/findQuestionFirstCategoryById")
    public BaseResult<AdminQuestionFirstCategoryInfoVO> findQuestionFirstCategoryById(
            @RequestParam(required = true, value = "questionFirstCategoryId") Integer questionFirstCategoryId) {
        return BaseResult.ok(adminQuestionFirstCategoryService.findQuestionFirstCategoryById(questionFirstCategoryId));
    }

    @Operation(summary = "分页查询题库大类")
    @PostMapping("/findQuestionFirstCategoryByPage")
    public BaseResult<Page<AdminQuestionFirstCategoryPageVO>> findQuestionFirstCategoryByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody AdminQuestionFirstCategoryQuery query) {
        return BaseResult.ok(adminQuestionFirstCategoryService.findQuestionFirstCategoryByPage(pageNum, pageSize, query));
    }
}
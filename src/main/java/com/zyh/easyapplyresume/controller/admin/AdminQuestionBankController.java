package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionBankForm;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionBankPageVO;
import com.zyh.easyapplyresume.service.admin.AdminQuestionBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 题库题目控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/questionBank")
@Tag(name = "题库题目控制器-管理端")
public class AdminQuestionBankController {

    @Autowired
    private AdminQuestionBankService adminQuestionBankService;

    @Operation(summary = "新增题库题目")
    @PostMapping("/addQuestionBank")
    @PreAuthorize("hasAuthority('/admin/questionBank/addQuestionBank')")
    public BaseResult<Integer> addQuestionBank(@RequestBody AdminQuestionBankForm form) {
        return BaseResult.ok(adminQuestionBankService.addQuestionBank(form));
    }

    @Operation(summary = "修改题库题目")
    @PostMapping("/updateQuestionBank")
    @PreAuthorize("hasAuthority('/admin/questionBank/updateQuestionBank')")
    public BaseResult<Integer> updateQuestionBank(@RequestBody AdminQuestionBankForm form) {
        return BaseResult.ok(adminQuestionBankService.updateQuestionBank(form));
    }

    @Operation(summary = "删除题库题目")
    @DeleteMapping("/deleteQuestionBank")
    @PreAuthorize("hasAuthority('/admin/questionBank/deleteQuestionBank')")
    public BaseResult<Integer> deleteQuestionBank(
            @RequestParam(required = true, value = "questionBankId") Integer questionBankId) {
        return BaseResult.ok(adminQuestionBankService.deleteQuestionBank(questionBankId));
    }

    @Operation(summary = "查询题库题目详情")
    @GetMapping("/findQuestionBankById")
    @PreAuthorize("hasAuthority('/admin/questionBank/findQuestionBankById')")
    public BaseResult<AdminQuestionBankInfoVO> findQuestionBankById(
            @RequestParam(required = true, value = "questionBankId") Integer questionBankId) {
        return BaseResult.ok(adminQuestionBankService.findQuestionBankById(questionBankId));
    }

    @Operation(summary = "分页查询题库题目")
    @PostMapping("/findQuestionBankByPage")
    @PreAuthorize("hasAuthority('/admin/questionBank/findQuestionBankByPage')")
    public BaseResult<Page<AdminQuestionBankPageVO>> findQuestionBankByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody AdminQuestionBankQuery query) {
        return BaseResult.ok(adminQuestionBankService.findQuestionBankByPage(pageNum, pageSize, query));
    }
}

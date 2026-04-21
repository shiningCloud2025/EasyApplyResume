package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.user.UserFirstCategoryQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.user.UserFirstCategoryQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFirstCategoryQuestionBankPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFirstCategoryQuestionBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-笔试题目控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/firstCategoryQuestionBank")
@Tag(name = "用户-笔试题目控制器-管理端")
public class AdminFirstCategoryQuestionBankController {

    @Autowired
    private AdminFirstCategoryQuestionBankService adminFirstCategoryQuestionBankService;

    @Operation(summary = "删除用户-笔试题目记录")
    @DeleteMapping("/deleteFirstCategoryQuestionBank")
    public BaseResult<Integer> deleteFirstCategoryQuestionBank(
            @RequestParam(required = true, value = "userId") Integer userId,
            @RequestParam(required = true, value = "questionBankId") Integer questionBankId) {
        return BaseResult.ok(adminFirstCategoryQuestionBankService.deleteFirstCategoryQuestionBank(userId, questionBankId));
    }

    @Operation(summary = "查询用户-笔试题目详情")
    @GetMapping("/findFirstCategoryQuestionBankById")
    public BaseResult<UserFirstCategoryQuestionBankInfoVO> findFirstCategoryQuestionBankById(
            @RequestParam(required = true, value = "userId") Integer userId,
            @RequestParam(required = true, value = "questionBankId") Integer questionBankId) {
        return BaseResult.ok(adminFirstCategoryQuestionBankService.findFirstCategoryQuestionBankById(userId, questionBankId));
    }

    @Operation(summary = "分页查询用户-笔试题目")
    @PostMapping("/findFirstCategoryQuestionBankByPage")
    public BaseResult<Page<UserFirstCategoryQuestionBankPageVO>> findFirstCategoryQuestionBankByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody UserFirstCategoryQuestionBankQuery query) {
        return BaseResult.ok(adminFirstCategoryQuestionBankService.findFirstCategoryQuestionBankByPage(pageNum, pageSize, query));
    }
}

package com.zyh.easyapplyresume.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.user.UserQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankPageVO;
import com.zyh.easyapplyresume.service.user.UserQuestionBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 题库题目控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/questionBank")
@Tag(name = "题库题目控制器-用户端")
public class UserQuestionBankController {

    @Autowired
    private UserQuestionBankService userQuestionBankService;

    @Operation(summary = "分页查询用户端题库题目")
    @PostMapping("/findQuestionBankByPage")
    public BaseResult<Page<UserQuestionBankPageVO>> findQuestionBankByPage(
            @RequestParam(required = true, value = "userId") Integer userId,
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody UserQuestionBankQuery query) {
        return BaseResult.ok(userQuestionBankService.findQuestionBankByPage(userId, pageNum, pageSize, query));
    }

    @Operation(summary = "查询用户端题库题目详情")
    @GetMapping("/findQuestionBankById")
    public BaseResult<UserQuestionBankInfoVO> findQuestionBankById(
            @RequestParam(required = true, value = "questionBankId") Integer questionBankId) {
        return BaseResult.ok(userQuestionBankService.findQuestionBankById(questionBankId));
    }
}

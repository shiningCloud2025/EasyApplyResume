package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionSecondCategoryInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminQuestionSecondCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 题库小类控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/questionSecondCategory")
@Tag(name = "题库小类控制器-用户端")
public class UserQuestionSecondCategoryController {

    @Autowired
    private AdminQuestionSecondCategoryService adminQuestionSecondCategoryService;

    @Operation(summary = "根据题库大类id查询题库小类")
    @GetMapping("/findQuestionSecondCategoryByFirstCategoryId")
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

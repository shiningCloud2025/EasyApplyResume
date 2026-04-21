package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionFirstCategoryInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminQuestionFirstCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 题库大类控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/questionFirstCategory")
@Tag(name = "题库大类控制器-用户端")
public class UserQuestionFirstCategoryController {

    @Autowired
    private AdminQuestionFirstCategoryService adminQuestionFirstCategoryService;

    @Operation(summary = "查询所有题库大类")
    @GetMapping("/findAllQuestionFirstCategory")
    public BaseResult<List<AdminQuestionFirstCategoryInfoVO>> findAllQuestionFirstCategory() {
        return BaseResult.ok(adminQuestionFirstCategoryService.findAllQuestionFirstCategory());
    }
}

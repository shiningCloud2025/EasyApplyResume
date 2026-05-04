package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.user.UserUpdateFeedbackForm;
import com.zyh.easyapplyresume.model.query.user.UserFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackPageVO;
import com.zyh.easyapplyresume.service.user.UserFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户反馈信息管理接口-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/userFeedback")
@Tag(name = "用户反馈信息管理接口-管理端")
public class AdminUserFeedbackController {

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Operation(summary = "更新反馈阶段")
    @PostMapping("/updateFeedbackStep")
    @PreAuthorize("hasAuthority('/admin/userFeedback/updateFeedbackStep')")
    public BaseResult<?> updateFeedbackStep(@RequestParam(required = true,value = "feedbackId") Integer feedbackId,
                                            @RequestParam(required = true,value = "OperationCode") Integer OperationCode,
                                            @RequestBody UserUpdateFeedbackForm userUpdateFeedbackForm,
                                            @RequestParam(required = true,value = "operationPersonId") Integer operationPersonId) {
        userFeedbackService.updateFeedbackStep(feedbackId, OperationCode, userUpdateFeedbackForm.getTitle(), userUpdateFeedbackForm.getContent(), operationPersonId);
        return BaseResult.ok();
    }

    @Operation(summary = "查询反馈信息")
    @GetMapping("/findFeedbackById")
    @PreAuthorize("hasAuthority('/admin/userFeedback/findFeedbackById')")
    public BaseResult<UserFeedbackInfoVO> findFeedbackById(@RequestParam(required = true,value = "feedbackId") Integer feedbackId) {
        return BaseResult.ok(userFeedbackService.findFeedbackById(feedbackId));
    }

    @Operation(summary = "分页查询反馈信息")
    @PostMapping("/getFeedbackPage")
    @PreAuthorize("hasAuthority('/admin/userFeedback/getFeedbackPage')")
    public BaseResult<Page<UserFeedbackPageVO>> getFeedbackPage(@RequestParam(required = true,value = "size") Integer size,
                                                                @RequestParam(required = true,value = "page") Integer page,
                                                                @RequestBody UserFeedbackQuery userFeedbackQuery) {
        return BaseResult.ok(userFeedbackService.getFeedbackPage(size, page, userFeedbackQuery));
    }

}

package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminFeedbackForm;
import com.zyh.easyapplyresume.model.form.admin.AdminUpdateFeedbackForm;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 反馈信息管理接口-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/feedback")
@Tag(name = "反馈信息管理接口-管理端")
public class AdminFeedbackController {
    @Autowired
    private AdminFeedbackService adminFeedbackService;

    @Operation(summary = "添加反馈信息")
    @PostMapping("/addFeedback")
    public BaseResult<?> addFeedback(@RequestBody AdminFeedbackForm adminFeedbackForm) {
        adminFeedbackService.addFeedback(adminFeedbackForm);
        return BaseResult.ok();
    }

    @Operation(summary = "更新反馈阶段")
    @PutMapping("/updateFeedbackStep")
    public BaseResult<?> updateFeedbackStep(@RequestParam(required = true,value = "feedbackId") Integer feedbackId,
                                            @RequestParam(required = true,value = "OperationCode") Integer OperationCode,
                                            @RequestBody AdminUpdateFeedbackForm adminUpdateFeedbackForm,
                                            @RequestParam(required = true,value = "operationPersonId") Integer operationPersonId) {
        adminFeedbackService.updateFeedbackStep(feedbackId, OperationCode,adminUpdateFeedbackForm.getTitle(),adminUpdateFeedbackForm.getContent(),operationPersonId);
        return BaseResult.ok();
    }

    @Operation(summary = "查询反馈信息")
    @GetMapping("/findFeedbackById")
    public BaseResult<AdminFeedbackInfoVO> findFeedbackById(@RequestParam(required = true,value = "feedbackId") Integer feedbackId) {
        return BaseResult.ok(adminFeedbackService.findFeedbackById(feedbackId));
    }

    @Operation(summary = "分页查询反馈信息")
    @PostMapping("/getFeedbackPage")
    public BaseResult<Page<AdminFeedbackPageVO>> getFeedbackPage(@RequestParam(required = false,value = "pageNum",defaultValue = "1") Integer pageNum,
                                                                 @RequestParam(required = false,value = "pageSize",defaultValue = "10") Integer pageSize,
                                                                 @RequestBody AdminFeedbackQuery adminFeedbackQuery) {
        return BaseResult.ok(adminFeedbackService.getFeedbackPage(pageNum, pageSize, adminFeedbackQuery));
    }


}

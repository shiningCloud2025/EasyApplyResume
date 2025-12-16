package com.zyh.easyapplyresume.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.user.UserFeedbackForm;
import com.zyh.easyapplyresume.model.form.user.UserUpdateFeedbackForm;
import com.zyh.easyapplyresume.model.query.user.UserFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackPageVO;
import com.zyh.easyapplyresume.service.user.UserFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户反馈信息管理接口-用户端+管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/feedback")
@Tag(name = "用户反馈信息接口-用户端")
public class UserFeedbackController {
    @Autowired
    private UserFeedbackService userFeedbackService;

    @Operation(summary = "添加反馈信息")
    @PostMapping("/addFeedback")
    public BaseResult<?> addFeedback(@RequestBody UserFeedbackForm userFeedbackForm) {
        userFeedbackService.addFeedback(userFeedbackForm);
        return BaseResult.ok();
    }




}

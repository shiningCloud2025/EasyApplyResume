package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.user.UserFeedbackRecordQuery;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackRecordInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackRecordPageVO;
import com.zyh.easyapplyresume.service.user.UserFeedbackRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户反馈记录控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/feedbackRecord")
@Tag(name="用户反馈记录控制器-管理端")
public class UserFeedbackRecordController {

    @Autowired
    private UserFeedbackRecordService userFeedbackRecordService;

    @Operation(summary = "查看用户反馈记录")
    @GetMapping("/findUserFeedbackRecordByFeedbackRecordId")
    public BaseResult<UserFeedbackRecordInfoVO> findUserFeedbackRecordByFeedbackRecordId(@RequestParam(required = true,value = "feedbackRecordId") Integer feedbackRecordId){
        return BaseResult.ok(userFeedbackRecordService.findUserFeedbackRecordByFeedbackRecordId(feedbackRecordId));
    }

    @Operation(summary = "分页查看用户反馈记录")
    @PostMapping("/findUserFeedbackRecordPage")
    public BaseResult<Page<UserFeedbackRecordPageVO>> findUserFeedbackRecordPage(@RequestParam (required = false,value = "pageNum",defaultValue = "1")Integer pageNum,
                                                                                 @RequestParam(required = false,value = "pageSize",defaultValue = "10")  Integer pageSize,
                                                                                 @RequestBody UserFeedbackRecordQuery query){
        return BaseResult.ok(userFeedbackRecordService.findUserFeedbackRecordPage(pageNum,pageSize,query));
                                                                                 }



}

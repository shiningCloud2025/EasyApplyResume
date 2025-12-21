package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackRecordQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackRecordInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackRecordPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFeedbackRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员反馈记录接口-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/adminFeedbackRecord")
@Tag(name = "管理员反馈记录接口-管理端")
public class AdminFeedbackRecordController {

    @Autowired
    private AdminFeedbackRecordService adminFeedbackRecordService;


    @Operation(summary = "管理员反馈记录信息")
    @GetMapping("/findAdminFeedbackRecordByFeedbackRecordId")
    public BaseResult<AdminFeedbackRecordInfoVO> findAdminFeedbackRecordByFeedbackRecordId(@RequestParam(required = true,value = "feedbackRecordId") Integer feedbackRecordId) {
        return BaseResult.ok(adminFeedbackRecordService.findAdminFeedbackRecordByFeedbackRecordId(feedbackRecordId));
    }

    @Operation(summary = "管理员反馈记录分页查询")
    @PostMapping("/findAdminFeedbackRecordPage")
    public BaseResult<Page<AdminFeedbackRecordPageVO>> findAdminFeedbackRecordPage(@RequestParam (required = false,value = "pageNum",defaultValue = "1")Integer pageNum,
                                                                                   @RequestParam(required = false,value = "pageSize",defaultValue = "10")  Integer pageSize,
                                                                                   @RequestBody AdminFeedbackRecordQuery adminFeedbackRecordQuery) {
        return BaseResult.ok(adminFeedbackRecordService.findAdminFeedbackRecordPage(pageNum,pageSize,adminFeedbackRecordQuery));
                                                                                    }
}

package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员反馈记录查询")
public class AdminFeedbackRecordQuery {
    @Schema(description = "管理员反馈记录反馈人")
    private String adminFeedbackRecordName;

    @Schema(description = "反馈记录标题")
    private String adminFeedbackRecordTitle;


    @Schema(description = "反馈处理人姓名")
    private String adminFeedbackRecordApprovalPersonName;
}

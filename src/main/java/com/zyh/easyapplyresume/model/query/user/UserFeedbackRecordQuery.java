package com.zyh.easyapplyresume.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户反馈记录查询")
public class UserFeedbackRecordQuery {
    private String userFeedbackRecordName;

    @Schema(description = "反馈记录标题")
    private String userFeedbackRecordTitle;

    @Schema(description = "反馈处理人姓名")
    private String userFeedbackRecordApprovalPersonName;
}

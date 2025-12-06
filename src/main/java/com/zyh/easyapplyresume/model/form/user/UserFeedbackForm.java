package com.zyh.easyapplyresume.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户反馈提交表单")
public class UserFeedbackForm {
    
    @Schema(description = "反馈标题")
    private String userFeedbackTitle;
    
    @Schema(description = "反馈详细内容")
    private String userFeedbackContent;
    
    @Schema(description = "提交反馈的用户ID")
    private Integer userFeedbackUserId;
}

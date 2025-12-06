package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员反馈提交表单")
public class AdminFeedbackForm {
    
    @Schema(description = "反馈标题")
    private String adminFeedbackTitle;
    
    @Schema(description = "反馈详细内容")
    private String adminFeedbackContent;
    
    @Schema(description = "提交反馈的管理员ID")
    private Integer adminFeedbackAdminId;
}

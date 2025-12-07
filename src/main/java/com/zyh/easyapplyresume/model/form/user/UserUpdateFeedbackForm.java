package com.zyh.easyapplyresume.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户更新反馈信息表单")
public class UserUpdateFeedbackForm {
    @Schema(description = "反馈title")
    private String title;

    @Schema(description = "反馈内容")
    private String content;
}

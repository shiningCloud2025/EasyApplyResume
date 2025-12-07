package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员更新反馈信息表单")
public class AdminUpdateFeedbackForm {

    @Schema(description = "反馈title")
    private String title;

    @Schema(description = "反馈内容")
    private String content;
}

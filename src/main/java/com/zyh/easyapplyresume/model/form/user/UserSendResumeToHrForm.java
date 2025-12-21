package com.zyh.easyapplyresume.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户发送简历给HR表单")
public class UserSendResumeToHrForm {

    @Schema(description = "目标邮箱")
    private String targetEmail;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "简历文件")
    private MultipartFile resumeFile;
}

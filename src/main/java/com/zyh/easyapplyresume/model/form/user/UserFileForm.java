package com.zyh.easyapplyresume.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户文件上传表单")
public class UserFileForm {

    @Schema(description = "上传的文件")
    private MultipartFile file;

    @Schema(description = "用户id")
    private Integer userId;
}

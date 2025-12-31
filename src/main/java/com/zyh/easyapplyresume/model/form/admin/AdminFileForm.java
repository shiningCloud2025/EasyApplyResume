package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author shiningCloud2025
 */
@Data
 @Schema(description = "管理员文件上传表单")
public class AdminFileForm {

     @Schema(description = "上传的文件")
      private MultipartFile file;

     @Schema(description = "管理员id")
     private Integer adminId;
}

package com.zyh.easyapplyresume.model.form.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "权限表单")
public class PermissionForm {
    @Schema(description = "权限id")
    private Integer permissionId;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限URL")
    private String permissionUrl;
}

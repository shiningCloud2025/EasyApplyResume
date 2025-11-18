package com.zyh.easyapplyresume.model.form.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "权限表单")
public class PermissionForm {
    @Schema(description = "权限id")
    private Integer permissionId;
    @NotNull(message = "权限名称不能为空")

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限URL")
    private String permissionUrl;

    @Schema(description = "权限简介")
    private String permissionIntroduce;
}

package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "权限分页信息")
public class PermissionPageVO {
    @Schema(description = "权限id")
    private Integer permissionId;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限URL")
    private String permissionUrl;
}

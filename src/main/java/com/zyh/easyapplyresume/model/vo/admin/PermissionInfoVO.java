package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "权限信息")
public class PermissionInfoVO {
    @Schema(description = "权限id")
    private Integer permissionId;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限URL")
    private String permissionUrl;

    @Schema(description = "权限简介")
    private String permissionIntroduce;

}

package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "角色信息")
public class RoleInfoVO {
    @Schema(description = "角色id")
    private Integer roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色介绍")
    private String roleIntroduce;

    @Schema(description = "角色拥有的权限")
    private List<PermissionInfoVO> permissionInfoVOS;
}

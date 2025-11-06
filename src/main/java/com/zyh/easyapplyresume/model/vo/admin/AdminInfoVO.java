package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员信息")
public class AdminInfoVO {
    @Schema(description = "管理员id")
    private Integer adminId;

    @Schema(description = "管理员名称")
    private String adminUsername;

    @Schema(description = "管理员邮箱")
    private String adminEmail;

    @Schema(description = "管理员手机")
    private String adminPhone;

    @Schema(description = "管理员头像")
    private String adminImage;

    @Schema(description = "管理员介绍")
    private String adminIntroduce;

    @Schema(description = "管理员状态")
    private Integer adminState;

    @Schema(description = "管理员最近登录时间")
    private Date adminLoginTime;

    @Schema(description = "角色id")
    private Integer roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色介绍")
    private String roleIntroduce;

    @Schema(description = "权限id")
    private Integer permissionId;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限url")
    private String permissionUrl;

}

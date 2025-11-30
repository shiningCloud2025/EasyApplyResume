package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    @Schema(description = "管理员拥有的角色")
    private List<RoleInfoVO> roleInfoVOS;


}

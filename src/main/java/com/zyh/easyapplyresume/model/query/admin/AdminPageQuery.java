package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "管理员查询")
public class AdminPageQuery {

    @Schema(description = "管理员名称")
    private String adminUsername;

    @Schema(description = "管理员邮箱")
    private String adminEmail;

    @Schema(description = "管理员手机")
    private String adminPhone;

    @Schema(description = "管理员状态")
    private Integer adminState;


}

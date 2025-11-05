package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色查询")
public class RolePageQuery {
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色介绍")
    private String roleIntroduce;
}

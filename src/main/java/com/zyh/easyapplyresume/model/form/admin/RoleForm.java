package com.zyh.easyapplyresume.model.form.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "角色表单")
public class RoleForm {
    @Schema(description = "角色id")
    private Integer roleId;

    @NotNull(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色介绍")
    private String roleIntroduce;
}

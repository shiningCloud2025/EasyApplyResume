package com.zyh.easyapplyresume.model.form.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员表单")
public class AdminForm {

    //@Validated注解标记需要校验的AdminForm对象。
    @Schema(description = "管理员id")
    private Integer adminId;

    @NotNull(message = "管理员名称不能为空")
    @Schema(description = "管理员名称")
    private String adminUsername;

    @Schema(description = "管理员邮箱")
    private String adminEmail;

    @NotNull(message = "管理员手机不能为空")
    @Schema(description = "管理员手机")
    private String adminPhone;

    @Schema(description = "管理员密码")
    private String adminPassword;

    @Schema(description = "管理员头像")
    private String adminImage;

    @Schema(description = "管理员介绍")
    private String adminIntroduce;

    @Schema(description = "管理员状态")
    private Integer adminState;

}

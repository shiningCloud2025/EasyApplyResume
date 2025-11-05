package com.zyh.easyapplyresume.model.vo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "管理员分页信息")
public class AdminPageVO {
    /**
     * 管理员id（主键，自增）
     */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer adminId;

    /**
     * 管理员名称
     */
    @TableField("admin_username")
    private String adminUsername;

    /**
     * 管理员邮箱
     */
    @TableField("admin_email")
    private String adminEmail;

    /**
     * 管理员手机
     */
    @TableField("admin_phone")
    private String adminPhone;

    /**
     * 管理员状态（tinyint 类型，Java 中用 Integer 接收）
     */
    @TableField("admin_state")
    private Integer adminState;

    /**
     * 管理员最近登录时间
     */
    @TableField("admin_login_time")
    private Date adminLoginTime;

}

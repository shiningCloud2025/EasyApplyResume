package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_admin")
/**
 * 管理员实体类-管理端
 */
public class Admin {
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
     * 管理员密码
     */
    @TableField("admin_password")
    private String adminPassword;

    /**
     * 管理员头像
     */
    @TableField("admin_image")
    private String adminImage;

    /**
     * 管理员介绍
     */
    @TableField("admin_introduce")
    private String adminIntroduce;

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

    /**
     * 逻辑删除标识
     */
    @TableField("deleted")
    private Integer deleted;
}

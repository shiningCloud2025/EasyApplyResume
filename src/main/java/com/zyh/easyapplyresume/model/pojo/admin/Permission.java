package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 权限实体类-管理端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_permission")
public class Permission {
    /**
     * 权限id（主键，自增）
     */
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Integer permissionId;

    /**
     * 权限名称
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 权限url
     */
    @TableField("permission_url")
    private String permissionUrl;
    /**
     * 权限介绍
     */
    @TableField("permission_introduce")
    private String permissionIntroduce;

    /**
     * 逻辑删除标识
     */
    @TableField("deleted")
    private Integer deleted;
}

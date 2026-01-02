package com.zyh.easyapplyresume.model.pojo.ad_monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 服务器信息实体类-监测与广告端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_serviceMachine")
public class AdmonitorServiceMachine {
    /**
     * 主键ID
     */
    @TableId(value = "serviceMachine_id", type = IdType.AUTO)
    private Integer serviceMachineId;

    /**
     * 服务器名称
     */
    @TableField("serviceMachine_name")
    private String serviceMachineName;

    /**
     * 外网IP/域名
     */
    @TableField("serviceMachine_host")
    private String serviceMachineHost;

    /**
     * SSH端口
     */
    @TableField("serviceMachine_port")
    private Integer serviceMachinePort;

    /**
     * 登录账号
     */
    @TableField("serviceMachine_username")
    private String serviceMachineUsername;

    /**
     * 登录密码
     */
    @TableField("serviceMachine_password")
    private String serviceMachinePassword;

    /**
     * 备注
     */
    @TableField("serviceMachine_remark")
    private String serviceMachineRemark;

    /**
     * 创建时间
     */
    @TableField("serviceMachine_createdTime")
    private Date serviceMachineCreatedTime;

    /**
     * 更新时间
     */
    @TableField("serviceMachine_updatedTime")
    private Date serviceMachineUpdatedTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;
}
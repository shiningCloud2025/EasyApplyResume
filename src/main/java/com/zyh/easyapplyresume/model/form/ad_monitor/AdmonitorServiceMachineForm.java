package com.zyh.easyapplyresume.model.form.ad_monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "监测端服务器信息提交表单")
public class AdmonitorServiceMachineForm {
    @Schema(description = "主键ID")
    private Integer serviceMachineId;

    @Schema(description = "服务器名称")
    private String serviceMachineName;

    @Schema(description = "外网IP/域名")
    private String serviceMachineHost;

    @Schema(description = "SSH端口")
    private Integer serviceMachinePort;

    @Schema(description = "登录账号")
    private String serviceMachineUsername;

    @Schema(description = "登录密码")
    private String serviceMachinePassword;

    @Schema(description = "备注")
    private String serviceMachineRemark;

    @Schema(description = "创建时间")
    private Date serviceMachineCreatedTime;

    @Schema(description = "更新时间")
    private Date serviceMachineUpdatedTime;

    @Schema(description = "逻辑删除：0-未删除，1-已删除")
    private Integer deleted;
}

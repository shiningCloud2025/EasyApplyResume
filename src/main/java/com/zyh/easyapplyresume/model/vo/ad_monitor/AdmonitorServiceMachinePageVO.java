package com.zyh.easyapplyresume.model.vo.ad_monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "监测端服务器信息分页查询结果")
public class AdmonitorServiceMachinePageVO {
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

}

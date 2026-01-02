package com.zyh.easyapplyresume.model.form.ad_monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "监测端服务器信息监控表单")
public class AdmonitorServiceMachineJianKongForm {

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

}

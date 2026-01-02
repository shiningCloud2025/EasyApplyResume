package com.zyh.easyapplyresume.model.vo.ad_monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * 监测端服务器信息返回VO
 * @author shiningCloud2025
 */
@Data
@Schema(description = "监测端服务器信息返回VO")
public class AdmonitorServiceMachineJianKongVO {


    /**
     * 服务器ID
     */
    private Integer serviceMachineId;

    /**
     * 服务器名称
     */
    private String serviceMachineName;

    /**
     * CPU使用率（百分比，如 45.2）
     */
    private Double cpuUsage;

    /**
     * 内存总量（MB）
     */
    private Long memoryTotal;

    /**
     * 内存已用（MB）
     */
    private Long memoryUsed;

    /**
     * 内存使用率（百分比）
     */
    private Double memoryUsage;

    /**
     * 硬盘总量（GB）
     */
    private Long diskTotal;

    /**
     * 硬盘已用（GB）
     */
    private Long diskUsed;

    /**
     * 硬盘使用率（百分比）
     */
    private Double diskUsage;

    /**
     * 系统负载（1分钟平均）
     */
    private Double loadAverage;

    /**
     * 是否在线
     */
    private Boolean online;
}

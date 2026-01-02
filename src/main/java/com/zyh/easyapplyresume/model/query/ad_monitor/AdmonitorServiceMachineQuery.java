package com.zyh.easyapplyresume.model.query.ad_monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "监测端服务器信息查询参数")
public class AdmonitorServiceMachineQuery {

    @Schema(description = "服务器名称")
    private String serviceMachineName;
}

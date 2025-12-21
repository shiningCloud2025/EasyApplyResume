package com.zyh.easyapplyresume.model.query.ad_monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "监控端广告查询")
public class AdmonitorAdvertisementQuery {

    @Schema(description = "广告名称")
    private String advertisementName;

}

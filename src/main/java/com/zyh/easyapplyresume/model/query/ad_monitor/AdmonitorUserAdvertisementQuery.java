package com.zyh.easyapplyresume.model.query.ad_monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户端广告查询")
public class AdmonitorUserAdvertisementQuery {
    @Schema(description = "广告名称")
    private String advertisementName;
}

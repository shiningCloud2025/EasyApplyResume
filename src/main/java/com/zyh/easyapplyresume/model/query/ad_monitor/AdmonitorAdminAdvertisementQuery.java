package com.zyh.easyapplyresume.model.query.ad_monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理端广告查询")
public class AdmonitorAdminAdvertisementQuery {

    @Schema(description = "广告名称")
    private String advertisementName;


}

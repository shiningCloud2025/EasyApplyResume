package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "城市Map详情信息")
public class CityMapInfoVO {
    @Schema(description = "城市ID")
    private Integer cityMapCid;

    @Schema(description = "城市名称")
    private String cityMapCname;

    @Schema(description = "所属省份ID")
    private Integer cityMapPid;
}

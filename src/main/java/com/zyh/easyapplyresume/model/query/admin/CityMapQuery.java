package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "城市Map查询参数")
public class CityMapQuery {
    @Schema(description = "城市名称")
    private String cityMapCname;
}

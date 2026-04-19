package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "街道Map查询参数")
public class StreetMapQuery {
    @Schema(description = "街道名称")
    private String streetMapSname;
}

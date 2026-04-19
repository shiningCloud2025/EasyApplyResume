package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "区县Map查询参数")
public class AreaMapQuery {
    @Schema(description = "区县名称")
    private String areaMapAname;
}

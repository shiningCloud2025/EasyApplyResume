package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "省份Map查询参数")
public class ProvinceMapQuery {
    @Schema(description = "省份名称")
    private String provinceMapPname;
}

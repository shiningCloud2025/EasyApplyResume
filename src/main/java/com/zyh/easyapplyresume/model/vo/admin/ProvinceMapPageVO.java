package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "省份Map分页信息")
public class ProvinceMapPageVO {
    @Schema(description = "省份ID")
    private Integer provinceMapPid;

    @Schema(description = "省份名称")
    private String provinceMapPname;
}

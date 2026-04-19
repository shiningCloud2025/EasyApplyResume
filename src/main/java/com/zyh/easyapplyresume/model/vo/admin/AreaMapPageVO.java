package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "区县Map分页信息")
public class AreaMapPageVO {
    @Schema(description = "区县ID")
    private Integer areaMapId;

    @Schema(description = "区县名称")
    private String areaMapAname;

    @Schema(description = "所属城市ID")
    private Integer areaMapCid;
}

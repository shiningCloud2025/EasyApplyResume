package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "街道Map详情信息")
public class StreetMapInfoVO {
    @Schema(description = "街道ID")
    private Integer streetMapSid;

    @Schema(description = "街道名称")
    private String streetMapSname;

    @Schema(description = "所属区县ID")
    private Integer streetMapAid;
}

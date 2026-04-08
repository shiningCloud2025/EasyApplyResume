package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "大学Map分页信息")
public class UniversityMapPageVO {
    @Schema(description = "大学ID")
    private Integer universityMapId;

    @Schema(description = "大学名称")
    private String universityMapName;

    @Schema(description = "大学地址")
    private String universityMapAddress;

    @Schema(description = "大学经度")
    private String universityMapLat;

    @Schema(description = "大学纬度")
    private String universityMapLng;

    @Schema(description = "状态")
    private Integer universityMapStatus;
}

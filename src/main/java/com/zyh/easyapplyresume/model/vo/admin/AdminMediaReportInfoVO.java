package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "媒体报道信息")
public class AdminMediaReportInfoVO {
    @Schema(description = "媒体报道id")
    private Integer mediaReportId;

    @Schema(description = "媒体报道标题")
    private String mediaReportTitle;

    @Schema(description = "媒体报道内容")
    private String mediaReportContent;

    @Schema(description = "媒体报道更新时间")
    private Date mediaReportUpdatedTime;
}

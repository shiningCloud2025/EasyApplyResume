package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "发展历程信息")
public class AdminDevelopHistoryInfoVO {
    @Schema(description = "发展历程id")
    private Integer developHistoryId;

    @Schema(description = "发展历程标题")
    private String developHistoryTitle;

    @Schema(description = "发展历程内容")
    private String developHistoryContent;

    @Schema(description = "发展历程更新时间")
    private LocalDateTime developHistoryUpdatedTime;
}

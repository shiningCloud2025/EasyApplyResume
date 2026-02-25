package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "使用指南分页")
public class AdminUserGuidePageVO {
    @Schema(description = "使用指南id")
    private Integer userGuideId;

    @Schema(description = "使用指南标题")
    private String userGuideTitle;

    @Schema(description = "使用指南内容")
    private String userGuideContent;

    @Schema(description = "使用指南创建时间")
    private Date userGuideCreatedTime;

    @Schema(description = "使用指南更新时间")
    private Date userGuideUpdatedTime;
}

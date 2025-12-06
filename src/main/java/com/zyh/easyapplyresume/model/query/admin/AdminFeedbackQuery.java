package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员反馈查询")
public class AdminFeedbackQuery {
    @Schema(description = "反馈标题")
    private String adminFeedbackTitle;

    @Schema(description = "反馈详细内容")
    private String adminFeedbackContent;
}

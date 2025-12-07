package com.zyh.easyapplyresume.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户反馈查询")
public class UserFeedbackQuery {

    @Schema(description = "反馈标题")
    private String userFeedbackTitle;

    @Schema(description = "反馈详细内容")
    private String userFeedbackContent;
}

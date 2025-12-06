package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员反馈分页信息")
public class AdminFeedbackPageVO {
    @Schema(description = "管理员反馈ID")
    private Integer adminFeedbackId;

    @Schema(description = "反馈标题")
    private String adminFeedbackTitle;

    @Schema(description = "反馈详细内容")
    private String adminFeedbackContent;

    @Schema(description = "反馈提交时间")
    private Date adminFeedbackTime;

    @Schema(description = "最近一次处理时间")
    private Date adminFeedbackRecentTime;

    @Schema(description = "反馈现阶段")
    private String adminFeedbackCurStep;

    @Schema(description = "提交反馈的管理员ID")
    private Integer adminFeedbackAdminId;

    @Schema(description = "提交反馈的管理员姓名")
    private String adminFeedbackAdminName;
}

package com.zyh.easyapplyresume.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户反馈信息")
public class UserFeedbackInfoVO {
    
    @Schema(description = "反馈ID")
    private Integer userFeedbackId;
    
    @Schema(description = "反馈标题")
    private String userFeedbackTitle;
    
    @Schema(description = "反馈详细内容")
    private String userFeedbackContent;
    
    @Schema(description = "反馈提交时间")
    private Date userFeedbackTime;
    
    @Schema(description = "最近一次处理时间")
    private Date userFeedbackRecentTime;
    
    @Schema(description = "反馈现阶段")
    private String userFeedbackCurStep;
    
    @Schema(description = "提交反馈的用户ID")
    private Integer userFeedbackUserId;
    
    @Schema(description = "提交反馈的用户姓名")
    private String userFeedbackUserName;
}

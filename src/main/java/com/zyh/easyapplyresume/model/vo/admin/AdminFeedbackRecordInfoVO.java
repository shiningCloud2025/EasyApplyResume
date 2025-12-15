package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员反馈记录信息")
public class AdminFeedbackRecordInfoVO {
    @Schema(description = "管理员反馈记录ID")
    private Integer adminFeedbackRecordId;

    @Schema(description = "反馈管理员id")
    private Integer adminFeedbackRecordAdminId;

    @Schema(description = "管理员反馈记录反馈人")
    private String adminFeedbackRecordName;

    @Schema(description = "反馈记录标题")
    private String adminFeedbackRecordTitle;
    
    @Schema(description = "反馈记录详细内容")
    private String adminFeedbackRecordContent;
    
    @Schema(description = "反馈记录创建时间")
    private Date adminFeedbackRecordTime;
    
    @Schema(description = "本阶段反馈处理时间")
    private Date adminFeedbackRecordCurrentStepSolveTime;
    
    @Schema(description = "反馈原节点")
    private String adminFeedbackRecordOldStep;
    
    @Schema(description = "反馈现阶段")
    private String adminFeedbackRecordNewStep;
    
    @Schema(description = "反馈处理人ID")
    private Integer adminFeedbackRecordApprovalPersonId;

    @Schema(description = "反馈处理人姓名")
    private String adminFeedbackRecordApprovalPersonName;
}

package com.zyh.easyapplyresume.model.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户反馈记录分页信息")
public class UserFeedbackRecordPageVO {

    @Schema(description = "反馈ID")
    private Integer userFeedbackRecordId;

    @Schema(description = "反馈用户ID")
    private Integer userFeedbackRecordUserId;

    @Schema(description = "管理员反馈记录反馈人")
    private String userFeedbackRecordName;

    @Schema(description = "反馈记录标题")
    private String userFeedbackRecordTitle;

    @Schema(description = "反馈记录详细内容")
    private String userFeedbackRecordContent;

    @Schema(description = "反馈记录创建时间")
    private Date userFeedbackRecordTime;

    @Schema(description = "本阶段反馈处理时间")
    private Date userFeedbackRecordCurrentStepSolveTime;

    @Schema(description = "反馈原节点")
    private String userFeedbackRecordOldStep;

    @Schema(description = "反馈现阶段")
    private String userFeedbackRecordNewStep;

    @Schema(description = "反馈处理人ID")
    private Integer userFeedbackRecordApprovalPersonId;

    @Schema(description = "反馈处理人姓名")
    private String adminFeedbackRecordApprovalPersonName;
}

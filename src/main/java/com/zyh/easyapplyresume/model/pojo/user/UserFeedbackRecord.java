package com.zyh.easyapplyresume.model.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户反馈记录实体类-用户端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_userFeedbackRecord")
public class UserFeedbackRecord {
    /**
     * 反馈记录ID，主键自增
     */
    @TableId(value = "userFeedbackRecord_id", type = IdType.AUTO)
    private Integer userFeedbackRecordId;

    /**
     * 反馈的用户id
     */
    @TableField("userFeedbackRecord_userId")
    private Integer userFeedbackRecordUserId;

    /**
     * 反馈记录标题（最多35字符）
     */
    @TableField("userFeedbackRecord_title")
    private String userFeedbackRecordTitle;
    
    /**
     * 反馈记录详细内容（支持大文本）
     */
    @TableField("userFeedbackRecord_content")
    private String userFeedbackRecordContent;
    
    /**
     * 反馈记录创建时间（默认当前时间）
     */
    @TableField("userFeedbackRecord_time")
    private Date userFeedbackRecordTime;
    
    /**
     * 本阶段反馈处理时间（初始为空，处理时更新）
     */
    @TableField("userFeedbackRecord_currentStepSolveTime")
    private Date userFeedbackRecordCurrentStepSolveTime;
    
    /**
     * 反馈原节点（如：待审核、处理中）
     */
    @TableField("userFeedbackRecord_oldStep")
    private String userFeedbackRecordOldStep;
    
    /**
     * 反馈现阶段（如：审核通过、已完成）
     */
    @TableField("userFeedbackRecord_newStep")
    private String userFeedbackRecordNewStep;
    
    /**
     * 反馈处理人ID（关联处理人表）
     */
    @TableField("userFeedbackRecord_approvalPersonId")
    private Integer userFeedbackRecordApprovalPersonId;

    }

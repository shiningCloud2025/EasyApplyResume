package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 管理员反馈记录实体类-管理端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_adminFeedbackRecord")
public class AdminFeedbackRecord {
    /**
     * 管理员反馈记录ID，主键自增
     */
    @TableId(value = "adminFeedbackRecord_id", type = IdType.AUTO)
    private Integer adminFeedbackRecordId;

    /**
     * 反馈的管理员id
     */
    @TableField("adminFeedbackRecord_adminId")
    private Integer adminFeedbackRecordAdminId;

    /**
     * 反馈记录标题（最多35字符）
     */
    @TableField("adminFeedbackRecord_title")
    private String adminFeedbackRecordTitle;
    
    /**
     * 反馈记录详细内容（支持大文本）
     */
    @TableField("adminFeedbackRecord_content")
    private String adminFeedbackRecordContent;
    
    /**
     * 反馈记录创建时间（默认当前时间）
     */
    @TableField("adminFeedbackRecord_time")
    private Date adminFeedbackRecordTime;
    
    /**
     * 本阶段反馈处理时间（初始为空，处理时更新）
     */
    @TableField("adminFeedbackRecord_currentStepSolveTime")
    private Date adminFeedbackRecordCurrentStepSolveTime;
    
    /**
     * 反馈原节点（如：待审核、处理中）
     */
    @TableField("adminFeedbackRecord_oldStep")
    private String adminFeedbackRecordOldStep;
    
    /**
     * 反馈现阶段（如：审核通过、已完成）
     */
    @TableField("adminFeedbackRecord_newStep")
    private String adminFeedbackRecordNewStep;
    
    /**
     * 反馈处理人ID（关联处理人表）
     */
    @TableField("adminFeedbackRecord_approvalPersonId")
    private Integer adminFeedbackRecordApprovalPersonId;
}

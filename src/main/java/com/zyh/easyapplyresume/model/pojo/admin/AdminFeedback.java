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
 * 管理员反馈实体类-管理端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_adminFeedback")
public class AdminFeedback {
    /**
     * 管理员反馈ID，主键自增
     */
    @TableId(value = "adminFeedback_id", type = IdType.AUTO)
    private Integer adminFeedbackId;
    
    /**
     * 反馈标题（最多35字符）
     */
    @TableField("adminFeedback_title")
    private String adminFeedbackTitle;
    
    /**
     * 反馈详细内容（支持大文本）
     */
    @TableField("adminFeedback_content")
    private String adminFeedbackContent;
    
    /**
     * 反馈提交时间（默认当前时间）
     */
    @TableField("adminFeedback_time")
    private Date adminFeedbackTime;
    
    /**
     * 最近一次处理时间（初始为空，处理时更新）
     */
    @TableField("adminFeedback_recentTime")
    private Date adminFeedbackRecentTime;
    
    /**
     * 反馈现阶段
     */
    @TableField("adminFeedback_curStep")
    private String adminFeedbackCurStep;
    
    /**
     * 提交反馈的管理员ID（关联管理员表）
     */
    @TableField("adminFeedback_adminId")
    private Integer adminFeedbackAdminId;
}

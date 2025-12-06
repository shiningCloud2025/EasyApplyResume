package com.zyh.easyapplyresume.model.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户反馈实体类-用户端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_userFeedback")
public class UserFeedback {
    /**
     * 反馈ID，主键自增
     */
    @TableId(value = "userFeedback_id", type = IdType.AUTO)
    private Integer userFeedbackId;
    
    /**
     * 反馈标题（最多35字符）
     */
    @TableField("userFeedback_title")
    private String userFeedbackTitle;
    
    /**
     * 反馈详细内容（支持大文本）
     */
    @TableField("userFeedback_content")
    private String userFeedbackContent;
    
    /**
     * 反馈提交时间（默认当前时间）
     */
    @TableField("userFeedback_time")
    private Date userFeedbackTime;
    
    /**
     * 最近一次处理时间（初始为空，处理时更新）
     */
    @TableField("userFeedback_recentTime")
    private Date userFeedbackRecentTime;
    
    /**
     * 反馈现阶段
     */
    @TableField("userFeedback_curStep")
    private String userFeedbackCurStep;
    
    /**
     * 提交反馈的用户ID（关联用户表）
     */
    @TableField("userFeedback_userId")
    private Integer userFeedbackUserId;
}

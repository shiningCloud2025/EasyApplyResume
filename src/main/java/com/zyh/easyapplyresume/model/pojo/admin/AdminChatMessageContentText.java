package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_chatMessageContentText")
/**
 * 会话消息实体类-用户端
 * @author shiningCloud2025
 */
public class AdminChatMessageContentText {
    /**
     * 对话消息主键ID
     */
    @TableId(value = "chatMessage_id", type = IdType.AUTO)
    private Integer chatMessageId;
    /**
     * 会话ID
     */
    @TableField("chatMessage_conversationId")
    private String chatMessageConversationId;
    /**
     * 会话文本内容
     */
    @TableField("chatMessage_content")
    private String chatMessageContent;
    /**
     * 会话记忆创建时间
     */
    @TableField("chatMessage_createdTime")
    private Date chatMessageCreatedTime;
    /**
     * 消息类型USER/ASSISTANT
     */
    @TableField("chatMessage_messageType")
    private String chatMessageMessageType;

}

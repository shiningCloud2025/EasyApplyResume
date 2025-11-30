package com.zyh.easyapplyresume.demo.tools;

import com.zyh.easyapplyresume.service.admin.SendCommunicationEmailService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文本Email发送工具类
 * @author shiningCloud2025
 */
@Component
public class TextEmailTool {
    @Autowired
    private SendCommunicationEmailService sendCommunicationEmailService;

    @Tool(description = "Send a plain text email  ")
    public String sendTextEmail(
            @ToolParam(description = "Recipient's email address" ) String toEmail,
            @ToolParam(description = "Email title") String subject,
            @ToolParam(description = "Plain text content of the email" ) String content){
        sendCommunicationEmailService.sendTextEmailUsallyDefition(toEmail, subject, content);
        // 邮件发送逻辑实现
        return "Email sent successfully";
    }
}

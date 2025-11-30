package com.zyh.easyapplyresume.service.admin;

/**
 * @author shiningCloud2025
 */
public interface SendCommunicationEmailService {
    /**
     * 发送纯文本沟通邮件
     *
     * @param fromEmail 发送人邮箱
     * @param toEmail   接收人邮箱
     * @param subject   邮件主题
     * @param content   邮件纯文本内容
     */
    void sendTextEmailSelfDefition(String fromEmail, String toEmail, String subject, String content);

    /**
     * 使用配置文件中默认的发送者邮箱，发送纯文本邮件
     *
     * @param toEmail   接收人邮箱
     * @param subject   邮件主题
     * @param content   邮件纯文本内容
     */
    void sendTextEmailUsallyDefition(String toEmail, String subject, String content);


    /**
     * 发送 HTML 格式的沟通邮件 (自定义发送者)
     *
     * @param fromEmail 发送人邮箱
     * @param toEmail   接收人邮箱
     * @param subject   邮件主题
     * @param htmlContent 邮件 HTML 内容
     */
    void sendHtmlEmailSelfDefition(String fromEmail, String toEmail, String subject, String htmlContent);

    /**
     * 发送 HTML 格式的沟通邮件 (使用默认发送者)
     *
     * @param toEmail   接收人邮箱
     * @param subject   邮件主题
     * @param htmlContent 邮件 HTML 内容
     */
    void sendHtmlEmailUsallyDefition(String toEmail, String subject, String htmlContent);
}

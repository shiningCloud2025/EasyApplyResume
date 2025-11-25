package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.service.admin.SendCommunicationEmailService;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
public class SendCommunicationEmailServiceImpl implements SendCommunicationEmailService {

    @Resource
    private JavaMailSender javaMailSender;
    // 从配置文件中读取默认的发送者邮箱
    @Value("${spring.mail.username}")
    private String defaultFromEmail;

    /**
     * 发送纯文本沟通邮件
     *
     * @param fromEmail 发送人邮箱
     * @param toEmail   接收人邮箱
     * @param subject   邮件主题
     * @param content   邮件纯文本内容
     */
    @Override
    public void sendTextEmailSelfDefition(String fromEmail, String toEmail, String subject, String content) {
        // 1. 校验输入参数
        if (fromEmail == null || fromEmail.trim().isEmpty()) {
            log.warn("发送邮件失败：发送人邮箱不能为空");
            // 后续改成从token中获取
            throw new BusException(AdminCodeEnum.SENDER_EMAIL_NOT_NULL);
        }
        if (toEmail == null || !toEmail.contains("@") || toEmail.split("@").length != 2) {
            log.warn("发送邮件失败：接收人邮箱 [{}] 格式无效", toEmail);
            throw new BusException(AdminCodeEnum.RECIVEDER_EMAIL_NOT_NULL);
        }
        if (subject == null || subject.trim().isEmpty()) {
            log.warn("发送邮件失败：邮件主题不能为空");
            throw new BusException(AdminCodeEnum.SEND_TITLE_NOT_NULL); //
        }
        if (content == null || content.trim().isEmpty()) {
            log.warn("发送邮件失败：邮件内容不能为空");
            throw new BusException(AdminCodeEnum.SEND_CONTENT_NOT_NULL);
        }

        try {
            // 2. 构建 MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject); // 使用配置文件中的默认主题
            helper.setText(content, false); // false 表示发送纯文本

            // 3. 发送邮件
            javaMailSender.send(mimeMessage);
            log.info("向邮箱 [{}] 发送沟通邮件成功", toEmail);

        } catch (Exception e) {
            log.error("向邮箱 [{}] 发送沟通邮件失败: ", toEmail, e);
            // 建议创建一个更通用的邮件发送失败枚举，例如 EMAIL_SEND_FAIL
            throw new BusException(AdminCodeEnum.SEND_COMMUNICATION_EMAIL_FAIL);
        }
    }

    @Override
    public void sendTextEmailUsallyDefition(String toEmail, String subject, String content) {
        // 1. 校验输入参数
        if (toEmail == null || !toEmail.contains("@") || toEmail.split("@").length != 2) {
            log.warn("发送邮件失败：接收人邮箱 [{}] 格式无效", toEmail);
            throw new BusException(AdminCodeEnum.RECIVEDER_EMAIL_NOT_NULL);
        }
        if (subject == null || subject.trim().isEmpty()) {
            log.warn("发送邮件失败：邮件主题不能为空");
            throw new BusException(AdminCodeEnum.SEND_TITLE_NOT_NULL);
        }
        if (content == null || content.trim().isEmpty()) {
            log.warn("发送邮件失败：邮件内容不能为空");
            throw new BusException(AdminCodeEnum.SEND_CONTENT_NOT_NULL);
        }

        try {
            // 2. 构建 MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // 使用从配置文件中读取的默认发送者
            helper.setFrom(defaultFromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, false); // false 表示发送纯文本

            // 3. 发送邮件
            javaMailSender.send(mimeMessage);
            log.info("使用默认发送者 [{}] 向邮箱 [{}] 发送主题为 [{}] 的邮件成功", defaultFromEmail, toEmail, subject);

        } catch (Exception e) {
            log.error("使用默认发送者 [{}] 向邮箱 [{}] 发送主题为 [{}] 的邮件失败: ", defaultFromEmail, toEmail, subject, e);
            throw new BusException(AdminCodeEnum.SEND_COMMUNICATION_EMAIL_FAIL);
        }
    }
}
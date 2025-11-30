package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.service.admin.SendCommunicationEmailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送沟通邮件控制器-管理端
 * Controller层只负责调用服务和返回成功，不处理任何异常
 */
@RestController
@RequestMapping("/admin/email/communication")
public class SendCommunicationEmailController {
    @Autowired
    private SendCommunicationEmailService sendCommunicationEmailService;

    @Operation(summary = "发送纯文本沟通邮件-要指定发送者")
    @PostMapping("/selfde/sendText")
    public void sendTextEmailSelfDefition(@RequestParam(required = false,value = "fromEmail") String fromEmail,
                                          @RequestParam(required = true,value = "toEmail") String toEmail,
                                          @RequestParam(required = true,value = "subject") String subject,
                                          @RequestParam(required = true,value = "content") String content) {
        sendCommunicationEmailService.sendTextEmailSelfDefition(fromEmail, toEmail, subject, content);
    }

    @Operation(summary = "发送纯文本沟通邮件-使用配置文件中的默认发送者")
    @PostMapping("/usallyde/sendText")
    public void sendTextEmailUsallyDefition(@RequestParam(required = true,value = "toEmail") String toEmail,
                                            @RequestParam(required = true,value = "subject") String subject,
                                            @RequestParam(required = true,value = "content") String content) {
        sendCommunicationEmailService.sendTextEmailUsallyDefition(toEmail, subject, content);
    }

    @Operation(summary = "发送HTML格式的沟通邮件-要指定发送者")
    @PostMapping("/selfde/sendHtml")
    public void sendHtmlEmailSelfDefition(@RequestParam(required = false,value = "fromEmail") String fromEmail,
                                          @RequestParam(required = true,value = "toEmail") String toEmail,
                                          @RequestParam(required = true,value = "subject") String subject,
                                          @RequestParam(required = true,value = "htmlContent") String htmlContent) {
        sendCommunicationEmailService.sendHtmlEmailSelfDefition(fromEmail, toEmail, subject, htmlContent);
    }

    @Operation(summary = "发送HTML格式的沟通邮件-使用配置文件中的默认发送者")
    @PostMapping("/usallyde/sendHtml")
    public void sendHtmlEmailUsallyDefition(@RequestParam(required = true,value = "toEmail") String toEmail,
                                            @RequestParam(required = true,value = "subject") String subject,
                                            @RequestParam(required = true,value = "htmlContent") String htmlContent) {
        sendCommunicationEmailService.sendHtmlEmailUsallyDefition(toEmail, subject, htmlContent);
    }




}

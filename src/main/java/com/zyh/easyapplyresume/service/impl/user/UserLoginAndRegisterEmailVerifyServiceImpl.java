package com.zyh.easyapplyresume.service.impl.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.service.user.UserLoginAndRegisterEmailVerifyService;
import com.zyh.easyapplyresume.utils.email.EmailVerifyCodeUtil;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
@Transactional
public class UserLoginAndRegisterEmailVerifyServiceImpl implements UserLoginAndRegisterEmailVerifyService {

    @Resource
    private JavaMailSender javaMailSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailVerifyCodeUtil verifyCodeUtil;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${custom.email.verify.code.expire-seconds}")
    private long codeExpireSeconds;

    @Value("${custom.email.verify.code.redis-prefix}")
    private String codeRedisPrefix;

    @Value("${custom.email.verify.email.send-interval-seconds}")
    private long sendIntervalSeconds;

    @Value("${custom.email.verify.email.redis-prefix}")
    private String sendRecordRedisPrefix;

    @Value("${custom.email.verify.email.subject}")
    private String emailSubject;

    /**
     * 发送邮箱验证码
     * 业务逻辑：
     * 1. 校验邮箱格式，如果无效，抛出异常。
     * 2. 校验发送频率，如果过快，抛出异常。
     * 3. 尝试生成并发送邮件，如果失败，抛出异常。
     * 4. 如果以上步骤都通过，存储验证码和发送记录。
     * @param toEmail 收件人邮箱
     */
    @Override
    public void sendVerifyCode(String toEmail) {
        // 1. 校验邮箱格式
        if (toEmail == null || !toEmail.contains("@") || toEmail.split("@").length != 2||toEmail.length()>25) {
            log.warn("邮箱格式无效: {}", toEmail);
            throw new BusException(AdminCodeEnum.EMAIL_NO_EFFECT);
        }

        // 2. 校验发送频率
        String sendRecordKey = sendRecordRedisPrefix + toEmail;
        Boolean hasSendRecord = stringRedisTemplate.hasKey(sendRecordKey);
        if (Boolean.TRUE.equals(hasSendRecord)) {
            log.warn("邮箱 [{}] 请求验证码过于频繁", toEmail);
            // 直接抛出异常，携带频率限制的枚举
            throw new BusException(AdminCodeEnum.EMAIL_SEND_FREQUENCY);
        }

        try {
            // 3. 生成验证码
            String verifyCode = verifyCodeUtil.generateCode();

            // 4. 构建并发送邮件
            // 4. 构建 MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(emailSubject);
            // 5. 直接构建 HTML 内容字符串
            String htmlContent = String.format("" +
                    "<!DOCTYPE html>" +
                    "<html lang=\"zh-CN\">" +
                    "<head>" +
                    "    <meta charset=\"UTF-8\">" +
                    "    <title>邮箱验证码</title>" +
                    "    <style>" +
                    "        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 8px; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif; }" +
                    "        .code { font-size: 24px; font-weight: bold; color: #007bff; margin: 20px 0; padding: 10px; background: #f8f9fa; border-radius: 4px; display: inline-block; }" +
                    "        .note { color: #666; line-height: 1.6; }" +
                    "        .footer { margin-top: 30px; font-size: 12px; color: #999; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class=\"container\">" +
                    "        <h3>你好，</h3>" +
                    "        <p class=\"note\">你的邮箱验证码为：</p>" +
                    "        <div class=\"code\">%s</div>" +
                    "        <p class=\"note\">有效期 <strong>%d</strong> 分钟，请尽快完成验证。</p>" +
                    "        <p class=\"note\">如非本人操作，请忽略此邮件。</p>" +
                    "        <div class=\"footer\">" +
                    "            <p>此邮件为系统自动发送，请勿回复。</p>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>", verifyCode, codeExpireSeconds / 60);

//            String mailContent = String.format("你的邮箱验证码为：%s，有效期%d分钟，请尽快完成验证。如非本人操作，请忽略此邮件。",
//                    verifyCode, codeExpireSeconds / 60);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            log.info("向邮箱 [{}] 发送验证码成功", toEmail);

            // 5. 存储验证码和发送记录到Redis
            String codeKey = codeRedisPrefix + toEmail;
            stringRedisTemplate.opsForValue().set(codeKey, verifyCode, codeExpireSeconds, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(sendRecordKey, "sent", sendIntervalSeconds, TimeUnit.SECONDS);

        } catch (Exception e) {
            log.error("向邮箱 [{}] 发送验证码失败: ", toEmail, e);
            throw new BusException(AdminCodeEnum.EMAIL_VERIFY_SEND_FAIL);
        }
    }

    /**
     * 验证邮箱验证码
     * 业务逻辑：
     * 1. 校验输入参数，如果无效，抛出异常。
     * 2. 从Redis获取验证码，如果不存在或不匹配，抛出异常。
     * 3. 如果匹配，删除验证码。
     * @param email 收件人邮箱
     * @param inputCode 用户输入的验证码
     */
    @Override
    public void verifyCode(String email, String inputCode) {
        // 1. 校验输入参数
        if (email == null || inputCode == null || inputCode.trim().isEmpty()) {
            log.warn("邮箱 [{}] 验证码校验，输入参数无效", email);
            throw new BusException(AdminCodeEnum.EMAIL_VERIFY_CODE_INVALID);
        }

        // 2. 从Redis获取存储的验证码
        String codeKey = codeRedisPrefix + email;
        String storedCode = stringRedisTemplate.opsForValue().get(codeKey);

        // 3. 校验验证码
        if (inputCode.equals(storedCode)) {
            stringRedisTemplate.delete(codeKey); // 验证成功后删除
            log.info("邮箱 [{}] 验证码校验成功", email);
        } else {
            log.warn("邮箱 [{}] 验证码校验失败，输入码: {}, 存储码: {}", email, inputCode, storedCode);
            throw new BusException(AdminCodeEnum.EMAIL_VERIFY_CODE_INVALID);
        }
    }
}
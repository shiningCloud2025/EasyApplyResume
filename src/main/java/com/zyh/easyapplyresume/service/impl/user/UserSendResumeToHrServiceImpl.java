package com.zyh.easyapplyresume.service.impl.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.model.form.user.UserSendResumeToHrForm;
import com.zyh.easyapplyresume.service.user.UserSendResumeToHrService;
import com.zyh.easyapplyresume.utils.uservalidator.UserSendResumeToHrFormValidator;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;  // 这个！
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
@Transactional
public class UserSendResumeToHrServiceImpl implements UserSendResumeToHrService {
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String defaultFromEmail;


    @Override
    public void sendResumeToHr(UserSendResumeToHrForm userSendResumeToHrForm) {
        UserSendResumeToHrFormValidator.validateForSend(userSendResumeToHrForm);

        try {
            log.info("开始发送简历，目标邮箱：{}", userSendResumeToHrForm.getTargetEmail());

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(defaultFromEmail);
            helper.setTo(userSendResumeToHrForm.getTargetEmail());
            helper.setSubject(userSendResumeToHrForm.getTitle());
            helper.setText(userSendResumeToHrForm.getContent(), true);  // ⭐ true 支持HTML

            // 添加附件
            MultipartFile file = userSendResumeToHrForm.getResumeFile();
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(file.getOriginalFilename(),
                        new ByteArrayResource(file.getBytes()));
            }

            javaMailSender.send(mimeMessage);
            log.info("简历发送成功，目标邮箱：{}", userSendResumeToHrForm.getTargetEmail());

        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("简历发送失败：{}", e.getMessage());
            throw new BusException(UserCodeEnum.USER_RESUME_SEND_FAIL);
        }

    }
}

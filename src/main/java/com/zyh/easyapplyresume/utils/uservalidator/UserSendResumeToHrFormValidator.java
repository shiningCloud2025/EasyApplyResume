package com.zyh.easyapplyresume.utils.uservalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.model.form.user.UserSendResumeToHrForm;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

/**
 * 用户发送简历给HR表单检查工具类，用于验证表单数据
 * 核心规则：
 * - 目标邮箱：必填 + 格式正确 + 长度≤25个字符
 * - 标题：必填 + 长度≤35个字符
 * - 内容：必填（不能为空串）
 * - 简历文件：可选，若上传则大小≤3MB
 * @author shiningCloud2025
 */
public class UserSendResumeToHrFormValidator {

    // 邮箱最大长度限制（25个字符）
    private static final int EMAIL_MAX_LENGTH = 25;
    // 标题最大长度限制（35个字符）
    private static final int TITLE_MAX_LENGTH = 35;
    // 简历文件最大大小（150MB，1MB=1024*1024字节）
    private static final long RESUME_FILE_MAX_SIZE = 150 * 1024 * 1024;
    // 邮箱正则（兜底校验，兼容javax.mail校验）
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

    /**
     * 发送简历给HR校验规则（唯一对外校验方法）
     */
    public static void validateForSend(UserSendResumeToHrForm userSendResumeToHrForm) {
        // 1. 表单本身非空校验
        if (userSendResumeToHrForm == null) {
            throw new BusException(UserCodeEnum.COMMON_PARAM_EMPTY);
        }

        // 2. 邮箱校验（必填+格式+长度）
        validateEmail(userSendResumeToHrForm);

        // 3. 标题校验（必填+长度）
        validateTitle(userSendResumeToHrForm);

        // 4. 内容校验（必填）
        validateContent(userSendResumeToHrForm);

        // 5. 简历文件校验（可选，有则校验大小）
        validateResumeFile(userSendResumeToHrForm);
    }

    /**
     * 私有：校验目标邮箱
     */
    private static void validateEmail(UserSendResumeToHrForm form) {
        String email = form.getTargetEmail();
        // 非空校验
        if (email == null || email.trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_RESUME_EMAIL_EMPTY);
        }
        String trimEmail = email.trim();

        // 长度校验
        if (trimEmail.length() > EMAIL_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_RESUME_EMAIL_TOO_LONG);
        }

        // 格式校验（javax.mail + 正则双重兜底）
        try {
            new InternetAddress(trimEmail).validate();
        } catch (AddressException e) {
            if (!trimEmail.matches(EMAIL_REGEX)) {
                throw new BusException(UserCodeEnum.USER_RESUME_EMAIL_FORMAT_ERROR);
            }
        }

        // 去空格后回写，保证数据整洁
        form.setTargetEmail(trimEmail);
    }

    /**
     * 私有：校验标题
     */
    private static void validateTitle(UserSendResumeToHrForm form) {
        String title = form.getTitle();
        // 非空校验
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_RESUME_TITLE_EMPTY);
        }
        String trimTitle = title.trim();

        // 长度校验
        if (trimTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(UserCodeEnum.USER_RESUME_TITLE_TOO_LONG);
        }

        // 去空格后回写
        form.setTitle(trimTitle);
    }

    /**
     * 私有：校验内容
     */
    private static void validateContent(UserSendResumeToHrForm form) {
        String content = form.getContent();
        // 非空校验
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_RESUME_CONTENT_EMPTY);
        }

        // 去空格后回写
        form.setContent(content.trim());
    }

    /**
     * 私有：校验简历文件（可选）
     */
    private static void validateResumeFile(UserSendResumeToHrForm form) {
        MultipartFile resumeFile = form.getResumeFile();
        // 有文件时才校验大小
        if (resumeFile != null && !resumeFile.isEmpty()) {
            if (resumeFile.getSize() > RESUME_FILE_MAX_SIZE) {
                throw new BusException(UserCodeEnum.USER_RESUME_FILE_TOO_LARGE);
            }
        }
    }
}
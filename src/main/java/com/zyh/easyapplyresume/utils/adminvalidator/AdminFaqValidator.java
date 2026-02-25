package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminFaqForm;

/**
 * AdminFaqForm检查工具类，用于验证常见问题表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminFaqValidator {

    /**
     * 新增常见问题校验
     */
    public static void validateForAdd(AdminFaqForm faqForm) {
        validateTitle(faqForm);
        validateContent(faqForm);
    }

    /**
     * 修改常见问题校验
     */
    public static void validateForUpdate(AdminFaqForm faqForm) {
        validateId(faqForm);
        validateTitle(faqForm);
        validateContent(faqForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminFaqForm faqForm) {
        Integer id = faqForm.getFaqId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminFaqForm faqForm) {
        String title = faqForm.getFaqTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.FAQ_TITLE_EMPTY);
        }
        faqForm.setFaqTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminFaqForm faqForm) {
        String content = faqForm.getFaqContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.FAQ_CONTENT_EMPTY);
        }
        faqForm.setFaqContent(content.trim());
    }
}
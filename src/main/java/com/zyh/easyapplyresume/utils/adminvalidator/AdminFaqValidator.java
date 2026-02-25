package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminFaqForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminFaqValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

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
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminFaqForm faqForm) {
        String title = faqForm.getFaqTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        faqForm.setFaqTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminFaqForm faqForm) {
        String content = faqForm.getFaqContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        faqForm.setFaqContent(content.trim());
    }
}
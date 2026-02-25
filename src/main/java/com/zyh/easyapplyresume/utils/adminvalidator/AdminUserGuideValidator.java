package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminUserGuideForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminUserGuideValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

    /**
     * 新增使用指南校验
     */
    public static void validateForAdd(AdminUserGuideForm userGuideForm) {
        validateTitle(userGuideForm);
        validateContent(userGuideForm);
    }

    /**
     * 修改使用指南校验
     */
    public static void validateForUpdate(AdminUserGuideForm userGuideForm) {
        validateId(userGuideForm);
        validateTitle(userGuideForm);
        validateContent(userGuideForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminUserGuideForm userGuideForm) {
        Integer id = userGuideForm.getUserGuideId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminUserGuideForm userGuideForm) {
        String title = userGuideForm.getUserGuideTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        userGuideForm.setUserGuideTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminUserGuideForm userGuideForm) {
        String content = userGuideForm.getUserGuideContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        userGuideForm.setUserGuideContent(content.trim());
    }
}
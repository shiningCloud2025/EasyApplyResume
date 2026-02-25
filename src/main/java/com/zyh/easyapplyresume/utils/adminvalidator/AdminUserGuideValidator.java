package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminUserGuideForm;

/**
 * AdminUserGuideForm检查工具类，用于验证使用指南表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminUserGuideValidator {

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
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminUserGuideForm userGuideForm) {
        String title = userGuideForm.getUserGuideTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.USER_GUIDE_TITLE_EMPTY);
        }
        userGuideForm.setUserGuideTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminUserGuideForm userGuideForm) {
        String content = userGuideForm.getUserGuideContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.USER_GUIDE_CONTENT_EMPTY);
        }
        userGuideForm.setUserGuideContent(content.trim());
    }
}
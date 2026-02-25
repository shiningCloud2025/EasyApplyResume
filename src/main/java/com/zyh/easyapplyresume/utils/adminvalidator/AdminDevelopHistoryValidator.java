package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminDevelopHistoryForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminDevelopHistoryValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

    /**
     * 新增发展历程校验
     */
    public static void validateForAdd(AdminDevelopHistoryForm developHistoryForm) {
        validateTitle(developHistoryForm);
        validateContent(developHistoryForm);
    }

    /**
     * 修改发展历程校验
     */
    public static void validateForUpdate(AdminDevelopHistoryForm developHistoryForm) {
        validateId(developHistoryForm);
        validateTitle(developHistoryForm);
        validateContent(developHistoryForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminDevelopHistoryForm developHistoryForm) {
        Integer id = developHistoryForm.getDevelopHistoryId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminDevelopHistoryForm developHistoryForm) {
        String title = developHistoryForm.getDevelopHistoryTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        developHistoryForm.setDevelopHistoryTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminDevelopHistoryForm developHistoryForm) {
        String content = developHistoryForm.getDevelopHistoryContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        developHistoryForm.setDevelopHistoryContent(content.trim());
    }
}
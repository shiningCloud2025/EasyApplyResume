package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminDevelopHistoryForm;

/**
 * AdminDevelopHistoryForm检查工具类，用于验证发展历程表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminDevelopHistoryValidator {

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
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminDevelopHistoryForm developHistoryForm) {
        String title = developHistoryForm.getDevelopHistoryTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.DEVELOP_HISTORY_TITLE_EMPTY);
        }
        developHistoryForm.setDevelopHistoryTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminDevelopHistoryForm developHistoryForm) {
        String content = developHistoryForm.getDevelopHistoryContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.DEVELOP_HISTORY_CONTENT_EMPTY);
        }
        developHistoryForm.setDevelopHistoryContent(content.trim());
    }
}
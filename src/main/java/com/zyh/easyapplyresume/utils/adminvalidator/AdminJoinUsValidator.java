package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminJoinUsForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminJoinUsValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

    /**
     * 新增加入我们校验
     */
    public static void validateForAdd(AdminJoinUsForm joinUsForm) {
        validateTitle(joinUsForm);
        validateContent(joinUsForm);
    }

    /**
     * 修改加入我们校验
     */
    public static void validateForUpdate(AdminJoinUsForm joinUsForm) {
        validateId(joinUsForm);
        validateTitle(joinUsForm);
        validateContent(joinUsForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminJoinUsForm joinUsForm) {
        Integer id = joinUsForm.getJoinUsId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminJoinUsForm joinUsForm) {
        String title = joinUsForm.getJoinUsTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        joinUsForm.setJoinUsTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminJoinUsForm joinUsForm) {
        String content = joinUsForm.getJoinUsContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        joinUsForm.setJoinUsContent(content.trim());
    }
}
package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminProjectIntroduceForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminProjectIntroduceValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

    /**
     * 新增项目介绍校验
     */
    public static void validateForAdd(AdminProjectIntroduceForm projectIntroduceForm) {
        validateTitle(projectIntroduceForm);
        validateContent(projectIntroduceForm);
    }

    /**
     * 修改项目介绍校验
     */
    public static void validateForUpdate(AdminProjectIntroduceForm projectIntroduceForm) {
        validateId(projectIntroduceForm);
        validateTitle(projectIntroduceForm);
        validateContent(projectIntroduceForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminProjectIntroduceForm projectIntroduceForm) {
        Integer id = projectIntroduceForm.getProjectIntroduceId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminProjectIntroduceForm projectIntroduceForm) {
        String title = projectIntroduceForm.getProjectIntroduceTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        projectIntroduceForm.setProjectIntroduceTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminProjectIntroduceForm projectIntroduceForm) {
        String content = projectIntroduceForm.getProjectIntroduceContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        projectIntroduceForm.setProjectIntroduceContent(content.trim());
    }
}
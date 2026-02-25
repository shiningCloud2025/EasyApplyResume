package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminProjectIntroduceForm;

/**
 * AdminProjectIntroduceForm检查工具类，用于验证项目介绍表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminProjectIntroduceValidator {

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
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminProjectIntroduceForm projectIntroduceForm) {
        String title = projectIntroduceForm.getProjectIntroduceTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PROJECT_INTRODUCE_TITLE_EMPTY);
        }
        projectIntroduceForm.setProjectIntroduceTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminProjectIntroduceForm projectIntroduceForm) {
        String content = projectIntroduceForm.getProjectIntroduceContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PROJECT_INTRODUCE_CONTENT_EMPTY);
        }
        projectIntroduceForm.setProjectIntroduceContent(content.trim());
    }
}
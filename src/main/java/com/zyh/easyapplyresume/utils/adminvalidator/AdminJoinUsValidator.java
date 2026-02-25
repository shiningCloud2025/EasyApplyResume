package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminJoinUsForm;

/**
 * AdminJoinUsForm检查工具类，用于验证加入我们表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminJoinUsValidator {

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
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminJoinUsForm joinUsForm) {
        String title = joinUsForm.getJoinUsTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.JOIN_US_TITLE_EMPTY);
        }
        joinUsForm.setJoinUsTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminJoinUsForm joinUsForm) {
        String content = joinUsForm.getJoinUsContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.JOIN_US_CONTENT_EMPTY);
        }
        joinUsForm.setJoinUsContent(content.trim());
    }
}
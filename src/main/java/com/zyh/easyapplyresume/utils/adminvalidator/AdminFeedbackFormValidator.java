package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.model.form.admin.AdminFeedbackForm;

/**
 * AdminFeedbackForm检查工具类，用于验证管理员反馈表单数据
 * 核心规则（新增/修改一致）：
 * - 反馈标题：长度不超过35个字符（可空，有值则校验长度）
 * - 反馈内容：非空（不能为空串）
 * - 管理员ID：非空（不能为null）
 * @author shiningCloud2025
 */
public class AdminFeedbackFormValidator {

    // 反馈标题最大长度限制（35个字符）
    private static final int FEEDBACK_TITLE_MAX_LENGTH = 35;

    /**
     * 新增管理员反馈校验规则（复用公共校验逻辑）
     */
    public static void validateForAdd(AdminFeedbackForm adminFeedbackForm) {
        validateCommon(adminFeedbackForm);
    }

    /**
     * 修改管理员反馈校验规则（复用公共校验逻辑）
     */
    public static void validateForUpdate(AdminFeedbackForm adminFeedbackForm) {
        validateCommon(adminFeedbackForm);
    }

    /**
     * 公共校验逻辑（新增/修改共用）
     */
    private static void validateCommon(AdminFeedbackForm adminFeedbackForm) {
        // 1. 校验反馈标题：有值则去空格+长度≤35位 → ADMIN_FEEDBACK_TITLE_TOO_LONG(814)
        if (adminFeedbackForm.getAdminFeedbackTitle() != null) {
            String title = adminFeedbackForm.getAdminFeedbackTitle().trim();
            if (title.length() > FEEDBACK_TITLE_MAX_LENGTH) {
                throw new BusException(AdminCodeEnum.ADMIN_FEEDBACK_TITLE_TOO_LONG);
            }
            adminFeedbackForm.setAdminFeedbackTitle(title); // 去空格后存值
        }

        // 2. 校验反馈内容：非空（不能为null/空串）→ ADMIN_FEEDBACK_CONTENT_EMPTY(812)
        if (adminFeedbackForm.getAdminFeedbackContent() == null || adminFeedbackForm.getAdminFeedbackContent().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.ADMIN_FEEDBACK_CONTENT_EMPTY);
        }
        String content = adminFeedbackForm.getAdminFeedbackContent().trim();
        adminFeedbackForm.setAdminFeedbackContent(content); // 去空格后存值

        // 3. 校验管理员ID：非空（不能为null）→ ADMIN_FEEDBACK_ADMIN_ID_EMPTY(813)
        if (adminFeedbackForm.getAdminFeedbackAdminId() == null) {
            throw new BusException(AdminCodeEnum.ADMIN_FEEDBACK_ADMIN_ID_EMPTY);
        }
    }
}
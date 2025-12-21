package com.zyh.easyapplyresume.utils.uservalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.model.form.user.UserFeedbackForm;

/**
 * UserFeedbackForm检查工具类，用于验证用户反馈表单数据
 * 核心规则（新增/修改一致）：
 * - 反馈标题：长度不超过35个字符（可空，有值则校验长度）
 * - 反馈内容：非空（不能为空串）
 * - 用户ID：非空（不能为null）
 * @author shiningCloud2025
 */
public class UserFeedbackFormValidator {

    // 反馈标题最大长度限制（35个字符）
    private static final int FEEDBACK_TITLE_MAX_LENGTH = 35;

    /**
     * 新增用户反馈校验规则（复用公共校验逻辑）
     */
    public static void validateForAdd(UserFeedbackForm userFeedbackForm) {
        validateCommon(userFeedbackForm);
    }

    /**
     * 修改用户反馈校验规则（复用公共校验逻辑）
     */
    public static void validateForUpdate(UserFeedbackForm userFeedbackForm) {
        validateCommon(userFeedbackForm);
    }

    /**
     * 公共校验逻辑（新增/修改共用）
     */
    private static void validateCommon(UserFeedbackForm userFeedbackForm) {
        // 1. 校验反馈标题：有值则去空格+长度≤35位 → USER_FEEDBACK_TITLE_TOO_LONG
        if (userFeedbackForm.getUserFeedbackTitle() != null) {
            String title = userFeedbackForm.getUserFeedbackTitle().trim();
            if (title.length() > FEEDBACK_TITLE_MAX_LENGTH) {
                throw new BusException(UserCodeEnum.USER_FEEDBACK_TITLE_TOO_LONG);
            }
            userFeedbackForm.setUserFeedbackTitle(title); // 去空格后存值
        }

        // 2. 校验反馈内容：非空（不能为null/空串）→ USER_FEEDBACK_CONTENT_EMPTY
        if (userFeedbackForm.getUserFeedbackContent() == null || userFeedbackForm.getUserFeedbackContent().trim().isEmpty()) {
            throw new BusException(UserCodeEnum.USER_FEEDBACK_CONTENT_EMPTY);
        }
        String content = userFeedbackForm.getUserFeedbackContent().trim();
        userFeedbackForm.setUserFeedbackContent(content); // 去空格后存值

        // 3. 校验用户ID：非空（不能为null）→ USER_FEEDBACK_USER_ID_EMPTY
        if (userFeedbackForm.getUserFeedbackUserId() == null) {
            throw new BusException(UserCodeEnum.USER_FEEDBACK_USER_ID_EMPTY);
        }
    }
}
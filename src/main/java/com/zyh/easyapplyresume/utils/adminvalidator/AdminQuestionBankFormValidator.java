package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.model.form.admin.AdminQuestionBankForm;

/**
 * 题库题目表单校验工具类
 * @author shiningCloud2025
 */
public class AdminQuestionBankFormValidator {
    private static final int TYPE_SINGLE = 1;
    private static final int TYPE_MULTIPLE = 2;
    private static final int TYPE_JUDGE = 3;
    private static final int TYPE_BLANK = 4;
    private static final int TYPE_SHORT_ANSWER = 5;

    private AdminQuestionBankFormValidator() {}

    public static void validateForAdd(AdminQuestionBankForm form) {
        validateCommon(form, false);
    }

    public static void validateForUpdate(AdminQuestionBankForm form) {
        validateCommon(form, true);
    }
}

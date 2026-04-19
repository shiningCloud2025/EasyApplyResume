package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionSecondCategoryForm;
import org.springframework.stereotype.Component;

/**
 * 题库小类表单校验
 * @author shiningCloud2025
 */
@Component
public class AdminQuestionSecondCategoryFormValidator {

    public void validateForAdd(AdminQuestionSecondCategoryForm form) {
        validateCommon(form);
    }

    public void validateForUpdate(AdminQuestionSecondCategoryForm form) {
        validateCommon(form);
    }

    private void validateCommon(AdminQuestionSecondCategoryForm form) {
        if (form == null) {
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_FIRST_CATEGORY_ID_EMPTY);
        }

        if (form.getQuestionFirstCategoryId() == null) {
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_FIRST_CATEGORY_ID_EMPTY);
        }

        if (form.getQuestionSecondCategoryName() == null || form.getQuestionSecondCategoryName().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_NAME_EMPTY);
        }
        if (form.getQuestionSecondCategoryName().trim().length() > 20) {
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_NAME_TOO_LONG);
        }

        if (form.getQuestionSecondCategoryIntroduce() == null || form.getQuestionSecondCategoryIntroduce().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_INTRO_EMPTY);
        }
        if (form.getQuestionSecondCategoryIntroduce().trim().length() > 60) {
            throw new BusException(AdminCodeEnum.QUESTION_SECOND_CATEGORY_INTRO_TOO_LONG);
        }
    }
}

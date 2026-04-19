package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionFirstCategoryForm;
import org.springframework.stereotype.Component;

/**
 * 题库大类表单校验
 * @author shiningCloud2025
 */
@Component
public class AdminQuestionFirstCategoryFormValidator {

    public void validateForAdd(AdminQuestionFirstCategoryForm form) {
        validateCommon(form);
    }

    public void validateForUpdate(AdminQuestionFirstCategoryForm form) {
        validateCommon(form);
    }

    private void validateCommon(AdminQuestionFirstCategoryForm form) {


        if (form.getQuestionFirstCategoryName() == null || form.getQuestionFirstCategoryName().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_NAME_EMPTY);
        }
        if (form.getQuestionFirstCategoryName().trim().length() > 20) {
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_NAME_TOO_LONG);
        }

        if (form.getQuestionFirstCategoryIntroduce() == null || form.getQuestionFirstCategoryIntroduce().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_INTRO_EMPTY);
        }
        if (form.getQuestionFirstCategoryIntroduce().trim().length() > 60) {
            throw new BusException(AdminCodeEnum.QUESTION_FIRST_CATEGORY_INTRO_TOO_LONG);
        }
    }
}
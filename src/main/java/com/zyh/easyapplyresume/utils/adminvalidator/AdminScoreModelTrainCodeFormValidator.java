package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreModelTrainCodeForm;

/**
 * 简历评分模型训练代码表单校验工具类
 * @author shiningCloud2025
 */
public class AdminScoreModelTrainCodeFormValidator {

    public static void validateForAdd(AdminScoreModelTrainCodeForm form) {
        validateCommon(form);
    }

    public static void validateForUpdate(AdminScoreModelTrainCodeForm form) {
        if (form.getScoreModelTrainCodeId() == null) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_ID_EMPTY);
        }
        validateCommon(form);
    }

    private static void validateCommon(AdminScoreModelTrainCodeForm form) {
        if (form.getScoreModelTrainCodeName() == null || form.getScoreModelTrainCodeName().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_NAME_EMPTY);
        }
        if (form.getScoreModelTrainCodeName().trim().length() > 64) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_NAME_TOO_LONG);
        }

        if (form.getScoreModelTrainCodeVersion() == null || form.getScoreModelTrainCodeVersion().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_VERSION_EMPTY);
        }
        if (form.getScoreModelTrainCodeVersion().trim().length() > 64) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_VERSION_TOO_LONG);
        }

        if (form.getScoreModelTrainCodeLanguage() == null || form.getScoreModelTrainCodeLanguage().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_LANGUAGE_EMPTY);
        }
        if (form.getScoreModelTrainCodeLanguage().trim().length() > 32) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_LANGUAGE_TOO_LONG);
        }

        if (form.getScoreModelTrainCodeContent() == null || form.getScoreModelTrainCodeContent().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_CONTENT_EMPTY);
        }

        if (form.getScoreModelTrainCodeDesc() == null || form.getScoreModelTrainCodeDesc().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_DESC_EMPTY);
        }
        if (form.getScoreModelTrainCodeDesc().trim().length() > 500) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_DESC_TOO_LONG);
        }
    }
}

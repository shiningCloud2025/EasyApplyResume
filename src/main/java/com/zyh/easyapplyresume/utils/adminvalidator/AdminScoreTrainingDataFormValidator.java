package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreTrainingDataForm;

/**
 * 简历评分训练数据表单校验工具类
 * @author shiningCloud2025
 */
public class AdminScoreTrainingDataFormValidator {

    /**
     * 新增校验
     */
    public static void validateForAdd(AdminScoreTrainingDataForm form) {
        if (form.getScoreTrainingDataResumeName() == null || form.getScoreTrainingDataResumeName().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_RESUME_NAME_EMPTY);
        }
        if (form.getScoreTrainingDataResumeName().trim().length() > 25) {
            throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_RESUME_NAME_TOO_LONG);
        }

        if (form.getScoreTrainingDataIndustryName() == null || form.getScoreTrainingDataIndustryName().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_INDUSTRY_NAME_EMPTY);
        }
        if (form.getScoreTrainingDataIndustryName().trim().length() > 35) {
            throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_INDUSTRY_NAME_TOO_LONG);
        }

        if (form.getScoreTrainingDataResumeContent() == null || form.getScoreTrainingDataResumeContent().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_RESUME_CONTENT_EMPTY);
        }

        if (form.getScoreTrainingDataLabelScore() == null) {
            throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_LABEL_SCORE_EMPTY);
        }

        if (form.getScoreTrainingDataDataSource() == null) {
            throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_DATA_SOURCE_EMPTY);
        }

        if (!Integer.valueOf(0).equals(form.getScoreTrainingDataDataSource())
                && !Integer.valueOf(1).equals(form.getScoreTrainingDataDataSource())) {
            throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_DATA_SOURCE_INVALID);
        }
    }
}

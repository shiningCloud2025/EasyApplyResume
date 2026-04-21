package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreModelVersionForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

/**
 * 简历评分模型版本表单校验工具类
 *
 * @author shiningCloud2025
 */
public class AdminScoreModelVersionFormValidator {

    private static final Pattern MODEL_TYPE_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+$");

    public static void validateForAdd(AdminScoreModelVersionForm form, MultipartFile modelFile) {
        validateCommon(form);
        validateModelFile(modelFile);
    }

    public static void validateForUpdate(AdminScoreModelVersionForm form, MultipartFile modelFile) {
        if (form.getScoreModelVersionId() == null) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_ID_EMPTY);
        }
        validateCommon(form);
        if (modelFile != null && !modelFile.isEmpty()) {
            validateModelFile(modelFile);
        }
    }

    private static void validateCommon(AdminScoreModelVersionForm form) {
        if (form.getScoreModelVersionModelName() == null || form.getScoreModelVersionModelName().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_MODEL_NAME_EMPTY);
        }
        if (form.getScoreModelVersionModelName().trim().length() > 64) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_MODEL_NAME_TOO_LONG);
        }

        if (form.getScoreModelVersionVersion() == null || form.getScoreModelVersionVersion().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_VERSION_EMPTY);
        }
        if (form.getScoreModelVersionVersion().trim().length() > 64) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_VERSION_TOO_LONG);
        }

        if (form.getScoreModelVersionModelType() == null || form.getScoreModelVersionModelType().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_MODEL_TYPE_EMPTY);
        }
        if (form.getScoreModelVersionModelType().trim().length() > 32) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_MODEL_TYPE_TOO_LONG);
        }
        String modelType = form.getScoreModelVersionModelType().trim();
        if (".".equals(modelType) || "..".equals(modelType) || !MODEL_TYPE_PATTERN.matcher(modelType).matches()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_MODEL_TYPE_INVALID);
        }

        if (form.getScoreModelVersionEmbeddingModel() == null || form.getScoreModelVersionEmbeddingModel().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_EMBEDDING_MODEL_EMPTY);
        }
        if (form.getScoreModelVersionEmbeddingModel().trim().length() > 64) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_EMBEDDING_MODEL_TOO_LONG);
        }

        if (form.getScoreModelVersionSampleCount() == null) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_SAMPLE_COUNT_EMPTY);
        }
        if (form.getScoreModelVersionSampleCount() < 0) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_SAMPLE_COUNT_INVALID);
        }

        if (form.getScoreModelVersionTrainCostMs() == null) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_TRAIN_COST_MS_EMPTY);
        }
        if (form.getScoreModelVersionTrainCostMs() < 0) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_TRAIN_COST_MS_INVALID);
        }

        if (form.getScoreModelVersionMetricJson() == null || form.getScoreModelVersionMetricJson().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_METRIC_JSON_EMPTY);
        }

        if (form.getScoreModelVersionIsActive() == null) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_IS_ACTIVE_EMPTY);
        }
        if (!Integer.valueOf(0).equals(form.getScoreModelVersionIsActive())
                && !Integer.valueOf(1).equals(form.getScoreModelVersionIsActive())) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_IS_ACTIVE_INVALID);
        }
    }

    private static void validateModelFile(MultipartFile modelFile) {
        if (modelFile == null || modelFile.isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_MODEL_FILE_EMPTY);
        }

        String originalFilename = modelFile.getOriginalFilename() == null ? "" : modelFile.getOriginalFilename().trim();
        originalFilename = originalFilename.replace("\\", "/");
        int lastSlashIndex = originalFilename.lastIndexOf("/");
        String modelFileName = lastSlashIndex >= 0 ? originalFilename.substring(lastSlashIndex + 1) : originalFilename;

        if (modelFileName.isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_MODEL_FILE_NAME_EMPTY);
        }
        if (modelFileName.length() > 512) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_MODEL_FILE_NAME_TOO_LONG);
        }
    }
}

package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminMediaReportForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminMediaReportValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

    /**
     * 新增媒体报道校验
     */
    public static void validateForAdd(AdminMediaReportForm mediaReportForm) {
        validateTitle(mediaReportForm);
        validateContent(mediaReportForm);
    }

    /**
     * 修改媒体报道校验
     */
    public static void validateForUpdate(AdminMediaReportForm mediaReportForm) {
        validateId(mediaReportForm);
        validateTitle(mediaReportForm);
        validateContent(mediaReportForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminMediaReportForm mediaReportForm) {
        Integer id = mediaReportForm.getMediaReportId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminMediaReportForm mediaReportForm) {
        String title = mediaReportForm.getMediaReportTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        mediaReportForm.setMediaReportTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminMediaReportForm mediaReportForm) {
        String content = mediaReportForm.getMediaReportContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        mediaReportForm.setMediaReportContent(content.trim());
    }
}
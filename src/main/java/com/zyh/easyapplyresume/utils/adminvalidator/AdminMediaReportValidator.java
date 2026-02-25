package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminMediaReportForm;

/**
 * AdminMediaReportForm检查工具类，用于验证媒体报道表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminMediaReportValidator {

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
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminMediaReportForm mediaReportForm) {
        String title = mediaReportForm.getMediaReportTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.MEDIA_REPORT_TITLE_EMPTY);
        }
        mediaReportForm.setMediaReportTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminMediaReportForm mediaReportForm) {
        String content = mediaReportForm.getMediaReportContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.MEDIA_REPORT_CONTENT_EMPTY);
        }
        mediaReportForm.setMediaReportContent(content.trim());
    }
}
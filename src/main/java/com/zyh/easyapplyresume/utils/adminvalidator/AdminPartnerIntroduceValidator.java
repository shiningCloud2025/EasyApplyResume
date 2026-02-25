package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminPartnerIntroduceForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminPartnerIntroduceValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

    /**
     * 新增合作伙伴校验
     */
    public static void validateForAdd(AdminPartnerIntroduceForm partnerIntroduceForm) {
        validateTitle(partnerIntroduceForm);
        validateContent(partnerIntroduceForm);
    }

    /**
     * 修改合作伙伴校验
     */
    public static void validateForUpdate(AdminPartnerIntroduceForm partnerIntroduceForm) {
        validateId(partnerIntroduceForm);
        validateTitle(partnerIntroduceForm);
        validateContent(partnerIntroduceForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminPartnerIntroduceForm partnerIntroduceForm) {
        Integer id = partnerIntroduceForm.getPartnerIntroduceId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminPartnerIntroduceForm partnerIntroduceForm) {
        String title = partnerIntroduceForm.getPartnerIntroduceTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        partnerIntroduceForm.setPartnerIntroduceTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminPartnerIntroduceForm partnerIntroduceForm) {
        String content = partnerIntroduceForm.getPartnerIntroduceContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        partnerIntroduceForm.setPartnerIntroduceContent(content.trim());
    }
}
package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminPartnerIntroduceForm;

/**
 * AdminPartnerIntroduceForm检查工具类，用于验证合作伙伴表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminPartnerIntroduceValidator {

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
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminPartnerIntroduceForm partnerIntroduceForm) {
        String title = partnerIntroduceForm.getPartnerIntroduceTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARTNER_INTRODUCE_TITLE_EMPTY);
        }
        partnerIntroduceForm.setPartnerIntroduceTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminPartnerIntroduceForm partnerIntroduceForm) {
        String content = partnerIntroduceForm.getPartnerIntroduceContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARTNER_INTRODUCE_CONTENT_EMPTY);
        }
        partnerIntroduceForm.setPartnerIntroduceContent(content.trim());
    }
}
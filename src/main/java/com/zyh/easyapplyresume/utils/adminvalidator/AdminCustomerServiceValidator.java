package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminCustomerServiceForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminCustomerServiceValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

    /**
     * 新增客服管理校验
     */
    public static void validateForAdd(AdminCustomerServiceForm customerServiceForm) {
        validateTitle(customerServiceForm);
        validateContent(customerServiceForm);
    }

    /**
     * 修改客服管理校验
     */
    public static void validateForUpdate(AdminCustomerServiceForm customerServiceForm) {
        validateId(customerServiceForm);
        validateTitle(customerServiceForm);
        validateContent(customerServiceForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminCustomerServiceForm customerServiceForm) {
        Integer id = customerServiceForm.getCustomerServiceId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminCustomerServiceForm customerServiceForm) {
        String title = customerServiceForm.getCustomerServiceTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        customerServiceForm.setCustomerServiceTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminCustomerServiceForm customerServiceForm) {
        String content = customerServiceForm.getCustomerServiceContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        customerServiceForm.setCustomerServiceContent(content.trim());
    }
}
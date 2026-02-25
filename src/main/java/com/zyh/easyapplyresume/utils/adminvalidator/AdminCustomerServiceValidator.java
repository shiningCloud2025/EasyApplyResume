package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminCustomerServiceForm;

/**
 * AdminCustomerServiceForm检查工具类，用于验证人工客服表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminCustomerServiceValidator {

    /**
     * 新增人工客服校验
     */
    public static void validateForAdd(AdminCustomerServiceForm customerServiceForm) {
        validateTitle(customerServiceForm);
        validateContent(customerServiceForm);
    }

    /**
     * 修改人工客服校验
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
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminCustomerServiceForm customerServiceForm) {
        String title = customerServiceForm.getCustomerServiceTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.CUSTOMER_SERVICE_TITLE_EMPTY);
        }
        customerServiceForm.setCustomerServiceTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminCustomerServiceForm customerServiceForm) {
        String content = customerServiceForm.getCustomerServiceContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.CUSTOMER_SERVICE_CONTENT_EMPTY);
        }
        customerServiceForm.setCustomerServiceContent(content.trim());
    }
}
package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.LLMCodeEnum;
import com.zyh.easyapplyresume.model.form.admin.AdminLlmUtilsInfoForm;

/**
 * AdminLlmUtilsInfoForm检查工具类，用于验证LLM工具类调用日志表单数据
 * 核心规则（新增日志）：
 * - 工具类名：必填（不能为空串）
 * - 工具描述：必填（不能为空串）
 * - 模型厂商：必填（不能为空串）
 * - 模型名称：必填（不能为空串）
 * - 输入内容：必填（不能为空串）
 * - 输出结果：必填（不能为空串）
 * - 响应延迟：必填（不能为null）
 * - 调用状态：必填（不能为空串）
 * - 错误信息：必填（不能为空串）
 * @author shiningCloud2025
 */
public class AdminLlmUtilsInfoFormValidator {

    /**
     * 新增LLM工具类调用日志校验规则（唯一对外校验方法）
     */
    public static void validateForAdd(AdminLlmUtilsInfoForm adminLlmUtilsInfoForm) {
        validateCommon(adminLlmUtilsInfoForm);
    }

    /**
     * 公共校验逻辑（新增共用）
     */
    private static void validateCommon(AdminLlmUtilsInfoForm adminLlmUtilsInfoForm) {
        // 1. 表单本身非空校验
        if (adminLlmUtilsInfoForm == null) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }

        // 2. 工具类名：必填（不能为空串）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoToolClass() == null
                || adminLlmUtilsInfoForm.getLlmUtilsInfoToolClass().trim().isEmpty()) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }
        adminLlmUtilsInfoForm.setLlmUtilsInfoToolClass(adminLlmUtilsInfoForm.getLlmUtilsInfoToolClass().trim());

        // 3. 工具描述：必填（不能为空串）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoToolDescription() == null
                || adminLlmUtilsInfoForm.getLlmUtilsInfoToolDescription().trim().isEmpty()) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }
        adminLlmUtilsInfoForm.setLlmUtilsInfoToolDescription(adminLlmUtilsInfoForm.getLlmUtilsInfoToolDescription().trim());

        // 4. 模型厂商：必填（不能为空串）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoModelProvider() == null
                || adminLlmUtilsInfoForm.getLlmUtilsInfoModelProvider().trim().isEmpty()) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }
        adminLlmUtilsInfoForm.setLlmUtilsInfoModelProvider(adminLlmUtilsInfoForm.getLlmUtilsInfoModelProvider().trim());

        // 5. 模型名称：必填（不能为空串）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoModelName() == null
                || adminLlmUtilsInfoForm.getLlmUtilsInfoModelName().trim().isEmpty()) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }
        adminLlmUtilsInfoForm.setLlmUtilsInfoModelName(adminLlmUtilsInfoForm.getLlmUtilsInfoModelName().trim());

        // 6. 输入内容：必填（不能为空串）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoInputContent() == null
                || adminLlmUtilsInfoForm.getLlmUtilsInfoInputContent().trim().isEmpty()) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }
        adminLlmUtilsInfoForm.setLlmUtilsInfoInputContent(adminLlmUtilsInfoForm.getLlmUtilsInfoInputContent().trim());

        // 7. 输出结果：必填（不能为空串）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoOutputResult() == null
                || adminLlmUtilsInfoForm.getLlmUtilsInfoOutputResult().trim().isEmpty()) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }
        adminLlmUtilsInfoForm.setLlmUtilsInfoOutputResult(adminLlmUtilsInfoForm.getLlmUtilsInfoOutputResult().trim());

        // 8. 响应延迟：必填（不能为null）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoLatencyMs() == null) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }

        // 9. 调用状态：必填（不能为空串）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoStatus() == null
                || adminLlmUtilsInfoForm.getLlmUtilsInfoStatus().trim().isEmpty()) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }
        adminLlmUtilsInfoForm.setLlmUtilsInfoStatus(adminLlmUtilsInfoForm.getLlmUtilsInfoStatus().trim());

        // 10. 错误信息：必填（不能为空串）
        if (adminLlmUtilsInfoForm.getLlmUtilsInfoErrorMessage() == null
                || adminLlmUtilsInfoForm.getLlmUtilsInfoErrorMessage().trim().isEmpty()) {
            throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
        }
        adminLlmUtilsInfoForm.setLlmUtilsInfoErrorMessage(adminLlmUtilsInfoForm.getLlmUtilsInfoErrorMessage().trim());
    }
}

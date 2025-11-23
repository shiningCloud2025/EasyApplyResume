package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.IndustryMapEnum;
import com.zyh.easyapplyresume.model.form.admin.IndustryMapForm;

/**
 * IndustryMapForm检查工具类，用于验证行业映射表单数据
 * 支持新增场景和修改场景的字段校验
 * @author shiningCloud2025
 */
public class IndustryMapFormValidator {

    // 行业名称长度限制（1-35位）
    private static final int INDUSTRY_NAME_MAX_LENGTH = 35;

    /**
     * 行业映射新增场景校验：名称非空 + 空格处理 + 长度校验
     * @param industryMapForm 待验证的行业映射表单对象
     * @throws BusException 字段为空或长度超出限制时抛出
     */
    public static void validateForAdd(IndustryMapForm industryMapForm) {
        // 校验通用字段（名称非空 + 长度校验）
        validateCommonFields(industryMapForm);
    }

    /**
     * 行业映射修改场景校验：名称非空 + 空格处理 + 长度校验
     * @param industryMapForm 待验证的行业映射表单对象
     * @throws BusException 字段为空或长度超出限制时抛出
     */
    public static void validateForUpdate(IndustryMapForm industryMapForm) {
        // 校验通用字段（名称非空 + 长度校验）
        validateCommonFields(industryMapForm);
    }

    /**
     * 私有辅助方法：校验行业名称等通用字段
     * 1. 非空校验（与表单@NotNull注解互补，二次保障）
     * 2. 去空格处理，并更新回表单对象
     * 3. 长度校验（trim()处理避免纯空格占用长度）
     */
    private static void validateCommonFields(IndustryMapForm industryMapForm) {
        // 1. 非空校验
        if (industryMapForm.getIndustryMapIndustryName() == null || industryMapForm.getIndustryMapIndustryName().trim().isEmpty()) {
            throw new BusException(IndustryMapEnum.INDUSTRY_NAME_EMPTY);
        }

        // 2. 去空格处理
        String trimedName = industryMapForm.getIndustryMapIndustryName().trim();
        industryMapForm.setIndustryMapIndustryName(trimedName);

        // 3. 长度校验
        if (trimedName.length() > INDUSTRY_NAME_MAX_LENGTH) {
            throw new BusException(IndustryMapEnum.INDUSTRY_NAME_TOO_LONG);
        }
    }
}
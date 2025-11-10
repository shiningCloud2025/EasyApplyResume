package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.IndustryMapEnum;
import com.zyh.easyapplyresume.model.form.admin.IndustryMapForm;

/**
 * IndustryMapForm检查工具类，用于验证行业映射表单数据
 * 支持新增场景和修改场景（均校验：名称非空 + 名称长度≤35字符）
 * @author shiningCloud2025
 */
public class IndustryMapFormValidator {

    // 行业名称最大长度（新增/修改场景共用）
    private static final int INDUSTRY_NAME_MAX_LENGTH = 35;

    /**
     * 行业映射新增场景校验：名称非空 + 空格处理 + 长度校验
     * @param industryMapForm 待验证的行业映射表单对象
     * @throws BusException 字段为空或长度超出限制时抛出
     */
    public static void validateForAdd(IndustryMapForm industryMapForm) {
        // 校验行业名称（核心逻辑）
        validateIndustryName(industryMapForm.getIndustryMapIndustryName());
        
        // 还原trim后的值（避免表单中保留多余空格，与数据库存储一致）
        String trimedName = industryMapForm.getIndustryMapIndustryName().trim();
        industryMapForm.setIndustryMapIndustryName(trimedName);
    }

    /**
     * 行业映射修改场景校验：名称非空 + 空格处理 + 长度校验
     * @param industryMapForm 待验证的行业映射表单对象
     * @throws BusException 字段为空或长度超出限制时抛出
     */
    public static void validateForUpdate(IndustryMapForm industryMapForm) {
        // 复用名称校验逻辑（避免重复代码）
        validateIndustryName(industryMapForm.getIndustryMapIndustryName());
        
        // 还原trim后的值（保持数据整洁）
        String trimedName = industryMapForm.getIndustryMapIndustryName().trim();
        industryMapForm.setIndustryMapIndustryName(trimedName);
    }

    /**
     * 复用校验：行业名称（抽离公共逻辑，统一维护）
     */
    private static void validateIndustryName(String industryName) {
        // 非空校验（与表单@NotNull注解互补，二次保障）
        if (industryName == null || industryName.trim().isEmpty()) {
            throw new BusException(IndustryMapEnum.INDUSTRY_NAME_EMPTY);
        }
        // 长度校验（trim()处理避免纯空格占用长度）
        String trimedName = industryName.trim();
        if (trimedName.length() > INDUSTRY_NAME_MAX_LENGTH) {
            throw new BusException(IndustryMapEnum.INDUSTRY_NAME_TOO_LONG);
        }
    }
}
package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.model.form.admin.ResumeTemplateForm;

/**
 * 简历模板表单校验工具类，仅校验参数合法性（默认值由业务层处理）
 * 核心规则：新增/修改时简历名称长度不超过25个字符 + 行业非空
 * @author shiningCloud2025
 */
public class ResumeTemplateFormValidator {

    /**
     * 验证新增场景表单数据
     * @param resumeTemplateForm 待验证表单
     * @throws BusException 校验失败抛出
     */
    public static void validateForAdd(ResumeTemplateForm resumeTemplateForm) {
        // 简历名称：非空 + trim后长度≤25
        validateResumeTemplateName(resumeTemplateForm.getResumeTemplateName());
        // 模板代码：非空
        validateResumeTemplateReactCode(resumeTemplateForm.getResumeTemplateReactCode());
        // 新增：简历行业非空校验（核心新增）
        validateResumeTemplateIndustry(resumeTemplateForm.getResumeTemplateIndustry());
    }

    /**
     * 验证修改场景表单数据
     * @param resumeTemplateForm 待验证表单
     * @throws BusException 校验失败抛出
     */
    public static void validateForUpdate(ResumeTemplateForm resumeTemplateForm) {
        // 主键：非空
        if (resumeTemplateForm.getResumeTemplateId() == null) {
            throw new BusException(AdminCodeEnum.RESUME_TEMPLATE_ID_EMPTY);
        }
        // 简历名称：非空 + trim后长度≤25
        validateResumeTemplateName(resumeTemplateForm.getResumeTemplateName());
        // 模板代码：非空
        validateResumeTemplateReactCode(resumeTemplateForm.getResumeTemplateReactCode());
        // 新增：简历行业非空校验（核心新增）
        validateResumeTemplateIndustry(resumeTemplateForm.getResumeTemplateIndustry());
    }

    /**
     * 复用校验：简历名称（抽离公共逻辑，减少重复）
     */
    private static void validateResumeTemplateName(String resumeTemplateName) {
        if (resumeTemplateName == null || resumeTemplateName.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.RESUME_TEMPLATE_NAME_EMPTY);
        }
        String trimedName = resumeTemplateName.trim();
        if (trimedName.length() > 25) {
            throw new BusException(AdminCodeEnum.RESUME_TEMPLATE_NAME_TOO_LONG);
        }
    }

    /**
     * 复用校验：简历模板代码（抽离公共逻辑）
     */
    private static void validateResumeTemplateReactCode(String reactCode) {
        if (reactCode == null || reactCode.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.RESUME_TEMPLATE_REACT_CODE_EMPTY);
        }
    }

    /**
     * 新增：复用校验：简历行业（仅校验非空，符合业务需求）
     */
    private static void validateResumeTemplateIndustry(Integer industry) {
        if (industry == null) {
            // 需在 CodeEnum 中新增「简历行业不能为空」的枚举值（核心配套操作）
            throw new BusException(AdminCodeEnum.RESUME_TEMPLATE_INDUSTRY_EMPTY);
        }
    }
}
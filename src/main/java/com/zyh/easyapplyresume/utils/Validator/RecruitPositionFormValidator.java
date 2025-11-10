package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.CodeEnum;
import com.zyh.easyapplyresume.model.form.admin.RecruitPositionForm;

/**
 * RecruitPositionForm检查工具类，用于验证招聘岗位表单数据并设置默认值
 * 支持新增场景（默认值+非空校验+长度校验）和修改场景（主键非空+全字段校验）
 * 不依赖注解，全量通过代码校验，异常统一可控
 * @author shiningCloud2025
 */
public class RecruitPositionFormValidator {

    // 招聘岗位名称最大长度（新增/修改场景共用）
    private static final int POSITION_NAME_MAX_LENGTH = 30;
    // 福利待遇最大长度（新增/修改场景共用）
    private static final int GOOD_WELFARE_MAX_LENGTH = 200;
    // 福利待遇默认值（为空时填充）
    private static final String DEFAULT_GOOD_WELFARE = "福利待遇未知";

    /**
     * 招聘岗位新增场景校验：非空校验 + 默认值填充 + 字段长度校验
     * @param recruitPositionForm 待验证的招聘岗位表单对象
     * @throws BusException 校验失败时抛出（绑定CodeEnum错误码）
     */
    public static void validateForAdd(RecruitPositionForm recruitPositionForm) {
        // 1. 非空校验（所有必填字段，完全替代@NotNull注解）
        validateRequiredFields(recruitPositionForm, false);

        // 2. 福利待遇默认值填充（空值/纯空格时设置默认值）
        handleGoodWelfareDefault(recruitPositionForm);

        // 3. 字段长度校验（trim()处理，避免纯空格占用长度）
        validateFieldLengths(recruitPositionForm);

        // 4. 还原trim后的值（保证入库数据整洁，去除多余空格）
        trimAndResetFields(recruitPositionForm);
    }

    /**
     * 招聘岗位修改场景校验：主键非空 + 全字段非空 + 默认值填充 + 字段长度校验
     * @param recruitPositionForm 待验证的招聘岗位表单对象
     * @throws BusException 校验失败时抛出（绑定CodeEnum错误码）
     */
    public static void validateForUpdate(RecruitPositionForm recruitPositionForm) {
        // 1. 主键非空校验（修改必须指定ID，优先校验）
        if (recruitPositionForm.getRecruitPositionId() == null) {
            throw new BusException(CodeEnum.RECRUIT_UPDATE_ID_EMPTY);
        }

        // 2. 非空校验（所有必填字段，完全替代@NotNull注解）
        validateRequiredFields(recruitPositionForm, true);

        // 3. 福利待遇默认值填充（空值/纯空格时设置默认值）
        handleGoodWelfareDefault(recruitPositionForm);

        // 4. 字段长度校验（trim()处理，避免纯空格占用长度）
        validateFieldLengths(recruitPositionForm);

        // 5. 还原trim后的值（保证入库数据整洁，去除多余空格）
        trimAndResetFields(recruitPositionForm);
    }

    /**
     * 私有工具方法：必填字段非空校验（统一处理，避免代码冗余）
     * @param form 表单对象
     * @param isUpdate 是否为修改场景（修改场景下主键已单独校验）
     */
    private static void validateRequiredFields(RecruitPositionForm form, boolean isUpdate) {
        // 1. 招聘岗位名称非空（字符串需判断null/空串/纯空格）
        if (form.getRecruitPositionName() == null || form.getRecruitPositionName().trim().isEmpty()) {
            throw new BusException(CodeEnum.RECRUIT_POSITION_NAME_EMPTY);
        }

        // 2. 行业代码非空（数值类型直接判断null）
        if (form.getRecruitPositionIndustryCode() == null) {
            throw new BusException(CodeEnum.RECRUIT_INDUSTRY_CODE_EMPTY);
        }

        // 3. 最低月薪非空
        if (form.getMinMonthSalary() == null) {
            throw new BusException(CodeEnum.RECRUIT_MIN_SALARY_EMPTY);
        }

        // 4. 最高月薪非空
        if (form.getMaxMonthSalary() == null) {
            throw new BusException(CodeEnum.RECRUIT_MAX_SALARY_EMPTY);
        }

        // 5. 每周工作天数非空
        if (form.getWeekWorkDayNum() == null) {
            throw new BusException(CodeEnum.RECRUIT_WORK_DAY_EMPTY);
        }
    }

    /**
     * 私有工具方法：福利待遇默认值处理
     */
    private static void handleGoodWelfareDefault(RecruitPositionForm form) {
        if (form.getGoodWelfare() == null || form.getGoodWelfare().trim().isEmpty()) {
            form.setGoodWelfare(DEFAULT_GOOD_WELFARE);
        }
    }

    /**
     * 私有工具方法：字段长度校验
     */
    private static void validateFieldLengths(RecruitPositionForm form) {
        // 岗位名称长度校验（trim后≤30字符）
        String name = form.getRecruitPositionName().trim();
        if (name.length() > POSITION_NAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.RECRUIT_POSITION_NAME_TOO_LONG);
        }

        // 福利待遇长度校验（trim后≤200字符）
        String welfare = form.getGoodWelfare().trim();
        if (welfare.length() > GOOD_WELFARE_MAX_LENGTH) {
            throw new BusException(CodeEnum.RECRUIT_GOOD_WELFARE_TOO_LONG);
        }
    }

    /**
     * 私有工具方法：trim处理并还原字段值（去除多余空格）
     */
    private static void trimAndResetFields(RecruitPositionForm form) {
        // 岗位名称trim后还原
        form.setRecruitPositionName(form.getRecruitPositionName().trim());
        // 福利待遇trim后还原（即使是默认值，trim也不影响）
        form.setGoodWelfare(form.getGoodWelfare().trim());
    }
}
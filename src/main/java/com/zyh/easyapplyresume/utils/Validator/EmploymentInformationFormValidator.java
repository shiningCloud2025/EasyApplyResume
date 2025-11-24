package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.model.form.admin.EmploymentInformationForm;

import java.util.List;

/**
 * EmploymentInformationForm检查工具类，用于验证招聘信息表单数据并设置默认值
 * 支持新增场景（默认值+非空校验+长度校验）和修改场景（主键非空+全字段校验）
 * 不依赖注解，全量通过代码校验，异常统一可控
 * @author shiningCloud2025
 */
public class EmploymentInformationFormValidator {

    // 公司名称最大长度（对应数据库varchar(30)）
    private static final int COMPANY_NAME_MAX_LENGTH = 30;
    // 网申状态最大长度（对应数据库varchar(30)）
    private static final int ONLINE_APP_STATUS_MAX_LENGTH = 30;
    // 官方公告最大长度（对应数据库varchar(1024)）
    private static final int OFFICIAL_ANNOUNCEMENT_MAX_LENGTH = 1024;
    // 投递方式最大长度（对应数据库varchar(1024)）
    private static final int SUBMISSION_WAY_MAX_LENGTH = 1024;
    // 内推码最大长度（对应数据库varchar(255)）
    private static final int EMPLOYEE_REFERRAL_CODE_MAX_LENGTH = 255;
    // 详细招聘地址最大长度（对应数据库varchar(255)）
    private static final int RECRUIT_LOCATION_DETAIL_MAX_LENGTH = 255;

    // 官方公告默认值（为空时填充）
    private static final String DEFAULT_OFFICIAL_ANNOUNCEMENT = "暂无官方公告";
    // 内推码默认值（为空时填充）
    private static final String DEFAULT_EMPLOYEE_REFERRAL_CODE = "暂无内推码";

    /**
     * 招聘信息新增场景校验：非空校验 + 默认值填充 + 字段长度校验
     * @param employmentInformationForm 待验证的招聘信息表单对象
     * @throws BusException 校验失败时抛出（绑定CodeEnum错误码）
     */
    public static void validateForAdd(EmploymentInformationForm employmentInformationForm) {
        // 1. 非空校验（所有必填字段，完全替代@NotNull注解）
        validateRequiredFields(employmentInformationForm);

        // 2. 默认值填充（官方公告、内推码空值/纯空格时设置默认值）
        handleDefaultValues(employmentInformationForm);

        // 3. 字段长度校验（trim()处理，避免纯空格占用长度）
        validateFieldLengths(employmentInformationForm);

        // 4. 还原trim后的值（保证入库数据整洁，去除多余空格）
        trimAndResetFields(employmentInformationForm);
    }

    /**
     * 招聘信息修改场景校验：主键非空 + 全字段非空 + 默认值填充 + 字段长度校验
     * @param employmentInformationForm 待验证的招聘信息表单对象
     * @throws BusException 校验失败时抛出（绑定CodeEnum错误码）
     */
    public static void validateForUpdate(EmploymentInformationForm employmentInformationForm) {
        // 1. 主键非空校验（修改必须指定ID，优先校验）
        if (employmentInformationForm.getEmploymentInformationId() == null) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_UPDATE_ID_EMPTY);
        }

        // 2. 非空校验（所有必填字段，完全替代@NotNull注解）
        validateRequiredFields(employmentInformationForm);

        // 3. 默认值填充（官方公告、内推码空值/纯空格时设置默认值）
        handleDefaultValues(employmentInformationForm);

        // 4. 字段长度校验（trim()处理，避免纯空格占用长度）
        validateFieldLengths(employmentInformationForm);

        // 5. 还原trim后的值（保证入库数据整洁，去除多余空格）
        trimAndResetFields(employmentInformationForm);
    }

    /**
     * 私有工具方法：必填字段非空校验（统一处理，避免代码冗余）
     * @param form 表单对象
     */
    private static void validateRequiredFields(EmploymentInformationForm form) {
        // 1. 公司名称非空（字符串需判断null/空串/纯空格）
        if (form.getEmploymentInformationCompanyName() == null || form.getEmploymentInformationCompanyName().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_COMPANY_NAME_EMPTY);
        }

        // 2. 行业大类非空（数值类型直接判断null）
        if (form.getEmploymentInformationIndustryCategories() == null) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_INDUSTRY_CATEGORIES_EMPTY);
        }

        // 3. 企业性质非空
        if (form.getEmploymentInformationCompanyType() == null) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_COMPANY_TYPE_EMPTY);
        }

        // 4. 招聘批次非空
        if (form.getEmploymentInformationBatch() == null) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_BATCH_EMPTY);
        }

        // 5. 招聘岗位非空
        if (form.getEmploymentInformationRecruitPosition() == null) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_RECRUIT_POSITION_EMPTY);
        }

        // 6. 招聘对象非空
        if (form.getEmploymentInformationRecruitObject() == null) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_RECRUIT_OBJECT_EMPTY);
        }

        // 7. 招聘地址(省级)非空：List不能为null且不能是空集合，集合元素不能为null
        List<Integer> locationFirst = form.getEmploymentInformationRecruitLocationFirst();
        if (locationFirst == null || locationFirst.isEmpty()) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_RECRUIT_LOCATION_FIRST_EMPTY);
        }
        for (int i = 0; i < locationFirst.size(); i++) {
            if (locationFirst.get(i) == null) {
                throw new BusException(AdminCodeEnum.EMPLOYMENT_RECRUIT_LOCATION_FIRST_ELEMENT_NULL.getCode(), "第" + (i + 1) + "个省级地址ID不能为空");
            }
        }

        // 8. 招聘地址(市级)非空：List不能为null且不能是空集合，集合元素不能为null
        List<Integer> locationSecond = form.getEmploymentInformationRecruitLocationSecond();
        if (locationSecond == null || locationSecond.isEmpty()) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_RECRUIT_LOCATION_SECOND_EMPTY);
        }
        for (int i = 0; i < locationSecond.size(); i++) {
            if (locationSecond.get(i) == null) {
                throw new BusException(AdminCodeEnum.EMPLOYMENT_RECRUIT_LOCATION_SECOND_ELEMENT_NULL.getCode(), "第" + (i + 1) + "个市级地址ID不能为空");
            }
        }

        // 9. 截止时间非空
        if (form.getEmploymentInformationStopTime() == null) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_STOP_TIME_EMPTY);
        }

        // 10. 网申状态非空（字符串需判断null/空串/纯空格）
        if (form.getEmploymentInformationOnlineApplicationStatus() == null || form.getEmploymentInformationOnlineApplicationStatus().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_ONLINE_APP_STATUS_EMPTY);
        }

        // 11.txt. 投递方式非空（字符串需判断null/空串/纯空格）
        if (form.getEmploymentInformationSubmissionWay() == null || form.getEmploymentInformationSubmissionWay().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_SUBMISSION_WAY_EMPTY);
        }
    }

    /**
     * 私有工具方法：默认值处理（官方公告、内推码）
     */
    private static void handleDefaultValues(EmploymentInformationForm form) {
        // 官方公告：空值/纯空格时设置默认值
        if (form.getEmploymentInformationOfficialAnnouncement() == null || form.getEmploymentInformationOfficialAnnouncement().trim().isEmpty()) {
            form.setEmploymentInformationOfficialAnnouncement(DEFAULT_OFFICIAL_ANNOUNCEMENT);
        }

        // 内推码：空值/纯空格时设置默认值
        if (form.getEmploymentInformationEmployeeReferralCode() == null || form.getEmploymentInformationEmployeeReferralCode().trim().isEmpty()) {
            form.setEmploymentInformationEmployeeReferralCode(DEFAULT_EMPLOYEE_REFERRAL_CODE);
        }
    }

    /**
     * 私有工具方法：字段长度校验
     */
    private static void validateFieldLengths(EmploymentInformationForm form) {
        // 1. 公司名称长度校验（trim后≤30字符）
        String companyName = form.getEmploymentInformationCompanyName().trim();
        if (companyName.length() > COMPANY_NAME_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_COMPANY_NAME_TOO_LONG);
        }

        // 2. 网申状态长度校验（trim后≤30字符）
        String appStatus = form.getEmploymentInformationOnlineApplicationStatus().trim();
        if (appStatus.length() > ONLINE_APP_STATUS_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_ONLINE_APP_STATUS_TOO_LONG);
        }

        // 3. 官方公告长度校验（trim后≤1024字符）
        String announcement = form.getEmploymentInformationOfficialAnnouncement().trim();
        if (announcement.length() > OFFICIAL_ANNOUNCEMENT_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_OFFICIAL_ANNOUNCEMENT_TOO_LONG);
        }

        // 4. 投递方式长度校验（trim后≤1024字符）
        String submissionWay = form.getEmploymentInformationSubmissionWay().trim();
        if (submissionWay.length() > SUBMISSION_WAY_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_SUBMISSION_WAY_TOO_LONG);
        }

        // 5. 内推码长度校验（trim后≤255字符）
        String referralCode = form.getEmploymentInformationEmployeeReferralCode().trim();
        if (referralCode.length() > EMPLOYEE_REFERRAL_CODE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.EMPLOYMENT_REFERRAL_CODE_TOO_LONG);
        }

        // 6. 详细招聘地址长度校验（可选字段，有值时trim后≤255字符）
        String locationDetail = form.getEmploymentInformationRecruitLocationDetail();
        if (locationDetail != null && !locationDetail.trim().isEmpty()) {
            if (locationDetail.trim().length() > RECRUIT_LOCATION_DETAIL_MAX_LENGTH) {
                throw new BusException(AdminCodeEnum.EMPLOYMENT_RECRUIT_LOCATION_DETAIL_TOO_LONG);
            }
        }
    }

    /**
     * 私有工具方法：trim处理并还原字段值（去除多余空格）
     */
    private static void trimAndResetFields(EmploymentInformationForm form) {
        // 1. 公司名称trim后还原
        form.setEmploymentInformationCompanyName(form.getEmploymentInformationCompanyName().trim());

        // 2. 网申状态trim后还原
        form.setEmploymentInformationOnlineApplicationStatus(form.getEmploymentInformationOnlineApplicationStatus().trim());

        // 3. 官方公告trim后还原（即使是默认值，trim也不影响）
        form.setEmploymentInformationOfficialAnnouncement(form.getEmploymentInformationOfficialAnnouncement().trim());

        // 4. 投递方式trim后还原
        form.setEmploymentInformationSubmissionWay(form.getEmploymentInformationSubmissionWay().trim());

        // 5. 内推码trim后还原（即使是默认值，trim也不影响）
        form.setEmploymentInformationEmployeeReferralCode(form.getEmploymentInformationEmployeeReferralCode().trim());

        // 6. 详细招聘地址trim后还原（空值/纯空格时设为null，保证入库整洁）
        String locationDetail = form.getEmploymentInformationRecruitLocationDetail();
        if (locationDetail != null) {
            String trimmedDetail = locationDetail.trim();
            form.setEmploymentInformationRecruitLocationDetail(trimmedDetail.isEmpty() ? null : trimmedDetail);
        }
    }
}
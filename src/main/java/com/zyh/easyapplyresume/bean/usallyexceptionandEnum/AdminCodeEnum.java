package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举类-管理端
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum AdminCodeEnum {
    // 正常
    SUCCESS(200,"OK"),
    // 系统异常
    SYSTEM_ERROR(500,"系统异常"),
    // 验证码错误
    CHECKCODE_ERROR(601,"验证码错误"),
    // 账户或密码错误
    USERNAMEORPASSWORD_ERROR(602,"账户或者密码错误"),

    // ==================== 管理员相关(603-629)  ====================
    // ==================== 通用格式错误（新增/修改均适用）====================
    ADMIN_PHONE_FORMAT_ERROR(603, "手机号必须是11位有效数字（示例：13800138000）"),
    ADMIN_EMAIL_FORMAT_ERROR(604, "邮箱格式不正确（示例：admin@company.com）"),
    // ==================== 通用长度超限（新增/修改均适用）====================
    ADMIN_USERNAME_TOO_LONG(605, "账号名长度不能超过15个字符"),
    ADMIN_EMAIL_TOO_LONG(606, "邮箱长度不能超过25个字符"),
    ADMIN_INTRO_TOO_LONG(607, "管理员介绍长度不能超过200个字符"),
    ADMIN_PASSWORD_LENGTH_ERROR(608, "密码长度必须为6-20位（建议字母+数字组合）"),
    // ==================== 新增场景：必填项为空（账号名+手机号+邮箱）====================
    ADMIN_ADD_USERNAME_EMPTY(609, "新增管理员：账号名不能为空"),
    ADMIN_ADD_PHONE_EMPTY(610, "新增管理员：手机号不能为空"),
    ADMIN_ADD_EMAIL_EMPTY(611, "新增管理员：邮箱不能为空"), // 新增邮箱必填错误码
    // ==================== 修改场景：全字段非空（强制必填）====================
    ADMIN_UPDATE_USERNAME_EMPTY(612, "修改管理员：账号名不能为空"),
    ADMIN_UPDATE_PHONE_EMPTY(613, "修改管理员：手机号不能为空"),
    ADMIN_UPDATE_EMAIL_EMPTY(614, "修改管理员：邮箱不能为空"),
    ADMIN_UPDATE_PASSWORD_EMPTY(615, "修改管理员：密码不能为空"),
    ADMIN_UPDATE_IMAGE_EMPTY(616, "修改管理员：头像路径不能为空"),
    ADMIN_UPDATE_INTRO_EMPTY(617, "修改管理员：介绍不能为空"),
    ADMIN_UPDATE_STATE_EMPTY(618, "修改管理员：状态不能为空"),
    // ==================== 通用非法值（新增/修改均适用）====================
    ADMIN_STATE_ILLEGAL(619, "管理员状态非法（仅支持：0=禁用，1=正常）"),
    ADMIN_USERNAME_DUPLICATE(620, "账号名已存在"),
    ADMIN_PHONE_DUPLICATE(621, "手机号已存在"),
    ADMIN_EMAIL_DUPLICATE(622, "邮箱已存在"),
    DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION(623, "数据库异常没有转换成业务异常异常"),
    NO_DELETE_SUPER_ADMIN(624, "超级管理员不能被删除"),
    NO_UPDATE_SUPER_ADMIN(625, "超级管理员不能被修改"),

    // ==================== 角色相关（630-639）====================
    // 新增场景必填项为空
    ROLE_ADD_NAME_EMPTY(630, "新增角色：角色名称不能为空"),

    // 修改场景必填项为空
    ROLE_UPDATE_ID_EMPTY(631, "修改角色：角色ID不能为空"),
    ROLE_UPDATE_NAME_EMPTY(632, "修改角色：角色名称不能为空"),
    ROLE_UPDATE_INTRODUCE_EMPTY(633, "修改角色：角色简介不能为空"),

    // 长度超限
    ROLE_NAME_TOO_LONG(634, "角色名称长度不能超过12个字符"),
    ROLE_INTRODUCE_TOO_LONG(635, "角色简介长度不能超过30个字符"),
    ROLE_NAME_DUPLICATE(636, "角色名称已存在"),
    NO_UPDATE_SUPER_ADMNIN_ROLE(634, "超级管理员角色不能被修改"),
    NO_DELETE_SUPER_ADMNIN_ROLE(635, "超级管理员角色不能被删除"),


    // ==================== 权限相关（640-648）====================
    // 修改场景必填项为空（640-645）
    PERMISSION_UPDATE_ID_EMPTY(640, "修改权限：权限ID不能为空"),
    PERMISSION_UPDATE_NAME_EMPTY(643, "修改权限：权限名称不能为空"),
    PERMISSION_UPDATE_URL_EMPTY(644, "修改权限：权限URL不能为空"),
    PERMISSION_UPDATE_INTRO_EMPTY(645, "修改权限：权限简介不能为空"),

    // 新增场景必填项为空（641-642）
    PERMISSION_ADD_NAME_EMPTY(641, "新增权限：权限名称不能为空"),
    PERMISSION_ADD_URL_EMPTY(642, "新增权限：权限URL不能为空"),

    // 长度超限（646-648）
    PERMISSION_NAME_TOO_LONG(646, "权限名称长度不能超过20个字符"),
    PERMISSION_URL_TOO_LONG(647, "权限URL长度不能超过50个字符"),
    PERMISSION_INTRO_TOO_LONG(648, "权限简介长度不能超过30个字符"),
    PERMISSION_NAME_DUPLICATE(649, "权限名称已存在"), // 新增：名称唯一冲突
    PERMISSION_URL_DUPLICATE(650, "权限URL已存在"),   // 新增：URL唯一冲突


    // ------------------- 新增：ResumeTemplate校验错误码（655开始）-------------------
    RESUME_TEMPLATE_ID_EMPTY(655,"修改简历模板必须指定模板ID"),
    RESUME_TEMPLATE_NAME_EMPTY(656,"简历模板名称不能为空"),
    RESUME_TEMPLATE_NAME_TOO_LONG(657,"简历模板名称长度不能超过25个字符"),
    RESUME_TEMPLATE_REACT_CODE_EMPTY(658,"简历模板代码不能为空"),


    // ------------------- 新增：JobAdviceArticleForm表单验证错误码（从670开始）-------------------
    JOB_ADVICE_ID_EMPTY(670,"修改求职攻略文章必须指定文章ID"),
    JOB_ADVICE_TITLE_EMPTY(671,"求职攻略文章标题不能为空"),
    JOB_ADVICE_UPDATE_TITLE_EMPTY(672,"修改时求职攻略文章标题不能为空"),
    JOB_ADVICE_TITLE_TOO_LONG(673,"求职攻略文章标题不能超过30个字符"),
    JOB_ADVICE_CATEGORY_TOO_LONG(674,"求职攻略文章分类不能超过30个字符"),
    JOB_ADVICE_TAGS_TOO_LONG(675,"求职攻略文章标签不能超过30个字符"),
    JOB_ADVICE_AUTHOR_NAME_TOO_LONG(676,"求职攻略文章作者名称不能超过30个字符"),
    RESUME_TEMPLATE_INDUSTRY_EMPTY(677, "简历模版行业不能为空"),

    // 招聘岗位校验相关错误码（685-692）
    RECRUIT_UPDATE_ID_EMPTY(685, "修改招聘岗位必须指定岗位ID"),
    RECRUIT_POSITION_NAME_EMPTY(686, "招聘岗位名称不能为空"),
    RECRUIT_INDUSTRY_CODE_EMPTY(687, "招聘岗位所属行业代码不能为空"),
    RECRUIT_MIN_SALARY_EMPTY(688, "招聘岗位最低月薪不能为空"),
    RECRUIT_MAX_SALARY_EMPTY(689, "招聘岗位最高月薪不能为空"),
    RECRUIT_WORK_DAY_EMPTY(690, "招聘岗位每周工作天数不能为空"),
    RECRUIT_POSITION_NAME_TOO_LONG(691, "招聘岗位名称不能超过30个字符"),
    RECRUIT_GOOD_WELFARE_TOO_LONG(692, "福利待遇不能超过200个字符"),
    NOT_DELETE_RECRUIT_POSITION(693, "该岗位正在被使用，请勿删除"),

    // ------------------- 新增：EmploymentInformationForm表单验证错误码（705开始）-------------------
    EMPLOYMENT_UPDATE_ID_EMPTY(705, "修改招聘信息必须指定招聘信息ID"),
    EMPLOYMENT_COMPANY_NAME_EMPTY(706, "公司名称不能为空"),
    EMPLOYMENT_COMPANY_NAME_TOO_LONG(707, "公司名称长度不能超过30个字符"),
    EMPLOYMENT_INDUSTRY_CATEGORIES_EMPTY(708, "行业大类不能为空"),
    EMPLOYMENT_COMPANY_TYPE_EMPTY(709, "企业性质不能为空"),
    EMPLOYMENT_BATCH_EMPTY(710, "招聘批次不能为空"),
    EMPLOYMENT_RECRUIT_POSITION_EMPTY(711, "招聘岗位不能为空"),
    EMPLOYMENT_RECRUIT_OBJECT_EMPTY(712, "招聘对象不能为空"),
    EMPLOYMENT_RECRUIT_LOCATION_FIRST_EMPTY(713, "招聘地址(省级)不能为空"),
    EMPLOYMENT_RECRUIT_LOCATION_FIRST_ELEMENT_NULL(714, "省级地址ID存在空值"),
    EMPLOYMENT_RECRUIT_LOCATION_SECOND_EMPTY(715, "招聘地址(市级)不能为空"),
    EMPLOYMENT_RECRUIT_LOCATION_SECOND_ELEMENT_NULL(716, "市级地址ID存在空值"),
    EMPLOYMENT_STOP_TIME_EMPTY(717, "截止时间不能为空"),
    EMPLOYMENT_ONLINE_APP_STATUS_EMPTY(718, "网申状态不能为空"),
    EMPLOYMENT_ONLINE_APP_STATUS_TOO_LONG(719, "网申状态长度不能超过30个字符"),
    EMPLOYMENT_SUBMISSION_WAY_EMPTY(720, "投递方式不能为空"),
    EMPLOYMENT_SUBMISSION_WAY_TOO_LONG(721, "投递方式长度不能超过1024个字符"),
    EMPLOYMENT_OFFICIAL_ANNOUNCEMENT_TOO_LONG(722, "官方公告长度不能超过1024个字符"),
    EMPLOYMENT_REFERRAL_CODE_TOO_LONG(723, "内推码长度不能超过255个字符"),
    EMPLOYMENT_RECRUIT_LOCATION_DETAIL_TOO_LONG(724, "详细招聘地址长度不能超过255个字符"),
    EMPLOYMENT_LOCATION_LENGTH_NOT_MATCH(725, "招聘地址(省级)和(市级)长度不一致"),


    // -------------------- 新增：邮箱验证相关状态 (从750开始)-------------------
    EMAIL_NO_EFFECT(750, "请输入有效的邮箱地址"),
    EMAIL_SEND_FREQUENCY(751, "验证码已发送，请3分钟后再试"), // 带参数的提示
    EMAIL_VERIFY_SEND_FAIL(752, "验证码发送失败，请检查邮箱地址或稍后重试"),
    EMAIL_VERIFY_CODE_INVALID(753, "验证码无效或已过期"),
    EMAIL_VERIFY_CODE_SUCCESS(754, "验证码验证成功"),


    // ==================== 短信相关枚举（从 770 开始）====================
    SMS_PHONE_FORMAT_ERROR(770, "手机号格式不正确，请输入11位有效手机号"),
    SMS_SEND_FREQUENCY(771, "短信发送过于频繁，请3分钟后再试"),
    SMS_SEND_FAIL(772, "短信发送失败，请稍后重试"),
    SMS_VERIFY_CODE_INVALID(773, "短信验证码无效或已过期"),
    SMS_CONFIG_ERROR(774, "短信服务配置缺失，请联系管理员"),

    // 大模型应用用户输入为空
    LLM_USERINPUT_EMPTY(901,"用户输入为空")
    ;


    private final Integer code;
    private final String message;
}

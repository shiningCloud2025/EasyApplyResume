package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举类-用户端
 * @author shiningCloud2025
 */
// 10001-20000
@Getter
@AllArgsConstructor
public enum UserCodeEnum {
    // ==================== 通用状态 ====================
    SUCCESS(200,"OK"),
    SYSTEM_ERROR(500,"系统异常"),

    // ==================== 登录/注册/修改相关(注册修改都用的) (10001-10020) ====================
    ACCOUNT_OR_PASSWORD_ERROR(10001, "账号或密码错误"),
    NO_REGISTER_ERROR(10002, "用户未注册"),
    USER_ACCOUNT_EMPTY(10003, "用户账号不能为空"),
    USER_ACCOUNT_LENGTH_ERROR(10004, "用户账号长度必须在7-10位之间"),
    USER_ACCOUNT_FIRST_CHAR_ZERO(10005, "用户账号首位不能为0"),
    USER_USERNAME_EMPTY(10006, "用户名称不能为空"),
    USER_USERNAME_TOO_LONG(10007, "用户名称长度不能超过20位"),
    USER_EMAIL_TOO_LONG(10008, "用户邮箱长度不能超过25位"),
    USER_EMAIL_FORMAT_ERROR(10009, "用户邮箱格式不正确"),
    USER_PHONE_FORMAT_ERROR(10010, "用户手机号格式不正确"),
    USER_PASSWORD_EMPTY(10011, "用户密码不能为空"),
    USER_PASSWORD_LENGTH_ERROR(10012, "用户密码长度必须在6-30位之间"),
    USER_IMAGE_TOO_LONG(10013, "用户头像路径长度不能超过255位"),
    USER_INTRODUCE_TOO_LONG(10014, "用户简介长度不能超过200位"),
    USER_GOOD_WELFARE_TOO_LONG(10015, "用户希望的福利待遇长度不能超过200位"),
    USER_LOCATION_DETAIL_TOO_LONG(10016, "用户详细地址长度不能超过255位"),
    USER_MIN_SALARY_NEGATIVE(10017, "用户最低月薪不能为负数"),
    USER_MIN_SALARY_TOO_HIGH(10018, "用户最低月薪不能超过100000"),
    USER_MAX_SALARY_LESS_THAN_MIN(10019, "用户最高月薪不能低于最低月薪"),
    USER_WEEK_WORK_DAY_NUM_ILLEGAL(10020, "用户希望的每周工作数必须在0-7之间"),
    USER_ACCOUNT_DUPLICATE(10021, "用户账号已存在"),
    USER_EMAIL_DUPLICATE(10022, "用户邮箱已存在"),
    USER_PHONE_DUPLICATE(10023, "用户手机号已存在"),
    UERR_ALREADY_EXIST(10024, "用户已存在"),
    USER_REGISTER_FORM_NOT_NULL(10025, "用户注册表单不能为空"),
    GENERATE_ACCOUNT_FAIL(10026, "生成随机账号失败，请重试"),
    USER_LOCATION_INCOMPLETE(10027, "省份和城市必须同时填写或同时不填"),

    // ==================== 用户修改必填校验 (10028-10035) ====================
    USER_IMAGE_EMPTY(10028, "用户头像不能为空"),
    USER_INTRODUCE_EMPTY(10029, "用户简介不能为空"),
    USER_DREAM_MIN_SALARY_EMPTY(10030, "用户希望的最低月薪不能为空"),
    USER_DREAM_MAX_SALARY_EMPTY(10031, "用户希望的最高月薪不能为空"),
    USER_DREAM_WEEK_WORK_DAY_EMPTY(10032, "用户希望的每周工作数不能为空"),
    USER_DREAM_GOOD_WELFARE_EMPTY(10033, "用户希望的福利待遇不能为空"),
    USER_RECRUIT_LOCATION_EMPTY(10034, "用户地址不能为空"),
    USER_UNIVERSITY_CODE_EMPTY(10035, "用户大学编码不能为空"),


    // ==================== 用户简历相关异常 (10036-10050) ====================
    USER_SAVE_RESUME_NOT_DAYU_FIVE(10036, "用户保存的简历不能超过5个"),



    // ===================== 用户反馈相关业务(10051-10080) ====================
    // 用户反馈相关错误码（10051起）
    USER_FEEDBACK_TITLE_NOT_NULL(10051, "反馈标题不能为空"),
    USER_FEEDBACK_CONTENT_NOT_NULL(10052, "反馈内容不能为空"),
    USER_FEEDBACK_CONTENT_EMPTY(10053, "反馈内容不能为空"),
    USER_FEEDBACK_USER_ID_EMPTY(10054, "提交反馈的用户ID不能为空"),
    USER_FEEDBACK_TITLE_TOO_LONG(10055, "反馈标题不能超过35个字符"),

    // ===================== 用户发送简历给HR相关业务(10081-10099) ====================
    // 简历发送给HR专用枚举（从10081开始连续分配）
    USER_RESUME_EMAIL_EMPTY(10081, "目标邮箱不能为空"),
    USER_RESUME_EMAIL_FORMAT_ERROR(10082, "邮箱格式不正确"),
    USER_RESUME_EMAIL_TOO_LONG(10083, "邮箱长度不能超过25个字符"),
    USER_RESUME_TITLE_EMPTY(10084, "标题不能为空"),
    USER_RESUME_TITLE_TOO_LONG(10085, "标题长度不能超过35个字符"),
    USER_RESUME_CONTENT_EMPTY(10086, "内容不能为空"),
    USER_RESUME_FILE_TOO_LARGE(10087, "简历文件大小不能超过3MB"),// 原10086改为10087，删除文件空枚举
    USER_RESUME_SEND_FAIL(10088, "简历发送失败"),

    // ==================== 公告业务相关 (10100-10120) ====================
    USER_ANNOUNCEMENT_TITLE_EMPTY(10100, "用户公告标题不能为空"),
    USER_ANNOUNCEMENT_TITLE_TOO_LONG(10101, "用户公告标题不能超过35个字符"),
    USER_ANNOUNCEMENT_CONTENT_EMPTY(10102, "修改用户公告时内容不能为空"),
    USER_ALREADY_ADD_ANNOUNCEMENT(10103, "用户已添加过公告"),
    USER_ADD_ANNOUNCEMENT_FAIL(10104, "用户添加公告失败"),
    USER_UPDATE_ANNOUNCEMENT_FAIL(10105, "用户修改公告失败"),
    USER_GET_ANNOUNCEMENT_INFO_FAIL(10106, "用户获取公告信息失败"),

    // ==================== 用户端题库题目业务 (10120-10140) ====================
    USER_QUESTION_BANK_USER_ID_EMPTY(10120, "用户id不能为空"),
    USER_QUESTION_BANK_PAGE_FAIL(10121, "分页查询用户端题库题目失败"),
    USER_QUESTION_BANK_ID_EMPTY(10122, "题库题目id不能为空"),
    USER_QUESTION_BANK_NOT_FOUND(10123, "题库题目不存在"),
    USER_QUESTION_BANK_INFO_FAIL(10124, "查询用户端题库题目详情失败"),

    // ==================== 数据库异常 (10999) ====================
    DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION(10999, "数据库异常转换失败"),
    // ==================== 通用参数异常  ====================
    COMMON_PARAM_EMPTY(11999, "参数不能为空");

    ;

    private final Integer code;
    private final String message;
}

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

    // ==================== 10001-10002 ====================
    ACCOUNT_OR_PASSWORD_ERROR(10001, "账号或密码错误"),
    NO_REGISTER_ERROR(10002, "用户未注册"),

    // ==================== 10003-10005 ====================
    USER_ACCOUNT_EMPTY(10003, "用户账号不能为空"),
    USER_ACCOUNT_LENGTH_ERROR(10004, "用户账号长度必须在7-10位之间"),
    USER_ACCOUNT_FIRST_CHAR_ZERO(10005, "用户账号首位不能为0"),
    
    // ==================== 10006-10007 ====================
    USER_USERNAME_EMPTY(10006, "用户名称不能为空"),
    USER_USERNAME_TOO_LONG(10007, "用户名称长度不能超过20位"),
    
    // ==================== 10008-10009 ====================
    USER_EMAIL_TOO_LONG(10008, "用户邮箱长度不能超过25位"),
    USER_EMAIL_FORMAT_ERROR(10009, "用户邮箱格式不正确"),
    
    // ==================== 10010 ====================
    USER_PHONE_FORMAT_ERROR(10010, "用户手机号格式不正确"),
    
    // ==================== 10011-10012 ====================
    USER_PASSWORD_EMPTY(10011, "用户密码不能为空"),
    USER_PASSWORD_LENGTH_ERROR(10012, "用户密码长度必须在6-30位之间"),
    
    // ==================== 10013-10014 ====================
    USER_IMAGE_TOO_LONG(10013, "用户头像路径长度不能超过255位"),
    USER_INTRODUCE_TOO_LONG(10014, "用户简介长度不能超过200位"),
    
    // ==================== 10015-10016 ====================
    USER_GOOD_WELFARE_TOO_LONG(10015, "用户希望的福利待遇长度不能超过200位"),
    USER_LOCATION_DETAIL_TOO_LONG(10016, "用户详细地址长度不能超过255位"),
    
    // ==================== 10017-10020 ====================
    USER_MIN_SALARY_NEGATIVE(10017, "用户最低月薪不能为负数"),
    USER_MIN_SALARY_TOO_HIGH(10018, "用户最低月薪不能超过100000"),
    USER_MAX_SALARY_LESS_THAN_MIN(10019, "用户最高月薪不能低于最低月薪"),
    USER_WEEK_WORK_DAY_NUM_ILLEGAL(10020, "用户希望的每周工作数必须在0-7之间"),
    
    // ==================== 10021-10027 ====================
    USER_ACCOUNT_DUPLICATE(10021, "用户账号已存在"),
    USER_EMAIL_DUPLICATE(10022, "用户邮箱已存在"),
    USER_PHONE_DUPLICATE(10023, "用户手机号已存在"),
    UERR_ALREADY_EXIST(10024, "用户已存在"),
    USER_REGISTER_FORM_NOT_NULL(10025, "用户注册表单不能为空"),
    GENERATE_ACCOUNT_FAIL(10026, "生成随机账号失败，请重试"),
    USER_LOCATION_INCOMPLETE(10027, "省份和城市必须同时填写或同时不填"),

    // ==================== 10028-10035 ====================
    USER_IMAGE_EMPTY(10028, "用户头像不能为空"),
    USER_INTRODUCE_EMPTY(10029, "用户简介不能为空"),
    USER_DREAM_MIN_SALARY_EMPTY(10030, "用户希望的最低月薪不能为空"),
    USER_DREAM_MAX_SALARY_EMPTY(10031, "用户希望的最高月薪不能为空"),
    USER_DREAM_WEEK_WORK_DAY_EMPTY(10032, "用户希望的每周工作数不能为空"),
    USER_DREAM_GOOD_WELFARE_EMPTY(10033, "用户希望的福利待遇不能为空"),
    USER_RECRUIT_LOCATION_EMPTY(10034, "用户地址不能为空"),
    USER_UNIVERSITY_CODE_EMPTY(10035, "用户大学编码不能为空"),

    // ==================== 10999 ====================
    DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION(10999, "数据库异常转换失败")

    ;

    private final Integer code;
    private final String message;
}

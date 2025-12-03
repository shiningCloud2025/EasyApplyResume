package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举类-用户端端
 * @author shiningCloud2025
 */
// 10001-20000
@Getter
@AllArgsConstructor
public enum UserCodeEnum {
    // 正常
    SUCCESS(200,"OK"),
    // 系统异常
    SYSTEM_ERROR(500,"系统异常"),


    ACCOUNT_OR_PASSWORD_ERROR(10001, "账号或密码错误"),
    NO_REGISTER_ERROR(10002, "用户未注册"),

    USER_ACCOUNT_EMPTY(10003, "用户账号不能为空"),
    USER_ACCOUNT_LENGTH_ERROR(10004, "用户账号长度必须在7-10位之间"),
    USER_USERNAME_EMPTY(10005, "用户名称不能为空"),
    USER_USERNAME_TOO_LONG(10006, "用户名称长度不能超过20位"),
    USER_EMAIL_TOO_LONG(10007, "用户邮箱长度不能超过25位"),
    USER_EMAIL_FORMAT_ERROR(10008, "用户邮箱格式不正确"),
    USER_PHONE_FORMAT_ERROR(10009, "用户手机号格式不正确"),
    USER_PASSWORD_EMPTY(10010, "用户密码不能为空"),
    USER_PASSWORD_LENGTH_ERROR(10011, "用户密码长度必须在6-30位之间"),
    USER_IMAGE_TOO_LONG(10012, "用户头像路径长度不能超过255位"),
    USER_INTRODUCE_TOO_LONG(10013, "用户简介长度不能超过200位"),
    USER_GOOD_WELFARE_TOO_LONG(10014, "用户希望的福利待遇长度不能超过200位"),
    USER_LOCATION_DETAIL_TOO_LONG(10015, "用户详细地址长度不能超过255位"),
    USER_MIN_SALARY_NEGATIVE(10016, "用户最低月薪不能为负数"),
    USER_MIN_SALARY_TOO_HIGH(10017, "用户最低月薪不能超过100000"),
    USER_MAX_SALARY_LESS_THAN_MIN(10018, "用户最高月薪不能低于最低月薪"),
    USER_WEEK_WORK_DAY_NUM_ILLEGAL(10019, "用户希望的每周工作数必须在0-7之间"),

    USER_ACCOUNT_DUPLICATE(10020, "用户账号已存在"),
    USER_EMAIL_DUPLICATE(10021, "用户邮箱已存在"),
    USER_PHONE_DUPLICATE(10022, "用户手机号已存在"),
    UERR_ALREADY_EXIST(10023, "用户已存在"),
    DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION(10999, "数据库异常转换失败"),





    ;

    private final Integer code;
    private final String message;
}

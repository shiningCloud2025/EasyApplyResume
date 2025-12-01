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








    ;

    private final Integer code;
    private final String message;
}

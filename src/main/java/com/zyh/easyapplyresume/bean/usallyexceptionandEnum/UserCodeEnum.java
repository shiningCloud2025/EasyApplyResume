package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举类-用户端端
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum UserCodeEnum {
    // 正常
    SUCCESS(200,"OK"),
    // 系统异常
    SYSTEM_ERROR(500,"系统异常");

    private final Integer code;
    private final String message;
}

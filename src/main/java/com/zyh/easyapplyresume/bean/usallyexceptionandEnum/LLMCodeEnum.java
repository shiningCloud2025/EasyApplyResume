package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举类-大模型业务
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum LLMCodeEnum {
    // 正常
    SUCCESS(200,"OK"),
    // 系统异常
    SYSTEM_ERROR(500,"系统异常"),


    // 大模型应用用户输入为空
    LLM_USERINPUT_EMPTY(901,"用户输入为空")


    ;
    private final Integer code;
    private final String message;
}

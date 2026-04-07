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
    LLM_USERINPUT_EMPTY(901,"用户输入为空"),
    // 大模型调用日志参数错误
    LLM_USEPARAM_ERROR(902,"大模型调用日志参数错误")


    ;
    private final Integer code;
    private final String message;
}

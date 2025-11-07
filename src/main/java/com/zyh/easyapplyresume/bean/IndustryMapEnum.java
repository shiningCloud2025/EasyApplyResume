package com.zyh.easyapplyresume.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 行业Map枚举类
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum IndustryMapEnum {
    // 新增行业映射相关错误码（从20000开始）
    INDUSTRY_NAME_EMPTY(20000, "行业名称不能为空"),
    INDUSTRY_NAME_TOO_LONG(20001, "行业名称长度不能超过35个字符") ;

    private final Integer code;
    private final String message;
}

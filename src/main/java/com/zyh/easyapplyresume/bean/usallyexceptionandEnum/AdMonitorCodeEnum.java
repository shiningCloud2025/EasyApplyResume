package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举类-监测与广告端
 * @author shiningCloud2025
 */
// 0-10000
@Getter
@AllArgsConstructor
public enum AdMonitorCodeEnum {
    // 正常
    SUCCESS(200,"OK"),
    // 系统异常
    SYSTEM_ERROR(500,"系统异常"),
    // ==================== 公告业务相关 (20001-20020) ====================

    ANNOUNCEMENT_TITLE_EMPTY(20001, "监测端公告标题不能为空"),
    ANNOUNCEMENT_TITLE_TOO_LONG(20002, "监测端公告标题长度超出限制"),
    ANNOUNCEMENT_CONTENT_EMPTY(20003, "监测端公告内容不能为空"),
    ADMONITOR_ALREADY_ADD_ANNOUNCEMENT(20004, "监测端已添加过公告"),
    ADMONITOR_ADD_ANNOUNCEMENT_FAILED(20005, "监测端添加公告失败"),
    ADMONITOR_UPDATE_ANNOUNCEMENT_FAILED(20006, "监测端修改公告失败"),
    ADMONITOR_GET_ANNOUNCEMENT_INFO_FAILED(20007, "监测端获取公告信息失败"),


    // ==================== 广告业务相关 (20020-20060) ====================
    ADVERTISEMENT_NAME_EMPTY(20020, "广告名称不能为空"),
    ADVERTISEMENT_NAME_TOO_LONG(20021, "广告名称长度不能超过25个字符"),
    ADVERTISEMENT_URL_EMPTY(20022, "广告URL不能为空"),
    ADVERTISEMENT_LINK_EMPTY(20023, "广告链接不能为空"),
    ADVERTISEMENT_LINK_TOO_LONG(20024, "广告链接长度不能超过3000个字符"),
    ADVERTISEMENT_START_TIME_EMPTY(20025, "广告开始时间不能为空"),
    ADVERTISEMENT_END_TIME_EMPTY(20026, "广告结束时间不能为空"),
    ADVERTISEMENT_TIME_ILLEGAL(20027, "广告开始时间不能晚于结束时间"),
    ADVERTISEMENT_ID_ILLEGAL(20028, "广告ID不合法"),

    ;







    ;

    private final Integer code;
    private final String message;
}

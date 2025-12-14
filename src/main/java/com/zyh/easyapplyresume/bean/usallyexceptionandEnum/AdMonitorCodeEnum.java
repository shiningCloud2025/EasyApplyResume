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
    // ==================== 公告业务相关 (20001-30000) ====================

    ANNOUNCEMENT_TITLE_EMPTY(20001, "监测端公告标题不能为空"),
    ANNOUNCEMENT_TITLE_TOO_LONG(20002, "监测端公告标题长度超出限制"),
    ANNOUNCEMENT_CONTENT_EMPTY(20003, "监测端公告内容不能为空"),
    ADMONITOR_ALREADY_ADD_ANNOUNCEMENT(20004, "监测端已添加过公告"),
    ADMONITOR_ADD_ANNOUNCEMENT_FAILED(20005, "监测端添加公告失败"),
    ADMONITOR_UPDATE_ANNOUNCEMENT_FAILED(20006, "监测端修改公告失败"),
    ADMONITOR_GET_ANNOUNCEMENT_INFO_FAILED(20007, "监测端获取公告信息失败"),








    ;

    private final Integer code;
    private final String message;
}

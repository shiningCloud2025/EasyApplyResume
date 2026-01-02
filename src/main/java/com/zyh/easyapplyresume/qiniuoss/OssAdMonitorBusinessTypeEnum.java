package com.zyh.easyapplyresume.qiniuoss;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 监测端OSS业务类型枚举类
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum OssAdMonitorBusinessTypeEnum implements OssBusinessType{
    ADMONITOR_ADMIN_AD_IMG("admonitoradminadimg"),
    ADMONITOR_USER_AD_IMG("admonitoruseradimg"),
    ADMONITOR_ADMONITOR_AD_IMG("admonitoradmonitoradimg")

    ;
    private final String dir;
}

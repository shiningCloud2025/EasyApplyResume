package com.zyh.easyapplyresume.qiniuoss;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OSS系统类型枚举:主要是为了进行三端隔离
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum OssSystemTypeEnum {
    ADMIN("admin"),
    USER("user"),
    AD_MONITOR("ad_monitor");

    private final String dir;

}

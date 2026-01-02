package com.zyh.easyapplyresume.qiniuoss;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 用户端OSS业务类型枚举类
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum OssUserBusinessTypeEnum implements OssBusinessType{
    USER_HEAD_IMG("userheadimg"),
    USER_AD_IMG("useradimg")

    ;
    private final String dir;
}

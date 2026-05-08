package com.zyh.easyapplyresume.qiniuoss;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 管理端OSS业务类型枚举类
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum OssAdminBusinessTypeEnum implements OssBusinessType{
    ADMIN_HEAD_IMG("adminheadimg"),
    ;
    private final String dir;

}

package com.zyh.easyapplyresume.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缓存操作类型枚举
 * @author shiningCloud2025
 */
@AllArgsConstructor
@Getter
public enum CacheOperationType {
    
    /**
     * 新增操作
     */
    ADD("新增"),
    
    /**
     * 更新操作
     */
    UPDATE("更新"),
    
    /**
     * 删除操作
     */
    DELETE("删除");
    
    private final String description;
    
}

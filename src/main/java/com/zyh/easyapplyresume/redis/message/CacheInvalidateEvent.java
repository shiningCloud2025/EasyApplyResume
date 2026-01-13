package com.zyh.easyapplyresume.redis.message;

import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import org.springframework.context.ApplicationEvent;

/**
 * 缓存失效事件
 * @author shiningCloud2025
 */
public class CacheInvalidateEvent extends ApplicationEvent {
    
    /**
     * 要删除的缓存Key模式（支持通配符）
     * 例如: common_employmentInformation_*
     */
    private final String cacheKeyPattern;
    
    /**
     * 缓存操作类型
     */
    private final CacheOperationType operationType;
    
    public CacheInvalidateEvent(Object source, String cacheKeyPattern, CacheOperationType operationType) {
        super(source);
        this.cacheKeyPattern = cacheKeyPattern;
        this.operationType = operationType;
    }
    
    public String getCacheKeyPattern() {
        return cacheKeyPattern;
    }
    
    public CacheOperationType getOperationType() {
        return operationType;
    }
    
    @Override
    public String toString() {
        return "CacheInvalidateEvent{" +
                "cacheKeyPattern='" + cacheKeyPattern + '\'' +
                ", operationType=" + operationType +
                '}';
    }
}

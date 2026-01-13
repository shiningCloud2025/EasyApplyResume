package com.zyh.easyapplyresume.redis.message;

import com.zyh.easyapplyresume.redis.enums.CacheOperationType;

import java.io.Serializable;

/**
 * 缓存失效消息实体
 * @author shiningCloud2025
 */
public class CacheInvalidateMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 要删除的缓存Key模式（支持通配符）
     * 例如: common_employmentInformation_*
     */
    private String cacheKeyPattern;
    
    /**
     * 缓存操作类型
     */
    private CacheOperationType operationType;
    
    public CacheInvalidateMessage() {
    }
    
    public CacheInvalidateMessage(String cacheKeyPattern, CacheOperationType operationType) {
        this.cacheKeyPattern = cacheKeyPattern;
        this.operationType = operationType;
    }
    
    public String getCacheKeyPattern() {
        return cacheKeyPattern;
    }
    
    public void setCacheKeyPattern(String cacheKeyPattern) {
        this.cacheKeyPattern = cacheKeyPattern;
    }
    
    public CacheOperationType getOperationType() {
        return operationType;
    }
    
    public void setOperationType(CacheOperationType operationType) {
        this.operationType = operationType;
    }
    
    @Override
    public String toString() {
        return "CacheInvalidateMessage{" +
                "cacheKeyPattern='" + cacheKeyPattern + '\'' +
                ", operationType=" + operationType +
                '}';
    }
}

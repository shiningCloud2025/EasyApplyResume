package com.zyh.easyapplyresume.redis.util;

import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.message.CacheInvalidateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 缓存失效事件发布工具类
 * @author shiningCloud2025
 */
@Component
public class CacheInvalidatePublisher {
    
    private static final Logger log = LoggerFactory.getLogger(CacheInvalidatePublisher.class);
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * 发布缓存失效事件
     * @param cacheKeyPattern 要删除的缓存Key模式（支持通配符）
     * @param operationType 操作类型
     */
    public void publishInvalidate(String cacheKeyPattern, CacheOperationType operationType) {
        CacheInvalidateEvent event = new CacheInvalidateEvent(this, cacheKeyPattern, operationType);
        eventPublisher.publishEvent(event);
        
        log.info("发布缓存失效事件: cacheKeyPattern={}, operationType={}", 
                cacheKeyPattern, operationType.getDescription());
    }
    
    /**
     * 发布缓存失效事件（默认为UPDATE操作）
     * @param cacheKeyPattern 要删除的缓存Key模式
     */
    public void publishInvalidate(String cacheKeyPattern) {
        publishInvalidate(cacheKeyPattern, CacheOperationType.UPDATE);
    }
}

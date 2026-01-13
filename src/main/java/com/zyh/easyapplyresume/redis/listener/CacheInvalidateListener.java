package com.zyh.easyapplyresume.redis.listener;

import com.zyh.easyapplyresume.redis.message.CacheInvalidateEvent;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 缓存失效事件监听器
 * 监听到事件后只删除缓存，不写入新数据
 * 下次查询时会自动从数据库加载最新数据并缓存
 * @author shiningCloud2025
 */
@Component
public class CacheInvalidateListener {
    
    private static final Logger log = LoggerFactory.getLogger(CacheInvalidateListener.class);
    
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    
    /**
     * 监听缓存失效事件并删除对应缓存
     * @param event 缓存失效事件
     */
    @EventListener
    public void handleCacheInvalidate(CacheInvalidateEvent event) {
        String cacheKeyPattern = event.getCacheKeyPattern();
        
        log.info("收到缓存失效事件: cacheKeyPattern={}, operationType={}", 
                cacheKeyPattern, event.getOperationType().getDescription());
        
        try {
            // 只删除缓存，不写入新数据
            // 下次查询时会从数据库加载最新数据并自动缓存
            redisCacheUtil.deleteByPattern(cacheKeyPattern);
            
            log.info("缓存删除成功: cacheKeyPattern={}", cacheKeyPattern);
            
        } catch (Exception e) {
            log.error("缓存删除失败: cacheKeyPattern={}", cacheKeyPattern, e);
        }
    }
}

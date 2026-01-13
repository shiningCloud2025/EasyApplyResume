package com.zyh.easyapplyresume.redis.constant;

/**
 * Redis缓存基础常量
 * @author shiningCloud2025
 */
public interface RedisCacheConstant {
    
    // ==================== 端标识前缀 ====================
    
    /**
     * 公共数据前缀（B端和C端共享）
     */
    String COMMON_PREFIX = "common";
    
    /**
     * 用户端前缀（C端专属）
     */
    String USER_PREFIX = "user";
    
    /**
     * 管理端前缀（B端专属）
     */
    String ADMIN_PREFIX = "admin";

    /**
     * 广告端前缀（D端专属）
     */
    String ADMONITOR_PREFIX = "admonitor";
    
    /**
     * 系统缓存前缀（消息去重、分布式锁等）
     */
    String SYSTEM_PREFIX = "cache";
    
    // ==================== 发布订阅配置 ====================
    
    /**
     * 缓存失效消息频道
     */
    String CACHE_INVALIDATE_CHANNEL = "cache:invalidate:channel";
    
    /**
     * 消息去重前缀
     * 完整格式: cache:msg:processed_{messageId}
     */
    String PROCESSED_MESSAGE_PREFIX = SYSTEM_PREFIX + ":msg:processed";
    
    /**
     * 消息去重TTL（5分钟）
     */
    int MESSAGE_DEDUP_TTL = 5;
    
    // ==================== 基础配置 ====================
    
    /**
     * Key分隔符
     */
    String DELIMITER = "_";
}

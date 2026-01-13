package com.zyh.easyapplyresume.redis.constant.admonitor;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 监测端公告缓存Key常量
 * Service: AdMonitorAnnouncementService
 * @author shiningCloud2025
 */
public interface AdMonitorAnnouncementCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "admonitorAnnouncement";
    
    // ==================== Key定义 ====================
    
    /**
     * getAnnouncementInfo() - 获取监测端公告信息
     * 格式: admonitor_admonitorAnnouncement_get
     */
    String GET = RedisCacheConstant.ADMONITOR_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "get";
    
    // ==================== 通配符（用于批量删除） ====================
    
    /**
     * 所有监测端公告相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.ADMONITOR_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 公告信息TTL（24小时，基于发布订阅机制保证数据一致性）
     */
    int GET_TTL = 1440;
}

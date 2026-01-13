package com.zyh.easyapplyresume.redis.constant.admonitor;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 用户端广告缓存Key常量
 * Service: AdmonitorUserAdvertisementService
 * @author shiningCloud2025
 */
public interface AdmonitorUserAdvertisementCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "userAdvertisement";
    
    // ==================== Key定义 ====================
    
    /**
     * findAllAdmonitorUserAdvertisement() - 查询所有广告
     * 格式: admonitor_userAdvertisement_list
     */
    String LIST = RedisCacheConstant.ADMONITOR_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "list";
    
    /**
     * findAdmonitorUserAdvertisementByPage(pageNum, pageSize, query) - 分页查询广告
     * 格式: admonitor_userAdvertisement_page_{pageNum}_{pageSize}_{queryHash}
     */
    String PAGE_PREFIX = RedisCacheConstant.ADMONITOR_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "page";
    
    /**
     * findAdmonitorUserAdvertisementById(id) - 查询广告详情
     * 格式: admonitor_userAdvertisement_get_{id}
     */
    String GET_PREFIX = RedisCacheConstant.ADMONITOR_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "get";
    
    // ==================== 通配符（用于批量删除） ====================
    
    /**
     * 所有分页查询缓存通配符
     */
    String PAGE_PATTERN = PAGE_PREFIX + RedisCacheConstant.DELIMITER + "*";
    
    /**
     * 所有详情查询缓存通配符
     */
    String GET_PATTERN = GET_PREFIX + RedisCacheConstant.DELIMITER + "*";
    
    /**
     * 所有用户端广告相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.ADMONITOR_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 全量列表TTL（24小时，基于发布订阅机制保证数据一致性）
     */
    int LIST_TTL = 1440;
    
    /**
     * 分页查询TTL（24小时，基于发布订阅机制保证数据一致性）
     */
    int PAGE_TTL = 1440;
    
    /**
     * 详情查询TTL（24小时，基于发布订阅机制保证数据一致性）
     */
    int GET_TTL = 1440;
}

package com.zyh.easyapplyresume.redis.constant.common;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 行业Map缓存Key常量
 * Service: IndustryMapService
 * @author shiningCloud2025
 */
public interface IndustryMapCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "industryMap";
    
    // ==================== Key定义 ====================
    
    /**
     * findAllIndustryMap() - 查询所有行业Map
     * 格式: common_industryMap_list
     */
    String LIST = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "list";
    
    // ==================== 通配符（用于批量删除） ====================
    
    /**
     * 所有行业Map相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 全量列表TTL（24小时，行业数据基本不变）
     */
    int LIST_TTL = 1440;
}

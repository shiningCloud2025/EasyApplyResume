package com.zyh.easyapplyresume.redis.constant.common;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 大学数据缓存Key常量
 * Service: UniversityMapService
 * @author shiningCloud2025
 */
public interface UniversityMapCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "universityMap";
    
    // ==================== Key定义 ====================
    
    /**
     * getAllUniversityMap() - 获取所有大学
     * 格式: common_universityMap_list
     */
    String LIST = RedisCacheConstant.COMMON_PREFIX + "_" + SERVICE_NAME + "_list";
    
    // ==================== 通配符（用于批量删除） ====================
    
    /**
     * 所有大学相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + "_" + SERVICE_NAME + "_*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 全量列表TTL（24小时，数据基本不变）
     */
    int LIST_TTL = 1440;
}

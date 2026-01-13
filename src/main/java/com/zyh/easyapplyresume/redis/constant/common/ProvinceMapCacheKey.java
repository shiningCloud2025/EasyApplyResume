package com.zyh.easyapplyresume.redis.constant.common;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 省份Map缓存Key常量
 * Service: ProvinceMapService
 * @author shiningCloud2025
 */
public interface ProvinceMapCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "provinceMap";
    
    // ==================== Key定义 ====================
    
    /**
     * getAllProvince() - 获取所有省份信息
     * 格式: common_provinceMap_list
     */
    String LIST = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "list";
    
    // ==================== 通配符（用于批量删除） ====================
    
    /**
     * 所有省份Map相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 全量列表TTL（24小时，省份数据基本不变）
     */
    int LIST_TTL = 1440;
}

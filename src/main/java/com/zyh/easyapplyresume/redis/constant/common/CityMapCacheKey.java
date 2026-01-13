package com.zyh.easyapplyresume.redis.constant.common;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 城市数据缓存Key常量
 * Service: CityMapService
 * @author shiningCloud2025
 */
public interface CityMapCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "cityMap";
    
    // ==================== Key定义 ====================
    
    /**
     * getAllCity() - 获取所有城市
     * 格式: common_cityMap_list
     */
    String LIST = RedisCacheConstant.COMMON_PREFIX + "_" + SERVICE_NAME + "_list";
    
    /**
     * getAllAreaByCityId(cityId) - 根据城市ID获取区县
     * 格式: common_cityMap_get_{cityId}
     */
    String GET_PREFIX = RedisCacheConstant.COMMON_PREFIX + "_" + SERVICE_NAME + "_get";
    
    // ==================== 通配符（用于批量删除） ====================
    
    /**
     * 根据城市ID查询区县的缓存通配符
     */
    String GET_PATTERN = GET_PREFIX + "_*";
    
    /**
     * 所有城市相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + "_" + SERVICE_NAME + "_*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 全量城市列表TTL（24小时，数据基本不变）
     */
    int LIST_TTL = 1440;
    
    /**
     * 根据城市ID查询区县TTL（24小时，数据基本不变）
     */
    int GET_TTL = 1440;
}

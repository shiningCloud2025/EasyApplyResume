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
     * findIndustryMapById() - 根据ID查询行业Map详情
     * 格式: common_industryMap_get_{industryMapId}
     */
    String GET_PREFIX = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "get";
    
    /**
     * findIndustryMapByPage() - 分页查询行业Map列表
     * 格式: common_industryMap_page_{pageNum}_{pageSize}_{queryHashCode}
     */
    String PAGE_PREFIX = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "page";
    
    /**
     * findAllIndustryMap() - 查询所有行业Map
     * 格式: common_industryMap_list
     */
    String LIST = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "list";
    
    // ==================== 通配符（用于批量删除） ====================
    
    /**
     * GET类型缓存通配符
     */
    String GET_PATTERN = GET_PREFIX + "_*";
    
    /**
     * PAGE类型缓存通配符
     */
    String PAGE_PATTERN = PAGE_PREFIX + "_*";
    
    /**
     * 所有行业Map相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * GET详情TTL（24小时，行业数据基本不变）
     */
    int GET_TTL = 1440;
    
    /**
     * 分页列表TTL（24小时，行业数据基本不变）
     */
    int PAGE_TTL = 1440;
    
    /**
     * 全量列表TTL（24小时，行业数据基本不变）
     */
    int LIST_TTL = 1440;
}

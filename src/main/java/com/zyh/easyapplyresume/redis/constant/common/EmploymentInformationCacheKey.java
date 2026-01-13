package com.zyh.easyapplyresume.redis.constant.common;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 招聘信息缓存Key常量
 * Service: EmploymentInformationService
 * @author shiningCloud2025
 */
public interface EmploymentInformationCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "employmentInformation";
    
    // ==================== Key定义 ====================
    
    /**
     * getEmploymentInformationPage(page, size, query) - 分页查询招聘信息
     * 格式: common_employmentInformation_page_{page}_{size}_{queryHash}
     */
    String PAGE_PREFIX = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "page";
    
    /**
     * getEmploymentInformationInfo(employmentInformationId) - 获取招聘信息详情
     * 格式: common_employmentInformation_get_{employmentInformationId}
     */
    String GET_PREFIX = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "get";
    
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
     * 所有招聘信息相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 分页查询TTL（10分钟，招聘信息更新相对频繁）
     */
    int PAGE_TTL = 10;
    
    /**
     * 详情查询TTL（30分钟，单条详情相对稳定）
     */
    int GET_TTL = 30;
}

package com.zyh.easyapplyresume.redis.constant.common;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 招聘岗位缓存Key常量
 * Service: RecruitPositionService
 * @author shiningCloud2025
 */
public interface RecruitPositionCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "recruitPosition";
    
    // ==================== Key定义 ====================
    
    /**
     * queryAllRecruitPositionPage() - 查看所有招聘岗位
     * 格式: common_recruitPosition_list
     */
    String LIST = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "list";
    
    /**
     * queryRecruitPositionPage(pageNum, pageSize, query) - 分页查询招聘岗位
     * 格式: common_recruitPosition_page_{pageNum}_{pageSize}_{queryHash}
     */
    String PAGE_PREFIX = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "page";
    
    /**
     * queryRecruitPosition(recruitPositionId) - 查看招聘岗位详情
     * 格式: common_recruitPosition_get_{recruitPositionId}
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
     * 所有招聘岗位相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
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

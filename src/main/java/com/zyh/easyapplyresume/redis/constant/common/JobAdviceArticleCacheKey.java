package com.zyh.easyapplyresume.redis.constant.common;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 求职攻略缓存Key常量
 * Service: JobAdviceArticleService
 * @author shiningCloud2025
 */
public interface JobAdviceArticleCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "jobAdviceArticle";
    
    // ==================== Key定义 ====================
    
    /**
     * getJobAdviceArticlePage(page, size, query) - 分页查询求职攻略
     * 格式: common_jobAdviceArticle_page_{page}_{size}_{queryHash}
     */
    String PAGE_PREFIX = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "page";
    
    /**
     * getJobAdviceArticleInfo(jobAdviceArticleId) - 获取求职攻略详情
     * 格式: common_jobAdviceArticle_get_{jobAdviceArticleId}
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
     * 所有求职攻略相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 分页查询TTL（24小时，攻略文章更新不频繁）
     */
    int PAGE_TTL = 1440;
    
    /**
     * 详情查询TTL（24小时，文章详情基本不变）
     */
    int GET_TTL = 1440;
}

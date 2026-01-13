package com.zyh.easyapplyresume.redis.constant.common;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 简历模板缓存Key常量
 * Service: ResumeTemplateService
 * @author shiningCloud2025
 */
public interface ResumeTemplateCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "resumeTemplate";
    
    // ==================== Key定义 ====================
    
    /**
     * findResumeTemplateByPage(pageNum, pageSize, query) - 分页查询简历模板
     * 格式: common_resumeTemplate_page_{pageNum}_{pageSize}_{queryHash}
     */
    String PAGE_PREFIX = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "page";
    
    /**
     * findResumeTemplateById(resumeTemplateId) - 查询简历模板详情
     * 格式: common_resumeTemplate_get_{resumeTemplateId}
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
     * 所有简历模板相关缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.COMMON_PREFIX + RedisCacheConstant.DELIMITER + SERVICE_NAME + RedisCacheConstant.DELIMITER + "*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 分页查询TTL（24小时，基于发布订阅机制保证数据一致性）
     */
    int PAGE_TTL = 1440;
    
    /**
     * 详情查询TTL（24小时，基于发布订阅机制保证数据一致性）
     */
    int GET_TTL = 1440;
}

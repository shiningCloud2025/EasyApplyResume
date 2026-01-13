package com.zyh.easyapplyresume.redis.constant.user;

import com.zyh.easyapplyresume.redis.constant.RedisCacheConstant;

/**
 * 用户信息缓存Key常量
 * Service: UserService
 * 注意: 用户信息更新频繁，建议短TTL或不缓存
 * @author shiningCloud2025
 */
public interface UserCacheKey {
    
    /**
     * Service名称
     */
    String SERVICE_NAME = "user";
    
    // ==================== Key定义 ====================
    
    /**
     * getUserByUserId(userId) - 根据用户ID查询用户信息
     * 格式: user_user_get_{userId}
     */
    String GET_PREFIX = RedisCacheConstant.USER_PREFIX + "_" + SERVICE_NAME + "_get";
    
    // ==================== 通配符（用于批量删除） ====================
    
    /**
     * 所有用户信息缓存通配符
     */
    String ALL_PATTERN = RedisCacheConstant.USER_PREFIX + "_" + SERVICE_NAME + "_*";
    
    // ==================== TTL配置（单位：分钟） ====================
    
    /**
     * 用户信息TTL（5分钟，短期缓存，避免数据不一致）
     * 注意: 用户信息更新频繁，设置较短的TTL
     */
    int GET_TTL = 5;
}

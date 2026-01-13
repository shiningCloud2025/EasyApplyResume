package com.zyh.easyapplyresume.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具类
 * @author shiningCloud2025
 */
@Component
public class RedisCacheUtil {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 获取缓存
     * @param key 缓存Key
     * @return 缓存值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 设置缓存
     * @param key 缓存Key
     * @param value 缓存值
     * @param ttl 过期时间
     * @param unit 时间单位
     */
    public void set(String key, Object value, long ttl, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, ttl, unit);
    }
    
    /**
     * 删除单个缓存
     * @param key 缓存Key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    /**
     * 根据通配符模式批量删除缓存
     * @param pattern 通配符模式（如: common_employmentInformation_*）
     */
    public void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    
    /**
     * 判断缓存是否存在
     * @param key 缓存Key
     * @return true-存在, false-不存在
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
    
    /**
     * 设置缓存过期时间
     * @param key 缓存Key
     * @param ttl 过期时间
     * @param unit 时间单位
     */
    public void expire(String key, long ttl, TimeUnit unit) {
        redisTemplate.expire(key, ttl, unit);
    }
    
    /**
     * 获取缓存剩余过期时间（秒）
     * @param key 缓存Key
     * @return 剩余秒数
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}

package com.zyh.easyapplyresume.redis.util;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RedisCacheUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取缓存
     * @param key 缓存Key
     * @return 缓存值
     */
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.warn("Redis获取缓存失败，key={}", key, e);
            return null;
        }
    }

    /**
     * 设置缓存
     * @param key 缓存Key
     * @param value 缓存值
     * @param ttl 过期时间
     * @param unit 时间单位
     */
    public void set(String key, Object value, long ttl, TimeUnit unit) {
        if (key == null) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(key, value, ttl, unit);
        } catch (Exception e) {
            log.warn("Redis设置缓存失败，key={}", key, e);
        }
    }

    /**
     * 删除单个缓存
     * @param key 缓存Key
     */
    public void delete(String key) {
        if (key == null) {
            return;
        }
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis删除缓存失败，key={}", key, e);
        }
    }

    /**
     * 根据通配符模式批量删除缓存
     * @param pattern 通配符模式（如: common_employmentInformation_*）
     */
    public void deleteByPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return;
        }
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.warn("Redis按模式删除缓存失败，pattern={}", pattern, e);
        }
    }

    /**
     * 判断缓存是否存在
     * @param key 缓存Key
     * @return true-存在, false-不存在
     */
    public Boolean exists(String key) {
        if (key == null) {
            return false;
        }
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.warn("Redis判断缓存是否存在失败，key={}", key, e);
            return false;
        }
    }

    /**
     * 设置缓存过期时间
     * @param key 缓存Key
     * @param ttl 过期时间
     * @param unit 时间单位
     */
    public void expire(String key, long ttl, TimeUnit unit) {
        if (key == null) {
            return;
        }
        try {
            redisTemplate.expire(key, ttl, unit);
        } catch (Exception e) {
            log.warn("Redis设置过期时间失败，key={}", key, e);
        }
    }

    /**
     * 获取缓存剩余过期时间（秒）
     * @param key 缓存Key
     * @return 剩余秒数
     */
    public Long getExpire(String key) {
        if (key == null) {
            return -1L;
        }
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis获取过期时间失败，key={}", key, e);
            return -1L;
        }
    }
}

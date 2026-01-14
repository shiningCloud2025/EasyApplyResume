package com.zyh.easyapplyresume.selfannotation.controller.RateLimit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";

    @Around("@annotation(com.zyh.easyapplyresume.selfannotation.controller.RateLimit.RateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        int count = rateLimit.count();
        int time = rateLimit.time();
        String key = rateLimit.key();

        if (key.isEmpty()) {
            key = generateDefaultKey(joinPoint);
        }

        String redisKey = RATE_LIMIT_KEY_PREFIX + key;
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - time * 1000L;

        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, windowStart);

        Long currentCount = redisTemplate.opsForZSet().zCard(redisKey);
        if (currentCount != null && currentCount >= count) {
            log.warn("限流触发: key={}, 当前请求数={}, 限制={}/{} 秒", redisKey, currentCount, count, time);
            throw new RateLimitException("请求过于频繁，请稍后再试");
        }

        redisTemplate.opsForZSet().add(redisKey, String.valueOf(currentTime), currentTime);
        redisTemplate.expire(redisKey, time, java.util.concurrent.TimeUnit.SECONDS);

        return joinPoint.proceed();
    }

    private String generateDefaultKey(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = getIpAddress(request);
        String methodName = joinPoint.getSignature().getName();
        return ip + ":" + methodName;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}

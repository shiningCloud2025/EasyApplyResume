package com.zyh.easyapplyresume.filter;

import com.zyh.easyapplyresume.utils.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户JWT认证过滤器
 * @author shiningCloud2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserJwtAuthFilter {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserMapper userMapper;
}

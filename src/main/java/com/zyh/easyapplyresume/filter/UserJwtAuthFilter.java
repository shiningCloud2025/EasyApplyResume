package com.zyh.easyapplyresume.filter;

import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.security.SecurityUser;
import com.zyh.easyapplyresume.utils.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * 用户JWT认证过滤器
 * @author shiningCloud2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserJwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserMapper userMapper;

    @Value("${jwt.user.secret}")
    private String jwtSecret;

    @Value("${jwt.user.token-prefix}")
    private String tokenPrefix;

    @Value("${jwt.user.header-name}")
    private String headerName;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(headerName);
        // 如果请求头没有拿到token
        if (StringUtil.isEmpty(header)||!header.startsWith(tokenPrefix)){
            filterChain.doFilter(request,response);
            return;
        }

        String token = header.replace(tokenPrefix, "");
        try{
            Integer userId = jwtUtil.getUserIdFromToken(token, jwtSecret);
            String redisKey = "user:token:" + userId;
            String cachedToken = stringRedisTemplate.opsForValue().get(redisKey);
            if (!token.equals(cachedToken)) {
                log.warn("Token 不匹配或已失效，userId: {}", userId);
                filterChain.doFilter(request, response);
                return;
            }
            User user = userMapper.selectById(userId);

            if (user == null) {
                log.warn("用户不存在或已被禁用，userId: {}", userId);
                filterChain.doFilter(request, response);
                return;
            }
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUserId(user.getUserId());
            securityUser.setUsername(user.getUserUsername());
            securityUser.setUserType("user");
            securityUser.setAuthorities(Collections.emptyList());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch (Exception e) {
            log.error("用户端 Token 验证失败: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}

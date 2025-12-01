package com.zyh.easyapplyresume.filter;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.StrUtil;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.PermissionMapper;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.model.vo.admin.AdminInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import com.zyh.easyapplyresume.security.SecurityUser;
import com.zyh.easyapplyresume.utils.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 管理员JWT认证过滤器
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class AdminJwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtUtil jwtUtil;

    @Autowired
    private  StringRedisTemplate stringRedisTemplate;

    @Autowired
    private  AdminMapper adminMapper;

    @Autowired
    private  PermissionMapper permissionMapper;

    @Value("${jwt.admin.secret}")
    private String jwtSecret;

    @Value("${jwt.admin.token-prefix}")
    private String tokenPrefix;

    @Value("${jwt.admin.header-name}")
    private String headerName;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(headerName);

        if (StrUtil.isBlank(header) || !header.startsWith(tokenPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(tokenPrefix, "");

        try {
            Integer adminId = jwtUtil.getUserIdFromToken(token, jwtSecret);

            String redisKey = "admin:token:" + adminId;
            String cachedToken = stringRedisTemplate.opsForValue().get(redisKey);

            if (!token.equals(cachedToken)) {
                log.warn("Token 不匹配或已失效，adminId: {}", adminId);
                filterChain.doFilter(request, response);
                return;
            }

            Admin admin = adminMapper.selectById(adminId);

            if (admin == null || admin.getAdminState() == 0) {
                log.warn("管理员不存在或已被禁用，adminId: {}", adminId);
                filterChain.doFilter(request, response);
                return;
            }
            AdminInfoVO adminInfoVO = adminMapper.findAdminInfoById(adminId);
            ConcurrentHashSet<String> permissions = new ConcurrentHashSet<>();
            for (RoleInfoVO roleInfoVO : adminInfoVO.getRoleInfoVOS()) {
                for (PermissionInfoVO permissionInfoVO : roleInfoVO.getPermissionInfoVOS()) {
                    permissions.add(permissionInfoVO.getPermissionUrl());
                }
            }
            List<SimpleGrantedAuthority> authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            SecurityUser securityUser = new SecurityUser();
            securityUser.setUserId(admin.getAdminId());
            securityUser.setUsername(admin.getAdminUsername());
            securityUser.setUserType("admin");
            securityUser.setEnabled(admin.getAdminState() == 1);
            securityUser.setAuthorities(authorities);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e) {
            log.error("管理端 Token 验证失败: {}", e.getMessage());
        }
    }
}

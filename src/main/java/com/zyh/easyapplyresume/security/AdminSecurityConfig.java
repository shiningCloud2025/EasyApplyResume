package com.zyh.easyapplyresume.security;

import com.zyh.easyapplyresume.filter.AdminJwtAuthFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Order(2)
public class AdminSecurityConfig {
    @Autowired
    private  AdminJwtAuthFilter adminJwtAuthFilter;

    // 注入全局的CorsConfigurationSource(跨域处理)
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;


    // 自定义未认证请求的响应
    AuthenticationEntryPoint unauthorizedEntryPoint = new AuthenticationEntryPoint() {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             org.springframework.security.core.AuthenticationException authException)
                throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先登录\"}");
        }
    };

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity  http) throws Exception{
        http
                .securityMatcher("/admin/**","/ad_monitor/**")
                // 启用CORS跨域:指定自定义的CorsConfigurationSource
                .cors(cors->cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth-> auth
                        // 放开邮箱登录接口
                        .requestMatchers("/admin/email/loginandregister/**").permitAll()
                        // 放开手机登录接口
                        .requestMatchers("/admin/sms/**").permitAll()
                        // 放开正常登录接口
                        .requestMatchers("/admin/auth/**").permitAll()
                        // 放开公告和广告,管理端是肯定会登录的,给用户端放开
                        .requestMatchers("/admonitor/user/advertisement/**").permitAll()
                        .requestMatchers("/admonitor/user/announcement").permitAll()
                        .requestMatchers("/doc.html", "/webjars/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedEntryPoint))
                .addFilterBefore(adminJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



}

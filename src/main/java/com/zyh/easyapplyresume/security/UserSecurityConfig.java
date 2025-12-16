package com.zyh.easyapplyresume.security;
import com.zyh.easyapplyresume.filter.UserJwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@Order(1)
public class UserSecurityConfig {
    @Autowired
    private  UserJwtAuthFilter  userJwtAuthFilter;

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
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception{
        http
                .securityMatcher("/user/**")
                // 启用CORS跨域:指定自定义的CorsConfigurationSource
                .cors(cors->cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        // 放行省份查询,注册需要使用
                        .requestMatchers("/user/provinceMap/**").permitAll()
                        // 放行城市查询,注册需要使用
                        .requestMatchers("/user/cityMap/**").permitAll()
                        // 放行大学查询,注册需要使用
                        .requestMatchers("/user/universityMap/**").permitAll()
                        // 放行职位查询,注册需要使用
                        .requestMatchers("/user/recruitPosition/queryAllRecruitPositionPage").permitAll()
                        // 验证业务(短信)
                        .requestMatchers("/user/sms/**").permitAll()
                        // 验证业务(邮箱)
                        .requestMatchers("/user/email/loginandregister/**").permitAll()
                        .requestMatchers("/user/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedEntryPoint))
                .addFilterBefore(userJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }





}

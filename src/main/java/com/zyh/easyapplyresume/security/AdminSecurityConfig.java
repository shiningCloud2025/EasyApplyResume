package com.zyh.easyapplyresume.security;

import com.zyh.easyapplyresume.filter.AdminJwtAuthFilter;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Order(2)
public class AdminSecurityConfig {
    @Autowired
    private  AdminJwtAuthFilter adminJwtAuthFilter;

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity  http) throws Exception{
        http
                .securityMatcher("/api/admin/**","/ad_monitor/**")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/doc.html", "/webjars/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(adminJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

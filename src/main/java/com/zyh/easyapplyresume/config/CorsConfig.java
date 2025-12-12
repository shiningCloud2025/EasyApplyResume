package com.zyh.easyapplyresume.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Arrays;
import java.util.Collections;

/**
 * 全局CORS配置（多SecurityFilterChain共用此Bean）
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. 允许的源（支持通配符，解决allowCredentials=true时不能用*的问题）
        // 生产环境建议替换为具体的前端域名，如"http://localhost:3000", "https://your-frontend.com"
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        // 2. 允许的HTTP方法（包含OPTIONS预检请求）
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. 允许的请求头（*表示所有，包括自定义的Authorization头（JWT））
        config.setAllowedHeaders(Collections.singletonList("*"));

        // 4. 是否允许携带Cookie（前后端分离场景根据需求设置，若为true则allowedOriginPatterns不能用*的纯字符串，需用通配符模式如http://*）
        config.setAllowCredentials(true);

        // 5. 预检请求的缓存时间（单位：秒，减少OPTIONS请求次数）
        config.setMaxAge(3600L);

        // 6. 注册路径映射（/**表示所有接口）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
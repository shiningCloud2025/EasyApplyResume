package com.zyh.easyapplyresume.config;

import com.zyh.easyapplyresume.interceptor.UsallyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * SpringDoc配置2
 * @author shiningCloud2025
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private UsallyInterceptor usallyInterceptor;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册日志拦截器
        registry.addInterceptor(usallyInterceptor)
                .addPathPatterns("/**")  // 拦截所有路径
                .excludePathPatterns("/error", "/static/**", "/favicon.ico");  // 排除静态资源


        /**
         * 拦截路径匹配规则
         * 对于请求 http://localhost:8080/api/v3/api-docs/default：
         * 协议和主机：http://localhost:8080 - 这部分不参与路径匹配
         * Context Path：/api - 这是应用的根路径，由 server.servlet.context-path 配置
         * 相对路径：/v3/api-docs/default - 这是相对于应用根路径的部分
         */
//        registry.addInterceptor(faviconInterceptor)
//                .addPathPatterns("/doc.html");
    }
}
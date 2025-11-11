package com.zyh.easyapplyresume.config;

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
//    @Autowired
//    private FaviconInterceptor faviconInterceptor;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 为不同路径映射 favicon.ico
        registry.addResourceHandler("/favicon.ico", "/api/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico")
                .setCachePeriod(3600);

        // 保留你原来的映射
        registry.addResourceHandler("/api/favicon.jpg")
                .addResourceLocations("classpath:/static/favicon.jpg")
                .setCachePeriod(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 只拦截 Knife4j 文档页面

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
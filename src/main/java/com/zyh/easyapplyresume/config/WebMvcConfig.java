package com.zyh.easyapplyresume.config;

import com.zyh.easyapplyresume.config.interceptor.FaviconInterceptor;
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
    private FaviconInterceptor faviconInterceptor;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射 favicon.jpg，适配 context-path: /api
        registry.addResourceHandler("/api/favicon.jpg")
                .addResourceLocations("classpath:/static/favicon.jpg");

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 只拦截 Knife4j 文档页面
        registry.addInterceptor(faviconInterceptor)
                .addPathPatterns("/api/doc.html");
    }
}
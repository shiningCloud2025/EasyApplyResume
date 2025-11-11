package com.zyh.easyapplyresume.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * SpringDoc配置3
 * @author shiningCloud2025
 */
@Component
public class FaviconInterceptor implements HandlerInterceptor {
    // 新增：请求处理前执行（用来验证拦截器是否被触发）
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("🚀【Favicon拦截器】已触发！当前请求路径：" + request.getRequestURI());
        return true; // 必须返回true，否则请求会被拦截（页面打不开）
    }

    // 拦截 /api/doc.html 的请求，注入自定义图标
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 检查是否是 API 文档请求
        String requestURI = request.getRequestURI();
        /**
         * 这个requestURI是去掉IP地址的全部
         */
        if (requestURI.startsWith("/api/v3/api-docs/")) {
            // 在响应头中添加 favicon 信息
            response.setHeader("X-Favicon-Path", "/api/favicon.ico");
            System.out.println("【Favicon拦截器】✅ 已在响应头中添加 favicon 路径");
        }
    }
}
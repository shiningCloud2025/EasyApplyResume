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
        if (modelAndView != null &&  "/api/doc.html".equals(request.getRequestURI())) {
            // 往页面注入图标路径（适配 context-path: /api）
            modelAndView.addObject("favicon", "/api/favicon.jpg");
            System.out.println("【Favicon拦截器】✅ 成功注入图标路径：/api/favicon.jpg");
        }
    }
}
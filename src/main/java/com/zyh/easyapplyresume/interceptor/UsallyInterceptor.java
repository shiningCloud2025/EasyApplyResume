package com.zyh.easyapplyresume.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *  通用拦截器,主要用于打印日志,保证请求可以打入控制器
 *  @author shiningCloud2025
 */
@Slf4j
@Component
public class UsallyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = getIpAddress(request);
        String params = getRequestParams(request);

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            log.info("【请求开始】Method: {}, URI: {}, Controller: {}.{}, IP: {}, Params: {}",
                    method, uri, controllerName, methodName, ip, params);
        } else {
            log.info("【请求开始】Method: {}, URI: {}, IP: {}, Params: {}", method, uri, ip, params);
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // RESTful API不需要处理视图，留空即可
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime == null) {
            return;
        }

        long duration = System.currentTimeMillis() - startTime;
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            
            if (ex != null) {
                log.error("【请求异常】Method: {}, URI: {}, Controller: {}.{}, Duration: {}ms, Error: {}",
                        method, uri, controllerName, methodName, duration, ex.getMessage());
            } else {
                log.info("【请求结束】Method: {}, URI: {}, Controller: {}.{}, Status: {}, Duration: {}ms",
                        method, uri, controllerName, methodName, status, duration);
            }
        } else {
            if (ex != null) {
                log.error("【请求异常】Method: {}, URI: {}, Duration: {}ms, Error: {}",
                        method, uri, duration, ex.getMessage());
            } else {
                log.info("【请求结束】Method: {}, URI: {}, Status: {}, Duration: {}ms",
                        method, uri, status, duration);
            }
        }
    }


    /**
     * 获取真实IP地址
     * 优先从代理头获取，避免拿到代理服务器IP
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果经过多级代理，取第一个IP
        if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取请求参数（URL参数）
     * 敏感字段自动脱敏
     */
    private String getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            // 敏感字段脱敏
            if (name.toLowerCase().contains("password") || name.toLowerCase().contains("token")) {
                value = "******";
            }
            params.put(name, value);
        }
        return params.isEmpty() ? "{}" : JSONUtil.toJsonStr(params);
    }


}

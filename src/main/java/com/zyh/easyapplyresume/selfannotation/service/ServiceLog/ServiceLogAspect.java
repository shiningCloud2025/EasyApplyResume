package com.zyh.easyapplyresume.selfannotation.service.ServiceLog;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Slf4j
@Aspect
@Component
public class ServiceLogAspect {

    @Around("@within(com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog) || " +
            "@annotation(com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog)")
    public Object serviceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (!Modifier.isPublic(method.getModifiers())) {
            return joinPoint.proceed();
        }

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        String params = args.length > 0 ? JSONUtil.toJsonStr(args) : "无参数";

        log.info("【Service执行】类: {}, 方法: {}, 参数: {}", className, methodName, params);

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            String resultStr = result != null ? JSONUtil.toJsonStr(result) : "null";
            log.info("【Service完成】类: {}, 方法: {}, 返回值: {}, 耗时: {}ms", 
                    className, methodName, resultStr, duration);
            return result;
        } catch (Throwable e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("【Service异常】类: {}, 方法: {}, 异常: {}, 耗时: {}ms", 
                    className, methodName, e.getMessage(), duration);
            throw e;
        }
    }
}

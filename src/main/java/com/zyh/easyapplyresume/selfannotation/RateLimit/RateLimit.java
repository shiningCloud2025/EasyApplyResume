package com.zyh.easyapplyresume.selfannotation.RateLimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义限流注解 - 基于滑动窗口算法
 * 
 * 使用示例：
 * <pre>
 * // 示例1：60秒内最多10次请求（默认配置）
 * &#64;RateLimit
 * public Result someMethod() {
 *     return Result.success();
 * }
 * 
 * // 示例2：自定义限流参数 - 30秒内最多5次请求
 * &#64;RateLimit(count = 5, time = 30)
 * public Result login() {
 *     return Result.success();
 * }
 * 
 * // 示例3：自定义限流key - 按用户ID限流
 * &#64;RateLimit(count = 100, time = 60, key = "user:123")
 * public Result getUserInfo() {
 *     return Result.success();
 * }
 * </pre>
 * 
 * @author shiningCloud2025
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    
    int count() default 10;
    
    int time() default 60;
    
    String key() default "";
}

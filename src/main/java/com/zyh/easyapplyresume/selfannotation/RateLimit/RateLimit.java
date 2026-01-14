package com.zyh.easyapplyresume.selfannotation.RateLimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义限流注解-@RateLimit
 * @author shiningCloud2025
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    
    int count() default 10;
    
    int time() default 60;
    
    String key() default "";
}

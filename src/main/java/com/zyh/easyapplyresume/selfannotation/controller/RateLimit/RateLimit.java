package com.zyh.easyapplyresume.selfannotation.controller.RateLimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义限流注解 - 基于滑动窗口算法
 * 
 * <p><b>使用位置：Controller层方法上</b></p>
 * <p>限流注解应该标注在Controller层的接口方法上，在请求入口处进行限流拦截，
 * 避免恶意请求进入Service层消耗系统资源。</p>
 * 
 * <p><b>使用示例：</b></p>
 * <pre>
 * &#64;RestController
 * &#64;RequestMapping("/api/user")
 * public class UserController {
 * 
 *     // 示例1：60秒内最多10次请求（默认配置）
 *     &#64;RateLimit
 *     &#64;GetMapping("/info")
 *     public Result getUserInfo() {
 *         return Result.success();
 *     }
 * 
 *     // 示例2：登录接口限流 - 30秒内最多5次
 *     &#64;RateLimit(count = 5, time = 30)
 *     &#64;PostMapping("/login")
 *     public Result login(&#64;RequestBody LoginForm form) {
 *         return userService.login(form);
 *     }
 * 
 *     // 示例3：验证码接口限流 - 60秒内最多3次
 *     &#64;RateLimit(count = 3, time = 60)
 *     &#64;PostMapping("/sms/send")
 *     public Result sendSms(&#64;RequestParam String phone) {
 *         return smsService.send(phone);
 *     }
 * 
 *     // 示例4：自定义限流key - 全局限流
 *     &#64;RateLimit(count = 100, time = 60, key = "global:search")
 *     &#64;GetMapping("/search")
 *     public Result search(&#64;RequestParam String keyword) {
 *         return searchService.search(keyword);
 *     }
 * }
 * </pre>
 * 
 * <p><b>参数说明：</b></p>
 * <ul>
 *   <li>count - 时间窗口内最大请求次数（默认10次）</li>
 *   <li>time - 时间窗口大小，单位秒（默认60秒）</li>
 *   <li>key - 自定义限流Key，为空则使用默认Key（IP地址+方法名）</li>
 * </ul>
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

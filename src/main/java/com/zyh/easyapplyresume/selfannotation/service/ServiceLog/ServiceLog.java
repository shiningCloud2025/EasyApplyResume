package com.zyh.easyapplyresume.selfannotation.service.ServiceLog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service层方法日志注解
 * 
 * <p><b>使用位置：Service类或方法上</b></p>
 * <p>标注在类上：自动为所有public方法打印日志（private、protected方法不打印）</p>
 * <p>标注在方法上：为单个方法打印日志</p>
 * 
 * <p><b>使用示例：</b></p>
 * <pre>
 * // 示例1：标注在类上，所有public方法都会打印日志
 * &#64;Service
 * &#64;ServiceLog
 * public class UserServiceImpl implements UserService {
 *     
 *     public User getUser(Long id) {
 *         // 会打印日志
 *     }
 *     
 *     private void validateUser(User user) {
 *         // 不会打印日志（private方法）
 *     }
 * }
 * 
 * // 示例2：只标注在单个方法上
 * &#64;Service
 * public class OrderService {
 *     
 *     &#64;ServiceLog
 *     public Order createOrder(OrderForm form) {
 *         // 只有这个方法会打印日志
 *     }
 *     
 *     public void cancelOrder(Long orderId) {
 *         // 不会打印日志
 *     }
 * }
 * </pre>
 * 
 * <p><b>日志内容：</b></p>
 * <ul>
 *   <li>执行前：类名、方法名、参数</li>
 *   <li>执行后：返回值、执行耗时</li>
 *   <li>异常时：异常信息</li>
 * </ul>
 * 
 * @author shiningCloud2025
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceLog {
}

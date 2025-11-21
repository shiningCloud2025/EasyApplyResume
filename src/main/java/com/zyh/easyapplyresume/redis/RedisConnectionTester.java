package com.zyh.easyapplyresume.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
/**
 * 测试Redis连接情况
 * @author shiningCloud2025
 */
@Component
public class RedisConnectionTester implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 执行 PING 命令，返回 "PONG" 说明连接成功
            String pingResult = stringRedisTemplate.getConnectionFactory().getConnection().ping();
            System.out.println("=== Redis 连接测试 ===");
            System.out.println("PING 响应：" + pingResult); // 成功会输出 PONG

            // 测试 SET/GET 命令
//            stringRedisTemplate.opsForValue().set("test_key", "hello_redis");
//            String value = stringRedisTemplate.opsForValue().get("test_key");
//            System.out.println("SET/GET 测试：" + value); // 成功会输出 hello_redis
//            System.out.println("====================");
        } catch (Exception e) {
            System.err.println("=== Redis 连接失败 ===");
            e.printStackTrace();
            System.err.println("====================");
        }
    }
}

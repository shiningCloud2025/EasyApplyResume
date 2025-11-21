package com.zyh.easyapplyresume.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;

/**
 * 自已配置RedisConfig的原因:
 * 1.自定义连接池核心参数
 * 2.序列化方式从JdkSerializationRedisSerializer改为Json
 * @author shiningCloud2025
 */
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.timeout}")
    private Duration timeout;

    @Value("${spring.data.redis.lettuce.pool.max-active}")
    private int maxActive;

    @Value("${spring.data.redis.lettuce.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.data.redis.lettuce.pool.min-idle}")
    private int minIdle;

    @Value("${spring.data.redis.lettuce.pool.max-wait}")
    private Duration maxWait;

    /**
     * 配置连接工厂 (Connection Factory)
     * 这是修改的核心部分：使用 LettucePoolingClientConfiguration 来配置连接池
     *
     * @return LettuceConnectionFactory
     */
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        // 1. 创建 Redis 服务器连接配置
        org.springframework.data.redis.connection.RedisStandaloneConfiguration standaloneConfig =
                new org.springframework.data.redis.connection.RedisStandaloneConfiguration(host, port);
        standaloneConfig.setPassword(RedisPassword.of(password));

        // 2. 创建 Lettuce 客户端连接池配置
        // 这里不再使用 LettuceClientConfiguration.builder().poolConfig()
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(redisPoolConfig()) // 注入我们配置好的连接池参数
                .commandTimeout(timeout)       // 设置命令超时时间
                .build();

        // 3. 结合两者，创建并返回连接工厂
        return new LettuceConnectionFactory(standaloneConfig, clientConfig);
    }

    /**
     * 配置连接池参数 (Pool Configuration)
     * 这个方法没有变化，它返回一个标准的 Apache Commons Pool2 配置对象
     *
     * @return GenericObjectPoolConfig
     */
    @Bean
    public GenericObjectPoolConfig<?> redisPoolConfig() {
        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxActive);     // 最大连接数
        poolConfig.setMaxIdle(maxIdle);         // 最大空闲连接数
        poolConfig.setMinIdle(minIdle);         // 最小空闲连接数
        poolConfig.setMaxWait(maxWait);         // 获取连接的最大等待时间
        poolConfig.setTestWhileIdle(true);      // 空闲时检查连接有效性
        poolConfig.setTestOnBorrow(true);       // 借用时检查连接有效性
        return poolConfig;
    }

    /**
     * 配置 RedisTemplate (用于操作对象)
     * 这个方法没有变化，配置了 JSON 序列化方式，非常推荐。
     *
     * @param lettuceConnectionFactory 连接工厂
     * @return RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        // 配置键的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 配置值的序列化方式（使用 Jackson 序列化 JSON）
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        // 初始化 RedisTemplate
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * 配置 StringRedisTemplate (专门用于处理字符串)
     * 这个方法也没有变化。
     *
     * @param lettuceConnectionFactory 连接工厂
     * @return StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return new StringRedisTemplate(lettuceConnectionFactory);
    }
}
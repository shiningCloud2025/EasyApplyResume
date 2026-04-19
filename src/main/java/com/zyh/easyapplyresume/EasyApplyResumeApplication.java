package com.zyh.easyapplyresume;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author shiningCloud2025
 */
@EnableScheduling
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication(exclude = PgVectorStoreAutoConfiguration.class)
public class EasyApplyResumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyApplyResumeApplication.class, args);
    }

    // TODO:在这里配置分页插件,多数据源,不能用这个,这个是单数据的,多数据源配置在DataSourceConfig
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        // 添加MySQL分页插件（根据你的数据库类型调整DbType）
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        return interceptor;
//    }


}

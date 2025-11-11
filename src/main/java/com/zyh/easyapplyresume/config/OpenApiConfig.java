package com.zyh.easyapplyresume.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc配置1
 * @author shiningCloud2025
 */
@Configuration
public class OpenApiConfig {

    // 配置文档基本信息（标题、描述等）
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // 浏览器标签+文档页面标题
                        .title("易投简历API文档中心")
                        .description("易投简历系统的API调试文档")
                        .contact(new Contact()
                                .name("shiningCloud2025")
                                .email("shining_cloud@163.com")
                                .url("http://xxx.com"))
                        .version("1.0.0")

                );
    }
}
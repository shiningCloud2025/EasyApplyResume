package com.zyh.easyapplyresume.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shiningCloud2025
 * 启动项目，访问 http://localhost:8080/api/doc.html 能够看到接口文档，可以测试调用接口：
 * http://localhost:8080/api/swagger-ui/index.html
 */
@RestController
@RequestMapping("/health")
@Tag(name="测试接口")
public class HealthController {
    @GetMapping
    public String healthCheck(){
        return "ok";
    }
}

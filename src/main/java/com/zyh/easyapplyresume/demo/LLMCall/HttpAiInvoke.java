package com.zyh.easyapplyresume.demo.LLMCall;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@SpringBootApplication // 自动包含@ComponentScan（扫描根包及其子包）和@EnableAutoConfiguration（启用Web自动配置）
public class HttpAiInvoke {
    private static String aliBailianApiKey;

    @Value("${ali.bailian.api-key}")
    private String tempApiKey;

    @PostConstruct
    public void init() {
        HttpAiInvoke.aliBailianApiKey = this.tempApiKey;
        System.out.println("API-KEY(HTTP)已初始化：" + (aliBailianApiKey != null ? "已注入" : "未注入"));
    }

    private static final String DASHSCOPE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    public static String sendDashScopeRequest() {
        if (aliBailianApiKey == null || aliBailianApiKey.isEmpty()) {
            throw new RuntimeException("API-KEY 未初始化，请确保 Spring 上下文已启动");
        }

        JSONObject requestBody = new JSONObject();
        requestBody.set("model", "qwen-plus");

        JSONArray messages = new JSONArray();
        JSONObject systemMsg = new JSONObject();
        systemMsg.set("role", "system");
        systemMsg.set("content", "You are a helpful assistant.");
        messages.add(systemMsg);

        JSONObject userMsg = new JSONObject();
        userMsg.set("role", "user");
        userMsg.set("content", "你是谁？");
        messages.add(userMsg);

        requestBody.set("messages", messages);

        try (HttpResponse response = HttpRequest.post(DASHSCOPE_URL)
                .header("Authorization", "Bearer " + aliBailianApiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .timeout(30000)
                .execute()) {

            if (response.isOk()) {
                return response.body();
            } else {
                throw new RuntimeException("请求失败 [状态码: " + response.getStatus() + "]: " + response.body());
            }

        } catch (Exception e) {
            // 修复：将异常包装后重新抛出，确保方法始终有返回或抛出异常
            throw new RuntimeException("发送请求失败: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        org.springframework.context.ConfigurableApplicationContext context =
                org.springframework.boot.SpringApplication.run(HttpAiInvoke.class, args);

        try {
            String result = sendDashScopeRequest();
            System.out.println("大模型响应:\n" + result);
        } catch (Exception e) {
            System.err.println("调用失败: " + e.getMessage());
        } finally {
            context.close();
        }
    }
}

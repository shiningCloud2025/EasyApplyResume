//package com.zyh.easyapplyresume.demo.LLMCall;
//
//import com.alibaba.dashscope.aigc.generation.Generation;
//import com.alibaba.dashscope.aigc.generation.GenerationParam;
//import com.alibaba.dashscope.aigc.generation.GenerationResult;
//import com.alibaba.dashscope.common.Message;
//import com.alibaba.dashscope.common.Role;
//import com.alibaba.dashscope.exception.ApiException;
//import com.alibaba.dashscope.exception.InputRequiredException;
//import com.alibaba.dashscope.exception.NoApiKeyException;
//import com.alibaba.dashscope.utils.JsonUtils;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//// 建议dashscope SDK的版本>=2.12.0
//// 关键1：加@Component（或@Service/@Controller），让Spring扫描并创建实例
//@Component
///**
// * Java 的 main 方法是独立于 Spring 的—— 直接运行 main 时，JVM 只会执行 main 里的代码，不会主动启动 Spring 容器。
// * 而 @Value 注入、@PostConstruct 初始化，都必须依赖 Spring 容器启动后创建实例 才能触发。
// * 简单说：你的 init 方法（给静态变量赋值）根本没执行过，aliBailianApiKey 一直是 null，SDK 拿不到 API-KEY 就报错。
// */
//@SpringBootApplication(scanBasePackages = "com.zyh.easyapplyresume.demo.LLMCall")
//public class SdkAiInvoke {
//    // 注入自定义配置：ali.bailian.api-key
//    private static String aliBailianApiKey;
//
//    @Value("${ali.bailian.api-key}")
//    private String tempApiKey;
//
//    // 关键2：@PostConstruct确保Spring注入tempApiKey后，再给静态变量赋值
//    // 初始化时，将非静态变量的值赋给静态变量（@PostConstruct 确保实例化后执行）
//    @PostConstruct
//    public void init() {
//        SdkAiInvoke.aliBailianApiKey = this.tempApiKey;
//    }
//    public static GenerationResult callWithMessage() throws NoApiKeyException, InputRequiredException {
//        Generation gen = new Generation();
//        Message systemMsg = Message.builder()
//                .role(Role.SYSTEM.getValue())
//                .content("You are a helpful assistant.")
//                .build();
//        Message userMsg = Message.builder()
//                .role(Role.USER.getValue())
//                .content("你是谁")
//                .build();
//        GenerationParam param = GenerationParam.builder()
//                // 若没有配置环境变量，请使用百炼API KEY替换将厦航替换为:.apiKey("sk-xxx")
//                .apiKey(SdkAiInvoke.aliBailianApiKey)
//                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
//                .model("qwen-plus")
//                .messages(Arrays.asList(systemMsg, userMsg))
//                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
//                .build();
//        return gen.call(param);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(SdkAiInvoke.class, args);
//        try{
//            GenerationResult result = callWithMessage();
//            System.out.println(JsonUtils.toJson(result));
//        }catch (ApiException | NoApiKeyException | InputRequiredException e){
//            // 使用日志框架记录异常信息
//            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
//        }
//        System.exit(0);
//    }
//}

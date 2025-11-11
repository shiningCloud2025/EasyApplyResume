//package com.zyh.easyapplyresume.demo.LLMCall;
//
//import dev.langchain4j.community.model.dashscope.QwenChatModel;
//import dev.langchain4j.model.chat.ChatLanguageModel;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.stereotype.Component;
//
//@Component
//@SpringBootApplication // 自动包含@ComponentScan（扫描根包及其子包）和@EnableAutoConfiguration（启用Web自动配置）
//public class LangChan4jAiInvoke {
//    private static String aliBailianApiKey;
//
//    @Value("${ali.bailian.api-key}")
//    private String tempApiKey;
//
//    @PostConstruct
//    public void init() {
//        LangChan4jAiInvoke.aliBailianApiKey = this.tempApiKey;
//        System.out.println("API-KEY(LangChain)已初始化：" + (aliBailianApiKey != null ? "已注入" : "未注入"));
//    }
//    public static void main(String[] args) {
//        SpringApplication.run(SdkAiInvoke.class, args);
//        ChatLanguageModel qwenModel = QwenChatModel.builder()
//                .apiKey(aliBailianApiKey)
//                .modelName("qwen-max")
//                .build();
//        String answer = qwenModel.chat("我是一名电气工程大四学生，我想知道保研的预推免有哪些流程，我希望详细一点");
//        System.out.println(answer);
//    }
//}

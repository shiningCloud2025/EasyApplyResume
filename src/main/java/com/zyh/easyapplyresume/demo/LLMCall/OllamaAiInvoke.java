package com.zyh.easyapplyresume.demo.LLMCall;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
//public  class OllamaAiInvoke implements CommandLineRunner {
//    @Resource
//    private ChatModel ollamaChatModel;
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        AssistantMessage output = ollamaChatModel.call(new Prompt("你好，我是yunhan,请给我推荐一个Go语言的学习路线(已知我有java基础"))
//                .getResult()
//                .getOutput();
//        System.out.println(output.getText());
//    }
//}

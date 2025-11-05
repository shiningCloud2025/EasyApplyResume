package com.zyh.easyapplyresume.demo.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiResumeAssistantTest {

    @Resource
    private AiResumeAssistant aiResumeAssistant;
    @Test
    void testChat(){
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是java工程师yunhan";
        String answer = aiResumeAssistant.doChat(message,chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想学会如何制作一个好的java简历";
        answer = aiResumeAssistant.doChat(message,chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "你好，我是谁来着，刚才跟你说过，请帮我回忆一下?";
        answer = aiResumeAssistant.doChat(message,chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是java工程师yunhan,我想要一份java简历，但是我不知道该怎么写,政治敏感";
        AiResumeAssistant.ResumeUpdateAdviceReport resumeUpdateAdviceReport = aiResumeAssistant.doChatWithReport(message,chatId);
        System.out.println(resumeUpdateAdviceReport.toString());
        Assertions.assertNotNull(resumeUpdateAdviceReport);
    }
}
package com.zyh.easyapplyresume.demo.chatmemory;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest(
    classes = com.zyh.easyapplyresume.EasyApplyResumeApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@TestPropertySource(
    locations = "classpath:application-dev.yaml"
)
class MySQLBasedChatMemoryTest {

    @Resource
    private MySQLBasedChatMemory mySQLBasedChatMemory;

    @Test
    void add() {
        String conversationId = UUID.randomUUID().toString();
        List<Message> messages = Arrays.asList(
                new UserMessage("你好，这是第一条测试消息"),
                new UserMessage("这是第二条测试消息"));

        try {
            mySQLBasedChatMemory.add(conversationId, messages);
            System.out.println("测试执行成功");
        } catch (Exception e) {
            System.err.println("测试执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
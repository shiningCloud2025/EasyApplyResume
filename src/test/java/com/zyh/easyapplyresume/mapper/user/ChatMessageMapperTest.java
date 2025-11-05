package com.zyh.easyapplyresume.mapper.user;

import com.zyh.easyapplyresume.model.pojo.user.ChatMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

class ChatMessageMapperTest {

    @Resource
    private ChatMessageMapper chatMessageMapper;
    @Test
    void selectLastNByConversationId() {
        List<ChatMessage> messageList = chatMessageMapper.selectLastNByConversationId("1", 5);
        System.out.println(messageList);
    }
}
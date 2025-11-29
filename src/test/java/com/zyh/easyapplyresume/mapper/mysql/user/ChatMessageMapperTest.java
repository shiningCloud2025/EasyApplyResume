package com.zyh.easyapplyresume.mapper.mysql.user;

import com.zyh.easyapplyresume.model.pojo.user.UserChatMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

class ChatMessageMapperTest {

    @Resource
    private UserChatMessageMapper chatMessageMapper;
    @Test
    void selectLastNByConversationId() {
        List<UserChatMessage> messageList = chatMessageMapper.selectLastNByConversationId("1", 5);
        System.out.println(messageList);
    }
}
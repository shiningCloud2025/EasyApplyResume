package com.zyh.easyapplyresume.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.user.ChatMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    List<ChatMessage> selectLastNByConversationId(@Param("conversationId") String conversationId, @Param("lastN") Integer lastN);
}
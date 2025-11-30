package com.zyh.easyapplyresume.mapper.mysql.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserChatMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface UserChatMessageMapper extends BaseMapper<UserChatMessage> {

    List<UserChatMessage> selectLastNByConversationId(@Param("conversationId") String conversationId, @Param("lastN") Integer lastN);
}
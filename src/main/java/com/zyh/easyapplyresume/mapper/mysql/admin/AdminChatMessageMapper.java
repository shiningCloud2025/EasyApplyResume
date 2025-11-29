package com.zyh.easyapplyresume.mapper.mysql.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.admin.AdminChatMessage;
import com.zyh.easyapplyresume.model.pojo.user.UserChatMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdminChatMessageMapper extends BaseMapper<AdminChatMessage> {

    List<AdminChatMessage> selectLastNByConversationId(@Param("conversationId") String conversationId, @Param("lastN") Integer lastN);
}

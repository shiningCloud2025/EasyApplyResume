package com.zyh.easyapplyresume.demo.chatmemory;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zyh.easyapplyresume.mapper.mysql.user.UserChatMessageContentTextMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserChatMessageMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserChatMessage;
import com.zyh.easyapplyresume.model.pojo.user.UserChatMessageContentText;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于MySQL持久化的对话记忆（修复反序列化InstantiationError问题）
 */
@Component
public class MySQLBasedChatMemory implements ChatMemory {

    private final UserChatMessageMapper chatMessageMapper;
    private final UserChatMessageContentTextMapper chatMessageContentTextMapper;
    // 全局唯一Kryo实例（确保配置复用）
    private static final Kryo kryo = new Kryo();

    static {
        // 关闭强制类型注册（无需提前注册所有类）
        kryo.setRegistrationRequired(false);
        // 支持无参构造函数的类实例化（适配Spring AI Message子类）
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        // 注册具体消息子类（关键：只注册可实例化的子类，不注册抽象Message接口）
        kryo.register(UserMessage.class);
        kryo.register(AssistantMessage.class);
        kryo.register(ToolResponseMessage.class);
    }

    // 构造器注入（保持原有依赖注入逻辑）
    @Autowired
    public MySQLBasedChatMemory(UserChatMessageMapper chatMessageMapper, UserChatMessageContentTextMapper chatMessageContentTextMapper) {
        this.chatMessageMapper = chatMessageMapper;
        this.chatMessageContentTextMapper = chatMessageContentTextMapper;
    }

    // --- 核心修改1：反序列化方法增加消息类型参数，直接反序列化为具体子类 ---
    /**
     * 从Base64字符串反序列化为具体Message子类（UserMessage/AssistantMessage）
     * @param base64Content 序列化后的Base64内容
     * @param messageType 消息类型（USER/ASSISTANT，从数据库获取）
     */
    private Message deserializeMessage(String base64Content, String messageType) {
        try {
            // 解码Base64为字节数组
            byte[] bytes = Base64.getDecoder().decode(base64Content);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                 Input input = new Input(bais)) {
                // 根据消息类型，直接反序列化为具体子类（避免实例化抽象接口）
                switch (messageType) {
                    case "USER":
                        return kryo.readObject(input, UserMessage.class);
                    case "ASSISTANT":
                        return kryo.readObject(input, AssistantMessage.class);
                    case "TOOL":
                        return kryo.readObject(input, ToolResponseMessage.class);
                    default:
                        throw new RuntimeException("不支持的消息类型：" + messageType);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("消息反序列化失败（类型：" + messageType + "，内容：" + base64Content + "）", e);
        }
    }

    // --- 原有序列化方法保持不变 ---
    /**
     * 序列化Message为Base64字符串（适配数据库字符串字段）
     */
    private String serializeMessage(Message message) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Output output = new Output(baos)) {
            // 序列化时直接传入具体子类（UserMessage/AssistantMessage），Kryo已注册
            kryo.writeObject(output, message);
            output.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("消息序列化失败（类型：" + message.getClass().getSimpleName() + "）", e);
        }
    }

    // --- 原有获取对话消息方法保持不变 ---
    /**
     * 获取指定对话的所有消息（按创建时间升序）
     */
    private List<UserChatMessage> getConversationMessages(String conversationId) {
        LambdaQueryWrapper<UserChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserChatMessage::getChatMessageConversationId, conversationId)
                .orderByAsc(UserChatMessage::getChatMessageCreatedTime); // 按时间升序保证消息顺序
        return chatMessageMapper.selectList(wrapper);
    }

    // --- 原有add方法保持不变（已正确记录消息类型） ---
    @Override
    public void add(String conversationId, List<Message> messages) {
        for (Message message : messages) {
            // 1. 插入主表（存储Base64序列化内容 + 消息类型）
            UserChatMessage mainEntity = new UserChatMessage();
            mainEntity.setChatMessageConversationId(conversationId);
            mainEntity.setChatMessageContent(serializeMessage(message));
            mainEntity.setChatMessageCreatedTime(new Date());
            // 标记消息类型（USER/ASSISTANT/TOOL，为反序列化提供依据）
            if (message instanceof UserMessage) {
                mainEntity.setChatMessageMessageType("USER");
            } else if (message instanceof AssistantMessage) {
                mainEntity.setChatMessageMessageType("ASSISTANT");
            } else if (message instanceof ToolResponseMessage) {
                mainEntity.setChatMessageMessageType("TOOL");
            } else {
                mainEntity.setChatMessageMessageType("UNKNOWN");
            }
            chatMessageMapper.insert(mainEntity);

            // 2. 插入文本表（存储纯文本，方便查看）
            UserChatMessageContentText textEntity = new UserChatMessageContentText();
            textEntity.setChatMessageConversationId(conversationId);
            textEntity.setChatMessageContent(message.getText());
            textEntity.setChatMessageCreatedTime(new Date());
            textEntity.setChatMessageMessageType(mainEntity.getChatMessageMessageType()); // 同步消息类型
            chatMessageContentTextMapper.insert(textEntity);
        }
    }

    // --- 核心修改2：get方法传递消息类型到反序列化方法 ---
    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 1. 查询该对话的所有消息（按时间升序）
        List<UserChatMessage> entities = getConversationMessages(conversationId);

        // 2. 截取最后N条消息（避免超出范围）
        int start = Math.max(0, entities.size() - lastN);
        List<UserChatMessage> lastNEntities = entities.subList(start, entities.size());

        // 3. 反序列化为具体Message子类（关键：传入数据库存储的消息类型）
        return lastNEntities.stream()
                .map(entity -> deserializeMessage(
                        entity.getChatMessageContent(),          // Base64序列化内容
                        entity.getChatMessageMessageType()       // 数据库记录的消息类型（USER/ASSISTANT）
                ))
                .collect(Collectors.toList());
    }

    // --- 修复clear方法的条件错误（原代码wrapper1条件错用ChatMessage字段） ---
    @Override
    public void clear(String conversationId) {
        // 1. 删除主表（user_chatMessage）数据
        LambdaQueryWrapper<UserChatMessage> mainWrapper = new LambdaQueryWrapper<>();
        mainWrapper.eq(UserChatMessage::getChatMessageConversationId, conversationId);
        chatMessageMapper.delete(mainWrapper);

        // 2. 删除文本表（chat_message_content_text）数据（修复原代码条件错误）
        LambdaQueryWrapper<UserChatMessageContentText> textWrapper = new LambdaQueryWrapper<>();
        textWrapper.eq(UserChatMessageContentText::getChatMessageConversationId, conversationId); // 用文本表自己的字段
        chatMessageContentTextMapper.delete(textWrapper);
    }
}
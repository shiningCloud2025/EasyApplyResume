package com.zyh.easyapplyresume.demo.chatmemory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 内存数据库混合对话记忆
 * @author shiningCloud2025
 */
@Component
public class InMemoryDbHybridChatMemory implements ChatMemory {
    // 内存缓存：conversationId -> 消息列表（线程安全）
    private final Map<String, List<Message>> memoryCache = new ConcurrentHashMap<>();

    // 数据库持久化层（注入已配置好Kryo的MySQLBasedChatMemory）
    private final MySQLBasedChatMemory dbChatMemory;

    // 构造器注入（只注入，不处理Kryo）
    public InMemoryDbHybridChatMemory(MySQLBasedChatMemory dbChatMemory) {
        this.dbChatMemory = dbChatMemory;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        // 1. 先更-新内存
        memoryCache.computeIfAbsent(conversationId, k -> new ArrayList<>()).addAll(messages);
        // 2. 再同步数据库（用dbChatMemory的add方法，已支持序列化）
        dbChatMemory.add(conversationId, messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 1. 先查内存
        List<Message> memoryMessages = memoryCache.get(conversationId);
        if (memoryMessages != null && !memoryMessages.isEmpty()) {
            int start = Math.max(0, memoryMessages.size() - lastN);
            return memoryMessages.subList(start, memoryMessages.size());
        }

        // 2. 内存无，查数据库（用dbChatMemory的get方法，已支持反序列化）
        List<Message> dbMessages = dbChatMemory.get(conversationId, lastN);
        // 3. 同步到内存
        if (!dbMessages.isEmpty()) {
            memoryCache.put(conversationId, new ArrayList<>(dbMessages));
        }
        return dbMessages;
    }

    @Override
    public void clear(String conversationId) {
        // 1. 清空内存
        memoryCache.remove(conversationId);
        // 2. 同步删除数据库
        dbChatMemory.clear(conversationId);
    }
}
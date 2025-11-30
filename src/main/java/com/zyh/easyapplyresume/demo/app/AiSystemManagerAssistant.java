package com.zyh.easyapplyresume.demo.app;

import com.zyh.easyapplyresume.demo.advisor.MyLoggerAdvisor;
import com.zyh.easyapplyresume.demo.advisor.ReReadingAdvisor;
import com.zyh.easyapplyresume.demo.advisor.SensitiveWordsAdvisor;
import com.zyh.easyapplyresume.demo.chatmemory.AdminInMemoryDbHybridChatMemory;
import com.zyh.easyapplyresume.demo.chatmemory.AdminMySQLBasedChatMemory;
import com.zyh.easyapplyresume.demo.chatmemory.MySQLBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 系统管理助手(B端)
 * @author shiningCloud2025
 */
@Component
@Slf4j
public class AiSystemManagerAssistant {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT =
            "你是【易投简历管理系统助手】，一个专为该系统管理员设计的AI技术支持与运营专家。\n\n" +

                    "## 核心职责\n" +
                    "1. 深度掌握易投简历系统的所有功能模块、后台设置和运维流程\n" +
                    "2. 通过专业引导和深度询问，精准诊断管理员遇到的系统问题\n" +
                    "3. 提供清晰、专业、可操作的技术解决方案和运营建议\n" +
                    "4. 帮助管理员提升系统运营效率，优化用户体验\n\n" +

                    "## 交互准则\n" +
                    "### 必须做到：\n" +
                    "- **主动引导**：用户描述问题后，立即追问关键细节（如错误提示、操作步骤、影响范围等）\n" +
                    "- **专业诊断**：使用技术术语但解释通俗，采用结构化的问题定位方法\n" +
                    "- **共情理解**：先安抚用户情绪，表达对问题影响的理解，再提供解决方案\n" +
                    "- **分步解决**：复杂问题提供清晰的步骤化解决方案，从简单到复杂排查\n\n" +

                    "### 禁止行为：\n" +
                    "- 避免提供与系统无关的建议\n" +
                    "- 不要假设用户已提供完整信息\n" +
                    "- 不要使用被动等待的回复方式\n\n" +

                    "## 问题诊断框架\n" +
                    "### 当用户问题不明确时，使用以下引导模板：\n" +
                    "\"为了更好地协助您，请告诉我您遇到的是哪类问题？\\n\" +\n" +
                    "\"• 用户与简历管理（用户数据、简历筛选、投递问题）\\n\" +\n" +
                    "\"• 企业与职位管理（职位发布、企业认证、套餐权限）\\n\" +\n" +
                    "\"• 系统设置与配置（权限分配、参数调整、邮件模板）\\n\" +\n" +
                    "\"• 数据统计与报表（数据看板、报表导出、统计异常）\\n\" +\n" +
                    "\"• 系统性能与异常（卡顿、报错、功能失效）\"\n\n" +

                    "### 具体领域追问指南：\n" +
                    "**用户与简历问题** → 追问：用户类型、具体操作步骤、错误提示、影响范围\n" +
                    "**企业与职位问题** → 追问：发布流程卡点、账户状态、套餐权限、错误信息\n" +
                    "**系统配置问题** → 追问：目标效果、当前设置、权限层级、参数配置\n" +
                    "**数据报表问题** → 追问：时间范围、数据维度、显示状态、导出格式\n\n" +

                    "## 解决方案格式\n" +
                    "1. **确认问题**：\"根据描述，您的问题是：[清晰复述]\"\n" +
                    "2. **原因分析**：\"这可能由于：[列出1-3个主要原因]\"\n" +
                    "3. **解决步骤**：\n" +
                    "   \"步骤1：[最简单操作，如清除缓存、刷新]\\n\" +\n" +
                    "   \"步骤2：[检查配置，如权限设置、参数验证]\\n\" +\n" +
                    "   \"步骤3：[深度排查，如日志检查、数据验证]\"\n" +
                    "4. **预防建议**：\"为避免再现，建议：[1-2条优化建议]\"\n" +
                    "5. **后续引导**：\"如果问题仍在，请提供[具体信息]我将进一步排查\"\n\n" +

                    "## 核心目标\n" +
                    "通过专业的引导式对话，确保每个系统问题都能得到准确诊断和有效解决，帮助管理员提升系统稳定性和用户体验。";

    public AiSystemManagerAssistant(ChatModel dashscopeChatModel, AdminMySQLBasedChatMemory adminMySQLBasedChatMemory, SensitiveWordsAdvisor sensitiveWordsAdvisor){
        // 初始化基于混合持久化(内存+数据库)的对话记忆
        ChatMemory chatMemory = new AdminInMemoryDbHybridChatMemory(adminMySQLBasedChatMemory);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 自定义日志Advisor，可按需开启
                        new MyLoggerAdvisor(),
                        // 自定义Re2Advisor,可按需开启
                        new ReReadingAdvisor(),
                        // 自定义敏感词过滤Advisor，可按需开启
                        sensitiveWordsAdvisor
                )
                .build();
    }

    @Resource
    private Advisor aiSystemManagerAssistantRagCloudAdvisor;
    @Resource
    private ToolCallback[] allTools;
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    /**
     * AI系统管理助手,面向B端用户
     * 使用技术:大模型调用+检索器+工具调用+MCP+RAG
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> AiSystemManagerAssistantDoChatWithStream(String message, String chatId){
        return chatClient.prompt()
                .user(message)
                .system(SYSTEM_PROMPT)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,20))
                .advisors(aiSystemManagerAssistantRagCloudAdvisor)
                .tools(allTools)
                .tools(toolCallbackProvider)
                .stream()
                .content();
    }


}

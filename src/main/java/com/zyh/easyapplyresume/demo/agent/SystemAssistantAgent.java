package com.zyh.easyapplyresume.demo.agent;

import com.zyh.easyapplyresume.demo.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * 系统助手智能体
 * @author shiningCloud2025
 */
@Component
public class SystemAssistantAgent extends ToolCallAgent{

    public SystemAssistantAgent(ToolCallback[] availableTools, ChatModel dashscopeChatModel){
        super(availableTools);
        this.setName("ResumeAssistantAgent");
        String SYSTEM_PROMPT = """
                你是 ResumeAssistantAgent，一个全能的 AI 助手，旨在解决用户提出的任何任务。
                你拥有各种可供调用的工具，可以高效地完成复杂的请求。
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """
                根据用户需求，主动选择最合适的工具或工具组合。
                对于复杂任务，你可以分解问题并逐步使用不同的工具来解决。
                使用每个工具后，清楚地解释执行结果并建议下一步操作。
                如果你想在任何时候停止交互，请使用 `terminate` 工具/函数调用。
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxSteps(10);
        // 初始化客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new MyLoggerAdvisor()
//                ,new MessageChatMemoryAdvisor(chatMemory))
                )
                .build();
        this.setChatClient(chatClient);
    }
}

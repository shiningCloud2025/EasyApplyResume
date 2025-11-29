package com.zyh.easyapplyresume.demo.agent;

import com.zyh.easyapplyresume.demo.advisor.MyLoggerAdvisor;
import com.zyh.easyapplyresume.demo.chatmemory.InMemoryDbHybridChatMemory;
import com.zyh.easyapplyresume.demo.chatmemory.MySQLBasedChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * 简历助手智能体
 * @author shiningCloud2025
 */
@Component
public class ResumeAssistantAgent extends ToolCallAgent {

    public ResumeAssistantAgent(ToolCallback[] availableTools, ChatModel dashscopeChatModel) {
        super(availableTools);
        this.setName("ResumeAssistantAgent");
        String SYSTEM_PROMPT = """  
                You are ResumeAssistantAgent, an all-capable AI assistant, aimed at solving any task presented by the user.  
                You have various tools at your disposal that you can call upon to efficiently complete complex requests.  
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """  
                Based on user needs, proactively select the most appropriate tool or combination of tools.  
                For complex tasks, you can break down the problem and use different tools step by step to solve it.  
                After using each tool, clearly explain the execution results and suggest the next steps.  
                If you want to stop the interaction at any point, use the `terminate` tool/function call.  
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

package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.demo.agent.SystemAssistantAgent;
import com.zyh.easyapplyresume.demo.app.AiSystemManagerAssistant;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 *  AI系统管理助手控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/aiSystemManagerAssistant")
public class AISystemManagerAssistantController {
    @Resource
    private AiSystemManagerAssistant aiSystemManagerAssistant;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    @Operation(summary = "AI系统管理助手应用对话")
    @PostMapping(value= "/application/chat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> applicationChat(@RequestBody String message,
                                        @RequestParam(required = false,value = "chatId") String chatId){
        chatId = UUID.randomUUID().toString();
        return aiSystemManagerAssistant.AiSystemManagerAssistantDoChatWithStream(message,chatId);
    }

    @Operation(summary = "AI系统管理助手Agent对话")
    @PostMapping(value = "/agent/chat")
    public SseEmitter agentChat(@RequestBody String message, @RequestParam(required = false,value = "chatId") String chatId){
        chatId = UUID.randomUUID().toString();
        SystemAssistantAgent systemAssistantAgent = new SystemAssistantAgent(allTools,dashscopeChatModel);
        return systemAssistantAgent.runStream(message,chatId);
    }


}

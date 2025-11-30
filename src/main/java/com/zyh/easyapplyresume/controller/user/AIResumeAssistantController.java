package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.demo.agent.ResumeAssistantAgent;
import com.zyh.easyapplyresume.demo.app.AiResumeAssistant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 *  AI简历助手控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/aiResumeAssistant")
@Tag(name="AI简历助手控制器-用户端")
public class AIResumeAssistantController {
    @Resource
    private AiResumeAssistant aiResumeAssistant;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    @Operation(summary = "AI简历助手应用对话")
    @PostMapping(value = "/application/chat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> applicationChat(@RequestBody String message,
                                        @RequestParam(required = false,value = "chatId") String chatId){
        chatId = UUID.randomUUID().toString();
        return aiResumeAssistant.AiResumeAssistantDoChatWithStream(message,chatId);
    }

    @Operation(summary = "AI简历助手Agent对话")
    @PostMapping(value = "/agent/chat")
    public SseEmitter agentChat(@RequestBody String message, @RequestParam(required = false,value = "chatId") String chatId){
        chatId = UUID.randomUUID().toString();
        ResumeAssistantAgent resumeAssistantAgent = new ResumeAssistantAgent(allTools,dashscopeChatModel);
        return resumeAssistantAgent.runStream(message,chatId);
    }

}

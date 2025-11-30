package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.demo.app.AiSystemManagerAssistant;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Get



}

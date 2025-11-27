package com.zyh.easyapplyresume.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;


/**
 * MCP客户端配置
 * @author shiningCloud2025
 */
@Component
@Slf4j
public class CustomMcpSyncClientCustomizer implements McpSyncClientCustomizer {
    @Override
    public void customize(String name, McpClient.SyncSpec spec) {
        // 自定义请求超时配置
//        spec.requestTimeout(Duration.ofSeconds(120));
        spec.requestTimeout(Duration.ofMinutes(5));        // 5分钟

        // 设置客户端可以访问的根目录URI
//        spec.roots(roots);
        // 设置处理消息创建请求的自定义采样处理器
//        spec.sampling((CreateMessageRequest messageRequest) -> {
//            // 处理采样
//            CreateMessageResult result = ...
//            return result;
//        });
//
        // 添加在可用工具变更时通知的消费者
        spec.toolsChangeConsumer((List<McpSchema.Tool> tools) -> {
            // 处理工具变更
            log.info("MCP 工具列表已更新，当前可用工具数量: {}", tools.size());
            tools.forEach(tool -> {
                log.info("工具名称: {}, 描述: {}", tool.name(), tool.description());
            });

        });
//
//        // 添加在可用资源变更时通知的消费者
//        spec.resourcesChangeConsumer((List<McpSchema.Resource> resources) -> {
//            // 处理资源变更
//        });
//
//        // 添加在可用提示词变更时通知的消费者
//        spec.promptsChangeConsumer((List<McpSchema.Prompt> prompts) -> {
//            // 处理提示词变更
//        });
//
        // 添加接收服务器日志消息时通知的消费者
        spec.loggingConsumer((McpSchema.LoggingMessageNotification logMessage) -> {
            // 处理日志消息
            // 根据日志级别分类处理
            String levelStr = logMessage.level().toString().toUpperCase();
            String data = logMessage.data();

            if (levelStr.equals("ERROR") || levelStr.equals("CRITICAL")) {
                log.error("MCP 错误: {}", data);
            } else if (levelStr.equals("WARNING")) {
                log.warn("MCP 警告: {}", data);
            } else if (levelStr.equals("INFO")) {
                log.info("MCP 信息: {}", data);
            } else if (levelStr.equals("DEBUG")) {
                log.debug("MCP 调试: {}", data);
            } else {
                log.trace("MCP 未知级别[{}]: {}", levelStr, data);
            }

        });

    }
}

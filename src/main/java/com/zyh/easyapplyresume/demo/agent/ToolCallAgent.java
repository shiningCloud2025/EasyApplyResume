//package com.zyh.easyapplyresume.demo.agent;
//
//import cn.hutool.core.collection.CollUtil;
//import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
//import dev.langchain4j.agent.tool.P;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.messages.AssistantMessage;
//import org.springframework.ai.chat.messages.Message;
//import org.springframework.ai.chat.messages.ToolResponseMessage;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.chat.prompt.ChatOptions;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.model.tool.ToolCallingManager;
//import org.springframework.ai.model.tool.ToolExecutionResult;
//import org.springframework.ai.tool.ToolCallback;
//import org.springframework.ai.tool.ToolCallbacks;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 工具调用Agent
// * 处理工具调用的基础代理类，具体实现了think和act方法，可以用作创建实例的父类
// */
//@Data
//@Slf4j
//public class ToolCallAgent extends ReActAgent{
//
//    // 可用的工具
//    private final ToolCallback[] availableTools;
//
//    // 保存了工具调用信息的响应
//    private ChatResponse toolCallChatResponse;
//
//    // 工具调用管理者
//    private final ToolCallingManager toolCallingManager;
//
//    // 禁用内置的工具调用机制，自已维护上下文
//    private final ChatOptions chatOptions;
//
//    public ToolCallAgent(ToolCallback[] availableTools){
//        super();
//        this.availableTools = availableTools;
//        this.toolCallingManager = ToolCallingManager.builder().build();
//        // 禁止SpringAI内置的工具调用机制,自已维护选项和消息上下文
//        this.chatOptions = DashScopeChatOptions.builder()
//                .withProxyToolCalls(true)
//                .build();
//    }
//
//    @Override
//    public boolean think() {
//        if (getNextStepPrompt()!=null&&!getNextStepPrompt().isEmpty()){
//            UserMessage userMessage = new UserMessage(getNextStepPrompt());
//            getMessageList().add(userMessage);
//        }
//        List<Message> messageList = getMessageList();
//        Prompt prompt = new Prompt(messageList,chatOptions);
//        try{
//            // 获取带工具选项的响应
//            ChatResponse chatResponse = getChatClient().prompt(prompt)
//                    .system(getSystemPrompt())
//                    .tools(availableTools)
//                    .call()
//                    .chatResponse();
//            // 记录响应,用于Act
//            this.toolCallChatResponse = chatResponse;
//            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
//            // 输出提示信息
//            String result = assistantMessage.getText();
//            List<AssistantMessage.ToolCall> toolCallsList = assistantMessage.getToolCalls();
//            log.info(getName() + "的思考: " + result);
//            log.info(getName() + "选择了 " + toolCallsList.size() + " 个工具来使用");
//            String toolCallInfo = toolCallsList.stream()
//                    .map(toolCall -> String.format("工具名称：%s，参数：%s",
//                            toolCall.name(),
//                            toolCall.arguments())
//                    )
//                    .collect(Collectors.joining("\n"));
//            log.info(getName() + "选择的工具信息：\n" + toolCallInfo);
//            if (toolCallsList.isEmpty()){
//                // 只有不调用工具时，才记录助手消息
//                getMessageList().add(assistantMessage);
//                return false;
//            }else {
//                // 需要调用工具时，无需记录助手信息，因为调用工具时会自动记录
//                return true;
//            }
//
//        }catch (Exception e){
//            log.error(getName() + "的思考过程遇到了问题: " + e.getMessage());
//            getMessageList().add(new AssistantMessage("处理时遇到错误: " + e.getMessage()));
//            return false;
//        }
//
//    }
//
//    /**
//     * 执行工具调用并处理结果
//     * @return 执行结果
//     */
//    @Override
//    public String act() {
//        if (!toolCallChatResponse.hasToolCalls()){
//            return "没有工具调用";
//        }
//        // 调用工具
//        Prompt prompt  = new Prompt(getMessageList(),chatOptions);
//        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);
//        // 记录消息上下文,conversationHistory 已经包含了助手消息和工具调用返回的结果
//        setMessageList(toolExecutionResult.conversationHistory());
//        // 当前工具调用的结果
//        ToolResponseMessage toolResponseMessage  = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
//        String results = toolResponseMessage.getResponses().stream()
//                .map(response -> "工具 " + response.name() + " 完成了它的任务！结果: " + response.responseData())
//                .collect(Collectors.joining("\n"));
//        log.info(results);
//        return results;
//    }
//}

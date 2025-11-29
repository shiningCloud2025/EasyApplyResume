package com.zyh.easyapplyresume.demo.agent;

import com.zyh.easyapplyresume.demo.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.StringUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象基础代理类
 * 用于管理代理状态和执行流程
 * 提供状态转换、内存管理和基于步骤的执行循环的基础功能
 * 注意:子类必须实现step方法
 * @author shiningCloud2025
 */
@Data
@Slf4j
public abstract class BaseAgent {
    // 核心属性
    private String name;

    // 提示词
    private String systemPrompt;
    private String nextStepPrompt;

    // LLM
    private ChatClient chatClient;

    // 状态
    private AgentState state = AgentState.IDLE;

    // 执行控制
    private int maxSteps = 10;
    private int currentStep = 0;

    // Memory(需要自主维护回话上下文)
    private List<Message> messageList = new LinkedList<>();

    /**
     * 运行Agent
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public String run(String userPrompt){
        if(this.state!=AgentState.IDLE){
            throw new RuntimeException("Cannot run agent from state: "+this.state);
        }
        if (StringUtil.isEmpty(userPrompt)){
            throw new RuntimeException("Cannot run agent with empty user prompt");
        }
        // 更改状态
        state = AgentState.RUNNING;
        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 保存结果列表
        List<String> results = new LinkedList<>();
        try{
            for(int i=0;i<maxSteps&&state!=AgentState.FINISHED;i++){
                int stepNumber = i+1;
                currentStep = stepNumber;
                log.info("Executing step " + stepNumber + "/" + maxSteps);
                // 单步执行
                String stepResult = step();
                results.add(stepResult);
            }
            // 检查是否超出步骤限制
            if (currentStep >= maxSteps){
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            }
            return String.join("\n", results);
        }catch (Exception e){
            state = AgentState.ERROR;
            log.error("Error executing agent: ", e);
            return "执行错误"+e.getMessage();
        }finally {
            // 清理资源
            this.cleanup();
        }
    }

    /**
     * 运行Agent(流式)
     * @param userPrompt
     * @return
     */
    public SseEmitter runStream(String userPrompt,String chatId){
        // 创建SseEmitter,设置较长的超时时间
        SseEmitter emitter = new SseEmitter(300000L); // 5分钟超时

        // 使用线程异步处理，避免阻塞主线程
        CompletableFuture.runAsync(()->{
            try{
                if (this.state != AgentState.IDLE) {
                    emitter.send("错误：无法从状态运行代理: " + this.state);
                    emitter.complete();
                    return;
                }
                if (StringUtil.isEmpty(userPrompt)) {
                    emitter.send("错误：不能使用空提示词运行代理");
                    emitter.complete();
                    return;
                }

                // 更改状态
                state = AgentState.RUNNING;
                // 记录消息上下文
                messageList.add(new UserMessage(userPrompt));

                try {
                    for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                        int stepNumber = i + 1;
                        currentStep = stepNumber;
                        log.info("Executing step " + stepNumber + "/" + maxSteps);

                        // 单步执行
                        String stepResult = step();
                        String result = "Step " + stepNumber + ": " + stepResult;

                        // 发送每一步的结果
                        emitter.send(result);
                    }
                    // 检查是否超出步骤限制
                    if (currentStep >= maxSteps) {
                        state = AgentState.FINISHED;
                        emitter.send("执行结束: 达到最大步骤 (" + maxSteps + ")");
                    }
                    // 正常完成
                    emitter.complete();
                     }catch (Exception e){
                        state = AgentState.ERROR;
                        try{
                            emitter.send("执行错误: " + e.getMessage());
                            emitter.complete();
                        }catch (Exception ex) {
                            emitter.completeWithError(ex);
                        }
                }
            }catch (Exception e){
                emitter.completeWithError(e);
            }
        });

        // 设置超时和完成回调
        emitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE连接超时");
        });

        emitter.onCompletion(() -> {
            if (this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE连接完成");
        });

        return emitter;
    }



    /**
     * 执行单个步骤
     * @return 步骤执行结果
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanup(){
        // 子类可以重写这个方法来清理资源！
    }

}

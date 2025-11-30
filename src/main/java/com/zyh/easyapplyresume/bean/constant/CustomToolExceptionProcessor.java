package com.zyh.easyapplyresume.bean.constant;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.execution.ToolExecutionException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * 工具调用异常处理--具体逻辑
 * @author shiningCloud2025
 */
@Component
public class CustomToolExceptionProcessor implements ToolExecutionExceptionProcessor{
    // 引入日志框架，用于记录异常信息
    private static final Logger logger = LoggerFactory.getLogger(CustomToolExceptionProcessor.class);
    @Override
    public String process(ToolExecutionException exception) {
        // 获取原始的、根本的异常原因
        Throwable rootCause = exception.getCause();

        // --- 开始根据不同的异常类型进行处理 ---

        // 1. 处理网络超时异常
        if (rootCause instanceof SocketTimeoutException) {
            logger.error("工具调用失败：网络连接超时", rootCause);
            return "连接外部服务超时。这通常是由于网络拥堵或服务响应慢。请尝试简化你的请求，或者稍后再试。";
        }
        // 2. 处理其他I/O相关错误，如连接被拒绝等
        else if (rootCause instanceof IOException) {
            logger.error("工具调用失败：I/O错误", rootCause);
            return "无法连接到外部资源。请检查你的网络连接是否正常。";
        }
        // 3. 处理我们自定义的业务异常
        else if (rootCause instanceof BusException) {
            BusException busException = (BusException) rootCause;
            logger.warn("工具调用业务异常: [{}] {}", busException.getCode(), busException.getMessage());
            // 直接返回业务异常信息，因为它通常已经是用户友好的
            return "操作失败: " + busException.getMessage();
        }
        // 4. 处理安全相关的严重错误，如认证失败
        else if (rootCause instanceof SecurityException) {
            logger.error("工具调用失败：安全异常，这是严重问题！", rootCause);
            // 对于安全问题，我们选择中断流程，而不是返回消息给AI
            throw exception;
        }
        // 5. 处理参数错误（例如AI生成的工具调用参数不正确）
        else if (rootCause instanceof IllegalArgumentException) {
            logger.warn("工具调用失败：参数无效", rootCause);
            return "提供的参数无效。请检查参数格式和取值范围后重试。";
        }
        // 6. 处理空指针异常（这通常是代码bug）
        else if (rootCause instanceof NullPointerException) {
            logger.error("工具调用失败：发生空指针异常，这是一个潜在的代码错误", rootCause);
            // 空指针通常意味着代码有问题，应立即失败以便修复
            return "出现了空指针异常,已经联系工程师处理！";
        }
        // 7. 所有其他未知异常的兜底处理
        else {
            logger.error("工具调用失败：发生未知异常", rootCause);
            return "很抱歉，系统在执行操作时发生了一个未知错误。工程师已收到通知并正在处理。";
        }
    }
}

package com.zyh.easyapplyresume.bean.constant;

import org.springframework.ai.tool.execution.ToolExecutionException;

/**
 * 工具调用异常处理
 * @author shiningCloud2025
 */
@FunctionalInterface // 函数式接口：只包含一个抽象方法的接口
public interface ToolExecutionExceptionProcessor {
    /**
     * 将工具抛出的异常转换为发送给 AI 模型的字符串，或者抛出一个新异常由调用者处理
     */
    String process(ToolExecutionException exception);
}

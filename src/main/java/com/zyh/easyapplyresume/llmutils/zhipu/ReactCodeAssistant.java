package com.zyh.easyapplyresume.llmutils.zhipu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * React代码AI助手
 * 使用智谱AI(glm-4-air)辅助优化和修改React代码
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class ReactCodeAssistant {

    @Autowired
    private ZhiPuAiChatModel zhipuAiChatModel;

    public String assistReactCode(String userRequest, String currentCode) {
        try {
            String prompt = """
                    你是React代码优化助手。用户需求：
                    %s
                    
                    当前代码：
                    %s
                    
                    请根据用户需求优化代码，要求：
                    1. 代码必须语法正确，可直接运行
                    2. 保持原有功能不变
                    3. 只返回完整的优化后代码，不要任何解释、markdown标记或注释
                    
                    请直接返回优化后的完整React代码：
                    """.formatted(userRequest, currentCode);

            log.info("开始智谱AI辅助优化React代码，用户需求：{}", userRequest);
            String optimizedCode = ChatClient.create(zhipuAiChatModel)
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            return cleanReactCode(optimizedCode);
        } catch (Exception e) {
            log.error("AI优化React代码失败", e);
            throw new RuntimeException("AI优化React代码失败: " + e.getMessage());
        }
    }

    private String cleanReactCode(String code) {
        code = code.replaceAll("```jsx", "")
                .replaceAll("```javascript", "")
                .replaceAll("```react", "")
                .replaceAll("```", "")
                .trim();
        return code;
    }
}

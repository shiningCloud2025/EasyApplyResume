package com.zyh.easyapplyresume.mlmodel.scoremodel;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * DashScope 向量化服务
 * @author shiningCloud2025
 */
@Slf4j
@Service
public class DashScopeEmbeddingService {
    @Value("${spring.ai.dashscope.api-key:${dashscope.api-key:}}")
    private String dashscopeApiKey;

    /**
     * 默认 embedding 模型
     */
    @Value("${score-model.embedding-model:text-embedding-v4}")
    private String defaultEmbeddingModel;

    @PostConstruct
    public void init() {
        if (dashscopeApiKey == null || dashscopeApiKey.trim().isEmpty()) {
            throw new RuntimeException("DashScope ApiKey 未配置");
        }
        Constants.apiKey = dashscopeApiKey;
    }

    /**
     * 使用默认模型做向量化
     */
    public float[] embed(String text) {
        return embed(text, defaultEmbeddingModel);
    }

    /**
     * 指定模型做向量化
     */
    public float[] embed(String text, String modelName) {
        try {
            String finalText = text == null ? "" : text.trim();
            if (finalText.isEmpty()) {
                throw new RuntimeException("向量化文本不能为空");
            }

            String finalModelName = (modelName == null || modelName.trim().isEmpty())
                    ? defaultEmbeddingModel
                    : modelName.trim();

            TextEmbeddingParam param = TextEmbeddingParam.builder()
                    .model(finalModelName)
                    .texts(Collections.singletonList(finalText))
                    .dimension(512)
                    .build();

            TextEmbedding textEmbedding = new TextEmbedding();
            TextEmbeddingResult result = textEmbedding.call(param);

            if (result == null
                    || result.getOutput() == null
                    || result.getOutput().getEmbeddings() == null
                    || result.getOutput().getEmbeddings().isEmpty()) {
                throw new RuntimeException("embedding 结果为空");
            }

            List<?> embeddingList = result.getOutput().getEmbeddings().get(0).getEmbedding();
            if (embeddingList == null || embeddingList.isEmpty()) {
                throw new RuntimeException("embedding 向量为空");
            }

            float[] embeddingArray = new float[embeddingList.size()];
            for (int i = 0; i < embeddingList.size(); i++) {
                Object value = embeddingList.get(i);
                if (!(value instanceof Number number)) {
                    throw new RuntimeException("embedding 结果格式错误");
                }
                embeddingArray[i] = number.floatValue();
            }

            return embeddingArray;
        } catch (NoApiKeyException e) {
            log.error("DashScope embedding 调用失败，ApiKey 未配置", e);
            throw new RuntimeException("DashScope embedding 调用失败");
        } catch (Exception e) {
            log.error("DashScope embedding 调用失败", e);
            throw new RuntimeException("DashScope embedding 调用失败");
        }
    }
}

package com.zyh.easyapplyresume.demo.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * AI简历助手向量数据库配置(SpringAI自带的基于内存的SimpleVectorStore)
 * @author shiningCloud2025
 */
@Configuration
public class AiResumeAssistantVectorStoreConfig {

    @Resource
    private AiResumeAssistantDocumentLoader aiResumeAssistantDocumentLoader;

    @Bean
    VectorStore aiResumeAssistantVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 加载文档
        List<Document> documents = aiResumeAssistantDocumentLoader.loadMarkdowns();
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }

}

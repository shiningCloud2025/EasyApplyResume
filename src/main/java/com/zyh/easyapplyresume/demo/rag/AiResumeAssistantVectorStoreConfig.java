package com.zyh.easyapplyresume.demo.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
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
    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;
    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Bean
    VectorStore aiResumeAssistantVectorStore(EmbeddingModel dashscopeEmbeddingModel) {

        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 加载文档
        List<Document> documents = aiResumeAssistantDocumentLoader.loadMarkdowns();
        // 自主切分
        List<Document> splitDocuments = myTokenTextSplitter.splitDocuments(documents);
        // 自动补充关键词元信息
        List<Document> splitDocuments1 = myKeywordEnricher.enrichDocuments( documents);
        simpleVectorStore.add(splitDocuments1);
        return simpleVectorStore;
    }

}

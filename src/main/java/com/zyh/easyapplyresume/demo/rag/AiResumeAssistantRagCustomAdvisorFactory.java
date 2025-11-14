package com.zyh.easyapplyresume.demo.rag;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;


/**
 * 文档过滤和检索-根据用户查询需求生成对应的advisor
 * @author shiningCloud2025
 */
@Component
public class AiResumeAssistantRagCustomAdvisorFactory {
    public static Advisor creatAiResumeAssistantRagCustomAdvisor(VectorStore vectorStore,String status){
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                // 过滤条件
                .filterExpression( expression)
                // 相似度过滤
                .similarityThreshold(0.5)
                // 返回文档数量
                .topK(3)
                .build();
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();
    }
}

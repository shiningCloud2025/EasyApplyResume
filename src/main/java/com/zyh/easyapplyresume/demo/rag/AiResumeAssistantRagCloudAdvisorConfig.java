package com.zyh.easyapplyresume.demo.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI简历助手RAG云知识库配置
 * @author shiningCloud2025
 */
@Configuration
@Slf4j
public class AiResumeAssistantRagCloudAdvisorConfig {

    @Value("${ali.bailian.api-key}")
    private String dashScopeApiKey;

    /**
     * 参数配置:
     * 1.自动同步知识索引
     * 2.智能分割+1000最大分段+Metadata抽取+官方向量v4+官方排序+0.45相似度+10召回+平台存储
     * @return
     */
    @Bean
    public Advisor aiResumeAssistantRagCloudAdvisor() {
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        final String KNOWLEDGE_INDEX = "AI简历助手云库-易投简历";
        DocumentRetriever documentRetriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KNOWLEDGE_INDEX)
                        .build());
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(
                        ContextualQueryAugmenter.builder()
                                .allowEmptyContext(true)
                                .build()
                )
                .build();
    }




}

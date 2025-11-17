package com.zyh.easyapplyresume.demo.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 优化组件-文档收集和切割-元数据自动标注组件
 * @author shiningCloud2025
 */
@Component
public class MyKeywordEnricher {
    @Resource
    private ChatModel dashscopeChatModel;

    List<Document> enrichDocuments(List<Document> documents){
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(this.dashscopeChatModel,5);
        return enricher.apply(documents);
    }
}

package com.zyh.easyapplyresume.demo.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * AI简历助手文档加载器
 * @author shiningCloud2025
 */
@Component
@Slf4j
public class AiResumeAssistantDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;


    AiResumeAssistantDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdowns(){
        List<Document> allDocuments = new LinkedList<>();
        try{
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for(Resource resource : resources){
                String fileName = resource.getFilename();
                // 提取文档倒数第 3 和第 2 个字作为标签
                String status = fileName.substring(fileName.length() - 6, fileName.length() - 4);
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        // 水平分隔线作为文档分割点
                        .withHorizontalRuleCreateDocument(true)
                        // 不包含代码块
                        .withIncludeCodeBlock(false)
                        // 不包括引用块
                        .withIncludeBlockquote(false)
                        // 元信息
                        .withAdditionalMetadata("fileName", fileName)
                        .withAdditionalMetadata("status", status)
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                allDocuments.addAll(reader.read());
            }
        }catch (Exception e ){
            log.error("加载文档失败：{}", e.getMessage());
        }
        return allDocuments;
    }
}

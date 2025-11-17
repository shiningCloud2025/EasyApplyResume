package com.zyh.easyapplyresume.demo.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * AI简历助手自定义处理器(处理知识库没有对应内容的场景)
 * @author shiningCloud2025
 */
public class AiResumeAssistantContextualQueryAugmenterFactory {
   public static ContextualQueryAugmenter createInstance(){
       PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                   你应该输出下面的内容：
                   抱歉，我只能回答简历和求职相关的问题，别的没办法帮到您哦，
                   有问题可以联系易投简历客服:shining_cloud2025@163.com
                   """);
       return ContextualQueryAugmenter.builder()
               .allowEmptyContext(false)
               .emptyContextPromptTemplate(emptyContextPromptTemplate)
               .build();

   }
}

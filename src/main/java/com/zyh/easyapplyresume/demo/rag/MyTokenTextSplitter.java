package com.zyh.easyapplyresume.demo.rag;

import cn.hutool.extra.tokenizer.TokenizerUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文档收集和切割-文档切片
 * @author shiningCloud2025
 */
@Component
public class MyTokenTextSplitter {
    public List<Document> splitDocuments(List<Document> documents){
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.apply( documents);
    }

    public List<Document> splitCustomized(List<Document> documents){
        TokenTextSplitter splitter = new TokenTextSplitter(200,100,10,5000,true);
        return splitter.apply( documents);
    }

}


package com.zyh.easyapplyresume.demo.advisor;

import com.zyh.easyapplyresume.bean.BusException;
import com.zyh.easyapplyresume.bean.CodeEnum;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义SensitiveWordsAdvisor
 * 过滤给大模型的敏感词，把它变成*****
 */
@Slf4j
@Component // 必须注册为Spring组件才能生效
public class SensitiveWordsAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    // 从配置文件注入敏感词列表
    @Value("${sensitive.words-user}")
    private String sensitiveWordsStr;

    // 拆分后的敏感词列表（初始化后复用）
    private List<String> sensitiveWordList;


    // 初始化：将字符串拆分为敏感词列表（仅启动时执行一次）
    @PostConstruct
    public void initSensitiveWords() {
        // 处理空配置：如果没配置敏感词，初始化空列表
        if (sensitiveWordsStr == null || sensitiveWordsStr.trim().isEmpty()) {
            sensitiveWordList = List.of();
            log.info("未配置用户敏感词，敏感词过滤功能不启用");
            return;
        }

        // 拆分逻辑：中文逗号分隔 → 去空格 → 过滤空字符串 → 去重
        sensitiveWordList = Arrays.stream(sensitiveWordsStr.split("，"))
                .map(String::trim) // 处理配置中的空格（比如换行导致的空格）
                .filter(word -> !word.isEmpty()) // 过滤空字符串（比如配置末尾多一个逗号）
                .distinct() // 去重，避免重复过滤
                .collect(Collectors.toList());

        log.info("敏感词初始化完成，共加载 {} 个敏感词", sensitiveWordList.size());
    }
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        return chain.nextAroundCall(this.before(advisedRequest));
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        return chain.nextAroundStream(this.before(advisedRequest));
    }


    // 前置处理：空输入校验 + 敏感词过滤
    private AdvisedRequest before(AdvisedRequest advisedRequest){
        String userInput = advisedRequest.userText().toString();
        // 1. 空输入校验
        if(userInput.isEmpty()&&userInput.length()==0){
            throw new BusException(CodeEnum.LLM_USERINPUT_EMPTY.getCode(),CodeEnum.LLM_USERINPUT_EMPTY.getMessage());
        }
        // 2. 敏感词过滤
        String filteredInput = filterSensitiveWords(userInput);

        // 3. 构建过滤后的请求（原请求不可变，需重新构建）
        return AdvisedRequest.from(advisedRequest)
                .userText(filteredInput)
                .build();
    }
    // 核心：敏感词过滤逻辑
    private String filterSensitiveWords(String userInput) {
        String result = userInput;

        // 遍历敏感词列表，判断并替换
        for (String sensitiveWord : sensitiveWordList) {
            // 跳过空敏感词（理论上初始化时已过滤，这里做双重保障）
            if (sensitiveWord.isEmpty()) {
                continue;
            }

            // 判断用户输入是否包含当前敏感词
            if (result.contains(sensitiveWord)) {
                // 替换为*****（也可改为与敏感词等长："*".repeat(sensitiveWord.length())）
                String replacement = "*****";
                result = result.replace(sensitiveWord, replacement);
                log.info("过滤敏感词：{} → 替换为：{}", sensitiveWord, replacement);
            }
        }

        return result;
    }

    @Override
    public String getName() {
        return SensitiveWordsAdvisor.class.getSimpleName();
    }

    @Override
    public int getOrder() {
        return -2147482649;
    }
}

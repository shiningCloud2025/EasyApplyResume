package com.zyh.easyapplyresume.llmutils.zhipu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 简历关键词提取器
 * 使用智谱AI(glm-4-air)从简历中提取关键词和技能标签
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class ResumeKeywordExtractor {

    @Autowired
    private ZhiPuAiChatModel zhipuAiChatModel;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KeywordExtractionResult extractKeywords(String resumeText) {
        try {
            String prompt = """
                    你是简历分析专家。请从以下简历中提取关键信息。
                    
                    简历内容：
                    %s
                    
                    请提取以下信息并返回JSON格式：
                    {
                      "skills": ["Java", "Spring Boot", "MySQL"],
                      "keywords": ["微服务", "高并发", "团队协作"],
                      "education": ["本科", "计算机科学与技术", "XX大学"],
                      "experience": ["3年工作经验", "互联网行业"],
                      "strengths": ["技术能力强", "项目经验丰富"]
                    }
                    
                    要求：
                    1. skills: 技术栈、工具、框架（5-15个）
                    2. keywords: 高频关键词（5-10个）
                    3. education: 教育背景关键信息
                    4. experience: 工作经验概述
                    5. strengths: 核心优势（3-5个）
                    
                    只返回JSON，不要其他内容。
                    """.formatted(resumeText);

            log.info("开始智谱AI提取简历关键词");
            String response = ChatClient.create(zhipuAiChatModel)
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            return parseKeywordResult(response);
        } catch (Exception e) {
            log.error("AI提取简历关键词失败", e);
            throw new RuntimeException("AI提取简历关键词失败: " + e.getMessage());
        }
    }

    private KeywordExtractionResult parseKeywordResult(String jsonResponse) {
        try {
            String cleanJson = jsonResponse
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            Map<String, Object> data = objectMapper.readValue(cleanJson, new TypeReference<Map<String, Object>>() {});

            KeywordExtractionResult result = new KeywordExtractionResult();
            result.setSkills((List<String>) data.getOrDefault("skills", new ArrayList<>()));
            result.setKeywords((List<String>) data.getOrDefault("keywords", new ArrayList<>()));
            result.setEducation((List<String>) data.getOrDefault("education", new ArrayList<>()));
            result.setExperience((List<String>) data.getOrDefault("experience", new ArrayList<>()));
            result.setStrengths((List<String>) data.getOrDefault("strengths", new ArrayList<>()));

            return result;
        } catch (Exception e) {
            log.error("解析AI返回的JSON失败", e);
            throw new RuntimeException("解析AI返回的JSON失败");
        }
    }

    public static class KeywordExtractionResult {
        private List<String> skills;
        private List<String> keywords;
        private List<String> education;
        private List<String> experience;
        private List<String> strengths;

        public List<String> getSkills() {
            return skills;
        }

        public void setSkills(List<String> skills) {
            this.skills = skills;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public List<String> getEducation() {
            return education;
        }

        public void setEducation(List<String> education) {
            this.education = education;
        }

        public List<String> getExperience() {
            return experience;
        }

        public void setExperience(List<String> experience) {
            this.experience = experience;
        }

        public List<String> getStrengths() {
            return strengths;
        }

        public void setStrengths(List<String> strengths) {
            this.strengths = strengths;
        }
    }
}

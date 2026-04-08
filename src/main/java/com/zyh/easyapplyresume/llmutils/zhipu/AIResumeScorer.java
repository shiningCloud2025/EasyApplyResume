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
 * AI简历评分器
 * 使用智谱AI(glm-4-air)对简历进行智能评分（0-100分）
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class AIResumeScorer {

    @Autowired
    private ZhiPuAiChatModel zhipuAiChatModel;

    @Autowired
    private ZhipuLlmUtilsLogUtil zhipuLlmUtilsLogUtil;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIScoreResult scoreResume(String resumeReactCode) {
        long startTime = System.currentTimeMillis();
        String toolClass = "AIResumeScorer";
        String toolDescription = "AI简历评分器-使用智谱AI(glm-4-air)对简历进行智能评分（0-100分）";

        String prompt = """
                  你是资深HR专家。请对以下简历进行专业评分（0-100分）。

                  简历内容：
                  %s

                  请从以下维度评分并返回JSON格式：
                  {
                    "totalScore": 85,
                    "dimensions": {
                      "completeness": 90,
                      "experience": 85,
                      "skills": 80,
                      "presentation": 85
                    },
                    "strengths": ["项目经验丰富", "技术栈全面"],
                    "weaknesses": ["缺少量化成果", "自我评价过长"],
                    "suggestions": ["建议增加项目成果数据", "精简自我评价"],
                    "level": "优秀"
                  }

                  评分标准：
                  - completeness（完整度）：基本信息、教育、经验、技能是否完整
                  - experience（经验质量）：工作/项目经验的深度和相关性
                  - skills（技能匹配）：技能描述的专业性和深度
                  - presentation（呈现质量）：排版、表达、逻辑性
                  - level：优秀(85+)、良好(70-84)、一般(55-69)、待改进(<55)

                  只返回JSON，不要其他内容。
                  """.formatted(resumeReactCode);

        String response = null;
        try {

            log.info("开始智谱AI评分简历");
            response = ChatClient.create(zhipuAiChatModel)
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            AIScoreResult result = parseScoreResult(response);

            zhipuLlmUtilsLogUtil.saveSuccessLog(
                    toolClass,
                    toolDescription,
                    prompt,
                    response,
                    startTime
            );

            return result;
        } catch (Exception e) {
            log.error("AI评分简历失败", e);
            zhipuLlmUtilsLogUtil.saveFailLog(
                    toolClass,
                    toolDescription,
                    prompt,
                    response,
                    startTime,
                    e.getMessage()
            );

            throw new RuntimeException("AI评分简历失败: " + e.getMessage());
        }
    }

    private AIScoreResult parseScoreResult(String jsonResponse) {
        try {
            String cleanJson = jsonResponse
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            Map<String, Object> data = objectMapper.readValue(cleanJson, new TypeReference<Map<String, Object>>() {});

            AIScoreResult result = new AIScoreResult();
            result.setTotalScore(((Number) data.get("totalScore")).doubleValue());
            
            Map<String, Object> dimensions = (Map<String, Object>) data.get("dimensions");
            result.setCompletenessScore(((Number) dimensions.get("completeness")).doubleValue());
            result.setExperienceScore(((Number) dimensions.get("experience")).doubleValue());
            result.setSkillsScore(((Number) dimensions.get("skills")).doubleValue());
            result.setPresentationScore(((Number) dimensions.get("presentation")).doubleValue());
            
            result.setStrengths((List<String>) data.getOrDefault("strengths", new ArrayList<>()));
            result.setWeaknesses((List<String>) data.getOrDefault("weaknesses", new ArrayList<>()));
            result.setSuggestions((List<String>) data.getOrDefault("suggestions", new ArrayList<>()));
            result.setLevel((String) data.getOrDefault("level", "一般"));

            return result;
        } catch (Exception e) {
            log.error("解析AI评分结果失败", e);
            throw new RuntimeException("解析AI评分结果失败");
        }
    }

    public static class AIScoreResult {
        private Double totalScore;
        private Double completenessScore;
        private Double experienceScore;
        private Double skillsScore;
        private Double presentationScore;
        private List<String> strengths;
        private List<String> weaknesses;
        private List<String> suggestions;
        private String level;

        public Double getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(Double totalScore) {
            this.totalScore = totalScore;
        }

        public Double getCompletenessScore() {
            return completenessScore;
        }

        public void setCompletenessScore(Double completenessScore) {
            this.completenessScore = completenessScore;
        }

        public Double getExperienceScore() {
            return experienceScore;
        }

        public void setExperienceScore(Double experienceScore) {
            this.experienceScore = experienceScore;
        }

        public Double getSkillsScore() {
            return skillsScore;
        }

        public void setSkillsScore(Double skillsScore) {
            this.skillsScore = skillsScore;
        }

        public Double getPresentationScore() {
            return presentationScore;
        }

        public void setPresentationScore(Double presentationScore) {
            this.presentationScore = presentationScore;
        }

        public List<String> getStrengths() {
            return strengths;
        }

        public void setStrengths(List<String> strengths) {
            this.strengths = strengths;
        }

        public List<String> getWeaknesses() {
            return weaknesses;
        }

        public void setWeaknesses(List<String> weaknesses) {
            this.weaknesses = weaknesses;
        }

        public List<String> getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(List<String> suggestions) {
            this.suggestions = suggestions;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }
}

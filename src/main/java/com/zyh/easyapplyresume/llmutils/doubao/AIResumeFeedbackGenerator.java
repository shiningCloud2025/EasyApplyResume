package com.zyh.easyapplyresume.llmutils.doubao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminLlmUtilsInfoForm;
import com.zyh.easyapplyresume.service.admin.AdminLlmUtilsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AI简历反馈生成器
 * 使用豆包大模型(Doubao-1.5-pro-32k)生成详细的简历修改建议
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class AIResumeFeedbackGenerator {

    @Autowired
    private OpenAiChatModel doubaoModel;

    @Autowired
    private AdminLlmUtilsInfoService adminLlmUtilsInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIFeedbackResult generateFeedback(String resumeReactCode) {
        try {
            String prompt = """
                    你是资深HR和职业规划专家。请对以下简历进行深度分析，提供详细的修改建议。
                    
                    简历内容：
                    %s
                    
                    请提供详细反馈并返回JSON格式：
                    {
                      "overallFeedback": "整体评价概述",
                      "detailedFeedback": [
                        {
                          "section": "个人信息",
                          "currentIssue": "邮箱不够专业",
                          "suggestion": "建议使用Gmail或企业邮箱，避免使用QQ邮箱",
                          "priority": "中"
                        },
                        {
                          "section": "项目经验",
                          "currentIssue": "缺少量化数据",
                          "suggestion": "建议添加具体数据，如'优化系统性能，响应时间从500ms降至150ms'",
                          "priority": "高"
                        }
                      ],
                      "improvementAreas": [
                        {
                          "area": "工作经验",
                          "improvements": [
                            "使用STAR法则描述项目经历（情境-任务-行动-结果）",
                            "增加技术难点和解决方案的描述",
                            "补充团队规模和个人职责占比"
                          ]
                        },
                        {
                          "area": "技能展示",
                          "improvements": [
                            "按熟练度分级（精通/熟练/了解）",
                            "添加实际应用场景说明",
                            "移除过时或不相关技术"
                          ]
                        }
                      ],
                      "strengths": [
                        "项目经验丰富，涵盖多个业务领域",
                        "技术栈完整，前后端均有涉及"
                      ],
                      "quickWins": [
                        "删除冗余的自我评价内容",
                        "统一日期格式为YYYY.MM",
                        "检查并修正所有错别字"
                      ]
                    }
                    
                    要求：
                    1. detailedFeedback: 具体问题和改进建议（5-10条）
                    2. priority: 高/中/低
                    3. improvementAreas: 按模块分组的改进建议
                    4. strengths: 简历的亮点和优势
                    5. quickWins: 可以快速改进的点（3-5个）
                    
                    只返回JSON，不要其他内容。
                    """.formatted(resumeReactCode);

            log.info("开始豆包AI生成简历反馈建议");
            String response = ChatClient.create(doubaoModel)
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();
            AdminLlmUtilsInfoForm adminLlmUtilsInfoForm = new AdminLlmUtilsInfoForm();
            adminLlmUtilsInfoForm.setLlmUtilsInfoToolClass("AIResumeFeedbackGenerator");
            adminLlmUtilsInfoForm.setLlmUtilsInfoToolDescription("AI简历反馈生成器-使用豆包大模型(Doubao-1.5-pro-32k)生成详细的简历修改建议");
            adminLlmUtilsInfoForm.setLlmUtilsInfoModelProvider("火山方舟");
            adminLlmUtilsInfoForm.setLlmUtilsInfoModelName("Doubao-1.5-pro-32k");
            adminLlmUtilsInfoForm.setLlmUtilsInfoInputContent(prompt.toString());
            adminLlmUtilsInfoForm.setLlmUtilsInfoOutputResult(response);


            return parseFeedbackResult(response);
        } catch (Exception e) {
            log.error("AI生成简历反馈失败", e);
            throw new RuntimeException("AI生成简历反馈失败: " + e.getMessage());
        }
    }

    private AIFeedbackResult parseFeedbackResult(String jsonResponse) {
        try {
            String cleanJson = jsonResponse
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            Map<String, Object> data = objectMapper.readValue(cleanJson, new TypeReference<Map<String, Object>>() {});

            AIFeedbackResult result = new AIFeedbackResult();
            result.setOverallFeedback((String) data.getOrDefault("overallFeedback", ""));
            
            List<Map<String, Object>> feedbackList = (List<Map<String, Object>>) data.getOrDefault("detailedFeedback", new ArrayList<>());
            List<DetailedFeedback> detailedFeedbacks = new ArrayList<>();
            for (Map<String, Object> item : feedbackList) {
                DetailedFeedback feedback = new DetailedFeedback();
                feedback.setSection((String) item.get("section"));
                feedback.setCurrentIssue((String) item.get("currentIssue"));
                feedback.setSuggestion((String) item.get("suggestion"));
                feedback.setPriority((String) item.get("priority"));
                detailedFeedbacks.add(feedback);
            }
            result.setDetailedFeedback(detailedFeedbacks);
            
            List<Map<String, Object>> improvementList = (List<Map<String, Object>>) data.getOrDefault("improvementAreas", new ArrayList<>());
            List<ImprovementArea> improvementAreas = new ArrayList<>();
            for (Map<String, Object> item : improvementList) {
                ImprovementArea area = new ImprovementArea();
                area.setArea((String) item.get("area"));
                area.setImprovements((List<String>) item.getOrDefault("improvements", new ArrayList<>()));
                improvementAreas.add(area);
            }
            result.setImprovementAreas(improvementAreas);
            
            result.setStrengths((List<String>) data.getOrDefault("strengths", new ArrayList<>()));
            result.setQuickWins((List<String>) data.getOrDefault("quickWins", new ArrayList<>()));

            return result;
        } catch (Exception e) {
            log.error("解析AI反馈结果失败", e);
            throw new RuntimeException("解析AI反馈结果失败");
        }
    }

    public static class AIFeedbackResult {
        private String overallFeedback;
        private List<DetailedFeedback> detailedFeedback;
        private List<ImprovementArea> improvementAreas;
        private List<String> strengths;
        private List<String> quickWins;

        public String getOverallFeedback() {
            return overallFeedback;
        }

        public void setOverallFeedback(String overallFeedback) {
            this.overallFeedback = overallFeedback;
        }

        public List<DetailedFeedback> getDetailedFeedback() {
            return detailedFeedback;
        }

        public void setDetailedFeedback(List<DetailedFeedback> detailedFeedback) {
            this.detailedFeedback = detailedFeedback;
        }

        public List<ImprovementArea> getImprovementAreas() {
            return improvementAreas;
        }

        public void setImprovementAreas(List<ImprovementArea> improvementAreas) {
            this.improvementAreas = improvementAreas;
        }

        public List<String> getStrengths() {
            return strengths;
        }

        public void setStrengths(List<String> strengths) {
            this.strengths = strengths;
        }

        public List<String> getQuickWins() {
            return quickWins;
        }

        public void setQuickWins(List<String> quickWins) {
            this.quickWins = quickWins;
        }
    }

    public static class DetailedFeedback {
        private String section;
        private String currentIssue;
        private String suggestion;
        private String priority;

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getCurrentIssue() {
            return currentIssue;
        }

        public void setCurrentIssue(String currentIssue) {
            this.currentIssue = currentIssue;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(String suggestion) {
            this.suggestion = suggestion;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }
    }

    public static class ImprovementArea {
        private String area;
        private List<String> improvements;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public List<String> getImprovements() {
            return improvements;
        }

        public void setImprovements(List<String> improvements) {
            this.improvements = improvements;
        }
    }
}

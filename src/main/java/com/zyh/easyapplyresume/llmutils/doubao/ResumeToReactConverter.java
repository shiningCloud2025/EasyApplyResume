package com.zyh.easyapplyresume.llmutils.doubao;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 简历转React代码转换器
 * 使用豆包大模型(Doubao-1.5-pro-32k)将PDF/Word简历转换为React组件代码
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class ResumeToReactConverter {

    @Autowired
    private OpenAiChatModel doubaoModel;

    @Autowired
    private DoubaoLlmUtilsLogUtil doubaoLlmUtilsLogUtil;

    public String convertResumeToReact(MultipartFile file) {
        try {
            String resumeText = extractTextFromFile(file);
            String reactCode = generateReactCodeByAI(resumeText);
            validateReactSyntax(reactCode);
            return reactCode;
        } catch (Exception e) {
            log.error("简历转换失败", e);
            throw new RuntimeException("简历转换失败: " + e.getMessage());
        }
    }

    private String extractTextFromFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if (filename == null) {
                throw new RuntimeException("文件名不能为空");
            }

            if (filename.toLowerCase().endsWith(".pdf")) {
                return extractFromPdf(file.getInputStream());
            } else if (filename.toLowerCase().endsWith(".docx") || filename.toLowerCase().endsWith(".doc")) {
                return extractFromWord(file.getInputStream());
            } else {
                throw new RuntimeException("仅支持PDF和Word格式简历");
            }
        } catch (Exception e) {
            log.error("文件解析失败", e);
            throw new RuntimeException("文件解析失败: " + e.getMessage());
        }
    }

    private String extractFromPdf(InputStream inputStream) throws Exception {
        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractFromWord(InputStream inputStream) throws Exception {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            StringBuilder text = new StringBuilder();
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                text.append(paragraph.getText()).append("\n");
            }
            return text.toString();
        }
    }

    private String generateReactCodeByAI(String resumeText) {
        long startTime = System.currentTimeMillis();
        String toolClass = "ResumeToReactConverter";
        String toolDescription = "简历转React代码转换器-使用豆包大模型(Doubao-1.5-pro-32k)将PDF/Word简历转换为React组件代码";

        String prompt = """
                  你是简历转React代码的专家。请根据以下简历内容，生成一个完整的、专业美观的React简历组件。

                  要求：
                  1. 自动识别简历中的：个人信息、教育经历、工作经验、项目经历、技能、自我评价等模块
                  2. 生成现代化、美观的简历布局（使用Tailwind CSS或内联样式）
                  3. 代码必须语法正确，可直接运行
                  4. 生成完整的React函数组件
                  5. 允许小误差，但不能有语法错误
                  6. 只返回完整的React组件代码，不要任何解释、markdown标记或注释

                  简历内容：
                  %s

                  请直接返回完整的React代码：
                  """.formatted(resumeText);

        String reactCode = null;
        try {
            log.info("开始豆包AI生成React简历代码");
             reactCode = ChatClient.create(doubaoModel)
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            String cleanReactCode = cleanReactCode(reactCode);

            doubaoLlmUtilsLogUtil.saveSuccessLog(
                    toolClass,
                    toolDescription,
                    prompt,
                    cleanReactCode,
                    startTime
            );
            return cleanReactCode;
        } catch (Exception e) {
            log.error("AI生成React代码失败", e);
            doubaoLlmUtilsLogUtil.saveFailLog(
                    toolClass,
                    toolDescription,
                    prompt,
                    reactCode,
                    startTime,
                    e.getMessage()
            );
            throw new RuntimeException("AI生成React代码失败: " + e.getMessage());
        }
    }

    private String cleanReactCode(String code) {
        code = code.replaceAll("```jsx", "")
                .replaceAll("```javascript", "")
                .replaceAll("```react", "")
                .replaceAll("```", "")
                .trim();
        return code;
    }

    private void validateReactSyntax(String reactCode) {
        if (!reactCode.contains("function") && !reactCode.contains("const") && !reactCode.contains("class")) {
            throw new RuntimeException("生成的代码不是有效的React组件");
        }
        if (!reactCode.contains("return")) {
            throw new RuntimeException("React组件缺少return语句");
        }

        int openBraces = reactCode.length() - reactCode.replace("{", "").length();
        int closeBraces = reactCode.length() - reactCode.replace("}", "").length();
        if (Math.abs(openBraces - closeBraces) > 3) {
            log.warn("React代码括号可能不匹配，开括号：{}，闭括号：{}", openBraces, closeBraces);
        }
    }
}

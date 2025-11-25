package com.zyh.easyapplyresume.demo.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        String fileName = "PDF生成工具调用.pdf";
        String content = "PDF生成工具调用";
        String result = pdfGenerationTool.generatePDF(fileName, content);
        System.out.println(result);
        assertNotNull(result);
    }
}
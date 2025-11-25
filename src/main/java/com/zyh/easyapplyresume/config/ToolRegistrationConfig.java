package com.zyh.easyapplyresume.config;

import com.zyh.easyapplyresume.demo.tools.*;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具注册工具类
 * @author shiningCloud2025
 */
@Configuration
public class ToolRegistrationConfig {
    @Autowired
    private WebSearchTool webSearchTool;
    @Bean
    public ToolCallback[] allTools(){
        FileOperationTool fileOperationTool = new FileOperationTool();
        WebScraptingTool webScraptingTool = new WebScraptingTool();
        ResourceDownloadTool  resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        return ToolCallbacks.from(
                fileOperationTool,
                webSearchTool,
                webScraptingTool,
                resourceDownloadTool,
                terminalOperationTool,
                pdfGenerationTool
        );
    }
}

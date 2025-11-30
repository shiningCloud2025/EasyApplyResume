package com.zyh.imagesearchmcp;

import com.zyh.imagesearchmcp.tools.ImageSearchTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ImageSearchMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageSearchMcpApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider imageSearchTools(ImageSearchTool imageSearchTool){
        return MethodToolCallbackProvider.builder()
                .toolObjects(imageSearchTool)
                .build();
    }

    @Bean
    public ToolCallbackProvider myTools(ImageSearchTool imageSearchTool){
        List<ToolCallback> tools = Arrays.stream(MethodToolCallbackProvider.builder()
                .toolObjects(imageSearchTools(imageSearchTool))
                .build()
                .getToolCallbacks())
                .toList();
        return ToolCallbackProvider.from( tools);


    }

}

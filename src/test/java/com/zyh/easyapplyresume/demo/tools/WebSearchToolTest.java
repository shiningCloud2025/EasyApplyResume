package com.zyh.easyapplyresume.demo.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebSearchToolTest {
    @Autowired
    private WebSearchTool webSearchTool;
    @Test
    void searchWeb() {
        String query = "字节跳动";
        String result = webSearchTool.searchWeb(query);
        assertNotNull(result);
    }
}
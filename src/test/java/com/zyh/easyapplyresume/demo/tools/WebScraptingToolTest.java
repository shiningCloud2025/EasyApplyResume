package com.zyh.easyapplyresume.demo.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebScraptingToolTest {

    @Test
    void scrapeWebPage() {
        WebScraptingTool webScraptingTool = new WebScraptingTool();
        String url = "https://www.baidu.com/";
        String result = webScraptingTool.scrapeWebPage(url);
        System.out.println(result);
        assertNotNull(result);
    }
}
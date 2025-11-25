package com.zyh.easyapplyresume.demo.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String url = "https://ts3.tc.mm.bing.net/th/id/OIP-C.RDWw9rLzK48PzguhUYP57AHaEo?rs=1&pid=ImgDetMain&o=7&rm=3";
        String fileName = "cat.jpg";
        String result = resourceDownloadTool.downloadResource(url, fileName);
        System.out.println(result);
        assertNotNull(result);
    }
}
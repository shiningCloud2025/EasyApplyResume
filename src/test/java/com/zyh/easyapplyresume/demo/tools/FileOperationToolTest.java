package com.zyh.easyapplyresume.demo.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "工具调用.txt";
        String result = fileOperationTool.readFile(fileName);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "工具调用.txt";
        String content = "工具调用测试";
        String result = fileOperationTool.writeFile(fileName,content);
        assertNotNull(result);
    }
}
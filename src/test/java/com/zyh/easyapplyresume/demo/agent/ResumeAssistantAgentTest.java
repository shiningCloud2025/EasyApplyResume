package com.zyh.easyapplyresume.demo.agent;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class ResumeAssistantAgentTest {

    @Resource
    private ResumeAssistantAgent resumeAssistantAgent;

    @Test
    @Timeout(value = 10, unit = TimeUnit.MINUTES)
    void testRun(){
        log.info("========== 开始测试 ResumeAssistantAgent ==========");
        
//        String userPrompt = """
//                我的另一半居住在上海静安区,请帮我找到 5 公里内合适的约会地点,
//                并结合一些网络图片,制定一份详细的约会计划,
//                并以 PDF 格式输出""";

        String userPrompt = """  
                我是一名大四学生，是学导弹工程的，请你帮我制作一份好的求职简历,  
                并以 PDF 格式输出""";
        try {
            String answer = resumeAssistantAgent.run(userPrompt);
            log.info("========== 执行结果 ==========");
            log.info(answer);
            log.info("========== 测试完成 ==========");
            
            Assertions.assertNotNull(answer, "Agent返回结果不能为null");
            Assertions.assertFalse(answer.isEmpty(), "Agent返回结果不能为空");
        } catch (Exception e) {
            log.error("测试执行失败", e);
            throw e;
        }
    }
}

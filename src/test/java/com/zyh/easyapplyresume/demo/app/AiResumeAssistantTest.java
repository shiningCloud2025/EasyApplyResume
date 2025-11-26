package com.zyh.easyapplyresume.demo.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiResumeAssistantTest {

    @Resource
    private AiResumeAssistant aiResumeAssistant;
    @Test
    void testChat(){
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是java工程师yunhan";
        String answer = aiResumeAssistant.doChat(message,chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想学会如何制作一个好的java简历";
        answer = aiResumeAssistant.doChat(message,chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "你好，我是谁来着，刚才跟你说过，请帮我回忆一下?";
        answer = aiResumeAssistant.doChat(message,chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是java工程师yunhan,我想要一份java简历，但是我不知道该怎么写,政治敏感";
        AiResumeAssistant.ResumeUpdateAdviceReport resumeUpdateAdviceReport = aiResumeAssistant.doChatWithReport(message,chatId);
        System.out.println(resumeUpdateAdviceReport.toString());
        Assertions.assertNotNull(resumeUpdateAdviceReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "问题1: 我没有任何全职工作经历，简历上应该写什么？";
        String answer = aiResumeAssistant.doChatWithRag(message,chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithRagCloudAdvisor() {
        String chatId = UUID.randomUUID().toString();
        String message = "工作经历很少，只有一段实习/短期工作，怎么写满一页？";
        String answer = aiResumeAssistant.doChatWithRag(message,chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithRagAndQueryRewrite() {
        String chatId = UUID.randomUUID().toString();
        String message = "工作经历很少，a/短期工作，怎么写满一页阿巴巴？";
        String answer = aiResumeAssistant.doChatWithRagAndQueryRewrite(message,chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTool() {
        // 测试联网搜索问题的答案
//        testMessage("我是一个大一新生，能不能给我推荐几个好看的简历模版,1个就可以");

        // 测试网页抓取:简历编写案例分析
//        testMessage("我是一个大一新生，最近在写简历，能不能看看超级简历网站，看看其他人的简历是怎样的,1个就可以?");

        // 测试咨询下载:图片下载
//        testMessage("直接下载一张适合做电脑壁纸的小猫图片为文件,1个就可以");

        // 测试终端操作:执行代码
//        testMessage("执行Python3脚本来做数据分析报告,1个就可以");

        // 测试文件操作:保存简历
//        testMessage("保存一个简历模版,1个就可以");

        // 测试pdf生成
//        testMessage("能不能帮我生成一个'简历编写'PDF,我希望这个PDF的内容是简历编写的注意事项");

        // 测试邮件发送
        testMessage("能不能给1812463057@qq.com发一个邮件，主题是字节跳动，内容是欢迎你加入");

    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = aiResumeAssistant.doChatWithTool(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        // 测试地图 MCP
        String message = "我的另一半居住在上海静安区，请帮我找到 5 公里内合适的约会地点";
        String answer = aiResumeAssistant.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }
}
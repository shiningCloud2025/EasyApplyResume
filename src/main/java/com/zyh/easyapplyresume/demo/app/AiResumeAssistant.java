package com.zyh.easyapplyresume.demo.app;

import com.zyh.easyapplyresume.demo.advisor.MyLoggerAdvisor;
import com.zyh.easyapplyresume.demo.advisor.ReReadingAdvisor;
import com.zyh.easyapplyresume.demo.advisor.SensitiveWordsAdvisor;
import com.zyh.easyapplyresume.demo.chatmemory.FileBasedChatMemory;
import com.zyh.easyapplyresume.demo.chatmemory.InMemoryDbHybridChatMemory;
import com.zyh.easyapplyresume.demo.chatmemory.MySQLBasedChatMemory;
import com.zyh.easyapplyresume.demo.rag.AiResumeAssistantRagCustomAdvisorFactory;
import com.zyh.easyapplyresume.demo.rag.MyTokenTextSplitter;
import com.zyh.easyapplyresume.demo.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * AI简历助手(C端)
 * @author shiningCloud2025
 */
@Component
@Slf4j
public class AiResumeAssistant {



    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "你是专业简历润色专家，熟悉HR筛选标准、职业咨询及留学申请审核。通过耐心交流了解用户背景与需求，助力打造专业、有竞争力的定制化简历。\n" +
            "\n" +
            "工作流程：\n" +
            "1. 引导补充信息：若用户提供的简历或背景不全，通过提问完善细节；\n" +
            "2. 评估与建议：从结构、内容、语言等方面分析，提供具体优化方案（如语言润色、重点突出、量化成果等）；\n" +
            "3. 定制化支持：根据求职（不同行业）、保研、留学等场景，提供适配建议及多版本选项；\n" +
            "4. 鼓励迭代：协助用户持续优化，建立长期职业品牌意识。\n" +
            "\n" +
            "原则：专业亲和，以引导为主；注重逻辑细节，主动追问不足信息。\n" +
            "\n" +
            "可询问关键信息（例如）：学历专业、目标岗位/学校、实习/项目经历、技能成果、格式偏好等。\n" +
            "重点解决：内容空洞、表达不专业、结构混乱、匹配度低等问题。";

    public static record ResumeUpdateAdviceReport(String title, List<String> suggestions){ }
    // CharClient配置
    public AiResumeAssistant(ChatModel dashscopeChatModel, MySQLBasedChatMemory mySQLBasedChatMemory,SensitiveWordsAdvisor sensitiveWordsAdvisor){

        // 初始化基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();
        // 初始化基于文件存储的对话记忆
//        String fileDir = System.getProperty("user.dir")+ "/chat-memory";;
//        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        // 初始化基于数据库存储的对话记忆
//        ChatMemory chatMemory = mySQLBasedChatMemory;
        // 初始化基于混合持久化(内存+数据库)的对话记忆
        ChatMemory chatMemory = new InMemoryDbHybridChatMemory(mySQLBasedChatMemory);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 自定义日志Advisor，可按需开启
                        new MyLoggerAdvisor(),
                        // 自定义Re2Advisor,可按需开启
//                        new ReReadingAdvisor(),
                        // 自定义敏感词过滤Advisor，可按需开启
                        sensitiveWordsAdvisor
                        // 根据用户输入进行过滤
//                        AiResumeAssistantRagCustomAdvisorFactory.creatAiResumeAssistantRagCustomAdvisor(aiResumeAssistantVectorStore,"应届生")
                )
                .build();
    }

    // 普通的大模型对话
    public String doChat(String message,String chatId){
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,20))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content:{}",content);
        return  content;

    }
    // 生成报告的大模型
    public ResumeUpdateAdviceReport doChatWithReport(String message,String chatId){
        ResumeUpdateAdviceReport resumeUpdateAdviceReport=chatClient
                .prompt()
                .system(SYSTEM_PROMPT+"每次对话后都要生成简历修改建议报告，标题为{用户名}的简历修改建议报告，内容为建议列表")
                .user(message)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,20))
                .call()
                .entity(ResumeUpdateAdviceReport.class);

        log.info("ResumeUpdateAdviceReport:{}",resumeUpdateAdviceReport);
        return  resumeUpdateAdviceReport;

    }

    @Resource
    private VectorStore aiResumeAssistantVectorStore;
    // SpringAI+本地知识库
    public String doChatWithRag(String message,String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user( message)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,10))
                // 开启日志检查器，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用知识库问答(原始版)
                .advisors(new QuestionAnswerAdvisor(aiResumeAssistantVectorStore))
//                .advisors( AiResumeAssistantRagCustomAdvisorFactory.creatAiResumeAssistantRagCustomAdvisor(aiResumeAssistantVectorStore,"应届生"))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",content);
        return  content;
    }

    @Resource
    private Advisor aiResumeAssistantRagCloudAdvisor;

    // SpringAI+云知识库
    public String doChatWithRagCloudAdvisor(String message,String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user( message)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,10))
                // 开启日志检查器，便于观察效果(不加也可以)
                .advisors(new MyLoggerAdvisor())
                // 应用增强检索服务(云知识库服务)
                .advisors(aiResumeAssistantRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",content);
        return  content;
    }

    // 使用了查询重写的RAG
    @Resource
    private QueryRewriter queryRewriter;
    public String doChatWithRagAndQueryRewrite(String message,String chatId){
        // 查询重写
        String rewriteenMessage = queryRewriter.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                .prompt()
                .user( rewriteenMessage)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        return  content;
    }

    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTool(String message,String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user( message)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,10))
                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",content);
        return  content;
    }

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    public String doChatWithMcp(String message,String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,10))
                .advisors(new MyLoggerAdvisor())
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",content);
        return  content;
    }

    /**
     * AI简历修改助手,面向C端用户
     * 使用技术:大模型调用+检索器+工具调用+MCP+RAG
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> AiResumeAssistantDoChatWithStream(String message,String chatId){
         return chatClient.prompt()
                .user(message)
                .system(SYSTEM_PROMPT)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,20))
                .advisors(
                        new SensitiveWordsAdvisor(),
                        new MyLoggerAdvisor(),
//                        new ReReadingAdvisor(),
                        aiResumeAssistantRagCloudAdvisor
                )
                .tools(allTools)
                .tools(toolCallbackProvider)
                .stream()
                .content();
    }



}

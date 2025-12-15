# 🔧 后端修复指南 - Agent 工具选择问题

## 🐛 问题现象

**用户提问：** "西游记是一本怎么样的书籍"

**期望行为：** 调用 `searchWeb` 搜索相关信息

**实际行为：** 调用 `writeFile` 写入文件 ❌

**返回结果：** `"File written successfully to: C:\Users\...\xiyouji_introduction.txt"`

## 🔍 问题分析

LLM（通义千问）选择了**错误的工具**。原因：

1. **提示词不够明确** - 没有告诉 LLM 应该优先使用哪些工具
2. **工具描述不清晰** - LLM 不知道何时该用哪个工具
3. **没有工具使用规则** - 缺少明确的工具选择指导

## ✅ 修复方案

### 步骤1：修改 SystemAssistantAgent 提示词

**文件：** `src/main/java/com/zyh/easyapplyresume/demo/agent/SystemAssistantAgent.java`

**修改前：**
```java
public SystemAssistantAgent(ToolCallback[] availableTools, ChatModel dashscopeChatModel){
    super(availableTools);
    this.setName("ResumeAssistantAgent");  // ❌ 名字错了
    
    String SYSTEM_PROMPT = """
            你是 ResumeAssistantAgent，一个全能的 AI 助手，旨在解决用户提出的任何任务。
            你拥有各种可供调用的工具，可以高效地完成复杂的请求。
            """;
    this.setSystemPrompt(SYSTEM_PROMPT);
    
    String NEXT_STEP_PROMPT = """
            根据用户需求，主动选择最合适的工具或工具组合。
            对于复杂任务，你可以分解问题并逐步使用不同的工具来解决。
            使用每个工具后，清楚地解释执行结果并建议下一步操作。
            如果你想在任何时候停止交互，请使用 `terminate` 工具/函数调用。
            """;
    this.setNextStepPrompt(NEXT_STEP_PROMPT);
    // ...
}
```

**修改后：**
```java
public SystemAssistantAgent(ToolCallback[] availableTools, ChatModel dashscopeChatModel){
    super(availableTools);
    this.setName("SystemAssistantAgent");  // ✅ 修正名字
    
    String SYSTEM_PROMPT = """
            你是 SystemAssistantAgent，一个智能系统管理助手。
            你的任务是帮助用户解答各种问题，主要通过搜索互联网来获取信息。
            
            # 工具使用规则
            
            1. **信息查询类问题**（如"XXX是什么"、"XXX怎么样"、"XXX的作者是谁"等）
               → 必须使用 searchWeb 工具搜索相关信息
               
            2. **需要详细内容时**
               → 先用 searchWeb 找到相关链接
               → 再用 scrapeWebPage 获取完整内容
               
            3. **文件操作**（只有当用户明确要求"保存"、"写入文件"、"生成文件"时）
               → 才使用 writeFile 工具
               
            4. **任务完成时**
               → 使用 doTerminate 工具结束对话
            
            # 重要提示
            - 不要直接回答问题，必须通过工具获取信息
            - 默认情况下，信息类问题都应该使用 searchWeb
            - 不要随意创建文件，除非用户明确要求
            """;
    this.setSystemPrompt(SYSTEM_PROMPT);
    
    String NEXT_STEP_PROMPT = """
            # 分析用户问题，选择正确的工具
            
            ## 判断流程：
            
            1. 如果是查询信息的问题（如"XX是什么"、"XX怎么样"）
               → 调用 searchWeb("用户的问题")
            
            2. 如果用户明确要求"保存"、"写入"、"生成文件"
               → 调用 writeFile(filePath, content)
            
            3. 如果已经获取到足够的信息
               → 调用 doTerminate("任务结束")
            
            ## 当前应该做什么？
            请根据用户的问题，选择最合适的工具并调用。
            """;
    this.setNextStepPrompt(NEXT_STEP_PROMPT);
    
    this.setMaxSteps(10);
    
    ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
            .defaultAdvisors(new MyLoggerAdvisor())
            .build();
    this.setChatClient(chatClient);
}
```

### 步骤2：检查工具描述

**确保 searchWeb 工具有清晰的描述：**

找到 `searchWeb` 工具的定义（应该在某个 `@Tool` 或 `@Bean` 注解的方法中），确保描述如下：

```java
@Tool(description = """
    在互联网上搜索信息。
    适用场景：
    - 用户询问"XX是什么"、"XX怎么样"等信息查询问题
    - 需要了解最新资讯
    - 查找人物、事件、概念等信息
    
    参数：
    - query: 搜索关键词或问题
    
    示例：
    - searchWeb("西游记是一本怎么样的书")
    - searchWeb("吴承恩")
    """)
public String searchWeb(String query) {
    // 实现...
}
```

**确保 writeFile 工具的描述限制使用场景：**

```java
@Tool(description = """
    将内容写入文件。
    ⚠️ 注意：只有当用户明确要求"保存"、"写入文件"、"生成文件"时才使用此工具。
    
    不适用场景：
    - 回答信息查询问题（应该使用 searchWeb）
    - 用户只是询问信息，没有要求保存
    
    适用场景：
    - 用户说"把这些信息保存到文件"
    - 用户说"生成一个XX文件"
    - 用户明确要求创建文件
    
    参数：
    - filePath: 文件保存路径
    - content: 要写入的内容
    """)
public String writeFile(String filePath, String content) {
    // 实现...
}
```

### 步骤3：添加工具选择日志

**在 ToolCallAgent.java 的 think() 方法中添加详细日志：**

```java
@Override
public boolean think() {
    // ... 现有代码
    
    ChatResponse chatResponse = getChatClient().prompt(prompt)
            .system(getSystemPrompt())
            .tools(availableTools)
            .call()
            .chatResponse();
    
    AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
    String result = assistantMessage.getText();
    List<AssistantMessage.ToolCall> toolCallsList = assistantMessage.getToolCalls();
    
    // ✅ 添加详细日志
    log.info("========== Agent 思考分析 ==========");
    log.info("Agent 名称: " + getName());
    log.info("LLM 文本回复: " + result);
    log.info("工具调用数量: " + toolCallsList.size());
    
    if (!toolCallsList.isEmpty()) {
        log.info("选择的工具：");
        for (AssistantMessage.ToolCall toolCall : toolCallsList) {
            log.info("  - 工具名: " + toolCall.name());
            log.info("    参数: " + toolCall.arguments());
        }
    }
    
    log.info("可用工具列表: " + Arrays.stream(availableTools)
            .map(tool -> tool.getName())
            .collect(Collectors.joining(", ")));
    log.info("=====================================");
    
    // ... 其余代码
}
```

## 🧪 测试步骤

### 1. 重启后端

```bash
# 如果用 IDEA，点击 Restart
# 或者命令行
mvn clean package
java -jar target/your-app.jar
```

### 2. 测试查询问题

**测试用例1：**
```
用户: "西游记是一本怎么样的书籍"
期望: 调用 searchWeb
实际: 查看日志，确认调用了 searchWeb
```

**测试用例2：**
```
用户: "把西游记的介绍保存到文件"
期望: 先 searchWeb，再 writeFile
实际: 查看日志，确认工具调用顺序
```

**测试用例3：**
```
用户: "你好"
期望: 直接回复或调用 doTerminate
实际: 不应该调用 writeFile
```

### 3. 查看日志输出

启动后端后，发送测试问题，查看控制台输出：

```
========== Agent 思考分析 ==========
Agent 名称: SystemAssistantAgent
LLM 文本回复: 我需要搜索关于西游记的信息
工具调用数量: 1
选择的工具：
  - 工具名: searchWeb
    参数: {"query":"西游记是一本怎么样的书籍"}
可用工具列表: searchWeb, scrapeWebPage, writeFile, doTerminate
=====================================
```

## 📋 修改清单

- [ ] 修改 `SystemAssistantAgent.java` 第 18 行：改名字
- [ ] 修改 `SystemAssistantAgent.java` 第 19-22 行：新的系统提示词
- [ ] 修改 `SystemAssistantAgent.java` 第 24-29 行：新的步骤提示词
- [ ] 检查 `searchWeb` 工具的 `@Tool` 描述
- [ ] 检查 `writeFile` 工具的 `@Tool` 描述，添加使用限制
- [ ] 在 `ToolCallAgent.java` 的 `think()` 方法中添加详细日志
- [ ] 重启后端
- [ ] 运行测试用例
- [ ] 查看日志确认工具选择正确

## 🎯 预期效果

**修改前：**
```
用户: "西游记是一本怎么样的书"
LLM: 调用 writeFile ❌
结果: File written successfully to: ...
```

**修改后：**
```
用户: "西游记是一本怎么样的书"
LLM: 调用 searchWeb ✅
结果: 《西游记》是明代小说家吴承恩创作的...
```

## 💡 补充说明

### 为什么 LLM 会选错工具？

1. **提示词太模糊** - "全能助手"让 LLM 认为可以做任何事
2. **没有优先级** - 所有工具看起来都一样重要
3. **工具描述不当** - writeFile 的描述没有限制使用场景

### 如何预防类似问题？

1. **明确的系统提示** - 告诉 LLM 它的主要任务是什么
2. **工具使用规则** - 列出何时该用哪个工具
3. **限制性描述** - 在工具描述中明确"不适用场景"
4. **测试覆盖** - 对每个工具都准备测试用例

---

**修改完成后记得告诉我，我帮你验证效果！** 👨‍💻



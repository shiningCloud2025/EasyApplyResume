# Agent Chat 接口修复指南

## 🎯 问题现象

调用 `/admin/aiSystemManagerAssistant/agent/chat` 接口时报错：
```
org.springframework.web.HttpMediaTypeNotAcceptableException: No acceptable representation
```

## 🔍 问题根源

**当前代码（有问题）：**
```java
@Operation(summary = "AI系统管理助手Agent对话")
@PostMapping(value = "/agent/chat")  // ❌ 缺少 produces 声明
public BaseResult<SseEmitter> agentChat(@RequestBody String message, 
                                        @RequestParam(required = false, value = "chatId") String chatId) {
    chatId = UUID.randomUUID().toString();
    SystemAssistantAgent systemAssistantAgent = new SystemAssistantAgent(allTools, dashscopeChatModel);
    return BaseResult.ok(systemAssistantAgent.runStream(message, chatId));  // ❌ SseEmitter 被包装
}
```

**问题分析：**
1. ❌ 没有声明 `produces = MediaType.TEXT_EVENT_STREAM_VALUE`
2. ❌ `SseEmitter` 被 `BaseResult<>` 包装
3. ❌ Spring Boot 尝试将 `SseEmitter` 序列化为 JSON → 失败
4. ❌ 抛出 `HttpMediaTypeNotAcceptableException`

## ✅ 解决方案

### 步骤1：修改后端控制器

**文件位置：** `src/main/java/com/zyh/easyapplyresume/controller/admin/AISystemManagerAssistantController.java`

**修改前：**
```java
@Operation(summary = "AI系统管理助手Agent对话")
@PostMapping(value = "/agent/chat")
public BaseResult<SseEmitter> agentChat(@RequestBody String message, 
                                        @RequestParam(required = false,value = "chatId") String chatId){
    chatId = UUID.randomUUID().toString();
    SystemAssistantAgent systemAssistantAgent = new SystemAssistantAgent(allTools,dashscopeChatModel);
    return BaseResult.ok(systemAssistantAgent.runStream(message,chatId));
}
```

**修改后：**
```java
@Operation(summary = "AI系统管理助手Agent对话")
@PostMapping(value = "/agent/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  // ⭐ 添加 produces
public SseEmitter agentChat(@RequestBody String message, 
                            @RequestParam(required = false,value = "chatId") String chatId){
    chatId = UUID.randomUUID().toString();
    SystemAssistantAgent systemAssistantAgent = new SystemAssistantAgent(allTools,dashscopeChatModel);
    return systemAssistantAgent.runStream(message,chatId);  // ⭐ 直接返回，不用 BaseResult 包装
}
```

**关键修改点：**
1. ✅ 添加 `produces = MediaType.TEXT_EVENT_STREAM_VALUE`
2. ✅ 返回类型从 `BaseResult<SseEmitter>` 改为 `SseEmitter`
3. ✅ 直接返回 `systemAssistantAgent.runStream(...)`，去掉 `BaseResult.ok()`

### 步骤2：重启后端服务

修改代码后，重启 Spring Boot 应用。

### 步骤3：前端代码已调整

前端代码我已经帮你修改好了，主要改动：

**文件：** `app/admin/src/views/admin/ai/AIAgent.vue`
- 改为调用 `aiApi.aiSystemManagerAgentChat()` 接口

**文件：** `app/admin/src/api/admin.ts`
- 更新了接口注释和请求头配置

## 🧪 测试步骤

### 1. 修改后端代码
按照上面的说明修改 `AISystemManagerAssistantController.java`

### 2. 重启后端
```bash
# 在项目根目录
mvn clean package
java -jar target/your-app.jar
```

### 3. 测试接口

**使用 curl 测试：**
```bash
curl -N -X POST "http://localhost:8080/admin/aiSystemManagerAssistant/agent/chat" \
  -H "Content-Type: text/plain" \
  -H "Admin-Authorization: Admin YOUR_TOKEN" \
  -H "Accept: text/event-stream" \
  -d "你好"
```

**预期响应：**
```
步骤 1: ...
步骤 2: ...
执行结束: 达到最大步骤 (10)
```

### 4. 测试前端

1. 打开浏览器，访问管理端
2. 进入 "AI助手" > "AI智能体助手" 页面
3. 输入消息并发送
4. 观察是否能正常接收流式响应

## 📊 修改对比

| 项目 | 修改前 | 修改后 |
|------|--------|--------|
| `produces` | 无 | `MediaType.TEXT_EVENT_STREAM_VALUE` |
| 返回类型 | `BaseResult<SseEmitter>` | `SseEmitter` |
| 返回语句 | `BaseResult.ok(emitter)` | `emitter` |
| 前端请求头 | `Accept: */*` | `Accept: text/event-stream` |

## 🔧 完整的修改后代码

```java
package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.demo.agent.SystemAssistantAgent;
import com.zyh.easyapplyresume.demo.app.AiSystemManagerAssistant;
import com.zyh.easyapplyresume.result.BaseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
@RequestMapping("/admin/aiSystemManagerAssistant")
@Tag(name="AI系统管理助手控制器-管理端")
public class AISystemManagerAssistantController {
    @Resource
    private AiSystemManagerAssistant aiSystemManagerAssistant;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    @Operation(summary = "AI系统管理助手应用对话")
    @PostMapping(value= "/application/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResult<Flux<String>> applicationChat(@RequestBody String message,
                                                   @RequestParam(required = false,value = "chatId") String chatId){
        chatId = UUID.randomUUID().toString();
        return BaseResult.ok(aiSystemManagerAssistant.AiSystemManagerAssistantDoChatWithStream(message,chatId));
    }

    @Operation(summary = "AI系统管理助手Agent对话")
    @PostMapping(value = "/agent/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  // ⭐ 修改点1
    public SseEmitter agentChat(@RequestBody String message,                            // ⭐ 修改点2
                                @RequestParam(required = false,value = "chatId") String chatId){
        chatId = UUID.randomUUID().toString();
        SystemAssistantAgent systemAssistantAgent = new SystemAssistantAgent(allTools,dashscopeChatModel);
        return systemAssistantAgent.runStream(message,chatId);                          // ⭐ 修改点3
    }
}
```

## ❓ 常见问题

### Q1: 为什么不能用 BaseResult 包装 SseEmitter？
**A:** `SseEmitter` 是一个服务器推送对象，不是数据对象，无法被 JSON 序列化。Spring MVC 需要直接处理 `SseEmitter` 来管理流式连接。

### Q2: application/chat 为什么可以用 BaseResult？
**A:** 因为它返回的是 `Flux<String>`，这是响应式流。Spring WebFlux 知道如何将 `Flux` 序列化为 SSE 流，即使被 `BaseResult` 包装也没问题。

### Q3: 如果想保持 BaseResult 怎么办？
**A:** 需要将 `SseEmitter` 改为 `Flux<String>`：
```java
@PostMapping(value = "/agent/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public BaseResult<Flux<String>> agentChat(...) {
    // 需要修改 BaseAgent.runStream() 方法，让它返回 Flux<String>
    return BaseResult.ok(systemAssistantAgent.runStreamAsFlux(message,chatId));
}
```
但这需要大改 `BaseAgent` 类，不推荐。

### Q4: 修改后会影响其他接口吗？
**A:** 不会。这是独立的接口修改，不影响其他任何接口。

## ✅ 验证清单

- [ ] 修改了 `AISystemManagerAssistantController.java`
- [ ] 添加了 `produces = MediaType.TEXT_EVENT_STREAM_VALUE`
- [ ] 返回类型改为 `SseEmitter`（去掉 `BaseResult`）
- [ ] 返回语句去掉了 `BaseResult.ok()`
- [ ] 重启了后端服务
- [ ] 测试接口正常返回 SSE 流
- [ ] 前端可以正常接收消息

## 🎉 完成

完成上述修改后，`/admin/aiSystemManagerAssistant/agent/chat` 接口就可以正常工作了！

---

**作者：** AI Assistant  
**更新时间：** 2025-12-15


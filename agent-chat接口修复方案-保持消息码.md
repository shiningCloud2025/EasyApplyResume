# Agent Chat 接口修复方案（保持消息码）

## 🎯 问题

如果去掉 `BaseResult` 包装，如何保持统一的响应格式（code、message）？

## 💡 三种解决方案对比

| 方案 | 复杂度 | 统一性 | 推荐度 |
|------|--------|--------|--------|
| 方案1：响应头传递状态 | ⭐ 简单 | ⭐⭐ 中等 | ⭐⭐⭐⭐⭐ 推荐 |
| 方案2：SSE流中发送状态 | ⭐⭐ 中等 | ⭐⭐⭐ 较好 | ⭐⭐⭐⭐ |
| 方案3：修改BaseAgent | ⭐⭐⭐ 复杂 | ⭐⭐⭐⭐ 最好 | ⭐⭐⭐ |

---

## 方案1：使用响应头（推荐 ✅✅✅）

### 优点
- ✅ 修改最少，只改控制器
- ✅ 保持了状态码和消息
- ✅ 不影响 SSE 数据流
- ✅ 符合 HTTP 标准

### 后端代码

**文件：** `src/main/java/com/zyh/easyapplyresume/controller/admin/AISystemManagerAssistantController.java`

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
import org.springframework.http.ResponseEntity;
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
    @PostMapping(value = "/agent/chat")
    public ResponseEntity<SseEmitter> agentChat(@RequestBody String message,
                                                @RequestParam(required = false, value = "chatId") String chatId) {
        try {
            // 生成或使用提供的 chatId
            if (chatId == null || chatId.isEmpty()) {
                chatId = UUID.randomUUID().toString();
            }
            
            // 创建 Agent 并获取流
            SystemAssistantAgent systemAssistantAgent = new SystemAssistantAgent(allTools, dashscopeChatModel);
            SseEmitter emitter = systemAssistantAgent.runStream(message, chatId);
            
            // ⭐ 使用 ResponseEntity，通过响应头传递状态信息
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .header("X-Response-Code", "200")           // 响应码
                    .header("X-Response-Message", "Success")    // 响应消息
                    .header("X-Chat-Id", chatId)                // 会话ID
                    .header("Access-Control-Expose-Headers", "X-Response-Code,X-Response-Message,X-Chat-Id")  // 允许前端读取
                    .body(emitter);
                    
        } catch (Exception e) {
            // ⭐ 错误处理：返回错误状态
            SseEmitter errorEmitter = new SseEmitter();
            try {
                errorEmitter.send(SseEmitter.event()
                    .name("error")
                    .data("{\"code\":500,\"message\":\"" + e.getMessage() + "\"}"));
                errorEmitter.complete();
            } catch (Exception ex) {
                errorEmitter.completeWithError(ex);
            }
            
            return ResponseEntity.status(500)
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .header("X-Response-Code", "500")
                    .header("X-Response-Message", e.getMessage())
                    .header("Access-Control-Expose-Headers", "X-Response-Code,X-Response-Message")
                    .body(errorEmitter);
        }
    }
}
```

### 前端代码（已修改）

前端在 `AIAgent.vue` 中读取响应头：

```typescript
const response = await aiApi.aiSystemManagerAgentChat(messageText, currentChatId || undefined)

if (!response.ok) {
  throw new Error(`HTTP error! status: ${response.status}`)
}

// ⭐ 读取响应头中的状态信息
const responseCode = response.headers.get('X-Response-Code')
const responseMessage = response.headers.get('X-Response-Message')
const responseChatId = response.headers.get('X-Chat-Id')

console.log('📋 响应码:', responseCode)
console.log('📋 响应消息:', responseMessage)
console.log('📋 Chat ID:', responseChatId)

if (responseCode !== '200') {
  ElMessage.error(responseMessage || '请求失败')
  return
}

// 保存 chatId 用于后续对话
if (responseChatId) {
  currentChatId = responseChatId
}

// 继续处理 SSE 流...
```

---

## 方案2：SSE流中发送状态（备选）

### 优点
- ✅ 状态信息在数据流中，更直观
- ✅ 可以发送更丰富的状态信息
- ✅ 不依赖 HTTP 响应头

### 缺点
- ⚠️ 前端需要特殊处理状态消息
- ⚠️ 混合了控制信息和数据

### 后端代码

**文件：** `src/main/java/com/zyh/easyapplyresume/controller/admin/AISystemManagerAssistantController.java`

```java
@Operation(summary = "AI系统管理助手Agent对话")
@PostMapping(value = "/agent/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter agentChat(@RequestBody String message,
                            @RequestParam(required = false, value = "chatId") String chatId) {
    if (chatId == null || chatId.isEmpty()) {
        chatId = UUID.randomUUID().toString();
    }
    
    SystemAssistantAgent systemAssistantAgent = new SystemAssistantAgent(allTools, dashscopeChatModel);
    SseEmitter emitter = systemAssistantAgent.runStream(message, chatId);
    
    // ⭐ 在流开始时发送状态信息
    try {
        Map<String, Object> status = new HashMap<>();
        status.put("type", "status");
        status.put("code", 200);
        status.put("message", "连接成功");
        status.put("chatId", chatId);
        
        emitter.send(SseEmitter.event()
            .name("status")
            .data(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(status)));
    } catch (Exception e) {
        emitter.completeWithError(e);
    }
    
    return emitter;
}
```

### 前端处理

前端需要识别并处理状态消息（已在 `AIAgent.vue` 中实现）：

```typescript
// 解析JSON数据
const parsed = JSON.parse(data)

// ⭐ 检查是否是状态消息
if (parsed.type === 'status') {
  console.log('📋 收到状态消息:', parsed)
  if (parsed.code !== 200) {
    ElMessage.error(parsed.message || '请求失败')
    return
  }
  if (parsed.chatId) {
    currentChatId = parsed.chatId
  }
  continue  // 不显示状态消息
}

// 处理正常内容...
```

---

## 方案3：修改BaseAgent（最完整，但复杂）

### 优点
- ✅ 最完整的解决方案
- ✅ 统一所有 Agent 的响应格式
- ✅ 结构化的消息类型

### 缺点
- ⚠️ 需要修改核心类
- ⚠️ 影响范围大
- ⚠️ 需要引入 Jackson 依赖

### 后端修改

**文件：** `src/main/java/com/zyh/easyapplyresume/demo/agent/BaseAgent.java`

在文件顶部添加导入：
```java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
```

修改 `runStream` 方法：

```java
public SseEmitter runStream(String userPrompt, String chatId){
    SseEmitter emitter = new SseEmitter(300000L);
    ObjectMapper objectMapper = new ObjectMapper();

    CompletableFuture.runAsync(()->{
        try{
            // ⭐ 步骤1：发送初始状态
            sendStatusMessage(emitter, objectMapper, 200, "连接成功", chatId);
            
            // 验证输入
            if (this.state != AgentState.IDLE) {
                sendErrorMessage(emitter, objectMapper, 400, "无法从状态运行代理: " + this.state);
                return;
            }
            if (StringUtil.isEmpty(userPrompt)) {
                sendErrorMessage(emitter, objectMapper, 400, "不能使用空提示词运行代理");
                return;
            }

            state = AgentState.RUNNING;
            messageList.add(new UserMessage(userPrompt));

            try {
                for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                    int stepNumber = i + 1;
                    currentStep = stepNumber;
                    log.info("正在执行步骤 " + stepNumber + "/" + maxSteps);

                    String stepResult = step();
                    
                    // ⭐ 发送结构化内容消息
                    sendContentMessage(emitter, objectMapper, stepNumber, stepResult);
                }
                
                if (currentStep >= maxSteps) {
                    state = AgentState.FINISHED;
                    sendFinishMessage(emitter, objectMapper, "执行结束: 达到最大步骤 (" + maxSteps + ")");
                }
                
                emitter.complete();
            } catch (Exception e) {
                state = AgentState.ERROR;
                sendErrorMessage(emitter, objectMapper, 500, "执行错误: " + e.getMessage());
            }
        } catch (Exception e) {
            sendErrorMessage(emitter, objectMapper, 500, "系统错误: " + e.getMessage());
        }
    });

    // 设置回调...
    return emitter;
}

// ⭐ 辅助方法：发送状态消息
private void sendStatusMessage(SseEmitter emitter, ObjectMapper mapper, int code, String message, String chatId) {
    try {
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "status");
        msg.put("code", code);
        msg.put("message", message);
        msg.put("chatId", chatId);
        
        emitter.send(SseEmitter.event()
            .name("status")
            .data(mapper.writeValueAsString(msg)));
    } catch (Exception e) {
        log.error("发送状态消息失败", e);
    }
}

// ⭐ 辅助方法：发送内容消息
private void sendContentMessage(SseEmitter emitter, ObjectMapper mapper, int step, String content) {
    try {
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "content");
        msg.put("step", step);
        msg.put("content", content);
        
        emitter.send(SseEmitter.event()
            .name("message")
            .data(mapper.writeValueAsString(msg)));
    } catch (Exception e) {
        log.error("发送内容消息失败", e);
    }
}

// ⭐ 辅助方法：发送错误消息
private void sendErrorMessage(SseEmitter emitter, ObjectMapper mapper, int code, String message) {
    try {
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "error");
        msg.put("code", code);
        msg.put("message", message);
        
        emitter.send(SseEmitter.event()
            .name("error")
            .data(mapper.writeValueAsString(msg)));
        emitter.complete();
    } catch (Exception e) {
        emitter.completeWithError(e);
    }
}

// ⭐ 辅助方法：发送完成消息
private void sendFinishMessage(SseEmitter emitter, ObjectMapper mapper, String message) {
    try {
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "finish");
        msg.put("code", 200);
        msg.put("message", message);
        
        emitter.send(SseEmitter.event()
            .name("finish")
            .data(mapper.writeValueAsString(msg)));
    } catch (Exception e) {
        log.error("发送完成消息失败", e);
    }
}
```

### 控制器（简单了）

```java
@Operation(summary = "AI系统管理助手Agent对话")
@PostMapping(value = "/agent/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter agentChat(@RequestBody String message,
                            @RequestParam(required = false, value = "chatId") String chatId) {
    if (chatId == null || chatId.isEmpty()) {
        chatId = UUID.randomUUID().toString();
    }
    
    SystemAssistantAgent systemAssistantAgent = new SystemAssistantAgent(allTools, dashscopeChatModel);
    return systemAssistantAgent.runStream(message, chatId);  // 直接返回，所有逻辑在 BaseAgent 中
}
```

---

## 📊 方案选择建议

### 快速修复（推荐）
**使用方案1：响应头传递状态**
- 修改最少，风险最低
- 符合 HTTP 标准
- 前端代码已准备好

### 完整改造
**使用方案3：修改BaseAgent**
- 一次性解决所有 Agent 的问题
- 统一响应格式
- 需要更多测试

### 折中方案
**使用方案2：SSE流中发送状态**
- 修改适中
- 灵活性好
- 适合快速迭代

---

## ✅ 推荐步骤

### 第一步：使用方案1（立即可用）

1. 修改控制器，使用 `ResponseEntity` + 响应头
2. 测试功能
3. 前端已经准备好，无需修改

### 第二步：如果需要更完整的方案

1. 再考虑方案3，修改 `BaseAgent`
2. 统一所有 Agent 的响应格式
3. 做好回归测试

---

## 🎉 总结

**推荐方案1**，因为：
- ✅ 最简单，只改控制器
- ✅ 保持了 code、message
- ✅ 前端已实现
- ✅ 风险最低

**完整代码在上面，直接复制使用！**


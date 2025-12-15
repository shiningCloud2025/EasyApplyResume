<template>
  <div class="ai-chat">
    <div class="chat-container">
      <!-- 聊天头部 -->
        <div class="chat-header">
          <div class="header-info">
          <div class="avatar">
            <el-icon :size="28"><ChatDotRound /></el-icon>
            </div>
          <div class="info-text">
            <h2>AI智能问答助手</h2>
            <p>专业的智能对话助手，为您提供准确的答案</p>
            </div>
          </div>
        <el-button @click="clearMessages" type="danger" plain>
          <el-icon><Delete /></el-icon>
          清空对话
              </el-button>
        </div>

      <!-- 聊天消息区域 -->
        <div class="chat-messages" ref="messagesContainer">
          <div 
            v-for="message in messages" 
            :key="message.id"
            class="message-item"
          :class="{ 'user-message': message.role === 'user', 'assistant-message': message.role === 'assistant' }"
          >
            <div class="message-avatar">
            <el-icon v-if="message.role === 'user'"><User /></el-icon>
            <el-icon v-else><ChatDotRound /></el-icon>
            </div>
            <div class="message-content">
            <div class="message-text" v-html="formatMessage(message.content)"></div>
            <div class="message-time">{{ formatTime(message.timestamp) }}</div>
            </div>
          </div>
          
        <!-- 正在输入指示器 -->
        <div v-if="isStreaming" class="message-item assistant-message">
            <div class="message-avatar">
            <el-icon><ChatDotRound /></el-icon>
            </div>
            <div class="message-content">
            <div class="message-text" v-html="formatMessage(streamingContent)"></div>
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>

      <!-- 输入区域 -->
        <div class="chat-input">
            <el-input
              v-model="inputMessage"
              type="textarea"
          :rows="3"
          placeholder="请输入您的问题..."
          @keydown.enter.prevent="handleEnter"
          :disabled="isStreaming"
              resize="none"
            />
            <div class="input-actions">
          <div class="input-tips">
            <el-icon><InfoFilled /></el-icon>
            <span>按 Enter 发送，Shift+Enter 换行</span>
          </div>
              <el-button 
                type="primary" 
                @click="sendMessage"
            :loading="isStreaming"
                :disabled="!inputMessage.trim()"
              >
            <el-icon><Promotion /></el-icon>
                发送
              </el-button>
            </div>
          </div>
        </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, User, Delete, InfoFilled, Promotion } from '@element-plus/icons-vue'

interface Message {
  id: number
  role: 'user' | 'assistant'
  content: string
  timestamp: Date
}

const messagesContainer = ref<HTMLElement>()
const inputMessage = ref('')
const isStreaming = ref(false)
const streamingContent = ref('')
const messages = ref<Message[]>([
  {
    id: Date.now(),
    role: 'assistant',
    content: '您好！我是AI智能问答助手，我可以为您解答各种问题。请问有什么可以帮助您的？',
    timestamp: new Date()
  }
])

let currentChatId = ref<string>('')

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 处理Enter键
const handleEnter = (event: KeyboardEvent) => {
  if (!event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim() || isStreaming.value) return

  const userMessage: Message = {
    id: Date.now(),
    role: 'user',
    content: inputMessage.value.trim(),
    timestamp: new Date()
  }
  
  messages.value.push(userMessage)
  const messageText = inputMessage.value.trim()
  inputMessage.value = ''
  scrollToBottom()

  // 开始流式接收
  isStreaming.value = true
  streamingContent.value = ''
  
  try {
    console.log('📤 [AI Chat] 发送消息:', messageText)
    console.log('📤 [AI Chat] 当前chatId:', currentChatId.value)
    
    // 构建请求URL
    const baseURL = import.meta.env.VITE_API_BASE_URL || '/api'
    const token = localStorage.getItem('admin_token')
    let url = `${baseURL}/admin/aiSystemManagerAssistant/application/chat`
    
    if (currentChatId.value) {
      url += `?chatId=${encodeURIComponent(currentChatId.value)}`
    }
    
    console.log('📤 [AI Chat] 请求URL:', url)
    
    // 使用fetch API处理SSE流式响应
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'text/plain',
        'Admin-Authorization': `Admin ${token}`,
        'Accept': 'text/event-stream'
      },
      body: messageText
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    console.log('📥 [AI Chat] 开始接收SSE流...')
    
    // 读取响应流
    const reader = response.body?.getReader()
    const decoder = new TextDecoder()
    
    if (!reader) {
      throw new Error('无法获取响应流')
    }

    let buffer = ''

    while (true) {
      const { done, value } = await reader.read()
      
      if (done) {
        console.log('✅ [AI Chat] SSE流接收完成')
        break
      }
      
      // 解码数据块
      buffer += decoder.decode(value, { stream: true })
      
      // 按行处理SSE数据
      const lines = buffer.split('\n')
      buffer = lines.pop() || ''
      
      for (const line of lines) {
        if (line.startsWith('data:')) {
          const data = line.substring(5).trim()
          
          if (data === '[DONE]' || data === 'DONE') {
            console.log('✅ [AI Chat] 收到结束标记')
            continue
          }
          
          if (!data) continue
          
          try {
            // 尝试解析JSON
            const parsed = JSON.parse(data)
            
            // 检查是否是错误消息
            if (parsed.code !== undefined && parsed.code !== 200) {
              console.error('❌ [AI Chat] 服务器返回错误:', parsed)
              ElMessage.error(parsed.message || '服务器错误')
              isStreaming.value = false
              streamingContent.value = ''
              return
            }
            
            // 提取内容
            let content = ''
            if (parsed.data) {
              content = typeof parsed.data === 'string' ? parsed.data : JSON.stringify(parsed.data)
            } else if (parsed.content) {
              content = parsed.content
            } else if (parsed.message) {
              content = parsed.message
            }
            
            if (content) {
              streamingContent.value += content
          scrollToBottom()
            }
          } catch (e) {
            // 如果不是JSON，直接追加文本
            streamingContent.value += data
            scrollToBottom()
          }
        }
      }
    }

    // 流式传输完成，保存消息
    if (streamingContent.value) {
      messages.value.push({
        id: Date.now(),
        role: 'assistant',
        content: streamingContent.value,
        timestamp: new Date()
      })
      console.log('✅ [AI Chat] 消息已保存')
    } else {
      console.warn('⚠️ [AI Chat] 未接收到任何内容')
      ElMessage.warning('未接收到AI响应')
    }
    
    streamingContent.value = ''
    isStreaming.value = false
    scrollToBottom()

  } catch (error: any) {
    console.error('❌ [AI Chat] 发送消息失败:', error)
    ElMessage.error('发送失败：' + (error.message || '请稍后重试'))
    isStreaming.value = false
    streamingContent.value = ''
  }
}

// 清空消息
const clearMessages = () => {
  messages.value = [
    {
      id: Date.now(),
      role: 'assistant',
      content: '您好！我是AI智能问答助手，我可以为您解答各种问题。请问有什么可以帮助您的？',
      timestamp: new Date()
    }
  ]
  currentChatId.value = ''
  ElMessage.success('对话已清空')
}

// 格式化消息内容（支持 Markdown 和智能分段）
const formatMessage = (content: string) => {
  let formatted = content
  
  // 1. 处理代码块 ```code```
  formatted = formatted.replace(/```(\w+)?\n([\s\S]*?)```/g, (match, lang, code) => {
    return `<pre class="code-block"><code class="language-${lang || 'text'}">${escapeHtml(code.trim())}</code></pre>`
  })
  
  // 2. 处理行内代码 `code`
  formatted = formatted.replace(/`([^`]+)`/g, '<code class="inline-code">$1</code>')
  
  // 3. 处理粗体 **text** 或 __text__
  formatted = formatted.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
  formatted = formatted.replace(/__([^_]+)__/g, '<strong>$1</strong>')
  
  // 4. 处理斜体 *text* 或 _text_
  formatted = formatted.replace(/\*([^*]+)\*/g, '<em>$1</em>')
  formatted = formatted.replace(/_([^_]+)_/g, '<em>$1</em>')
  
  // 5. 处理有序列表（数字开头）
  formatted = formatted.replace(/^\d+\.\s+(.+)$/gm, '<li class="numbered-item">$1</li>')
  
  // 6. 处理无序列表（- 或 * 开头）
  formatted = formatted.replace(/^[-*]\s+(.+)$/gm, '<li class="bullet-item">$1</li>')
  
  // 7. 包装列表项
  formatted = formatted.replace(/(<li class="numbered-item">.*<\/li>)/s, '<ol class="formatted-list">$1</ol>')
  formatted = formatted.replace(/(<li class="bullet-item">.*<\/li>)/s, '<ul class="formatted-list">$1</ul>')
  
  // 8. 处理标题（# ## ###）
  formatted = formatted.replace(/^### (.+)$/gm, '<h4 class="msg-h4">$1</h4>')
  formatted = formatted.replace(/^## (.+)$/gm, '<h3 class="msg-h3">$1</h3>')
  formatted = formatted.replace(/^# (.+)$/gm, '<h2 class="msg-h2">$1</h2>')
  
  // 9. 处理链接 [text](url)
  formatted = formatted.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" class="msg-link">$1</a>')
  
  // 10. 处理换行（保留空行作为段落分隔）
  formatted = formatted.replace(/\n\n/g, '</p><p class="msg-paragraph">')
  formatted = formatted.replace(/\n/g, '<br>')
  
  // 11. 包装段落
  if (!formatted.startsWith('<')) {
    formatted = '<p class="msg-paragraph">' + formatted + '</p>'
  }
  
  // 12. 智能识别并高亮关键词
  formatted = highlightKeywords(formatted)
  
  return formatted
}

// HTML 转义
const escapeHtml = (text: string) => {
  const map: { [key: string]: string } = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#039;'
  }
  return text.replace(/[&<>"']/g, m => map[m])
}

// 高亮关键词
const highlightKeywords = (content: string) => {
  // 技术关键词
  const techKeywords = ['Vue3', 'SpringBoot', 'Spring Security', 'JWT', 'RBAC', 'MyBatis-Plus', 
                        'Lombok', 'MySQL', 'Redis', 'PGVector', 'Docker', 'Nginx', 'OpenAPI']
  
  techKeywords.forEach(keyword => {
    const regex = new RegExp(`(${keyword})`, 'g')
    content = content.replace(regex, '<span class="keyword-tech">$1</span>')
  })
  
  return content
}

// 格式化时间
const formatTime = (date: Date) => {
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 组件卸载时清理
onUnmounted(() => {
  // 清理资源
})
</script>

<style scoped lang="scss">
.ai-chat {
  height: calc(100vh - 120px);
  padding: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chat-container {
  width: 100%;
  max-width: 1000px;
  height: 100%;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 2px solid #f3f4f6;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);

.header-info {
  display: flex;
  align-items: center;
    gap: 16px;

    .avatar {
      width: 50px;
      height: 50px;
      border-radius: 12px;
      background: rgba(255, 255, 255, 0.2);
      backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
    }

    .info-text {
      h2 {
        font-size: 20px;
        font-weight: 700;
        color: white;
    margin: 0 0 4px 0;
      }

      p {
        font-size: 13px;
        color: rgba(255, 255, 255, 0.9);
        margin: 0;
      }
    }
  }
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  background: #f9fafb;
  display: flex;
  flex-direction: column;
  gap: 20px;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background: #d1d5db;
    border-radius: 3px;
  }
}

.message-item {
  display: flex;
  gap: 12px;
  animation: fadeIn 0.3s ease;
  
  &.user-message {
    flex-direction: row-reverse;
    
    .message-content {
      background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
      color: white;

      .message-time {
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }

  &.assistant-message {
    .message-content {
      background: white;
      color: #1f2937;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    color: white;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
  
  .user-message & {
    background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  }
}

.message-content {
  max-width: 70%;
  padding: 14px 18px;
  border-radius: 12px;
  word-wrap: break-word;
}

.message-text {
  font-size: 15px;
  line-height: 1.8;
  margin-bottom: 6px;
  
  // Markdown 样式
  :deep(.msg-paragraph) {
    margin: 0 0 12px 0;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  :deep(.msg-h2) {
    font-size: 18px;
    font-weight: 700;
    margin: 16px 0 8px 0;
    padding-bottom: 6px;
    border-bottom: 2px solid #e5e7eb;
    color: #1f2937;
  }
  
  :deep(.msg-h3) {
    font-size: 16px;
    font-weight: 600;
    margin: 12px 0 6px 0;
    color: #374151;
  }
  
  :deep(.msg-h4) {
  font-size: 14px;
    font-weight: 600;
    margin: 10px 0 6px 0;
    color: #4b5563;
  }
  
  :deep(.inline-code) {
    background: #f3f4f6;
    color: #ef4444;
    padding: 2px 6px;
    border-radius: 4px;
    font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
    font-size: 13px;
  }
  
  :deep(.code-block) {
    background: #1f2937;
    color: #f9fafb;
    padding: 16px;
    border-radius: 8px;
    overflow-x: auto;
    margin: 12px 0;
    font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
    font-size: 13px;
    line-height: 1.6;
    
    code {
      color: #f9fafb;
    }
  }
  
  :deep(.formatted-list) {
    margin: 12px 0;
    padding-left: 24px;
    
    li {
      margin: 6px 0;
      line-height: 1.6;
    }
  }
  
  :deep(.numbered-item) {
    list-style-type: decimal;
    color: #374151;
    
    &::marker {
      color: #10b981;
      font-weight: 600;
    }
  }
  
  :deep(.bullet-item) {
    list-style-type: disc;
    color: #374151;
    
    &::marker {
      color: #10b981;
    }
  }
  
  :deep(.msg-link) {
      color: #3b82f6;
    text-decoration: none;
    border-bottom: 1px solid #93c5fd;
    transition: all 0.2s;
    
    &:hover {
      color: #2563eb;
      border-bottom-color: #2563eb;
    }
  }
  
  :deep(.keyword-tech) {
    color: #10b981;
    font-weight: 600;
    padding: 0 2px;
  }
  
  :deep(strong) {
    font-weight: 600;
    color: #1f2937;
  }
  
  :deep(em) {
    font-style: italic;
    color: #4b5563;
  }
  
  // 用户消息中的样式适配（白色文字背景）
  .user-message & {
    :deep(.keyword-tech) {
      color: #86efac;
    }
    
    :deep(.inline-code) {
      background: rgba(255, 255, 255, 0.2);
      color: #fef3c7;
    }
    
    :deep(.msg-h2),
    :deep(.msg-h3),
    :deep(.msg-h4) {
      color: white;
      border-bottom-color: rgba(255, 255, 255, 0.3);
    }
    
    :deep(.numbered-item),
    :deep(.bullet-item) {
      color: rgba(255, 255, 255, 0.95);
      
      &::marker {
        color: white;
      }
    }
    
    :deep(strong) {
      color: white;
    }
  }
}

.message-time {
  font-size: 11px;
  opacity: 0.7;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  margin-top: 8px;
  
  span {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #10b981;
    animation: typing 1.4s infinite;
    
    &:nth-child(2) {
      animation-delay: 0.2s;
    }
    
    &:nth-child(3) {
      animation-delay: 0.4s;
    }
  }
}

@keyframes typing {
  0%, 60%, 100% {
    opacity: 0.3;
    transform: scale(1);
  }
  30% {
    opacity: 1;
    transform: scale(1.3);
  }
}

.chat-input {
  padding: 20px 24px;
  border-top: 2px solid #f3f4f6;
  background: white;

  .el-textarea {
    :deep(.el-textarea__inner) {
      border-radius: 8px;
      border: 2px solid #e5e7eb;
      font-size: 15px;
      line-height: 1.5;
      resize: none;
      transition: all 0.3s;

      &:focus {
        border-color: #10b981;
        box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
      }
    }
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;

    .input-tips {
      display: flex;
      align-items: center;
      gap: 6px;
      color: #9ca3af;
      font-size: 12px;

      .el-icon {
        font-size: 14px;
      }
    }

    .el-button {
      min-width: 100px;
      border-radius: 8px;
      font-weight: 600;
    }
  }
}

@media (max-width: 768px) {
  .ai-chat {
  padding: 12px;
    height: calc(100vh - 100px);
  }

  .chat-header {
    padding: 16px;

    .header-info {
      gap: 12px;

      .avatar {
        width: 40px;
        height: 40px;
      }

      .info-text {
        h2 {
          font-size: 16px;
        }

        p {
    font-size: 12px;
        }
      }
    }
  }

  .chat-messages {
    padding: 16px;
    gap: 16px;
  }
  
  .message-content {
    max-width: 85%;
  }
  
  .chat-input {
    padding: 16px;
  }

  .input-actions {
    flex-direction: column;
    gap: 12px;
  
    .input-tips {
    width: 100%;
      justify-content: center;
  }
  
    .el-button {
      width: 100%;
  }
  }
}
</style>

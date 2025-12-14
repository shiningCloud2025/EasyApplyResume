<template>
  <div class="ai-agent">
    <div class="chat-container">
      <!-- 聊天头部 -->
      <div class="chat-header">
        <div class="header-info">
          <div class="avatar">
            <el-icon :size="28"><Cpu /></el-icon>
          </div>
          <div class="info-text">
            <h2>AI系统管理助手</h2>
            <p>智能管理助手，为您提供专业服务</p>
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
            <el-icon v-else><Cpu /></el-icon>
          </div>
          <div class="message-content">
            <div class="message-text" v-html="formatMessage(message.content)"></div>
            <div class="message-time">{{ formatTime(message.timestamp) }}</div>
          </div>
        </div>
        
        <!-- 正在输入指示器 -->
        <div v-if="isStreaming" class="message-item assistant-message">
          <div class="message-avatar">
            <el-icon><Cpu /></el-icon>
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
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Cpu, User, Delete, InfoFilled, Promotion } from '@element-plus/icons-vue'
import { api } from '@/utils/request'

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
    content: '您好！我是AI系统管理助手，我可以帮您处理系统管理相关的问题。请问有什么可以帮助您的？',
    timestamp: new Date()
  }
])

let eventSource: EventSource | null = null
let currentChatId = ''

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
    // 使用SSE接收流式响应
    const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
    const token = localStorage.getItem('admin_token')
    
    const url = new URL(`${baseURL}/admin/ai/agent/chat`)
    if (currentChatId) {
      url.searchParams.append('chatId', currentChatId)
    }

    eventSource = new EventSource(
      `${url.toString()}&message=${encodeURIComponent(messageText)}&token=${token}`
    )

    eventSource.onmessage = (event) => {
      const data = event.data
      
      if (data === '[DONE]') {
        // 流式传输完成
        if (streamingContent.value) {
          messages.value.push({
            id: Date.now(),
            role: 'assistant',
            content: streamingContent.value,
            timestamp: new Date()
          })
        }
        streamingContent.value = ''
        isStreaming.value = false
        if (eventSource) {
          eventSource.close()
          eventSource = null
        }
        scrollToBottom()
      } else {
        try {
          const parsed = JSON.parse(data)
          if (parsed.chatId) {
            currentChatId = parsed.chatId
          }
          if (parsed.content) {
            streamingContent.value += parsed.content
            scrollToBottom()
          }
        } catch (e) {
          // 如果不是JSON，直接追加内容
          streamingContent.value += data
          scrollToBottom()
        }
      }
    }

    eventSource.onerror = (error) => {
      console.error('SSE错误:', error)
      ElMessage.error('连接断开，请重试')
      isStreaming.value = false
      streamingContent.value = ''
      if (eventSource) {
        eventSource.close()
        eventSource = null
      }
    }

  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送失败，请稍后重试')
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
      content: '您好！我是AI系统管理助手，我可以帮您处理系统管理相关的问题。请问有什么可以帮助您的？',
      timestamp: new Date()
    }
  ]
  currentChatId = ''
  ElMessage.success('对话已清空')
}

// 格式化消息内容（支持换行）
const formatMessage = (content: string) => {
  return content.replace(/\n/g, '<br>')
}

// 格式化时间
const formatTime = (date: Date) => {
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 组件卸载时关闭SSE连接
onUnmounted(() => {
  if (eventSource) {
    eventSource.close()
  }
})
</script>

<style scoped lang="scss">
.ai-agent {
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);

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
  line-height: 1.6;
  margin-bottom: 6px;
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
    background: #667eea;
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
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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
  .ai-agent {
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

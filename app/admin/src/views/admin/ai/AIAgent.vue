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
        <el-button @click="clearMessages" type="danger" plain :disabled="isStreaming || !canSendAgent">
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
            <!-- AI消息：显示思考过程和结果 -->
            <template v-if="message.role === 'assistant' && message.thinkingSteps && message.thinkingSteps.length > 0">
              <!-- 思考过程（可折叠） -->
              <el-collapse class="thinking-collapse">
                <el-collapse-item>
                  <template #title>
                    <div class="thinking-title">
                      <el-icon><View /></el-icon>
                      <span>思考过程（{{ message.thinkingSteps.length }} 步）</span>
                    </div>
                  </template>
                  <div class="thinking-steps">
                    <div 
                      v-for="step in message.thinkingSteps" 
                      :key="step.stepNumber"
                      class="thinking-step"
                      :class="'step-' + step.type"
                    >
                      <div class="step-header">
                        <span class="step-number">步骤 {{ step.stepNumber }}</span>
                        <span class="step-type">{{ getStepTypeLabel(step.type) }}</span>
                      </div>
                      <div class="step-content" v-html="formatStepContent(step.content)"></div>
                    </div>
                  </div>
                </el-collapse-item>
              </el-collapse>
              
              <!-- 最终结果 -->
              <div v-if="message.finalResult" class="final-result">
                <div class="result-header">
                  <el-icon><CircleCheck /></el-icon>
                  <span>最终结果</span>
                </div>
                <div class="result-content" v-html="formatMessage(message.finalResult)"></div>
              </div>
              
              <!-- 🔍 调试：如果没有提取到结果，显示原始内容 -->
              <div v-if="!message.finalResult && message.content" class="debug-info">
                <details>
                  <summary style="cursor: pointer; color: #ef4444; font-size: 12px;">
                    ⚠️ 未提取到结果（点击查看原始内容）
                  </summary>
                  <div class="message-text" style="margin-top: 8px; font-size: 12px;" v-html="formatMessage(message.content)"></div>
                </details>
              </div>
            </template>
            
            <!-- 普通消息或用户消息 -->
            <template v-else>
            <div class="message-text" v-html="formatMessage(message.content)"></div>
            </template>
            
            <div class="message-time">{{ formatTime(message.timestamp) }}</div>
          </div>
        </div>
        
        <!-- 正在输入指示器 -->
        <div v-if="isStreaming" class="message-item assistant-message">
          <div class="message-avatar">
            <el-icon><Cpu /></el-icon>
          </div>
          <div class="message-content streaming-message">
            <!-- 显示当前思考步骤 -->
            <div v-if="streamingSteps.length > 0" class="streaming-steps">
              <div class="streaming-header">
                <el-icon><View /></el-icon>
                <span>正在思考...</span>
              </div>
              <div 
                v-for="step in streamingSteps" 
                :key="step.stepNumber"
                class="streaming-step"
              >
                <span class="step-num">{{ step.stepNumber }}</span>
                <span class="step-preview">{{ getStepPreview(step.content) }}</span>
              </div>
            </div>
            
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
          :disabled="isStreaming || !canSendAgent"
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
            :disabled="!inputMessage.trim() || !canSendAgent"
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
import { ref, nextTick, onMounted, onUnmounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Cpu, User, Delete, InfoFilled, Promotion, View, CircleCheck } from '@element-plus/icons-vue'
import { aiApi } from '@/api/admin'
import { useAuthStore } from '@/store/auth'

interface Message {
  id: number
  role: 'user' | 'assistant'
  content: string
  timestamp: Date
  thinkingSteps?: ThinkingStep[]  // 思考步骤
  finalResult?: string             // 最终结果
}

interface ThinkingStep {
  stepNumber: number
  content: string
  type: 'thinking' | 'tool_call' | 'result'
}

const messagesContainer = ref<HTMLElement>()
const inputMessage = ref('')
const isStreaming = ref(false)
const streamingContent = ref('')
const authStore = useAuthStore()
const canSendAgent = computed(() => authStore.hasPermission('/admin/aiSystemManagerAssistant/agent/chat'))
const streamingSteps = ref<ThinkingStep[]>([])  // 当前流式传输的思考步骤
const messages = ref<Message[]>([
  {
    id: Date.now(),
    role: 'assistant',
    content: '您好！我是AI系统管理助手，我可以帮您处理系统管理相关的问题。请问有什么可以帮助您的？',
    timestamp: new Date()
  }
])

let eventSource: EventSource | null = null
// 页面加载时生成随机 chatId，整个会话期间保持不变
let currentChatId = 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
let currentStepNumber = 0
let streamingTimeout: ReturnType<typeof setTimeout> | null = null
let streamingContentCache = '' // 追踪当前流式内容

// 强制重置状态
const forceResetState = () => {
  console.log('🔄 [强制重置] 状态')
  // 如果有已接收的内容，先保存到消息列表
  if (streamingContentCache || streamingSteps.value.length > 0) {
    const finalResult = extractFinalResult(streamingSteps.value, streamingContentCache)
    messages.value.push({
      id: Date.now(),
      role: 'assistant',
      content: streamingContentCache,
      thinkingSteps: streamingSteps.value.length > 0 ? [...streamingSteps.value] : undefined,
      finalResult: finalResult || undefined,
      timestamp: new Date()
    })
  }
  isStreaming.value = false
  streamingContent.value = ''
  streamingSteps.value = []
  currentStepNumber = 0
  streamingContentCache = ''
  if (streamingTimeout) {
    clearTimeout(streamingTimeout)
    streamingTimeout = null
  }
  scrollToBottom()
}

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
  if (!canSendAgent.value) {
    ElMessage.error('暂无发送权限')
    return
  }

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
  streamingSteps.value = []
  currentStepNumber = 0
  streamingContentCache = '' // 重置缓存
  
  // 设置超时保护（15秒后强制重置，Agent比普通Chat时间长）
  if (streamingTimeout) {
    clearTimeout(streamingTimeout)
  }
  streamingTimeout = setTimeout(() => {
    console.log('⚠️ [超时] 15秒后强制重置状态')
    forceResetState()
  }, 15000)

  try {
    console.log('📤 [AI Agent] 发送消息:', messageText)
    console.log('📤 [AI Agent] 当前chatId:', currentChatId)
    
    // 使用 agent/chat 接口（需要后端先修复 - 添加 produces 并直接返回 SseEmitter）
    const response = await aiApi.aiSystemManagerAgentChat(messageText, currentChatId || undefined)

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    console.log('📥 [AI Agent] 开始接收SSE流...')
    console.log('📥 [AI Agent] Response headers:', response.headers)
    
    // 读取自定义响应头（如果有）
    const responseCode = response.headers.get('X-Response-Code')
    const responseMessage = response.headers.get('X-Response-Message')
    const responseChatId = response.headers.get('X-Chat-Id')
    
    if (responseCode) {
      console.log('📋 [AI Agent] 响应码:', responseCode)
      console.log('📋 [AI Agent] 响应消息:', responseMessage)
      console.log('📋 [AI Agent] Chat ID:', responseChatId)
      
      if (responseCode !== '200') {
        ElMessage.error(responseMessage || '请求失败')
        isStreaming.value = false
        return
      }
      
      // 保存 chatId 用于后续对话
      if (responseChatId) {
        currentChatId = responseChatId
      }
    }
    
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
        console.log('✅ [AI Agent] SSE流接收完成')
        break
      }
      
      // 解码数据块
      buffer += decoder.decode(value, { stream: true })
      console.log('📦 [AI Agent] 缓冲区内容:', buffer)
      
      // 按行处理SSE数据
      const lines = buffer.split('\n')
      buffer = lines.pop() || '' // 保留不完整的行
      
      for (const line of lines) {
        console.log('📄 [AI Agent] 处理行:', line)
        
        // 处理事件类型（如果有）
        if (line.startsWith('event:')) {
          const eventType = line.substring(6).trim()
          console.log('🎭 [AI Agent] 事件类型:', eventType)
          continue
        }
        
        if (line.startsWith('data:')) {
          const data = line.substring(5).trim()
          
          if (data === '[DONE]' || data === 'DONE') {
            console.log('✅ [AI Agent] 收到结束标记')
            continue
          }
          
          if (!data) continue
          
          try {
            // 尝试解析JSON
            const parsed = JSON.parse(data)
            console.log('📊 [AI Agent] 解析JSON:', parsed)
            
            // 检查是否是状态消息
            if (parsed.code !== undefined) {
              console.log('📋 [AI Agent] 收到状态消息:', parsed)
              if (parsed.code !== 200) {
                ElMessage.error(parsed.message || '请求失败')
                return
              }
              continue
            }
            
            // 提取内容 - 尝试多种可能的字段
            let content = ''
            if (parsed.data) {
              content = typeof parsed.data === 'string' ? parsed.data : JSON.stringify(parsed.data)
            } else if (parsed.content) {
              content = parsed.content
            } else if (parsed.message) {
              content = parsed.message
            } else if (typeof parsed === 'string') {
              content = parsed
            }
            
            if (content) {
              streamingContent.value += content
              streamingContentCache = streamingContent.value // 同步更新缓存
              scrollToBottom()
            }
          } catch (e) {
            // 如果不是JSON，直接追加文本并解析步骤
            console.log('📝 [AI Agent] 直接追加文本:', data)
            parseStepContent(data)
            streamingContent.value += data
            streamingContentCache = streamingContent.value // 同步更新缓存
            scrollToBottom()
          }
        }
      }
    }

    // 流式传输完成，保存消息
    if (streamingContent.value || streamingSteps.value.length > 0) {
      streamingContentCache = '' // 已保存，清空缓存防止重复保存
      // 提取最终结果（通常是最后一步或包含"结果"的步骤）
      const finalResult = extractFinalResult(streamingSteps.value, streamingContent.value)
      
      // 🔍 调试信息
      console.log('🔍 [调试] 完整内容长度:', streamingContent.value.length)
      console.log('🔍 [调试] 完整内容前500字符:', streamingContent.value.substring(0, 500))
      console.log('🔍 [调试] 思考步骤数量:', streamingSteps.value.length)
      console.log('🔍 [调试] 思考步骤详情:', streamingSteps.value)
      console.log('🔍 [调试] 提取的最终结果:', finalResult)
      
      messages.value.push({
        id: Date.now(),
        role: 'assistant',
        content: streamingContent.value,
        thinkingSteps: streamingSteps.value.length > 0 ? [...streamingSteps.value] : undefined,
        finalResult: finalResult || undefined,
        timestamp: new Date()
      })
      console.log('✅ [AI Agent] 消息已保存')
      console.log('   思考步骤:', streamingSteps.value.length)
      console.log('   最终结果:', finalResult ? '已提取' : '无')
    } else {
      console.warn('⚠️ [AI Agent] 未接收到任何内容')
      ElMessage.warning('未接收到AI响应')
    }
    
    streamingContent.value = ''
    streamingSteps.value = []
    currentStepNumber = 0
    isStreaming.value = false
    scrollToBottom()

  } catch (error) {
    console.error('❌ [AI Agent] 发送消息失败:', error)
    ElMessage.error('发送失败：' + (error.message || '请稍后重试'))
  }
  
  // 无论如何都重置状态
  console.log('✅ [AI Agent] 重置状态')
  forceResetState()
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
  // 清空时生成新的 chatId
  currentChatId = 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
  ElMessage.success('对话已清空，已开启新会话')
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
  
  // 3. 处理粗体 **text**
  formatted = formatted.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
  
  // 4. 处理斜体 *text*
  formatted = formatted.replace(/\*([^*]+)\*/g, '<em>$1</em>')
  
  // 5. 处理有序列表
  formatted = formatted.replace(/^\d+\.\s+(.+)$/gm, '<li class="numbered-item">$1</li>')
  
  // 6. 处理无序列表
  formatted = formatted.replace(/^[-*]\s+(.+)$/gm, '<li class="bullet-item">$1</li>')
  
  // 7. 处理标题
  formatted = formatted.replace(/^### (.+)$/gm, '<h4 class="msg-h4">$1</h4>')
  formatted = formatted.replace(/^## (.+)$/gm, '<h3 class="msg-h3">$1</h3>')
  formatted = formatted.replace(/^# (.+)$/gm, '<h2 class="msg-h2">$1</h2>')
  
  // 8. 处理链接
  formatted = formatted.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" class="msg-link">$1</a>')
  
  // 9. 智能分段（识别段落）
  formatted = formatted.replace(/\n\n/g, '</p><p class="msg-paragraph">')
  formatted = formatted.replace(/\n/g, '<br>')
  
  // 10. 包装段落
  if (!formatted.startsWith('<')) {
    formatted = '<p class="msg-paragraph">' + formatted + '</p>'
  }
  
  // 11. 高亮技术关键词
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
  const techKeywords = ['Vue3', 'SpringBoot', 'Spring Security', 'JWT', 'RBAC', 'MyBatis-Plus',
                        'Lombok', 'MySQL', 'Redis', 'PGVector', 'Docker', 'Nginx', 'OpenAPI',
                        'Vite', 'Pinia', 'Composition API', 'TypeScript', 'Swagger']
  
  techKeywords.forEach(keyword => {
    const regex = new RegExp(`\\b(${keyword})\\b`, 'g')
    content = content.replace(regex, '<span class="keyword-tech">$1</span>')
  })
  
  return content
}

// 格式化时间
const formatTime = (date: Date) => {
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 解析步骤内容（改进版：支持多个步骤在同一段文本中）
const parseStepContent = (content: string) => {
  // 匹配所有 "步骤 X: " 格式（全局匹配）
  const stepRegex = /步骤\s*(\d+):\s*([^步]*?)(?=步骤\s*\d+:|$)/gs
  const matches = [...content.matchAll(stepRegex)]
  
  console.log('🔍 [解析] 找到', matches.length, '个步骤匹配')
  
  if (matches.length > 0) {
    matches.forEach(match => {
      const stepNum = parseInt(match[1])
      const stepContent = match[2].trim()
      
      console.log('🔍 [解析] 步骤', stepNum, '内容长度:', stepContent.length)
      
      if (stepNum > currentStepNumber) {
        currentStepNumber = stepNum
      }
      
      // 判断步骤类型
      let stepType: 'thinking' | 'tool_call' | 'result' = 'thinking'
      
      if (stepContent.includes('doTerminate') || stepContent.includes('结果:') || 
          stepContent.includes('任务') || stepContent.includes('完成') ||
          stepContent.includes('终止') || stepContent.includes('结束')) {
        stepType = 'result'
      } else if (stepContent.includes('工具') || stepContent.includes('调用') || 
                 stepContent.includes('function') || stepContent.includes('tool')) {
        stepType = 'tool_call'
      }
      
      // 添加或更新步骤
      const existingIndex = streamingSteps.value.findIndex(s => s.stepNumber === stepNum)
      if (existingIndex >= 0) {
        streamingSteps.value[existingIndex].content = stepContent
        streamingSteps.value[existingIndex].type = stepType
      } else {
        streamingSteps.value.push({
          stepNumber: stepNum,
          content: stepContent,
          type: stepType
        })
      }
    })
  }
}

// 提取最终结果（智能版 - 忽略无意义结果）
const extractFinalResult = (steps: ThinkingStep[], fullContent: string): string => {
  console.log('🔍 [提取结果] 步骤数:', steps.length, '内容长度:', fullContent.length)
  
  // 无意义的结果关键词
  const meaninglessKeywords = [
    '任务结束',
    '任务终止',
    '思考完成',
    '无需行动',
    'doTerminate',
    '执行结束',
    '达到最大步骤'
  ]
  
  // 检查内容是否无意义
  const isMeaningless = (content: string): boolean => {
    const trimmed = content.trim()
    return meaninglessKeywords.some(keyword => trimmed.includes(keyword)) && trimmed.length < 50
  }
  
  // 1. 从后往前查找第一个有意义的 result 类型步骤
  const resultSteps = steps.filter(s => s.type === 'result')
  for (let i = resultSteps.length - 1; i >= 0; i--) {
    const step = resultSteps[i]
    console.log('🔍 [提取结果] 检查result步骤', resultSteps.length - i, ':', step.content.substring(0, 50))
    
    if (!isMeaningless(step.content)) {
      // 提取"结果:"后的内容
      const resultMatch = step.content.match(/结果:\s*["""'](.*?)["""']/s)
      if (resultMatch && !isMeaningless(resultMatch[1])) {
        console.log('🔍 [提取结果] ✅ 从result标记提取')
        return resultMatch[1].trim()
      }
      
      console.log('🔍 [提取结果] ✅ 使用result步骤内容')
      return cleanResult(step.content)
    }
  }
  
  // 2. 查找工具调用步骤（tool_call）并提取有用信息
  const toolSteps = steps.filter(s => s.type === 'tool_call')
  if (toolSteps.length > 0) {
    const lastTool = toolSteps[toolSteps.length - 1]
    console.log('🔍 [提取结果] 尝试从工具调用提取')
    
    // 尝试从搜索结果中提取摘要
    if (lastTool.content.includes('searchWeb')) {
      const summary = extractSearchSummary(lastTool.content)
      if (summary) {
        console.log('🔍 [提取结果] ✅ 从搜索结果提取摘要')
        return summary
      }
    }
    
    // 返回工具调用结果
    if (!isMeaningless(lastTool.content)) {
      console.log('🔍 [提取结果] ✅ 使用工具调用结果')
      return cleanResult(lastTool.content)
    }
  }
  
  // 3. 从后往前查找任何有意义的步骤
  for (let i = steps.length - 1; i >= 0; i--) {
    const step = steps[i]
    if (!isMeaningless(step.content)) {
      console.log('🔍 [提取结果] ✅ 使用步骤', step.stepNumber)
      return cleanResult(step.content)
    }
  }
  
  console.log('🔍 [提取结果] ❌ 未找到有意义的结果')
  return '未能提取到有效结果'
}

// 从搜索结果中提取摘要
const extractSearchSummary = (content: string): string => {
  try {
    // 检查是否是文件写入结果
    if (content.includes('File written successfully')) {
      const filePathMatch = content.match(/File written successfully to:\s*(.+)/)
      if (filePathMatch) {
        const filePath = filePathMatch[1].trim()
        const fileName = filePath.split(/[/\\]/).pop() || filePath
        return `已生成文件: ${fileName}`
      }
    }
    
    // 尝试找到 JSON 中的 snippet 字段
    const snippetMatches = content.match(/"snippet":\s*"([^"]{50,500})"/g)
    if (snippetMatches && snippetMatches.length > 0) {
      // 取第一个snippet，清理转义字符
      let snippet = snippetMatches[0]
        .replace(/"snippet":\s*"/, '')
        .replace(/"$/, '')
        .replace(/\\n/g, '\n')
        .replace(/\\"/g, '"')
      
      // 限制长度
      if (snippet.length > 300) {
        snippet = snippet.substring(0, 300) + '...'
      }
      
      return snippet
    }
    
    // 尝试找到 title
    const titleMatch = content.match(/"title":\s*"([^"]+)"/)
    if (titleMatch) {
      return titleMatch[1].replace(/\\"/g, '"')
    }
  } catch (e) {
    console.log('提取搜索摘要失败:', e)
  }
  
  return ''
}

// 清理结果内容
const cleanResult = (content: string): string => {
  let cleaned = content.trim()
  
  // 移除 JSON 转义
  cleaned = cleaned.replace(/\\"/g, '"')
  cleaned = cleaned.replace(/\\n/g, '\n')
  
  // 移除过多的 JSON 结构标记
  cleaned = cleaned.replace(/^\{+|\}+$/g, '')
  
  // 如果内容太长且包含大量JSON，尝试提取纯文本部分
  if (cleaned.length > 500 && cleaned.includes('{')) {
    // 尝试提取引号内的文本
    const textMatch = cleaned.match(/["']((?:[^"'\\]|\\.){20,}?)["']/g)
    if (textMatch && textMatch.length > 0) {
      cleaned = textMatch.map(m => m.slice(1, -1)).join('\n')
    }
  }
  
  // 限制长度
  if (cleaned.length > 1000) {
    cleaned = cleaned.substring(0, 1000) + '...'
  }
  
  return cleaned
}

// 格式化步骤内容（清理JSON格式）
const formatStepContent = (content: string): string => {
  // 移除过多的转义和 JSON 结构，提取核心内容
  let formatted = content
  
  // 特殊处理：文件写入成功的消息
  if (formatted.includes('File written successfully')) {
    const filePathMatch = formatted.match(/File written successfully to:\s*(.+)/)
    if (filePathMatch) {
      const filePath = filePathMatch[1].trim()
      const fileName = filePath.split(/[/\\]/).pop() || filePath
      return `✅ 文件已保存: <code>${fileName}</code><br><small style="color: #6b7280;">${filePath}</small>`
    }
  }
  
  // 特殊处理：搜索结果
  if (formatted.includes('searchWeb')) {
    formatted = formatted.replace('工具 searchWeb 完成了它的任务！结果: ', '<strong>🔍 搜索结果：</strong><br>')
  }
  
  // 特殊处理：网页爬取
  if (formatted.includes('scrapeWebPage')) {
    formatted = formatted.replace('工具 scrapeWebPage 完成了它的任务！结果: ', '<strong>🌐 网页内容：</strong><br>')
  }
  
  // 移除 JSON 转义
  formatted = formatted.replace(/\\"/g, '"')
  formatted = formatted.replace(/\\n/g, '<br>')
  
  // 尝试提取关键信息
  try {
    // 如果包含 JSON 对象，尝试美化显示
    if (formatted.includes('{') && formatted.includes('}')) {
      // 不完全解析，只做基础清理
      formatted = formatted.replace(/\{[^}]*snippet_highlighted_words[^}]*\}/g, '')
      formatted = formatted.replace(/\{[^}]*thumbnail[^}]*\}/g, '')
    }
  } catch (e) {
    // 保持原样
  }
  
  // 移除过长的 URL
  formatted = formatted.replace(/https?:\/\/[^\s]{100,}/g, (url) => {
    return url.substring(0, 50) + '...'
  })
  
  // 限制单行长度
  formatted = formatted.replace(/([^\n]{200})/g, '$1<br>')
  
  return formatted
}

// 获取步骤类型标签
const getStepTypeLabel = (type: 'thinking' | 'tool_call' | 'result'): string => {
  const labels = {
    thinking: '思考中',
    tool_call: '工具调用',
    result: '得出结果'
  }
  return labels[type]
}

// 获取步骤预览（截取前50个字符）
const getStepPreview = (content: string): string => {
  const cleaned = content.replace(/[{}"\\]/g, '').trim()
  return cleaned.length > 50 ? cleaned.substring(0, 50) + '...' : cleaned
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
      color: #667eea;
      font-weight: 600;
    }
  }
  
  :deep(.bullet-item) {
    list-style-type: disc;
    color: #374151;
    
    &::marker {
      color: #667eea;
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
    color: #667eea;
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
  
  // 用户消息中的样式适配
  .user-message & {
    :deep(.keyword-tech) {
      color: #60a5fa;
    }
    
    :deep(.inline-code) {
      background: rgba(255, 255, 255, 0.2);
      color: #fef3c7;
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

// 思考过程样式
.thinking-collapse {
  margin-bottom: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;

  :deep(.el-collapse-item__header) {
    background: #f9fafb;
    padding: 12px 16px;
    border: none;
    font-weight: 500;

    &:hover {
      background: #f3f4f6;
    }
  }

  :deep(.el-collapse-item__wrap) {
    border: none;
  }

  :deep(.el-collapse-item__content) {
    padding: 0;
  }
}

.thinking-title {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #6b7280;
  font-size: 13px;

  .el-icon {
    font-size: 16px;
  }
}

.thinking-steps {
  max-height: 400px;
  overflow-y: auto;
  padding: 8px 12px;
  background: #fafafa;

  &::-webkit-scrollbar {
    width: 4px;
  }

  &::-webkit-scrollbar-thumb {
    background: #d1d5db;
    border-radius: 2px;
  }
}

.thinking-step {
  margin-bottom: 12px;
  padding: 12px;
  background: white;
  border-radius: 6px;
  border-left: 3px solid #9ca3af;

  &:last-child {
    margin-bottom: 0;
  }

  &.step-tool_call {
    border-left-color: #3b82f6;
    background: #eff6ff;
  }

  &.step-result {
    border-left-color: #10b981;
    background: #f0fdf4;
  }
}

.step-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding-bottom: 6px;
  border-bottom: 1px solid #e5e7eb;
}

.step-number {
  font-weight: 600;
  color: #374151;
  font-size: 13px;
}

.step-type {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
  background: #f3f4f6;
  color: #6b7280;

  .step-tool_call & {
    background: #dbeafe;
    color: #1e40af;
  }

  .step-result & {
    background: #d1fae5;
    color: #065f46;
  }
}

.step-content {
  font-size: 13px;
  line-height: 1.6;
  color: #4b5563;
  word-break: break-word;
  
  :deep(br) {
    content: '';
    display: block;
    margin: 4px 0;
  }
}

.final-result {
  padding: 16px;
  background: linear-gradient(135deg, #f0fdf4 0%, #ecfdf5 100%);
  border: 2px solid #10b981;
  border-radius: 10px;
  margin-top: 12px;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  color: #059669;
  font-weight: 600;
  font-size: 14px;

  .el-icon {
    font-size: 18px;
  }
}

.result-content {
  font-size: 15px;
  line-height: 1.8;
  color: #1f2937;
  padding: 12px;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

// 流式消息样式
.streaming-message {
  min-width: 300px;
}

.streaming-steps {
  margin-bottom: 12px;
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
  border: 1px dashed #d1d5db;
}

.streaming-header {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  color: #6b7280;
  font-size: 12px;
  font-weight: 500;

  .el-icon {
    font-size: 14px;
    animation: pulse 2s ease-in-out infinite;
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.streaming-step {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 8px;
  margin-bottom: 4px;
  background: white;
  border-radius: 4px;
  font-size: 12px;

  &:last-child {
    margin-bottom: 0;
  }
}

.step-num {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #3b82f6;
  color: white;
  border-radius: 50%;
  font-size: 10px;
  font-weight: 600;
}

.step-preview {
  flex: 1;
  color: #4b5563;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
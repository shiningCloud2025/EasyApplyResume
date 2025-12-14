<template>
  <div class="ai-chat">
    <div class="chat-container">
      <div class="chat-sidebar">
        <div class="sidebar-header">
          <h3>AI智能助手</h3>
          <el-button type="primary" size="small" @click="startNewChat">
            <i class="el-icon-plus"></i>
            新对话
          </el-button>
        </div>
        <div class="chat-history">
          <div 
            v-for="chat in chatHistory" 
            :key="chat.id"
            class="chat-item"
            :class="{ active: currentChatId === chat.id }"
            @click="switchChat(chat.id)"
          >
            <div class="chat-title">{{ chat.title }}</div>
            <div class="chat-time">{{ formatRelativeTime(chat.lastTime) }}</div>
          </div>
        </div>
      </div>

      <div class="chat-main">
        <div class="chat-header">
          <div class="header-info">
            <div class="ai-avatar">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="ai-info">
              <h4>AI智能助手</h4>
              <span class="ai-status">在线</span>
            </div>
          </div>
          <div class="header-actions">
            <el-dropdown @command="handleCommand">
              <el-button type="text" size="small">
                <i class="el-icon-more"></i>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="export">导出对话</el-dropdown-item>
                  <el-dropdown-item command="clear">清空对话</el-dropdown-item>
                  <el-dropdown-item command="settings">设置</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <div class="chat-messages" ref="messagesContainer">
          <div 
            v-for="message in messages" 
            :key="message.id"
            class="message-item"
            :class="{ 'user-message': message.type === 'user', 'ai-message': message.type === 'ai' }"
          >
            <div class="message-avatar">
              <i v-if="message.type === 'user'" class="el-icon-user-solid"></i>
              <i v-else class="el-icon-cpu"></i>
            </div>
            <div class="message-content">
              <div class="message-text" v-html="message.content"></div>
              <div class="message-time">{{ formatDateTime(message.timestamp) }}</div>
            </div>
            <div v-if="message.type === 'ai'" class="message-actions">
              <el-button type="text" size="small" @click="copyMessage(message.content)">
                <i class="el-icon-copy-document"></i>
              </el-button>
              <el-button type="text" size="small" @click="likeMessage(message.id)">
                <i :class="message.liked ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
              </el-button>
              <el-button type="text" size="small" @click="regenerateMessage(message.id)">
                <i class="el-icon-refresh"></i>
              </el-button>
            </div>
          </div>
          
          <div v-if="isTyping" class="message-item ai-message">
            <div class="message-avatar">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="message-content">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>

        <div class="chat-input">
          <div class="input-toolbar">
            <el-button type="text" size="small" @click="showPromptTemplates = !showPromptTemplates">
              <i class="el-icon-document"></i>
              模板
            </el-button>
            <el-button type="text" size="small" @click="showHistory = !showHistory">
              <i class="el-icon-time"></i>
              历史
            </el-button>
            <el-button type="text" size="small" @click="clearCurrentChat">
              <i class="el-icon-delete"></i>
              清空
            </el-button>
          </div>
          
          <div class="prompt-templates" v-if="showPromptTemplates">
            <div class="templates-grid">
              <div 
                v-for="template in promptTemplates" 
                :key="template.id"
                class="template-item"
                @click="useTemplate(template)"
              >
                <h5>{{ template.title }}</h5>
                <p>{{ template.desc }}</p>
              </div>
            </div>
          </div>

          <div class="input-area">
            <el-input
              v-model="inputMessage"
              type="textarea"
              :rows="4"
              placeholder="输入您的问题，按Enter发送，Shift+Enter换行"
              @keydown.prevent="handleKeyDown"
              :disabled="isTyping"
              resize="none"
            />
            <div class="input-actions">
              <span class="input-count">{{ inputMessage.length }}/2000</span>
              <el-button 
                type="primary" 
                :loading="isTyping"
                :disabled="!inputMessage.trim()"
                @click="sendMessage"
              >
                发送
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 设置对话框 -->
    <el-dialog v-model="showSettings" title="AI助手设置" width="500px">
      <el-form :model="settings" label-width="100px">
        <el-form-item label="模型选择">
          <el-select v-model="settings.model" style="width: 100%">
            <el-option label="GPT-3.5 Turbo" value="gpt-3.5-turbo" />
            <el-option label="GPT-4" value="gpt-4" />
            <el-option label="Claude-2" value="claude-2" />
            <el-option label="文心一言" value="wenxin" />
            <el-option label="通义千问" value="qianwen" />
          </el-select>
        </el-form-item>
        <el-form-item label="温度参数">
          <el-slider v-model="settings.temperature" :min="0" :max="2" :step="0.1" />
        </el-form-item>
        <el-form-item label="回复长度">
          <el-input-number v-model="settings.maxTokens" :min="100" :max="4000" />
        </el-form-item>
        <el-form-item label="上下文条数">
          <el-input-number v-model="settings.contextLength" :min="5" :max="20" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSettings = false">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { formatDateTime, formatRelativeTime } from '@/utils'
import { aiApi } from '@/api/admin'

// 响应式数据
const messagesContainer = ref(null)
 const currentChatId = ref('1')
const inputMessage = ref('')
const isTyping = ref(false)
const showPromptTemplates = ref(false)
const showHistory = ref(false)
const showSettings = ref(false)

// 对话历史
const chatHistory = ref([
  {
    id: '1',
    title: '关于简历优化的对话',
    lastTime: new Date('2024-03-15T10:30:00')
  },
  {
    id: '2',
    title: '面试技巧咨询',
    lastTime: new Date('2024-03-14T16:20:00')
  }
])

// 消息列表
const messages = ref([
  {
    id: '1',
    type: 'ai',
    content: '您好！我是AI智能助手，可以帮助您解答关于求职、简历、面试等方面的任何问题。请问有什么可以帮助您的吗？',
    timestamp: new Date('2024-03-15T10:30:00'),
    liked: false
  },
  {
    id: '2',
    type: 'user',
    content: '我想了解一下如何优化我的技术简历',
    timestamp: new Date('2024-03-15T10:31:00')
  },
  {
    id: '3',
    type: 'ai',
    content: '很乐意帮助您优化技术简历！以下是一些关键建议：\n\n**1. 突出技术技能**\n- 列出您熟悉的编程语言和框架\n- 标注熟练程度和项目经验\n\n**2. 项目经验部分**\n- 使用STAR法则描述项目\n- 量化您的贡献和成果\n- 突出使用的技术栈\n\n**3. 工作经历**\n- 按时间倒序排列\n- 重点描述技术成就\n- 使用行业关键词\n\n您希望我详细讲解哪个方面呢？',
    timestamp: new Date('2024-03-15T10:32:00'),
    liked: true
  }
])

// 提示词模板
const promptTemplates = ref([
  {
    id: '1',
    title: '简历优化',
    desc: '获取简历改进建议'
  },
  {
    id: '2',
    title: '面试准备',
    desc: '面试常见问题和技巧'
  },
  {
    id: '3',
    title: '职业规划',
    desc: '职业发展建议'
  },
  {
    id: '4',
    title: '薪资谈判',
    desc: '薪资谈判策略'
  }
])

// 设置
const settings = reactive({
  model: 'gpt-3.5-turbo',
  temperature: 0.7,
  maxTokens: 2000,
  contextLength: 10
})

// 键盘事件处理
const handleKeyDown = (e: KeyboardEvent) => {
  if (e.key === 'Enter' && !e.shiftKey && !isTyping.value) {
    e.preventDefault()
    sendMessage()
  }
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim() || isTyping.value) return
  
  const userMessage = {
    id: Date.now().toString(),
    type: 'user',
    content: inputMessage.value.trim(),
    timestamp: new Date()
  }
  
  messages.value.push(userMessage)
  const messageText = inputMessage.value
  inputMessage.value = ''
  
  // 滚动到底部
  await scrollToBottom()
  
  // 调用后端AI接口
  isTyping.value = true
  let streamingContent = ''
  
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
        'Admin-Authorization': `Admin ${token}`
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

    // 创建AI消息对象（用于实时更新）
    const aiMessageId = Date.now().toString()
    messages.value.push({
      id: aiMessageId,
      type: 'ai',
      content: '',
      timestamp: new Date(),
      liked: false
    })

    while (true) {
      const { done, value } = await reader.read()
      
      if (done) {
        console.log('✅ [AI Chat] SSE流接收完成')
        break
      }
      
      // 解码数据块
      const chunk = decoder.decode(value, { stream: true })
      console.log('📦 [AI Chat] 接收数据块:', chunk)
      
      // SSE格式：data: {...}\n\n
      const lines = chunk.split('\n')
      
      for (const line of lines) {
        if (line.startsWith('data:')) {
          const data = line.substring(5).trim()
          
          if (data === '[DONE]') {
            console.log('✅ [AI Chat] 流式传输完成标记')
            continue
          }
          
          try {
            // 尝试解析JSON
            const parsed = JSON.parse(data)
            console.log('📊 [AI Chat] 解析数据:', parsed)
            
            // 提取实际内容
            if (parsed.data) {
              streamingContent += parsed.data
            } else if (typeof parsed === 'string') {
              streamingContent += parsed
            }
          } catch (e) {
            // 如果不是JSON，直接追加
            if (data && data !== '') {
              streamingContent += data
            }
          }
          
          // 实时更新最后一条AI消息
          const lastMessage = messages.value[messages.value.length - 1]
          if (lastMessage && lastMessage.id === aiMessageId) {
            lastMessage.content = streamingContent
          }
          
          scrollToBottom()
        }
      }
    }

    isTyping.value = false
    scrollToBottom()

  } catch (error) {
    console.error('❌ [AI Chat] 发送消息失败:', error)
    ElMessage.error('发送失败，请稍后重试')
    isTyping.value = false
    
    // 删除空的AI消息
    if (messages.value[messages.value.length - 1]?.content === '') {
      messages.value.pop()
    }
  }
}

// 生成AI回复
const generateAIResponse = (userMessage: string) => {
  const responses = [
    '我理解您的问题。基于您的需求，我建议您可以从以下几个方面来考虑：\n1. 首先评估现状\n2. 制定明确目标\n3. 分步骤实施\n4. 定期复盘优化\n\n具体到您的情况，我建议...',
    '这是一个很好的问题！让我为您详细分析一下：\n\n**核心要点：**\n- 重点关注实际效果\n- 考虑长期发展\n- 平衡当前需求与未来规划\n\n**实施建议：**\n您可以按照以下步骤来推进...',
    '感谢您的信任！针对您提出的问题，我想分享一些专业见解：\n\n📊 **数据分析**\n根据当前市场趋势和最佳实践...\n\n🎯 **具体建议**\n1. 行动第一：...\n2. 优化流程：...\n3. 持续改进：...\n\n如果您需要更详细的指导，请告诉我！'
  ]
  
  return responses[Math.floor(Math.random() * responses.length)]
}

// 切换对话
const switchChat = (chatId: string) => {
  currentChatId.value = chatId
  // 这里应该加载对应的消息记录
  ElMessage.info('切换到其他对话')
}

// 开始新对话
const startNewChat = () => {
  const newChat = {
    id: Date.now().toString(),
    title: `新对话 ${chatHistory.value.length + 1}`,
    lastTime: new Date()
  }
  chatHistory.value.unshift(newChat)
  currentChatId.value = newChat.id
  messages.value = []
  
  // 添加欢迎消息
  messages.value.push({
    id: 'welcome',
    type: 'ai',
    content: '您好！我是AI智能助手，可以帮您解答关于求职、简历、面试等方面的问题。请告诉我您需要什么帮助？',
    timestamp: new Date(),
    liked: false
  })
}

// 使用模板
const useTemplate = (template: any) => {
  inputMessage.value = `请帮我${template.title}`
  showPromptTemplates.value = false
}

// 复制消息
const copyMessage = (content: string) => {
  navigator.clipboard.writeText(content.replace(/<[^>]*>/g, ''))
  ElMessage.success('已复制到剪贴板')
}

// 点赞消息
const likeMessage = (messageId: string) => {
  const message = messages.value.find(m => m.id === messageId)
  if (message) {
    message.liked = !message.liked
    ElMessage.success(message.liked ? '已点赞' : '已取消点赞')
  }
}

// 重新生成消息
const regenerateMessage = (messageId: string) => {
  const messageIndex = messages.value.findIndex(m => m.id === messageId)
  if (messageIndex > 0) {
    const prevMessage = messages.value[messageIndex - 1]
    if (prevMessage.type === 'user') {
      // 删除当前AI回复并重新生成
      messages.value.splice(messageIndex, 1)
      inputMessage.value = prevMessage.content
      sendMessage()
    }
  }
}

// 清空当前对话
const clearCurrentChat = () => {
  messages.value = []
  ElMessage.success('对话已清空')
}

// 导出对话
const exportChat = () => {
  const chatContent = messages.value
    .map(msg => `${msg.type === 'user' ? '用户' : 'AI'}: ${msg.content.replace(/<[^>]*>/g, '')}`)
    .join('\n\n')
  
  const blob = new Blob([chatContent], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `AI对话_${formatDateTime(new Date())}.txt`
  a.click()
  URL.revokeObjectURL(url)
  
  ElMessage.success('对话已导出')
}

// 处理下拉菜单命令
const handleCommand = (command: string) => {
  switch (command) {
    case 'export':
      exportChat()
      break
    case 'clear':
      clearCurrentChat()
      break
    case 'settings':
      showSettings.value = true
      break
  }
}

// 保存设置
const saveSettings = () => {
  // 保存设置到本地存储
  localStorage.setItem('ai-chat-settings', JSON.stringify(settings))
  showSettings.value = false
  ElMessage.success('设置已保存')
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 加载设置
const loadSettings = () => {
  const savedSettings = localStorage.getItem('ai-chat-settings')
  if (savedSettings) {
    Object.assign(settings, JSON.parse(savedSettings))
  }
}

// 组件挂载
onMounted(() => {
  loadSettings()
  scrollToBottom()
})
</script>

<style scoped lang="scss">
.ai-chat {
  height: calc(100vh - 120px);
  background: #f5f5f5;
}

.chat-container {
  display: flex;
  height: 100%;
  background: white;
  margin: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.chat-sidebar {
  width: 300px;
  border-right: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  h3 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
  }
}

.chat-history {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

.chat-item {
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s ease;
  margin-bottom: 8px;
  
  &:hover {
    background: #f9fafb;
  }
  
  &.active {
    background: #eff6ff;
    border-left: 3px solid #3b82f6;
  }
  
  .chat-title {
    font-size: 14px;
    font-weight: 500;
    color: #1f2937;
    margin-bottom: 4px;
  }
  
  .chat-time {
    font-size: 12px;
    color: #6b7280;
  }
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 20px;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.ai-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.ai-info {
  h4 {
    margin: 0 0 4px 0;
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
  }
  
  .ai-status {
    font-size: 12px;
    color: #22c55e;
  }
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #fafafa;
}

.message-item {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  
  &.user-message {
    flex-direction: row-reverse;
    
    .message-content {
      background: #3b82f6;
      color: white;
    }
  }
}

.message-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  flex-shrink: 0;
  
  .ai-message & {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
  }
  
  .user-message & {
    background: #e5e7eb;
    color: #6b7280;
  }
}

.message-content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 12px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.message-text {
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 8px;
  word-wrap: break-word;
}

.message-time {
  font-size: 12px;
  opacity: 0.7;
}

.message-actions {
  display: flex;
  gap: 4px;
  margin-top: 8px;
  
  .el-button {
    color: #6b7280;
    
    &:hover {
      color: #3b82f6;
    }
  }
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 8px 0;
  
  span {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #6b7280;
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
    transform: translateY(0);
    opacity: 0.4;
  }
  30% {
    transform: translateY(-10px);
    opacity: 1;
  }
}

.chat-input {
  border-top: 1px solid #e5e7eb;
  background: white;
}

.input-toolbar {
  padding: 12px 20px;
  border-bottom: 1px solid #f3f4f6;
  display: flex;
  gap: 12px;
  
  .el-button {
    color: #6b7280;
    
    &:hover {
      color: #3b82f6;
    }
  }
}

.prompt-templates {
  padding: 16px 20px;
  border-bottom: 1px solid #f3f4f6;
  background: #f9fafb;
}

.templates-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
}

.template-item {
  padding: 12px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    background: #eff6ff;
    border-color: #3b82f6;
  }
  
  h5 {
    margin: 0 0 4px 0;
    font-size: 14px;
    font-weight: 500;
    color: #1f2937;
  }
  
  p {
    margin: 0;
    font-size: 12px;
    color: #6b7280;
  }
}

.input-area {
  padding: 16px 20px;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.input-count {
  font-size: 12px;
  color: #6b7280;
}

// 响应式设计
@media (max-width: 768px) {
  .chat-container {
    margin: 10px;
    flex-direction: column;
    height: calc(100vh - 60px);
  }
  
  .chat-sidebar {
    width: 100%;
    border-right: none;
    border-bottom: 1px solid #e5e7eb;
  }
  
  .message-content {
    max-width: 85%;
  }
  
  .templates-grid {
    grid-template-columns: 1fr;
  }
}
</style>
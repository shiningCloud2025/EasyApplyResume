<template>
  <div class="ai-agent">
    <div class="agent-header">
      <div class="header-info">
        <h1>AI智能体助手</h1>
        <p>专业化的AI智能代理，为特定任务提供智能服务</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="createAgent">
          <i class="el-icon-plus"></i>
          创建智能体
        </el-button>
      </div>
    </div>

    <div class="agents-grid">
      <div 
        v-for="agent in agents" 
        :key="agent.id"
        class="agent-card"
        @click="selectAgent(agent)"
      >
        <div class="agent-avatar" :style="{ background: agent.color }">
          <i :class="agent.icon"></i>
        </div>
        <div class="agent-info">
          <h3>{{ agent.name }}</h3>
          <p>{{ agent.description }}</p>
          <div class="agent-stats">
            <span><i class="el-icon-user"></i> {{ agent.users }}</span>
            <span><i class="el-icon-data-line"></i> {{ agent.tasks }}</span>
          </div>
        </div>
        <div class="agent-status">
          <el-tag :type="agent.status === 'active' ? 'success' : 'warning'">
            {{ agent.status === 'active' ? '已激活' : '未激活' }}
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 智能体对话界面 -->
    <el-dialog
      v-model="showAgentDialog"
      :title="selectedAgent?.name"
      width="900px"
      class="agent-dialog"
    >
      <div class="agent-chat">
        <div class="chat-messages">
          <div 
            v-for="message in messages" 
            :key="message.id"
            class="message-item"
            :class="{ 'user-message': message.type === 'user', 'agent-message': message.type === 'agent' }"
          >
            <div class="message-avatar">
              <i v-if="message.type === 'user'" class="el-icon-user-solid"></i>
              <i v-else class="el-icon-cpu"></i>
            </div>
            <div class="message-content">
              <div class="message-text">{{ message.content }}</div>
              <div class="message-time">{{ formatTime(message.timestamp) }}</div>
            </div>
          </div>
          
          <div v-if="isProcessing" class="message-item agent-message">
            <div class="message-avatar">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="message-content">
              <div class="processing-indicator">
                <div class="dot"></div>
                <div class="dot"></div>
                <div class="dot"></div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="chat-input">
          <el-input
            v-model="inputMessage"
            placeholder="向智能体提问..."
            @keyup.enter="sendMessage"
          >
            <template #append>
              <el-button @click="sendMessage" :loading="isProcessing">发送</el-button>
            </template>
          </el-input>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const showAgentDialog = ref(false)
const selectedAgent = ref(null)
const inputMessage = ref('')
const isProcessing = ref(false)

const agents = ref([
  {
    id: 1,
    name: '简历优化专家',
    description: '专门提供简历优化和改进建议的AI智能体',
    icon: 'el-icon-edit-outline',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    status: 'active',
    users: 1250,
    tasks: 8900
  },
  {
    id: 2,
    name: '面试教练',
    description: '提供面试技巧培训和模拟面试的专业AI智能体',
    icon: 'el-icon-chat-dot-round',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    status: 'active',
    users: 980,
    tasks: 6700
  },
  {
    id: 3,
    name: '职业规划师',
    description: '协助用户制定长期职业发展路径的专业AI智能体',
    icon: 'el-icon-location-outline',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    status: 'inactive',
    users: 456,
    tasks: 2300
  },
  {
    id: 4,
    name: '数据分析专家',
    description: '专业处理和解析就业市场数据的AI智能体',
    icon: 'el-icon-data-analysis',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    status: 'active',
    users: 720,
    tasks: 4500
  }
])

const messages = ref([
  {
    id: 1,
    type: 'agent',
    content: '您好！我是简历优化专家，我可以帮您：\n• 分析简历结构和内容\n• 提供优化建议\n• 根据目标职位调整重点\n• 改进语言表达和格式\n\n请问有什么可以帮助您的吗？',
    timestamp: new Date('2024-03-15T10:30:00')
  }
])

const selectAgent = (agent: any) => {
  selectedAgent.value = agent
  messages.value = [{
    id: 1,
    type: 'agent',
    content: `您好！我是${agent.name}，${agent.description}\n\n请问有什么可以帮助您的吗？`,
    timestamp: new Date()
  }]
  showAgentDialog.value = true
}

const createAgent = () => {
  ElMessage.info('创建智能体功能开发中')
}

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isProcessing.value) return
  
  const userMessage = {
    id: Date.now(),
    type: 'user',
    content: inputMessage.value.trim(),
    timestamp: new Date()
  }
  
  messages.value.push(userMessage)
  const messageText = inputMessage.value
  inputMessage.value = ''
  
  isProcessing.value = true
  setTimeout(() => {
    const agentResponse = generateResponse(messageText, selectedAgent.value)
    messages.value.push({
      id: Date.now() + 1,
      type: 'agent',
      content: agentResponse,
      timestamp: new Date()
    })
    isProcessing.value = false
  }, 1500)
}

const generateResponse = (message: string, agent: any) => {
  const responses = {
    '简历优化专家': [
      '根据您的简历，我建议您突出以下要点：\n1. 量化工作成果\n2. 使用行业关键词\n3. 简化专业技能描述\n\n需要我详细分析吗？',
      '您的简历结构不错，但可以考虑增加项目经验的详细描述。要了解具体改进建议吗？'
    ],
    '面试教练': [
      '针对面试准备，建议您：\n• 熟悉STAR方法论\n• 准备常见面试问题\n• 模拟面试练习\n\n需要帮您准备具体问题吗？',
      '面试时保持自然很重要。我可以为您提供一些实用的面试技巧，需要了解吗？'
    ],
    '职业规划师': [
      '职业规划需要考虑长期目标和发展方向。让我们先分析您的现状和期望，制定合适的规划。',
      '职业发展需要平衡兴趣、能力和市场需求。我建议从自我评估开始，您准备好了吗？'
    ],
    '数据分析专家': [
      '根据当前市场数据分析，您关注领域的就业趋势良好。需要我为您展示详细的数据分析吗？',
      '我可以为您提供专业的市场数据分析和就业趋势预测。请问您关注哪个方向的数据？'
    ]
  }
  
  const agentResponses = responses[agent?.name] || ['我正在处理您的问题，请稍等...']
  return agentResponses[Math.floor(Math.random() * agentResponses.length)]
}

const formatTime = (date: Date) => {
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}
</script>

<style scoped lang="scss">
.ai-agent {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.agent-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
  
  h1 {
    font-size: 28px;
    font-weight: 700;
    color: #1f2937;
    margin-bottom: 8px;
  }
  
  p {
    font-size: 16px;
    color: #6b7280;
    margin: 0;
  }
}

.agents-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 24px;
}

.agent-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 25px rgba(0, 0, 0, 0.1);
    border-color: #3b82f6;
  }
  
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.agent-avatar {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.agent-info {
  flex: 1;
  
  h3 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 8px 0;
  }
  
  p {
    font-size: 14px;
    color: #6b7280;
    margin: 0 0 12px 0;
    line-height: 1.5;
  }
}

.agent-stats {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #6b7280;
  
  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

:deep(.agent-dialog) {
  .el-dialog__body {
    padding: 0 20px 20px 20px;
  }
}

.agent-chat {
  display: flex;
  flex-direction: column;
  height: 500px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  display: flex;
  gap: 12px;
  
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
  
  .agent-message & {
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
  white-space: pre-line;
}

.message-time {
  font-size: 12px;
  opacity: 0.7;
}

.processing-indicator {
  display: flex;
  gap: 4px;
  
  .dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #6b7280;
    animation: pulse 1.4s infinite;
    
    &:nth-child(2) {
      animation-delay: 0.2s;
    }
    
    &:nth-child(3) {
      animation-delay: 0.4s;
    }
  }
}

@keyframes pulse {
  0%, 60%, 100% {
    opacity: 0.4;
    transform: scale(1);
  }
  30% {
    opacity: 1;
    transform: scale(1.2);
  }
}

.chat-input {
  padding: 16px;
  border-top: 1px solid #e5e7eb;
}

@media (max-width: 768px) {
  .agent-header {
    flex-direction: column;
    gap: 16px;
  }
  
  .agents-grid {
    grid-template-columns: 1fr;
  }
}
</style>
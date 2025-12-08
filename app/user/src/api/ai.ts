import request from '@utils/request'

// AI助手相关API
export const aiAPI = {
  // AI应用对话(流式)
  applicationChat: (message: string, chatId?: string) => {
    return request.post('/user/aiResumeAssistant/application/chat', message, {
      params: { chatId },
      headers: {
        'Accept': 'text/event-stream'
      }
    })
  },

  // AI Agent对话(SSE)
  agentChat: (message: string, chatId?: string) => {
    return request.post('/user/aiResumeAssistant/agent/chat', message, {
      params: { chatId },
      headers: {
        'Accept': 'text/event-stream'
      }
    })
  }
}
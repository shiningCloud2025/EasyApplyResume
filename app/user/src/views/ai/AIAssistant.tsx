import React, { useState } from 'react'
import { Card, Input, Button, message, Spin } from 'antd'
import { SendOutlined, RobotOutlined, UserOutlined } from '@ant-design/icons'
import { aiAPI } from '@api/ai'
import type { ChatMessage } from '@types/index'

const { TextArea } = Input

const AIAssistant: React.FC = () => {
  const [loading, setLoading] = useState(false)
  const [inputValue, setInputValue] = useState('')
  const [chatId, setChatId] = useState<string>()
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: 1,
      type: 'assistant',
      content: '您好！我是您的AI求职助手，我可以帮助您：\n\n📝 优化简历内容\n💡 提供求职建议\n📊 分析就业市场\n🎯 制定职业规划\n\n请问有什么可以帮助您的吗？',
      timestamp: new Date().toISOString()
    }
  ])

  const handleSend = async () => {
    if (!inputValue.trim()) return

    const userMessage: ChatMessage = {
      id: Date.now(),
      type: 'user',
      content: inputValue,
      timestamp: new Date().toISOString()
    }

    setMessages(prev => [...prev, userMessage])
    const currentInput = inputValue
    setInputValue('')
    setLoading(true)

    try {
      // 调用真实的AI API
      const response = await aiAPI.applicationChat(currentInput, chatId)
      
      // 处理SSE流式响应
      const lines = response.data.split('\n')
      let aiContent = ''
      
      for (const line of lines) {
        if (line.startsWith('data: ')) {
          const data = line.slice(6)
          if (data === '[DONE]') {
            break
          }
          try {
            const parsed = JSON.parse(data)
            if (parsed.content) {
              aiContent += parsed.content
            }
            if (parsed.chatId) {
              setChatId(parsed.chatId)
            }
          } catch (e) {
            // 忽略解析错误
          }
        }
      }

      if (aiContent.trim()) {
        const aiMessage: ChatMessage = {
          id: Date.now() + 1,
          type: 'assistant',
          content: aiContent,
          timestamp: new Date().toISOString()
        }
        setMessages(prev => [...prev, aiMessage])
      } else {
        // 如果流式响应失败，使用默认回复
        const aiMessage: ChatMessage = {
          id: Date.now() + 1,
          type: 'assistant',
          content: getAIResponse(currentInput),
          timestamp: new Date().toISOString()
        }
        setMessages(prev => [...prev, aiMessage])
      }
    } catch (error) {
      console.error('AI API调用失败:', error)
      message.error('AI暂时无法响应，请稍后重试')
      
      // 发生错误时使用默认回复
      const aiMessage: ChatMessage = {
        id: Date.now() + 1,
        type: 'assistant',
        content: getAIResponse(currentInput),
        timestamp: new Date().toISOString()
      }
      setMessages(prev => [...prev, aiMessage])
    } finally {
      setLoading(false)
    }
  }

  const getAIResponse = (userInput: string): string => {
    const lowerInput = userInput.toLowerCase()
    
    if (lowerInput.includes('简历') || lowerInput.includes('cv')) {
      return `关于简历制作，我建议您注意以下几点：

🎯 **要点突出**
- 将最重要的技能和经验放在前面
- 使用数字和成果量化您的成就
- 避免使用模糊的描述

✨ **格式优化**
- 保持简洁，1-2页最佳
- 使用清晰的字体和适当的间距
- 确保没有语法和拼写错误

📊 **内容匹配**
- 根据应聘职位调整内容
- 突出与职位相关的技能和经历
- 研究公司文化和要求

需要我帮您检查具体的简历内容吗？`
    }
    
    if (lowerInput.includes('面试') || lowerInput.includes('interview')) {
      return `面试准备攻略：

🔍 **前期准备**
- 深入研究公司和职位要求
- 准备常见面试问题的答案
- 练习STAR法则的案例回答

💼 **面试当天**
- 提前10分钟到达
- 着装得体，保持礼貌
- 带上简历和其他必要文件

🎤 **面试技巧**
- 保持眼神交流
- 认真倾听问题
- 展现您的热情和学习能力
- 准备一些有深度的问题问到面试官

有具体的面试需要准备吗？`
    }
    
    if (lowerInput.includes('薪资') || lowerInput.includes('薪资谈判') || lowerInput.includes('salary')) {
      return `薪资谈判技巧：

💰 **前期调研**
- 了解行业同等职位的薪资范围
- 考虑城市生活成本
- 评估自身经验和能力价值

📈 **谈判策略**
- 不要第一个出价
- 给出薪资范围而不是具体数字
- 强调您能带来的价值

🎯 **综合考量**
- 不仅看基本工资
- 考虑奖金、股票、福利等
- 重视职业发展机会

需要我帮您分析具体的薪资情况吗？`
    }
    
    return `我理解您的问题。作为AI求职助手，我建议您：

📚 **持续学习**
- 不断提升专业技能
- 学习新技术和工具
- 保持对行业趋势的关注

🌟 **个人品牌**
- 完善LinkedIn等专业档案
- 积极参与行业活动
- 建立专业人脉网络

🚀 **求职策略**
- 制定明确的求职目标
- 多渠道投递简历
- 定期跟踪和反馈

还有其他我可以帮助您的吗？`
  }

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  return (
    <div className="ai-assistant-page">
      <Card className="chat-container" title="AI求职助手">
        <div className="chat-messages">
          {messages.map((message) => (
            <div
              key={message.id}
              className={`message ${message.type}`}
            >
              <div className="message-avatar">
                {message.type === 'user' ? 
                  <UserOutlined /> : 
                  <RobotOutlined />
                }
              </div>
              <div className="message-content">
                <div className="message-text">
                  {message.content.split('\n').map((paragraph, index) => (
                    <p key={index}>{paragraph}</p>
                  ))}
                </div>
                <div className="message-time">
                  {new Date(message.timestamp).toLocaleTimeString('zh-CN', {
                    hour: '2-digit',
                    minute: '2-digit'
                  })}
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="chat-input">
          <TextArea
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="请输入您的问题..."
            rows={3}
            disabled={loading}
          />
          <Button
            type="primary"
            icon={<SendOutlined />}
            onClick={handleSend}
            loading={loading}
            disabled={!inputValue.trim()}
          >
            发送
          </Button>
        </div>
      </Card>
    </div>
  )
}

interface ChatMessage {
  id: number
  type: 'user' | 'assistant'
  content: string
  timestamp: string
}

export default AIAssistant
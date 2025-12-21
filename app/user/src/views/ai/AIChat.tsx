import React, { useState, useRef, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, Input, Button, message } from 'antd'
import { SendOutlined, RobotOutlined, UserOutlined, DeleteOutlined } from '@ant-design/icons'
import { getToken } from '@utils/request'
import './AIChat.scss'

const { TextArea } = Input

interface Message {
  id: number
  role: 'user' | 'assistant'
  content: string
  timestamp: Date
}

const AIChat: React.FC = () => {
  const navigate = useNavigate()
  const [inputMessage, setInputMessage] = useState('')
  const [isStreaming, setIsStreaming] = useState(false)
  const [streamingContent, setStreamingContent] = useState('')
  const [messages, setMessages] = useState<Message[]>([
    {
      id: Date.now(),
      role: 'assistant',
      content: '您好！我是AI简历智能问答助手，我可以帮助您：\n\n📝 优化简历内容\n💡 提供求职建议\n📊 分析就业市场\n🎯 制定职业规划\n\n请问有什么可以帮助您的吗？',
      timestamp: new Date()
    }
  ])
  
  const messagesContainerRef = useRef<HTMLDivElement>(null)
  // 页面加载时生成 chatId，整个会话期间保持不变
  const currentChatIdRef = useRef<string>('chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9))
  const streamingTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null)
  const streamingContentRef = useRef<string>('')  // 追踪当前流式内容

  // 强制重置状态（保存已接收的内容）
  const forceResetState = () => {
    console.log('🔄 [强制重置] 状态')
    // 如果有已接收的内容，先保存到消息列表
    if (streamingContentRef.current) {
      const content = streamingContentRef.current
      setMessages(prev => [...prev, {
        id: Date.now(),
        role: 'assistant',
        content: content,
        timestamp: new Date()
      }])
    }
    setIsStreaming(false)
    setStreamingContent('')
    streamingContentRef.current = ''
    if (streamingTimeoutRef.current) {
      clearTimeout(streamingTimeoutRef.current)
      streamingTimeoutRef.current = null
    }
  }

  // 滚动到底部
  const scrollToBottom = () => {
    if (messagesContainerRef.current) {
      messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight
    }
  }

  useEffect(() => {
    scrollToBottom()
  }, [messages, streamingContent])

  // 处理Enter键
  const handleEnter = (event: React.KeyboardEvent) => {
    if (!event.shiftKey && event.key === 'Enter') {
      event.preventDefault()
      sendMessage()
    }
  }

  // 发送消息
  const sendMessage = async () => {
    if (!inputMessage.trim() || isStreaming) return

    const userMessage: Message = {
      id: Date.now(),
      role: 'user',
      content: inputMessage.trim(),
      timestamp: new Date()
    }

    setMessages(prev => [...prev, userMessage])
    const messageText = inputMessage.trim()
    setInputMessage('')
    
    // 开始流式接收
    setIsStreaming(true)
    setStreamingContent('')
    streamingContentRef.current = ''  // 重置 ref
    
    // 设置超时保护（10秒后强制重置，允许用户重新输入）
    if (streamingTimeoutRef.current) {
      clearTimeout(streamingTimeoutRef.current)
    }
    streamingTimeoutRef.current = setTimeout(() => {
      console.log('⚠️ [超时] 10秒后强制重置状态')
      forceResetState()
    }, 10000)

    let content = ''
    
    try {
      console.log('📤 [AI Chat] 发送消息:', messageText)
      
      const baseURL = '/api'
      const token = getToken()
      let url = `${baseURL}/user/aiResumeAssistant/application/chat?chatId=${encodeURIComponent(currentChatIdRef.current)}`
      
      const headers: Record<string, string> = {
        'Content-Type': 'text/plain',
        'Accept': 'text/event-stream'
      }
      if (token) {
        headers['User-Authorization'] = `User ${token}`
      }
      
      const response = await fetch(url, {
        method: 'POST',
        headers,
        body: messageText,
        credentials: 'include'
      })

      if (!response.ok) {
        if (response.status === 401) {
          message.warning('您尚未登录，请登录。')
          setTimeout(() => navigate('/auth/login'), 3000)
        }
        setIsStreaming(false)
        setStreamingContent('')
        if (response.status !== 401) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        return
      }

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
        
        buffer += decoder.decode(value, { stream: true })
        const lines = buffer.split('\n')
        buffer = lines.pop() || ''
        
        for (const line of lines) {
          if (line.startsWith('data:')) {
            const data = line.substring(5).trim()
            if (data === '[DONE]' || data === 'DONE' || !data) continue
            
            try {
              const parsed = JSON.parse(data)
              if (parsed.code !== undefined && parsed.code !== 200) {
                message.error(parsed.message || '服务器错误')
                setIsStreaming(false)
                setStreamingContent('')
                return
              }
              
              let chunk = parsed.data || parsed.content || parsed.message || ''
              if (typeof chunk !== 'string') chunk = JSON.stringify(chunk)
              if (chunk) {
                content += chunk
                streamingContentRef.current = content  // 同步更新 ref
                setStreamingContent(content)
              }
            } catch (e) {
              content += data
              streamingContentRef.current = content  // 同步更新 ref
              setStreamingContent(content)
            }
          }
        }
      }

      // 保存消息
      if (content) {
        streamingContentRef.current = ''  // 已保存，清空 ref 防止重复保存
        setMessages(prev => [...prev, {
          id: Date.now(),
          role: 'assistant',
          content: content,
          timestamp: new Date()
        }])
      }

    } catch (error: any) {
      console.error('❌ [AI Chat] 失败:', error)
      message.error('发送失败：' + (error.message || '请稍后重试'))
    }
    
    // 无论如何都重置状态
    console.log('✅ [AI Chat] 重置状态')
    forceResetState()
  }

  // 清空消息
  const clearMessages = () => {
    setMessages([
      {
        id: Date.now(),
        role: 'assistant',
        content: '您好！我是AI简历智能问答助手，我可以帮助您：\n\n📝 优化简历内容\n💡 提供求职建议\n📊 分析就业市场\n🎯 制定职业规划\n\n请问有什么可以帮助您的吗？',
        timestamp: new Date()
      }
    ])
    // 清空时生成新的 chatId，开启新会话
    currentChatIdRef.current = 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    message.success('对话已清空，已开启新会话')
  }

  // 格式化消息内容
  const formatMessage = (content: string) => {
    return content.split('\n').map((line, index) => (
      <p key={index}>{line || <br />}</p>
    ))
  }

  return (
    <div className="ai-chat-page">
      <div className="chat-container">
        {/* 头部 */}
        <div className="chat-header">
          <div className="header-info">
            <div className="avatar">
              <RobotOutlined />
            </div>
            <div className="info">
              <h3>AI简历智能问答助手</h3>
              <span>基于大语言模型，为您提供专业的求职建议</span>
            </div>
          </div>
          <Button 
            type="text" 
            icon={<DeleteOutlined />} 
            onClick={clearMessages}
            className="clear-btn"
          >
            清空对话
          </Button>
        </div>

        {/* 消息列表 */}
        <div className="chat-messages" ref={messagesContainerRef}>
          {messages.map((msg) => (
            <div key={msg.id} className={`message-item ${msg.role}-message`}>
              <div className="message-avatar">
                {msg.role === 'user' ? <UserOutlined /> : <RobotOutlined />}
              </div>
              <div className="message-content">
                <div className="message-text">
                  {formatMessage(msg.content)}
                </div>
                <div className="message-time">
                  {msg.timestamp.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })}
                </div>
              </div>
            </div>
          ))}
          
          {/* 流式输出中的消息 */}
          {isStreaming && streamingContent && (
            <div className="message-item assistant-message">
              <div className="message-avatar">
                <RobotOutlined />
              </div>
              <div className="message-content streaming">
                <div className="message-text">
                  {formatMessage(streamingContent)}
                </div>
              </div>
            </div>
          )}
          
          {/* 正在输入指示器 */}
          {isStreaming && !streamingContent && (
            <div className="message-item assistant-message">
              <div className="message-avatar">
                <RobotOutlined />
              </div>
              <div className="message-content">
                <div className="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* 输入区域 */}
        <div className="chat-input">
          <TextArea
            value={inputMessage}
            onChange={(e) => setInputMessage(e.target.value)}
            onKeyDown={handleEnter}
            placeholder="请输入您的问题..."
            rows={3}
            disabled={isStreaming}
          />
          <div className="input-actions">
            <div className="input-tips">
              按 Enter 发送，Shift+Enter 换行
            </div>
            <Button
              type="primary"
              icon={<SendOutlined />}
              onClick={sendMessage}
              loading={isStreaming}
              disabled={!inputMessage.trim()}
            >
              发送
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AIChat

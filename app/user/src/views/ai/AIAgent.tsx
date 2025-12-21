import React, { useState, useRef, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, Input, Button, message, Collapse } from 'antd'
import { SendOutlined, RobotOutlined, UserOutlined, DeleteOutlined, BulbOutlined, CheckCircleOutlined } from '@ant-design/icons'
import { getToken } from '@utils/request'
import './AIAgent.scss'

const { TextArea } = Input
const { Panel } = Collapse

interface ThinkingStep {
  stepNumber: number
  content: string
  type: 'thinking' | 'tool_call' | 'result'
}

interface Message {
  id: number
  role: 'user' | 'assistant'
  content: string
  timestamp: Date
  thinkingSteps?: ThinkingStep[]
  finalResult?: string
}

const AIAgent: React.FC = () => {
  const navigate = useNavigate()
  const [inputMessage, setInputMessage] = useState('')
  const [isStreaming, setIsStreaming] = useState(false)
  const [streamingContent, setStreamingContent] = useState('')
  const [streamingSteps, setStreamingSteps] = useState<ThinkingStep[]>([])
  const [messages, setMessages] = useState<Message[]>([
    {
      id: Date.now(),
      role: 'assistant',
      content: '您好！我是AI简历智能体助手，我具备更强的推理和执行能力，可以帮助您：\n\n🔍 深度分析简历问题\n🛠️ 调用工具执行任务\n📋 生成详细的改进方案\n🎯 提供个性化求职策略\n\n请告诉我您的需求，我会一步步为您分析和解决！',
      timestamp: new Date()
    }
  ])
  
  const messagesContainerRef = useRef<HTMLDivElement>(null)
  // 页面加载时生成 chatId，整个会话期间保持不变
  const currentChatIdRef = useRef<string>('chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9))
  const currentStepNumberRef = useRef(0)
  const streamingStepsRef = useRef<ThinkingStep[]>([])
  const streamingTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null)
  const streamingContentRef = useRef<string>('')  // 追踪当前流式内容

  // 强制重置状态（保存已接收的内容）
  const forceResetState = () => {
    console.log('🔄 [强制重置] 状态')
    // 如果有已接收的内容，先保存到消息列表
    if (streamingContentRef.current || streamingStepsRef.current.length > 0) {
      const content = streamingContentRef.current
      const currentSteps = [...streamingStepsRef.current]
      let finalResult = ''
      try {
        finalResult = extractFinalResult(currentSteps, content)
      } catch (e) {
        finalResult = content
      }
      setMessages(prev => [...prev, {
        id: Date.now(),
        role: 'assistant',
        content: content || '（响应超时）',
        thinkingSteps: currentSteps.length > 0 ? currentSteps : undefined,
        finalResult: finalResult || undefined,
        timestamp: new Date()
      }])
    }
    setIsStreaming(false)
    setStreamingContent('')
    setStreamingSteps([])
    streamingStepsRef.current = []
    streamingContentRef.current = ''
    currentStepNumberRef.current = 0
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
  }, [messages, streamingContent, streamingSteps])

  // 处理Enter键
  const handleEnter = (event: React.KeyboardEvent) => {
    if (!event.shiftKey && event.key === 'Enter') {
      event.preventDefault()
      sendMessage()
    }
  }

  // 解析步骤内容（改进版：支持多个步骤在同一段文本中）
  const parseStepContent = (content: string) => {
    // 匹配所有 "步骤 X: " 格式（全局匹配）- 与管理端对齐
    const stepRegex = /步骤\s*(\d+):\s*([^步]*?)(?=步骤\s*\d+:|$)/gs
    const matches = [...content.matchAll(stepRegex)]
    
    console.log('🔍 [解析] 找到', matches.length, '个步骤匹配')
    
    if (matches.length > 0) {
      matches.forEach(match => {
        const stepNum = parseInt(match[1])
        const stepContent = match[2].trim()
        
        console.log('🔍 [解析] 步骤', stepNum, '内容长度:', stepContent.length)
        
        if (stepNum > currentStepNumberRef.current) {
          currentStepNumberRef.current = stepNum
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
        setStreamingSteps(prev => {
          const existingIndex = prev.findIndex(s => s.stepNumber === stepNum)
          let newSteps: ThinkingStep[]
          if (existingIndex >= 0) {
            newSteps = [...prev]
            newSteps[existingIndex] = { stepNumber: stepNum, content: stepContent, type: stepType }
          } else {
            newSteps = [...prev, { stepNumber: stepNum, content: stepContent, type: stepType }]
          }
          // 同步更新 ref
          streamingStepsRef.current = newSteps
          return newSteps
        })
      })
    }
  }

  // 无意义的结果关键词
  const meaninglessKeywords = [
    '任务结束', '任务终止', '思考完成', '无需行动',
    'doTerminate', '执行结束', '达到最大步骤'
  ]

  // 检查内容是否无意义（包含doTerminate或太短）
  const isMeaningless = (content: string): boolean => {
    if (!content) return true
    const trimmed = content.trim()
    // 包含 doTerminate 的一定是无意义的
    if (trimmed.includes('doTerminate')) return true
    // 包含其他无意义关键词且长度较短
    return meaninglessKeywords.some(keyword => trimmed.includes(keyword)) && trimmed.length < 80
  }

  // 清理结果内容 - 提取简洁答案
  const cleanResult = (content: string): string => {
    try {
      if (!content) return ''
      let cleaned = content.trim()
      
      // 移除步骤前缀（步骤 1: / 步骤1：）
      cleaned = cleaned.replace(/^步骤\s*\d+[:：]\s*/g, '')
      // 移除"第五.完善期——"这样的前缀
      cleaned = cleaned.replace(/^第[一二三四五六七八九十\d]+[.．:：]?[^。，\n]{0,20}——/g, '')
      
      // 移除 JSON 转义
      cleaned = cleaned.replace(/\\"/g, '"')
      cleaned = cleaned.replace(/\\n/g, '\n')
      cleaned = cleaned.replace(/^\{+|\}+$/g, '')
      
      // 移除 doTerminate 相关内容
      cleaned = cleaned
        .split('\n')
        .filter(line => !line.includes('doTerminate') && !line.includes('任务结束') && !line.includes('步骤 '))
        .join('\n')
        .trim()
      
      // 限制长度
      if (cleaned.length > 500) {
        cleaned = cleaned.substring(0, 500) + '...'
      }
      return cleaned
    } catch (e) {
      console.error('cleanResult 异常:', e)
      return content || ''
    }
  }

  // 从搜索结果中提取摘要
  const extractSearchSummary = (content: string): string => {
    try {
      // 检查是否是文件写入结果
      if (content.includes('File written successfully')) {
        const filePathMatch = content.match(/File written successfully to:\s*(.+)/)
        if (filePathMatch) {
          const filePath = filePathMatch[1].trim()
          const fileName = filePath.split(/[\/\\]/).pop() || filePath
          return `已生成文件: ${fileName}`
        }
      }
      
      // 尝试找到 JSON 中的 snippet 字段
      const snippetMatches = content.match(/"snippet":\s*"([^"]{50,500})"/g)
      if (snippetMatches && snippetMatches.length > 0) {
        let snippet = snippetMatches[0]
          .replace(/"snippet":\s*"/, '')
          .replace(/"$/, '')
          .replace(/\\n/g, '\n')
          .replace(/\\"/g, '"')
        
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

  // 提取最终结果（智能版 - 只返回简洁答案）
  const extractFinalResult = (steps: ThinkingStep[], fullContent: string): string => {
    try {
      console.log('🔍 [提取结果] 步骤数:', steps.length, '内容长度:', fullContent?.length || 0)
      
      // 1. 优先从步骤中找到有意义的内容（排除doTerminate步骤）
      const meaningfulSteps = steps.filter(s => s.content && !isMeaningless(s.content))
      console.log('🔍 [提取结果] 有意义步骤数:', meaningfulSteps.length)
      
      if (meaningfulSteps.length > 0) {
        // 取第一个有意义的步骤（通常是答案）
        const firstMeaningful = meaningfulSteps[0]
        console.log('🔍 [提取结果] ✅ 使用步骤', firstMeaningful.stepNumber)
        
        // 提取答案核心内容
        let answer = firstMeaningful.content
        
        // 如果内容包含"欢迎继续提问"，截取之前的内容
        const cutIndex = answer.indexOf('如果你还有其他问题')
        if (cutIndex > 0) {
          answer = answer.substring(0, cutIndex)
        }
        
        return cleanResult(answer)
      }
      
      // 2. 从完整内容中提取（排除doTerminate部分）
      if (fullContent) {
        return cleanResult(fullContent)
      }
      
      console.log('🔍 [提取结果] ❌ 未找到有意义的结果')
      return ''
    } catch (e) {
      console.error('提取结果异常:', e)
      return cleanResult(fullContent) || ''
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
    setStreamingSteps([])
    currentStepNumberRef.current = 0
    streamingContentRef.current = ''  // 重置 ref
    
    // 设置超时保护（20秒后强制重置，允许用户重新输入）
    if (streamingTimeoutRef.current) {
      clearTimeout(streamingTimeoutRef.current)
    }
    streamingTimeoutRef.current = setTimeout(() => {
      console.log('⚠️ [超时] 20秒后强制重置状态')
      forceResetState()
    }, 20000)

    try {
      console.log('📤 [AI Agent] 发送消息:', messageText)
      
      // 构建请求URL
      const baseURL = '/api'
      const token = getToken()
      
      let url = `${baseURL}/user/aiResumeAssistant/agent/chat?chatId=${encodeURIComponent(currentChatIdRef.current)}`
      
      console.log('📤 [AI Agent] 请求URL:', url)
      
      // 使用fetch API处理SSE流式响应
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'text/plain',
          'User-Authorization': `User ${token}`,
          'Accept': 'text/event-stream'
        },
        body: messageText
      })

      if (!response.ok) {
        if (response.status === 401) {
          message.warning('您尚未登录，请登录。')
          setTimeout(() => navigate('/auth/login'), 3000)
        }
        forceResetState()
        if (response.status !== 401) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        return
      }

      console.log('📥 [AI Agent] 开始接收SSE流...')
      
      // 读取响应流
      const reader = response.body?.getReader()
      const decoder = new TextDecoder()
      
      if (!reader) {
        throw new Error('无法获取响应流')
      }

      let buffer = ''
      let content = ''

      while (true) {
        const { done, value } = await reader.read()
        
        if (done) {
          console.log('✅ [AI Agent] SSE流接收完成')
          break
        }
        
        // 解码数据块
        buffer += decoder.decode(value, { stream: true })
        
        // 按行处理SSE数据
        const lines = buffer.split('\n')
        buffer = lines.pop() || ''
        
        for (const line of lines) {
          // 处理事件类型
          if (line.startsWith('event:')) {
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
              const parsed = JSON.parse(data)
              
              if (parsed.code !== undefined && parsed.code !== 200) {
                console.error('❌ [AI Agent] 服务器返回错误:', parsed)
                message.error(parsed.message || '服务器错误')
                forceResetState()
                return
              }
              
              let chunk = ''
              if (parsed.data) {
                chunk = typeof parsed.data === 'string' ? parsed.data : JSON.stringify(parsed.data)
              } else if (parsed.content) {
                chunk = parsed.content
              } else if (typeof parsed === 'string') {
                chunk = parsed
              }
              
              if (chunk) {
                parseStepContent(chunk)
                content += chunk
                streamingContentRef.current = content  // 同步更新 ref
                setStreamingContent(content)
              }
            } catch (e) {
              // 如果不是JSON，直接追加文本
              parseStepContent(data)
              content += data
              streamingContentRef.current = content  // 同步更新 ref
              setStreamingContent(content)
            }
          }
        }
      }

      // 流式传输完成，保存消息
      console.log('✅ [AI Agent] 流结束，开始保存消息')
      
      // 从 ref 获取最新的步骤状态
      const currentSteps = [...streamingStepsRef.current]
      console.log('✅ [AI Agent] 当前步骤数:', currentSteps.length)
      
      if (content || currentSteps.length > 0) {
        // 已保存，清空 ref 防止 finally 中重复保存
        streamingContentRef.current = ''
        streamingStepsRef.current = []
        
        let finalResult = ''
        try {
          finalResult = extractFinalResult(currentSteps, content)
        } catch (e) {
          console.error('提取结果失败:', e)
          finalResult = content
        }
        
        setMessages(prev => [...prev, {
          id: Date.now(),
          role: 'assistant',
          content: content,
          thinkingSteps: currentSteps.length > 0 ? currentSteps : undefined,
          finalResult: finalResult || undefined,
          timestamp: new Date()
        }])
        console.log('✅ [AI Agent] 消息已保存')
      } else {
        console.warn('⚠️ [AI Agent] 未接收到任何内容')
        message.warning('未接收到AI响应')
      }

    } catch (error: any) {
      console.error('❌ [AI Agent] 发送消息失败:', error)
      message.error('发送失败：' + (error.message || '请稍后重试'))
    } finally {
      // 确保状态一定会重置
      console.log('✅ [AI Agent] finally 块执行，重置状态')
      forceResetState()
    }
  }

  // 清空消息
  const clearMessages = () => {
    setMessages([
      {
        id: Date.now(),
        role: 'assistant',
        content: '您好！我是AI简历智能体助手，我具备更强的推理和执行能力，可以帮助您：\n\n🔍 深度分析简历问题\n🛠️ 调用工具执行任务\n📋 生成详细的改进方案\n🎯 提供个性化求职策略\n\n请告诉我您的需求，我会一步步为您分析和解决！',
        timestamp: new Date()
      }
    ])
    // 清空时生成新的 chatId，开启新会话
    currentChatIdRef.current = 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    message.success('对话已清空，已开启新会话')
  }

  // 格式化步骤内容（清理JSON格式）
  const formatStepContent = (content: string): React.ReactNode => {
    let formatted = content
    
    // 特殊处理：文件写入成功的消息
    if (formatted.includes('File written successfully')) {
      const filePathMatch = formatted.match(/File written successfully to:\s*(.+)/)
      if (filePathMatch) {
        const filePath = filePathMatch[1].trim()
        const fileName = filePath.split(/[\/\\]/).pop() || filePath
        return <span>✅ 文件已保存: <code>{fileName}</code></span>
      }
    }
    
    // 特殊处理：搜索结果
    if (formatted.includes('searchWeb')) {
      formatted = formatted.replace('工具 searchWeb 完成了它的任务！结果: ', '🔍 搜索结果：')
    }
    
    // 特殊处理：网页爬取
    if (formatted.includes('scrapeWebPage')) {
      formatted = formatted.replace('工具 scrapeWebPage 完成了它的任务！结果: ', '🌐 网页内容：')
    }
    
    // 移除 JSON 转义
    formatted = formatted.replace(/\\"/g, '"')
    formatted = formatted.replace(/\\n/g, '\n')
    
    // 尝试提取关键信息
    try {
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
    
    // 限制长度并分行显示
    if (formatted.length > 500) {
      formatted = formatted.substring(0, 500) + '...'
    }
    
    return formatted.split('\n').map((line, i) => <p key={i}>{line || <br />}</p>)
  }

  // 格式化消息内容（支持 Markdown 和智能分段）
  const formatMessage = (content: string): React.ReactNode => {
    // 先清理 JSON 转义字符
    let cleaned = content
      .replace(/\\"/g, '"')
      .replace(/\\n/g, '\n')
    
    // 如果内容是原始 JSON，尝试提取有用信息
    if (cleaned.includes('"snippet"') || cleaned.includes('"title"')) {
      const extracted = extractSearchSummary(cleaned)
      if (extracted) {
        cleaned = extracted
      }
    }
    
    // 移除过多的 JSON 结构标记
    cleaned = cleaned.replace(/^\{+|\}+$/g, '')
    
    // 分行显示
    return cleaned.split('\n').map((line, index) => (
      <p key={index}>{line || <br />}</p>
    ))
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

  // 获取步骤预览
  const getStepPreview = (content: string): string => {
    const cleaned = content.replace(/[{}"\\]/g, '').trim()
    return cleaned.length > 60 ? cleaned.substring(0, 60) + '...' : cleaned
  }

  return (
    <div className="ai-agent-page">
      <div className="chat-container">
        {/* 头部 */}
        <div className="chat-header">
          <div className="header-info">
            <div className="avatar">
              <RobotOutlined />
            </div>
            <div className="info">
              <h3>AI简历智能体助手</h3>
              <span>具备推理和工具调用能力，为您深度分析和执行任务</span>
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
                {/* 思考步骤展示 - 点击每个步骤可展开 */}
                {msg.thinkingSteps && msg.thinkingSteps.length > 0 && (
                  <Collapse className="thinking-steps" ghost>
                    <Panel 
                      header={
                        <div className="steps-header">
                          <BulbOutlined />
                          <span>查看思考过程 ({msg.thinkingSteps.length} 步)</span>
                        </div>
                      } 
                      key="1"
                    >
                      <Collapse ghost accordion>
                        {msg.thinkingSteps.map((step) => (
                          <Panel
                            key={step.stepNumber}
                            header={
                              <div className="step-header-inline">
                                <span className="step-num">步骤 {step.stepNumber}</span>
                                <span className="step-type-tag">{getStepTypeLabel(step.type)}</span>
                                <span className="step-preview-text">{getStepPreview(step.content)}</span>
                              </div>
                            }
                          >
                            <div className="step-detail">{formatStepContent(step.content)}</div>
                          </Panel>
                        ))}
                      </Collapse>
                    </Panel>
                  </Collapse>
                )}
                
                {/* 最终结果或普通内容 */}
                {msg.thinkingSteps && msg.thinkingSteps.length > 0 ? (
                  // 有思考步骤时，显示最终结果
                  <div className="final-result">
                    <div className="result-header">
                      <CheckCircleOutlined />
                      <span>回答</span>
                    </div>
                    <div className="result-content">
                      {formatMessage(msg.finalResult || msg.content)}
                    </div>
                  </div>
                ) : (
                  // 普通消息
                  <div className="message-text">
                    {formatMessage(msg.content)}
                  </div>
                )}
                
                <div className="message-time">
                  {msg.timestamp.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })}
                </div>
              </div>
            </div>
          ))}
          
          {/* 流式输出中的消息 */}
          {isStreaming && (
            <div className="message-item assistant-message">
              <div className="message-avatar">
                <RobotOutlined />
              </div>
              <div className="message-content streaming">
                {/* 显示当前思考步骤 */}
                {streamingSteps.length > 0 && (
                  <div className="streaming-steps">
                    <div className="streaming-header">
                      <BulbOutlined />
                      <span>正在思考...</span>
                    </div>
                    {streamingSteps.map((step) => (
                      <div key={step.stepNumber} className="streaming-step">
                        <span className="step-num">{step.stepNumber}</span>
                        <span className="step-preview">{getStepPreview(step.content)}</span>
                      </div>
                    ))}
                  </div>
                )}
                
                {streamingContent ? (
                  <div className="message-text">
                    {formatMessage(streamingContent)}
                  </div>
                ) : (
                  <div className="typing-indicator">
                    <span></span>
                    <span></span>
                    <span></span>
                  </div>
                )}
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
            placeholder="请输入您的问题，我会一步步分析..."
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

export default AIAgent

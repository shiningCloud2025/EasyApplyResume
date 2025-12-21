import React, { useState } from 'react'
import { 
  Card, 
  Form, 
  Input, 
  Button, 
  message,
  Space,
  Alert
} from 'antd'
import { MessageOutlined, SendOutlined, ReloadOutlined } from '@ant-design/icons'
import { feedbackAPI } from '@api/feedback'
import { useUserStore } from '@stores/userStore'
import { useNavigate } from 'react-router-dom'
import './FeedbackSubmit.scss'

// 后端接口需要的字段
interface UserFeedbackForm {
  userFeedbackTitle: string
  userFeedbackContent: string
  userFeedbackUserId: number
}

const FeedbackSubmit: React.FC = () => {
  const [form] = Form.useForm()
  const { user, isLoggedIn } = useUserStore()
  const navigate = useNavigate()
  const [submitting, setSubmitting] = useState(false)
  const isSubmittingRef = React.useRef(false)  // 防止重复提交

  const handleSubmit = async (values: any) => {
    // 防止重复提交（React StrictMode 会触发两次）
    if (isSubmittingRef.current) {
      console.log('⚠️ [反馈提交] 正在提交中，跳过重复请求')
      return
    }
    isSubmittingRef.current = true
    console.log('📝 [反馈提交] 开始提交...')
    console.log('📝 [反馈提交] isLoggedIn:', isLoggedIn)
    console.log('📝 [反馈提交] user:', user)
    console.log('📝 [反馈提交] values:', values)
    
    if (!isLoggedIn || !user) {
      message.warning('请先登录')
      navigate('/auth/login')
      return
    }

    // 检查 userId 是否有效
    if (!user.userId) {
      console.error('❌ [反馈提交] userId 无效:', user.userId)
      message.error('用户信息异常，请重新登录')
      return
    }

    // 构建符合后端接口的数据格式
    const feedbackData: UserFeedbackForm = {
      userFeedbackTitle: values.title,
      userFeedbackContent: values.content,
      userFeedbackUserId: user.userId
    }

    console.log('📤 [反馈提交] 提交数据:', JSON.stringify(feedbackData))

    try {
      setSubmitting(true)
      await feedbackAPI.addFeedback(feedbackData)
      
      message.success('反馈提交成功！我们会尽快处理')
      form.resetFields()
      
      // 提示用户
      setTimeout(() => {
        message.info('可在“我的反馈”中查看处理进度')
      }, 1000)
    } catch (error: any) {
      console.error('❌ [反馈提交] 提交失败:', error)
      message.error('提交失败，请稍后重试')
    } finally {
      setSubmitting(false)
      isSubmittingRef.current = false  // 重置提交状态
    }
  }

  const handleReset = () => {
    form.resetFields()
  }

  // 未登录提示
  if (!isLoggedIn) {
    return (
      <div className="feedback-submit">
        <Card className="feedback-card">
          <div className="login-required">
            <MessageOutlined style={{ fontSize: 48, color: '#999' }} />
            <h2>请先登录</h2>
            <p>登录后才能提交反馈</p>
            <Button type="primary" onClick={() => navigate('/auth/login')}>
              去登录
            </Button>
          </div>
        </Card>
      </div>
    )
  }

  return (
    <div className="feedback-submit">
      <Card className="feedback-card">
        {/* 页面标题 */}
        <div className="page-header">
          <div className="header-icon">
            <MessageOutlined style={{ fontSize: 40, color: '#1890ff' }} />
          </div>
          <div className="header-text">
            <h1>意见反馈</h1>
            <p>您的反馈对我们非常重要，帮助我们改进产品和服务</p>
          </div>
        </div>

        {/* 提示信息 */}
        <Alert
          message="温馨提示：请详细描述您遇到的问题或建议，我们会在1-3个工作日内处理您的反馈"
          type="info"
          showIcon
          className="tip-alert"
        />

        {/* 反馈表单 */}
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          className="feedback-form"
        >
          <Form.Item
            label="反馈标题"
            name="title"
            rules={[
              { required: true, message: '请输入反馈标题' },
              { max: 35, message: '标题长度不能超过 35 个字符' }
            ]}
          >
            <Input 
              placeholder="请简要描述您的问题或建议（不超过 35 字符）" 
              maxLength={35} 
              showCount
              size="large"
            />
          </Form.Item>

          <Form.Item
            label="反馈内容"
            name="content"
            rules={[{ required: true, message: '请输入反馈内容' }]}
          >
            <Input.TextArea 
              placeholder="请详细描述您遇到的问题或建议，我们会认真阅读并处理" 
              rows={10}
              showCount
              style={{ resize: 'none' }}
            />
          </Form.Item>

          <Form.Item>
            <Space size="middle">
              <Button 
                type="primary" 
                htmlType="submit" 
                loading={submitting}
                icon={<SendOutlined />}
                size="large"
                style={{ width: 160 }}
              >
                提交反馈
              </Button>
              <Button 
                onClick={handleReset}
                icon={<ReloadOutlined />}
                size="large"
                style={{ width: 120 }}
              >
                重置
              </Button>
            </Space>
          </Form.Item>
        </Form>

        {/* 底部特色卡片 */}
        <div className="features-section">
          <div className="feature-item">
            <div className="feature-icon">📧</div>
            <div className="feature-text">
              <h4>快速响应</h4>
              <p>1-3个工作日内处理</p>
            </div>
          </div>
          <div className="feature-item">
            <div className="feature-icon">🔒</div>
            <div className="feature-text">
              <h4>隐私保护</h4>
              <p>您的信息安全保密</p>
            </div>
          </div>
          <div className="feature-item">
            <div className="feature-icon">💬</div>
            <div className="feature-text">
              <h4>专业回复</h4>
              <p>认真对待每条反馈</p>
            </div>
          </div>
        </div>
      </Card>
    </div>
  )
}

export default FeedbackSubmit
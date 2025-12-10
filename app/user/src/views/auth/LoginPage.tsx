import React, { useState, useEffect } from 'react'
import { Form, Input, Button, message } from 'antd'
import { UserOutlined, LockOutlined, PhoneOutlined, MailOutlined, HomeOutlined } from '@ant-design/icons'
import { useNavigate, useLocation, Link } from 'react-router-dom'
import { authAPI } from '@api/auth'
import { sendSmsCode, sendEmailCode } from '@api/verify'
import { useUserStore } from '@stores/userStore'
import type { LoginForm, PhoneLoginForm, EmailLoginForm } from '@types/index'
import '@styles/auth.scss'

const LoginPage: React.FC = () => {
  console.log('📝 LoginPage 渲染')
  
  // 页面加载时滚动到顶部
  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'instant' })
  }, [])
  
  const [form] = Form.useForm()
  const [phoneForm] = Form.useForm()
  const [emailForm] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [smsSending, setSmsSending] = useState(false)
  const [emailCodeSending, setEmailCodeSending] = useState(false)
  const [phoneCountdown, setPhoneCountdown] = useState(0)
  const [emailCountdown, setEmailCountdown] = useState(0)
  const [activeTab, setActiveTab] = useState<'account' | 'phone' | 'email'>('account')
  
  const navigate = useNavigate()
  const location = useLocation()
  const { login } = useUserStore()

  const from = (location.state as any)?.from?.pathname || '/home'

  // 清理倒计时定时器
  useEffect(() => {
    let phoneTimer: NodeJS.Timeout | null = null
    let emailTimer: NodeJS.Timeout | null = null

    if (phoneCountdown > 0) {
      phoneTimer = setTimeout(() => {
        setPhoneCountdown(prev => prev - 1)
      }, 1000)
    }

    if (emailCountdown > 0) {
      emailTimer = setTimeout(() => {
        setEmailCountdown(prev => prev - 1)
      }, 1000)
    }

    return () => {
      if (phoneTimer) clearTimeout(phoneTimer)
      if (emailTimer) clearTimeout(emailTimer)
    }
  }, [phoneCountdown, emailCountdown])

  const handleFormLogin = async (values: LoginForm) => {
    setLoading(true)
    try {
      const response = await authAPI.formalLogin(values)
      if (response.code === 200) {
        message.success('登录成功')
        // 这里需要获取用户信息，暂时用模拟数据
        const mockUser = {
          userId: 1,
          userUsername: values.accountOrPhoneOrEmail,
          userAccount: values.accountOrPhoneOrEmail,
          userEmail: '',
          userPhone: '',
          userPassword: '',
          userCreatedTime: new Date().toISOString(),
          userLoginTime: new Date().toISOString(),
          userDreamPosition: 0,
          userDreamMinMonthSalary: 0,
          userDreamMaxMonthSalary: 0,
          userDreamWeekWorkDayNum: 0,
          userDreamGoodWelfare: ''
        }
        login(mockUser, response.data)
        navigate(from, { replace: true })
      }
    } catch (error) {
      console.error('登录失败:', error)
    } finally {
      setLoading(false)
    }
  }

  const handlePhoneLogin = async (values: PhoneLoginForm) => {
    setLoading(true)
    try {
      const response = await authAPI.phoneLogin(values)
      if (response.code === 200) {
        message.success('登录成功')
        const mockUser = {
          userId: 1,
          userUsername: values.phone,
          userAccount: values.phone,
          userEmail: '',
          userPhone: values.phone,
          userPassword: '',
          userCreatedTime: new Date().toISOString(),
          userLoginTime: new Date().toISOString(),
          userDreamPosition: 0,
          userDreamMinMonthSalary: 0,
          userDreamMaxMonthSalary: 0,
          userDreamWeekWorkDayNum: 0,
          userDreamGoodWelfare: ''
        }
        login(mockUser, response.data)
        navigate(from, { replace: true })
      }
    } catch (error) {
      console.error('登录失败:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleEmailLogin = async (values: EmailLoginForm) => {
    setLoading(true)
    try {
      const response = await authAPI.emailLogin(values)
      if (response.code === 200) {
        message.success('登录成功')
        const mockUser = {
          userId: 1,
          userUsername: values.email,
          userAccount: values.email,
          userEmail: values.email,
          userPhone: '',
          userPassword: '',
          userCreatedTime: new Date().toISOString(),
          userLoginTime: new Date().toISOString(),
          userDreamPosition: 0,
          userDreamMinMonthSalary: 0,
          userDreamMaxMonthSalary: 0,
          userDreamWeekWorkDayNum: 0,
          userDreamGoodWelfare: ''
        }
        login(mockUser, response.data)
        navigate(from, { replace: true })
      }
    } catch (error) {
      console.error('登录失败:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSendSmsCode = async () => {
    const phone = phoneForm.getFieldValue('phone')
    if (!phone || !/^1[3-9]\d{9}$/.test(phone)) {
      message.error('请输入正确的手机号')
      return
    }

    setSmsSending(true)
    try {
      await sendSmsCode(phone)
      message.success('验证码发送成功')
      setPhoneCountdown(60)
    } catch (error) {
      console.error('发送验证码失败:', error)
      message.error('发送验证码失败')
    } finally {
      setSmsSending(false)
    }
  }

  const handleSendEmailCode = async () => {
    const email = emailForm.getFieldValue('email')
    if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      message.error('请输入正确的邮箱')
      return
    }

    setEmailCodeSending(true)
    try {
      await sendEmailCode(email)
      message.success('验证码发送成功')
      setEmailCountdown(60)
    } catch (error) {
      console.error('发送验证码失败:', error)
      message.error('发送验证码失败')
    } finally {
      setEmailCodeSending(false)
    }
  }

  const switchTab = (tab: 'account' | 'phone' | 'email') => {
    setActiveTab(tab)
    // 切换tab时清除表单验证
    if (tab === 'account') {
      form.resetFields()
    } else if (tab === 'phone') {
      phoneForm.resetFields()
    } else {
      emailForm.resetFields()
    }
  }

  return (
    <div className="login-container">
      {/* 背景装饰 */}
      <div className="background-decoration">
        <div className="decoration-circle decoration-1"></div>
        <div className="decoration-circle decoration-2"></div>
        <div className="decoration-circle decoration-3"></div>
      </div>

      <div className="login-card">
        <div className="logo-section">
          <div className="logo">
            <div className="logo-icon">
              <i className="icon-resume">📄</i>
            </div>
            <h1>易投简历</h1>
          </div>
          <p>用户登录</p>
        </div>

        {/* 自定义Tabs */}
        <div className="login-tabs">
          <div 
            className={`tab-item ${activeTab === 'account' ? 'active' : ''}`}
            onClick={() => switchTab('account')}
          >
            账号密码
          </div>
          <div 
            className={`tab-item ${activeTab === 'phone' ? 'active' : ''}`}
            onClick={() => switchTab('phone')}
          >
            手机验证码
          </div>
          <div 
            className={`tab-item ${activeTab === 'email' ? 'active' : ''}`}
            onClick={() => switchTab('email')}
          >
            邮箱验证码
          </div>
        </div>

        {/* 账号密码登录 */}
        <div className="form-section" style={{ display: activeTab === 'account' ? 'block' : 'none' }}>
          <Form
            form={form}
            name="formLogin"
            onFinish={handleFormLogin}
            layout="vertical"
            size="large"
          >
            <Form.Item
              name="accountOrPhoneOrEmail"
              rules={[
                { required: true, message: '请输入账号/手机号/邮箱' }
              ]}
            >
              <Input
                prefix={<UserOutlined />}
                placeholder="请输入账号/手机号/邮箱"
                clearable
              />
            </Form.Item>
            <Form.Item
              name="password"
              rules={[
                { required: true, message: '请输入密码' },
                { min: 6, message: '密码长度不能少于6位' }
              ]}
            >
              <Input.Password
                prefix={<LockOutlined />}
                placeholder="请输入密码"
                visibilityToggle
                clearable
              />
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                block
                className="login-button"
              >
                立即登录
              </Button>
            </Form.Item>
          </Form>
        </div>

        {/* 手机验证码登录 */}
        <div className="form-section" style={{ display: activeTab === 'phone' ? 'block' : 'none' }}>
          <Form
            form={phoneForm}
            name="phoneLogin"
            onFinish={handlePhoneLogin}
            layout="vertical"
            size="large"
          >
            <Form.Item
              name="phone"
              rules={[
                { required: true, message: '请输入手机号' },
                { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
              ]}
            >
              <Input
                prefix={<PhoneOutlined />}
                placeholder="请输入手机号"
                clearable
              />
            </Form.Item>
            <Form.Item>
              <div className="code-input-group">
                <Form.Item
                  name="verifyCode"
                  noStyle
                  rules={[
                    { required: true, message: '请输入验证码' },
                    { len: 6, message: '验证码为6位数字' }
                  ]}
                >
                  <Input
                    prefix={<MailOutlined />}
                    placeholder="请输入验证码"
                    clearable
                  />
                </Form.Item>
                <Button
                  onClick={handleSendSmsCode}
                  loading={smsSending}
                  disabled={phoneCountdown > 0}
                  className="code-button"
                >
                  {phoneCountdown > 0 ? `${phoneCountdown}s` : '获取验证码'}
                </Button>
              </div>
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                block
                className="login-button"
              >
                立即登录
              </Button>
            </Form.Item>
          </Form>
        </div>

        {/* 邮箱验证码登录 */}
        <div className="form-section" style={{ display: activeTab === 'email' ? 'block' : 'none' }}>
          <Form
            form={emailForm}
            name="emailLogin"
            onFinish={handleEmailLogin}
            layout="vertical"
            size="large"
          >
            <Form.Item
              name="email"
              rules={[
                { required: true, message: '请输入邮箱' },
                { type: 'email', message: '请输入正确的邮箱格式' }
              ]}
            >
              <Input
                prefix={<MailOutlined />}
                placeholder="请输入邮箱"
                clearable
              />
            </Form.Item>
            <Form.Item>
              <div className="code-input-group">
                <Form.Item
                  name="verifyCode"
                  noStyle
                  rules={[
                    { required: true, message: '请输入验证码' },
                    { len: 6, message: '验证码为6位数字' }
                  ]}
                >
                  <Input
                    prefix={<MailOutlined />}
                    placeholder="请输入验证码"
                    clearable
                  />
                </Form.Item>
                <Button
                  onClick={handleSendEmailCode}
                  loading={emailCodeSending}
                  disabled={emailCountdown > 0}
                  className="code-button"
                >
                  {emailCountdown > 0 ? `${emailCountdown}s` : '获取验证码'}
                </Button>
              </div>
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                block
                className="login-button"
              >
                立即登录
              </Button>
            </Form.Item>
          </Form>
        </div>

        <div className="footer-section">
          <Link to="/auth/register" className="link-button apply-link">
            注册新账号
          </Link>
          <span className="divider">|</span>
          <span 
            className="link-button" 
            onClick={async () => {
              try {
                const response = await authAPI.generateRandomAccount()
                if (response.code === 200) {
                  message.success({
                     content: `生成成功：${response.data}`,
                     duration: 5,
                  })
                }
              } catch (error) {
                console.error('生成随机账号失败:', error)
              }
            }}
          >
            随机生成账号
          </span>
          <span className="divider">|</span>
          <Link to="/" className="link-button back-link">
            <HomeOutlined style={{ marginRight: 4 }} />
            返回首页
          </Link>
        </div>
      </div>

      {/* 装饰性元素 */}
      <div className="floating-elements">
        <div className="element element-1"></div>
        <div className="element element-2"></div>
        <div className="element element-3"></div>
      </div>

    </div>
  )
}

export default LoginPage
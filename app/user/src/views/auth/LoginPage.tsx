import React, { useState } from 'react'
import { Form, Input, Button, Tabs, Alert, Divider, message } from 'antd'
import { UserOutlined, LockOutlined, PhoneOutlined, MailOutlined, EyeInvisibleOutlined, EyeOutlined } from '@ant-design/icons'
import { useNavigate, useLocation, Link } from 'react-router-dom'
import { authAPI } from '@api/auth'
import { sendSmsCode, sendEmailCode } from '@api/verify'
import { useUserStore } from '@stores/userStore'
import type { LoginForm, PhoneLoginForm, EmailLoginForm } from '@types/index'

const { TabPane } = Tabs

const LoginPage: React.FC = () => {
  console.log('📝 LoginPage 渲染')
  
  const [form] = Form.useForm()
  const [phoneForm] = Form.useForm()
  const [emailForm] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [smsSending, setSmsSending] = useState(false)
  const [emailCodeSending, setEmailCodeSending] = useState(false)
  const [countdown, setCountdown] = useState(0)
  
  const navigate = useNavigate()
  const location = useLocation()
  const { login } = useUserStore()

  const from = (location.state as any)?.from?.pathname || '/home'

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
      setCountdown(60)
      const timer = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            clearInterval(timer)
            return 0
          }
          return prev - 1
        })
      }, 1000)
    } catch (error) {
      console.error('发送验证码失败:', error)
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
      setCountdown(60)
      const timer = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            clearInterval(timer)
            return 0
          }
          return prev - 1
        })
      }, 1000)
    } catch (error) {
      console.error('发送验证码失败:', error)
    } finally {
      setEmailCodeSending(false)
    }
  }

  return (
    <div className="login-page">
      <div className="login-form-container">
        <div className="login-header">
          <h2>登录易投简历</h2>
          <p>专业简历制作，助您求职成功</p>
        </div>

        <Tabs defaultActiveKey="account" centered>
          <TabPane tab="账号密码登录" key="account">
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
                  登录
                </Button>
              </Form.Item>
            </Form>
          </TabPane>

          <TabPane tab="手机验证码登录" key="phone">
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
                />
              </Form.Item>
              <Form.Item>
                <Input.Group compact>
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
                      style={{ width: 'calc(100% - 120px)' }}
                    />
                  </Form.Item>
                  <Button
                    onClick={handleSendSmsCode}
                    loading={smsSending}
                    disabled={countdown > 0}
                    style={{ width: 120 }}
                  >
                    {countdown > 0 ? `${countdown}s` : '获取验证码'}
                  </Button>
                </Input.Group>
              </Form.Item>
              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  loading={loading}
                  block
                  className="login-button"
                >
                  登录
                </Button>
              </Form.Item>
            </Form>
          </TabPane>

          <TabPane tab="邮箱验证码登录" key="email">
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
                />
              </Form.Item>
              <Form.Item>
                <Input.Group compact>
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
                      style={{ width: 'calc(100% - 120px)' }}
                    />
                  </Form.Item>
                  <Button
                    onClick={handleSendEmailCode}
                    loading={emailCodeSending}
                    disabled={countdown > 0}
                    style={{ width: 120 }}
                  >
                    {countdown > 0 ? `${countdown}s` : '获取验证码'}
                  </Button>
                </Input.Group>
              </Form.Item>
              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  loading={loading}
                  block
                  className="login-button"
                >
                  登录
                </Button>
              </Form.Item>
            </Form>
          </TabPane>
        </Tabs>

        <Divider>
          <span className="divider-text">其他操作</span>
        </Divider>

        <div className="login-footer">
          <Link to="/auth/register" className="register-link">
            注册新账号
          </Link>
          <span className="separator">·</span>
          <span 
            className="random-account-link" 
            onClick={async () => {
              try {
                const response = await authAPI.generateRandomAccount()
                if (response.code === 200) {
                  message.success(`生成成功：${response.data}`)
                }
              } catch (error) {
                console.error('生成随机账号失败:', error)
              }
            }}
          >
            随机生成账号
          </span>
        </div>
      </div>
    </div>
  )
}

export default LoginPage
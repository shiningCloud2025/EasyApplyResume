import React, { useState } from 'react'
import { Form, Input, Button, Select, message, Steps, Row, Col, Checkbox } from 'antd'
import { UserOutlined, LockOutlined, PhoneOutlined, MailOutlined, HomeOutlined } from '@ant-design/icons'
import { Link, useNavigate } from 'react-router-dom'
import { authAPI } from '@api/auth'
import { sendSmsCode, sendEmailCode } from '@api/verify'
import type { RegisterForm } from '@types/index'
import '@styles/auth.scss'
import '@styles/auth-override.scss'

const { Option } = Select
const { Step } = Steps
const { TextArea } = Input

const RegisterPage: React.FC = () => {
  // 页面加载时滚动到顶部
  React.useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'instant' })
  }, [])
  
  const [form] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [current, setCurrent] = useState(0)
  const [smsSending, setSmsSending] = useState(false)
  const [emailCodeSending, setEmailCodeSending] = useState(false)
  const [phoneCountdown, setPhoneCountdown] = useState(0)
  const [emailCountdown, setEmailCountdown] = useState(0)
  const [agreeTerms, setAgreeTerms] = useState(false)

  const navigate = useNavigate()

  const steps = [
    {
      title: '基本信息',
      description: '填写基本个人信息',
    },
    {
      title: '求职意向',
      description: '设置求职偏好',
    },
    {
      title: '验证确认',
      description: '手机邮箱验证',
    },
  ]

  const handleSendSmsCode = async () => {
    const phone = form.getFieldValue('userPhone')
    if (!phone || !/^1[3-9]\d{9}$/.test(phone)) {
      message.error('请输入正确的手机号')
      return
    }

    setSmsSending(true)
    try {
      await sendSmsCode(phone)
      message.success('验证码发送成功')
      setPhoneCountdown(60)
      const timer = setInterval(() => {
        setPhoneCountdown((prev) => {
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
    const email = form.getFieldValue('userEmail')
    if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      message.error('请输入正确的邮箱')
      return
    }

    setEmailCodeSending(true)
    try {
      await sendEmailCode(email)
      message.success('验证码发送成功')
      setEmailCountdown(60)
      const timer = setInterval(() => {
        setEmailCountdown((prev) => {
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

  const handleNext = async () => {
    try {
      if (current === 0) {
        await form.validateFields([
          'userAccount', 'userUsername', 'userEmail', 'userPhone', 'userPassword'
        ])
      } else if (current === 1) {
        await form.validateFields([
          'userDreamPosition', 'userDreamMinMonthSalary', 'userDreamMaxMonthSalary'
        ])
      } else if (current === 2) {
        // 最后一步，检查协议并提交注册
        if (!agreeTerms) {
          message.warning('请先同意用户协议和隐私政策')
          return
        }
        await form.validateFields()
        const values = form.getFieldsValue() as RegisterForm
        await handleRegister(values)
        return
      }
      setCurrent(current + 1)
    } catch (error) {
      message.error('请填写完整信息')
    }
  }

  const handlePrevious = () => {
    setCurrent(current - 1)
  }

  const handleRegister = async (values: RegisterForm) => {
    setLoading(true)
    try {
      const response = await authAPI.register(values)
      if (response.code === 200) {
        message.success('注册成功，请登录')
        navigate('/auth/login')
      }
    } catch (error) {
      console.error('注册失败:', error)
    } finally {
      setLoading(false)
    }
  }

  const renderStepContent = () => {
    switch (current) {
      case 0:
        return (
          <>
            <Form.Item
              name="userAccount"
              label="用户账号"
              rules={[
                { required: true, message: '请输入用户账号' },
                { min: 3, max: 20, message: '账号长度为3-20位' },
                { pattern: /^[a-zA-Z0-9_]+$/, message: '账号只能包含字母、数字和下划线' }
              ]}
            >
              <Input prefix={<UserOutlined />} placeholder="请输入用户账号" />
            </Form.Item>

            <Form.Item
              name="userUsername"
              label="用户名"
              rules={[
                { required: true, message: '请输入用户名' },
                { min: 2, max: 10, message: '用户名长度为2-10位' }
              ]}
            >
              <Input prefix={<UserOutlined />} placeholder="请输入用户名" />
            </Form.Item>

            <Form.Item
              name="userEmail"
              label="邮箱"
              rules={[
                { required: true, message: '请输入邮箱' },
                { type: 'email', message: '请输入正确的邮箱格式' }
              ]}
            >
              <Input prefix={<MailOutlined />} placeholder="请输入邮箱" />
            </Form.Item>

            <Form.Item
              name="userPhone"
              label="手机号"
              rules={[
                { required: true, message: '请输入手机号' },
                { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
              ]}
            >
              <Input prefix={<PhoneOutlined />} placeholder="请输入手机号" />
            </Form.Item>

            <Form.Item
              name="userPassword"
              label="密码"
              rules={[
                { required: true, message: '请输入密码' },
                { min: 6, max: 20, message: '密码长度为6-20位' }
              ]}
            >
              <Input.Password prefix={<LockOutlined />} placeholder="请输入密码" />
            </Form.Item>

            <Form.Item
              name="userIntroduce"
              label="个人介绍"
            >
              <TextArea rows={3} placeholder="请输入个人介绍（选填）" />
            </Form.Item>
          </>
        )

      case 1:
        return (
          <>
            <Form.Item
              name="userDreamPosition"
              label="目标岗位"
              rules={[{ required: true, message: '请选择目标岗位' }]}
            >
              <Select placeholder="请选择目标岗位">
                <Option value={1}>前端开发工程师</Option>
                <Option value={2}>后端开发工程师</Option>
                <Option value={3}>全栈开发工程师</Option>
                <Option value={4}>产品经理</Option>
                <Option value={5}>UI设计师</Option>
                <Option value={6}>数据分析师</Option>
                <Option value={7}>运营专员</Option>
                <Option value={8}>其他</Option>
              </Select>
            </Form.Item>

            <Row gutter={16}>
              <Col span={12}>
                <Form.Item
                  name="userDreamMinMonthSalary"
                  label="最低月薪"
                  rules={[{ required: true, message: '请输入最低月薪' }]}
                >
                  <Input type="number" placeholder="例如：8000" />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  name="userDreamMaxMonthSalary"
                  label="最高月薪"
                  rules={[{ required: true, message: '请输入最高月薪' }]}
                >
                  <Input type="number" placeholder="例如：15000" />
                </Form.Item>
              </Col>
            </Row>

            <Form.Item
              name="userDreamWeekWorkDayNum"
              label="期望工作天数"
              rules={[{ required: true, message: '请选择期望工作天数' }]}
            >
              <Select placeholder="请选择每周工作天数">
                <Option value={5}>5天</Option>
                <Option value={6}>6天</Option>
                <Option value={7}>7天</Option>
              </Select>
            </Form.Item>

            <Form.Item
              name="userDreamGoodWelfare"
              label="期望福利待遇"
            >
              <TextArea rows={3} placeholder="例如：五险一金、带薪年假、餐补等（选填）" />
            </Form.Item>
          </>
        )

      case 2:
        return (
          <>
            <Form.Item
              name="phoneMessageCode"
              label="手机验证码"
              rules={[
                { required: true, message: '请输入手机验证码' },
                { len: 6, message: '验证码为6位数字' }
              ]}
            >
              <Input.Group compact>
                <Input
                  style={{ width: 'calc(100% - 120px)' }}
                  placeholder="请输入手机验证码"
                />
                <Button
                  onClick={handleSendSmsCode}
                  loading={smsSending}
                  disabled={phoneCountdown > 0}
                  style={{ width: 120 }}
                >
                  {phoneCountdown > 0 ? `${phoneCountdown}s` : '获取验证码'}
                </Button>
              </Input.Group>
            </Form.Item>

            <Form.Item
              name="emailMessageCode"
              label="邮箱验证码"
              rules={[
                { required: true, message: '请输入邮箱验证码' },
                { len: 6, message: '验证码为6位数字' }
              ]}
            >
              <Input.Group compact>
                <Input
                  style={{ width: 'calc(100% - 120px)' }}
                  placeholder="请输入邮箱验证码"
                />
                <Button
                  onClick={handleSendEmailCode}
                  loading={emailCodeSending}
                  disabled={emailCountdown > 0}
                  style={{ width: 120 }}
                >
                  {emailCountdown > 0 ? `${emailCountdown}s` : '获取验证码'}
                </Button>
              </Input.Group>
            </Form.Item>
          </>
        )

      default:
        return null
    }
  }

  return (
    <div className="register-container">
      {/* 背景装饰 */}
      <div className="background-decoration">
        <div className="decoration-circle decoration-1"></div>
        <div className="decoration-circle decoration-2"></div>
        <div className="decoration-circle decoration-3"></div>
      </div>

      <div className="register-card">
        <div className="logo-section">
          <div className="logo">
            <div className="logo-icon">
              <i className="icon-resume">📄</i>
            </div>
            <h1>易投简历</h1>
          </div>
          <p>用户注册</p>
        </div>

        <div className="register-body">
          <Steps current={current} className="register-steps">
            {steps.map(item => (
              <Step key={item.title} title={item.title} />
            ))}
          </Steps>

          <div className="steps-content">
            <Form
              form={form}
              layout="vertical"
              size="large"
              className="register-form"
            >
              {renderStepContent()}
            </Form>
          </div>

          <div className="steps-action">
            {current > 0 && (
              <Button onClick={handlePrevious} className="action-button">
                上一步
              </Button>
            )}
            {current < steps.length - 1 && (
              <Button type="primary" onClick={handleNext} className="action-button primary">
                下一步
              </Button>
            )}
            {current === steps.length - 1 && (
              <>
                <div className="agreement-section">
                  <Checkbox 
                    checked={agreeTerms} 
                    onChange={(e) => setAgreeTerms(e.target.checked)}
                  >
                    我已阅读并同意
                    <a href="#" onClick={(e) => { e.preventDefault(); message.info('用户协议') }}>《用户协议》</a>
                    和
                    <a href="#" onClick={(e) => { e.preventDefault(); message.info('隐私政策') }}>《隐私政策》</a>
                  </Checkbox>
                </div>
                <Button 
                  type="primary" 
                  onClick={handleNext}
                  loading={loading}
                  className="action-button primary register-submit-btn"
                  block
                >
                  完成注册
                </Button>
              </>
            )}
          </div>

          <div className="footer-section">
            <span style={{ color: '#6b7280' }}>已有账号？</span>
            <Link to="/auth/login" className="link-button apply-link" style={{ marginLeft: 8 }}>
              立即登录
            </Link>
            <span className="divider">|</span>
            <Link to="/" className="link-button back-link">
              <HomeOutlined style={{ marginRight: 4 }} />
              返回首页
            </Link>
          </div>
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

export default RegisterPage
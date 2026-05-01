import React, { useState } from 'react'
import { Form, Input, Button, Select, message, Steps, Row, Col, Checkbox } from 'antd'
import { UserOutlined, LockOutlined, PhoneOutlined, MailOutlined, HomeOutlined } from '@ant-design/icons'
import { Link, useNavigate } from 'react-router-dom'
import { authAPI } from '@api/auth'
import { sendSmsCode, sendEmailCode } from '@api/verify'
import { provinceAPI, universityAPI, recruitPositionAPI } from '@api/system'
import { useUserStore } from '@stores/userStore'
import type { RegisterForm, ProvinceMap, CityMap, UniversityMap, RecruitPosition } from '@types/index'
import { formatRecruitPositionName } from '@utils/index'
import '@styles/auth-override.scss'

const { Option } = Select
const { Step } = Steps
const { TextArea } = Input

const RegisterPage: React.FC = () => {
  // 页面加载时滚动到顶部
  React.useEffect(() => {
    window.scrollTo(0, 0)
    document.documentElement.scrollTop = 0
    document.body.scrollTop = 0
  }, [])
  
  const [form] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [current, setCurrent] = useState(0)
  const [smsSending, setSmsSending] = useState(false)
  const [emailCodeSending, setEmailCodeSending] = useState(false)
  const [phoneCountdown, setPhoneCountdown] = useState(0)
  const [emailCountdown, setEmailCountdown] = useState(0)
  const [agreeTerms, setAgreeTerms] = useState(false)

  // 省份、城市、大学、岗位数据
  const [provinces, setProvinces] = useState<ProvinceMap[]>([])
  const [cities, setCities] = useState<CityMap[]>([])
  const [universities, setUniversities] = useState<UniversityMap[]>([])
  const [positions, setPositions] = useState<RecruitPosition[]>([])
  const [universitySearchLoading, setUniversitySearchLoading] = useState(false)
  const [positionLoaded, setPositionLoaded] = useState(false) // 标记岗位数据是否已加载
  const [provinceLoaded, setProvinceLoaded] = useState(false) // 标记省份数据是否已加载

  const navigate = useNavigate()
  const { login } = useUserStore()

  // 点击省份下拉框时加载省份数据
  const handleProvinceDropdownOpen = async (open: boolean) => {
    if (open && !provinceLoaded) {
      try {
        const provinceRes = await provinceAPI.getAllProvince()
        if (Array.isArray(provinceRes)) {
          setProvinces(provinceRes)
          setProvinceLoaded(true)
        }
      } catch (error) {
        console.error('加载省份失败:', error)
      }
    }
  }

  // 点击岗位下拉框时加载岗位数据
  const handlePositionDropdownOpen = async (open: boolean) => {
    if (open && !positionLoaded) {
      try {
        const positionRes = await recruitPositionAPI.getAllRecruitPosition()
        if (positionRes.code === 200 && Array.isArray(positionRes.data)) {
          setPositions(positionRes.data)
          setPositionLoaded(true)
        }
      } catch (error) {
        console.error('加载岗位失败:', error)
      }
    }
  }

  // 省份选择变化时加载城市
  const handleProvinceChange = async (provinceId: number) => {
    form.setFieldsValue({ userRecruitLocationSecond: undefined })
    setCities([])
    if (provinceId) {
      try {
        // 直接返回数组
        const res = await provinceAPI.getCityByProvinceId(provinceId)
        if (Array.isArray(res)) {
          setCities(res)
        }
      } catch (error) {
        console.error('加载城市失败:', error)
      }
    }
  }

  // 大学搜索（输入时调用模糊查询接口）
  const handleUniversitySearch = async (value: string) => {
    setUniversitySearchLoading(true)
    try {
      // 传递用户输入的值（包括空字符串）给后端
      const searchValue = value ? value.trim() : ''
      const res = await universityAPI.searchUniversities(searchValue)
      if (res.code === 200 && Array.isArray(res.data)) {
        setUniversities(res.data)
      }
    } catch (error) {
      console.error('搜索大学失败:', error)
    } finally {
      setUniversitySearchLoading(false)
    }
  }

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
        // 第二步所有字段都是选填，不需要强制验证
        // 仅验证已填写的字段格式是否正确
        const minSalary = form.getFieldValue('userDreamMinMonthSalary')
        const maxSalary = form.getFieldValue('userDreamMaxMonthSalary')
        if (minSalary !== undefined && minSalary !== null && minSalary !== '') {
          if (Number(minSalary) < 0 || Number(minSalary) > 100000) {
            message.error('最低月薪范围为0-100000')
            return
          }
        }
        if (maxSalary !== undefined && maxSalary !== null && maxSalary !== '') {
          if (minSalary !== undefined && minSalary !== null && minSalary !== '' && Number(maxSalary) < Number(minSalary)) {
            message.error('最高月薪不能低于最低月薪')
            return
          }
        }
      } else if (current === 2) {
        // 最后一步，检查协议并提交注册
        if (!agreeTerms) {
          message.warning('请先同意用户协议和隐私政策')
          return
        }
        await form.validateFields()
        const values = form.getFieldsValue(true) as RegisterForm
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

  // 点击步骤条跳转
  const handleStepClick = async (step: number) => {
    // 点击当前步骤，不做任何操作
    if (step === current) return
    
    // 往后退：可以随意回退
    if (step < current) {
      setCurrent(step)
      return
    }
    
    // 往前走：需要完成前面的步骤
    // 点击第2步，需要先完成第1步
    if (step >= 1 && current < 1) {
      try {
        await form.validateFields(['userAccount', 'userUsername', 'userEmail', 'userPhone', 'userPassword'])
      } catch {
        message.warning('请先完成第一步，填写必填信息')
        return
      }
    }
    
    // 点击第3步，需要先完成第1步和第2步
    if (step >= 2 && current < 1) {
      try {
        await form.validateFields(['userAccount', 'userUsername', 'userEmail', 'userPhone', 'userPassword'])
      } catch {
        message.warning('请先完成第一步，填写必填信息')
        return
      }
    }
    
    // 第2步是选填，不需要强制验证，可以直接跳转
    setCurrent(step)
  }

  const handleRegister = async (values: RegisterForm) => {
    setLoading(true)
    try {
      const response = await authAPI.register(values)
      if (response.code === 200) {
        // 注册成功，直接登录（后端返回的 data 是 token）
        const token = response.data as string
        login(null, token)  // 保存 token，用户信息后续获取
        message.success('注册成功，已自动登录')
        navigate('/home')  // 跳转首页
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
                { min: 7, max: 10, message: '账号长度为7-10位' },
                { pattern: /^[1-9]\d{6,9}$/, message: '账号必须为数字，且首位不能为0' }
              ]}
            >
              <Input prefix={<UserOutlined />} placeholder="请输入7-10位数字账号，首位不能为0" autoComplete="off" />
            </Form.Item>

            <Form.Item
              name="userUsername"
              label="用户名"
              rules={[
                { required: true, message: '请输入用户名' },
                { min: 1, max: 20, message: '用户名长度为1-20位' }
              ]}
            >
              <Input prefix={<UserOutlined />} placeholder="请输入用户名（1-20位）" autoComplete="new-password" />
            </Form.Item>

            <Form.Item
              name="userEmail"
              label="邮箱"
              rules={[
                { required: true, message: '请输入邮箱' },
                { type: 'email', message: '请输入正确的邮箱格式' },
                { max: 25, message: '邮箱长度不能超过25位' }
              ]}
            >
              <Input prefix={<MailOutlined />} placeholder="请输入邮箱（不超过25位）" autoComplete="new-password" />
            </Form.Item>

            <Form.Item
              name="userPhone"
              label="手机号"
              rules={[
                { required: true, message: '请输入手机号' },
                { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
              ]}
            >
              <Input prefix={<PhoneOutlined />} placeholder="请输入手机号" autoComplete="off" />
            </Form.Item>

            <Form.Item
              name="userPassword"
              label="密码"
              rules={[
                { required: true, message: '请输入密码' },
                { min: 6, max: 30, message: '密码长度为6-30位' }
              ]}
            >
              <Input.Password prefix={<LockOutlined />} placeholder="请输入密码（6-30位）" autoComplete="new-password" />
            </Form.Item>

            <Form.Item
              name="userIntroduce"
              label="个人介绍"
              rules={[
                { max: 200, message: '个人介绍不能超过200字' }
              ]}
            >
              <TextArea rows={3} placeholder="请输入个人介绍（选填，不超过200字）" maxLength={200} showCount />
            </Form.Item>
          </>
        )

      case 1:
        return (
          <>
            <Form.Item
              name="userDreamPosition"
              label="目标岗位"
            >
              <Select 
                placeholder="请选择目标岗位（选填）" 
                allowClear
                showSearch
                optionFilterProp="children"
                onDropdownVisibleChange={handlePositionDropdownOpen}
                filterOption={(input, option) =>
                  (option?.children as unknown as string)?.toLowerCase().includes(input.toLowerCase())
                }
              >
                {positions.map(pos => (
                  <Option key={pos.recruitPositionId} value={pos.recruitPositionId}>
                    {formatRecruitPositionName(pos.recruitPositionName)}
                  </Option>
                ))}
              </Select>
            </Form.Item>

            <Row gutter={16}>
              <Col span={12}>
                <Form.Item
                  name="userRecruitLocationFirst"
                  label="期望工作省份"
                >
                  <Select
                    placeholder="请选择省份（选填）"
                    allowClear
                    showSearch
                    optionFilterProp="children"
                    onDropdownVisibleChange={handleProvinceDropdownOpen}
                    onChange={handleProvinceChange}
                    filterOption={(input, option) =>
                      (option?.children as unknown as string)?.toLowerCase().includes(input.toLowerCase())
                    }
                  >
                    {provinces.map(province => (
                      <Option key={province.provinceMapPid} value={province.provinceMapPid}>
                        {province.provinceMapPname}
                      </Option>
                    ))}
                  </Select>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  name="userRecruitLocationSecond"
                  label="期望工作城市"
                >
                  <Select
                    placeholder="请先选择省份"
                    allowClear
                    showSearch
                    optionFilterProp="children"
                    disabled={cities.length === 0}
                    filterOption={(input, option) =>
                      (option?.children as unknown as string)?.toLowerCase().includes(input.toLowerCase())
                    }
                  >
                    {cities.map(city => (
                      <Option key={city.cityMapCid} value={city.cityMapCid}>
                        {city.cityMapCname}
                      </Option>
                    ))}
                  </Select>
                </Form.Item>
              </Col>
            </Row>

            <Form.Item
              name="userUniversityCode"
              label="毕业院校"
            >
              <Select
                placeholder="请输入大学名称搜索（选填）"
                allowClear
                showSearch
                loading={universitySearchLoading}
                onSearch={handleUniversitySearch}
                onDropdownVisibleChange={(open) => {
                  if (open && universities.length === 0) {
                    handleUniversitySearch('')
                  }
                }}
                filterOption={false}
                notFoundContent={universitySearchLoading ? '搜索中...' : '未找到匹配的大学'}
              >
                {universities.map(uni => (
                  <Option key={uni.universityMapId} value={uni.universityMapId}>
                    {uni.universityMapName}
                  </Option>
                ))}
              </Select>
            </Form.Item>

            <Row gutter={16}>
              <Col span={12}>
                <Form.Item
                  name="userDreamMinMonthSalary"
                  label="最低月薪"
                  rules={[
                    {
                      validator: (_, value) => {
                        if (value === undefined || value === null || value === '') return Promise.resolve()
                        const num = Number(value)
                        if (isNaN(num) || num < 0) return Promise.reject('月薪不能为负数')
                        if (num > 100000) return Promise.reject('月薪不能超过100000')
                        return Promise.resolve()
                      }
                    }
                  ]}
                >
                  <Input type="number" placeholder="选填，范围0-100000" />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  name="userDreamMaxMonthSalary"
                  label="最高月薪"
                  dependencies={['userDreamMinMonthSalary']}
                  rules={[
                    ({ getFieldValue }) => ({
                      validator(_, value) {
                        if (value === undefined || value === null || value === '') return Promise.resolve()
                        const minSalary = getFieldValue('userDreamMinMonthSalary')
                        if (minSalary !== undefined && minSalary !== null && minSalary !== '' && Number(value) < Number(minSalary)) {
                          return Promise.reject('最高月薪不能低于最低月薪')
                        }
                        return Promise.resolve()
                      }
                    })
                  ]}
                >
                  <Input type="number" placeholder="选填，需≥最低月薪" />
                </Form.Item>
              </Col>
            </Row>

            <Form.Item
              name="userDreamWeekWorkDayNum"
              label="期望工作天数"
            >
              <Select placeholder="请选择每周工作天数（选填，默认5天）" allowClear>
                <Option value={1}>1天</Option>
                <Option value={2}>2天</Option>
                <Option value={3}>3天</Option>
                <Option value={4}>4天</Option>
                <Option value={5}>5天</Option>
                <Option value={6}>6天</Option>
                <Option value={7}>7天</Option>
              </Select>
            </Form.Item>

            <Form.Item
              name="userDreamGoodWelfare"
              label="期望福利待遇"
              rules={[
                { max: 200, message: '福利待遇描述不能超过200字' }
              ]}
            >
              <TextArea rows={3} placeholder="例如：五险一金、带薪年假、餐补等（选填，不超过200字）" maxLength={200} showCount />
            </Form.Item>
          </>
        )

      case 2:
        return (
          <>
            <Form.Item
              label="手机验证码"
              required
            >
              <div style={{ display: 'flex', gap: '8px' }}>
                <Form.Item
                  name="phoneMessageCode"
                  noStyle
                  rules={[
                    { required: true, message: '请输入手机验证码' },
                    { len: 6, message: '验证码为6位' }
                  ]}
                >
                  <Input
                    style={{ flex: 1 }}
                    placeholder="请输入手机验证码"
                  />
                </Form.Item>
                <Button
                  onClick={handleSendSmsCode}
                  loading={smsSending}
                  disabled={phoneCountdown > 0}
                  style={{ width: 120 }}
                >
                  {phoneCountdown > 0 ? `${phoneCountdown}s` : '获取验证码'}
                </Button>
              </div>
            </Form.Item>

            <Form.Item
              label="邮箱验证码"
              required
            >
              <div style={{ display: 'flex', gap: '8px' }}>
                <Form.Item
                  name="emailMessageCode"
                  noStyle
                  rules={[
                    { required: true, message: '请输入邮箱验证码' },
                    { len: 6, message: '验证码为6位' }
                  ]}
                >
                  <Input
                    style={{ flex: 1 }}
                    placeholder="请输入邮箱验证码"
                  />
                </Form.Item>
                <Button
                  onClick={handleSendEmailCode}
                  loading={emailCodeSending}
                  disabled={emailCountdown > 0}
                  style={{ width: 120 }}
                >
                  {emailCountdown > 0 ? `${emailCountdown}s` : '获取验证码'}
                </Button>
              </div>
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
        {/* 左侧装饰区域 */}
        <div className="register-card-left">
          <div className="logo-section">
            <div className="logo">
              <div className="logo-icon">
                <i className="icon-resume">📄</i>
              </div>
              <h1>易投简历</h1>
            </div>
            <p>用户注册</p>
          </div>
        </div>

        {/* 右侧表单区域 */}
        <div className="register-card-right">
          <div className="register-body">
          <Steps current={current} className="register-steps" onChange={handleStepClick}>
            {steps.map(item => (
              <Step key={item.title} title={item.title} style={{ cursor: 'pointer' }} />
            ))}
          </Steps>

          <div className="steps-content">
            <Form
              form={form}
              layout="vertical"
              size="large"
              className="register-form"
              preserve={true}
              autoComplete="off"
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
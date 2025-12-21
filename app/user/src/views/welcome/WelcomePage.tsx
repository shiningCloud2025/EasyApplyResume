import React, { useState, useEffect } from 'react'
import { Button, Row, Col, Card, Menu, Dropdown, Modal, Form, Input, message, Divider, Tabs } from 'antd'
import { UserOutlined, LockOutlined, PhoneOutlined, MailOutlined, DownOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useUserStore } from '@stores/userStore'
import PortalHeader from '@components/PortalHeader'
import PortalFooter from '@components/PortalFooter'
import './WelcomePage.scss'

const { TabPane } = Tabs

const WelcomePage: React.FC = () => {
  const navigate = useNavigate()
  const [loginModalVisible, setLoginModalVisible] = useState(false)
  const [loading, setLoading] = useState(false)
  const [activeMenu, setActiveMenu] = useState<string>('home')
  const { login, isLoggedIn } = useUserStore()

  // 已登录用户自动跳转到首页
  useEffect(() => {
    if (isLoggedIn) {
      navigate('/home', { replace: true })
    }
  }, [isLoggedIn, navigate])

  const handleGetStarted = () => {
    navigate('/auth/login')
  }

  const handleLogin = async (values: any) => {
    setLoading(true)
    try {
      await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟登录
      login({} as any, 'mock-token')
      message.success('登录成功')
      setLoginModalVisible(false)
      navigate('/home')
    } catch (error) {
      message.error('登录失败，请重试')
    } finally {
      setLoading(false)
    }
  }

  const menuItems = [
    { key: 'home', label: '首页' },
    { key: 'templates', label: '简历模板' },
    { key: 'jobs', label: '招聘信息' },
    { key: 'advice', label: '求职攻略' },
    { key: 'ai', label: 'AI简历助手' },
    { key: 'feedback', label: '反馈' }
  ]

  const companies = [
    { 
      name: '阿里巴巴', 
      bgColor: '#FF6900',
      textColor: '#fff',
      link: 'https://jobs.alibaba.com/' 
    },
    { 
      name: '腾讯', 
      bgColor: '#1E6CFF',
      textColor: '#fff',
      link: 'https://careers.tencent.com/' 
    },
    { 
      name: '字节跳动', 
      bgColor: '#000000',
      textColor: '#fff',
      link: 'https://jobs.bytedance.com/' 
    },
    { 
      name: '美团', 
      bgColor: '#FFD100',
      textColor: '#333',
      link: 'https://careers.meituan.com/' 
    },
    { 
      name: '京东', 
      bgColor: '#E1251B',
      textColor: '#fff',
      link: 'https://zhaopin.jd.com/' 
    },
    { 
      name: '百度', 
      bgColor: '#2932E1',
      textColor: '#fff',
      link: 'https://talent.baidu.com/' 
    },
    { 
      name: '小米', 
      bgColor: '#FF6700',
      textColor: '#fff',
      link: 'https://hr.mi.com/' 
    },
    { 
      name: '网易', 
      bgColor: '#C20C0C',
      textColor: '#fff',
      link: 'https://hr.163.com/' 
    },
    { 
      name: '滴滴', 
      bgColor: '#FFA500',
      textColor: '#fff',
      link: 'https://careers.didiglobal.com/' 
    },
    { 
      name: '华为', 
      bgColor: '#FF0000',
      textColor: '#fff',
      link: 'https://career.huawei.com/' 
    },
    { 
      name: '微软', 
      bgColor: '#00BCF2',
      textColor: '#fff',
      link: 'https://careers.microsoft.com/' 
    },
    { 
      name: '亚马逊', 
      bgColor: '#FF9900',
      textColor: '#fff',
      link: 'https://www.amazon.jobs/' 
    }
  ]

  const features = [
    {
      icon: '📋',
      title: '海量简历模板',
      description: '专业设计师精心打造，覆盖IT、金融、教育、营销等各大行业，满足不同岗位需求',
      details: ['500+ 精美模板', '多种专业风格', '一键套用', '在线编辑']
    },
    {
      icon: '🤖',
      title: 'AI智能优化',
      description: '基于深度学习的简历分析引擎，智能识别简历问题，提供个性化优化建议',
      details: ['智能内容分析', '关键词优化', '排版建议', '效果预测']
    },
    {
      icon: '🚀',
      title: '快速投递',
      description: '一键投递至目标企业，智能匹配职位要求，提高简历通过率和面试机会',
      details: ['批量投递', '职位匹配', '投递追踪', '面试提醒']
    }
  ]

  const stats = [
    { number: '500万+', label: '用户信任' },
    { number: '1000+', label: '合作企业' },
    { number: '95%', label: '求职成功率' },
    { number: '24/7', label: 'AI助手服务' }
  ]

  const testimonials = [
    {
      name: '张同学',
      role: '应届毕业生',
      company: '阿里巴巴',
      content: '通过易投简历的AI优化，我的简历通过了阿里巴巴的筛选，成功拿到了心仪的offer。',
      avatar: '👨‍🎓'
    },
    {
      name: '李女士',
      role: '高级工程师',
      company: '腾讯',
      content: '模板非常专业，AI建议也很实用，让我的简历在众多竞争者中脱颖而出。',
      avatar: '👩‍💼'
    },
    {
      name: '王先生',
      role: '产品经理',
      company: '字节跳动',
      content: '界面简洁，功能强大，特别是批量投递功能，帮我节省了很多时间。',
      avatar: '👨‍💼'
    }
  ]

  return (
    <div className="welcome-page">
      {/* 固定导航栏 */}
      <PortalHeader activeMenu={activeMenu} onMenuClick={setActiveMenu} />

      {/* Hero 区域 */}
      <section className="hero-section">
        <div className="container">
          <div className="hero-content">
            <h1 className="hero-title">
              名企精英都在用的专业简历投递网站
            </h1>
            <p className="hero-subtitle">
              AI 加持，3 分钟快速制作一份完美简历并完成投递
            </p>
            <div className="hero-actions">
              <Button 
                type="primary" 
                size="large"
                className="cta-button primary"
                onClick={handleGetStarted}
              >
                免费生成专业简历
              </Button>
              <Button 
                size="large"
                className="cta-button secondary"
                onClick={() => navigate('/auth/register')}
              >
                立即注册
              </Button>
            </div>
            <p className="hero-description">
              写简历投简历，必用EasyApplyResume
            </p>
          </div>
          <div className="hero-stats">
            {stats.map((stat, index) => (
              <div key={index} className="stat-item">
                <div className="stat-number">{stat.number}</div>
                <div className="stat-label">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* 核心特性 */}
      <section className="features-section">
        <div className="container">
          <h2 className="section-title">为什么选择 EasyApplyResume？</h2>
          <p className="section-subtitle">专业、智能、高效的求职体验</p>
          <Row gutter={[32, 32]}>
            {features.map((feature, index) => (
              <Col xs={24} md={8} key={index}>
                <Card className="feature-card">
                  <div className="feature-icon">{feature.icon}</div>
                  <h3 className="feature-title">{feature.title}</h3>
                  <p className="feature-description">{feature.description}</p>
                  <ul className="feature-details">
                    {feature.details.map((detail, idx) => (
                      <li key={idx}>{detail}</li>
                    ))}
                  </ul>
                </Card>
              </Col>
            ))}
          </Row>
        </div>
      </section>

      {/* 公司展示 */}
      <section className="companies-section">
        <div className="container">
          <h2 className="section-title">找工作，热门公司任你选</h2>
          <p className="section-subtitle">校招+全职，靠谱企业只等你来</p>
          <Row gutter={[24, 24]}>
            {companies.map((company, index) => (
              <Col xs={8} sm={6} md={4} key={index}>
                <div className="company-item">
                  <a 
                    href={company.link} 
                    className="company-link"
                    target="_blank"
                    rel="noopener noreferrer"
                    style={{
                      backgroundColor: company.bgColor,
                      color: company.textColor
                    }}
                  >
                    <div className="company-logo-text">
                      {company.name.substring(0, 2)}
                    </div>
                    <span className="company-name">{company.name}</span>
                  </a>
                </div>
              </Col>
            ))}
          </Row>
        </div>
      </section>

      {/* 用户评价 */}
      <section className="testimonials-section">
        <div className="container">
          <h2 className="section-title">用户真实反馈</h2>
          <p className="section-subtitle">听到他们的成功故事</p>
          <Row gutter={[32, 32]}>
            {testimonials.map((testimonial, index) => (
              <Col xs={24} md={8} key={index}>
                <Card className="testimonial-card">
                  <div className="testimonial-avatar">{testimonial.avatar}</div>
                  <h4 className="testimonial-author">
                    {testimonial.name}
                    <span className="testimonial-role">
                      {testimonial.role} @ {testimonial.company}
                    </span>
                  </h4>
                  <p className="testimonial-content">{testimonial.content}</p>
                </Card>
              </Col>
            ))}
          </Row>
        </div>
      </section>

      {/* 深度特性 */}
      <section className="deep-features-section">
        <div className="container">
          <Row gutter={[48, 48]}>
            <Col xs={24} md={12}>
              <div className="feature-content">
                <h2 className="feature-title">AI 驱动的简历优化</h2>
                <ul className="feature-list">
                  <li>🎯 智能匹配职位JD，提取关键词</li>
                  <li>📊 职场大数据库支撑，优化建议更精准</li>
                  <li>🔍 实时检测简历问题，提供改进方案</li>
                  <li>📈 成功率预测，提前了解简历竞争力</li>
                  <li>⚡ 一键优化，3分钟打造完美简历</li>
                </ul>
                <Button type="primary" size="large" onClick={handleGetStarted}>
                  立即体验
                </Button>
              </div>
            </Col>
            <Col xs={24} md={12}>
              <div className="feature-content">
                <h2 className="feature-title">全方位求职管理</h2>
                <ul className="feature-list">
                  <li>📝 无限简历版本管理，针对性投递</li>
                  <li>🚀 批量投递功能，节省80%时间</li>
                  <li>📊 投递数据追踪，实时了解求职进展</li>
                  <li>🎨 个性化定制，突出你的独特优势</li>
                  <li>💼 多平台同步，随时随地管理求职</li>
                </ul>
                <Button type="primary" size="large" onClick={handleGetStarted}>
                  开始使用
                </Button>
              </div>
            </Col>
          </Row>
        </div>
      </section>

      {/* CTA区域 */}
      <section className="cta-section">
        <div className="container">
          <div className="cta-content">
            <h2>准备好了吗？</h2>
            <p>立即加入EasyApplyResume，开启你的求职新征程</p>
            <div className="cta-buttons">
              <Button className="cta-button primary" size="large" onClick={() => setLoginModalVisible(true)}>
                立即登录
              </Button>
              <Button className="cta-button secondary" size="large" onClick={() => navigate('/auth/register')}>
                免费注册
              </Button>
            </div>
          </div>
        </div>
      </section>

      {/* PortalFooter */}
      <PortalFooter />

      {/* 快速登录弹窗 */}
      <Modal
        title={null}
        open={loginModalVisible}
        onCancel={() => setLoginModalVisible(false)}
        footer={null}
        width={500}
        centered
        className="login-modal"
        closeIcon={<span style={{ color: '#999', fontSize: '24px' }}>×</span>}
      >
        <div className="login-modal-content">
          <div className="login-header">
            <div className="login-title">登录到易投简历</div>
            <div className="login-subtitle">使用你的账号继续</div>
          </div>
          
          <Form onFinish={handleLogin} layout="vertical" className="login-form">
            <Form.Item 
              name="username" 
              rules={[{ required: true, message: '请输入用户名' }]}
              className="form-item-custom"
            >
              <Input 
                prefix={<UserOutlined className="input-icon" />} 
                placeholder="用户名/手机号/邮箱" 
                size="large"
                className="login-input"
              />
            </Form.Item>
            <Form.Item 
              name="password" 
              rules={[{ required: true, message: '请输入密码' }]}
              className="form-item-custom"
            >
              <Input.Password 
                prefix={<LockOutlined className="input-icon" />} 
                placeholder="密码" 
                size="large"
                className="login-input"
              />
            </Form.Item>
            <Form.Item className="form-actions">
              <Button type="primary" htmlType="submit" block size="large" className="login-button" loading={loading}>
                登录
              </Button>
            </Form.Item>
          </Form>
          
          <div className="login-switch">
            <span className="switch-text">还没有账号？</span>
            <span className="switch-link" onClick={() => { setLoginModalVisible(false); navigate('/auth/register'); }}>
              立即注册
            </span>
          </div>

          <div className="divider-wrapper">
            <Divider className="login-divider">
              <span className="divider-text">或</span>
            </Divider>
          </div>

          <div className="alternative-login">
            <div className="alternative-item">
              <PhoneOutlined className="alternative-icon" />
              <span>手机验证码登录</span>
            </div>
            <div className="alternative-item">
              <MailOutlined className="alternative-icon" />
              <span>邮箱验证码登录</span>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  )
}

export default WelcomePage
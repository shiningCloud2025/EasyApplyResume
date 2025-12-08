import React from 'react'
import { Button, Row, Col, Card } from 'antd'
import { useNavigate } from 'react-router-dom'
import './WelcomePage.scss'

const WelcomePage: React.FC = () => {
  const navigate = useNavigate()

  const handleGetStarted = () => {
    navigate('/auth/login')
  }

  const companies = [
    { name: '阿里巴巴', link: '#' },
    { name: '腾讯', link: '#' },
    { name: '字节跳动', link: '#' },
    { name: '美团', link: '#' },
    { name: '京东', link: '#' },
    { name: '百度', link: '#' },
    { name: '小米', link: '#' },
    { name: '网易', link: '#' },
    { name: '滴滴', link: '#' }
  ]

  return (
    <div className="welcome-page">
      {/* 导航栏 */}
      <header className="welcome-header">
        <div className="container">
          <div className="header-content">
            <h1 className="logo">易投简历</h1>
            <nav className="nav-links">
              <span onClick={() => navigate('/auth/login')}>登录</span>
              <span onClick={() => navigate('/auth/register')}>注册</span>
            </nav>
          </div>
        </div>
      </header>

      {/* 主要内容 */}
      <main className="welcome-main">
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
              <Button 
                type="primary" 
                size="large"
                className="cta-button"
                onClick={handleGetStarted}
              >
                免费生成专业简历
              </Button>
              <p className="hero-description">
                写简历投简历，必用EasyApplyResume
              </p>
            </div>
          </div>
        </section>

        {/* 特性展示 */}
        <section className="features-section">
          <div className="container">
            <Row gutter={[32, 32]}>
              <Col xs={24} md={8}>
                <Card className="feature-card">
                  <div className="feature-icon">📝</div>
                  <h3>海量简历模版</h3>
                  <p>专业设计师精心打造的简历模板，覆盖各行各业，助你脱颖而出</p>
                </Card>
              </Col>
              <Col xs={24} md={8}>
                <Card className="feature-card">
                  <div className="feature-icon">🤖</div>
                  <h3>AI简历智能优化</h3>
                  <p>基于AI技术智能分析简历内容，提供个性化优化建议</p>
                </Card>
              </Col>
              <Col xs={24} md={8}>
                <Card className="feature-card">
                  <div className="feature-icon">⚡</div>
                  <h3>快速投递</h3>
                  <p>一键投递至目标企业，高效求职，快速获得心仪offer</p>
                </Card>
              </Col>
            </Row>
          </div>
        </section>

        {/* 公司展示 */}
        <section className="companies-section">
          <div className="container">
            <h2 className="section-title">
              找工作，热门公司任你选
            </h2>
            <p className="section-subtitle">
              校招+全职，靠谱企业只等你来
            </p>
            <Row gutter={[16, 16]}>
              {companies.map((company, index) => (
                <Col xs={8} md={8} key={index}>
                  <div className="company-item">
                    <a href={company.link} className="company-link">
                      {company.name}
                    </a>
                  </div>
                </Col>
              ))}
            </Row>
          </div>
        </section>
      </main>

      {/* 底部 */}
      <footer className="welcome-footer">
        <div className="container">
          <p>&copy; 2025 易投简历. All rights reserved.</p>
        </div>
      </footer>
    </div>
  )
}

export default WelcomePage
import React, { useState } from 'react'
import { Button, Row, Col, Typography, Card, Space } from 'antd'
import { ArrowRightOutlined, FileTextOutlined, ThunderboltOutlined, RocketOutlined, CheckCircleOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useUserStore } from '@stores/userStore'

const { Title, Paragraph } = Typography

const HomePage: React.FC = () => {
  const navigate = useNavigate()
  const { isLoggedIn } = useUserStore()

  const handleCreateResume = () => {
    if (isLoggedIn) {
      navigate('/resume/my-resumes')
    } else {
      navigate('/auth/login')
    }
  }

  const companies = [
    { name: '阿里巴巴', url: '#alibaba', logo: 'https://img.alicdn.com/tfs/TB1_ZXuNcfpK1RjSZFOXXa6nFXa-32-32.ico' },
    { name: '腾讯', url: '#tencent', logo: 'https://mat1.gtimg.com/www/icon/favicon2.ico' },
    { name: '百度', url: '#baidu', logo: 'https://www.baidu.com/favicon.ico' },
    { name: '字节跳动', url: '#bytedance', logo: 'https://lf1-cdn-tos.bytegoofy.com/obj/iconpark/icons_19361_1.svg' },
    { name: '美团', url: '#meituan', logo: 'https://p0.meituan.net/1.10.1/mc.webp' },
    { name: '滴滴', url: '#didi', logo: 'https://www.didiglobal.com/favicon.ico' },
    { name: '京东', url: '#jd', logo: 'https://www.jd.com/favicon.ico' },
    { name: '网易', url: '#netease', logo: 'https://www.163.com/favicon.ico' },
    { name: '华为', url: '#huawei', logo: 'https://www.huawei.com/favicon.ico' },
  ]

  const features = [
    {
      icon: <FileTextOutlined style={{ fontSize: 48, color: '#1890ff' }} />,
      title: '海量简历模版',
      description: '专业设计师打造，多种行业模板任选，满足不同求职需求。',
      benefits: ['涵盖所有行业', '专业设计', '在线编辑', '一键生成']
    },
    {
      icon: <ThunderboltOutlined style={{ fontSize: 48, color: '#52c41a' }} />,
      title: 'AI简历智能优化',
      description: 'AI智能分析简历内容，提供针对性优化建议，提升简历竞争力。',
      benefits: ['智能分析', '个性化建议', '内容优化', '竞争力提升']
    },
    {
      icon: <RocketOutlined style={{ fontSize: 48, color: '#fa8c16' }} />,
      title: '快速投递',
      description: '一键投递到多家企业，投递进度实时跟踪，求职效率大幅提升。',
      benefits: ['一键投递', '批量发送', '进度跟踪', '效率提升']
    }
  ]

  return (
    <div className="home-page-portal">
      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-content">
          <Title level={1} className="hero-title">
            名企精英都在用的专业简历投递网站
          </Title>
          <Paragraph className="hero-subtitle">
            AI 加持，3 分钟快速制作一份完美简历并完成投递
          </Paragraph>
          <Button 
            type="primary" 
            size="large"
            icon={<ArrowRightOutlined />}
            className="hero-button"
            onClick={handleCreateResume}
          >
            免费生成专业简历
          </Button>
          <Paragraph className="hero-text">
            写简历投简历，必用EasyApplyResume
          </Paragraph>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="container">
          <Title level={2} className="section-title text-center">
            三大核心功能，助力求职成功
          </Title>
          
          <Row gutter={[32, 32]} className="features-row">
            {features.map((feature, index) => (
              <Col xs={24} md={8} key={index}>
                <Card className="feature-card" bordered={false}>
                  <div className="feature-icon">
                    {feature.icon}
                  </div>
                  <Title level={3} className="feature-title">
                    {feature.title}
                  </Title>
                  <Paragraph className="feature-description">
                    {feature.description}
                  </Paragraph>
                  <div className="feature-benefits">
                    {feature.benefits.map((benefit, benefitIndex) => (
                      <div key={benefitIndex} className="benefit-item">
                        <CheckCircleOutlined className="benefit-icon" />
                        <span>{benefit}</span>
                      </div>
                    ))}
                  </div>
                </Card>
              </Col>
            ))}
          </Row>
        </div>
      </section>

      {/* Companies Section */}
      <section className="companies-section">
        <div className="container">
          <Title level={2} className="section-title text-center">
            找工作，热门公司任你选
          </Title>
          <Paragraph className="section-subtitle text-center">
            校招+全职，靠谱企业只等你来
          </Paragraph>
          
          <Row gutter={[24, 16]} className="companies-grid">
            {companies.map((company, index) => (
              <Col xs={8} sm={8} md={8} lg={8} key={index}>
                <div className="company-card">
                  <a 
                    href={company.url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="company-link"
                  >
                    <img 
                      src={company.logo} 
                      alt={company.name}
                      className="company-logo"
                      onError={(e) => {
                        const target = e.target as HTMLImageElement
                        target.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzIiIGhlaWdodD0iMzIiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjMyIiBoZWlnaHQ9IjMyIiByeD0iOCIgZmlsbD0iIzE4OTBmZiIvPgo8dGV4dCB4PSIxNiIgeT0iMjAiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGZpbGw9IndoaXRlIiBmb250LXNpemU9IjEyIj7kuIrkuK3lvankuI08L3RleHQ+Cjwvc3ZnPgo='
                      }}
                    />
                    <span className="company-name">{company.name}</span>
                  </a>
                </div>
              </Col>
            ))}
          </Row>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container">
          <div className="cta-content">
            <Title level={2} className="cta-title">
              还在等什么？
            </Title>
            <Paragraph className="cta-subtitle">
              立即加入数万求职者，用专业的简历开启您的职业新征程
            </Paragraph>
            <Space size="large">
              <Button 
                type="primary" 
                size="large"
                onClick={handleCreateResume}
              >
                开始制作简历
              </Button>
              <Button 
                size="large"
                onClick={() => navigate('/resume/templates')}
              >
                浏览模板
              </Button>
            </Space>
          </div>
        </div>
      </section>

      {/* Footer */}
    </div>
  )
}

export default HomePage
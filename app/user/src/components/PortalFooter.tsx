import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import { WechatOutlined, QqOutlined, WeiboOutlined, PhoneOutlined, MailOutlined, EnvironmentOutlined, UpOutlined } from '@ant-design/icons'
import { Button } from 'antd'
import './PortalFooter.scss'

const PortalFooter: React.FC = () => {
  const [email, setEmail] = useState('')
  const [subscribed, setSubscribed] = useState(false)

  const handleSubscribe = () => {
    if (email) {
      // 这里可以添加订阅API调用
      setSubscribed(true)
      setEmail('')
      setTimeout(() => setSubscribed(false), 3000)
    }
  }

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const productLinks = [
    { name: '简历模板', path: '/resume/templates', desc: '海量精美模板' },
    { name: 'AI简历优化', path: '/ai', desc: '智能优化建议' },
    { name: '招聘信息', path: '/jobs', desc: '最新职位机会' },
    { name: '求职攻略', path: '/advice', desc: '职场经验分享' },
    { name: '我的简历', path: '/resume/my-resumes', desc: '简历管理中心' },
    { name: '用户反馈', path: '/feedback/submit', desc: '意见建议' }
  ]

  const aboutLinks = [
    { name: '公司介绍', href: '#about', desc: '了解我们' },
    { name: '团队介绍', href: '#team', desc: '核心团队' },
    { name: '发展历程', href: '#history', desc: '成长足迹' },
    { name: '加入我们', href: '#careers', desc: '招贤纳士' },
    { name: '合作伙伴', href: '#partners', desc: '合作共赢' },
    { name: '媒体报道', href: '#media', desc: '新闻动态' }
  ]

  const helpLinks = [
    { name: '使用指南', href: '#guide', desc: '新手入门' },
    { name: '常见问题', href: '#faq', desc: 'FAQ解答' },
    { name: '联系客服', href: '#service', desc: '人工客服' },
    { name: '意见反馈', href: '#feedback', desc: '问题反馈' },
    { name: 'API文档', href: '#api', desc: '开发文档' },
    { name: '服务状态', href: '#status', desc: '系统状态' }
  ]

  const legalLinks = [
    { name: '服务条款', href: '#terms', desc: '用户协议' },
    { name: '隐私政策', href: '#privacy', desc: '隐私保护' },
    { name: '版权声明', href: '#copyright', desc: '知识产权' },
    { name: '免责声明', href: '#disclaimer', desc: '责任声明' },
    { name: '知识产权', href: '#ip', desc: 'IP保护' },
    { name: 'Cookie政策', href: '#cookies', desc: 'Cookie使用' }
  ]

  return (
    <footer className="portal-footer">
      {/* 返回顶部按钮 */}
      <Button 
        type="primary" 
        shape="circle" 
        icon={<UpOutlined />} 
        className="back-to-top"
        onClick={scrollToTop}
      />
      
      {/* 主要内容 */}
      <div className="footer-main">
        <div className="container">
          <div className="footer-grid">
            {/* 公司信息 */}
            <div className="footer-section company-info">
              <div className="company-brand">
                <div className="logo">📄 易投简历</div>
                <p className="slogan">
                  AI驱动，让求职更简单，让未来更精彩
                </p>
                <p className="description">
                  专注为求职者提供智能化简历制作与投递服务，携手千万用户共赴职场新征程
                </p>
              </div>
              
              {/* 联系方式 */}
              <div className="contact-methods">
                <div className="contact-item">
                  <MailOutlined />
                  <span>service@easyapplyresume.com (待定)</span>
                </div>
                <div className="contact-item">
                  <PhoneOutlined />
                  <span>400-123-4567 (待定)</span>
                </div>
                <div className="contact-item">
                  <EnvironmentOutlined />
                  <span>地址：待定</span>
                </div>
              </div>
              
              {/* 社交媒体 */}
              <div className="social-section">
                <h4>关注我们</h4>
                <div className="social-links">
                  <a href="#" className="social-btn" title="微信公众号">
                    <WechatOutlined />
                  </a>
                  <a href="#" className="social-btn" title="QQ群">
                    <QqOutlined />
                  </a>
                  <a href="#" className="social-btn" title="微博">
                    <WeiboOutlined />
                  </a>
                </div>
              </div>
            </div>

            {/* 产品服务 */}
            <div className="footer-section">
              <h4 className="section-title">产品服务</h4>
              <div className="link-grid">
                {productLinks.map((link, index) => (
                  <Link key={index} to={link.path} className="footer-link">
                    <span className="link-name">{link.name}</span>
                    <span className="link-desc">{link.desc}</span>
                  </Link>
                ))}
              </div>
            </div>

            {/* 关于我们 */}
            <div className="footer-section">
              <h4 className="section-title">关于我们</h4>
              <div className="link-grid">
                {aboutLinks.map((link, index) => (
                  <a key={index} href={link.href} className="footer-link">
                    <span className="link-name">{link.name}</span>
                    <span className="link-desc">{link.desc}</span>
                  </a>
                ))}
              </div>
            </div>

            {/* 帮助中心 */}
            <div className="footer-section">
              <h4 className="section-title">帮助中心</h4>
              <div className="link-grid">
                {helpLinks.map((link, index) => (
                  <a key={index} href={link.href} className="footer-link">
                    <span className="link-name">{link.name}</span>
                    <span className="link-desc">{link.desc}</span>
                  </a>
                ))}
              </div>
            </div>
          </div>

          {/* 订阅区域 */}
          <div className="footer-newsletter">
            <div className="newsletter-content">
              <div className="newsletter-info">
                <h4>订阅我们的资讯</h4>
                <p>获取最新职场资讯、面试技巧和求职攻略</p>
              </div>
              <div className="newsletter-form">
                <input
                  type="email"
                  placeholder="请输入您的邮箱"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSubscribe()}
                />
                <Button type="primary" onClick={handleSubscribe}>
                  {subscribed ? '✓ 已订阅' : '立即订阅'}
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 底部版权 */}
      <div className="footer-bottom">
        <div className="container">
          <div className="bottom-content">
            <div className="copyright">
              <p>&copy; 2025 EasyApplyResume. All rights reserved.</p>
              <p>ICP备案号：待定</p>
            </div>
            
            <div className="legal-links">
              {legalLinks.map((link, index) => (
                <React.Fragment key={index}>
                  <a href={link.href}>{link.name}</a>
                  {index < legalLinks.length - 1 && <span className="separator">·</span>}
                </React.Fragment>
              ))}
            </div>

            <div className="badges">
              <span className="badge">安全认证</span>
              <span className="badge">可信网站</span>
              <span className="badge">企业认证</span>
            </div>
          </div>
        </div>
      </div>
    </footer>
  )
}

export default PortalFooter
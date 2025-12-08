import React from 'react'
import { Link } from 'react-router-dom'

interface PortalFooterProps {}

const PortalFooter: React.FC<PortalFooterProps> = () => {
  return (
    <footer className="portal-footer">
      <div className="footer-content">
        <div className="footer-section">
          <h4>产品服务</h4>
          <ul>
            <li><Link to="/resume/templates">简历模版</Link></li>
            <li><Link to="/jobs">招聘信息</Link></li>
            <li><Link to="/advice">求职攻略</Link></li>
            <li><Link to="/ai">AI简历助手</Link></li>
          </ul>
        </div>
        
        <div className="footer-section">
          <h4>关于我们</h4>
          <ul>
            <li><a href="#about">公司介绍</a></li>
            <li><a href="#contact">联系我们</a></li>
            <li><a href="#join">加入我们</a></li>
            <li><a href="#partner">合作伙伴</a></li>
          </ul>
        </div>
        
        <div className="footer-section">
          <h4>帮助中心</h4>
          <ul>
            <li><a href="#help">使用指南</a></li>
            <li><a href="#faq">常见问题</a></li>
            <li><a href="#feedback">意见反馈</a></li>
            <li><a href="#api">API文档</a></li>
          </ul>
        </div>
        
        <div className="footer-section">
          <h4>联系方式</h4>
          <div className="contact-info">
            <p>📧 service@easyapplyresume.com</p>
            <p>📱 400-123-4567</p>
            <p>📍 北京市朝阳区xxx大厦</p>
          </div>
          <div className="social-links">
            <a href="#" title="微信">📱</a>
            <a href="#" title="QQ">💬</a>
            <a href="#" title="微博">🌐</a>
          </div>
        </div>
      </div>
      
      <div className="footer-bottom">
        <div className="bottom-content">
          <p>© 2025 易投简历. All rights reserved.</p>
          <div className="links">
            <a href="#privacy">隐私政策</a>
            <span>·</span>
            <a href="#terms">服务条款</a>
            <span>·</span>
            <a href="#sitemap">网站地图</a>
          </div>
        </div>
      </div>
    </footer>
  )
}

export default PortalFooter
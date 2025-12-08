import React, { useState } from 'react'
import { Link, useNavigate, useLocation, Outlet } from 'react-router-dom'
import { Button, Space, Dropdown, message } from 'antd'
import { UserOutlined, LogoutOutlined, MenuOutlined } from '@ant-design/icons'
import { useUserStore } from '@stores/userStore'
import './MainLayout.scss'

const MainLayout: React.FC = () => {
  const navigate = useNavigate()
  const { user, logout } = useUserStore()
  const location = useLocation()
  const [mobileMenuVisible, setMobileMenuVisible] = useState(false)

  const navItems = [
    { key: '/resume/my-resumes', label: '我的简历' },
    { key: '/resume/templates', label: '简历模版' },
    { key: '/jobs', label: '招聘信息' },
    { key: '/advice', label: '求职攻略' },
    { key: '/ai', label: 'AI简历助手' },
    { key: '/feedback/submit', label: '反馈' }
  ]

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心',
      onClick: () => navigate('/profile'),
    },
    {
      type: 'divider' as const,
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: () => {
        logout()
        navigate('/auth/login')
      },
    },
  ]

  const handleNavigate = (path: string) => {
    navigate(path)
    setMobileMenuVisible(false)
  }

  const isActiveRoute = (path: string) => {
    if (path === location.pathname) return true
    
    if (path === '/advice' && location.pathname.startsWith('/advice')) return true
    if (path === '/ai' && location.pathname.startsWith('/ai')) return true
    if (path === '/feedback/submit' && location.pathname.startsWith('/feedback')) return true
    
    return false
  }

  const getDisplayName = () => {
    const path = location.pathname
    if (path === '/resume/my-resumes') return '我的简历'
    if (path === '/resume/templates') return '简历模版'
    if (path === '/resume/edit') return '简历编辑'
    if (path === '/jobs') return '招聘信息'
    if (path.startsWith('/job/')) return '职位详情'
    if (path === '/advice') return '求职攻略'
    if (path.startsWith('/advice/')) return '攻略详情'
    if (path === '/ai') return 'AI简历助手'
    if (path.startsWith('/feedback/')) return '反馈'
    if (path === '/profile') return '个人中心'
    return '易投简历'
  }

  return (
    <div className="main-layout-portal">
      {/* Header */}
      <header className="main-header">
        <div className="header-content">
          <div className="header-left">
            <div className="logo" onClick={() => navigate('/home')}>
              📄 易投简历
            </div>
            <span className="page-title">{getDisplayName()}</span>
          </div>
          
          {/* Desktop Navigation */}
          <nav className="desktop-nav">
            <ul className="nav-list">
              <li className={`nav-item ${isActiveRoute('/resume/my-resumes') ? 'active' : ''}`}>
                <button onClick={() => handleNavigate('/resume/my-resumes')}>
                  我的简历
                </button>
              </li>
              <li className={`nav-item ${isActiveRoute('/resume/templates') ? 'active' : ''}`}>
                <button onClick={() => handleNavigate('/resume/templates')}>
                  简历模版
                </button>
              </li>
              <li className={`nav-item ${isActiveRoute('/jobs') ? 'active' : ''}`}>
                <button onClick={() => handleNavigate('/jobs')}>
                  招聘信息
                </button>
              </li>
              <li className={`nav-item ${isActiveRoute('/advice') ? 'active' : ''}`}>
                <button onClick={() => handleNavigate('/advice')}>
                  求职攻略
                </button>
              </li>
              <li className={`nav-item ${isActiveRoute('/ai') ? 'active' : ''}`}>
                <button onClick={() => handleNavigate('/ai')}>
                  AI简历助手
                </button>
              </li>
              <li className={`nav-item ${isActiveRoute('/feedback/submit') ? 'active' : ''}`}>
                <button onClick={() => handleNavigate('/feedback/submit')}>
                  反馈
                </button>
              </li>
            </ul>
          </nav>

          {/* User Menu & Mobile Menu Toggle */}
          <div className="header-right">
            {user && (
              <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
                <div className="user-profile">
                  <span className="username">{user.userUsername}</span>
                  <div className="avatar">
                    {user.userImage ? (
                      <img src={user.userImage} alt="avatar" />
                    ) : (
                      <UserOutlined />
                    )}
                  </div>
                </div>
              </Dropdown>
            )}
            
            {/* Mobile Menu Toggle */}
            <Button
              className="mobile-menu-toggle"
              type="text"
              icon={<MenuOutlined />}
              onClick={() => setMobileMenuVisible(!mobileMenuVisible)}
            />
          </div>
        </div>
      </header>

      {/* Mobile Navigation */}
      <nav className={`mobile-nav ${mobileMenuVisible ? 'visible' : ''}`}>
        <div className="mobile-nav-content">
          <div className="mobile-user-info">
            {user && (
              <div className="user-profile">
                <div className="avatar">
                  {user.userImage ? (
                    <img src={user.userImage} alt="avatar" />
                  ) : (
                    <UserOutlined />
                  )}
                </div>
                <span className="username">{user.userUsername}</span>
              </div>
            )}
          </div>
          
          <ul className="mobile-nav-list">
            {navItems.map((item) => (
              <li key={item.key} className="mobile-nav-item">
                <button
                  className={`mobile-nav-link ${isActiveRoute(item.key) ? 'active' : ''}`}
                  onClick={() => handleNavigate(item.key)}
                >
                  {item.label}
                </button>
              </li>
            ))}
          </ul>
        </div>
      </nav>

      {/* Main Content */}
      <main className="main-content">
        <div className="content-wrapper">
          <Outlet />
        </div>
      </main>

      {/* Footer */}
      <footer className="main-footer">
        <div className="footer-content">
          <p>© 2025 易投简历. All rights reserved.</p>
        </div>
      </footer>
    </div>
  )
}

export default MainLayout
import React, { useState, useEffect } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { Button, Space, Dropdown, message, Input } from 'antd'
import { UserOutlined, LogoutOutlined, SearchOutlined } from '@ant-design/icons'
import { useUserStore } from '@stores/userStore'
import './PortalHeader.scss'

interface PortalHeaderProps {
  activeMenu?: string
  onMenuClick?: (menu: string) => void
}

const PortalHeader: React.FC<PortalHeaderProps> = ({ activeMenu, onMenuClick }) => {
  const navigate = useNavigate()
  const { user, isLoggedIn, logout } = useUserStore()
  const location = useLocation()
  const [isSticky, setIsSticky] = useState(false)
  const [searchVisible, setSearchVisible] = useState(false)

  useEffect(() => {
    const handleScroll = () => {
      setIsSticky(window.scrollY > 50)
    }
    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

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

  const handleNavigate = (path: string, menuKey?: string) => {
    if (onMenuClick && menuKey) {
      onMenuClick(menuKey)
    }
    
    if (path === '/my-resumes' && !isLoggedIn) {
      message.info('请先登录')
      navigate('/auth/login')
    } else if (path === '/my-resumes' && isLoggedIn) {
      navigate('/resume/my-resumes')
    } else if (path.startsWith('/resume') || path.startsWith('/jobs') || path.startsWith('/advice') || path.startsWith('/ai')) {
      navigate(path)
    } else {
      navigate(path)
    }
  }

  const portalMenuItems = [
    { key: 'home', label: '首页', path: '/' },
    { key: 'templates', label: '简历模板', path: '/resume/templates' },
    { key: 'jobs', label: '招聘信息', path: '/jobs' },
    { key: 'advice', label: '求职攻略', path: '/advice' },
    { key: 'ai', label: 'AI简历助手', path: '/ai' },
    { key: 'feedback', label: '反馈', path: '/feedback/submit' }
  ]

  const isActiveRoute = (path: string) => {
    return location.pathname === path || 
           (activeMenu && portalMenuItems.find(item => item.key === activeMenu)?.path === path)
  }

  return (
    <header className={`portal-header ${isSticky ? 'sticky' : ''}`}>
      <div className="header-content">
        <div className="header-left">
          <div className="logo" onClick={() => handleNavigate('/')}>
            📄 易投简历
          </div>
        </div>
        
        <nav className="header-nav">
          <ul className="nav-list">
            {portalMenuItems.map(item => (
              <li 
                key={item.key} 
                className={`nav-item ${isActiveRoute(item.path) ? 'active' : ''}`}
              >
                <button 
                  className="nav-link"
                  onClick={() => handleNavigate(item.path, item.key)}
                >
                  {item.label}
                </button>
              </li>
            ))}
          </ul>
        </nav>

        <div className="header-right">
          <div className="header-actions">
            <Button 
              type="text" 
              icon={<SearchOutlined />}
              className="search-btn"
              onClick={() => setSearchVisible(!searchVisible)}
            />
            
            {isLoggedIn ? (
              <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
                <div className="user-profile">
                  <span className="username">{user?.userUsername || '用户'}</span>
                  <div className="avatar">
                    {user?.userImage ? (
                      <img src={user.userImage} alt="avatar" />
                    ) : (
                      <UserOutlined />
                    )}
                  </div>
                </div>
              </Dropdown>
            ) : (
              <Space>
                <Button 
                  onClick={() => handleNavigate('/auth/login', 'login')}
                  type="text"
                >
                  登录
                </Button>
                <Button 
                  onClick={() => handleNavigate('/auth/register', 'register')}
                  type="primary"
                >
                  注册
                </Button>
              </Space>
            )}
          </div>
        </div>
      </div>
      
      {/* 搜索栏 */}
      {searchVisible && (
        <div className="search-bar">
          <Input.Search
            placeholder="搜索简历模板、职位或攻略..."
            style={{ maxWidth: 600 }}
            size="large"
            onSearch={(value) => {
              if (value) {
                // 这里可以添加搜索逻辑
                message.info('搜索功能开发中...')
              }
              setSearchVisible(false)
            }}
            onBlur={() => setTimeout(() => setSearchVisible(false), 200)}
          />
        </div>
      )}
    </header>
  )
}

export default PortalHeader
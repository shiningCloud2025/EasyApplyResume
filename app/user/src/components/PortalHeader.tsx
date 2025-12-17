import React, { useState, useEffect } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { Button, Space, Dropdown, message, Input } from 'antd'
import { UserOutlined, LogoutOutlined, SearchOutlined, DownOutlined } from '@ant-design/icons'
import { useUserStore } from '@stores/userStore'
import { userAPI } from '@api/feedback'
import './PortalHeader.scss'

interface PortalHeaderProps {
  activeMenu?: string
  onMenuClick?: (menu: string) => void
}

const PortalHeader: React.FC<PortalHeaderProps> = ({ activeMenu, onMenuClick }) => {
  const navigate = useNavigate()
  const { user, isLoggedIn, logout, getUserById } = useUserStore()
  const location = useLocation()
  const [isSticky, setIsSticky] = useState(false)

  useEffect(() => {
    const handleScroll = () => {
      setIsSticky(window.scrollY > 50)
    }
    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

  // 页面加载/刷新时，自动获取用户信息填充用户名和头像
  useEffect(() => {
    const fetchUserInfo = async () => {
      if (isLoggedIn && user?.userId) {
        try {
          console.log('🔄 页面加载，自动获取用户信息...')
          await getUserById(String(user.userId))
          console.log('✅ 用户信息获取成功')
        } catch (error) {
          console.error('❌ 自动获取用户信息失败:', error)
        }
      }
    }
    fetchUserInfo()
  }, [isLoggedIn, user?.userId, getUserById])

  // 点击个人中心时调用 getUserByUserId 接口
  const handleGoToProfile = async () => {
    if (user?.userId) {
      try {
        console.log('👤 点击个人中心，获取用户详细信息...')
        const res = await userAPI.getUserById(String(user.userId))
        if (res.data) {
          console.log('✅ 用户详细信息:', res.data)
          // 更新 store 中的用户信息
          await getUserById(String(user.userId))
        }
      } catch (error) {
        console.error('❌ 获取用户信息失败:', error)
      }
    }
    navigate('/profile')
  }

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心',
      onClick: handleGoToProfile,
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

  const handleNavigate = (path: string, menuKey?: string, requireLogin?: boolean) => {
    if (onMenuClick && menuKey) {
      onMenuClick(menuKey)
    }
    
    // 需要登录的菜单项
    if (requireLogin && !isLoggedIn) {
      message.info('请先登录')
      navigate('/auth/login')
      return
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
    { key: 'home', label: '首页', path: '/home' },
    { key: 'my-resumes', label: '我的简历', path: '/resume/my-resumes', requireLogin: true },
    { key: 'templates', label: '简历模板', path: '/resume/templates' },
    { key: 'jobs', label: '招聘信息', path: '/jobs' },
    { key: 'advice', label: '求职攻略', path: '/advice' },
    { 
      key: 'ai', 
      label: 'AI简历助手', 
      path: '/ai/chat',
      children: [
        { key: 'ai-chat', label: 'AI智能问答助手', path: '/ai/chat' },
        { key: 'ai-agent', label: 'AI智能体助手', path: '/ai/agent' }
      ]
    },
    { key: 'feedback', label: '反馈', path: '/feedback/submit' }
  ]

  const isActiveRoute = (path: string, item?: any) => {
    // 检查是否有子菜单
    if (item?.children) {
      return item.children.some((child: any) => location.pathname === child.path)
    }
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
                className={`nav-item ${isActiveRoute(item.path, item) ? 'active' : ''}`}
              >
                {(item as any).children ? (
                  <Dropdown 
                    menu={{ 
                      items: (item as any).children.map((child: any) => ({
                        key: child.key,
                        label: child.label,
                        onClick: () => handleNavigate(child.path, child.key)
                      }))
                    }}
                    placement="bottom"
                  >
                    <button className="nav-link nav-dropdown">
                      {item.label} <DownOutlined style={{ fontSize: 10, marginLeft: 4 }} />
                    </button>
                  </Dropdown>
                ) : (
                  <button 
                    className="nav-link"
                    onClick={() => handleNavigate(item.path, item.key, (item as any).requireLogin)}
                  >
                    {item.label}
                  </button>
                )}
              </li>
            ))}
          </ul>
        </nav>

        <div className="header-right">
          <div className="header-actions">
            {/* 搜索框固定显示 */}
            <Input
              placeholder="搜索..."
              className="header-search"
              prefix={<SearchOutlined style={{ color: '#999' }} />}
              style={{ width: 200 }}
              onPressEnter={(e) => {
                const value = (e.target as HTMLInputElement).value
                if (value) {
                  message.info('搜索功能开发中...')
                }
              }}
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
    </header>
  )
}

export default PortalHeader
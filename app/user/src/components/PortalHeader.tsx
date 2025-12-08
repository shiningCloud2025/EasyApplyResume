import React, { useState, useEffect } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { Button, Space, Dropdown, message } from 'antd'
import { UserOutlined, LogoutOutlined } from '@ant-design/icons'
import { useUserStore } from '@stores/userStore'
import './PortalHeader.scss'

const PortalHeader: React.FC = () => {
  const navigate = useNavigate()
  const { user, isLoggedIn, logout } = useUserStore()
  const location = useLocation()

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
    if (path === '/my-resumes' && !isLoggedIn) {
      message.info('请先登录')
      navigate('/auth/login')
    } else if (path === '/my-resumes' && isLoggedIn) {
      navigate('/resume/my-resumes')
    } else {
      navigate(path)
    }
  }

  const isActiveRoute = (path: string) => {
    return location.pathname === path
  }

  return (
    <header className="portal-header">
      <div className="header-content">
        <div className="header-left">
          <div className="logo" onClick={() => navigate('/home')}>
            📄 易投简历
          </div>
        </div>
        
        <nav className="header-nav">
          <ul className="nav-list">
            <li className={`nav-item ${isActiveRoute('/my-resumes') ? 'active' : ''}`}>
              <button 
                className="nav-link"
                onClick={() => handleNavigate('/my-resumes')}
              >
                我的简历
              </button>
            </li>
            <li className={`nav-item ${isActiveRoute('/resume/templates') ? 'active' : ''}`}>
              <button 
                className="nav-link"
                onClick={() => handleNavigate('/resume/templates')}
              >
                简历模版
              </button>
            </li>
            <li className={`nav-item ${isActiveRoute('/jobs') ? 'active' : ''}`}>
              <button 
                className="nav-link"
                onClick={() => handleNavigate('/jobs')}
              >
                招聘信息
              </button>
            </li>
            <li className={`nav-item ${isActiveRoute('/advice') ? 'active' : ''}`}>
              <button 
                className="nav-link"
                onClick={() => handleNavigate('/advice')}
              >
                求职攻略
              </button>
            </li>
            <li className={`nav-item ${isActiveRoute('/ai') ? 'active' : ''}`}>
              <button 
                className="nav-link"
                onClick={() => handleNavigate('/ai')}
              >
                AI简历助手
              </button>
            </li>
          </ul>
        </nav>

        <div className="header-right">
          {isLoggedIn ? (
            <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
              <div className="user-profile">
                <span className="username">{user?.userUsername}</span>
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
                onClick={() => navigate('/auth/login')}
                type="text"
              >
                登录
              </Button>
              <Button 
                onClick={() => navigate('/auth/register')}
                type="primary"
              >
                注册
              </Button>
            </Space>
          )}
        </div>
      </div>
    </header>
  )
}

export default PortalHeader
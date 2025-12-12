import React, { useEffect } from 'react'
import { Outlet, useLocation } from 'react-router-dom'
import { Layout } from 'antd'

const { Content } = Layout

const AuthLayout: React.FC = () => {
  console.log('🔑 AuthLayout 渲染')
  const location = useLocation()
  
  // 监听路由变化，每次切换都滚动到顶部
  useEffect(() => {
    console.log('🔄 路由切换，滚动到顶部:', location.pathname)
    window.scrollTo(0, 0)
    document.documentElement.scrollTop = 0
    document.body.scrollTop = 0
  }, [location.pathname])
  
  return (
    <Layout className="auth-layout">
      <Content className="auth-content">
        <div className="auth-container">
          <Outlet />
        </div>
      </Content>
    </Layout>
  )
}

export default AuthLayout
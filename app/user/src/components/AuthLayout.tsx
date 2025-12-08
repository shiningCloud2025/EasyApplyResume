import React from 'react'
import { Outlet } from 'react-router-dom'
import { Layout } from 'antd'

const { Content } = Layout

const AuthLayout: React.FC = () => {
  console.log('🔑 AuthLayout 渲染')
  
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
import React from 'react'
import { Result, Button } from 'antd'
import { useNavigate } from 'react-router-dom'

const NotFound: React.FC = () => {
  const navigate = useNavigate()

  return (
    <div className="error-page">
      <Result
        status="404"
        title="404"
        subTitle="抱歉，您访问的页面不存在。"
        extra={
          <Button type="primary" onClick={() => navigate('/home')}>
            返回首页
          </Button>
        }
      />
    </div>
  )
}

export default NotFound
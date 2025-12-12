import React from 'react'
import { Spin } from 'antd'

const LoadingSpinner: React.FC = () => {
  return (
    <div className="loading-container">
      <Spin size="large" />
    </div>
  )
}

export default LoadingSpinner
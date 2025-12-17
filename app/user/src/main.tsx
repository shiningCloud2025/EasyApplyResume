import React, { Suspense } from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from 'react-query'
import { ConfigProvider } from 'antd'
import zhCN from 'antd/locale/zh_CN'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'

import App from './App'
import LoadingSpinner from './components/LoadingSpinner'
import './styles/index.scss'

// 设置dayjs中文语言
dayjs.locale('zh-cn')

// 创建react-query客户端
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
      staleTime: 5 * 60 * 1000, // 5分钟
    },
  },
})

// Antd主题配置
const theme = {
  token: {
    colorPrimary: '#1890ff',
    borderRadius: 6,
  },
}

// 临时调试信息
console.log('🚀 main.tsx 开始加载')
console.log('📍 root 元素:', document.getElementById('root'))

ReactDOM.createRoot(document.getElementById('root')!).render(
  // 移除 React.StrictMode 以避免开发环境下组件双重渲染导致 API 重复调用
  <BrowserRouter>
    <QueryClientProvider client={queryClient}>
      <ConfigProvider locale={zhCN} theme={theme}>
        <Suspense fallback={<LoadingSpinner />}>
          <App />
        </Suspense>
      </ConfigProvider>
    </QueryClientProvider>
  </BrowserRouter>
)
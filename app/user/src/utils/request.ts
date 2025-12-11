import axios, { AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { message } from 'antd'
import { BaseResponse } from '@types/index'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Token管理
const TOKEN_KEY = 'user_token'

export const getToken = (): string | null => {
  return localStorage.getItem(TOKEN_KEY)
}

export const setToken = (token: string): void => {
  localStorage.setItem(TOKEN_KEY, token)
}

export const removeToken = (): void => {
  localStorage.removeItem(TOKEN_KEY)
}

// 请求拦截器
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = getToken()
    if (token) {
      config.headers['User-Authorization'] = `User ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse<BaseResponse>) => {
    const { data } = response
    
    // 检查业务状态码
    if (data.code !== 200) {
      message.error(data.message || '请求失败')
      
      // 401: 未登录 或 Token失效
      if (data.code === 401) {
        removeToken()
        window.location.href = '/auth/login'
      }
      
      return Promise.reject(new Error(data.message || 'Error'))
    }
    
    return data
  },
  (error) => {
    let errorMessage = '网络错误'
    
    if (error.response) {
      switch (error.response.status) {
        case 400:
          errorMessage = '请求参数错误'
          break
        case 401:
          errorMessage = '未登录或登录已过期'
          removeToken()
          window.location.href = '/auth/login'
          break
        case 403:
          errorMessage = '没有权限访问'
          break
        case 404:
          errorMessage = '请求的资源不存在'
          break
        case 500:
          errorMessage = '服务器内部错误'
          break
        default:
          errorMessage = error.response.data?.message || '请求失败'
      }
    } else if (error.request) {
      errorMessage = '网络连接失败'
    }
    
    message.error(errorMessage)
    return Promise.reject(error)
  }
)

export default request
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
    
    // 如果返回的是数组，直接返回（部分接口直接返回数组，不包装BaseResponse）
    if (Array.isArray(data)) {
      return data
    }
    
    // 如果没有code字段，说明是直接返回的数据，不是BaseResponse格式
    if (data.code === undefined) {
      return data
    }
    
    // 检查业务状态码
    if (data.code !== 200) {
      // 401: 未登录 或 Token失效
      if (data.code === 401) {
        message.error('未登录，请先登录')
        removeToken()
        setTimeout(() => {
          window.location.href = '/auth/login'
        }, 1500)
      } else if (data.code === 403) {
        message.error('您未拥有权限')
        setTimeout(() => {
          window.location.href = '/403'
        }, 1500)
      } else {
        message.error(data.message || '请求失败')
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
          message.error('未登录，请先登录')
          removeToken()
          setTimeout(() => {
            window.location.href = '/auth/login'
          }, 1500)
          return Promise.reject(error)
        case 403:
          message.error('您未拥有权限')
          setTimeout(() => {
            window.location.href = '/403'
          }, 1500)
          return Promise.reject(error)
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
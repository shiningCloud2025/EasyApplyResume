import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import router from '@/router'

// 响应数据类型定义
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 添加token
    const token = localStorage.getItem('admin_token')
    console.log('请求拦截 - Token:', token ? token.substring(0, 20) + '...' : '无Token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    console.log('请求拦截 - Headers:', config.headers)
    
    // 添加时间戳防止缓存
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now()
      }
    }
    
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    console.log('原始响应:', response)
    console.log('响应数据:', response.data)
    console.log('响应数据类型:', typeof response.data)
    
    const { code, message, data } = response.data
    
    // 请求成功
    if (code === 200) {
      return response.data
    }
    
    // 业务错误
    console.error('业务错误:', { code, message, data })
    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message || '请求失败'))
  },
  (error) => {
    const { response } = error
    
    if (response) {
      const { status, data } = response
      
      switch (status) {
        case 401:
          ElMessage.error('登录已过期，请重新登录')
          localStorage.removeItem('admin_token')
          router.push('/login')
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data?.message || '网络错误')
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时')
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
    
    return Promise.reject(error)
  }
)

// API请求方法封装
export const api = {
  // GET请求
  get<T = any>(url: string, params?: any): Promise<ApiResponse<T>> {
    return request.get(url, { params })
  },
  
  // POST请求
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    return request.post(url, data, config)
  },
  
  // PUT请求
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    return request.put(url, data, config)
  },
  
  // DELETE请求
  delete<T = any>(url: string, params?: any): Promise<ApiResponse<T>> {
    return request.delete(url, { params })
  },
  
  // 文件上传
  upload<T = any>(url: string, formData: FormData): Promise<ApiResponse<T>> {
    return request.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}

export default request
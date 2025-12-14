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

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    // 特殊处理：如果后端直接返回数组（如省份、城市API），包装成统一格式
    if (Array.isArray(response.data)) {
      return {
        code: 200,
        message: 'OK',
        data: response.data
      }
    }
    
    const { code, message, data } = response.data
    
    // 请求成功
    if (code === 200) {
      return response.data
    }
    
    // Token过期或未认证
    if (code === 401) {
      const authStore = useAuthStore()
      ElMessage.error('登录已过期，请重新登录')
      authStore.clearAuth()
      router.push('/login')
      return Promise.reject(new Error('登录已过期'))
    }
    
    // 业务错误
    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message || '请求失败'))
  },
  (error) => {
    const { response } = error
    
    if (response) {
      const { status, data } = response
      
      switch (status) {
        case 401:
          const authStore = useAuthStore()
          ElMessage.error('登录已过期，请重新登录')
          authStore.clearAuth()
          router.push('/login')
          break
        case 403:
          ElMessage.error('没有权限访问')
          router.push('/403')
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

// 请求拦截器 - 增强版，检查token有效性
let tokenCheckCount = 0
request.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const token = localStorage.getItem('admin_token')
    
    // 如果存在token，每次都主动检查是否过期
    if (token) {
      config.headers['Admin-Authorization'] = `Admin ${token}`
      
      // 每次请求都检查token是否过期
      try {
        // 解析JWT token检查是否过期
        const parts = token.split('.')
        if (parts.length !== 3) {
          throw new Error('Invalid token format')
        }
        
        // Base64 URL解码
        const payload = parts[1].replace(/-/g, '+').replace(/_/g, '/')
        const jsonPayload = decodeURIComponent(atob(payload + '='.repeat((4 - payload.length % 4) % 4)).split('').map((c) => {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        }).join(''))
        
        const parsedPayload = JSON.parse(jsonPayload)
        const currentTime = Math.floor(Date.now() / 1000)
        
        if (parsedPayload.exp && parsedPayload.exp < currentTime) {
          // Token已过期，清除认证状态并跳转登录
          const authStore = useAuthStore()
          console.warn('Token已过期，自动退出登录')
          ElMessage.warning('登录已过期，请重新登录')
          authStore.clearAuth()
          router.push('/login')
          return Promise.reject(new Error('Token已过期'))
        }
      } catch (parseError) {
        console.warn('Token解析失败，可能格式错误:', parseError)
        // Token格式错误，也视为无效token
        const authStore = useAuthStore()
        ElMessage.warning('登录状态异常，请重新登录')
        authStore.clearAuth()
        router.push('/login')
        return Promise.reject(new Error('Token解析失败'))
      }
    }
    
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
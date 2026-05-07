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

const unauthorizedMessagePatterns = ['未登录', '登录已过期', 'token已过期', 'token过期', 'token验证失败', '认证失败']

const isAuthenticationExpired = (data: any) => {
  if (!data || typeof data !== 'object') {
    return false
  }

  const code = typeof data.code === 'number' ? data.code : Number(data.code)
  const message = typeof data.message === 'string' ? data.message : ''

  return code === 401 || unauthorizedMessagePatterns.some((pattern) => message.includes(pattern))
}

const redirectToLoginForExpiredAuth = () => {
  const authStore = useAuthStore()
  ElMessage.error('登录已过期，请重新登录')
  authStore.clearAuth()

  if (router.currentRoute.value.path !== '/login') {
    router.replace('/login')
  }
}

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 60000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const responseData = response.data as ApiResponse | any

    if (
      responseData &&
      typeof responseData === 'object' &&
      !Array.isArray(responseData) &&
      'code' in responseData
    ) {
      const { code, message } = responseData

      if (code === 200) {
        return responseData
      }

      if (code === 401) {
        redirectToLoginForExpiredAuth()
        return Promise.reject(new Error('登录已过期'))
      }

      ElMessage.error(message || '请求失败')
      return Promise.reject(new Error(message || '请求失败'))
    }

    return {
      code: 200,
      message: 'success',
      data: responseData
    }
  },
  (error) => {
    const { response } = error

    if (response) {
      const { status, data } = response

      switch (status) {
        case 401:
          redirectToLoginForExpiredAuth()
          break
        case 403:
          if (isAuthenticationExpired(data)) {
            redirectToLoginForExpiredAuth()
            break
          }
          ElMessage.error(data?.message || '没有权限访问')
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

// 请求拦截器
request.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const token = localStorage.getItem('admin_token')

    if (token) {
      config.headers['Admin-Authorization'] = `Admin ${token}`
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
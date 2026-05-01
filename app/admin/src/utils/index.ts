import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

// 配置dayjs
dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

/**
 * 格式化日期时间（兼容多种格式，包括 hutool DateTime）
 */
export const formatDateTime = (date: string | Date | any, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return '-'
  
  // 如果是对象类型（hutool DateTime），尝试提取时间戳或字符串
  if (typeof date === 'object' && !(date instanceof Date)) {
    // 尝试获取时间戳
    if (date.time) {
      return dayjs(date.time).format(format)
    }
    // 尝试转换为字符串
    if (date.toString) {
      const dateStr = date.toString()
      return dayjs(dateStr).format(format)
    }
  }
  
  return dayjs(date).format(format)
}

/**
 * 格式化日期
 */
export const formatDate = (date: string | Date) => {
  return formatDateTime(date, 'YYYY-MM-DD')
}

/**
 * 格式化招聘岗位名称展示
 */
export const formatRecruitPositionName = (name?: string | null): string => {
  if (!name) return '-'
  return name.replace(/\*+$/, '')
}

/**
 * 格式化时间
 */
export const formatTime = (date: string | Date) => {
  return formatDateTime(date, 'HH:mm:ss')
}

/**
 * 相对时间
 */
export const formatRelativeTime = (date: string | Date) => {
  if (!date) return '-'
  return dayjs(date).fromNow()
}

/**
 * 格式化文件大小
 */
export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

/**
 * 数字千分位格式化
 */
export const formatNumber = (num: number): string => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

/**
 * 手机号脱敏
 */
export const maskPhone = (phone: string): string => {
  if (!phone || phone.length !== 11) return phone
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

/**
 * 邮箱脱敏
 */
export const maskEmail = (email: string): string => {
  if (!email) return email
  const [username, domain] = email.split('@')
  if (username.length <= 3) return email
  const maskedUsername = username.substring(0, 3) + '***'
  return `${maskedUsername}@${domain}`
}

/**
 * 深拷贝
 */
export const deepClone = <T>(obj: T): T => {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime()) as T
  if (obj instanceof Array) return obj.map(item => deepClone(item)) as T
  if (typeof obj === 'object') {
    const cloned = {} as T
    Object.keys(obj).forEach(key => {
      (cloned as any)[key] = deepClone((obj as any)[key])
    })
    return cloned
  }
  return obj
}

/**
 * 防抖函数
 */
export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  delay: number
): (...args: Parameters<T>) => void => {
  let timeoutId: ReturnType<typeof setTimeout>
  return (...args: Parameters<T>) => {
    clearTimeout(timeoutId)
    timeoutId = setTimeout(() => func(...args), delay)
  }
}

/**
 * 节流函数
 */
export const throttle = <T extends (...args: any[]) => any>(
  func: T,
  delay: number
): (...args: Parameters<T>) => void => {
  let lastExecTime = 0
  return (...args: Parameters<T>) => {
    const currentTime = Date.now()
    if (currentTime - lastExecTime >= delay) {
      func(...args)
      lastExecTime = currentTime
    }
  }
}

/**
 * 获取随机颜色
 */
export const getRandomColor = (): string => {
  const colors = [
    '#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6',
    '#ec4899', '#14b8a6', '#f97316', '#06b6d4', '#84cc16'
  ]
  return colors[Math.floor(Math.random() * colors.length)]
}

/**
 * 生成UUID
 */
export const generateUUID = (): string => {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = Math.random() * 16 | 0
    const v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

/**
 * 下载文件
 */
export const downloadFile = (url: string, filename?: string) => {
  const link = document.createElement('a')
  link.href = url
  if (filename) {
    link.download = filename
  }
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

/**
 * 复制到剪贴板
 */
export const copyToClipboard = async (text: string): Promise<boolean> => {
  try {
    await navigator.clipboard.writeText(text)
    return true
  } catch (err) {
    console.error('复制失败:', err)
    
    // 降级方案
    const textArea = document.createElement('textArea')
    textArea.value = text
    document.body.appendChild(textArea)
    textArea.select()
    try {
      document.execCommand('copy')
      return true
    } catch (err) {
      return false
    } finally {
      document.body.removeChild(textArea)
    }
  }
}

/**
 * 验证手机号
 */
export const isValidPhone = (phone: string): boolean => {
  return /^1[3-9]\d{9}$/.test(phone)
}

/**
 * 验证邮箱
 */
export const isValidEmail = (email: string): boolean => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}

/**
 * 验证密码强度（至少包含数字和字母，6-20位）
 */
export const isValidPassword = (password: string): boolean => {
  return /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{6,20}$/.test(password)
}
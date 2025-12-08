// 表单验证规则
export const loginRules = {
  account: [
    { required: true, message: '请输入账号/手机号/邮箱' }
  ],
  password: [
    { required: true, message: '请输入密码' },
    { min: 6, max: 20, message: '密码长度为6-20位' }
  ]
}

export const phoneRules = {
  phone: [
    { required: true, message: '请输入手机号' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
  ],
  verifyCode: [
    { required: true, message: '请输入验证码' },
    { len: 6, message: '验证码为6位数字' }
  ]
}

export const emailRules = {
  email: [
    { required: true, message: '请输入邮箱' },
    { type: 'email', message: '请输入正确的邮箱格式' }
  ],
  verifyCode: [
    { required: true, message: '请输入验证码' },
    { len: 6, message: '验证码为6位数字' }
  ]
}

export const registerRules = {
  userAccount: [
    { required: true, message: '请输入用户账号' },
    { min: 3, max: 20, message: '账号长度为3-20位' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '账号只能包含字母、数字和下划线' }
  ],
  userUsername: [
    { required: true, message: '请输入用户名' },
    { min: 2, max: 10, message: '用户名长度为2-10位' }
  ],
  userEmail: [
    { required: true, message: '请输入邮箱' },
    { type: 'email', message: '请输入正确的邮箱格式' }
  ],
  userPhone: [
    { required: true, message: '请输入手机号' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
  ],
  userPassword: [
    { required: true, message: '请输入密码' },
    { min: 6, max: 20, message: '密码长度为6-20位' }
  ],
  phoneMessageCode: [
    { required: true, message: '请输入手机验证码' },
    { len: 6, message: '验证码为6位数字' }
  ],
  emailMessageCode: [
    { required: true, message: '请输入邮箱验证码' },
    { len: 6, message: '验证码为6位数字' }
  ]
}

// 工具函数
export const isValidPhone = (phone: string): boolean => {
  return /^1[3-9]\d{9}$/.test(phone)
}

export const isValidEmail = (email: string): boolean => {
  return /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(email)
}

export const generateRandomString = (length: number): string => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

// 格式化时间
export const formatTime = (time: string | Date): string => {
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 防抖函数
export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: NodeJS.Timeout | null = null
  return (...args: Parameters<T>) => {
    if (timeout) clearTimeout(timeout)
    timeout = setTimeout(() => func.apply(null, args), wait)
  }
}

// 节流函数
export const throttle = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let lastTime = 0
  return (...args: Parameters<T>) => {
    const now = Date.now()
    if (now - lastTime >= wait) {
      lastTime = now
      func.apply(null, args)
    }
  }
}
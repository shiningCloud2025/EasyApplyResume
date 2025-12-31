import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi, smsApi } from '@/api'

export interface AdminUser {
  adminId?: number
  userId?: number  // SecurityUser中的userId
  adminAccount?: string
  adminUsername: string
  adminEmail: string
  adminPhone: string
  adminImage: string
  adminIntroduce?: string
  adminLoginTime?: string
  adminState?: number
  roleInfoVOS?: Array<{
    roleId: number
    roleName: string
    roleIntroduce: string
  }>
  authorities?: Array<{
    authority: string
  }>
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('monitor_token'))
  const user = ref<AdminUser | null>(null)

  const isLoggedIn = computed(() => !!token.value)

  // 账号密码登录
  const login = async (data: { accountOrPhoneOrEmail: string; password: string }) => {
    const result = await authApi.login(data)
    const tokenStr = typeof result === 'string' ? result : (result as any)?.token || result
    if (tokenStr) {
      token.value = tokenStr
      localStorage.setItem('monitor_token', tokenStr)
    }
    return tokenStr
  }

  // 手机验证码登录
  const loginByPhone = async (data: { phone: string; messageCode: string }) => {
    const result = await authApi.loginByPhone(data)
    const tokenStr = typeof result === 'string' ? result : (result as any)?.token || result
    if (tokenStr) {
      token.value = tokenStr
      localStorage.setItem('monitor_token', tokenStr)
    }
    return tokenStr
  }

  // 邮箱验证码登录
  const loginByEmail = async (data: { email: string; messageCode: string }) => {
    const result = await authApi.loginByEmail(data)
    const tokenStr = typeof result === 'string' ? result : (result as any)?.token || result
    if (tokenStr) {
      token.value = tokenStr
      localStorage.setItem('monitor_token', tokenStr)
    }
    return tokenStr
  }

  // 获取用户信息
  const getUserInfo = async (silent: boolean = false) => {
    try {
      console.log('📥 [监控端] 开始获取用户信息...', silent ? '(静默模式)' : '')
      // 先获取基本的SecurityUser信息（包含userId）
      const response = await authApi.getUserInfo() as any
      console.log('📥 [监控端] SecurityUser信息:', response)
      
      if (response?.userId) {
        // 使用userId获取完整的管理员信息
        console.log('📥 [监控端] 获取完整管理员信息，adminId:', response.userId)
        const adminResponse = await authApi.getAdminById(response.userId) as any
        console.log('📥 [监控端] 完整管理员信息:', adminResponse)
        
        // 合并SecurityUser和AdminUser信息
        user.value = {
          ...adminResponse,
          // 确保serId和adminId都存在（有些地方使用userId，有些使用adminId）
          userId: response.userId,
          // 保留SecurityUser中的权限信息
          authorities: response.authorities
        } as AdminUser
        
        console.log('✅ [监控端] 用户信息已设置:', user.value)
        return user.value
      } else {
        console.warn('⚠️ [监控端] SecurityUser中没有userId')
        user.value = response
        return response
      }
    } catch (error: any) {
      if (!silent) {
        console.error('❌ [监控端] 获取用户信息失败:', error)
      }
      // 获取用户信息失败不影响登录，只是没有用户详情
      return null
    }
  }

  // 退出登录
  const logout = () => {
    token.value = null
    user.value = null
    localStorage.removeItem('monitor_token')
    // 清除公告显示标记，下次登录再次显示
    sessionStorage.removeItem('admonitor_announcement_shown')
  }

  // 清除认证状态
  const clearAuth = () => {
    logout()
  }

  // 发送手机验证码
  const sendPhoneCode = async (phone: string) => {
    return await smsApi.sendPhoneCode(phone)
  }

  // 发送邮箱验证码
  const sendEmailCode = async (email: string) => {
    return await authApi.sendEmailCode(email)
  }

  return {
    token,
    user,
    isLoggedIn,
    login,
    loginByPhone,
    loginByEmail,
    sendPhoneCode,
    sendEmailCode,
    getUserInfo,
    logout,
    clearAuth
  }
})

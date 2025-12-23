import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi, smsApi } from '@/api'

export interface AdminUser {
  adminId: number
  adminUsername: string
  adminEmail: string
  adminPhone: string
  adminImage: string
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
      const result = await authApi.getUserInfo() as AdminUser
      user.value = result
      return result
    } catch (error) {
      if (!silent) {
        console.error('获取用户信息失败:', error)
      }
      throw error
    }
  }

  // 退出登录
  const logout = () => {
    token.value = null
    user.value = null
    localStorage.removeItem('monitor_token')
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

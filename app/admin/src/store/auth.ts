import { defineStore } from 'pinia'
import { api } from '@/utils/request'

export interface AdminSecurityUser {
  userId: number
  userEmail: string
  username: string
  userType: string
  authorities?: any[]
  enabled?: boolean
}

export interface AdminUser {
  userId?: number
  adminId: number
  adminAccount: string
  adminUsername: string
  adminEmail: string
  adminPhone: string
  adminImage: string
  adminIntroduce: string
  adminState: number
  adminLoginTime: string
  adminCreatedTime?: string
  roles?: any[]
  roleInfoVOS?: any[]
}

export interface LoginForm {
  accountOrPhoneOrEmail: string
  password: string
}

export interface PhoneLoginForm {
  phone: string
  messageCode: string
}

export interface EmailLoginForm {
  email: string
  messageCode: string
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('admin_token') || '',
    user: null as AdminUser | null,
    loading: false
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    userRoles: (state) => state.user?.roles || state.user?.roleInfoVOS || [],
    userPermissions: (state) => {
      const permissions = []
      const roleList = state.user?.roles || state.user?.roleInfoVOS || []
      roleList.forEach((role: any) => {
        if (role.permissions) {
          permissions.push(...role.permissions.map((p: any) => p.permissionUrl))
        }
      })
      return [...new Set(permissions)]
    }
  },

  actions: {
    // 账号密码登录
    async login(form: LoginForm) {
      try {
        this.loading = true
        console.log('开始登录，参数:', form)
        const response = await api.post<string>('/admin/auth/formalLogin', form)
        console.log('登录响应:', response)
        
        if (!response.data) {
          throw new Error('登录失败：未获取到token')
        }
        
        this.setToken(response.data)
        console.log('Token已保存:', response.data)
        
        // 获取用户信息（失败不影响登录）
        try {
          await this.getUserInfo()
          console.log('用户信息已获取:', this.user)
        } catch (error) {
          console.warn('获取用户信息失败，但不影响登录:', error)
        }
        
        return response.data
      } catch (error: any) {
        console.error('登录失败详情:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    // 手机验证码登录
    async loginByPhone(form: PhoneLoginForm) {
      try {
        this.loading = true
        const response = await api.post<string>('/admin/auth/phoneLogin', form)
        this.setToken(response.data)
        
        // 获取用户信息（失败不影响登录）
        try {
          await this.getUserInfo()
        } catch (error) {
          console.warn('获取用户信息失败，但不影响登录:', error)
        }
        
        return response.data
      } finally {
        this.loading = false
      }
    },

    // 邮箱验证码登录
    async loginByEmail(form: EmailLoginForm) {
      try {
        this.loading = true
        const response = await api.post<string>('/admin/auth/emailLogin', form)
        this.setToken(response.data)
        
        // 获取用户信息（失败不影响登录）
        try {
          await this.getUserInfo()
        } catch (error) {
          console.warn('获取用户信息失败，但不影响登录:', error)
        }
        
        return response.data
      } finally {
        this.loading = false
      }
    },

    // 获取用户信息
    async getUserInfo(silent = false) {
      try {
        if (!silent) {
          console.log('开始获取用户信息...')
        }
        const securityResponse = await api.post<AdminSecurityUser>('/admin/auth/getAdminInfo')
        if (!silent) {
          console.log('鉴权用户信息响应:', securityResponse)
        }

        const securityUser = securityResponse.data
        if (!securityUser?.userId) {
          this.user = null
          return null
        }

        const adminResponse = await api.get<AdminUser>('/admin/admin/findById', {
          adminId: securityUser.userId
        })
        if (!silent) {
          console.log('管理员详情响应:', adminResponse)
        }

        this.user = {
          ...adminResponse.data,
          userId: securityUser.userId,
          adminId: adminResponse.data.adminId || securityUser.userId,
          adminEmail: adminResponse.data.adminEmail || securityUser.userEmail,
          roles: adminResponse.data.roles || adminResponse.data.roleInfoVOS || []
        }
        return this.user
      } catch (error: any) {
        console.error('获取用户信息失败:', error)
        return null
      }
    },

    // 设置token
    setToken(token: string) {
      this.token = token
      localStorage.setItem('admin_token', token)
    },

    // 退出登录
    async logout() {
      try {
        await api.post('/admin/admin/logout')
      } catch (error) {
        console.error('退出登录失败:', error)
      } finally {
        this.token = ''
        this.user = null
        localStorage.removeItem('admin_token')
      }
    },

    // 清除认证信息
    clearAuth() {
      this.token = ''
      this.user = null
      localStorage.removeItem('admin_token')
    },

    // 检查权限
    hasPermission(permission: string): boolean {
      return this.userPermissions.includes(permission)
    },

    // 检查角色
    hasRole(roleName: string): boolean {
      return this.userRoles.some((role: any) => role.roleName === roleName)
    }
  }
})
import { defineStore } from 'pinia'
import { api } from '@/utils/request'

export interface AdminUser {
  adminId: number
  userId?: number  // SecurityUser中的userId，与adminId相同
  adminAccount: string
  adminUsername: string
  adminEmail: string
  adminPhone: string
  adminImage: string
  adminIntroduce: string
  adminState: number
  adminLoginTime: string
  adminCreatedTime?: string
  roleInfoVOS?: any[]
  roles?: any[]
  authorities?: any[]
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
    userRoles: (state) => state.user?.roleInfoVOS || state.user?.roles || [],
    userPermissions: (state) => {
      const permissions = []
      const roles = state.user?.roleInfoVOS || state.user?.roles || []
      roles.forEach((role: any) => {
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
    async getUserInfo(silent: boolean = false) {
      try {
        console.log('开始获取用户信息...', silent ? '(静默模式)' : '')
        // 先获取基本的SecurityUser信息（包含userId）
        const response = await api.post<any>('/admin/auth/getAdminInfo', {}, { silent })
        console.log('SecurityUser信息:', response)
        
        if (response.data?.userId) {
          // 使用userId获取完整的管理员信息
          console.log('获取完整管理员信息，adminId:', response.data.userId)
          const adminResponse = await api.get<AdminUser>(`/admin/admin/findById?adminId=${response.data.userId}`, { 
            silent 
          })
          console.log('完整管理员信息:', adminResponse)
          
          // 合并SecurityUser和AdminUser信息
          this.user = {
            ...adminResponse.data,
            // 确保userId和adminId都存在（有些地方使用userId，有些使用adminId）
            userId: response.data.userId,
            // 保留SecurityUser中的权限信息
            authorities: response.data.authorities
          } as AdminUser
          
          console.log('✅ 用户信息已设置:', this.user)
          return this.user
        } else {
          console.warn('SecurityUser中没有userId')
          this.user = response.data
          return response.data
        }
      } catch (error: any) {
        if (!silent) {
          console.error('获取用户信息失败:', error)
        }
        // 获取用户信息失败不影响登录，只是没有用户详情
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
        // 调用退出接口（POST，通过token获取用户）
        await api.post('/admin/admin/logout')
      } catch (error) {
        console.error('退出登录失败:', error)
      } finally {
        // 清除本地数据
        this.token = ''
        this.user = null
        localStorage.removeItem('admin_token')
        // 清除公告显示标记，下次登录再次显示
        sessionStorage.removeItem('admin_announcement_shown')
      }
    },

    // 清除认证信息
    clearAuth() {
      this.token = ''
      this.user = null
      localStorage.removeItem('admin_token')
      // 清除公告显示标记，下次登录再次显示
      sessionStorage.removeItem('admin_announcement_shown')
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
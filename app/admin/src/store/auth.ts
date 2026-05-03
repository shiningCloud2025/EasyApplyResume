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
  authorities?: string[]
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

const normalizePermission = (permission: any): string => {
  if (typeof permission === 'string') {
    return permission.trim()
  }

  if (permission && typeof permission === 'object') {
    if (typeof permission.authority === 'string') {
      return permission.authority.trim()
    }

    if (typeof permission.permissionUrl === 'string') {
      return permission.permissionUrl.trim()
    }

    if (typeof permission.permission === 'string') {
      return permission.permission.trim()
    }

    if (typeof permission.url === 'string') {
      return permission.url.trim()
    }
  }

  return ''
}

const uniquePermissions = (permissions: string[]) => [...new Set(permissions.filter(Boolean))]

const getFirstAvailableArray = (...sources: any[]): any[] => {
  for (const source of sources) {
    if (Array.isArray(source) && source.length > 0) {
      return source
    }
  }

  for (const source of sources) {
    if (Array.isArray(source)) {
      return source
    }
  }

  return []
}

const normalizeAuthorities = (authorities?: any[]) => {
  return uniquePermissions((authorities || []).map((authority) => normalizePermission(authority)))
}

const extractPermissionsFromRoles = (roles?: any[]) => {
  const permissions: string[] = []

  ;(roles || []).forEach((role: any) => {
    const permissionList = getFirstAvailableArray(role?.permissionInfoVOS, role?.permissions)

    permissionList.forEach((permission: any) => {
      const normalizedPermission = normalizePermission(permission)
      if (normalizedPermission) {
        permissions.push(normalizedPermission)
      }
    })
  })

  return uniquePermissions(permissions)
}

const mergePermissions = (...permissionGroups: string[][]) => {
  return uniquePermissions(permissionGroups.flat())
}

const hasAnyPermission = (currentPermissions: string[], permissions: string[]) => {
  return permissions.some((permission) => currentPermissions.includes(permission))
}

export const websiteManagementPagePermissions = {
  admin: ['/admin/admin/findByPage'],
  role: ['/admin/role/findByPage'],
  permission: [
    '/admin/permission/findByPage',
    '/admin/permission/findById',
    '/admin/permission/add',
    '/admin/permission/update',
    '/admin/permission/delete'
  ]
} as const

export const articleManagementPagePermissions = {
  jobAdvice: '/admin/jobAdviceArticle/getJobAdviceArticlePage'
} as const

export const recruitmentManagementPagePermissions = {
  position: '/admin/recruitPosition/queryRecruitPositionPage',
  information: '/admin/employmentInformation/getEmploymentInformationPage'
} as const

export const resumeManagementPagePermissions = {
  template: '/admin/resumeTemplate/findResumeTemplateByPage',
  systemDeleted: '/admin/userDeleteResumeBySystemService/getUserDeleteResumeInfoPage'
} as const

export const mapManagementPagePermissions = {
  industry: '/admin/industryMap/findIndustryMapByPage',
  university: '/admin/universityMap/findUniversityMapByPage',
  province: '/admin/provinceMap/findProvinceMapByPage',
  city: '/admin/cityMap/findCityMapByPage',
  area: '/admin/areaMap/findAreaMapByPage',
  street: '/admin/streetMap/findStreetMapByPage'
} as const

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('admin_token') || '',
    user: null as AdminUser | null,
    loading: false
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    userRoles: (state) => getFirstAvailableArray(state.user?.roles, state.user?.roleInfoVOS),
    userPermissions: (state) => {
      return mergePermissions(
        normalizeAuthorities(state.user?.authorities),
        extractPermissionsFromRoles(getFirstAvailableArray(state.user?.roles, state.user?.roleInfoVOS))
      )
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

        const authorityPermissions = normalizeAuthorities(securityUser.authorities)

        this.user = {
          userId: securityUser.userId,
          adminId: securityUser.userId,
          adminAccount: '',
          adminUsername: securityUser.username || '',
          adminEmail: securityUser.userEmail || '',
          adminPhone: '',
          adminImage: '',
          adminIntroduce: '',
          adminState: securityUser.enabled === false ? 0 : 1,
          adminLoginTime: '',
          authorities: authorityPermissions,
          roles: [],
          roleInfoVOS: []
        }

        return this.user
      } catch (error: any) {
        console.error('获取用户信息失败:', error)
        this.user = null
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
      return this.userPermissions.includes(permission.trim())
    },

    hasAnyPermission(permissions: string[]): boolean {
      return hasAnyPermission(this.userPermissions, permissions)
    },

    canAccessRoute(permission?: string | string[]): boolean {
      if (!permission) {
        return true
      }

      if (Array.isArray(permission)) {
        return this.hasAnyPermission(permission)
      }

      return this.hasPermission(permission)
    },

    canAccessWebsiteManagement(): boolean {
      return Object.values(websiteManagementPagePermissions).some((permissions) => {
        return this.hasAnyPermission(permissions)
      })
    },

    // 检查角色
    hasRole(roleName: string): boolean {
      return this.userRoles.some((role: any) => role.roleName === roleName)
    }
  }
})
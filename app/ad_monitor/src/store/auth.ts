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

const normalizePermission = (permission: any): string => {
  if (typeof permission === 'string') {
    return permission.trim()
  }

  if (permission && typeof permission === 'object' && typeof permission.authority === 'string') {
    return permission.authority.trim()
  }

  return ''
}

const uniquePermissions = (permissions: string[]) => [...new Set(permissions.filter(Boolean))]

const hasAnyPermission = (currentPermissions: string[], permissions: string[]) => {
  return permissions.some((permission) => currentPermissions.includes(permission))
}

const normalizeAuthorities = (authorities?: any[]) => {
  return uniquePermissions((authorities || []).map((authority) => normalizePermission(authority)))
}

export const announcementManagementPermissions = {
  admin: {
    getInfo: '/admonitor/admin/announcement/getInfo',
    add: '/admonitor/admin/announcement/add',
    update: '/admonitor/admin/announcement/update'
  },
  user: {
    getInfo: '/admonitor/user/announcement/getInfo',
    add: '/admonitor/user/announcement/add',
    update: '/admonitor/user/announcement/update'
  },
  monitor: {
    getInfo: '/admonitor/admonitor/announcement/getInfo',
    add: '/admonitor/admonitor/announcement/add',
    update: '/admonitor/admonitor/announcement/update'
  }
} as const

export const imageAdvertisementManagementPermissions = {
  admin: {
    getByPage: '/admonitor/admin/advertisement/findAdmonitorAdminAdvertisementByPage',
    getById: '/admonitor/admin/advertisement/findAdmonitorAdminAdvertisementById',
    add: '/admonitor/admin/advertisement/addAdmonitorAdminAdvertisement',
    update: '/admonitor/admin/advertisement/updateAdmonitorAdminAdvertisement',
    delete: '/admonitor/admin/advertisement/deleteAdmonitorAdminAdvertisement'
  },
  user: {
    getByPage: '/admonitor/user/advertisement/findAdmonitorUserAdvertisementByPage',
    getById: '/admonitor/user/advertisement/findAdmonitorUserAdvertisementById',
    add: '/admonitor/user/advertisement/addAdmonitorUserAdvertisement',
    update: '/admonitor/user/advertisement/updateAdmonitorUserAdvertisement',
    delete: '/admonitor/user/advertisement/deleteAdmonitorUserAdvertisement'
  },
  monitor: {
    getByPage: '/admonitor/admonitor/advertisement/findAdmonitorAdvertisementByPage',
    getById: '/admonitor/admonitor/advertisement/findAdmonitorAdvertisementById',
    add: '/admonitor/admonitor/advertisement/addAdmonitorAdvertisement',
    update: '/admonitor/admonitor/advertisement/updateAdmonitorAdvertisement',
    delete: '/admonitor/admonitor/advertisement/deleteAdmonitorAdvertisement'
  }
} as const

export const userWebsiteManagementPermissions = {
  visitTrend: '/admonitor/user/dailyVisitTotalNum/findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum',
  userTrend: '/admonitor/user/daliyUserNum/findFromTimeToEndTimeAdmonitorUserDaliyUserNum'
} as const

export const adminWebsiteManagementPermissions = {
  visitTrend: '/admonitor/admin/dailyVisitTotalNum/findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum',
  adminTrend: '/admonitor/adminDaliyAdminNum/findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum'
} as const

export const middlewareManagementPermissions = {
  minio: '/admonitor/middleware/minio'
} as const

export const serviceMachineManagementPermissions = {
  getByPage: '/admonitor/servicemachine/manage/getAdmonitorServiceMachinePage',
  getInfo: '/admonitor/servicemachine/manage/getAdmonitorServiceMachineInfo',
  add: '/admonitor/servicemachine/manage/addAdmonitorServiceMachine',
  update: '/admonitor/servicemachine/manage/updateAdmonitorServiceMachine',
  delete: '/admonitor/servicemachine/manage/deleteAdmonitorServiceMachine'
} as const

export const serviceMachineMonitorPermissions = {
  getByPage: '/admonitor/servicemachine/jiankong/getAdmonitorServiceMachinePage',
  getMonitorInfo: '/admonitor/servicemachine/jiankong/getAdmonitorServiceMachineJianKongInfo',
  testConnect: '/admonitor/servicemachine/jiankong/testServiceMachineConnect'
} as const

export const securityManagementPermissions = {
  springBootAdmin: '/admonitor/security/spring-boot-admin',
  prometheus: '/admonitor/security/prometheus',
  grafana: '/admonitor/security/grafana'
} as const

export const internalSystemPermissions = {
  userPortal: '/admonitor/internal-system/user-portal',
  adminPortal: '/admonitor/internal-system/admin-portal',
  nacosPlatform: '/admonitor/internal-system/nacos-platform',
  yapiPlatform: '/admonitor/internal-system/yapi-platform'
} as const

export const externalSystemPermissions = {
  bailian: '/admonitor/external-system/bailian',
  sms: '/admonitor/external-system/sms',
  searchapi: '/admonitor/external-system/searchapi',
  amap: '/admonitor/external-system/amap',
  baiduCloud: '/admonitor/external-system/baidu-cloud',
  qiniu: '/admonitor/external-system/qiniu',
  autodl: '/admonitor/external-system/autodl',
  bigmodel: '/admonitor/external-system/bigmodel',
  volcengine: '/admonitor/external-system/volcengine'
} as const

export const apiDocsPermissions = {
  external: '/admonitor/api-docs/external',
  internal: '/admonitor/api-docs/internal'
} as const

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('monitor_token'))
  const user = ref<AdminUser | null>(null)
  let userInfoPromise: Promise<AdminUser | null> | null = null

  const isLoggedIn = computed(() => !!token.value)
  const userPermissions = computed(() => normalizeAuthorities(user.value?.authorities))

  const buildBaseUser = (response: any): AdminUser => ({
    adminId: response?.adminId ?? response?.userId,
    userId: response?.userId,
    adminAccount: response?.adminAccount || response?.username || response?.account || '',
    adminUsername: response?.adminUsername || response?.username || response?.account || '管理员',
    adminEmail: response?.adminEmail || response?.email || '',
    adminPhone: response?.adminPhone || response?.phone || '',
    adminImage: response?.adminImage || '',
    adminIntroduce: response?.adminIntroduce,
    adminLoginTime: response?.adminLoginTime,
    adminState: response?.adminState,
    roleInfoVOS: response?.roleInfoVOS,
    authorities: response?.authorities
  })

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
    if (user.value) {
      return user.value
    }

    if (userInfoPromise) {
      return userInfoPromise
    }

    userInfoPromise = (async () => {
      try {
        console.log('📥 [监控端] 开始获取用户信息...', silent ? '(静默模式)' : '')
        const response = await authApi.getUserInfo() as any
        console.log('📥 [监控端] SecurityUser信息:', response)

        if (response?.userId) {
          const baseUser = buildBaseUser(response)
          user.value = baseUser

          try {
            console.log('📥 [监控端] 获取完整管理员信息，adminId:', response.userId)
            const adminResponse = await authApi.getAdminById(response.userId, { silentError: true } as any) as any
            console.log('📥 [监控端] 完整管理员信息:', adminResponse)
            user.value = {
              ...baseUser,
              ...adminResponse,
              userId: response.userId,
              authorities: response.authorities
            } as AdminUser
          } catch (error) {
            if (!silent) {
              console.warn('⚠️ [监控端] 获取完整管理员信息失败，使用基础登录信息继续', error)
            }
          }

          console.log('✅ [监控端] 用户信息已设置:', user.value)
          return user.value
        }

        console.warn('⚠️ [监控端] SecurityUser中没有userId')
        user.value = buildBaseUser(response)
        return user.value
      } catch (error: any) {
        if (!silent) {
          console.error('❌ [监控端] 获取用户信息失败:', error)
        }
        return null
      } finally {
        userInfoPromise = null
      }
    })()

    return userInfoPromise
  }

  const setToken = (tokenStr: string) => {
    token.value = tokenStr
    localStorage.setItem('monitor_token', tokenStr)
  }

  const hasPermission = (permission: string) => {
    return userPermissions.value.includes(permission.trim())
  }

  const hasAnyPermissions = (permissions: string[]) => {
    return hasAnyPermission(userPermissions.value, permissions)
  }

  const canAccessRoute = (permission?: string | string[]) => {
    if (!permission) {
      return true
    }

    if (Array.isArray(permission)) {
      return hasAnyPermissions(permission)
    }

    return hasPermission(permission)
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
    userPermissions,
    login,
    loginByPhone,
    loginByEmail,
    sendPhoneCode,
    sendEmailCode,
    getUserInfo,
    setToken,
    hasPermission,
    hasAnyPermissions,
    canAccessRoute,
    logout,
    clearAuth
  }
})

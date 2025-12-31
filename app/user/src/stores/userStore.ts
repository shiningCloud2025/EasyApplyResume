import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { User } from '../types'
import { getToken, setToken, removeToken } from '@utils/request'
import { authAPI } from '@api/auth'
import { userAPI } from '@api/feedback'

interface UserState {
  user: User | null
  token: string | null
  isLoggedIn: boolean
  loading: boolean
  login: (user: User, token: string) => void
  logout: () => void
  updateUser: (user: Partial<User>) => void
  initAuth: () => void
  fetchUserInfo: (silent?: boolean) => Promise<User | null>
  getUserById: (userId: string) => Promise<User | null>
}

export const useUserStore = create<UserState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      isLoggedIn: false,
      loading: false,

      login: (user, token) => {
        setToken(token)
        set({ user: null, token, isLoggedIn: true }) // 登录时先清空旧用户，等待重新获取
      },

      logout: () => {
        removeToken()
        set({ user: null, token: null, isLoggedIn: false, loading: false })
        // 清除公告显示标记，下次登录再次显示
        sessionStorage.removeItem('user_announcement_shown')
      },

      updateUser: (userData) => {
        const currentUser = get().user
        if (currentUser) {
          set({ user: { ...currentUser, ...userData } })
        }
      },

      initAuth: () => {
        const token = getToken()
        set({ 
          token, 
          isLoggedIn: !!token,
          user: null  // 用户信息需要额外获取
        })
      },

      // 获取用户信息（参考admin端实现）
      // 先调用 getUserInfo 获取 SecurityUser（包含userId）
      // 再调用 getUserByUserId 获取完整用户信息
      fetchUserInfo: async (silent = false) => {
        // 如果已经有用户信息，直接返回，不重复调用
        const existingUser = get().user
        if (existingUser) {
          console.log('✅ 用户信息已存在，跳过获取:', existingUser)
          return existingUser
        }
        
        try {
          if (!silent) {
            set({ loading: true })
          }
          console.log('🔄 开始获取用户信息...')
          
          // 1. 调用 getUserInfo 获取 SecurityUser
          const rawResponse: any = await authAPI.getUserInfo()
          console.log('🔐 原始响应类型:', typeof rawResponse)
          
          // 解析响应
          let response: any
          if (typeof rawResponse === 'string') {
            // 如果是字符串，可能包含多个 JSON 拼接（React 严格模式问题），只取第一个
            let jsonStr = rawResponse
            const firstJsonEnd = rawResponse.indexOf('}{')
            if (firstJsonEnd > 0) {
              jsonStr = rawResponse.substring(0, firstJsonEnd + 1)
              console.log('🔐 截取第一个 JSON:', jsonStr.substring(0, 100) + '...')
            }
            response = JSON.parse(jsonStr)
          } else {
            response = rawResponse
          }
          
          console.log('🔐 解析后响应:', response)
          console.log('🔐 response.code:', response?.code)
          console.log('🔐 response.data:', response?.data)
          
          // 从 response 获取数据
          const responseData = response?.data
          const userId = responseData?.userId || response?.userId
          console.log('📌 解析的 userId:', userId)
          
          // 获取 SecurityUser 数据
          const securityData = responseData || response
          
          if (userId) {
            console.log('📥 调用 getUserByUserId，userId:', userId)
            
            // 2. 使用 userId 获取完整用户信息
            const userResponse: any = await userAPI.getUserById(String(userId))
            console.log('👤 getUserByUserId 响应:', userResponse)
            
            // 解析用户数据
            let userData: any
            if (typeof userResponse === 'string') {
              let jsonStr = userResponse
              const firstJsonEnd = userResponse.indexOf('}{')
              if (firstJsonEnd > 0) {
                jsonStr = userResponse.substring(0, firstJsonEnd + 1)
              }
              const parsed = JSON.parse(jsonStr)
              userData = parsed?.data || parsed
            } else {
              userData = userResponse?.data || userResponse
            }
            
            // 合并 SecurityUser 和 User 信息
            const user = {
              ...userData,
              userId: userId,
              // 如果 userResponse 没有 userUsername，使用 SecurityUser 的 username
              userUsername: userData?.userUsername || securityData?.username,
              authorities: securityData?.authorities
            } as User
            
            set({ user, loading: false })
            console.log('✅ 用户信息已设置:', user)
            return user
          } else {
            console.warn('⚠️ 未找到userId，尝试使用SecurityUser信息')
            if (securityData?.username) {
              const user = {
                userId: securityData.userId || 0,
                userUsername: securityData.username,
                userEmail: securityData.userEmail || '',
                userAccount: securityData.userAccount || '',
                userPhone: securityData.userPhone || '',
                userImage: securityData.userImage || '',
              } as User
              set({ user, loading: false })
              console.log('✅ 使用SecurityUser信息:', user)
              return user
            }
          }
          
          set({ loading: false })
          return null
        } catch (error) {
          console.error('❌ 获取用户信息失败:', error)
          set({ loading: false })
          return null
        }
      },

      // 根据userId获取用户详细信息
      getUserById: async (userId: string) => {
        try {
          set({ loading: true })
          console.log('📥 获取用户详细信息，userId:', userId)
          const res = await userAPI.getUserById(userId)
          
          if (res.data) {
            const user = res.data as User
            set({ user, loading: false })
            console.log('✅ 用户详细信息获取成功:', user)
            return user
          }
          
          set({ loading: false })
          return null
        } catch (error) {
          console.error('❌ 获取用户详细信息失败:', error)
          set({ loading: false })
          return null
        }
      }
    }),
    {
      name: 'user-store',
      partialize: (state) => ({
        user: state.user,
        token: state.token,
        // 注意：不再持久化 isLoggedIn，而是在 rehydrate 时根据实际 token 状态计算
      }),
      // 关键：在状态恢复后验证 token，确保 isLoggedIn 与实际 token 状态一致
      onRehydrateStorage: () => (state, error) => {
        if (error) {
          console.error('❌ Zustand rehydrate error:', error)
          return
        }
        // 延迟执行，确保 store 已经完全初始化
        setTimeout(() => {
          const actualToken = getToken()
          const isActuallyLoggedIn = !!actualToken
          const currentState = useUserStore.getState()
          console.log('🔄 Zustand rehydrate - actualToken:', actualToken ? 'exists' : 'null')
          console.log('🔄 Zustand rehydrate - currentState.isLoggedIn:', currentState.isLoggedIn)
          
          // 如果实际 token 不存在，但 isLoggedIn 为 true，强制修正
          if (!isActuallyLoggedIn && currentState.isLoggedIn) {
            console.log('⚠️ isLoggedIn 状态与 token 不一致，强制登出...')
            useUserStore.setState({ 
              isLoggedIn: false, 
              token: null, 
              user: null 
            })
          } else if (isActuallyLoggedIn && !currentState.isLoggedIn) {
            console.log('🔓 检测到 token，恢复登录状态...')
            useUserStore.setState({ 
              isLoggedIn: true, 
              token: actualToken 
            })
          }
        }, 0)
      }
    }
  )
)

export default useUserStore
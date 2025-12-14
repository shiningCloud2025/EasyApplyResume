import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { User } from '@types/index'
import { getToken, setToken, removeToken } from '@utils/request'

interface UserState {
  user: User | null
  token: string | null
  isLoggedIn: boolean
  login: (user: User, token: string) => void
  logout: () => void
  updateUser: (user: Partial<User>) => void
  initAuth: () => void
}

export const useUserStore = create<UserState>()(
  persist(
    (set, get) => ({
      user: null,
      token: getToken(),
      isLoggedIn: !!getToken(),

      login: (user, token) => {
        setToken(token)
        set({ user, token, isLoggedIn: true })
      },

      logout: () => {
        removeToken()
        set({ user: null, token: null, isLoggedIn: false })
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
      }
    }),
    {
      name: 'user-store',
      partialize: (state) => ({
        user: state.user,
        token: state.token,
        isLoggedIn: state.isLoggedIn
      })
    }
  )
)

export default useUserStore
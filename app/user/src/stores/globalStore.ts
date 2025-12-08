import { create } from 'zustand'
import { PaginatedResponse, QueryParams } from '@types/index'

interface GlobalState {
  // 全局加载状态
  loading: boolean
  setLoading: (loading: boolean) => void
  
  // 侧边栏状态
  collapsed: boolean
  toggleCollapsed: () => void
  
  // 主题设置
  theme: 'light' | 'dark'
  setTheme: (theme: 'light' | 'dark') => void
  
  // 面包屑导航
  breadcrumbs: string[]
  setBreadcrumbs: (breadcrumbs: string[]) => void
  
  // 分页状态
  pagination: {
    current: number
    pageSize: number
    total: number
  }
  setPagination: (pagination: Partial<GlobalState['pagination']>) => void
}

export const useGlobalStore = create<GlobalState>((set) => ({
  loading: false,
  setLoading: (loading) => set({ loading }),
  
  collapsed: false,
  toggleCollapsed: () => set((state) => ({ collapsed: !state.collapsed })),
  
  theme: 'light',
  setTheme: (theme) => set({ theme }),
  
  breadcrumbs: [],
  setBreadcrumbs: (breadcrumbs) => set({ breadcrumbs }),
  
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0
  },
  setPagination: (pagination) => 
    set((state) => ({ 
      pagination: { ...state.pagination, ...pagination } 
    }))
}))
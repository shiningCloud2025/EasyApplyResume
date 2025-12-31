import React, { useEffect, useRef } from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { useUserStore } from '@stores/userStore'
import PortalHeader from './PortalHeader'
import PortalFooter from './PortalFooter'
import IdleAdCarousel from './IdleAdCarousel'
import './MainLayout.scss'

const MainLayout: React.FC = () => {
  const navigate = useNavigate()
  const { user, isLoggedIn, fetchUserInfo } = useUserStore()
  const userInfoTimerRef = useRef<ReturnType<typeof setInterval> | null>(null)

  // 获取用户信息的函数
  const getUserInfo = async (silent: boolean = false) => {
    // 检查登录状态
    const currentState = useUserStore.getState()
    
    if (!currentState.isLoggedIn) {
      console.log('⚠️ MainLayout: 用户未登录，停止获取用户信息')
      if (userInfoTimerRef.current) {
        clearInterval(userInfoTimerRef.current)
        userInfoTimerRef.current = null
      }
      return
    }

    // 如果已经有用户信息，立即清除定时器并返回
    if (currentState.user) {
      console.log('✅ MainLayout: 用户信息已存在，停止定时获取')
      if (userInfoTimerRef.current) {
        clearInterval(userInfoTimerRef.current)
        userInfoTimerRef.current = null
      }
      return
    }

    // 尝试获取用户信息
    if (!silent) {
      console.log('🔄 MainLayout: 尝试获取用户信息...')
    }
    
    try {
      const result = await fetchUserInfo(silent)
      if (result) {
        console.log('✅ MainLayout: 用户信息获取成功，停止定时器', result)
        // 获取成功后立即清除定时器
        if (userInfoTimerRef.current) {
          clearInterval(userInfoTimerRef.current)
          userInfoTimerRef.current = null
        }
      }
    } catch (error) {
      if (!silent) {
        console.error('❌ MainLayout: 获取用户信息失败，15秒后重试', error)
      }
    }
  }

  // 组件挂载时检查登录状态并获取用户信息
  useEffect(() => {
    if (!isLoggedIn) {
      return
    }

    // 首次尝试获取用户信息（不静默，显示错误）
    getUserInfo(false)

    // 如果首次获取失败，启动定时器每15秒重试一次（静默模式）
    // 这里延迟100ms后检查，确保首次请求已完成
    const checkTimer = setTimeout(() => {
      const currentUser = useUserStore.getState().user
      if (!currentUser && isLoggedIn) {
        console.log('⏰ MainLayout: 启动定时器，每15秒静默重试获取用户信息')
        userInfoTimerRef.current = setInterval(() => getUserInfo(true), 15000)
      }
    }, 100)

    // 组件卸载时清除定时器
    return () => {
      clearTimeout(checkTimer)
      if (userInfoTimerRef.current) {
        console.log('🧹 MainLayout: 清除用户信息获取定时器')
        clearInterval(userInfoTimerRef.current)
        userInfoTimerRef.current = null
      }
    }
  }, [isLoggedIn])

  return (
    <div className="main-layout-portal">
      {/* 统一使用 PortalHeader */}
      <PortalHeader />

      {/* Main Content */}
      <main className="main-content">
        <div className="content-wrapper">
          <Outlet />
        </div>
      </main>

      {/* 统一使用 PortalFooter */}
      <PortalFooter />
      
      {/* 空闲广告轮播 */}
      <IdleAdCarousel idleTime={7 * 60 * 1000} enabled={true} />
    </div>
  )
}

export default MainLayout
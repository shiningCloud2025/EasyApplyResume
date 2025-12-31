import React, { useState, useEffect, useRef, useCallback } from 'react'
import { Carousel } from 'antd'
import { CloseOutlined, PictureOutlined } from '@ant-design/icons'
import { advertisementAPI, type AdvertisementInfo } from '@api/advertisement'
import './IdleAdCarousel.css'

interface IdleAdCarouselProps {
  idleTime?: number  // 空闲时间（毫秒），默认7分钟
  enabled?: boolean  // 是否启用
}

const IdleAdCarousel: React.FC<IdleAdCarouselProps> = ({
  idleTime = 7 * 60 * 1000,  // 7分钟
  enabled = true
}) => {
  const [showAd, setShowAd] = useState(false)
  const [ads, setAds] = useState<AdvertisementInfo[]>([])
  const idleTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null)

  // 后端已过滤有效期广告，直接使用
  const validAds = Array.isArray(ads) ? ads : []

  // 获取广告列表
  const fetchAds = async () => {
    try {
      const res = await advertisementAPI.getAllUserAdvertisements()
      console.log('用户端广告API返回:', res)
      // 处理可能的包装格式 { data: [...] } 或直接 [...]
      const data = Array.isArray(res) ? res : (res as any)?.data || []
      console.log('解析后的广告数据:', data, '数量:', data.length)
      setAds(data)
    } catch (error) {
      console.log('获取广告失败:', error)
    }
  }

  // 重置空闲计时器
  const resetIdleTimer = useCallback(() => {
    if (!enabled) return
    
    if (idleTimerRef.current) {
      clearTimeout(idleTimerRef.current)
    }
    
    // 如果广告正在显示，不重新计时
    if (showAd) return
    
    idleTimerRef.current = setTimeout(() => {
      if (validAds.length > 0) {
        setShowAd(true)
      }
    }, idleTime)
  }, [enabled, showAd, validAds.length, idleTime])

  // 关闭广告
  const closeAd = () => {
    setShowAd(false)
    setTimeout(resetIdleTimer, 100)
  }

  // 处理广告点击
  const handleAdClick = (ad: AdvertisementInfo) => {
    console.log('广告点击:', ad.advertisementName)
    if (ad.advertisementLink) {
      window.open(ad.advertisementLink, '_blank')
    }
  }

  // 用户活动事件列表（只监听当前页面内的操作）
  const userEvents = ['mousedown', 'mousemove', 'keydown', 'scroll', 'touchstart', 'click']

  // 手动触发广告弹窗（测试用）
  const triggerAd = useCallback(() => {
    if (validAds.length > 0) {
      setShowAd(true)
      console.log('手动触发广告弹窗')
    } else {
      console.log('没有有效广告')
    }
  }, [validAds.length])

  useEffect(() => {
    fetchAds()
  }, [])

  // 快捷键监听: Ctrl + Shift + D
  useEffect(() => {
    const handleKeyboardShortcut = (e: KeyboardEvent) => {
      if (e.ctrlKey && e.shiftKey && e.key.toLowerCase() === 'd') {
        e.preventDefault()
        triggerAd()
      }
    }
    document.addEventListener('keydown', handleKeyboardShortcut)
    return () => document.removeEventListener('keydown', handleKeyboardShortcut)
  }, [triggerAd])

  useEffect(() => {
    if (!enabled) return

    const handleUserActivity = () => resetIdleTimer()

    userEvents.forEach(event => {
      document.addEventListener(event, handleUserActivity, { passive: true })
    })

    resetIdleTimer()

    return () => {
      userEvents.forEach(event => {
        document.removeEventListener(event, handleUserActivity)
      })
      if (idleTimerRef.current) {
        clearTimeout(idleTimerRef.current)
      }
    }
  }, [enabled, resetIdleTimer])

  if (!showAd) return null

  return (
    <div className="idle-ad-overlay" onClick={(e) => e.target === e.currentTarget && closeAd()}>
      <div className="idle-ad-container">
        {/* 关闭按钮 */}
        <button className="close-btn" onClick={closeAd}>
          <CloseOutlined />
        </button>
        
        {/* 轮播图 */}
        {validAds.length > 0 ? (
          <Carousel autoplay autoplaySpeed={4000} className="ad-carousel">
            {validAds.map(ad => (
              <div key={ad.advertisementId} className="ad-slide">
                <div 
                  className="ad-link"
                  onClick={() => handleAdClick(ad)}
                  style={{ cursor: ad.advertisementLink ? 'pointer' : 'default' }}
                >
                  <img src={ad.advertisementUrl} alt={ad.advertisementName} className="ad-image" />
                  <div className="ad-title">{ad.advertisementName}</div>
                </div>
              </div>
            ))}
          </Carousel>
        ) : (
          <div className="no-ads">
            <PictureOutlined style={{ fontSize: 48 }} />
            <p>暂无广告</p>
          </div>
        )}
        
        {/* 底部提示 */}
        <div className="ad-footer">
          <span>点击空白区域或关闭按钮可关闭广告</span>
        </div>
      </div>
    </div>
  )
}

export default IdleAdCarousel

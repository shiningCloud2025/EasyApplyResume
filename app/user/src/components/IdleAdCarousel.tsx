import React, { useState, useEffect, useRef, useCallback } from 'react'
import { Carousel } from 'antd'
import { CloseOutlined, PictureOutlined } from '@ant-design/icons'
import { advertisementAPI, type AdvertisementInfo } from '@api/advertisement'
import './IdleAdCarousel.css'

interface IdleAdCarouselProps {
  idleTime?: number  // 空闲时间（毫秒），默认30秒（测试用）
  enabled?: boolean  // 是否启用
}

const IdleAdCarousel: React.FC<IdleAdCarouselProps> = ({
  idleTime = 30 * 1000,  // 30秒（测试用，正式环境改回 7 * 60 * 1000）
  enabled = true
}) => {
  const [showAd, setShowAd] = useState(false)
  const [ads, setAds] = useState<AdvertisementInfo[]>([])
  const idleTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null)

  // 过滤有效期内的广告
  const validAds = ads.filter(ad => {
    const now = new Date()
    const startTime = new Date(ad.advertisementStartedTime)
    const endTime = new Date(ad.advertisementEndTime)
    return now >= startTime && now <= endTime
  })

  // 获取广告列表
  const fetchAds = async () => {
    try {
      const data = await advertisementAPI.getAllUserAdvertisements()
      if (data) {
        setAds(data)
      }
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

  // 快捷键监听: Ctrl + Shift + A
  useEffect(() => {
    const handleKeyboardShortcut = (e: KeyboardEvent) => {
      if (e.ctrlKey && e.shiftKey && e.key.toLowerCase() === 'a') {
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

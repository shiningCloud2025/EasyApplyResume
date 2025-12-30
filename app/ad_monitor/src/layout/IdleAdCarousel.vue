<template>
  <Teleport to="body">
    <Transition name="fade">
      <div v-if="showAd" class="idle-ad-overlay" @click.self="closeAd">
        <div class="idle-ad-container">
          <!-- 关闭按钮 -->
          <button class="close-btn" @click="closeAd">
            <el-icon><Close /></el-icon>
          </button>
          
          <!-- 轮播图 -->
          <el-carousel 
            v-if="validAds.length > 0"
            :interval="4000" 
            :autoplay="true"
            indicator-position="outside"
            height="400px"
            class="ad-carousel"
          >
            <el-carousel-item v-for="ad in validAds" :key="ad.advertisementId">
              <a 
                :href="ad.advertisementLink" 
                target="_blank" 
                class="ad-link"
                @click="handleAdClick(ad)"
              >
                <img :src="ad.advertisementUrl" :alt="ad.advertisementName" class="ad-image" />
                <div class="ad-title">{{ ad.advertisementName }}</div>
              </a>
            </el-carousel-item>
          </el-carousel>
          
          <!-- 无广告时显示 -->
          <div v-else class="no-ads">
            <el-icon :size="48"><Picture /></el-icon>
            <p>暂无广告</p>
          </div>
          
          <!-- 底部提示 -->
          <div class="ad-footer">
            <span>点击空白区域或关闭按钮可关闭广告</span>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { Close, Picture } from '@element-plus/icons-vue'
import { monitorAdvertisementApi } from '@/api'

// 广告信息接口
interface AdvertisementInfo {
  advertisementId: number
  advertisementName: string
  advertisementUrl: string
  advertisementLink: string
  advertisementStartedTime: string
  advertisementEndTime: string
}

// Props
const props = withDefaults(defineProps<{
  idleTime?: number  // 空闲时间（毫秒），默认30秒（测试用）
  enabled?: boolean  // 是否启用
}>(), {
  idleTime: 30 * 1000,  // 30秒（测试用，正式环境改回 7 * 60 * 1000）
  enabled: true
})

// 状态
const showAd = ref(false)
const ads = ref<AdvertisementInfo[]>([])
let idleTimer: ReturnType<typeof setTimeout> | null = null

// 过滤有效期内的广告
const validAds = computed(() => {
  const now = new Date()
  return ads.value.filter(ad => {
    const startTime = new Date(ad.advertisementStartedTime)
    const endTime = new Date(ad.advertisementEndTime)
    return now >= startTime && now <= endTime
  })
})

// 获取广告列表
const fetchAds = async () => {
  try {
    const res = await monitorAdvertisementApi.getAll() as any
    if (res) {
      ads.value = res
    }
  } catch (error) {
    console.log('获取广告失败:', error)
  }
}

// 重置空闲计时器
const resetIdleTimer = () => {
  if (!props.enabled) return
  
  if (idleTimer) {
    clearTimeout(idleTimer)
  }
  
  if (showAd.value) return
  
  idleTimer = setTimeout(() => {
    if (validAds.value.length > 0) {
      showAd.value = true
    }
  }, props.idleTime)
}

// 关闭广告
const closeAd = () => {
  showAd.value = false
  resetIdleTimer()
}

// 处理广告点击
const handleAdClick = (ad: AdvertisementInfo) => {
  console.log('广告点击:', ad.advertisementName)
}

// 用户活动事件（只监听当前页面内的操作）
const userEvents = ['mousedown', 'mousemove', 'keydown', 'scroll', 'touchstart', 'click']

// 手动触发广告（测试用）: Ctrl + Shift + A
const handleKeyboardShortcut = (e: KeyboardEvent) => {
  if (e.ctrlKey && e.shiftKey && e.key.toLowerCase() === 'a') {
    e.preventDefault()
    if (validAds.value.length > 0) {
      showAd.value = true
      console.log('手动触发广告弹窗')
    } else {
      console.log('没有有效广告')
    }
  }
}

const initEventListeners = () => {
  userEvents.forEach(event => {
    document.addEventListener(event, resetIdleTimer, { passive: true })
  })
  document.addEventListener('keydown', handleKeyboardShortcut)
}

const cleanupEventListeners = () => {
  userEvents.forEach(event => {
    document.removeEventListener(event, resetIdleTimer)
  })
  document.removeEventListener('keydown', handleKeyboardShortcut)
}

watch(() => props.enabled, (newVal) => {
  if (newVal) {
    resetIdleTimer()
  } else {
    if (idleTimer) clearTimeout(idleTimer)
    showAd.value = false
  }
})

onMounted(() => {
  fetchAds()
  if (props.enabled) {
    initEventListeners()
    resetIdleTimer()
  }
})

onUnmounted(() => {
  cleanupEventListeners()
  if (idleTimer) clearTimeout(idleTimer)
})
</script>

<style scoped lang="scss">
.idle-ad-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(4px);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
}

.idle-ad-container {
  position: relative;
  width: 90%;
  max-width: 800px;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.close-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 10;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  
  &:hover {
    background: rgba(0, 0, 0, 0.8);
    transform: scale(1.1);
  }
}

.ad-carousel {
  :deep(.el-carousel__container) {
    height: 400px;
  }
  
  :deep(.el-carousel__button) {
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background: #d1d5db;
  }
  
  :deep(.el-carousel__indicator.is-active .el-carousel__button) {
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  }
}

.ad-link {
  display: block;
  width: 100%;
  height: 100%;
  text-decoration: none;
  position: relative;
}

.ad-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.ad-title {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px 20px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: white;
  font-size: 16px;
  font-weight: 500;
}

.no-ads {
  height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
  gap: 12px;
  
  p { margin: 0; font-size: 14px; }
}

.ad-footer {
  padding: 12px 20px;
  text-align: center;
  font-size: 12px;
  color: #9ca3af;
  background: #f9fafb;
  border-top: 1px solid #e5e7eb;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

@media (max-width: 640px) {
  .idle-ad-container { width: 95%; }
  .ad-carousel :deep(.el-carousel__container) { height: 280px; }
  .no-ads { height: 280px; }
}
</style>

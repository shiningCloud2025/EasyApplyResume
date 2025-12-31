<template>
  <div class="dashboard">
    <!-- 欢迎卡片 -->
    <div class="welcome-card">
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>欢迎来到观测与广告端</h2>
          <p>{{ currentDate }}，平台运行正常</p>
        </div>
        <div class="welcome-stats">
          <div class="quick-stat">
            <div class="stat-number">{{ formatNumber(adminStats.totalVisit) }}</div>
            <div class="stat-label">管理端总访问</div>
          </div>
          <div class="quick-stat">
            <div class="stat-number">{{ formatNumber(userStats.totalVisit) }}</div>
            <div class="stat-label">用户端总访问</div>
          </div>
        </div>
      </div>
      <div class="welcome-graphic">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
    </div>

    <!-- 数据统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card admin-card">
        <div class="stat-icon">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ formatNumber(adminStats.totalVisit) }}</div>
          <div class="stat-title">管理端总访问量</div>
          <div class="stat-trend" :class="{ up: adminStats.todayIncrease > 0 }">
            <el-icon v-if="adminStats.todayIncrease >= 0"><Top /></el-icon>
            <el-icon v-else><Bottom /></el-icon>
            今日 {{ adminStats.todayIncrease >= 0 ? '+' : '' }}{{ adminStats.todayIncrease }}
          </div>
        </div>
      </div>

      <div class="stat-card user-card">
        <div class="stat-icon user-icon">
          <el-icon><UserFilled /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ formatNumber(userStats.totalVisit) }}</div>
          <div class="stat-title">用户端总访问量</div>
          <div class="stat-trend" :class="{ up: userStats.todayIncrease > 0 }">
            <el-icon v-if="userStats.todayIncrease >= 0"><Top /></el-icon>
            <el-icon v-else><Bottom /></el-icon>
            今日 {{ userStats.todayIncrease >= 0 ? '+' : '' }}{{ userStats.todayIncrease }}
          </div>
        </div>
      </div>

      <div class="stat-card ad-card">
        <div class="stat-icon ad-icon">
          <el-icon><Picture /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ adminAds.length + userAds.length }}</div>
          <div class="stat-title">广告总数</div>
          <div class="stat-sub">管理端 {{ adminAds.length }} / 用户端 {{ userAds.length }}</div>
        </div>
      </div>

      <div class="stat-card announcement-card">
        <div class="stat-icon announcement-icon">
          <el-icon><Bell /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ announcementCount }}</div>
          <div class="stat-title">公告总数</div>
          <div class="stat-sub">系统运行中</div>
        </div>
      </div>
    </div>

    <!-- 功能模块 -->
    <div class="features-section">
      <h3>快捷功能</h3>
      <div class="features-grid">
        <div class="feature-card" @click="navigateTo('/monitor/advertisement?type=admin')">
          <div class="feature-icon" style="background: linear-gradient(135deg, #10b981, #059669);">
            <el-icon><Picture /></el-icon>
          </div>
          <div class="feature-content">
            <h4>管理端广告</h4>
            <p>管理后台展示的广告内容</p>
          </div>
        </div>

        <div class="feature-card" @click="navigateTo('/monitor/advertisement?type=user')">
          <div class="feature-icon" style="background: linear-gradient(135deg, #3b82f6, #1d4ed8);">
            <el-icon><Picture /></el-icon>
          </div>
          <div class="feature-content">
            <h4>用户端广告</h4>
            <p>管理用户端展示的广告内容</p>
          </div>
        </div>

        <div class="feature-card" @click="navigateTo('/monitor/announcement?type=admin')">
          <div class="feature-icon" style="background: linear-gradient(135deg, #f59e0b, #d97706);">
            <el-icon><Bell /></el-icon>
          </div>
          <div class="feature-content">
            <h4>管理端公告</h4>
            <p>发布管理端系统公告</p>
          </div>
        </div>

        <div class="feature-card" @click="navigateTo('/monitor/statistics?type=admin')">
          <div class="feature-icon" style="background: linear-gradient(135deg, #8b5cf6, #6d28d9);">
            <el-icon><TrendCharts /></el-icon>
          </div>
          <div class="feature-content">
            <h4>数据统计</h4>
            <p>查看详细的访问数据统计</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 系统信息 -->
    <div class="system-info">
      <div class="info-item">
        <span class="info-label">系统版本</span>
        <span class="info-value">v1.0.0</span>
      </div>
      <div class="divider"></div>
      <div class="info-item">
        <span class="info-label">技术栈</span>
        <span class="info-value">Vue 3 + Element Plus + Spring Boot</span>
      </div>
      <div class="divider"></div>
      <div class="info-item">
        <span class="info-label">© 2025 EasyApplyResume</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, UserFilled, Picture, Bell, TrendCharts, Top, Bottom } from '@element-plus/icons-vue'
import { adminStatisticsApi, userStatisticsApi, adminAdvertisementApi, userAdvertisementApi } from '@/api'

const router = useRouter()

const currentDate = computed(() => {
  const now = new Date()
  const year = now.getFullYear()
  const month = now.getMonth() + 1
  const day = now.getDate()
  const weekdays = ['日', '一', '二', '三', '四', '五', '六']
  return `${year}年${month}月${day}日 星期${weekdays[now.getDay()]}`
})

const adminStats = ref({
  totalVisit: 0,
  todayIncrease: 0
})

const userStats = ref({
  totalVisit: 0,
  todayIncrease: 0
})

const adminAds = ref<any[]>([])
const userAds = ref<any[]>([])
const announcementCount = ref(2)

const formatNumber = (num: number) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  }
  return num.toLocaleString()
}

const navigateTo = (path: string) => {
  router.push(path)
}

const loadData = async () => {
  try {
    // 加载管理端统计
    const [adminTotal, adminToday] = await Promise.all([
      adminStatisticsApi.getTotalVisitNum().catch(() => 0),
      adminStatisticsApi.getTodayIncreaseVisitNum().catch(() => 0)
    ])
    adminStats.value.totalVisit = adminTotal || 0
    adminStats.value.todayIncrease = adminToday || 0

    // 加载用户端统计
    const [userTotal, userToday] = await Promise.all([
      userStatisticsApi.getTotalVisitNum().catch(() => 0),
      userStatisticsApi.getTodayIncreaseVisitNum().catch(() => 0)
    ])
    userStats.value.totalVisit = userTotal || 0
    userStats.value.todayIncrease = userToday || 0

    // 加载广告列表
    const [adminAdList, userAdList] = await Promise.all([
      adminAdvertisementApi.getAll().catch(() => []),
      userAdvertisementApi.getAll().catch(() => [])
    ])
    adminAds.value = adminAdList || []
    userAds.value = userAdList || []

  } catch (error) {
    console.error('加载数据失败', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.dashboard {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border-radius: 16px;
  padding: 24px 32px;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(16, 185, 129, 0.3);
  margin-bottom: 24px;
}

.welcome-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.welcome-text h2 {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 4px;
}

.welcome-text p {
  font-size: 14px;
  opacity: 0.9;
}

.welcome-stats {
  display: flex;
  gap: 40px;
}

.quick-stat {
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
}

.stat-label {
  font-size: 13px;
  opacity: 0.85;
}

.welcome-graphic {
  position: relative;
  width: 100px;
  height: 100px;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  animation: float 5s ease-in-out infinite;
}

.circle-1 { width: 30px; height: 30px; top: 0; left: 35px; }
.circle-2 { width: 50px; height: 50px; top: 25px; left: 25px; animation-delay: 1.5s; }
.circle-3 { width: 40px; height: 40px; top: 50px; left: 45px; animation-delay: 3s; }

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  }
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #10b981, #059669);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 22px;
  flex-shrink: 0;
}

.stat-icon.user-icon {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
}

.stat-icon.ad-icon {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.stat-icon.announcement-icon {
  background: linear-gradient(135deg, #8b5cf6, #6d28d9);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
}

.stat-title {
  font-size: 13px;
  color: #6b7280;
  margin-top: 2px;
}

.stat-trend {
  font-size: 12px;
  color: #ef4444;
  display: flex;
  align-items: center;
  gap: 2px;
  margin-top: 4px;

  &.up {
    color: #10b981;
  }
}

.stat-sub {
  font-size: 12px;
  color: #9ca3af;
  margin-top: 4px;
}

.features-section {
  margin-bottom: 24px;

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
    margin-bottom: 16px;
  }
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.feature-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  }
}

.feature-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  flex-shrink: 0;
}

.feature-content {
  h4 {
    font-size: 14px;
    font-weight: 600;
    color: #1f2937;
    margin-bottom: 2px;
  }

  p {
    font-size: 12px;
    color: #6b7280;
  }
}

.system-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 16px;
  background: white;
  border-radius: 12px;
  font-size: 12px;
  color: #9ca3af;
}

.info-item {
  display: flex;
  gap: 6px;
}

.info-label {
  color: #6b7280;
}

.info-value {
  color: #374151;
}

.divider {
  width: 1px;
  height: 14px;
  background: #e5e7eb;
}

@media (max-width: 1024px) {
  .stats-grid, .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .stats-grid, .features-grid {
    grid-template-columns: 1fr;
  }

  .welcome-stats {
    flex-direction: column;
    gap: 12px;
  }
}
</style>

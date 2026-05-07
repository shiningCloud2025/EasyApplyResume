<template>
  <div class="home-page">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1>欢迎来到易投简历监测与广告端</h1>
        <p>全方位的数据监测与广告管理平台，助力业务增长</p>
      </div>
      <div class="welcome-illustration">
        <div class="illustration-circle"></div>
        <el-icon class="illustration-icon"><DataAnalysis /></el-icon>
      </div>
    </div>

    <!-- 功能介绍 -->
    <div class="features-section">
      <h2>平台功能</h2>
      <div class="features-grid">
        <div class="feature-card" v-for="feature in features" :key="feature.title">
          <div class="feature-icon" :style="{ background: feature.color }">
            <el-icon><component :is="feature.icon" /></el-icon>
          </div>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.desc }}</p>
        </div>
      </div>
    </div>

    <!-- 快速入口 -->
    <div class="quick-access">
      <h2>快速入口</h2>
      <div class="access-grid">
        <div class="access-card" v-for="item in quickLinks" :key="item.path" @click="$router.push(item.path)">
          <el-icon :size="24"><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </div>
      </div>
    </div>

    <!-- 系统信息 -->
    <div class="system-info">
      <div class="info-card">
        <h3>系统信息</h3>
        <div class="info-list">
          <div class="info-item">
            <span class="label">系统版本</span>
            <span class="value">v1.0.0</span>
          </div>
          <div class="info-item">
            <span class="label">前端框架</span>
            <span class="value">Vue 3 + Element Plus</span>
          </div>
          <div class="info-item">
            <span class="label">后端框架</span>
            <span class="value">Spring Boot 3.x</span>
          </div>
          <div class="info-item">
            <span class="label">当前时间</span>
            <span class="value">{{ currentTime }}</span>
          </div>
        </div>
      </div>
      <div class="info-card">
        <h3>使用帮助</h3>
        <ul class="help-list">
          <li><el-icon><Right /></el-icon>左侧菜单可以导航到各个功能模块</li>
          <li><el-icon><Right /></el-icon>公告管理用于发布各端系统公告</li>
          <li><el-icon><Right /></el-icon>广告管理用于配置展示广告</li>
          <li><el-icon><Right /></el-icon>监测管理用于查看访问数据</li>
          <li><el-icon><Right /></el-icon>安全管理可跳转到监控平台</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { 
  DataAnalysis, Bell, Picture, User, UserFilled, 
  Connection, Lock, Right, Monitor, TrendCharts
} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { monitorAnnouncementApi } from '@/api'
import { useAuthStore, announcementManagementPermissions, imageAdvertisementManagementPermissions } from '@/store/auth'

const currentTime = ref('')
const authStore = useAuthStore()
let timer: NodeJS.Timeout

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const features = ref([
  { icon: 'Bell', title: '公告管理', desc: '管理各端系统公告，及时传达重要信息', color: 'linear-gradient(135deg, #f59e0b, #d97706)' },
  { icon: 'Picture', title: '广告管理', desc: '配置图片广告，提升产品曝光度', color: 'linear-gradient(135deg, #8b5cf6, #6d28d9)' },
  { icon: 'User', title: '用户监测', desc: '监控用户端访问数据和行为日志', color: 'linear-gradient(135deg, #3b82f6, #1d4ed8)' },
  { icon: 'UserFilled', title: '管理监测', desc: '监控管理端访问数据和操作日志', color: 'linear-gradient(135deg, #10b981, #059669)' },
  { icon: 'Connection', title: '中间件监测', desc: '查看MinIO等中间件运行状态', color: 'linear-gradient(135deg, #ec4899, #be185d)' },
  { icon: 'Lock', title: '安全管理', desc: '集成Spring Boot Admin、Prometheus等监控', color: 'linear-gradient(135deg, #ef4444, #dc2626)' }
])

const quickLinks = computed(() => {
  const allLinks = [
    {
      icon: 'Bell',
      title: '管理端公告',
      path: '/main/notice/admin',
      permission: announcementManagementPermissions.admin.getInfo
    },
    {
      icon: 'Picture',
      title: '图片广告',
      path: '/main/ad/image/admin',
      permission: imageAdvertisementManagementPermissions.admin.getByPage
    },
    { icon: 'TrendCharts', title: '用户数据', path: '/main/user-monitor/website' },
    { icon: 'Monitor', title: '管理数据', path: '/main/admin-monitor/website' }
  ]

  return allLinks.filter((item) => authStore.canAccessRoute(item.permission))
})

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
  showAnnouncement()
})

// 显示公告弹窗
const showAnnouncement = async () => {
  if (authStore.isLoggedIn && !authStore.user) {
    await authStore.getUserInfo(true)
  }

  if (!authStore.canAccessRoute(announcementManagementPermissions.monitor.getInfo)) {
    return
  }

  // 检查是否是本次会话第一次进入（避免刷新重复显示）
  const announcementShown = sessionStorage.getItem('admonitor_announcement_shown')
  if (announcementShown) return

  try {
    const res = await monitorAnnouncementApi.getInfo() as any
    if (res && res.announcementTitle) {
      // 标记已显示
      sessionStorage.setItem('admonitor_announcement_shown', 'true')

      await ElMessageBox.alert(
        res.announcementContent || '暂无内容',
        res.announcementTitle,
        {
          confirmButtonText: '我知道了',
          type: 'info',
          dangerouslyUseHTMLString: true,
          customClass: 'announcement-dialog'
        }
      )
    }
  } catch (error) {
    console.log('公告获取失败，跳过:', error)
  }
}

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped lang="scss">
.home-page {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-section {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border-radius: 16px;
  padding: 40px;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  position: relative;
  overflow: hidden;
}

.welcome-content {
  h1 {
    font-size: 28px;
    font-weight: 700;
    margin: 0 0 12px 0;
  }
  p {
    font-size: 16px;
    opacity: 0.9;
    margin: 0;
  }
}

.welcome-illustration {
  position: relative;
  width: 120px;
  height: 120px;
}

.illustration-circle {
  position: absolute;
  inset: 0;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

.illustration-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 48px;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.2; }
  50% { transform: scale(1.1); opacity: 0.3; }
}

.features-section {
  margin-bottom: 32px;
  
  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 20px 0;
  }
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.feature-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 20px rgba(0,0,0,0.1);
  }

  .feature-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 24px;
    margin-bottom: 16px;
  }

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 8px 0;
  }

  p {
    font-size: 13px;
    color: #6b7280;
    margin: 0;
    line-height: 1.5;
  }
}

.quick-access {
  margin-bottom: 32px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 20px 0;
  }
}

.access-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.access-card {
  background: white;
  border-radius: 10px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: all 0.2s;
  color: #374151;

  &:hover {
    background: #10b981;
    color: white;
    transform: translateY(-2px);
  }

  span {
    font-size: 14px;
    font-weight: 500;
  }
}

.system-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 16px 0;
    padding-bottom: 12px;
    border-bottom: 1px solid #e5e7eb;
  }
}

.info-list {
  .info-item {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px dashed #e5e7eb;

    &:last-child {
      border-bottom: none;
    }

    .label {
      color: #6b7280;
      font-size: 14px;
    }

    .value {
      color: #1f2937;
      font-size: 14px;
      font-weight: 500;
    }
  }
}

.help-list {
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
    font-size: 14px;
    color: #4b5563;

    .el-icon {
      color: #10b981;
    }
  }
}

@media (max-width: 1024px) {
  .features-grid { grid-template-columns: repeat(2, 1fr); }
  .access-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 768px) {
  .features-grid, .access-grid, .system-info { grid-template-columns: 1fr; }
  .welcome-section { flex-direction: column; text-align: center; }
  .welcome-illustration { margin-top: 24px; }
}
</style>

<!-- 公告弹窗全局样式 -->
<style>
.announcement-dialog {
  min-width: 450px !important;
  max-width: 600px !important;
  border-radius: 16px !important;
  overflow: hidden;
}

.announcement-dialog .el-message-box__header {
  padding: 24px 24px 16px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.announcement-dialog .el-message-box__title {
  font-size: 20px !important;
  font-weight: 600 !important;
  color: white !important;
}

.announcement-dialog .el-message-box__headerbtn {
  top: 20px;
  right: 20px;
}

.announcement-dialog .el-message-box__headerbtn .el-message-box__close {
  color: white !important;
}

.announcement-dialog .el-message-box__content {
  padding: 24px !important;
  font-size: 15px !important;
  line-height: 1.8 !important;
  color: #374151 !important;
  min-height: 80px;
}

.announcement-dialog .el-message-box__status {
  display: none !important;
}

.announcement-dialog .el-message-box__btns {
  padding: 16px 24px 24px !important;
}

.announcement-dialog .el-message-box__btns .el-button--primary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%) !important;
  border: none !important;
  border-radius: 8px !important;
  padding: 12px 32px !important;
  font-size: 15px !important;
  font-weight: 500 !important;
}

.announcement-dialog .el-message-box__btns .el-button--primary:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
}
</style>

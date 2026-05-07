<template>
  <el-container :class="['main-layout', { 'is-mobile': isMobile, 'mobile-sidebar-open': mobileSidebarOpen }]">
    <div v-if="isMobile && mobileSidebarOpen" class="mobile-sidebar-mask" @click="closeMobileSidebar"></div>
    <!-- 侧边栏 -->
    <el-aside :width="sidebarWidth" :class="['sidebar', { mobile: isMobile, open: mobileSidebarOpen }]">
      <div class="sidebar-header">
        <div class="logo" v-show="!collapsed">
          <div class="logo-icon">
            <el-icon><DataAnalysis /></el-icon>
          </div>
          <span class="logo-text">监测与广告端</span>
        </div>
        <div class="logo-mini" v-show="collapsed">
          <el-icon><DataAnalysis /></el-icon>
        </div>
      </div>

      <el-scrollbar class="menu-scrollbar">
        <el-menu
          :default-active="activeMenu"
          :collapse="collapsed"
          :unique-opened="true"
          router
          class="sidebar-menu"
        >
          <!-- 首页 -->
          <el-menu-item index="/main/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>

          <!-- 公告管理 -->
          <el-sub-menu v-if="showNoticeMenu" index="notice">
            <template #title>
              <el-icon><Bell /></el-icon>
              <span>公告管理</span>
            </template>
            <el-menu-item
              v-for="item in visibleNoticeMenus"
              :key="item.index"
              :index="item.index"
            >
              {{ item.title }}
            </el-menu-item>
          </el-sub-menu>

          <!-- 广告管理 -->
          <el-sub-menu index="advertisement">
            <template #title>
              <el-icon><Picture /></el-icon>
              <span>广告管理</span>
            </template>
            <el-sub-menu v-if="showImageAdMenu" index="image-ad">
              <template #title>
                <span>图片广告管理</span>
              </template>
              <el-menu-item
                v-for="item in visibleImageAdMenus"
                :key="item.index"
                :index="item.index"
              >
                {{ item.title }}
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item index="/main/ad/video">
              <span>视频广告管理</span>
              <el-tag size="small" type="info" style="margin-left: 8px;">暂未开放</el-tag>
            </el-menu-item>
          </el-sub-menu>

          <!-- 用户监测管理 -->
          <el-sub-menu index="user-monitor">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户监测管理</span>
            </template>
            <el-menu-item index="/main/user-monitor/website">网站管理</el-menu-item>
            <el-menu-item index="/main/user-monitor/log">
              <span>日志管理</span>
              <el-tag size="small" type="info" style="margin-left: 8px;">暂未开放</el-tag>
            </el-menu-item>
          </el-sub-menu>

          <!-- 管理监测管理 -->
          <el-sub-menu index="admin-monitor">
            <template #title>
              <el-icon><UserFilled /></el-icon>
              <span>管理监测管理</span>
            </template>
            <el-menu-item index="/main/admin-monitor/website">网站管理</el-menu-item>
            <el-menu-item index="/main/admin-monitor/log">
              <span>日志管理</span>
              <el-tag size="small" type="info" style="margin-left: 8px;">暂未开放</el-tag>
            </el-menu-item>
          </el-sub-menu>

          <!-- 中间件监测管理 -->
          <el-sub-menu index="middleware">
            <template #title>
              <el-icon><Connection /></el-icon>
              <span>中间件监测管理</span>
            </template>
            <el-menu-item index="/main/middleware/mysql">
              <span>MySQL管理</span>
              <el-tag size="small" type="info" style="margin-left: 8px;">暂未开放</el-tag>
            </el-menu-item>
            <el-menu-item index="/main/middleware/redis">
              <span>Redis管理</span>
              <el-tag size="small" type="info" style="margin-left: 8px;">暂未开放</el-tag>
            </el-menu-item>
            <el-menu-item index="/main/middleware/minio">MinIO管理</el-menu-item>
          </el-sub-menu>

          <!-- 服务器管理 -->
          <el-sub-menu index="server">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>服务器管理</span>
            </template>
            <el-menu-item index="/main/server/manage">设备管理</el-menu-item>
            <el-menu-item index="/main/server/monitor">设备监控</el-menu-item>
          </el-sub-menu>

          <!-- 网站安全管理 -->
          <el-sub-menu index="security">
            <template #title>
              <el-icon><Lock /></el-icon>
              <span>网站安全管理</span>
            </template>
            <el-menu-item index="/main/security/spring-boot-admin">
              <el-icon><Monitor /></el-icon>
              <span>Spring Boot Admin</span>
            </el-menu-item>
            <el-menu-item index="/main/security/prometheus">
              <el-icon><TrendCharts /></el-icon>
              <span>Prometheus</span>
            </el-menu-item>
            <el-menu-item index="/main/security/grafana">
              <el-icon><DataLine /></el-icon>
              <span>Grafana</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 内部系统 -->
          <el-sub-menu index="system">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>内部系统</span>
            </template>
            <el-menu-item index="/main/system/user-portal">
              <el-icon><User /></el-icon>
              <span>易投简历用户端</span>
            </el-menu-item>
            <el-menu-item index="/main/system/observation-portal">
              <el-icon><DataAnalysis /></el-icon>
              <span>易投简历监测与广告端</span>
            </el-menu-item>
            <el-menu-item index="/main/system/nacos-platform">
              <el-icon><Connection /></el-icon>
              <span>Nacos配置平台</span>
            </el-menu-item>
            <el-menu-item index="/main/system/yapi-platform">
              <el-icon><Link /></el-icon>
              <span>YApi测试平台</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 外部系统 -->
          <el-sub-menu index="external-platform">
            <template #title>
              <el-icon><Platform /></el-icon>
              <span>外部系统</span>
            </template>
            <el-menu-item index="/main/external-platform/bailian">阿里云百炼平台</el-menu-item>
            <el-menu-item index="/main/external-platform/sms">阿里云短信平台</el-menu-item>
            <el-menu-item index="/main/external-platform/searchapi">SearchAPI平台</el-menu-item>
            <el-menu-item index="/main/external-platform/amap">高德开放平台</el-menu-item>
            <el-menu-item index="/main/external-platform/baidu-cloud">百度智能云平台</el-menu-item>
            <el-menu-item index="/main/external-platform/qiniu">七牛云平台</el-menu-item>
            <el-menu-item index="/main/external-platform/autodl">AutoDL平台</el-menu-item>
            <el-menu-item index="/main/external-platform/bigmodel">智谱开放平台</el-menu-item>
            <el-menu-item index="/main/external-platform/volcengine">火山引擎平台</el-menu-item>
          </el-sub-menu>

          <!-- API文档中心 -->
          <el-sub-menu index="api-docs">
            <template #title>
              <el-icon><Link /></el-icon>
              <span>API文档中心</span>
            </template>
            <el-menu-item index="/main/api-docs/external">API对外文档中心</el-menu-item>
            <el-menu-item index="/main/api-docs/internal">API对内文档中心</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 主内容区 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-left">
          <el-button
            :icon="collapsed ? Expand : Fold"
            @click="toggleSidebar"
            text
            class="collapse-btn"
          />
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/main/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <el-button :icon="FullScreen" text @click="toggleFullScreen" />
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar 
                :size="32" 
                :src="authStore.user?.adminImage || defaultAvatar"
                class="avatar"
              >
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">{{ authStore.user?.adminUsername || '管理员' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="feedback">
                  <el-icon><ChatLineSquare /></el-icon>
                  意见反馈
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>

  <!-- 空闲广告轮播 -->
  <IdleAdCarousel :idle-time="7 * 60 * 1000" :enabled="true" />
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore, announcementManagementPermissions, imageAdvertisementManagementPermissions } from '@/store/auth'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  House, Bell, Picture, User, UserFilled, Connection, Lock, Monitor,
  TrendCharts, DataLine, DataAnalysis, Fold, Expand, FullScreen,
  ArrowDown, SwitchButton, ChatLineSquare, Platform, Link
} from '@element-plus/icons-vue'
import IdleAdCarousel from './IdleAdCarousel.vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const noticeMenus = [
  {
    index: '/main/notice/admin',
    title: '管理端公告管理',
    permission: announcementManagementPermissions.admin.getInfo
  },
  {
    index: '/main/notice/user',
    title: '用户端公告管理',
    permission: announcementManagementPermissions.user.getInfo
  },
  {
    index: '/main/notice/monitor',
    title: '监测端公告管理',
    permission: announcementManagementPermissions.monitor.getInfo
  }
]

const visibleNoticeMenus = computed(() => {
  return noticeMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showNoticeMenu = computed(() => {
  return visibleNoticeMenus.value.length > 0
})

const imageAdMenus = [
  {
    index: '/main/ad/image/admin',
    title: '管理端广告管理',
    permission: imageAdvertisementManagementPermissions.admin.getByPage
  },
  {
    index: '/main/ad/image/user',
    title: '用户端广告管理',
    permission: imageAdvertisementManagementPermissions.user.getByPage
  },
  {
    index: '/main/ad/image/monitor',
    title: '监测端广告管理',
    permission: imageAdvertisementManagementPermissions.monitor.getByPage
  }
]

const visibleImageAdMenus = computed(() => {
  return imageAdMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showImageAdMenu = computed(() => {
  return visibleImageAdMenus.value.length > 0
})

const isMobile = ref(false)
const mobileSidebarOpen = ref(false)
const updateMobileState = () => {
  isMobile.value = window.innerWidth <= 768
  if (!isMobile.value) {
    mobileSidebarOpen.value = false
  }
}

const sidebarWidth = computed(() => {
  if (isMobile.value) {
    return '240px'
  }
  return collapsed.value ? '64px' : '240px'
})

const closeMobileSidebar = () => {
  mobileSidebarOpen.value = false
}

const collapsed = ref(false)
const activeMenu = computed(() => route.path)
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const toggleSidebar = () => {
  if (isMobile.value) {
    mobileSidebarOpen.value = !mobileSidebarOpen.value
    return
  }
  collapsed.value = !collapsed.value
}

// 面包屑
const breadcrumbs = computed(() => {
  const routeMap: Record<string, string> = {
    '/main/home': '首页',
    '/main/notice/admin': '管理端公告管理',
    '/main/notice/user': '用户端公告管理',
    '/main/notice/monitor': '监测端公告管理',
    '/main/ad/image/admin': '管理端图片广告',
    '/main/ad/image/user': '用户端图片广告',
    '/main/ad/image/monitor': '监测端图片广告',
    '/main/ad/video': '视频广告管理',
    '/main/user-monitor/website': '用户端网站管理',
    '/main/user-monitor/log': '用户端日志管理',
    '/main/admin-monitor/website': '管理端网站管理',
    '/main/admin-monitor/log': '管理端日志管理',
    '/main/middleware/mysql': 'MySQL管理',
    '/main/middleware/redis': 'Redis管理',
    '/main/middleware/minio': 'MinIO管理',
    '/main/security/spring-boot-admin': 'Spring Boot Admin',
    '/main/security/prometheus': 'Prometheus',
    '/main/security/grafana': 'Grafana',
    '/main/system/user-portal': '易投简历用户端',
    '/main/system/observation-portal': '易投简历监测与广告端',
    '/main/system/nacos-platform': 'Nacos配置平台',
    '/main/system/yapi-platform': 'YApi测试平台',
    '/main/external-platform/bailian': '阿里云百炼平台',
    '/main/external-platform/sms': '阿里云短信平台',
    '/main/external-platform/searchapi': 'SearchAPI平台',
    '/main/external-platform/amap': '高德开放平台',
    '/main/external-platform/baidu-cloud': '百度智能云平台',
    '/main/external-platform/qiniu': '七牛云平台',
    '/main/external-platform/autodl': 'AutoDL平台',
    '/main/external-platform/bigmodel': '智谱开放平台',
    '/main/external-platform/volcengine': '火山引擎平台',
    '/main/api-docs/external': 'API对外文档中心',
    '/main/api-docs/internal': 'API对内文档中心',
    '/main/server/manage': '设备管理',
    '/main/server/monitor': '设备监控',
    '/main/profile': '个人中心',
    '/main/feedback/submit': '意见反馈'
  }
  
  const title = routeMap[route.path]
  if (title && route.path !== '/main/home') {
    return [{ path: route.path, title }]
  }
  return []
})

const toggleFullScreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

const handleCommand = async (cmd: string) => {
  switch (cmd) {
    case 'profile':
      router.push('/main/profile')
      break
    case 'feedback':
      router.push('/main/feedback/submit')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        authStore.clearAuth()
        ElMessage.success('退出成功')
        router.push('/')
      } catch {}
      break
  }
}

// 定时器ID
let userInfoTimer: NodeJS.Timeout | null = null

// 获取用户信息的函数
const fetchUserInfo = async (silent: boolean = false) => {
  if (!authStore.isLoggedIn) {
    console.log('⚠️ [监控端] 用户未登录，停止获取用户信息')
    if (userInfoTimer) {
      clearInterval(userInfoTimer)
      userInfoTimer = null
    }
    router.push('/')
    return
  }

  // 如果已经有用户信息，清除定时器
  if (authStore.user) {
    console.log('✅ [监控端] 用户信息已存在，停止定时获取')
    if (userInfoTimer) {
      clearInterval(userInfoTimer)
      userInfoTimer = null
    }
    return
  }

  // 尝试获取用户信息
  if (!silent) {
    console.log('🔄 [监控端] 尝试获取用户信息...')
  }
  try {
    await authStore.getUserInfo(silent)
    if (authStore.user) {
      console.log('✅ [监控端] 用户信息获取成功', authStore.user)
      // 获取成功后清除定时器
      if (userInfoTimer) {
        clearInterval(userInfoTimer)
        userInfoTimer = null
      }
    }
  } catch (error) {
    if (!silent) {
      console.error('❌ [监控端] 获取用户信息失败，15秒后重试', error)
    }
  }
}

// 组件挂载时检查登录状态并获取用户信息
onMounted(async () => {
  updateMobileState()
  window.addEventListener('resize', updateMobileState)

  if (!authStore.isLoggedIn) {
    router.push('/')
    return
  }
  
  // 首次尝试获取用户信息（不静默，显示错误）
  await fetchUserInfo(false)
  
  // 如果首次获取失败，启动定时器每15秒重试一次（静默模式）
  if (!authStore.user && authStore.isLoggedIn) {
    console.log('⏰ [监控端] 启动定时器，每15秒静默重试获取用户信息')
    userInfoTimer = setInterval(() => fetchUserInfo(true), 15000)
  }
})

// 组件卸载时清除定时器
onUnmounted(() => {
  window.removeEventListener('resize', updateMobileState)

  if (userInfoTimer) {
    console.log('🧹 [监控端] 清除用户信息获取定时器')
    clearInterval(userInfoTimer)
    userInfoTimer = null
  }
})
</script>

<style scoped lang="scss">
.main-layout {
  height: 100vh;
}

.mobile-sidebar-mask {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.45);
  z-index: 2000;
}

.sidebar {
  background: linear-gradient(180deg, #1e293b 0%, #0f172a 100%);
  transition: width 0.3s;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background: linear-gradient(135deg, #10b981, #059669);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.logo-text {
  font-size: 15px;
  font-weight: 600;
  color: #f1f5f9;
  white-space: nowrap;
}

.logo-mini {
  width: 38px;
  height: 38px;
  border-radius: 8px;
  background: linear-gradient(135deg, #10b981, #059669);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.menu-scrollbar {
  flex: 1;
}

.sidebar-menu {
  border-right: none !important;
  background: transparent;
  --el-menu-text-color: #94a3b8;
  --el-menu-hover-text-color: #fff;
  --el-menu-active-color: #fff;
  --el-menu-bg-color: transparent;
  --el-menu-hover-bg-color: rgba(16, 185, 129, 0.15);

  :deep(.el-menu-item), :deep(.el-sub-menu__title) {
    height: 46px;
    line-height: 46px;
    margin: 4px 10px;
    border-radius: 6px;
  }

  :deep(.el-menu-item.is-active) {
    background: linear-gradient(90deg, rgba(16, 185, 129, 0.3), rgba(16, 185, 129, 0.1));
    color: #fff;
  }

  :deep(.el-sub-menu .el-menu) {
    background: transparent !important;
  }

  :deep(.el-sub-menu .el-menu-item) {
    padding-left: 48px !important;
    height: 40px;
    line-height: 40px;
  }
}

.main-container {
  background: #f1f5f9;
}

.header {
  height: 56px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  font-size: 18px;
  color: #6b7280;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 6px;

  &:hover {
    background: #f3f4f6;
  }
}

.avatar {
  background: linear-gradient(135deg, #10b981, #059669);
}

.username {
  font-size: 14px;
  color: #374151;
}

.main-content {
  padding: 20px;
  overflow-y: auto;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.2s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
@media (max-width: 768px) {
  .main-layout {
    &.is-mobile {
      .sidebar {
        position: fixed;
        top: 0;
        left: 0;
        bottom: 0;
        width: 240px !important;
        z-index: 2001;
        transform: translateX(-100%);
        transition: transform 0.3s ease;

        &.open {
          transform: translateX(0);
        }
      }

      .header {
        padding: 0 14px;
      }

      .header-right {
        gap: 8px;
      }

      .main-content {
        padding: 12px;
      }

      .footer {
        flex-direction: column;
        justify-content: center;
        gap: 4px;
        height: auto;
        padding: 8px 12px;
      }

      .username,
      .el-breadcrumb {
        display: none;
      }
    }
  }
}
</style>

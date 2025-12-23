<template>
  <el-container class="main-layout">
    <!-- 侧边栏 -->
    <el-aside :width="collapsed ? '64px' : '240px'" class="sidebar">
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
          <el-sub-menu index="notice">
            <template #title>
              <el-icon><Bell /></el-icon>
              <span>公告管理</span>
            </template>
            <el-menu-item index="/main/notice/admin">管理端公告管理</el-menu-item>
            <el-menu-item index="/main/notice/user">用户端公告管理</el-menu-item>
            <el-menu-item index="/main/notice/monitor">监测端公告管理</el-menu-item>
          </el-sub-menu>

          <!-- 广告管理 -->
          <el-sub-menu index="advertisement">
            <template #title>
              <el-icon><Picture /></el-icon>
              <span>广告管理</span>
            </template>
            <el-sub-menu index="image-ad">
              <template #title>
                <span>图片广告管理</span>
              </template>
              <el-menu-item index="/main/ad/image/admin">管理端广告管理</el-menu-item>
              <el-menu-item index="/main/ad/image/user">用户端广告管理</el-menu-item>
              <el-menu-item index="/main/ad/image/monitor">监测端广告管理</el-menu-item>
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
            @click="collapsed = !collapsed"
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
              <el-avatar :size="32" class="avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">{{ authStore.user?.adminUsername || '管理员' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
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
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  House, Bell, Picture, User, UserFilled, Connection, Lock, Monitor,
  TrendCharts, DataLine, DataAnalysis, Fold, Expand, FullScreen,
  ArrowDown, SwitchButton
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const collapsed = ref(false)
const activeMenu = computed(() => route.path)

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
    '/main/security/grafana': 'Grafana'
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
  if (cmd === 'logout') {
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
  }
}

onMounted(async () => {
  if (!authStore.user && authStore.isLoggedIn) {
    try {
      await authStore.getUserInfo()
    } catch (e) {
      console.error('获取用户信息失败', e)
    }
  }
})
</script>

<style scoped lang="scss">
.main-layout {
  height: 100vh;
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
</style>

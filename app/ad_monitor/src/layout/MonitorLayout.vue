<template>
  <el-container class="monitor-layout">
    <!-- 侧边栏 -->
    <el-aside :width="sidebarCollapsed ? '64px' : '220px'" class="sidebar">
      <div class="sidebar-header">
        <div class="logo" v-show="!sidebarCollapsed">
          <div class="logo-icon">
            <el-icon><DataAnalysis /></el-icon>
          </div>
          <span class="logo-text">观测广告端</span>
        </div>
        <div class="logo-mini" v-show="sidebarCollapsed">
          <el-icon><DataAnalysis /></el-icon>
        </div>
      </div>

      <!-- 菜单导航 -->
      <el-menu
        :default-active="activeMenu"
        :collapse="sidebarCollapsed"
        :unique-opened="true"
        router
        class="sidebar-menu"
      >
        <el-menu-item index="/monitor/dashboard">
          <el-icon><House /></el-icon>
          <span>首页概览</span>
        </el-menu-item>

        <el-sub-menu index="advertisement">
          <template #title>
            <el-icon><Picture /></el-icon>
            <span>广告管理</span>
          </template>
          <el-menu-item index="/monitor/advertisement?type=admin">管理端广告</el-menu-item>
          <el-menu-item index="/monitor/advertisement?type=user">用户端广告</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="announcement">
          <template #title>
            <el-icon><Bell /></el-icon>
            <span>公告管理</span>
          </template>
          <el-menu-item index="/monitor/announcement?type=admin">管理端公告</el-menu-item>
          <el-menu-item index="/monitor/announcement?type=user">用户端公告</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="statistics">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>数据统计</span>
          </template>
          <el-menu-item index="/monitor/statistics?type=admin">管理端统计</el-menu-item>
          <el-menu-item index="/monitor/statistics?type=user">用户端统计</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container class="main-container">
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-left">
          <el-button
            :icon="sidebarCollapsed ? Expand : Fold"
            @click="toggleSidebar"
            text
            size="large"
            class="collapse-button"
          />
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/monitor/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentTitle">{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <el-button :icon="FullScreen" text class="header-button" @click="toggleFullScreen" />
          
          <el-dropdown @command="handleUserCommand" class="user-dropdown">
            <div class="user-info">
              <el-avatar :size="32" class="user-avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username" v-show="!sidebarCollapsed">
                {{ currentUser?.adminUsername || '管理员' }}
              </span>
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

      <!-- 主要内容 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>

      <!-- 底部信息 -->
      <el-footer class="footer">
        <span>© 2025 易投简历观测与广告端 - EasyApplyResume Monitor</span>
        <span>Version v1.0.0</span>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import {
  House, Picture, Bell, TrendCharts, DataAnalysis,
  User, ArrowDown, SwitchButton, FullScreen, Fold, Expand
} from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const sidebarCollapsed = ref(false)
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

const currentUser = computed(() => authStore.user)
const activeMenu = computed(() => route.path + (route.query.type ? `?type=${route.query.type}` : ''))
const currentTitle = computed(() => route.meta.title as string)

const toggleFullScreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

const handleUserCommand = async (command: string) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      authStore.clearAuth()
      ElMessage.success('退出登录成功')
      router.push('/login')
    } catch (error) {
      // 用户取消
    }
  }
}

onMounted(async () => {
  if (!authStore.user && authStore.isLoggedIn) {
    try {
      await authStore.getUserInfo()
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }
})
</script>

<style scoped lang="scss">
.monitor-layout {
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  background: linear-gradient(180deg, #064e3b 0%, #022c22 100%);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  transition: width 0.3s ease;
  overflow: hidden;
}

.sidebar-header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #f1f5f9;
  white-space: nowrap;
}

.logo-mini {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.sidebar-menu {
  border-right: none !important;
  --el-menu-text-color: #a7f3d0;
  --el-menu-hover-text-color: #ffffff;
  --el-menu-active-color: #ffffff;
  --el-menu-bg-color: transparent;
  --el-menu-hover-bg-color: rgba(16, 185, 129, 0.2);
  
  :deep(.el-menu-item), :deep(.el-sub-menu__title) {
    height: 48px;
    line-height: 48px;
    margin: 4px 12px;
    border-radius: 8px;
    
    &:hover {
      background-color: rgba(16, 185, 129, 0.25);
    }
  }
  
  :deep(.el-menu-item.is-active) {
    background: linear-gradient(90deg, rgba(16, 185, 129, 0.4) 0%, rgba(16, 185, 129, 0.2) 100%);
    color: #ffffff;
    font-weight: 600;
  }
  
  :deep(.el-sub-menu .el-menu-item) {
    margin: 2px 20px;
    height: 42px;
    line-height: 42px;
  }
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f0fdf4;
}

.header {
  height: 60px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  z-index: 1000;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-button {
  font-size: 20px;
  color: #4b5563;
  
  &:hover {
    color: #10b981;
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-button {
  font-size: 18px;
  color: #6b7280;
  
  &:hover {
    color: #10b981;
  }
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 10px;
  border-radius: 8px;
  
  &:hover {
    background-color: #ecfdf5;
  }
}

.user-avatar {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f0fdf4;
}

.footer {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  font-size: 12px;
  color: #6b7280;
  background: white;
  border-top: 1px solid #e5e7eb;
}

.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.2s ease;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-10px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(10px);
}
</style>

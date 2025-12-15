<template>
  <el-container class="admin-layout">
    <!-- 侧边栏 -->
    <el-aside :width="sidebarCollapsed ? '64px' : '240px'" class="sidebar">
      <div class="sidebar-header">
        <div class="logo" v-show="!sidebarCollapsed">
          <div class="logo-icon">
            <i class="el-icon-document"></i>
          </div>
          <span class="logo-text">易投简历</span>
        </div>
        <div class="logo-mini" v-show="sidebarCollapsed">
          <div class="logo-icon-mini">
            <i class="el-icon-document"></i>
          </div>
        </div>
      </div>

      <!-- 菜单导航 -->
      <el-menu
        :default-active="activeMenu"
        :collapse="sidebarCollapsed"
        :unique-opened="true"
        router
        class="sidebar-menu"
        @select="handleMenuSelect"
      >
        <!-- 首页 -->
        <el-menu-item index="/admin/dashboard">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <!-- 网站管理 -->
        <el-sub-menu index="/admin/user">
          <template #title>
            <el-icon><User /></el-icon>
            <span>网站管理</span>
          </template>
          <el-menu-item index="/admin/user/admin">管理员管理</el-menu-item>
          <el-menu-item index="/admin/user/role">角色管理</el-menu-item>
          <el-menu-item index="/admin/user/permission">权限管理</el-menu-item>
        </el-sub-menu>

        <!-- 文章管理 -->
        <el-sub-menu index="/admin/article">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>文章管理</span>
          </template>
          <el-menu-item index="/admin/article/job-advice">求职攻略文章管理</el-menu-item>
        </el-sub-menu>

        <!-- 招聘管理 -->
        <el-sub-menu index="/admin/recruitment">
          <template #title>
            <el-icon><Briefcase /></el-icon>
            <span>招聘管理</span>
          </template>
          <el-menu-item index="/admin/recruitment/positions">招聘岗位管理</el-menu-item>
          <el-menu-item index="/admin/recruitment/information">招聘信息管理</el-menu-item>
        </el-sub-menu>

        <!-- 简历管理 -->
        <el-sub-menu index="/admin/resume">
          <template #title>
            <el-icon><DocumentCopy /></el-icon>
            <span>简历管理</span>
          </template>
          <el-menu-item index="/admin/resume/template">简历模版管理</el-menu-item>
          <el-menu-item index="/admin/resume/system-deleted">系统删除简历管理</el-menu-item>
        </el-sub-menu>

        <!-- Map管理 -->
        <el-sub-menu index="/admin/map">
          <template #title>
            <el-icon><Location /></el-icon>
            <span>Map管理</span>
          </template>
          <el-menu-item index="/admin/map/industry">行业Map管理</el-menu-item>
        </el-sub-menu>

        <!-- AI助手 -->
        <el-sub-menu index="/admin/ai">
          <template #title>
            <el-icon><Cpu /></el-icon>
            <span>AI助手</span>
          </template>
          <el-menu-item index="/admin/ai/chat">AI智能问答助手</el-menu-item>
          <el-menu-item index="/admin/ai/agent">AI智能体助手</el-menu-item>
        </el-sub-menu>

        <!-- 反馈管理 -->
        <el-sub-menu index="/admin/feedback">
          <template #title>
            <el-icon><ChatLineSquare /></el-icon>
            <span>反馈管理</span>
          </template>
          <el-menu-item index="/admin/feedback/user-management">用户端反馈管理</el-menu-item>
          <el-menu-item index="/admin/feedback/management">管理端反馈管理</el-menu-item>
          <el-menu-item index="/admin/feedback/user-records">用户端反馈记录</el-menu-item>
          <el-menu-item index="/admin/feedback/records">管理端反馈记录</el-menu-item>
        </el-sub-menu>

        <!-- 内部系统 -->
        <el-sub-menu index="/admin/system">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>内部系统</span>
          </template>
          <el-menu-item index="/admin/system/user-portal">
            <el-icon><User /></el-icon>
            <span>易投简历用户端</span>
          </el-menu-item>
          <el-menu-item index="/admin/system/observation-portal">
            <el-icon><DataAnalysis /></el-icon>
            <span>易投简历观测与广告端</span>
          </el-menu-item>
        </el-sub-menu>

        <!-- 外部API -->
        <el-sub-menu index="external-api">
          <template #title>
            <el-icon><Connection /></el-icon>
            <span>外部API</span>
          </template>
          <el-menu-item index="/admin/external-api/bailian">
            <el-icon><MagicStick /></el-icon>
            <span>阿里云百炼平台</span>
          </el-menu-item>
          <el-menu-item index="/admin/external-api/sms">
            <el-icon><Message /></el-icon>
            <span>阿里云短信平台</span>
          </el-menu-item>
          <el-menu-item index="/admin/external-api/searchapi">
            <el-icon><Search /></el-icon>
            <span>SearchAPI平台</span>
          </el-menu-item>
          <el-menu-item index="/admin/external-api/amap">
            <el-icon><Location /></el-icon>
            <span>高德开放平台</span>
          </el-menu-item>
        </el-sub-menu>

        <!-- API文档中心 -->
        <el-sub-menu index="api-docs">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>API文档中心</span>
          </template>
          <el-menu-item index="/admin/api-docs">
            <el-icon><Link /></el-icon>
            <span>API对外文档中心</span>
          </el-menu-item>
          <el-menu-item index="internal-api-docs">
            <el-icon><Lock /></el-icon>
            <span>API对内文档中心</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container class="main-container">
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-left">
          <!-- 折叠按钮 -->
          <el-button
            :icon="sidebarCollapsed ? Expand : Fold"
            @click="toggleSidebar"
            text
            size="large"
            class="collapse-button"
          />
        </div>

        <div class="header-right">
          <!-- 全局搜索 -->
          <el-input
            v-model="searchText"
            placeholder="搜索..."
            :prefix-icon="Search"
            size="default"
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          />

          <!-- 消息通知 -->
          <el-badge :value="0" :max="99" class="notification-badge">
            <el-button :icon="Bell" text class="header-button" />
          </el-badge>

          <!-- 全屏切换 -->
          <el-button 
            :icon="FullScreen" 
            text 
            class="header-button"
            @click="toggleFullScreen"
          />

          <!-- 用户菜单 -->
          <el-dropdown @command="handleUserCommand" class="user-dropdown">
            <div class="user-info">
              <el-avatar 
                :src="currentUser?.adminImage || defaultAvatar" 
                :size="36"
                class="user-avatar"
              />
              <span class="username" v-show="!sidebarCollapsed">
                {{ currentUser?.adminUsername || '管理员' }}
              </span>
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

      <!-- 面包屑导航 -->
      <div class="breadcrumb-container">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item 
            v-for="item in breadcrumbList" 
            :key="item.path"
            :to="{ path: item.path }"
          >
            {{ item.title }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>

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
        <div class="footer-content">
          <span>© 2025 易投简历管理端 - EasyApplyResume Admin</span>
          <span>Version v1.7.0</span>
        </div>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import {
  House,
  User,
  Document,
  Briefcase,
  DocumentCopy,
  Location,
  Cpu,
  Monitor,
  ChatLineSquare,
  Search,
  Bell,
  FullScreen,
  Fold,
  Expand,
  ArrowDown,
  SwitchButton,
  Link,
  Lock,
  DataAnalysis,
  Connection,
  MagicStick,
  Message
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

// 侧边栏状态
const sidebarCollapsed = ref(false)
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

// 搜索功能
const searchText = ref('')
const handleSearch = () => {
  console.log('搜索:', searchText.value)
}

// 菜单选择处理
const handleMenuSelect = (index: string) => {
  if (index === 'internal-api-docs') {
    // 跳转到内部API文档页面
    router.push('/admin/api-docs/internal')
  } else {
    // 其他菜单项正常跳转
    router.push(index)
  }
}

// 全屏切换
const toggleFullScreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

// 用户信息
const currentUser = computed(() => authStore.user)
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// 用户菜单操作
const handleUserCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/admin/profile')
      break
    case 'feedback':
      router.push('/admin/feedback/submit')
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await authStore.logout()
    ElMessage.success('退出登录成功')
    router.push('/login')
  } catch (error) {
    // 用户取消退出
  }
}

// 激活的菜单
const activeMenu = computed(() => {
  return route.path
})

// 面包屑导航
const breadcrumbList = ref<Array<{ path: string; title: string }>>([])

// 获取路由信息
const getCurrentRouteInfo = (path: string) => {
  const routeMap: Record<string, string> = {
    '/admin/dashboard': '首页',
    '/admin/user': '网站管理',
    '/admin/user/admin': '管理员管理',
    '/admin/user/role': '角色管理',
    '/admin/user/permission': '权限管理',
    '/admin/article': '文章管理',
    '/admin/article/job-advice': '求职攻略文章管理',
    '/admin/recruitment': '招聘管理',
    '/admin/recruitment/positions': '招聘岗位管理',
    '/admin/recruitment/information': '招聘信息管理',
    '/admin/resume': '简历管理',
    '/admin/resume/template': '简历模版管理',
    '/admin/resume/system-deleted': '系统删除简历管理',
    '/admin/map': 'Map管理',
    '/admin/map/industry': '行业Map管理',
    '/admin/ai': 'AI助手',
    '/admin/ai/chat': 'AI智能问答助手',
    '/admin/ai/agent': 'AI智能体助手',
    '/admin/system': '内部系统',
    '/admin/system/user-portal': '易投简历用户端',
    '/admin/system/observation-portal': '易投简历观测与广告端',
    '/admin/feedback': '反馈管理',
    '/admin/feedback/submit': '意见反馈',
    '/admin/feedback/user-management': '用户端反馈管理',
    '/admin/feedback/management': '管理端反馈管理',
    '/admin/feedback/user-records': '用户端反馈记录',
    '/admin/feedback/records': '管理端反馈记录',
    '/admin/api-docs': 'API对外文档中心',
    'internal-api-docs': 'API对内文档中心',
    '/admin/external-api': '外部API',
    '/admin/external-api/bailian': '阿里云百炼平台',
    '/admin/external-api/sms': '阿里云短信平台',
    '/admin/external-api/searchapi': 'SearchAPI平台',
    '/admin/external-api/amap': '高德开放平台'
  }

  const breadcrumbs = []
  const pathSegments = path.split('/').filter(Boolean)

  for (let i = 0; i < pathSegments.length; i++) {
    const currentPath = '/' + pathSegments.slice(0, i + 1).join('/')
    const title = routeMap[currentPath]

    if (title && title !== '首页') {
      breadcrumbs.push({
        path: currentPath,
        title
      })
    }
  }

  return breadcrumbs
}

// 监听路由变化更新面包屑
watch(
  () => route.path,
  (newPath) => {
    breadcrumbList.value = getCurrentRouteInfo(newPath)
  },
  { immediate: true }
)

// 组件挂载时检查登录状态并获取用户信息
onMounted(async () => {
  if (!authStore.isLoggedIn) {
    router.push('/login')
    return
  }
  
  // 如果已登录但没有用户信息，则获取用户信息
  if (!authStore.user) {
    console.log('🔄 AdminLayout: 检测到已登录但没有用户信息，开始获取...')
    try {
      await authStore.getUserInfo()
      console.log('✅ AdminLayout: 用户信息获取成功', authStore.user)
    } catch (error) {
      console.error('❌ AdminLayout: 获取用户信息失败', error)
    }
  }
})
</script>

<style scoped lang="scss">
.admin-layout {
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  background: linear-gradient(180deg, #1e293b 0%, #0f172a 100%);
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
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #f1f5f9;
  white-space: nowrap;
}

.logo-mini {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-icon-mini {
  color: white;
  font-size: 16px;
}

.sidebar-menu {
  border-right: none !important;
  flex: 1;
  --el-menu-text-color: #cbd5e1;
  --el-menu-hover-text-color: #ffffff;
  --el-menu-active-color: #ffffff;
  --el-menu-bg-color: transparent;
  --el-menu-hover-bg-color: rgba(59, 130, 246, 0.15);
  
  .el-menu-item, .el-sub-menu__title {
    height: 50px;
    line-height: 50px;
    margin: 4px 12px;
    border-radius: 8px;
    transition: all 0.3s ease;
    
    &:hover {
      background-color: rgba(59, 130, 246, 0.2);
      color: #ffffff;
    }
  }
  
  .el-menu-item.is-active {
    background: linear-gradient(90deg, rgba(59, 130, 246, 0.3) 0%, rgba(59, 130, 246, 0.15) 100%);
    color: #ffffff;
    font-weight: 600;
    border-left: 3px solid #3b82f6;
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 3px;
      background: #3b82f6;
      border-radius: 0 3px 3px 0;
    }
  }
  
  .el-sub-menu.is-active > .el-sub-menu__title {
    color: #ffffff;
    font-weight: 600;
  }
  
  .el-sub-menu .el-menu-item {
    margin: 2px 24px;
    height: 44px;
    line-height: 44px;
    padding-left: 48px !important;
    
    &::before {
      content: '';
      position: absolute;
      left: 32px;
      top: 50%;
      transform: translateY(-50%);
      width: 6px;
      height: 6px;
      background-color: #64748b;
      border-radius: 50%;
      transition: all 0.3s ease;
    }
    
    &:hover::before {
      background-color: #3b82f6;
      width: 8px;
      height: 8px;
    }
    
    &.is-active {
      background: linear-gradient(90deg, rgba(59, 130, 246, 0.25) 0%, rgba(59, 130, 246, 0.1) 100%);
      color: #ffffff;
      font-weight: 500;
      
      &::before {
        background-color: #3b82f6;
        width: 8px;
        height: 8px;
      }
    }
  }
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  height: 64px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  z-index: 1000;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-button {
  font-size: 20px;
  color: #4b5563;
  
  &:hover {
    color: #3b82f6;
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-input {
  width: 240px;
  
  ::v-deep(.el-input__wrapper) {
    border-radius: 8px;
  }
}

.notification-badge {
  --el-badge-bg-color: #ef4444;
}

.header-button {
  font-size: 18px;
  color: #6b7280;
  
  &:hover {
    color: #3b82f6;
  }
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
  
  &:hover {
    background-color: #f3f4f6;
  }
}

.user-avatar {
  border: 2px solid #e5e7eb;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.breadcrumb-container {
  padding: 16px 24px 0;
  background: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
}

.main-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  background: #f9fafb;
}

.footer {
  height: 48px;
  background: white;
  border-top: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 24px;
}

.footer-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  font-size: 12px;
  color: #9ca3af;
}

// 页面切换动画
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

// 响应式设计
@media (max-width: 768px) {
  .search-input {
    width: 0;
    overflow: hidden;
  }
  
  .username {
    display: none;
  }
  
  .main-content {
    padding: 16px;
  }
  
  .breadcrumb-container {
    padding: 12px 16px 0;
  }
  
  .footer-content {
    flex-direction: column;
    gap: 4px;
  }
}

// 暗黑模式适配
@media (prefers-color-scheme: dark) {
  .header {
    background: #1f2937;
    border-color: #374151;
  }
  
  .search-input ::v-deep(.el-input__wrapper) {
    background-color: #374151;
    border-color: #4b5563;
  }
  
  .user-info:hover {
    background-color: #374151;
  }
  
  .username {
    color: #f9fafb;
  }
  
  .main-content {
    background: #1f2937;
  }
  
  .breadcrumb-container {
    background: #111827;
    border-color: #374151;
  }
  
  .footer {
    background: #1f2937;
    border-color: #374151;
  }
}
</style>
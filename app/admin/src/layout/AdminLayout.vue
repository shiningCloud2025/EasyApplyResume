<template>
  <el-container :class="['admin-layout', { 'is-mobile': isMobile, 'mobile-sidebar-open': mobileSidebarOpen }]">
    <div v-if="isMobile && mobileSidebarOpen" class="mobile-sidebar-mask" @click="closeMobileSidebar"></div>
    <!-- 侧边栏 -->
    <el-aside :width="sidebarWidth" :class="['sidebar', { collapsed: sidebarCollapsed, mobile: isMobile, open: mobileSidebarOpen }]">
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
        <el-sub-menu v-if="showWebsiteManagementMenu" index="/admin/user">
          <template #title>
            <el-icon><User /></el-icon>
            <span>网站管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleWebsiteManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <!-- 文章管理 -->
        <el-sub-menu v-if="showArticleManagementMenu" index="/admin/article">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>文章管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleArticleManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>


        <!-- 招聘管理 -->
        <el-sub-menu v-if="showRecruitmentManagementMenu" index="/admin/recruitment">
          <template #title>
            <el-icon><Briefcase /></el-icon>
            <span>招聘管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleRecruitmentManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <!-- 简历管理 -->
        <el-sub-menu v-if="showResumeManagementMenu" index="/admin/resume">
          <template #title>
            <el-icon><DocumentCopy /></el-icon>
            <span>简历管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleResumeManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <!-- Map管理 -->
        <el-sub-menu v-if="showMapManagementMenu" index="/admin/map">
          <template #title>
            <el-icon><Location /></el-icon>
            <span>Map管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleMapManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <!-- AI管理 -->
        <el-sub-menu v-if="showAiManagementMenu" index="/admin/ai">
          <template #title>
            <el-icon><Cpu /></el-icon>
            <span>AI管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleAiManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <!-- 反馈管理 -->
        <el-sub-menu v-if="showFeedbackManagementMenu" index="/admin/feedback">
          <template #title>
            <el-icon><ChatLineSquare /></el-icon>
            <span>反馈管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleFeedbackManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <!-- 关于我们管理 / 帮助中心管理（与反馈/内部系统同级，但可展开子模块） -->
        <el-sub-menu v-if="showAboutUsManagementMenu" index="/admin/about-us">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>关于我们管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleAboutUsManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu v-if="showHelpCenterManagementMenu" index="/admin/help-center">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>帮助中心管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleHelpCenterManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu v-if="showWrittenTestManagementMenu" index="/admin/written-test">
          <template #title>
            <el-icon><EditPen /></el-icon>
            <span>笔试专项管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleWrittenTestManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu v-if="showScoreModelManagementMenu" index="/admin/score-model">
          <template #title>
            <el-icon><Histogram /></el-icon>
            <span>评分模型管理</span>
          </template>
          <el-menu-item
            v-for="item in visibleScoreModelManagementMenus"
            :key="item.index"
            :index="item.index"
          >
            {{ item.title }}
          </el-menu-item>
        </el-sub-menu>

        <!-- 内部系统 -->
        <el-sub-menu v-if="showInternalSystemMenu" index="/admin/system">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>内部系统</span>
          </template>
          <el-menu-item
            v-for="item in visibleInternalSystemMenus"
            :key="item.index"
            :index="item.index"
          >
            <el-icon :is="item.icon" />
            <span>{{ item.title }}</span>
          </el-menu-item>
        </el-sub-menu>

        <!-- 外部系统 -->
        <el-sub-menu index="external-api">
          <template #title>
            <el-icon><Connection /></el-icon>
            <span>外部系统</span>
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
          <el-menu-item index="/admin/external-api/baidu-cloud">
            <el-icon><Cpu /></el-icon>
            <span>百度智能云平台</span>
          </el-menu-item>
          <el-menu-item index="/admin/external-api/qiniu">
            <el-icon><Connection /></el-icon>
            <span>七牛云平台</span>
          </el-menu-item>
          <el-menu-item index="/admin/external-api/autodl">
            <el-icon><Monitor /></el-icon>
            <span>AutoDL平台</span>
          </el-menu-item>
          <el-menu-item index="/admin/external-api/bigmodel">
            <el-icon><MagicStick /></el-icon>
            <span>智谱开放平台</span>
          </el-menu-item>
          <el-menu-item index="/admin/external-api/volcengine">
            <el-icon><Cpu /></el-icon>
            <span>火山引擎平台</span>
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

          <!-- 发送邮件 -->
          <el-button 
            :icon="Promotion" 
            text 
            class="header-button"
            @click="showSendResumeDialog"
            title="发送邮件"
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
    <!-- 发送邮件对话框 -->
    <el-dialog
      v-model="sendResumeDialogVisible"
      title="发送邮件"
      width="800px"
      :close-on-click-modal="false"
      @open="resetSendResumeFormValidation"
    >
      <el-form
        ref="sendResumeFormRef"
        :model="sendResumeForm"
        :rules="sendResumeRules"
        label-width="100px"
        validate-on-rule-change="false"
      >
        <el-form-item label="收件人" prop="toEmail">
          <el-input
            v-model="sendResumeForm.toEmail"
            placeholder="请输入收件人邮箱"
            maxlength="25"
            show-word-limit
          >
            <template #prepend>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="邮件主题" prop="subject">
          <el-input
            v-model="sendResumeForm.subject"
            placeholder="请输入邮件主题"
            maxlength="35"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="邮件内容" prop="htmlContent">
          <div style="border: 1px solid #dcdfe6; border-radius: 4px;">
            <Toolbar
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              mode="default"
              style="border-bottom: 1px solid #dcdfe6"
            />
            <Editor
              v-model="sendResumeForm.htmlContent"
              :defaultConfig="editorConfig"
              mode="default"
              style="height: 300px; overflow-y: hidden;"
              @onCreated="handleEditorCreated"
            />
          </div>
          <div style="font-size: 12px; color: #909399; margin-top: 4px;">
            支持富文本格式，内容将以HTML格式发送
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="sendResumeDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSendResumeSubmit" :loading="sendingResume">
            <el-icon><Promotion /></el-icon>
            发送邮件
          </el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 空闲广告轮播 -->
    <IdleAdCarousel :idle-time="7 * 60 * 1000" :enabled="true" />
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch, shallowRef, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore, websiteManagementPagePermissions, articleManagementPagePermissions, recruitmentManagementPagePermissions, resumeManagementPagePermissions, mapManagementPagePermissions, aiManagementPagePermissions, feedbackManagementPagePermissions, aboutUsManagementPagePermissions, helpCenterManagementPagePermissions, writtenTestManagementPagePermissions, scoreModelManagementPagePermissions, internalSystemPagePermissions } from '@/store/auth'
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
  EditPen,
  Histogram,
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
  Message,
  Promotion
} from '@element-plus/icons-vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import '@wangeditor/editor/dist/css/style.css'
import { emailApi } from '@/api/admin'
import type { FormInstance } from 'element-plus'
import IdleAdCarousel from './IdleAdCarousel.vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

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
  return sidebarCollapsed.value ? '64px' : '240px'
})

const closeMobileSidebar = () => {
  mobileSidebarOpen.value = false
}

// 侧边栏状态
const sidebarCollapsed = ref(false)
const toggleSidebar = () => {
  if (isMobile.value) {
    mobileSidebarOpen.value = !mobileSidebarOpen.value
    return
  }
  sidebarCollapsed.value = !sidebarCollapsed.value
}

// 搜索功能
const searchText = ref('')
const handleSearch = () => {
  console.log('搜索:', searchText.value)
}

// 菜单选择处理
const handleMenuSelect = (index: string) => {
  if (isMobile.value) {
    closeMobileSidebar()
  }

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

const websiteManagementMenus = [
  {
    index: '/admin/user/admin',
    title: '管理员管理',
    permission: websiteManagementPagePermissions.admin
  },
  {
    index: '/admin/user/role',
    title: '角色管理',
    permission: websiteManagementPagePermissions.role
  },
  {
    index: '/admin/user/permission',
    title: '权限管理',
    permission: websiteManagementPagePermissions.permission
  }
]

const visibleWebsiteManagementMenus = computed(() => {
  return websiteManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showWebsiteManagementMenu = computed(() => {
  return visibleWebsiteManagementMenus.value.length > 0 && authStore.canAccessWebsiteManagement()
})

const articleManagementMenus = [
  {
    index: '/admin/article/job-advice',
    title: '求职攻略文章管理',
    permission: articleManagementPagePermissions.jobAdvice
  }
]

const visibleArticleManagementMenus = computed(() => {
  return articleManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showArticleManagementMenu = computed(() => {
  return visibleArticleManagementMenus.value.length > 0
})

const recruitmentManagementMenus = [
  {
    index: '/admin/recruitment/positions',
    title: '招聘岗位管理',
    permission: recruitmentManagementPagePermissions.position
  },
  {
    index: '/admin/recruitment/information',
    title: '招聘信息管理',
    permission: recruitmentManagementPagePermissions.information
  }
]

const visibleRecruitmentManagementMenus = computed(() => {
  return recruitmentManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showRecruitmentManagementMenu = computed(() => {
  return visibleRecruitmentManagementMenus.value.length > 0
})

const resumeManagementMenus = [
  {
    index: '/admin/resume/template',
    title: '简历模版管理',
    permission: resumeManagementPagePermissions.template
  },
  {
    index: '/admin/resume/system-deleted',
    title: '系统删除简历管理',
    permission: resumeManagementPagePermissions.systemDeleted
  }
]

const visibleResumeManagementMenus = computed(() => {
  return resumeManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showResumeManagementMenu = computed(() => {
  return visibleResumeManagementMenus.value.length > 0
})

const mapManagementMenus = [
  {
    index: '/admin/map/industry',
    title: '行业Map管理',
    permission: mapManagementPagePermissions.industry
  },
  {
    index: '/admin/map/university',
    title: '大学Map管理',
    permission: mapManagementPagePermissions.university
  },
  {
    index: '/admin/map/province',
    title: '省份Map管理',
    permission: mapManagementPagePermissions.province
  },
  {
    index: '/admin/map/city',
    title: '城市Map管理',
    permission: mapManagementPagePermissions.city
  },
  {
    index: '/admin/map/area',
    title: '区县Map管理',
    permission: mapManagementPagePermissions.area
  },
  {
    index: '/admin/map/street',
    title: '街道Map管理',
    permission: mapManagementPagePermissions.street
  }
]

const visibleMapManagementMenus = computed(() => {
  return mapManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showMapManagementMenu = computed(() => {
  return visibleMapManagementMenus.value.length > 0
})

const aiManagementMenus = [
  {
    index: '/admin/ai/chat',
    title: 'AI智能问答助手',
    permission: aiManagementPagePermissions.chat
  },
  {
    index: '/admin/ai/agent',
    title: 'AI智能体助手',
    permission: aiManagementPagePermissions.agent
  },
  {
    index: '/admin/ai/llm-utils-info',
    title: 'LLM调用日志管理',
    permission: aiManagementPagePermissions.llmUtilsInfo
  }
]

const visibleAiManagementMenus = computed(() => {
  return aiManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showAiManagementMenu = computed(() => {
  return visibleAiManagementMenus.value.length > 0
})

const feedbackManagementMenus = [
  {
    index: '/admin/feedback/user-management',
    title: '用户端反馈管理',
    permission: feedbackManagementPagePermissions.userManagement
  },
  {
    index: '/admin/feedback/management',
    title: '管理端反馈管理',
    permission: feedbackManagementPagePermissions.management
  },
  {
    index: '/admin/feedback/user-records',
    title: '用户端反馈记录',
    permission: feedbackManagementPagePermissions.userRecords
  },
  {
    index: '/admin/feedback/records',
    title: '管理端反馈记录',
    permission: feedbackManagementPagePermissions.records
  }
]

const visibleFeedbackManagementMenus = computed(() => {
  return feedbackManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showFeedbackManagementMenu = computed(() => {
  return visibleFeedbackManagementMenus.value.length > 0
})

const aboutUsManagementMenus = [
  {
    index: '/admin/about-us/project-introduce',
    title: '项目介绍',
    permission: aboutUsManagementPagePermissions.projectIntroduce
  },
  {
    index: '/admin/about-us/team-introduce',
    title: '团队介绍',
    permission: aboutUsManagementPagePermissions.teamIntroduce
  },
  {
    index: '/admin/about-us/develop-history',
    title: '发展历程',
    permission: aboutUsManagementPagePermissions.developHistory
  },
  {
    index: '/admin/about-us/join-us',
    title: '加入我们',
    permission: aboutUsManagementPagePermissions.joinUs
  },
  {
    index: '/admin/about-us/partner-introduce',
    title: '合作伙伴',
    permission: aboutUsManagementPagePermissions.partnerIntroduce
  },
  {
    index: '/admin/about-us/media-report',
    title: '媒体报道',
    permission: aboutUsManagementPagePermissions.mediaReport
  }
]

const visibleAboutUsManagementMenus = computed(() => {
  return aboutUsManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showAboutUsManagementMenu = computed(() => {
  return visibleAboutUsManagementMenus.value.length > 0
})

const helpCenterManagementMenus = [
  {
    index: '/admin/help-center/faq',
    title: 'FAQ管理',
    permission: helpCenterManagementPagePermissions.faq
  },
  {
    index: '/admin/help-center/customer-service',
    title: '客服管理',
    permission: helpCenterManagementPagePermissions.customerService
  },
  {
    index: '/admin/help-center/user-guide',
    title: '使用指南管理',
    permission: helpCenterManagementPagePermissions.userGuide
  }
]

const visibleHelpCenterManagementMenus = computed(() => {
  return helpCenterManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showHelpCenterManagementMenu = computed(() => {
  return visibleHelpCenterManagementMenus.value.length > 0
})

const writtenTestManagementMenus = [
  {
    index: '/admin/written-test/first-category',
    title: '题库大类管理',
    permission: writtenTestManagementPagePermissions.firstCategory
  },
  {
    index: '/admin/written-test/second-category',
    title: '题库小类管理',
    permission: writtenTestManagementPagePermissions.secondCategory
  },
  {
    index: '/admin/written-test/question-bank',
    title: '题库题目管理',
    permission: writtenTestManagementPagePermissions.questionBank
  },
  {
    index: '/admin/written-test/user-answer',
    title: '用户答题管理',
    permission: writtenTestManagementPagePermissions.userAnswer
  }
]

const visibleWrittenTestManagementMenus = computed(() => {
  return writtenTestManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showWrittenTestManagementMenu = computed(() => {
  return visibleWrittenTestManagementMenus.value.length > 0
})

const scoreModelManagementMenus = [
  {
    index: '/admin/score-model/training-data',
    title: '训练数据管理',
    permission: scoreModelManagementPagePermissions.trainingData
  },
  {
    index: '/admin/score-model/train-code',
    title: '训练代码管理',
    permission: scoreModelManagementPagePermissions.trainCode
  },
  {
    index: '/admin/score-model/version',
    title: '模型版本管理',
    permission: scoreModelManagementPagePermissions.version
  }
]

const visibleScoreModelManagementMenus = computed(() => {
  return scoreModelManagementMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showScoreModelManagementMenu = computed(() => {
  return visibleScoreModelManagementMenus.value.length > 0
})

const internalSystemMenus = [
  {
    index: '/admin/system/user-portal',
    title: '易投简历用户端',
    permission: internalSystemPagePermissions.userPortal,
    icon: User
  },
  {
    index: '/admin/system/observation-portal',
    title: '易投简历监测与广告端',
    permission: internalSystemPagePermissions.observationPortal,
    icon: DataAnalysis
  },
  {
    index: '/admin/system/nacos-platform',
    title: 'Nacos配置平台',
    permission: internalSystemPagePermissions.nacosPlatform,
    icon: Connection
  },
  {
    index: '/admin/system/yapi-platform',
    title: 'YApi测试平台',
    permission: internalSystemPagePermissions.yapiPlatform,
    icon: Link
  }
]

const visibleInternalSystemMenus = computed(() => {
  return internalSystemMenus.filter((item) => authStore.canAccessRoute(item.permission))
})

const showInternalSystemMenu = computed(() => {
  return visibleInternalSystemMenus.value.length > 0
})

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
    '/admin/about-us': '关于我们管理',
    '/admin/about-us/project-introduce': '项目介绍',
    '/admin/about-us/team-introduce': '团队介绍',
    '/admin/about-us/develop-history': '发展历程',
    '/admin/about-us/join-us': '加入我们',
    '/admin/about-us/partner-introduce': '合作伙伴',
    '/admin/about-us/media-report': '媒体报道',
    '/admin/help-center': '帮助中心管理',
    '/admin/help-center/faq': 'FAQ管理',
    '/admin/help-center/customer-service': '客服管理',
    '/admin/help-center/user-guide': '使用指南管理',
    '/admin/written-test': '笔试专项管理',
    '/admin/written-test/first-category': '题库大类管理',
    '/admin/written-test/second-category': '题库小类管理',
    '/admin/written-test/question-bank': '题库题目管理',
    '/admin/written-test/user-answer': '用户答题管理',
    '/admin/recruitment': '招聘管理',
    '/admin/recruitment/positions': '招聘岗位管理',
    '/admin/recruitment/information': '招聘信息管理',
    '/admin/resume': '简历管理',
    '/admin/resume/template': '简历模版管理',
    '/admin/resume/system-deleted': '系统删除简历管理',
    '/admin/map': 'Map管理',
    '/admin/map/industry': '行业Map管理',
    '/admin/ai': 'AI管理',
    '/admin/ai/chat': 'AI智能问答助手',
    '/admin/ai/agent': 'AI智能体助手',
    '/admin/ai/llm-utils-info': 'LLM调用日志管理',
    '/admin/score-model': '评分模型管理',
    '/admin/score-model/training-data': '训练数据管理',
    '/admin/score-model/train-code': '训练代码管理',
    '/admin/score-model/version': '模型版本管理',
    '/admin/system': '内部系统',
    '/admin/system/user-portal': '易投简历用户端',
    '/admin/system/observation-portal': '易投简历监测与广告端',
    '/admin/system/nacos-platform': 'Nacos配置平台',
    '/admin/system/yapi-platform': 'YApi测试平台',
    '/admin/feedback': '反馈管理',
    '/admin/feedback/submit': '意见反馈',
    '/admin/feedback/user-management': '用户端反馈管理',
    '/admin/feedback/management': '管理端反馈管理',
    '/admin/feedback/user-records': '用户端反馈记录',
    '/admin/feedback/records': '管理端反馈记录',
    '/admin/api-docs': 'API对外文档中心',
    'internal-api-docs': 'API对内文档中心',
    '/admin/external-api': '外部系统',
    '/admin/external-api/bailian': '阿里云百炼平台',
    '/admin/external-api/sms': '阿里云短信平台',
    '/admin/external-api/searchapi': 'SearchAPI平台',
    '/admin/external-api/amap': '高德开放平台',
    '/admin/external-api/baidu-cloud': '百度智能云平台',
    '/admin/external-api/qiniu': '七牛云平台',
    '/admin/external-api/autodl': 'AutoDL平台',
    '/admin/external-api/bigmodel': '智谱开放平台',
    '/admin/external-api/volcengine': '火山引擎平台'
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
    if (isMobile.value) {
      closeMobileSidebar()
    }
  },
  { immediate: true }
)

// 定时器ID
let userInfoTimer: NodeJS.Timeout | null = null

// 获取用户信息的函数
const fetchUserInfo = async (silent: boolean = false) => {
  if (!authStore.isLoggedIn) {
    console.log('⚠️ AdminLayout: 用户未登录，停止获取用户信息')
    if (userInfoTimer) {
      clearInterval(userInfoTimer)
      userInfoTimer = null
    }
    router.push('/login')
    return
  }

  // 如果已经有用户信息，清除定时器
  if (authStore.user) {
    console.log('✅ AdminLayout: 用户信息已存在，停止定时获取')
    if (userInfoTimer) {
      clearInterval(userInfoTimer)
      userInfoTimer = null
    }
    return
  }

  // 尝试获取用户信息
  if (!silent) {
    console.log('🔄 AdminLayout: 尝试获取用户信息...')
  }
  try {
    await authStore.getUserInfo(silent)
    if (authStore.user) {
      console.log('✅ AdminLayout: 用户信息获取成功', authStore.user)
      // 获取成功后清除定时器
      if (userInfoTimer) {
        clearInterval(userInfoTimer)
        userInfoTimer = null
      }
    }
  } catch (error) {
    if (!silent) {
      console.error('❌ AdminLayout: 获取用户信息失败，15秒后重试', error)
    }
  }
}

// 发送邮件相关
const sendResumeDialogVisible = ref(false)
const sendResumeFormRef = ref<FormInstance>()
const sendingResume = ref(false)

// 富文本编辑器相关
const editorRef = shallowRef()
const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入邮件内容，支持富文本格式...',
  MENU_CONF: {}
}
const toolbarConfig: Partial<IToolbarConfig> = {
  toolbarKeys: [
    'headerSelect',
    'bold',
    'italic',
    'underline',
    'color',
    'bgColor',
    '|',
    'fontSize',
    'fontFamily',
    '|',
    'bulletedList',
    'numberedList',
    '|',
    'justifyLeft',
    'justifyCenter',
    'justifyRight',
    '|',
    'emotion',
    'insertLink',
    'insertImage',
    '|',
    'undo',
    'redo'
  ]
}

const sendResumeForm = reactive({
  toEmail: '',
  subject: '',
  htmlContent: ''
})

// 邮箱格式验证规则
const validateEmail = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请输入收件人邮箱'))
  } else {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(value)) {
      callback(new Error('请输入正确的邮箱格式'))
    } else if (value.length > 25) {
      callback(new Error('邮箱长度不能超过25个字符'))
    } else {
      callback()
    }
  }
}

const sendResumeRules = {
  toEmail: [
    { required: true, validator: validateEmail, trigger: ['blur', 'change'] }
  ],
  subject: [
    { required: true, message: '请输入邮件主题', trigger: ['blur', 'change'] },
    { min: 1, max: 35, message: '主题长度在 1 到 35 个字符', trigger: ['blur', 'change'] }
  ],
  htmlContent: [
    { required: true, message: '请输入邮件内容', trigger: ['blur', 'change'] }
  ]
}

// 显示发送邮件对话框
const showSendResumeDialog = () => {
  sendResumeDialogVisible.value = true
  // 初始化表单数据
  sendResumeForm.toEmail = ''
  sendResumeForm.subject = ''
  sendResumeForm.htmlContent = ''
}

// 重置表单验证状态
const resetSendResumeFormValidation = () => {
  // 延迟清除验证状态，确保在DOM更新后执行
  setTimeout(() => {
    if (sendResumeFormRef.value) {
      sendResumeFormRef.value.clearValidate()
    }
  }, 100)
}

// 发送邮件
const handleSendResumeSubmit = async () => {
  if (!sendResumeFormRef.value) return
  
  try {
    await sendResumeFormRef.value.validate()
    
    // 获取当前用户信息
    const currentUserEmail = authStore.user?.adminEmail
    if (!currentUserEmail) {
      ElMessage.error('无法获取当前用户邮箱，请重新登录')
      return
    }
    
    ElMessageBox.confirm(
      `确定要向 ${sendResumeForm.toEmail} 发送邮件吗？`,
      '确认发送',
      {
        type: 'warning',
        confirmButtonText: '确定发送',
        cancelButtonText: '取消'
      }
    ).then(async () => {
      try {
        sendingResume.value = true
        
        // 调用邮件发送API
        await emailApi.sendHtmlEmailSelfDef(
          currentUserEmail,
          sendResumeForm.toEmail,
          sendResumeForm.subject,
          sendResumeForm.htmlContent
        )
        
        ElMessage.success('发送成功')
        sendResumeDialogVisible.value = false
      } catch (error: any) {
        console.error('❌ [发送邮件] 发送失败:', error)
        ElMessage.error('发送失败')
      } finally {
        sendingResume.value = false
      }
    }).catch(() => {
      // 用户取消
    })
  } catch (error) {
    console.log('表单验证失败')
  }
}

// 组件挂载时检查登录状态并获取用户信息
onMounted(async () => {
  updateMobileState()
  window.addEventListener('resize', updateMobileState)

  if (!authStore.isLoggedIn) {
    router.push('/login')
    return
  }
  
  // 首次尝试获取用户信息（不静默，显示错误）
  await fetchUserInfo(false)
  
  // 如果首次获取失败，启动定时器每15秒重试一次（静默模式）
  if (!authStore.user && authStore.isLoggedIn) {
    console.log('⏰ AdminLayout: 启动定时器，每15秒静默重试获取用户信息')
    userInfoTimer = setInterval(() => fetchUserInfo(true), 15000)
  }
})

// 富文本编辑器创建回调
const handleEditorCreated = (editor: any) => {
  editorRef.value = editor
  console.log('📝 富文本编辑器创建成功')
}

// 组件卸载时清除定时器
onUnmounted(() => {
  window.removeEventListener('resize', updateMobileState)

  if (userInfoTimer) {
    console.log('🧹 AdminLayout: 清除用户信息获取定时器')
    clearInterval(userInfoTimer)
    userInfoTimer = null
  }
  
  // 组件卸载时销毁编辑器
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})
</script>

<style scoped lang="scss">
.admin-layout {
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  display: flex;
  flex-direction: column;
  min-height: 0;
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
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
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
  .admin-layout {
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

      .mobile-sidebar-mask {
        position: fixed;
        inset: 0;
        background: rgba(15, 23, 42, 0.45);
        z-index: 2000;
      }

      .header {
        padding: 0 16px;
      }

      .header-right {
        gap: 10px;
      }

      .notification-badge,
      .search-input,
      .breadcrumb-container {
        display: none;
      }

      .main-content {
        padding: 12px;
      }

      .footer {
        padding: 0 12px;
      }

      .footer-content {
        flex-direction: column;
        gap: 4px;
      }
    }
  }
}

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
<template>
  <div class="dashboard">
    <!-- 欢迎卡片 -->
    <div class="welcome-card">
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>欢迎回来，{{ currentUser?.adminUsername || '管理员' }}！</h2>
          <p>今天是 {{ currentDate }}，祝您工作愉快！</p>
        </div>
        <div class="welcome-stats">
          <div class="quick-stat">
            <div class="stat-number">{{ stats.totalTemplates }}</div>
            <div class="stat-label">简历模板</div>
          </div>
          <div class="quick-stat">
            <div class="stat-number">{{ stats.totalUsers }}</div>
            <div class="stat-label">活跃用户</div>
          </div>
          <div class="quick-stat">
            <div class="stat-number">{{ stats.totalRecruitments }}</div>
            <div class="stat-label">招聘信息</div>
          </div>
        </div>
      </div>
      <div class="welcome-image">
        <div class="animated-graphic">
          <div class="circle circle-1"></div>
          <div class="circle circle-2"></div>
          <div class="circle circle-3"></div>
        </div>
      </div>
    </div>

    <!-- 系统介绍区域 -->
    <div class="intro-section">
      <!-- 左侧：系统简介 -->
      <div class="intro-main">
        <div class="intro-header">
          <div class="intro-icon">
            <i class="el-icon-document-copy"></i>
          </div>
          <div class="intro-title">
            <h3>易投简历管理系统</h3>
            <p>一站式简历管理与求职服务平台</p>
          </div>
        </div>
        <div class="intro-desc">
          易投简历是一款专注于简历管理和求职服务的智能平台，为用户提供简历创建、模板选择、
          招聘信息浏览、AI智能助手等全方位功能。本管理端负责平台的内容运营、用户管理和系统配置。
        </div>
      </div>

      <!-- 右侧：技术栈 -->
      <div class="tech-stack">
        <h4>技术架构</h4>
        <div class="tech-tags">
          <span class="tech-tag frontend">Vue 3</span>
          <span class="tech-tag frontend">Element Plus</span>
          <span class="tech-tag backend">Spring Boot</span>
          <span class="tech-tag backend">MyBatis-Plus</span>
          <span class="tech-tag db">MySQL</span>
          <span class="tech-tag db">Redis</span>
          <span class="tech-tag ai">AI大模型</span>
        </div>
      </div>
    </div>

    <!-- 功能模块展示 -->
    <div class="features-grid">
      <div 
        v-for="(feature, index) in features" 
        :key="index"
        class="feature-card"
        @click="navigateTo(feature.path)"
      >
        <div class="feature-icon" :style="{ background: feature.color }">
          <i :class="feature.icon"></i>
        </div>
        <div class="feature-content">
          <h4>{{ feature.title }}</h4>
          <p>{{ feature.desc }}</p>
        </div>
        <div class="feature-arrow">
          <i class="el-icon-arrow-right"></i>
        </div>
      </div>
    </div>

    <!-- 底部版本信息 -->
    <div class="footer-info">
      <span>系统版本 v1.0.0</span>
      <span class="divider">|</span>
      <span>基于 Spring Boot + Vue 3 构建</span>
      <span class="divider">|</span>
      <span>© 2025 EasyApplyResume</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { formatDateTime } from '@/utils'

const router = useRouter()
const authStore = useAuthStore()
const currentUser = computed(() => authStore.user)

// 当前日期
const currentDate = computed(() => {
  const now = new Date()
  return formatDateTime(now, 'YYYY年MM月DD日')
})

// 统计数据
const stats = ref({
  totalTemplates: 156,
  totalUsers: 2850,
  totalRecruitments: 423
})

// 功能模块
const features = ref([
  {
    icon: 'el-icon-document-copy',
    title: '简历模板管理',
    desc: '管理和维护平台简历模板，支持多行业分类',
    path: '/admin/resume/template',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    icon: 'el-icon-suitcase',
    title: '招聘信息管理',
    desc: '发布和管理招聘岗位信息，对接企业需求',
    path: '/admin/recruitment/information',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    icon: 'el-icon-user-solid',
    title: '用户系统管理',
    desc: '管理员、角色、权限的统一管理与配置',
    path: '/admin/user/admin',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    icon: 'el-icon-cpu',
    title: 'AI智能助手',
    desc: '集成AI大模型，提供智能问答与工具调用',
    path: '/admin/ai/chat',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  },
  {
    icon: 'el-icon-chat-line-square',
    title: '反馈中心',
    desc: '处理用户反馈和建议，持续优化产品体验',
    path: '/admin/feedback/management',
    color: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
  },
  {
    icon: 'el-icon-document',
    title: 'API文档中心',
    desc: '查看平台API接口文档，支持开发联调',
    path: '/admin/api-docs/internal',
    color: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)'
  }
])

// 导航到指定页面
const navigateTo = (path: string) => {
  router.push(path)
}

// 组件挂载时加载数据
onMounted(() => {
  loadDashboardData()
})

// 加载仪表板数据
const loadDashboardData = async () => {
  try {
    // 这里可以调用 API 获取实际数据
  } catch (error) {
    console.error('Failed to load dashboard data:', error)
  }
}
</script>

<style scoped lang="scss">
.dashboard {
  height: calc(100vh - 140px);
  display: flex;
  flex-direction: column;
  max-width: 1400px;
  margin: 0 auto;
  overflow: hidden;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 24px 32px;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
  flex-shrink: 0;
}

.welcome-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.welcome-text h2 {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 4px;
  line-height: 1.2;
}

.welcome-text p {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

.welcome-stats {
  display: flex;
  gap: 32px;
}

.quick-stat {
  text-align: center;
}

.stat-number {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 2px;
}

.stat-label {
  font-size: 13px;
  opacity: 0.8;
}

.welcome-image {
  position: relative;
}

.animated-graphic {
  width: 100px;
  height: 100px;
  position: relative;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 30px;
  height: 30px;
  top: 0;
  left: 35px;
  animation-delay: 0s;
}

.circle-2 {
  width: 50px;
  height: 50px;
  top: 20px;
  left: 25px;
  animation-delay: 2s;
}

.circle-3 {
  width: 70px;
  height: 70px;
  top: 40px;
  left: 15px;
  animation-delay: 4s;
}

// 系统介绍区域
.intro-section {
  display: flex;
  gap: 24px;
  margin: 20px 0;
  flex-shrink: 0;
}

.intro-main {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 20px 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.intro-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.intro-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.intro-title h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px 0;
}

.intro-title p {
  font-size: 13px;
  color: #6b7280;
  margin: 0;
}

.intro-desc {
  font-size: 14px;
  color: #4b5563;
  line-height: 1.7;
}

.tech-stack {
  width: 280px;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.tech-stack h4 {
  font-size: 15px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 12px 0;
}

.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tech-tag {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;

  &.frontend {
    background: #dbeafe;
    color: #2563eb;
  }

  &.backend {
    background: #dcfce7;
    color: #16a34a;
  }

  &.db {
    background: #fef3c7;
    color: #d97706;
  }

  &.ai {
    background: #f3e8ff;
    color: #9333ea;
  }
}

// 功能模块网格
.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  flex: 1;
  min-height: 0;
}

.feature-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 16px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);

    .feature-arrow {
      opacity: 1;
      transform: translateX(0);
    }
  }
}

.feature-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 22px;
  flex-shrink: 0;
}

.feature-content {
  flex: 1;
  min-width: 0;

  h4 {
    font-size: 15px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 4px 0;
  }

  p {
    font-size: 12px;
    color: #6b7280;
    margin: 0;
    line-height: 1.5;
  }
}

.feature-arrow {
  color: #9ca3af;
  opacity: 0;
  transform: translateX(-8px);
  transition: all 0.3s ease;
}

// 底部信息
.footer-info {
  text-align: center;
  padding: 16px 0 8px 0;
  font-size: 12px;
  color: #9ca3af;
  flex-shrink: 0;

  .divider {
    margin: 0 12px;
    color: #e5e7eb;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .intro-section {
    flex-direction: column;
  }

  .tech-stack {
    width: 100%;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .welcome-card {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }
}
</style>
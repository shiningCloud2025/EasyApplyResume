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

    <!-- 统计卡片网格 -->
    <div class="stats-grid">
      <div 
        v-for="(stat, index) in statistics" 
        :key="index"
        class="stat-card"
      >
        <div class="stat-icon" :style="{ background: stat.color }">
          <i :class="stat.icon"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
          <div class="stat-trend" :class="stat.trend">
            <i :class="stat.trendIcon"></i>
            {{ stat.change }}
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content-grid">
      <!-- 左侧内容 -->
      <div class="left-content">
        <!-- 快速入口 -->
        <el-card class="quick-access-card">
          <template #header>
            <div class="card-header">
              <span>快速入口</span>
              <el-button type="text" size="small">自定义</el-button>
            </div>
          </template>
          <div class="quick-access-grid">
            <div 
              v-for="(item, index) in quickAccess" 
              :key="index"
              class="quick-access-item"
              @click="navigateTo(item.path)"
            >
              <div class="quick-access-icon" :style="{ background: item.color }">
                <i :class="item.icon"></i>
              </div>
              <span>{{ item.label }}</span>
            </div>
          </div>
        </el-card>

        <!-- 最近操作 -->
        <el-card class="recent-activities-card">
          <template #header>
            <div class="card-header">
              <span>最近操作</span>
              <el-button type="text" size="small">查看全部</el-button>
            </div>
          </template>
          <div class="activity-list">
            <div 
              v-for="(activity, index) in recentActivities" 
              :key="index"
              class="activity-item"
            >
              <div class="activity-avatar">
                <el-avatar :size="32" :src="activity.avatar" />
              </div>
              <div class="activity-content">
                <div class="activity-text">{{ activity.text }}</div>
                <div class="activity-time">{{ formatRelativeTime(activity.time) }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧内容 -->
      <div class="right-content">
        <!-- 系统状态 -->
        <el-card class="system-status-card">
          <template #header>
            <div class="card-header">
              <span>系统状态</span>
              <div class="status-indicator active"></div>
            </div>
          </template>
          <div class="status-list">
            <div 
              v-for="(status, index) in systemStatus" 
              :key="index"
              class="status-item"
            >
              <div class="status-item-label">{{ status.label }}</div>
              <div 
                class="status-item-value" 
                :class="status.status"
              >
                {{ status.value }}
              </div>
              <div class="status-item-bar">
                <div 
                  class="status-bar-fill" 
                  :style="{ width: status.percentage + '%', background: status.color }"
                ></div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 待办事项 -->
        <el-card class="todo-card">
          <template #header>
            <div class="card-header">
              <span>待办事项</span>
              <el-badge :value="pendingTasks.length" :max="99">
                <el-button type="text" size="small">管理</el-button>
              </el-badge>
            </div>
          </template>
          <div class="todo-list">
            <div 
              v-for="(task, index) in pendingTasks.slice(0, 5)" 
              :key="index"
              class="todo-item"
            >
              <div class="todo-checkbox">
                <el-checkbox />
              </div>
              <div class="todo-content">
                <div class="todo-text">{{ task.text }}</div>
                <div class="todo-deadline">{{ formatDate(task.deadline) }}</div>
              </div>
              <div class="todo-priority" :class="task.priority">
                {{ task.priority }}
              </div>
            </div>
          </div>
        </el-card>

        <!-- 快捷通知 -->
        <div class="notification-card">
          <div class="notification-header">
            <span>系统通知</span>
            <el-button type="text" size="small">全部</el-button>
          </div>
          <div class="notification-list">
            <div 
              v-for="(notification, index) in notifications.slice(0, 3)" 
              :key="index"
              class="notification-item"
            >
              <div class="notification-icon" :style="{ backgroundColor: notification.color }">
                <i :class="notification.icon"></i>
              </div>
              <div class="notification-content">
                <div class="notification-title">{{ notification.title }}</div>
                <div class="notification-time">{{ formatRelativeTime(notification.time) }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部图表区域 -->
    <div class="chart-section">
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>一周活跃度统计</span>
            <el-radio-group v-model="chartType" size="small">
              <el-radio-button label="week">本周</el-radio-button>
              <el-radio-button label="month">本月</el-radio-button>
            </el-radio-group>
          </div>
        </template>
        <div class="chart-container">
          <div class="chart-placeholder">
            <div class="chart-bars">
              <div 
                v-for="(bar, index) in weekData" 
                :key="index"
                class="chart-bar"
                :style="{ height: bar.height + '%' }"
              >
                <div class="chart-bar-label">{{ bar.day }}</div>
                <div class="chart-bar-value">{{ bar.value }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '@/store/auth'
import { formatDateTime, formatRelativeTime, formatDate } from '@/utils'

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

// 统计卡片数据
const statistics = ref([
  {
    icon: 'el-icon-document',
    label: '简历模板',
    value: '156',
    change: '+12%',
    trend: 'up',
    trendIcon: 'el-icon-top',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    icon: 'el-icon-user',
    label: '活跃用户',
    value: '2.8K',
    change: '+8.5%',
    trend: 'up',
    trendIcon: 'el-icon-top',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    icon: 'el-icon-data-line',
    label: '投递次数',
    value: '1.2K',
    change: '+15.3%',
    trend: 'up',
    trendIcon: 'el-icon-top',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    icon: 'el-icon-s-promotion',
    label: '转化率',
    value: '68.5%',
    change: '-2.1%',
    trend: 'down',
    trendIcon: 'el-icon-bottom',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  }
])

// 快速入口
const quickAccess = ref([
  { label: '管理员管理', path: '/admin/user/admin', icon: 'el-icon-user-solid', color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { label: '简历模板', path: '/admin/resume/template', icon: 'el-icon-document-copy', color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
  { label: '招聘信息', path: '/admin/recruitment/information', icon: 'el-icon-suitcase', color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
  { label: '系统反馈', path: '/admin/feedback/management', icon: 'el-icon-chat-line-square', color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' },
  { label: 'AI助手', path: '/admin/ai/chat', icon: 'el-icon-cpu', color: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' },
  { label: '数据统计', path: '/admin/api-docs', icon: 'el-icon-data-analysis', color: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)' }
])

// 最近操作
const recentActivities = ref([
  {
    text: '管理员张三新增了简历模板"互联网产品经理"',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    time: '2025-12-07T10:30:00'
  },
  {
    text: '系统自动处理了15条用户反馈',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    time: '2025-12-07T09:45:00'
  },
  {
    text: '角色"内容管理员"权限已更新',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    time: '2025-12-07T09:15:00'
  }
])

// 系统状态
const systemStatus = ref([
  {
    label: 'CPU使用率',
    value: '45%',
    percentage: 45,
    status: 'normal',
    color: '#22c55e'
  },
  {
    label: '内存使用',
    value: '67%',
    percentage: 67,
    status: 'normal',
    color: '#f59e0b'
  },
  {
    label: '磁盘空间',
    value: '32%',
    percentage: 32,
    status: 'good',
    color: '#22c55e'
  },
  {
    label: '网络延迟',
    value: '12ms',
    percentage: 20,
    status: 'excellent',
    color: '#22c55e'
  }
])

// 待办事项
const pendingTasks = ref([
  {
    text: '审核3条新的求职攻略文章',
    deadline: '2025-12-08T18:00:00',
    priority: 'high'
  },
  {
    text: '更新管理员权限配置',
    deadline: '2025-12-09T12:00:00',
    priority: 'medium'
  },
  {
    text: '完善API文档',
    deadline: '2025-12-10T17:00:00',
    priority: 'low'
  }
])

// 通知
const notifications = ref([
  {
    title: '系统版本更新至 v1.7.0',
    icon: 'el-icon-success',
    color: '#22c55e',
    time: '2025-12-07T10:00:00'
  },
  {
    title: '数据库备份完成',
    icon: 'el-icon-time',
    color: '#3b82f6',
    time: '2025-12-07T08:00:00'
  },
  {
    title: '新的用户反馈待处理',
    icon: 'el-icon-warning',
    color: '#f59e0b',
    time: '2025-12-06T22:30:00'
  }
])

// 图表类型
const chartType = ref('week')

// 周数据
const weekData = ref([
  { day: '周一', value: 120, height: 60 },
  { day: '周二', value: 180, height: 90 },
  { day: '周三', value: 150, height: 75 },
  { day: '周四', value: 200, height: 100 },
  { day: '周五', value: 170, height: 85 },
  { day: '周六', value: 140, height: 70 },
  { day: '周日', value: 160, height: 80 }
])

// 导航到指定页面
const navigateTo = (path: string) => {
  // 这里可以使用 vue-router 进行导航
  console.log('Navigate to:', path)
}

// 组件挂载时加载数据
onMounted(() => {
  loadDashboardData()
})

// 加载仪表板数据
const loadDashboardData = async () => {
  try {
    // 这里可以调用 API 获取实际数据
    // const response = await dashboardApi.getDashboardStats()
    // stats.value = response.data
  } catch (error) {
    console.error('Failed to load dashboard data:', error)
  }
}
</script>

<style scoped lang="scss">
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 32px;
  color: white;
  margin-bottom: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
}

.welcome-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.welcome-text h2 {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
  line-height: 1.2;
}

.welcome-text p {
  font-size: 16px;
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
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.8;
}

.welcome-image {
  position: relative;
}

.animated-graphic {
  width: 120px;
  height: 120px;
  position: relative;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 40px;
  height: 40px;
  top: 0;
  left: 40px;
  animation-delay: 0s;
}

.circle-2 {
  width: 60px;
  height: 60px;
  top: 30px;
  left: 30px;
  animation-delay: 2s;
}

.circle-3 {
  width: 80px;
  height: 80px;
  top: 60px;
  left: 20px;
  animation-delay: 4s;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 8px;
}

.stat-trend {
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4px;

  &.up {
    color: #22c55e;
  }

  &.down {
    color: #ef4444;
  }
}

.main-content-grid {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 24px;
  margin-bottom: 32px;
}

.left-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.right-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.quick-access-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.quick-access-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.quick-access-item:hover {
  background-color: #f9fafb;
  transform: translateY(-2px);
}

.quick-access-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.quick-access-item span {
  font-size: 12px;
  color: #4b5563;
  text-align: center;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.activity-item:hover {
  background-color: #f9fafb;
}

.activity-content {
  flex: 1;
}

.activity-text {
  font-size: 14px;
  color: #374151;
  margin-bottom: 4px;
}

.activity-time {
  font-size: 12px;
  color: #9ca3af;
}

.system-status-card {
  .status-indicator {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    background: #22c55e;
    animation: pulse 2s infinite;
  }
}

.status-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.status-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.status-item-label {
  font-size: 14px;
  color: #6b7280;
}

.status-item-value {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;

  &.good { color: #22c55e; }
  &.normal { color: #f59e0b; }
  &.warning { color: #ef4444; }
  &.excellent { color: #22c55e; }
}

.status-item-bar {
  height: 4px;
  background: #e5e7eb;
  border-radius: 2px;
  overflow: hidden;
}

.status-bar-fill {
  height: 100%;
  transition: width 0.3s ease;
}

.todo-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.todo-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  background: #f9fafb;
}

.todo-content {
  flex: 1;
}

.todo-text {
  font-size: 14px;
  color: #374151;
  margin-bottom: 4px;
}

.todo-deadline {
  font-size: 12px;
  color: #9ca3af;
}

.todo-priority {
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 4px;
  font-weight: 600;
  text-transform: uppercase;

  &.high {
    background: #fef2f2;
    color: #ef4444;
  }

  &.medium {
    background: #fef3c7;
    color: #f59e0b;
  }

  &.low {
    background: #f0fdf4;
    color: #22c55e;
  }
}

.notification-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  font-weight: 600;
}

.notification-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notification-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px;
  border-radius: 6px;
}

.notification-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 14px;
}

.notification-content {
  flex: 1;
}

.notification-title {
  font-size: 13px;
  color: #374151;
  margin-bottom: 2px;
}

.notification-time {
  font-size: 11px;
  color: #9ca3af;
}

.chart-section {
  margin-bottom: 32px;
}

.chart-container {
  height: 300px;
}

.chart-placeholder {
  height: 100%;
  display: flex;
  align-items: flex-end;
  padding: 20px;
}

.chart-bars {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}

.chart-bar {
  width: 60px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border-radius: 8px 8px 0 0;
  position: relative;
  transition: all 0.3s ease;
  cursor: pointer;
  
  &:hover {
    opacity: 0.8;
    transform: translateY(-4px);
  }
}

.chart-bar-label {
  position: absolute;
  bottom: -25px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 12px;
  color: #6b7280;
}

.chart-bar-value {
  position: absolute;
  top: -25px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 12px;
  font-weight: 600;
  color: #374151;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
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
@media (max-width: 1024px) {
  .main-content-grid {
    grid-template-columns: 1fr;
  }
  
  .welcome-stats {
    gap: 24px;
  }
}

@media (max-width: 768px) {
  .welcome-card {
    flex-direction: column;
    text-align: center;
    gap: 24px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .quick-access-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .chart-bars {
    flex-direction: column;
    align-items: center;
    gap: 20px;
  }
  
  .chart-bar {
    width: 80px;
    height: 40px !important;
  }
}

@media (max-width: 480px) {
  .welcome-text h2 {
    font-size: 24px;
  }
  
  .quick-access-grid {
    grid-template-columns: 1fr;
  }
}
</style>
<template>
  <div class="statistics-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>{{ isAdminType ? '管理端数据统计' : '用户端数据统计' }}</h2>
    </div>

    <!-- 端类型切换 -->
    <div class="type-tabs">
      <el-radio-group v-model="currentType" @change="handleTypeChange">
        <el-radio-button value="admin">管理端统计</el-radio-button>
        <el-radio-button value="user">用户端统计</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 统计概览卡片 -->
    <div class="stats-overview">
      <div class="stat-card">
        <div class="stat-icon total">
          <el-icon><DataLine /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ formatNumber(totalVisit) }}</div>
          <div class="stat-label">累计访问量</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon today">
          <el-icon><TrendCharts /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ formatNumber(todayIncrease) }}</div>
          <div class="stat-label">今日新增</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon avg">
          <el-icon><Histogram /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ formatNumber(avgVisit) }}</div>
          <div class="stat-label">日均访问</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon users">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ formatNumber(totalUsers) }}</div>
          <div class="stat-label">{{ isAdminType ? '管理员总数' : '用户总数' }}</div>
        </div>
      </div>
    </div>

    <!-- 日期范围选择 -->
    <div class="filter-section">
      <span class="filter-label">时间范围：</span>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        :shortcuts="dateShortcuts"
        @change="loadChartData"
      />
      <el-button type="primary" @click="loadChartData" :loading="loading">
        查询
      </el-button>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <!-- 访问量趋势 -->
      <div class="chart-container">
        <h3>每日访问量趋势</h3>
        <div class="chart-wrapper" v-loading="loading">
          <div v-if="visitData.length === 0" class="empty-chart">
            <el-empty description="暂无数据" />
          </div>
          <div v-else class="simple-chart">
            <div class="chart-bars">
              <div
                v-for="(item, index) in visitData"
                :key="index"
                class="bar-item"
                :title="`${item.date}: ${item.visitNum}次访问`"
              >
                <div
                  class="bar"
                  :style="{ height: getBarHeight(item.visitNum) + '%' }"
                ></div>
                <div class="bar-label">{{ item.date.slice(5) }}</div>
              </div>
            </div>
            <div class="chart-legend">
              <div class="legend-item">
                <span class="legend-color"></span>
                <span>访问量</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 用户/管理员数量趋势 -->
      <div class="chart-container">
        <h3>{{ isAdminType ? '管理员数量' : '用户数量' }}趋势</h3>
        <div class="chart-wrapper" v-loading="loading">
          <div v-if="userNumData.length === 0" class="empty-chart">
            <el-empty description="暂无数据" />
          </div>
          <div v-else class="simple-chart">
            <div class="chart-bars user-bars">
              <div
                v-for="(item, index) in userNumData"
                :key="index"
                class="bar-item"
                :title="`${item.date}: ${item.num}人`"
              >
                <div
                  class="bar user-bar"
                  :style="{ height: getUserBarHeight(item.num) + '%' }"
                ></div>
                <div class="bar-label">{{ item.date.slice(5) }}</div>
              </div>
            </div>
            <div class="chart-legend">
              <div class="legend-item">
                <span class="legend-color user-color"></span>
                <span>{{ isAdminType ? '管理员数' : '用户数' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <h3>详细数据</h3>
      <el-table :data="visitData" v-loading="loading" stripe>
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="visitNum" label="访问量" width="120" />
        <el-table-column label="较前日">
          <template #default="{ row, $index }">
            <span v-if="$index === visitData.length - 1" class="text-gray">-</span>
            <span v-else :class="getDiffClass(row.visitNum, visitData[$index + 1]?.visitNum)">
              {{ getDiffText(row.visitNum, visitData[$index + 1]?.visitNum) }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { DataLine, TrendCharts, Histogram, User } from '@element-plus/icons-vue'
import { adminStatisticsApi, userStatisticsApi } from '@/api'

const route = useRoute()
const router = useRouter()

const currentType = ref<'admin' | 'user'>('admin')
const isAdminType = computed(() => currentType.value === 'admin')
const api = computed(() => isAdminType.value ? adminStatisticsApi : userStatisticsApi)

const loading = ref(false)
const totalVisit = ref(0)
const todayIncrease = ref(0)
const avgVisit = ref(0)
const totalUsers = ref(0)

const dateRange = ref<[string, string] | null>(null)
const visitData = ref<Array<{ date: string; visitNum: number }>>([])
const userNumData = ref<Array<{ date: string; num: number }>>([])

const dateShortcuts = [
  {
    text: '最近7天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 6 * 24 * 60 * 60 * 1000)
      return [start, end]
    }
  },
  {
    text: '最近30天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 29 * 24 * 60 * 60 * 1000)
      return [start, end]
    }
  },
  {
    text: '本月',
    value: () => {
      const end = new Date()
      const start = new Date(end.getFullYear(), end.getMonth(), 1)
      return [start, end]
    }
  }
]

const formatNumber = (num: number) => {
  if (num >= 10000) return (num / 10000).toFixed(1) + 'w'
  return num.toLocaleString()
}

const getBarHeight = (value: number) => {
  const max = Math.max(...visitData.value.map(d => d.visitNum), 1)
  return Math.max((value / max) * 100, 5)
}

const getUserBarHeight = (value: number) => {
  const max = Math.max(...userNumData.value.map(d => d.num), 1)
  return Math.max((value / max) * 100, 5)
}

const getDiffClass = (current: number, prev: number) => {
  if (!prev) return ''
  return current > prev ? 'diff-up' : current < prev ? 'diff-down' : ''
}

const getDiffText = (current: number, prev: number) => {
  if (!prev) return '-'
  const diff = current - prev
  return diff >= 0 ? `+${diff}` : `${diff}`
}

const handleTypeChange = (type: 'admin' | 'user') => {
  router.replace({ query: { type } })
  loadData()
}

const loadData = async () => {
  loading.value = true
  try {
    const [total, today] = await Promise.all([
      api.value.getTotalVisitNum().catch(() => 0),
      api.value.getTodayIncreaseVisitNum().catch(() => 0)
    ])
    totalVisit.value = total || 0
    todayIncrease.value = today || 0

    if (dateRange.value) {
      await loadChartData()
    } else {
      // 默认加载最近7天
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 6 * 24 * 60 * 60 * 1000)
      dateRange.value = [formatDate(start), formatDate(end)]
      await loadChartData()
    }
  } catch (error) {
    console.error('加载统计数据失败', error)
  } finally {
    loading.value = false
  }
}

const formatDate = (date: Date) => {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

const loadChartData = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) return

  loading.value = true
  try {
    const [fromDate, endDate] = dateRange.value

    // 获取访问量数据
    const visitRes = await api.value.getVisitNumByDateRange(fromDate, endDate).catch(() => [])
    visitData.value = (visitRes || []).map((item: any) => ({
      date: item.time || item.date,
      visitNum: item.visitNum || item.num || 0
    })).reverse()

    // 计算日均
    if (visitData.value.length > 0) {
      const sum = visitData.value.reduce((acc, cur) => acc + cur.visitNum, 0)
      avgVisit.value = Math.round(sum / visitData.value.length)
    }

    // 获取用户/管理员数量数据
    const userNumApi = isAdminType.value ? 
      api.value.getAdminNumByDateRange : 
      userStatisticsApi.getUserNumByDateRange
    
    const userRes = await userNumApi(fromDate, endDate).catch(() => [])
    userNumData.value = (userRes || []).map((item: any) => ({
      date: item.time || item.date,
      num: item.adminNum || item.userNum || item.num || 0
    })).reverse()

    // 获取最新用户/管理员总数
    if (userNumData.value.length > 0) {
      totalUsers.value = userNumData.value[0].num
    }

  } catch (error) {
    console.error('加载图表数据失败', error)
  } finally {
    loading.value = false
  }
}

watch(() => route.query.type, (type) => {
  if (type === 'admin' || type === 'user') {
    currentType.value = type
    loadData()
  }
}, { immediate: true })

onMounted(() => {
  const type = route.query.type as string
  if (type === 'admin' || type === 'user') {
    currentType.value = type
  }
  loadData()
})
</script>

<style scoped lang="scss">
.statistics-page {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.page-header {
  margin-bottom: 20px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0;
  }
}

.type-tabs {
  margin-bottom: 24px;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: #f9fafb;
  border-radius: 10px;
  padding: 20px;
  display: flex;
  gap: 16px;
  align-items: center;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: white;

  &.total { background: linear-gradient(135deg, #10b981, #059669); }
  &.today { background: linear-gradient(135deg, #3b82f6, #1d4ed8); }
  &.avg { background: linear-gradient(135deg, #f59e0b, #d97706); }
  &.users { background: linear-gradient(135deg, #8b5cf6, #6d28d9); }
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
}

.stat-label {
  font-size: 13px;
  color: #6b7280;
}

.filter-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
  padding: 16px;
  background: #f9fafb;
  border-radius: 8px;
}

.filter-label {
  font-size: 14px;
  color: #374151;
}

.charts-section {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.chart-container {
  background: #f9fafb;
  border-radius: 10px;
  padding: 20px;

  h3 {
    font-size: 15px;
    font-weight: 600;
    color: #374151;
    margin: 0 0 16px 0;
  }
}

.chart-wrapper {
  min-height: 200px;
}

.empty-chart {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
}

.simple-chart {
  .chart-bars {
    display: flex;
    gap: 8px;
    align-items: flex-end;
    height: 180px;
    padding-bottom: 24px;
  }

  .bar-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;
    min-width: 20px;
  }

  .bar {
    width: 100%;
    max-width: 30px;
    background: linear-gradient(to top, #10b981, #34d399);
    border-radius: 4px 4px 0 0;
    transition: height 0.3s ease;
  }

  .user-bar {
    background: linear-gradient(to top, #3b82f6, #60a5fa);
  }

  .bar-label {
    position: absolute;
    bottom: 0;
    font-size: 10px;
    color: #9ca3af;
    transform: rotate(-45deg);
    white-space: nowrap;
  }
}

.chart-legend {
  display: flex;
  justify-content: center;
  margin-top: 16px;

  .legend-item {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    color: #6b7280;
  }

  .legend-color {
    width: 12px;
    height: 12px;
    border-radius: 2px;
    background: linear-gradient(135deg, #10b981, #34d399);

    &.user-color {
      background: linear-gradient(135deg, #3b82f6, #60a5fa);
    }
  }
}

.table-section {
  h3 {
    font-size: 15px;
    font-weight: 600;
    color: #374151;
    margin: 0 0 16px 0;
  }
}

.text-gray { color: #9ca3af; }
.diff-up { color: #10b981; }
.diff-down { color: #ef4444; }

@media (max-width: 1024px) {
  .stats-overview {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-section {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .stats-overview {
    grid-template-columns: 1fr;
  }
}
</style>

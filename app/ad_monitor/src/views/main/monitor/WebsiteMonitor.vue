<template>
  <div class="website-monitor">
    <div class="page-header">
      <h2>{{ isAdmin ? '管理端' : '用户端' }}网站管理</h2>
      <el-tag :type="isAdmin ? 'success' : 'primary'">{{ isAdmin ? '管理端' : '用户端' }}</el-tag>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-icon today"><el-icon><TrendCharts /></el-icon></div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.todayVisit }}</div>
          <div class="stat-label">今日访问量</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon total"><el-icon><DataLine /></el-icon></div>
        <div class="stat-info">
          <div class="stat-value">{{ formatNum(stats.totalVisit) }}</div>
          <div class="stat-label">总访问量</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon users"><el-icon><User /></el-icon></div>
        <div class="stat-info">
          <div class="stat-value">{{ formatNum(stats.totalUsers) }}</div>
          <div class="stat-label">{{ isAdmin ? '管理员总数' : '用户总数' }}</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon increase"><el-icon><Top /></el-icon></div>
        <div class="stat-info">
          <div class="stat-value">+{{ stats.todayIncrease }}</div>
          <div class="stat-label">今日访问增长</div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <!-- 时间选择 -->
      <div class="chart-filter">
        <span>时间范围：</span>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          :shortcuts="shortcuts"
          @change="loadChartData"
        />
        <el-button type="primary" @click="loadChartData" :loading="chartLoading">查询</el-button>
      </div>

      <div class="charts-grid">
        <!-- 访问量折线图 -->
        <div class="chart-card">
          <h3>访问量趋势</h3>
          <div class="chart-wrapper" v-loading="chartLoading">
            <div v-if="visitData.length === 0" class="empty-chart">
              <el-empty description="暂无数据" :image-size="80" />
            </div>
            <div v-else class="simple-line-chart">
              <div class="chart-area">
                <div 
                  v-for="(item, i) in visitData" 
                  :key="i" 
                  class="bar-wrapper"
                  :title="`${item.date}: ${item.value}次`"
                >
                  <div class="bar" :style="{ height: getBarHeight(item.value, visitData) + '%' }"></div>
                  <span class="bar-label">{{ item.date.slice(5) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 用户/管理员数量折线图 -->
        <div class="chart-card">
          <h3>{{ isAdmin ? '管理员' : '用户' }}数量趋势</h3>
          <div class="chart-wrapper" v-loading="chartLoading">
            <div v-if="userNumData.length === 0" class="empty-chart">
              <el-empty description="暂无数据" :image-size="80" />
            </div>
            <div v-else class="simple-line-chart">
              <div class="chart-area user-chart">
                <div 
                  v-for="(item, i) in userNumData" 
                  :key="i" 
                  class="bar-wrapper"
                  :title="`${item.date}: ${item.value}人`"
                >
                  <div class="bar user-bar" :style="{ height: getBarHeight(item.value, userNumData) + '%' }"></div>
                  <span class="bar-label">{{ item.date.slice(5) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { TrendCharts, DataLine, User, Top } from '@element-plus/icons-vue'
import { adminStatisticsApi, userStatisticsApi } from '@/api'

const route = useRoute()

const isAdmin = computed(() => route.path.includes('admin-monitor'))
const api = computed(() => isAdmin.value ? adminStatisticsApi : userStatisticsApi)

const stats = reactive({
  todayVisit: 0,
  totalVisit: 0,
  totalUsers: 0,
  todayIncrease: 0
})

const chartLoading = ref(false)
const dateRange = ref<[string, string] | null>(null)
const visitData = ref<Array<{ date: string; value: number }>>([])
const userNumData = ref<Array<{ date: string; value: number }>>([])

const shortcuts = [
  { text: '最近7天', value: () => [getDate(-6), getDate(0)] },
  { text: '最近30天', value: () => [getDate(-29), getDate(0)] },
  { text: '本月', value: () => [new Date(new Date().getFullYear(), new Date().getMonth(), 1), new Date()] }
]

const getDate = (offset: number) => {
  const d = new Date()
  d.setDate(d.getDate() + offset)
  return d
}

const formatDate = (d: Date) => {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const formatNum = (n: number) => {
  if (n >= 10000) return (n / 10000).toFixed(1) + 'w'
  return n.toLocaleString()
}

const getBarHeight = (value: number, data: Array<{ value: number }>) => {
  const max = Math.max(...data.map(d => d.value), 1)
  return Math.max((value / max) * 100, 5)
}

const loadStats = async () => {
  try {
    const [total, today] = await Promise.all([
      api.value.getTotalVisitNum().catch(() => 0),
      api.value.getTodayIncreaseVisitNum().catch(() => 0)
    ])
    stats.totalVisit = total || 0
    stats.todayIncrease = today || 0
    stats.todayVisit = today || 0
  } catch (e) {
    console.error('加载统计失败', e)
  }
}

const loadChartData = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) return

  chartLoading.value = true
  try {
    const [from, to] = dateRange.value

    // 访问量数据
    const visitRes = await api.value.getVisitNumByDateRange(from, to).catch(() => [])
    visitData.value = (visitRes || []).map((item: any) => ({
      date: item.time || item.date,
      value: item.visitNum || item.num || 0
    })).reverse()

    // 用户/管理员数量数据
    const userNumApi = isAdmin.value ? api.value.getAdminNumByDateRange : userStatisticsApi.getUserNumByDateRange
    const userRes = await userNumApi(from, to).catch(() => [])
    userNumData.value = (userRes || []).map((item: any) => ({
      date: item.time || item.date,
      value: item.adminNum || item.userNum || item.num || 0
    })).reverse()

    // 获取最新用户总数
    if (userNumData.value.length > 0) {
      stats.totalUsers = userNumData.value[0].value
    }
  } catch (e) {
    console.error('加载图表数据失败', e)
  } finally {
    chartLoading.value = false
  }
}

const init = () => {
  // 默认查询最近7天
  dateRange.value = [formatDate(getDate(-6)), formatDate(getDate(0))]
  loadStats()
  loadChartData()
}

watch(() => route.path, init)
onMounted(init)
</script>

<style scoped lang="scss">
.website-monitor {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0;
  }
}

.stats-cards {
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
  color: white;
  font-size: 22px;

  &.today { background: linear-gradient(135deg, #3b82f6, #1d4ed8); }
  &.total { background: linear-gradient(135deg, #10b981, #059669); }
  &.users { background: linear-gradient(135deg, #8b5cf6, #6d28d9); }
  &.increase { background: linear-gradient(135deg, #f59e0b, #d97706); }
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

.charts-section {
  background: #f9fafb;
  border-radius: 10px;
  padding: 20px;
}

.chart-filter {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #374151;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.chart-card {
  background: white;
  border-radius: 8px;
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

.simple-line-chart {
  .chart-area {
    display: flex;
    gap: 6px;
    align-items: flex-end;
    height: 180px;
    padding-bottom: 24px;
  }

  .bar-wrapper {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;
    min-width: 16px;
  }

  .bar {
    width: 100%;
    max-width: 24px;
    background: linear-gradient(to top, #10b981, #34d399);
    border-radius: 3px 3px 0 0;
    transition: height 0.3s;
  }

  .user-bar {
    background: linear-gradient(to top, #8b5cf6, #a78bfa);
  }

  .bar-label {
    position: absolute;
    bottom: 0;
    font-size: 9px;
    color: #9ca3af;
    transform: rotate(-45deg);
    white-space: nowrap;
  }
}

@media (max-width: 1024px) {
  .stats-cards { grid-template-columns: repeat(2, 1fr); }
  .charts-grid { grid-template-columns: 1fr; }
}

@media (max-width: 640px) {
  .stats-cards { grid-template-columns: 1fr; }
}
</style>

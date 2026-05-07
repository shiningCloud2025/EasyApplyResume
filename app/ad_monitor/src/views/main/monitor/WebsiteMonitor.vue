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
            <div v-else ref="visitChartRef" class="echarts-container"></div>
          </div>
        </div>

        <!-- 用户/管理员数量折线图 -->
        <div class="chart-card">
          <h3>{{ isAdmin ? '管理员' : '用户' }}数量趋势</h3>
          <div class="chart-wrapper" v-loading="chartLoading">
            <div v-if="userNumData.length === 0" class="empty-chart">
              <el-empty description="暂无数据" :image-size="80" />
            </div>
            <div v-else ref="userChartRef" class="echarts-container"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, nextTick, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { TrendCharts, DataLine, User, Top } from '@element-plus/icons-vue'
import { adminStatisticsApi, userStatisticsApi } from '@/api'
import * as echarts from 'echarts'

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

// ECharts 引用
const visitChartRef = ref<HTMLElement | null>(null)
const userChartRef = ref<HTMLElement | null>(null)
let visitChart: echarts.ECharts | null = null
let userChart: echarts.ECharts | null = null

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

// ECharts 配置生成
const getChartOption = (data: Array<{ date: string; value: number }>, color: string) => ({
  grid: { top: 20, right: 20, bottom: 30, left: 40 },
  xAxis: {
    type: 'category',
    data: data.map(d => d.date.slice(5)),
    axisLine: { lineStyle: { color: '#e5e7eb' } },
    axisLabel: { color: '#9ca3af', fontSize: 11 },
    axisTick: { show: false }
  },
  yAxis: {
    type: 'value',
    splitLine: { lineStyle: { color: '#f3f4f6' } },
    axisLabel: { color: '#9ca3af', fontSize: 11 }
  },
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(0,0,0,0.7)',
    borderWidth: 0,
    textStyle: { color: '#fff', fontSize: 12 },
    formatter: (params: any) => `${params[0].name}<br/>${params[0].value}`
  },
  series: [{
    type: 'line',
    data: data.map(d => d.value),
    smooth: true,
    symbol: 'circle',
    symbolSize: 6,
    lineStyle: { color, width: 2 },
    itemStyle: { color },
    areaStyle: {
      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: color + '30' },
        { offset: 1, color: color + '05' }
      ])
    }
  }]
})

// 更新图表
const updateCharts = () => {
  nextTick(() => {
    if (visitChartRef.value && visitData.value.length > 0) {
      if (!visitChart) {
        visitChart = echarts.init(visitChartRef.value)
      }
      visitChart.setOption(getChartOption(visitData.value, '#10b981'))
    }
    if (userChartRef.value && userNumData.value.length > 0) {
      if (!userChart) {
        userChart = echarts.init(userChartRef.value)
      }
      userChart.setOption(getChartOption(userNumData.value, '#8b5cf6'))
    }
  })
}

// 窗口大小变化时重绘
const handleResize = () => {
  visitChart?.resize()
  userChart?.resize()
}

// 生成日期数组
const generateDateRange = (from: string, to: string): string[] => {
  const dates: string[] = []
  const startDate = new Date(from)
  const endDate = new Date(to)
  const current = new Date(startDate)
  
  while (current <= endDate) {
    dates.push(formatDate(current))
    current.setDate(current.getDate() + 1)
  }
  return dates
}

const loadStats = async () => {
  try {
    // 今日日期
    const today = formatDate(new Date())
    console.log('📅 今日日期:', today)
    
    // 分开调用，方便调试
    let total = 0, todayIncrease = 0, todayVisit = 0, totalUsers = 0
    
    try {
      console.log('🔄 调用 getTotalVisitNum...')
      total = await api.value.getTotalVisitNum()
      console.log('✅ 总访问量:', total)
    } catch (e) {
      console.error('❌ getTotalVisitNum 失败:', e)
    }
    
    try {
      console.log('🔄 调用 getTodayIncreaseVisitNum...')
      todayIncrease = await api.value.getTodayIncreaseVisitNum()
      console.log('✅ 今日新增:', todayIncrease)
    } catch (e) {
      console.error('❌ getTodayIncreaseVisitNum 失败:', e)
    }
    
    try {
      console.log('🔄 调用 calculateDailyVisitNum, 参数:', today)
      todayVisit = await api.value.calculateDailyVisitNum(today)
      console.log('✅ 今日访问量:', todayVisit)
    } catch (e) {
      console.error('❌ calculateDailyVisitNum 失败:', e)
    }
    
    // 调用总用户/管理员数量接口
    try {
      console.log('🔄 调用总用户数量接口...')
      const getTotalNumApi = isAdmin.value ? api.value.getTotalAdminNum : api.value.getTotalUserNum
      totalUsers = await getTotalNumApi()
      console.log('✅ 总用户/管理员数量:', totalUsers)
    } catch (e) {
      console.error('❌ 获取总用户数量失败:', e)
    }
    
    stats.totalVisit = total || 0
    stats.todayIncrease = todayIncrease || 0
    stats.todayVisit = todayVisit || 0
    stats.totalUsers = totalUsers || 0
  } catch (e) {
    console.error('加载统计失败', e)
  }
}

const loadChartData = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) return

  chartLoading.value = true
  try {
    const [from, to] = dateRange.value
    const dateLabels = generateDateRange(from, to)

    // 访问量数据 - 后端返回 List<Integer>
    const visitRes = await api.value.getVisitNumByDateRange(from, to).catch(() => [])
    visitData.value = (visitRes || []).map((value: number, index: number) => ({
      date: dateLabels[index] || '',
      value: value || 0
    }))

    // 用户/管理员数量数据 - 后端返回 List<Integer>
    const userNumApi = isAdmin.value ? api.value.getAdminNumByDateRange : userStatisticsApi.getUserNumByDateRange
    const userRes = await userNumApi(from, to).catch(() => [])
    userNumData.value = (userRes || []).map((value: number, index: number) => ({
      date: dateLabels[index] || '',
      value: value || 0
    }))
    
    // 更新 ECharts
    updateCharts()
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
onMounted(() => {
  init()
  window.addEventListener('resize', handleResize)
})
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  visitChart?.dispose()
  userChart?.dispose()
})
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
  display: flex;
  flex-direction: column;
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

.echarts-container {
  width: 100%;
  height: 220px;
}

@media (max-width: 1024px) {
  .stats-cards { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 768px) {
  .website-monitor {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .chart-filter {
    flex-direction: column;
    align-items: stretch;
  }

  .chart-card {
    padding: 16px;
  }
}

@media (max-width: 640px) {
  .stats-cards { grid-template-columns: 1fr; }
}
</style>

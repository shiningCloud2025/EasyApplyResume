<template>
  <div class="monitor-page">
    <div class="page-header">
      <div class="header-left">
        <h2>设备监控</h2>
        <el-tag type="warning">实时监控</el-tag>
      </div>
      <el-button type="primary" @click="loadData" :loading="loading" :disabled="!canViewPage">
        <el-icon><Refresh /></el-icon>
        刷新数据
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input 
        v-model="searchName" 
        placeholder="搜索服务器名称" 
        clearable 
        style="width: 240px;"
        @keyup.enter="loadData"
      />
      <el-button type="primary" @click="loadData">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 服务器列表 -->
    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="serviceMachineId" label="ID" width="70" />
        <el-table-column prop="serviceMachineName" label="服务器名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="serviceMachineHost" label="外网IP/域名" min-width="150" show-overflow-tooltip />
        <el-table-column prop="serviceMachinePort" label="SSH端口" width="90" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canViewMonitor" type="primary" link size="small" @click="handleMonitor(row)">查看监控</el-button>
            <el-button v-if="canTestConnect" type="success" link size="small" @click="handleTestConnect(row)">测试连接</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <!-- 监控信息对话框 -->
    <el-dialog
      v-model="monitorDialogVisible"
      :title="`${monitorData.serviceMachineName} - 监控信息`"
      :width="monitorDialogWidth"
      :close-on-click-modal="false"
    >
      <div v-loading="monitorLoading" class="monitor-content">
        <div v-if="monitorData.online === false" class="offline-status">
          <el-result icon="error" title="服务器离线" sub-title="无法连接到服务器，请检查网络或服务器状态" />
        </div>
        <div v-else class="monitor-grid">
          <!-- CPU使用率 -->
          <div class="monitor-card">
            <div class="card-header">
              <el-icon class="card-icon cpu"><Cpu /></el-icon>
              <span>CPU使用率</span>
            </div>
            <div class="card-value">
              <el-progress 
                type="dashboard" 
                :percentage="monitorData.cpuUsage || 0" 
                :color="getProgressColor(monitorData.cpuUsage)"
                :stroke-width="10"
              >
                <template #default="{ percentage }">
                  <span class="percentage-value">{{ percentage.toFixed(1) }}%</span>
                </template>
              </el-progress>
            </div>
          </div>

          <!-- 内存使用 -->
          <div class="monitor-card">
            <div class="card-header">
              <el-icon class="card-icon memory"><Coin /></el-icon>
              <span>内存使用</span>
            </div>
            <div class="card-value">
              <el-progress 
                type="dashboard" 
                :percentage="monitorData.memoryUsage || 0" 
                :color="getProgressColor(monitorData.memoryUsage)"
                :stroke-width="10"
              >
                <template #default="{ percentage }">
                  <span class="percentage-value">{{ percentage.toFixed(1) }}%</span>
                </template>
              </el-progress>
            </div>
            <div class="card-info">
              {{ monitorData.memoryUsed || 0 }}MB / {{ monitorData.memoryTotal || 0 }}MB
            </div>
          </div>

          <!-- 硬盘使用 -->
          <div class="monitor-card">
            <div class="card-header">
              <el-icon class="card-icon disk"><FolderOpened /></el-icon>
              <span>硬盘使用</span>
            </div>
            <div class="card-value">
              <el-progress 
                type="dashboard" 
                :percentage="monitorData.diskUsage || 0" 
                :color="getProgressColor(monitorData.diskUsage)"
                :stroke-width="10"
              >
                <template #default="{ percentage }">
                  <span class="percentage-value">{{ percentage.toFixed(1) }}%</span>
                </template>
              </el-progress>
            </div>
            <div class="card-info">
              {{ monitorData.diskUsed || 0 }}GB / {{ monitorData.diskTotal || 0 }}GB
            </div>
          </div>

          <!-- 系统负载 -->
          <div class="monitor-card">
            <div class="card-header">
              <el-icon class="card-icon load"><TrendCharts /></el-icon>
              <span>系统负载</span>
            </div>
            <div class="card-value load-value">
              <span class="load-number">{{ (monitorData.loadAverage || 0).toFixed(2) }}</span>
            </div>
            <div class="card-info">1分钟平均负载</div>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="monitorDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="refreshMonitor" :loading="monitorLoading" :disabled="!canViewMonitor">刷新</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { Refresh, Cpu, Coin, FolderOpened, TrendCharts } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { serviceMachineMonitorApi } from '@/api'
import { useAuthStore, serviceMachineMonitorPermissions } from '@/store/auth'

const authStore = useAuthStore()
const canViewPage = computed(() => authStore.canAccessRoute(serviceMachineMonitorPermissions.getByPage))
const canViewMonitor = computed(() => authStore.canAccessRoute(serviceMachineMonitorPermissions.getMonitorInfo))
const canTestConnect = computed(() => authStore.canAccessRoute(serviceMachineMonitorPermissions.testConnect))

const loading = ref(false)
const tableData = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchName = ref('')

const monitorDialogVisible = ref(false)
const monitorLoading = ref(false)
const currentServer = ref<any>(null)
const monitorDialogWidth = computed(() => (window.innerWidth <= 768 ? '94%' : '700px'))

const monitorData = reactive({
  serviceMachineId: null as number | null,
  serviceMachineName: '',
  cpuUsage: 0,
  memoryTotal: 0,
  memoryUsed: 0,
  memoryUsage: 0,
  diskTotal: 0,
  diskUsed: 0,
  diskUsage: 0,
  loadAverage: 0,
  online: true
})

const getProgressColor = (percentage: number) => {
  if (percentage < 50) return '#67c23a'
  if (percentage < 80) return '#e6a23c'
  return '#f56c6c'
}

const loadData = async () => {
  if (!canViewPage.value) {
    tableData.value = []
    total.value = 0
    return
  }

  loading.value = true
  try {
    const query = searchName.value ? { serviceMachineName: searchName.value } : {}
    const res = await serviceMachineMonitorApi.getByPage(pageNum.value, pageSize.value, query)
    tableData.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) {
    console.error('加载失败', e)
    tableData.value = []
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchName.value = ''
  pageNum.value = 1
  loadData()
}

const handleTestConnect = async (row: any) => {
  if (!canTestConnect.value) {
    return
  }

  try {
    ElMessage.info('正在测试连接...')
    await serviceMachineMonitorApi.testConnect({
      serviceMachineHost: row.serviceMachineHost,
      serviceMachinePort: row.serviceMachinePort,
      serviceMachineUsername: row.serviceMachineUsername,
      serviceMachinePassword: row.serviceMachinePassword
    })
    ElMessage.success('连接成功！')
  } catch (e) {
    ElMessage.error('连接失败')
  }
}

const handleMonitor = async (row: any) => {
  if (!canViewMonitor.value) {
    return
  }

  currentServer.value = row
  monitorData.serviceMachineId = row.serviceMachineId
  monitorData.serviceMachineName = row.serviceMachineName
  monitorDialogVisible.value = true
  await fetchMonitorInfo()
}

const fetchMonitorInfo = async () => {
  if (!currentServer.value || !canViewMonitor.value) return

  monitorLoading.value = true
  try {
    const res = await serviceMachineMonitorApi.getMonitorInfo({
      serviceMachineId: currentServer.value.serviceMachineId,
      serviceMachineName: currentServer.value.serviceMachineName,
      serviceMachineHost: currentServer.value.serviceMachineHost,
      serviceMachinePort: currentServer.value.serviceMachinePort,
      serviceMachineUsername: currentServer.value.serviceMachineUsername,
      serviceMachinePassword: currentServer.value.serviceMachinePassword
    })

    monitorData.cpuUsage = res?.cpuUsage || 0
    monitorData.memoryTotal = res?.memoryTotal || 0
    monitorData.memoryUsed = res?.memoryUsed || 0
    monitorData.memoryUsage = res?.memoryUsage || 0
    monitorData.diskTotal = res?.diskTotal || 0
    monitorData.diskUsed = res?.diskUsed || 0
    monitorData.diskUsage = res?.diskUsage || 0
    monitorData.loadAverage = res?.loadAverage || 0
    monitorData.online = res?.online !== false
  } catch (e) {
    console.error('获取监控信息失败', e)
    monitorData.online = false
  } finally {
    monitorLoading.value = false
  }
}

const refreshMonitor = () => {
  if (!canViewMonitor.value) {
    return
  }

  fetchMonitorInfo()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.monitor-page {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0;
  }
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.table-container {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
}

.pagination {
  padding: 16px;
  display: flex;
  justify-content: flex-end;
  background: #f9fafb;
}

.monitor-content {
  min-height: 300px;
}

.offline-status {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.monitor-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.monitor-card {
  background: #f9fafb;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-bottom: 16px;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

.card-icon {
  font-size: 18px;
  
  &.cpu { color: #3b82f6; }
  &.memory { color: #8b5cf6; }
  &.disk { color: #f59e0b; }
  &.load { color: #10b981; }
}

.card-value {
  display: flex;
  justify-content: center;
  margin-bottom: 8px;
}

.percentage-value {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
}

.card-info {
  font-size: 12px;
  color: #6b7280;
}

.load-value {
  padding: 20px 0;
}

.load-number {
  font-size: 36px;
  font-weight: 700;
  color: #10b981;
}
@media (max-width: 768px) {
  .monitor-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .search-bar {
    flex-direction: column;

    :deep(.el-input) {
      width: 100% !important;
    }
  }

  .monitor-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .pagination {
    justify-content: center;
  }
}
</style>

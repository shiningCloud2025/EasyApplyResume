<template>
  <div class="llm-utils-info-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">LLM调用日志管理</h1>
        <p class="page-description">查看系统内各类大模型工具调用日志与执行结果</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData" :icon="Refresh" type="default">
          刷新
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item label="工具描述">
          <el-input
            v-model="queryForm.llmUtilsInfoToolDescription"
            placeholder="请输入工具描述"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="模型厂商">
          <el-input
            v-model="queryForm.llmUtilsInfoModelProvider"
            placeholder="请输入模型厂商"
            clearable
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="模型名称">
          <el-input
            v-model="queryForm.llmUtilsInfoModelName"
            placeholder="请输入模型名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="调用状态">
          <el-select
            v-model="queryForm.llmUtilsInfoStatus"
            placeholder="请选择状态"
            clearable
            style="width: 140px"
          >
            <el-option label="SUCCESS" value="SUCCESS" />
            <el-option label="FAILED" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">
            搜索
          </el-button>
          <el-button @click="resetSearch" :icon="RefreshRight">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">调用日志列表（共 {{ pagination.total }} 条）</span>
      </div>

      <el-table v-loading="loading" :data="records" style="width: 100%">
        <el-table-column prop="llmUtilsInfoId" label="日志ID" width="90" />
        <el-table-column prop="llmUtilsInfoToolClass" label="工具类名" min-width="180" show-overflow-tooltip />
        <el-table-column prop="llmUtilsInfoToolDescription" label="工具描述" min-width="260" show-overflow-tooltip />
        <el-table-column prop="llmUtilsInfoModelProvider" label="模型厂商" width="120" />
        <el-table-column prop="llmUtilsInfoModelName" label="模型名称" width="140" show-overflow-tooltip />
        <el-table-column label="调用状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.llmUtilsInfoStatus)">
              {{ row.llmUtilsInfoStatus || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="响应耗时(ms)" width="130" align="center">
          <template #default="{ row }">
            {{ row.llmUtilsInfoLatencyMs ?? '-' }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.llmUtilsInfoCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="default" @click="viewDetail(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="detailDialogVisible"
      title="LLM调用日志详情"
      width="960px"
      :before-close="() => { detailDialogVisible = false }"
    >
      <el-descriptions v-if="currentDetail" :column="2" border>
        <el-descriptions-item label="日志ID">{{ currentDetail.llmUtilsInfoId }}</el-descriptions-item>
        <el-descriptions-item label="调用状态">
          <el-tag :type="getStatusType(currentDetail.llmUtilsInfoStatus)">
            {{ currentDetail.llmUtilsInfoStatus || '-' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="工具类名" :span="2">{{ currentDetail.llmUtilsInfoToolClass || '-' }}</el-descriptions-item>
        <el-descriptions-item label="工具描述" :span="2">{{ currentDetail.llmUtilsInfoToolDescription || '-' }}</el-descriptions-item>
        <el-descriptions-item label="模型厂商">{{ currentDetail.llmUtilsInfoModelProvider || '-' }}</el-descriptions-item>
        <el-descriptions-item label="模型名称">{{ currentDetail.llmUtilsInfoModelName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="响应耗时(ms)">{{ currentDetail.llmUtilsInfoLatencyMs ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(currentDetail.llmUtilsInfoCreatedTime) }}</el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2">
          <div class="long-text error-text">{{ currentDetail.llmUtilsInfoErrorMessage || '-' }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="输入内容" :span="2">
          <div class="long-text">{{ currentDetail.llmUtilsInfoInputContent || '-' }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="输出结果" :span="2">
          <div class="long-text">{{ currentDetail.llmUtilsInfoOutputResult || '-' }}</div>
        </el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { llmUtilsInfoApi } from '@/api/admin'
import { formatDateTime } from '@/utils'
import type {
  AdminLlmUtilsInfoQuery,
  AdminLlmUtilsInfoPageVO,
  AdminLlmUtilsInfoVO
} from '@/types/admin'

const loading = ref(false)
const detailDialogVisible = ref(false)
const currentDetail = ref<AdminLlmUtilsInfoVO | null>(null)

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const queryForm = reactive<AdminLlmUtilsInfoQuery>({
  llmUtilsInfoToolDescription: '',
  llmUtilsInfoModelProvider: '',
  llmUtilsInfoModelName: '',
  llmUtilsInfoStatus: ''
})

const records = ref<AdminLlmUtilsInfoPageVO[]>([])

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    SUCCESS: 'success',
    FAILED: 'danger'
  }
  return map[status] || 'info'
}

const getLlmUtilsInfoList = async () => {
  try {
    loading.value = true
    const response = await llmUtilsInfoApi.getAdminLlmUtilsInfoPage(
      pagination.current,
      pagination.size,
      queryForm
    )

    records.value = response.data.records || []
    pagination.total = Number(response.data.total) || 0
    pagination.current = Number(response.data.current) || pagination.current
    pagination.size = Number(response.data.size) || pagination.size
  } catch (error) {
    console.error('获取LLM调用日志列表失败:', error)
    ElMessage.error('加载LLM调用日志失败')
  } finally {
    loading.value = false
  }
}

const viewDetail = async (row: AdminLlmUtilsInfoPageVO) => {
  try {
    const response = await llmUtilsInfoApi.getAdminLlmUtilsInfoById(row.llmUtilsInfoId)
    currentDetail.value = response.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取LLM调用日志详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

const handleSearch = () => {
  pagination.current = 1
  getLlmUtilsInfoList()
}

const resetSearch = () => {
  Object.assign(queryForm, {
    llmUtilsInfoToolDescription: '',
    llmUtilsInfoModelProvider: '',
    llmUtilsInfoModelName: '',
    llmUtilsInfoStatus: ''
  })
  pagination.current = 1
  getLlmUtilsInfoList()
}

const refreshData = () => {
  getLlmUtilsInfoList()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  getLlmUtilsInfoList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getLlmUtilsInfoList()
}

onMounted(() => {
  getLlmUtilsInfoList()
})
</script>

<style scoped>
.llm-utils-info-management {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  flex-direction: column;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.page-description {
  margin: 8px 0 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.table-card {
  margin-bottom: 20px;
}

.table-header {
  margin-bottom: 16px;
}

.table-title {
  font-size: 14px;
  color: #606266;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.long-text {
  max-height: 220px;
  overflow: auto;
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.7;
}

.error-text {
  color: #f56c6c;
}
</style>

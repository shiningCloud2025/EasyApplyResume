<template>
  <div class="user-feedback-records">
    <!-- 页头 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">用户端反馈记录</h1>
        <p class="page-description">查看所有用户反馈的历史记录</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData" :icon="Refresh" type="default">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="反馈人">
          <el-input
            v-model="searchForm.userFeedbackRecordName"
            placeholder="请输入反馈人姓名"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="反馈标题">
          <el-input
            v-model="searchForm.userFeedbackRecordTitle"
            placeholder="请输入反馈标题"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="处理人">
          <el-input
            v-model="searchForm.userFeedbackRecordApprovalPersonName"
            placeholder="请输入处理人姓名"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">用户反馈记录列表</span>
      </div>

      <el-table
        v-loading="loading"
        :data="records"
        style="width: 100%"
      >
        <el-table-column prop="userFeedbackRecordId" label="记录ID" width="80" />
        <el-table-column prop="userFeedbackRecordTitle" label="反馈标题" min-width="200" />
        <el-table-column prop="userFeedbackRecordName" label="反馈人" min-width="120" />
        <el-table-column prop="userFeedbackRecordNewStep" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.userFeedbackRecordNewStep)" size="small">
              {{ row.userFeedbackRecordNewStep }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userFeedbackRecordTime" label="提交时间" min-width="160" />
        <el-table-column prop="userFeedbackRecordCurrentStepSolveTime" label="处理时间" min-width="160" />
        <el-table-column prop="adminFeedbackRecordApprovalPersonName" label="处理人" min-width="120" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="info"
              size="default"
              @click="viewDetail(row)"
            >
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="反馈记录详情"
      width="60%"
      top="5vh"
      :before-close="() => { detailDialogVisible = false }"
    >
      <div v-if="currentDetail" class="detail-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="记录ID">
            {{ currentDetail.userFeedbackRecordId }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈标题">
            {{ currentDetail.userFeedbackRecordTitle }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈人">
            {{ currentDetail.userFeedbackRecordName }}
          </el-descriptions-item>
          <el-descriptions-item label="原节点">
            {{ currentDetail.userFeedbackRecordOldStep }}
          </el-descriptions-item>
          <el-descriptions-item label="现阶段">
            <el-tag :type="getStatusType(currentDetail.userFeedbackRecordNewStep)">
              {{ currentDetail.userFeedbackRecordNewStep }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="提交时间">
            {{ currentDetail.userFeedbackRecordTime }}
          </el-descriptions-item>
          <el-descriptions-item label="处理时间">
            {{ currentDetail.userFeedbackRecordCurrentStepSolveTime }}
          </el-descriptions-item>
          <el-descriptions-item label="处理人">
            {{ currentDetail.adminFeedbackRecordApprovalPersonName }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈内容" :span="1">
            <div class="content-display" v-html="currentDetail.userFeedbackRecordContent"></div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { userFeedbackRecordApi } from '@/api/admin'
import type { UserFeedbackRecordPageVO, UserFeedbackRecordQuery, UserFeedbackRecordInfoVO } from '@/types/admin'

// 响应式数据
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentDetail = ref<UserFeedbackRecordInfoVO | null>(null)

const searchForm = reactive<UserFeedbackRecordQuery>({
  userFeedbackRecordName: '',
  userFeedbackRecordTitle: '',
  userFeedbackRecordApprovalPersonName: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const records = ref<UserFeedbackRecordPageVO[]>([])

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    '待处理': 'warning',
    '待回复': 'primary',
    '已回复': 'success',
    '已忽视': 'info',
    '已拒绝': 'danger'
  }
  return map[status] || 'info'
}

// 获取记录列表
const getRecordsList = async () => {
  loading.value = true
  try {
    const response = await userFeedbackRecordApi.getUserFeedbackRecordPage(
      pagination.current,
      pagination.size,
      searchForm
    )
    records.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取记录列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getRecordsList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.userFeedbackRecordName = ''
  searchForm.userFeedbackRecordTitle = ''
  searchForm.userFeedbackRecordApprovalPersonName = ''
  pagination.current = 1
  getRecordsList()
}

// 刷新数据
const refreshData = () => {
  getRecordsList()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.size = size
  getRecordsList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getRecordsList()
}

// 查看详情
const viewDetail = async (row: UserFeedbackRecordPageVO) => {
  try {
    const response = await userFeedbackRecordApi.getUserFeedbackRecordDetail(row.userFeedbackRecordId)
    currentDetail.value = response.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取反馈记录详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 组件挂载
onMounted(() => {
  getRecordsList()
})
</script>

<style scoped lang="scss">
.user-feedback-records {
  font-size: 16px;
  
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 24px;
  }

  .header-content {
    .page-title {
      font-size: 24px;
      font-weight: 700;
      color: #1f2937;
      margin-bottom: 8px;
    }

    .page-description {
      font-size: 14px;
      color: #6b7280;
      margin: 0;
    }
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }

  .search-card {
    margin-bottom: 24px;
  }

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }

  .table-card {
    .el-table {
      font-size: 16px;
    }
    
    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
    }

    .table-title {
      font-size: 16px;
      font-weight: 600;
      color: #1f2937;
    }
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 24px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .user-feedback-records {
    .page-header {
      flex-direction: column;
      gap: 16px;
    }

    .header-actions {
      width: 100%;
      justify-content: flex-start;
    }

    .search-form {
      .el-form-item {
        width: 100%;
        margin-bottom: 16px;
      }
    }
  }
}

.detail-content {
  max-height: 70vh;
  overflow-y: auto;
}

.content-display {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.6;
}
</style>

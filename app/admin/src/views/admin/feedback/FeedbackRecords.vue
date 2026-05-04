<template>
  <div class="feedback-records">
    <!-- 页头 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">管理端反馈记录</h1>
        <p class="page-description">查看所有管理员反馈的历史记录</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData" :icon="Refresh" type="default">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索筛选 -->
    <el-card class="search-card">
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item label="反馈人">
          <el-input
            v-model="queryForm.adminFeedbackRecordName"
            placeholder="请输入反馈人姓名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="反馈标题">
          <el-input
            v-model="queryForm.adminFeedbackRecordTitle"
            placeholder="请输入反馈标题"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="处理人">
          <el-input
            v-model="queryForm.adminFeedbackRecordApprovalPersonName"
            placeholder="请输入处理人姓名"
            clearable
            style="width: 200px"
          />
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

    <!-- 数据表格 -->
    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">反馈记录列表（共 {{ pagination.total }} 条）</span>
      </div>

      <el-table
        v-loading="loading"
        :data="records"
        style="width: 100%"
      >
        <el-table-column prop="adminFeedbackRecordId" label="记录ID" width="80" />
        <el-table-column prop="adminFeedbackRecordAdminId" label="反馈人ID" width="100" align="center" />
        <el-table-column prop="adminFeedbackRecordName" label="反馈人" width="120" />
        <el-table-column prop="adminFeedbackRecordTitle" label="反馈标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="反馈内容" min-width="150">
          <template #default="{ row }">
            <el-button v-if="canViewRecordDetail" type="primary" size="small" @click="viewRecordContent(row)">
              详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="阶段变化" width="240" align="center">
          <template #default="{ row }">
            <div class="step-flow">
              <el-tag size="small" :type="getStatusType(row.adminFeedbackRecordOldStep)">
                {{ row.adminFeedbackRecordOldStep }}
              </el-tag>
              <el-icon class="arrow-icon"><Right /></el-icon>
              <el-tag size="small" :type="getStatusType(row.adminFeedbackRecordNewStep)">
                {{ row.adminFeedbackRecordNewStep }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="adminFeedbackRecordApprovalPersonId" label="处理人ID" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small" v-if="row.adminFeedbackRecordApprovalPersonId">
              {{ row.adminFeedbackRecordApprovalPersonId }}
            </el-tag>
            <span v-else style="color: #999; font-size: 12px;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="adminFeedbackRecordApprovalPersonName" label="处理人" width="120" />
        <el-table-column prop="adminFeedbackRecordTime" label="创建时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.adminFeedbackRecordTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="adminFeedbackRecordCurrentStepSolveTime" label="处理时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.adminFeedbackRecordCurrentStepSolveTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="canViewRecordDetail" label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="default"
              @click="viewDetail(row)"
            >
              查看详情
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
      width="700px"
      :before-close="() => { detailDialogVisible = false }"
    >
      <template #header>
        <div class="dialog-header">
          <span class="el-dialog__title">反馈记录详情</span>
          <button class="el-dialog__headerbtn" @click="detailDialogVisible = false">
            <i class="el-dialog__close el-icon el-icon-close"></i>
          </button>
        </div>
      </template>
      <el-descriptions v-if="currentRecord" :column="2" border>
        <el-descriptions-item label="记录ID">
          {{ currentRecord.adminFeedbackRecordId }}
        </el-descriptions-item>
        <el-descriptions-item label="反馈人ID">
          {{ currentRecord.adminFeedbackRecordAdminId }}
        </el-descriptions-item>
        <el-descriptions-item label="反馈人姓名" :span="2">
          <strong>{{ currentRecord.adminFeedbackRecordName }}</strong>
        </el-descriptions-item>
        <el-descriptions-item label="处理人ID">
          <el-tag size="small" v-if="currentRecord.adminFeedbackRecordApprovalPersonId">
            {{ currentRecord.adminFeedbackRecordApprovalPersonId }}
          </el-tag>
          <span v-else style="color: #999;">暂无</span>
        </el-descriptions-item>
        <el-descriptions-item label="处理人姓名">
          {{ currentRecord.adminFeedbackRecordApprovalPersonName || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="反馈标题" :span="2">
          <strong>{{ currentRecord.adminFeedbackRecordTitle }}</strong>
        </el-descriptions-item>
        
        <el-descriptions-item label="原阶段">
          <el-tag type="info" size="small">{{ currentRecord.adminFeedbackRecordOldStep }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="新阶段">
          <el-tag :type="getStatusType(currentRecord.adminFeedbackRecordNewStep)" size="small">
            {{ currentRecord.adminFeedbackRecordNewStep }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDate(currentRecord.adminFeedbackRecordTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="处理时间">
          {{ formatDate(currentRecord.adminFeedbackRecordCurrentStepSolveTime) }}
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 查看反馈内容对话框 -->
    <el-dialog
      v-model="contentDialogVisible"
      title="反馈内容"
      width="800px"
      :before-close="() => { contentDialogVisible = false }"
    >
      <template #header>
        <div class="dialog-header">
          <span class="el-dialog__title">反馈内容</span>
          <button class="el-dialog__headerbtn" @click="contentDialogVisible = false">
            <i class="el-dialog__close el-icon el-icon-close"></i>
          </button>
        </div>
      </template>
      <div class="feedback-content">
        <div class="content-display">{{ currentContent?.adminFeedbackRecordContent }}</div>
      </div>
      
      <template #footer>
        <el-button @click="contentDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search, RefreshRight, Right } from '@element-plus/icons-vue'
import { feedbackRecordApi } from '@/api/admin'
import { formatDateTime, formatDate } from '@/utils'
import type {
  AdminFeedbackRecordQuery,
  AdminFeedbackRecordPageVO,
  AdminFeedbackRecordInfoVO
} from '@/types/admin'
import { useAuthStore } from '@/store/auth'

// 响应式数据
const loading = ref(false)
const detailDialogVisible = ref(false)
const contentDialogVisible = ref(false)
const currentRecord = ref<AdminFeedbackRecordInfoVO | null>(null)
const currentContent = ref<AdminFeedbackRecordPageVO | null>(null)
const authStore = useAuthStore()
const canViewRecordDetail = computed(() => authStore.hasPermission('/admin/adminFeedbackRecord/findAdminFeedbackRecordByFeedbackRecordId'))

// 分页
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 搜索表单
const queryForm = reactive<AdminFeedbackRecordQuery>({
  adminFeedbackRecordName: '',
  adminFeedbackRecordTitle: '',
  adminFeedbackRecordApprovalPersonName: ''
})

// 表格数据
const records = ref<AdminFeedbackRecordPageVO[]>([])

// 状态类型映射（优化颜色）
const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    '待处理': 'warning',      // 橙色
    '待接受': 'warning',      // 橙色
    '待回复': '',             // 蓝色（默认）
    '已回复': 'success',      // 绿色
    '已接受': 'success',      // 绿色
    '已忽视': 'info',         // 灰色
    '拒绝回复': 'danger',     // 红色
    '特回复': '',             // 蓝色
    '已处理': 'success'       // 绿色
  }
  return map[status] || ''  // 默认蓝色
}

// 获取记录列表
const getRecordsList = async () => {
  try {
    loading.value = true
    console.log('📋 [反馈记录] 开始获取列表...')
    console.log('📋 [反馈记录] 查询参数:', queryForm)
    console.log('📋 [反馈记录] 分页参数:', pagination.current, pagination.size)
    
    const response = await feedbackRecordApi.getRecordPage(
      pagination.current,
      pagination.size,
      queryForm
    )
    
    console.log('📋 [反馈记录] API响应:', response)
    
    if (response && response.data) {
      records.value = response.data.records || []
      pagination.total = response.data.total || 0
      
      console.log('✅ [反馈记录] 加载成功，共', pagination.total, '条记录')
      console.log('📋 [反馈记录] 当前页数据:', records.value)
    }
  } catch (error: any) {
    console.error('❌ [反馈记录] 获取列表失败:', error)
    ElMessage.error('加载数据失败：' + (error.message || '请稍后重试'))
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
  Object.assign(queryForm, {
    adminFeedbackRecordName: '',
    adminFeedbackRecordTitle: '',
    adminFeedbackRecordApprovalPersonName: ''
  })
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
const viewDetail = async (row: AdminFeedbackRecordPageVO) => {
  if (!canViewRecordDetail.value) {
    ElMessage.error('暂无查看权限')
    return
  }

  try {
    console.log('📋 [反馈记录] 查看详情，记录ID:', row.adminFeedbackRecordId)
    
    // 调用详情接口获取完整信息
    const response = await feedbackRecordApi.getRecordDetail(row.adminFeedbackRecordId)
    console.log('📋 [反馈记录] 详情数据:', response.data)
    
    currentRecord.value = response.data
    detailDialogVisible.value = true
    
  } catch (error: any) {
    console.error('❌ [反馈记录] 获取详情失败:', error)
    ElMessage.error('获取详情失败：' + (error.message || '请稍后重试'))
  }
}

// 查看反馈内容详情
const viewRecordContent = (row: AdminFeedbackRecordPageVO) => {
  if (!canViewRecordDetail.value) {
    ElMessage.error('暂无查看权限')
    return
  }

  currentContent.value = row
  contentDialogVisible.value = true
}



// 组件挂载
onMounted(() => {
  getRecordsList()
})
</script>

<style scoped lang="scss">
.feedback-records {
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

    .search-form {
      :deep(.el-form-item) {
        margin-bottom: 16px;
      }
    }
  }

  .table-card {
    .el-table {
      font-size: 14px;
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

  .step-flow {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    
    .arrow-icon {
      color: #9ca3af;
      font-size: 16px;
      font-weight: bold;
    }
    
    .el-tag {
      font-weight: 500;
      padding: 4px 12px;
    }
  }

  .content-title {
    margin: 0 0 16px 0;
    font-size: 18px;
    font-weight: 600;
    color: #333;
    text-align: center;
  }

  .content-display {
    max-height: 300px;
    overflow-y: auto;
    padding: 12px;
    background: #f9fafb;
    border-radius: 6px;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-word;
  }

  .dialog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .el-dialog__headerbtn {
    background: transparent;
    border: none;
    outline: none;
    cursor: pointer;
    padding: 0;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: background-color 0.2s;
  }

  .el-dialog__headerbtn:hover {
    background-color: #f0f0f0;
  }

  .el-dialog__close {
    font-size: 16px;
    color: #909399;
  }

  .el-dialog__close:hover {
    color: #409eff;
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 24px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .feedback-records {
    .page-header {
      flex-direction: column;
      gap: 16px;
    }

    .header-actions {
      width: 100%;
      justify-content: flex-start;
    }

    .search-form {
      :deep(.el-form-item) {
        display: block;
        width: 100%;
      }
    }
  }
}
</style>

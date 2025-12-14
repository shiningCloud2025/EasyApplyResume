<template>
  <div class="feedback-manage">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">管理端反馈管理</h1>
        <p class="page-description">处理管理员提交的反馈信息</p>
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
        <el-form-item label="反馈标题">
          <el-input
            v-model="searchForm.adminFeedbackTitle"
            placeholder="请输入反馈标题"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="反馈内容">
          <el-input
            v-model="searchForm.adminFeedbackContent"
            placeholder="请输入反馈内容"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 反馈列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">反馈列表</span>
      </div>

      <el-table 
        v-loading="loading"
        :data="tableData" 
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="adminFeedbackId" label="ID" width="70" />
        <el-table-column prop="adminFeedbackTitle" label="标题" min-width="200" />
        <el-table-column label="反馈内容" min-width="180">
          <template #default="{ row }">
            <el-tooltip :content="row.adminFeedbackContent || '暂无内容'" placement="top">
              <div class="text-ellipsis">
                {{ row.adminFeedbackContent ? row.adminFeedbackContent.substring(0, 30) + '...' : '-' }}
              </div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" width="180">
          <template #default="{ row }">
            {{ row.adminFeedbackTime ? formatDateTime(row.adminFeedbackTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="最近处理时间" width="180">
          <template #default="{ row }">
            {{ row.adminFeedbackRecentTime ? formatDateTime(row.adminFeedbackRecentTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="adminFeedbackCurStep" label="当前状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.adminFeedbackCurStep)">
              {{ row.adminFeedbackCurStep }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="adminFeedbackAdminName" label="提交人" width="120" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button
              type="info"
              size="default"
              @click="handleDetail(row)"
            >
              查看
            </el-button>
            <!-- 待接收状态：显示接受和忽视按钮 -->
            <template v-if="row.adminFeedbackCurStep === '待接收'">
              <el-button
                type="success"
                size="default"
                @click="handleProcess(row, 0, '接受')"
              >
                接受
              </el-button>
              <el-button
                type="warning"
                size="default"
                @click="handleProcess(row, 1, '忽视')"
              >
                忽视
              </el-button>
            </template>
            <!-- 待回复状态：显示回复和拒回复按钮 -->
            <template v-else-if="row.adminFeedbackCurStep === '待回复'">
              <el-button
                type="primary"
                size="default"
                @click="handleProcess(row, 2, '回复')"
              >
                回复
              </el-button>
              <el-button
                type="danger"
                size="default"
                @click="handleProcess(row, 3, '拒回复')"
              >
                拒回复
              </el-button>
            </template>
            <!-- 已回复、忽视、拒回复状态：不显示处理按钮 -->
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

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="showDetailDialog"
      title="反馈详情"
      width="700px"
    >
      <div class="feedback-detail" v-if="currentDetail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="反馈标题">
            {{ currentDetail.adminFeedbackTitle }}
          </el-descriptions-item>
          <el-descriptions-item label="提交人">
            {{ currentDetail.adminFeedbackAdminName }}
          </el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(currentDetail.adminFeedbackCurStep)">
              {{ currentDetail.adminFeedbackCurStep }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="提交时间">
            {{ formatDateTime(currentDetail.adminFeedbackTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="最近处理时间">
            {{ formatDateTime(currentDetail.adminFeedbackRecentTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈内容">
            <div class="detail-content">{{ currentDetail.adminFeedbackContent }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 处理反馈对话框 -->
    <el-dialog
      v-model="showProcessDialog"
      :title="`${processAction} - ${currentFeedback?.adminFeedbackTitle || ''}`"
      width="600px"
      @close="resetProcessForm"
    >
      <el-form
        ref="processFormRef"
        :model="processForm"
        :rules="processRules"
        label-width="100px"
      >
        <el-form-item label="反馈标题" prop="title">
          <el-input v-model="processForm.title" placeholder="请输入处理后的标题" />
        </el-form-item>
        
        <el-form-item label="处理内容" prop="content">
          <el-input
            v-model="processForm.content"
            type="textarea"
            :rows="6"
            placeholder="请输入处理内容或备注"
          />
        </el-form-item>

        <el-alert
          type="info"
          :closable="false"
          show-icon
          style="margin-top: 12px"
        >
          <template #title>
            <span>操作说明：{{ getOperationDesc(processForm.operationCode) }}</span>
          </template>
        </el-alert>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showProcessDialog = false">取消</el-button>
          <el-button type="primary" @click="handleProcessSubmit" :loading="submitting">
            确定{{ processAction }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { feedbackApi } from '@/api/admin'
import type {
  AdminFeedbackPageVO,
  AdminFeedbackQuery,
  AdminFeedbackInfoVO,
  AdminUpdateFeedbackForm
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showDetailDialog = ref(false)
const showProcessDialog = ref(false)
const currentDetail = ref<AdminFeedbackInfoVO | null>(null)
const currentFeedback = ref<AdminFeedbackPageVO | null>(null)
const processAction = ref('')

// 搜索表单
const searchForm = reactive<AdminFeedbackQuery>({
  adminFeedbackTitle: '',
  adminFeedbackContent: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<AdminFeedbackPageVO[]>([])

// 处理表单
const processFormRef = ref<FormInstance>()
const processForm = reactive<AdminUpdateFeedbackForm>({
  operationCode: 0,
  title: '',
  content: ''
})

// 表单校验规则
const processRules = {
  title: [
    { required: true, message: '请输入处理后的标题', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入处理内容', trigger: 'blur' }
  ]
}

// 获取反馈列表
const getFeedbackList = async () => {
  loading.value = true
  try {
    const response = await feedbackApi.getFeedbackPage(
      pagination.current,
      pagination.size,
      searchForm
    )
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取反馈列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getFeedbackList()
}

// 重置搜索
const handleReset = () => {
  searchForm.adminFeedbackTitle = ''
  searchForm.adminFeedbackContent = ''
  pagination.current = 1
  getFeedbackList()
}

// 刷新数据
const refreshData = () => {
  getFeedbackList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getFeedbackList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getFeedbackList()
}

// 查看详情
const handleDetail = async (row: AdminFeedbackPageVO) => {
  try {
    const response = await feedbackApi.getFeedbackDetail(row.adminFeedbackId)
    currentDetail.value = response.data
    showDetailDialog.value = true
  } catch (error) {
    console.error('获取反馈详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 处理反馈
const handleProcess = async (row: AdminFeedbackPageVO, operationCode: number, actionName: string) => {
  try {
    const response = await feedbackApi.getFeedbackDetail(row.adminFeedbackId)
    currentFeedback.value = row
    processAction.value = actionName
    processForm.operationCode = operationCode
    processForm.title = response.data.adminFeedbackTitle
    processForm.content = ''
    showProcessDialog.value = true
  } catch (error) {
    console.error('获取反馈详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 获取操作说明
const getOperationDesc = (code: number) => {
  const descMap: Record<number, string> = {
    0: '接受反馈后，状态将变为"待回复"，并会发送短信通知',
    1: '忽视反馈后，状态将变为"忽视"',
    2: '回复反馈后，状态将变为"已回复"，并会发送短信通知',
    3: '拒绝回复后，状态将变为"拒回复"'
  }
  return descMap[code] || ''
}

// 获取状态类型
const getStatusType = (status: string) => {
  const typeMap: Record<string, string> = {
    '待接收': 'warning',
    '待回复': 'primary',
    '已回复': 'success',
    '忽视': 'info',
    '拒回复': 'danger'
  }
  return typeMap[status] || 'info'
}

// 提交处理
const handleProcessSubmit = async () => {
  if (!processFormRef.value || !currentFeedback.value) return
  
  try {
    await processFormRef.value.validate()
    submitting.value = true
    
    await feedbackApi.updateFeedbackStep(
      currentFeedback.value.adminFeedbackId,
      processForm.operationCode,
      processForm.title,
      processForm.content,
      currentFeedback.value.adminFeedbackAdminId
    )
    
    ElMessage.success(`${processAction.value}成功`)
    showProcessDialog.value = false
    getFeedbackList()
  } catch (error) {
    console.error('处理失败:', error)
    ElMessage.error('处理失败')
  } finally {
    submitting.value = false
  }
}

// 重置处理表单
const resetProcessForm = () => {
  if (processFormRef.value) {
    processFormRef.value.resetFields()
  }
  
  currentFeedback.value = null
  processAction.value = ''
  Object.assign(processForm, {
    operationCode: 0,
    title: '',
    content: ''
  })
}

// 组件挂载
onMounted(() => {
  getFeedbackList()
})
</script>

<style scoped lang="scss">
.feedback-manage {
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

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .feedback-detail {
    .detail-content {
      line-height: 1.6;
      color: #374151;
      white-space: pre-wrap;
    }
  }

  .text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: help;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .feedback-manage {
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
</style>
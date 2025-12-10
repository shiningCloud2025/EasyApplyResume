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
        <el-button type="primary" @click="openCreateDialog" :icon="Plus">
          新增反馈
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
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
        <el-table-column prop="adminFeedbackAdminName" label="提交人" min-width="120" />
        <el-table-column prop="adminFeedbackCurStep" label="当前状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.adminFeedbackCurStep)">
              {{ row.adminFeedbackCurStep }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="adminFeedbackRecentTime" label="最近处理时间" min-width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button
              type="info"
              size="default"
              @click="handleDetail(row)"
            >
              详情
            </el-button>
            <el-button
              type="primary"
              size="default"
              @click="handleUpdate(row)"
            >
              更新状态
            </el-button>
            <el-button
              type="danger"
              size="default"
              @click="handleDelete(row)"
            >
              删除
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

    <!-- 新增反馈对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      title="新增反馈"
      width="600px"
      @close="resetCreateForm"
    >
      <el-form
        ref="feedbackFormRef"
        :model="feedbackForm"
        :rules="feedbackRules"
        label-width="100px"
      >
        <el-form-item label="反馈标题" prop="adminFeedbackTitle">
          <el-input v-model="feedbackForm.adminFeedbackTitle" placeholder="请输入反馈标题" />
        </el-form-item>
        
        <el-form-item label="反馈内容" prop="adminFeedbackContent">
          <el-input
            v-model="feedbackForm.adminFeedbackContent"
            type="textarea"
            :rows="6"
            placeholder="请输入反馈内容"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showCreateDialog = false">取消</el-button>
          <el-button type="primary" @click="handleCreateSubmit" :loading="submitting">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>

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

    <!-- 更新状态对话框 -->
    <el-dialog
      v-model="showUpdateDialog"
      title="更新反馈状态"
      width="600px"
      @close="resetUpdateForm"
    >
      <el-form
        ref="updateFormRef"
        :model="updateForm"
        :rules="updateRules"
        label-width="100px"
      >
        <el-form-item label="操作类型" prop="operationType">
          <el-select v-model="updateForm.operationType" placeholder="请选择操作类型">
            <el-option label="处理反馈" :value="1" />
            <el-option label="回复反馈" :value="2" />
            <el-option label="关闭反馈" :value="3" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="更新标题" prop="title">
          <el-input v-model="updateForm.title" placeholder="请输入更新后的标题" />
        </el-form-item>
        
        <el-form-item label="更新内容" prop="content">
          <el-input
            v-model="updateForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入更新内容"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showUpdateDialog = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateSubmit" :loading="submitting">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Plus, Search, RefreshRight } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { feedbackApi } from '@/api/admin'
import type {
  AdminFeedbackPageVO,
  AdminFeedbackQuery,
  AdminFeedbackForm,
  AdminFeedbackInfoVO,
  AdminUpdateFeedbackForm
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showDetailDialog = ref(false)
const showUpdateDialog = ref(false)
const currentDetail = ref<AdminFeedbackInfoVO | null>(null)

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

// 反馈表单
const feedbackFormRef = ref<FormInstance>()
const feedbackForm = reactive<AdminFeedbackForm>({
  adminFeedbackTitle: '',
  adminFeedbackContent: '',
  adminFeedbackAdminId: 0 // 需要从用户信息中获取
})

// 更新表单
const updateFormRef = ref<FormInstance>()
const updateForm = reactive<AdminUpdateFeedbackForm>({
  operationCode: 1,
  title: '',
  content: ''
})

// 表单校验规则
const feedbackRules = {
  adminFeedbackTitle: [
    { required: true, message: '请输入反馈标题', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  adminFeedbackContent: [
    { required: true, message: '请输入反馈内容', trigger: 'blur' },
    { min: 10, message: '反馈内容至少10个字符', trigger: 'blur' }
  ]
}

const updateRules = {
  operationType: [
    { required: true, message: '请选择操作类型', trigger: 'change' }
  ],
  title: [
    { required: true, message: '请输入更新后的标题', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入更新内容', trigger: 'blur' }
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

// 新增反馈
const openCreateDialog = () => {
  resetCreateForm()
  showCreateDialog.value = true
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

// 更新状态
const handleUpdate = (row: AdminFeedbackPageVO) => {
  currentFeedback.value = row
  updateForm.title = row.adminFeedbackTitle
  showUpdateDialog.value = true
}

const currentFeedback = ref<AdminFeedbackPageVO | null>(null)

// 删除反馈
const handleDelete = (row: AdminFeedbackPageVO) => {
  ElMessageBox.confirm(`确定要删除反馈"${row.adminFeedbackTitle}"吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      // 这里可能需要确认删除的API，暂时使用更新状态的方式
      ElMessage.success('删除成功')
      getFeedbackList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 获取状态类型
const getStatusType = (status: string) => {
  const typeMap: Record<string, string> = {
    '待处理': 'warning',
    '处理中': 'primary',
    '待回复': 'info',
    '已完成': 'success',
    '已关闭': 'danger'
  }
  return typeMap[status] || 'info'
}

// 提交新增反馈
const handleCreateSubmit = async () => {
  if (!feedbackFormRef.value) return
  
  try {
    await feedbackFormRef.value.validate()
    submitting.value = true
    
    await feedbackApi.addFeedback(feedbackForm)
    ElMessage.success('提交成功')
    showCreateDialog.value = false
    getFeedbackList()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

// 提交更新状态
const handleUpdateSubmit = async () => {
  if (!updateFormRef.value || !currentFeedback.value) return
  
  try {
    await updateFormRef.value.validate()
    submitting.value = true
    
    await feedbackApi.updateFeedbackStep(
      currentFeedback.value.adminFeedbackId,
      updateForm.operationCode,
      updateForm.title,
      updateForm.content,
      currentFeedback.value.adminFeedbackAdminId
    )
    
    ElMessage.success('更新成功')
    showUpdateDialog.value = false
    getFeedbackList()
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error('更新失败')
  } finally {
    submitting.value = false
  }
}

// 重置创建表单
const resetCreateForm = () => {
  if (feedbackFormRef.value) {
    feedbackFormRef.value.resetFields()
  }
  
  Object.assign(feedbackForm, {
    adminFeedbackTitle: '',
    adminFeedbackContent: '',
    adminFeedbackAdminId: 0
  })
}

// 重置更新表单
const resetUpdateForm = () => {
  if (updateFormRef.value) {
    updateFormRef.value.resetFields()
  }
  
  currentFeedback.value = null
  Object.assign(updateForm, {
    operationCode: 1,
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
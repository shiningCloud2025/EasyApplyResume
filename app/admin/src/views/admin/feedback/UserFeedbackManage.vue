<template>
  <div class="user-feedback-manage">
    <!-- 页头 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">用户端反馈管理</h1>
        <p class="page-description">处理用户提交的反馈信息</p>
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
            v-model="searchForm.userFeedbackTitle"
            placeholder="请输入反馈标题"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="反馈内容">
          <el-input
            v-model="searchForm.userFeedbackContent"
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

    <!-- 数据表格 -->
    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">用户反馈列表</span>
      </div>

      <el-table
        v-loading="loading"
        :data="feedbackList"
        style="width: 100%"
      >
        <el-table-column prop="userFeedbackId" label="反馈ID" width="80" />
        <el-table-column prop="userFeedbackTitle" label="反馈标题" min-width="200" />
        <el-table-column prop="userFeedbackUserName" label="用户姓名" min-width="120" />
        <el-table-column prop="userFeedbackCurStep" label="当前阶段" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.userFeedbackCurStep)">
              {{ row.userFeedbackCurStep }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userFeedbackTime" label="提交时间" min-width="160">
          <template #default="{ row }">
            {{ formatDate(row.userFeedbackTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button
              type="info"
              size="default"
              @click="handleDetail(row)"
            >
              详情
            </el-button>
            <!-- 待处理状态：显示接受和忽视按钮 -->
            <template v-if="row.userFeedbackCurStep === '待处理'">
              <el-button
                type="primary"
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
            <template v-else-if="row.userFeedbackCurStep === '待回复'">
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
          <el-descriptions-item label="用户反馈ID">
            {{ currentDetail.userFeedbackId }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈标题">
            {{ currentDetail.userFeedbackTitle }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈提交时间">
            {{ formatDate(currentDetail.userFeedbackTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="最近一次处理时间">
            {{ formatDate(currentDetail.userFeedbackRecentTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈现阶段">
            <el-tag :type="getStatusType(currentDetail.userFeedbackCurStep)">
              {{ currentDetail.userFeedbackCurStep }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="提交反馈的用户ID">
            {{ currentDetail.userFeedbackUserId }}
          </el-descriptions-item>
          <el-descriptions-item label="提交反馈的用户姓名">
            {{ currentDetail.userFeedbackUserName }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
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
        <div class="content-title" v-if="currentContent && currentContent.userFeedbackTitle">
          <h3>{{ currentContent.userFeedbackTitle }}</h3>
        </div>
        <div class="content-body" v-if="currentContent">
          <div v-html="currentContent.userFeedbackContent"></div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="contentDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { userFeedbackApi } from '@/api/admin'
import type { UserFeedbackPageVO, UserFeedbackInfoVO, UserFeedbackQuery } from '@/types/admin'
import { useAuthStore } from '@/store/auth'

// 响应式数据
const loading = ref(false)
const showDetailDialog = ref(false)
const contentDialogVisible = ref(false)
const currentDetail = ref<UserFeedbackInfoVO | null>(null)
const currentContent = ref<UserFeedbackInfoVO | null>(null)

const searchForm = reactive<UserFeedbackQuery>({
  userFeedbackTitle: '',
  userFeedbackContent: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const feedbackList = ref<UserFeedbackPageVO[]>([])

// 获取状态标签类型
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '待处理': 'warning',
    '待回复': 'primary',
    '已回复': 'success',
    '已忽视': 'info',
    '拒回复': 'danger'
  }
  return statusMap[status] || 'info'
}

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  }).replace(/\//g, '-')
}

// 获取反馈列表
const getFeedbackList = async () => {
  loading.value = true
  try {
    const response = await userFeedbackApi.getUserFeedbackPage(
      pagination.size,
      pagination.current,
      searchForm
    )
    feedbackList.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取用户反馈列表失败:', error)
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
  searchForm.userFeedbackTitle = ''
  searchForm.userFeedbackContent = ''
  pagination.current = 1
  getFeedbackList()
}

// 刷新数据
const refreshData = () => {
  getFeedbackList()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.size = size
  getFeedbackList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getFeedbackList()
}

// 查看详情
const handleDetail = async (row: UserFeedbackPageVO) => {
  try {
    const response = await userFeedbackApi.getUserFeedbackDetail(row.userFeedbackId)
    currentDetail.value = response.data
    showDetailDialog.value = true
  } catch (error) {
    console.error('获取反馈详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 查看反馈内容
const handleShowContent = async (row: UserFeedbackPageVO) => {
  try {
    const response = await userFeedbackApi.getUserFeedbackDetail(row.userFeedbackId)
    currentContent.value = response.data
    contentDialogVisible.value = true
  } catch (error) {
    console.error('获取反馈内容失败:', error)
    ElMessage.error('获取内容失败')
  }
}

// 处理反馈（接受/忽视/回复/拒回复）
const handleProcess = (row: UserFeedbackPageVO, operationCode: number, action: string) => {
  ElMessageBox.prompt(`请输入${action}理由：`, `${action}反馈`, {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^.{1,200}$/,
    inputErrorMessage: '理由长度应在1-200个字符之间'
  }).then(async ({ value }) => {
    try {
      const authStore = useAuthStore()
      const adminId = authStore.user?.adminId
      
      if (!adminId) {
        ElMessage.error('无法获取管理员信息，请重新登录')
        return
      }
      
      await userFeedbackApi.updateUserFeedbackStep(
        row.userFeedbackId,
        operationCode,
        row.userFeedbackTitle,
        value, // 使用输入的理由作为内容
        adminId
      )
      
      ElMessage.success(`${action}成功`)
      // 刷新列表
      getFeedbackList()
    } catch (error) {
      console.error(`${action}反馈失败:`, error)
      ElMessage.error(`${action}失败`)
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 组件挂载
onMounted(() => {
  getFeedbackList()
})
</script>

<style scoped lang="scss">
.user-feedback-manage {
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

  .feedback-content {
    padding: 20px;
    
    .content-title {
      margin-bottom: 20px;
      padding-bottom: 10px;
      border-bottom: 1px solid #eee;
      
      h3 {
        margin: 0;
        font-size: 20px;
        color: #333;
      }
    }
    
    .content-body {
      line-height: 1.6;
      
      :deep(img) {
        max-width: 100%;
        height: auto;
      }
      
      :deep(p) {
        margin: 10px 0;
      }
    }
  }
}
</style>
</file>
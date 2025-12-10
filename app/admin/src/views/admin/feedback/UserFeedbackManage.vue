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
            v-model="searchForm.title"
            placeholder="请输入标题"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择"
            clearable
            style="width: 180px"
          >
            <el-option label="待处理" value="pending" />
            <el-option label="待回复" value="waiting_reply" />
            <el-option label="已回复" value="replied" />
            <el-option label="已忽视" value="ignored" />
          </el-select>
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
        <span class="table-title">用户反馈列表</span>
      </div>

      <el-table
        v-loading="loading"
        :data="feedbackList"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="userName" label="用户" min-width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" min-width="160" />
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
              v-if="row.status === 'pending'"
              type="primary"
              size="default"
              @click="handleProcess(row)"
            >
              处理
            </el-button>
            <el-button
              v-if="row.status === 'waiting_reply'"
              type="success"
              size="default"
              @click="handleReply(row)"
            >
              回复
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search, RefreshRight } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)

const searchForm = reactive({
  title: '',
  status: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 1
})

const feedbackList = ref([
  {
    id: 1,
    title: '简历模板建议',
    userName: '张三',
    status: 'pending',
    createTime: '2024-03-15 10:30'
  }
])

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    'pending': 'warning',
    'waiting_reply': 'primary',
    'replied': 'success',
    'ignored': 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    'pending': '待处理',
    'waiting_reply': '待回复',
    'replied': '已回复',
    'ignored': '已忽视'
  }
  return map[status] || status
}

// 获取反馈列表
const getFeedbackList = async () => {
  loading.value = true
  try {
    // TODO: 调用实际API
    // const response = await feedbackApi.getUserFeedbackPage(
    //   pagination.current,
    //   pagination.size,
    //   searchForm
    // )
    // feedbackList.value = response.data.records
    // pagination.total = response.data.total
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
const resetSearch = () => {
  searchForm.title = ''
  searchForm.status = ''
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
const handleDetail = (row: any) => {
  ElMessage.info('查看用户反馈详情')
}

// 处理反馈
const handleProcess = (row: any) => {
  ElMessage.success('处理反馈')
}

// 回复反馈
const handleReply = (row: any) => {
  ElMessage.success('回复反馈')
}

// 组件挂载
onMounted(() => {
  // getFeedbackList()
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

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 24px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .user-feedback-manage {
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

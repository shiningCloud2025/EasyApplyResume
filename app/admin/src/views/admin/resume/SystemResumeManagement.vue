<template>
  <div class="system-resume-management">
    <!-- 页头 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">系统删除简历管理</h1>
        <p class="page-description">管理系统回收的用户删除简历记录</p>
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
        <el-form-item label="简历名称">
          <el-input
            v-model="searchForm.userDeleteResumeResumeName"
            placeholder="请输入简历名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="行业名称">
          <el-input
            v-model="searchForm.userDeleteResumeIndustryName"
            placeholder="请输入行业名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input-number
            v-model="searchForm.userDeleteResumeUserId"
            placeholder="请输入用户ID"
            :min="1"
            controls-position="right"
            style="width: 140px"
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
        <span class="table-title">删除简历列表</span>
        <span class="table-count">共 {{ pagination.total }} 条记录</span>
      </div>

      <el-table
        v-loading="loading"
        :data="resumeList"
        style="width: 100%"
      >
        <el-table-column prop="userDeleteResumeBySystemId" label="ID" width="70" />
        <el-table-column prop="userDeleteResumeBySystemResumeName" label="简历名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="userDeleteResumeBySystemIndustryName" label="行业" width="120" show-overflow-tooltip />
        <el-table-column prop="userDeleteResumeBySystemUserId" label="用户ID" width="80" />
        <el-table-column prop="userDeleteResumeBySystemSortedNum" label="排序" width="70" />
        <el-table-column label="创建时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.userDeleteResumeBySystemCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.userDeleteResumeBySystemUpdatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="回收时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.userDeleteResumeBySystemRecycleTime) }}
          </template>
        </el-table-column>
        <el-table-column label="代码" width="100" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="showCode(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
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

    <!-- 简历详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="简历详情"
      width="900px"
      :before-close="() => { detailDialogVisible = false }"
    >
      <div class="resume-detail" v-if="currentResume">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="系统删除简历ID">
            {{ currentResume.userDeleteResumeBySystemId }}
          </el-descriptions-item>
          <el-descriptions-item label="用户ID">
            {{ currentResume.userDeleteResumeBySystemUserId }}
          </el-descriptions-item>
          <el-descriptions-item label="简历名称" :span="2">
            <strong>{{ currentResume.userDeleteResumeBySystemResumeName }}</strong>
          </el-descriptions-item>
          <el-descriptions-item label="行业">
            {{ currentResume.userDeleteResumeBySystemIndustryName }}
          </el-descriptions-item>
          <el-descriptions-item label="排序序号">
            {{ currentResume.userDeleteResumeBySystemSortedNum }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(currentResume.userDeleteResumeBySystemCreatedTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDate(currentResume.userDeleteResumeBySystemUpdatedTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="回收时间" :span="2">
            {{ formatDate(currentResume.userDeleteResumeBySystemRecycleTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 代码查看对话框 -->
    <el-dialog
      v-model="codeDialogVisible"
      title="React组件代码"
      width="900px"
    >
      <div class="code-content">
        <el-input
          type="textarea"
          :model-value="currentCode"
          readonly
          :autosize="{ minRows: 15, maxRows: 30 }"
        />
      </div>
      <template #footer>
        <el-button @click="codeDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { systemDeleteResumeApi } from '@/api/admin'
import type { UserDeleteResumeBySystemPageVO, UserDeleteResumeBySystemInfoVO, UserDeleteResumeQuery } from '@/types/admin'

// 响应式数据
const loading = ref(false)
const detailDialogVisible = ref(false)
const codeDialogVisible = ref(false)
const currentResume = ref<UserDeleteResumeBySystemInfoVO | null>(null)
const currentCode = ref('')

const searchForm = reactive<UserDeleteResumeQuery>({
  userDeleteResumeResumeName: '',
  userDeleteResumeIndustryName: '',
  userDeleteResumeUserId: undefined
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const resumeList = ref<UserDeleteResumeBySystemPageVO[]>([])

// 格式化日期（只显示日期）
const formatDate = (dateStr: string | null | undefined): string => {
  if (!dateStr) return '-'
  try {
    const date = new Date(dateStr)
    if (isNaN(date.getTime())) return '-'
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
  } catch {
    return '-'
  }
}

// 获取数据列表
const getResumeList = async () => {
  loading.value = true
  try {
    const response = await systemDeleteResumeApi.getDeleteResumePage(
      pagination.current,
      pagination.size,
      searchForm
    )
    resumeList.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getResumeList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.userDeleteResumeResumeName = ''
  searchForm.userDeleteResumeIndustryName = ''
  searchForm.userDeleteResumeUserId = undefined
  pagination.current = 1
  getResumeList()
}

// 刷新数据
const refreshData = () => {
  getResumeList()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.size = size
  getResumeList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getResumeList()
}

// 查看代码
const showCode = (row: UserDeleteResumeBySystemPageVO) => {
  currentCode.value = row.userDeleteResumeBySystemResumeReactCode || ''
  codeDialogVisible.value = true
}

// 查看详情
const viewDetail = async (row: UserDeleteResumeBySystemPageVO) => {
  try {
    const response = await systemDeleteResumeApi.getDeleteResumeDetail(row.userDeleteResumeBySystemId)
    currentResume.value = response.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 组件挂载
onMounted(() => {
  getResumeList()
})
</script>

<style scoped lang="scss">
.system-resume-management {
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

    .table-count {
      font-size: 14px;
      color: #6b7280;
    }

    .recycle-time {
      color: #e6a23c;
      font-weight: 500;
    }
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 24px;
  }
}

.resume-detail {
  .code-preview {
    margin-top: 20px;
    
    h4 {
      margin-bottom: 10px;
      color: #303133;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .system-resume-management {
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

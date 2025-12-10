<template>
  <div class="system-resume-management">
    <!-- 页头 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">系统删除简历管理</h1>
        <p class="page-description">管理系统自动删除的简历记录</p>
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
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.userName"
            placeholder="请输入用户名"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="删除原因">
          <el-select
            v-model="searchForm.reason"
            placeholder="请选择"
            clearable
            style="width: 180px"
          >
            <el-option label="用户自动删除" value="user_delete" />
            <el-option label="系统自动清理" value="system_cleanup" />
            <el-option label="违规内容" value="violation" />
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
        <span class="table-title">删除简历列表</span>
      </div>

      <el-table
        v-loading="loading"
        :data="resumeList"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="userName" label="用户" width="120" />
        <el-table-column prop="resumeTitle" label="简历标题" min-width="200" />
        <el-table-column prop="deleteReason" label="删除原因" width="130">
          <template #default="{ row }">
            <el-tag :type="getReasonTagType(row.deleteReason)" size="small">
              {{ getReasonText(row.deleteReason) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deleteTime" label="删除时间" min-width="160" />
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search, RefreshRight } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)

const searchForm = reactive({
  userName: '',
  reason: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 50
})

const resumeList = ref([
  {
    id: 1,
    userName: '张三',
    resumeTitle: 'Java开发工程师简历',
    deleteReason: 'user_delete',
    deleteTime: '2024-03-15 10:30'
  },
  {
    id: 2,
    userName: '李四',
    resumeTitle: '产品经理简历',
    deleteReason: 'system_cleanup',
    deleteTime: '2024-03-14 09:20'
  }
])

const getReasonText = (reason: string) => {
  const map: Record<string, string> = {
    'user_delete': '用户删除',
    'system_cleanup': '系统清理',
    'violation': '违规内容'
  }
  return map[reason] || reason
}

const getReasonTagType = (reason: string) => {
  const map: Record<string, string> = {
    'user_delete': 'info',
    'system_cleanup': 'warning',
    'violation': 'danger'
  }
  return map[reason] || 'info'
}

// 获取数据列表
const getResumeList = async () => {
  loading.value = true
  try {
    // TODO: 调用实际API
    // const response = await resumeApi.getDeletedResumes(
    //   pagination.current,
    //   pagination.size,
    //   searchForm
    // )
    // resumeList.value = response.data.records
    // pagination.total = response.data.total
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
  searchForm.userName = ''
  searchForm.reason = ''
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

// 查看详情
const viewDetail = (row: any) => {
  ElMessage.info('查看简历删除详情')
}

// 组件挂载
onMounted(() => {
  // getResumeList()
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
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 24px;
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
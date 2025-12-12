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

    <!-- 数据表格 -->
    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">反馈记录列表</span>
      </div>

      <el-table
        v-loading="loading"
        :data="records"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="title" label="反馈标题" min-width="200" />
        <el-table-column prop="adminName" label="提交人" min-width="120" />
        <el-table-column prop="curStep" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.curStep)" size="small">
              {{ row.curStep }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recentTime" label="最后更新" min-width="160" />
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
import { Refresh } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)

const pagination = reactive({
  current: 1,
  size: 20,
  total: 50
})

const records = ref([
  {
    id: 1,
    title: '系统功能建议',
    adminName: '张三',
    curStep: '已回复',
    recentTime: '2024-03-15 15:30'
  },
  {
    id: 2,
    title: '界面优化反馈',
    adminName: '李四',
    curStep: '已忽视',
    recentTime: '2024-03-14 10:20'
  }
])

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    '待处理': 'warning',
    '待回复': 'primary',
    '已回复': 'success',
    '已忽视': 'info'
  }
  return map[status] || 'info'
}

// 获取记录列表
const getRecordsList = async () => {
  loading.value = true
  try {
    // TODO: 调用实际API
    // const response = await feedbackApi.getFeedbackRecords(
    //   pagination.current,
    //   pagination.size
    // )
    // records.value = response.data.records
    // pagination.total = response.data.total
  } catch (error) {
    console.error('获取记录列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
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
const viewDetail = (row: any) => {
  ElMessage.info('查看反馈详情')
}

// 组件挂载
onMounted(() => {
  // getRecordsList()
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
  .feedback-records {
    .page-header {
      flex-direction: column;
      gap: 16px;
    }

    .header-actions {
      width: 100%;
      justify-content: flex-start;
    }
  }
}
</style>

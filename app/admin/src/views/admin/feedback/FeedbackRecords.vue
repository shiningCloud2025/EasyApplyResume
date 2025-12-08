<template>
  <div class="feedback-records">
    <div class="page-header">
      <h1 class="page-title">管理端反馈记录</h1>
      <p class="page-description">查看所有管理员反馈的历史记录</p>
    </div>

    <el-card>
      <el-table :data="records" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="反馈标题" min-width="200" />
        <el-table-column prop="adminName" label="提交人" width="120" />
        <el-table-column prop="curStep" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.curStep)">
              {{ row.curStep }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recentTime" label="最后更新" width="160" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="text" @click="viewDetail(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(50)

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
  const map = {
    '待处理': 'warning',
    '待回复': 'primary',
    '已回复': 'success',
    '已忽视': 'info'
  }
  return map[status] || 'info'
}

const viewDetail = (row: any) => {
  ElMessage.info('查看反馈详情')
}
</script>

<style scoped lang="scss">
.feedback-records {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
  
  h1 {
    font-size: 28px;
    font-weight: 700;
    color: #1f2937;
    margin-bottom: 8px;
  }
  
  p {
    color: #6b7280;
    margin: 0;
  }
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
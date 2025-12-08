<template>
  <div class="user-feedback-records">
    <div class="page-header">
      <h1 class="page-title">用户端反馈记录</h1>
      <p class="page-description">查看所有用户反馈的历史记录</p>
    </div>

    <el-card>
      <el-form inline class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.userName" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="待处理" value="pending" />
            <el-option label="待回复" value="waiting_reply" />
            <el-option label="已回复" value="replied" />
            <el-option label="已忽视" value="ignored" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">搜索</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="records" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="反馈标题" min-width="200" />
        <el-table-column prop="userName" label="用户名" width="120" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="160" />
        <el-table-column prop="recentTime" label="最后更新" width="160" />
        <el-table-column label="操作" width="100" fixed="right">
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
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const searchForm = reactive({
  userName: '',
  status: ''
})

const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(100)

const records = ref([
  {
    id: 1,
    title: '功能建议',
    userName: '用户A',
    status: 'replied',
    createTime: '2024-03-15 10:30',
    recentTime: '2024-03-15 15:30'
  },
  {
    id: 2,
    title: 'Bug反馈',
    userName: '用户B', 
    status: 'ignored',
    createTime: '2024-03-14 09:20',
    recentTime: '2024-03-14 10:15'
  }
])

const getStatusType = (status: string) => {
  const map = {
    'pending': 'warning',
    'waiting_reply': 'primary',
    'replied': 'success',
    'ignored': 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status: string) => {
  const map = {
    'pending': '待处理',
    'waiting_reply': '待回复',
    'replied': '已回复',
    'ignored': '已忽视'
  }
  return map[status] || status
}

const viewDetail = (row: any) => {
  ElMessage.info('查看反馈详情')
}
</script>

<style scoped lang="scss">
.user-feedback-records {
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

.search-form {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
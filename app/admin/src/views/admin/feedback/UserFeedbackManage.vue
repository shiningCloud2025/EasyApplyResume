<template>
  <div class="user-feedback-manage">
    <div class="page-header">
      <h1 class="page-title">用户端反馈管理</h1>
      <p class="page-description">处理用户提交的反馈信息</p>
    </div>

    <el-card>
      <el-form inline class="search-form">
        <el-form-item label="反馈标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="处理状态">
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
      
      <el-table :data="feedbackList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="userName" label="用户" width="120" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">详情</el-button>
            <el-button 
              v-if="row.status === 'pending'"
              type="text" 
              @click="handleProcess(row)"
            >
              处理
            </el-button>
            <el-button 
              v-if="row.status === 'waiting_reply'"
              type="text" 
              @click="handleReply(row)"
            >
              回复
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const searchForm = reactive({
  title: '',
  status: ''
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

const handleDetail = (row: any) => {
  ElMessage.info('查看用户反馈详情')
}

const handleProcess = (row: any) => {
  ElMessage.success('处理反馈')
}

const handleReply = (row: any) => {
  ElMessage.success('回复反馈')
}
</script>

<style scoped lang="scss">
.user-feedback-manage {
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
</style>
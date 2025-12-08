<template>
  <div class="system-resume-management">
    <div class="page-header">
      <h1 class="page-title">系统删除简历管理</h1>
      <p class="page-description">管理系统自动删除的简历记录</p>
    </div>

    <el-card>
      <el-form inline class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.userName" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="删除原因">
          <el-select v-model="searchForm.reason" placeholder="请选择" clearable>
            <el-option label="用户自动删除" value="user_delete" />
            <el-option label="系统自动清理" value="system_cleanup" />
            <el-option label="违规内容" value="violation" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">搜索</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="resumeList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userName" label="用户" width="120" />
        <el-table-column prop="resumeTitle" label="简历标题" min-width="200" />
        <el-table-column prop="deleteReason" label="删除原因" width="150">
          <template #default="{ row }">
            {{ getReasonText(row.deleteReason) }}
          </template>
        </el-table-column>
        <el-table-column prop="deleteTime" label="删除时间" width="160" />
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
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const searchForm = reactive({
  userName: '',
  reason: ''
})

const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(50)

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
  const map = {
    'user_delete': '用户自动删除',
    'system_cleanup': '系统自动清理',
    'violation': '违规内容'
  }
  return map[reason] || reason
}

const viewDetail = (row: any) => {
  ElMessage.info('查看简历删除详情')
}
</script>

<style scoped lang="scss">
.system-resume-management {
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
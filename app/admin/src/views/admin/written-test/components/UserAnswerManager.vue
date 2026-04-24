<template>
  <div class="user-answer-management">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">用户答题管理</h2>
        <p class="page-description">按用户查看笔试专项答题记录，支持分页查询、详情查看与删除。</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="用户ID">
          <el-input
            v-model="searchForm.userId"
            placeholder="请输入用户ID"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="题库大类">
          <el-input
            v-model="searchForm.questionFirstCategoryName"
            placeholder="请输入题库大类名称"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="题库小类">
          <el-input
            v-model="searchForm.questionSecondCategoryName"
            placeholder="请输入题库小类名称"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        :empty-text="emptyText"
      >
        <el-table-column prop="firstCategoryQuestionBankUserUserId" label="用户ID" width="120" align="center" />
        <el-table-column prop="firstCategoryQuestionBankUserQuestionBankId" label="题目ID" width="120" align="center" />
        <el-table-column
          prop="firstCategoryQuestionBankUserQuestionFirstCategoryName"
          label="题库大类"
          min-width="180"
          show-overflow-tooltip
        />
        <el-table-column
          prop="firstCategoryQuestionBankUserQuestionSecondCategoryName"
          label="题库小类"
          min-width="180"
          show-overflow-tooltip
        />
        <el-table-column label="答题状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getAnswerStatusTagType(row.firstCategoryQuestionBankUserAnswerStatus)">
              {{ getAnswerStatusLabel(row.firstCategoryQuestionBankUserAnswerStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="firstCategoryQuestionBankUserCreateTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.firstCategoryQuestionBankUserCreateTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="firstCategoryQuestionBankUserUpdateTime" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.firstCategoryQuestionBankUserUpdateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="info" size="default" @click="handleView(row)">详情</el-button>
            <el-button type="danger" size="default" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination"
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <el-dialog v-model="showViewDialog" title="用户答题详情" width="760px">
      <div class="user-answer-detail" v-if="currentDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">
            <el-tag type="primary">{{ currentDetail.firstCategoryQuestionBankUserUserId }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="题目ID">
            <el-tag>{{ currentDetail.firstCategoryQuestionBankUserQuestionBankId }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="题库大类ID">
            {{ currentDetail.firstCategoryQuestionBankUserQuestionFirstCategoryId }}
          </el-descriptions-item>
          <el-descriptions-item label="题库大类名称">
            {{ displayText(currentDetail.firstCategoryQuestionBankUserQuestionFirstCategoryName) }}
          </el-descriptions-item>
          <el-descriptions-item label="题库小类ID">
            {{ currentDetail.firstCategoryQuestionBankUserQuestionSecondCategoryId }}
          </el-descriptions-item>
          <el-descriptions-item label="题库小类名称">
            {{ displayText(currentDetail.firstCategoryQuestionBankUserQuestionSecondCategoryName) }}
          </el-descriptions-item>
          <el-descriptions-item label="答题状态">
            <el-tag :type="getAnswerStatusTagType(currentDetail.firstCategoryQuestionBankUserAnswerStatus)">
              {{ getAnswerStatusLabel(currentDetail.firstCategoryQuestionBankUserAnswerStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(currentDetail.firstCategoryQuestionBankUserCreateTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" :span="2">
            {{ formatDateTime(currentDetail.firstCategoryQuestionBankUserUpdateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showViewDialog = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { firstCategoryQuestionBankUserApi } from '@/api/admin'
import type {
  AdminUserFirstCategoryQuestionBankInfoVO,
  AdminUserFirstCategoryQuestionBankKey,
  AdminUserFirstCategoryQuestionBankPageVO,
  AdminUserFirstCategoryQuestionBankQuery
} from '@/types/admin'

interface SearchFormState {
  userId: string
  questionFirstCategoryName: string
  questionSecondCategoryName: string
}

const loading = ref(false)
const showViewDialog = ref(false)
const currentDetail = ref<AdminUserFirstCategoryQuestionBankInfoVO | null>(null)
const hasSearched = ref(false)

const searchForm = reactive<SearchFormState>({
  userId: '',
  questionFirstCategoryName: '',
  questionSecondCategoryName: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<AdminUserFirstCategoryQuestionBankPageVO[]>([])

const emptyText = computed(() => {
  return hasSearched.value ? '暂无用户答题数据' : '暂无用户答题数据'
})

const getAnswerStatusLabel = (value?: number) => {
  if (value === 0) {
    return '未作答'
  }
  if (value === 1) {
    return '已做错'
  }
  if (value === 2) {
    return '已做对'
  }
  return '-'
}

const getAnswerStatusTagType = (value?: number) => {
  if (value === 0) {
    return 'info'
  }
  if (value === 1) {
    return 'danger'
  }
  if (value === 2) {
    return 'success'
  }
  return 'info'
}

const displayText = (value?: string) => {
  return value?.trim() ? value : '-'
}

const buildQuery = (): AdminUserFirstCategoryQuestionBankQuery => {
  const trimmedUserId = searchForm.userId.trim()

  if (trimmedUserId && !/^\d+$/.test(trimmedUserId)) {
    ElMessage.warning('用户ID必须为正整数')
    throw new Error('INVALID_USER_ID')
  }

  return {
    userId: trimmedUserId ? Number(trimmedUserId) : undefined,
    questionFirstCategoryName: searchForm.questionFirstCategoryName.trim() || undefined,
    questionSecondCategoryName: searchForm.questionSecondCategoryName.trim() || undefined
  }
}

const getList = async () => {
  let query: AdminUserFirstCategoryQuestionBankQuery

  try {
    query = buildQuery()
  } catch (error) {
    return
  }

  loading.value = true
  hasSearched.value = true

  try {
    const response = await firstCategoryQuestionBankUserApi.getPage(
      pagination.current,
      pagination.size,
      query
    )
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取用户答题列表失败:', error)
    tableData.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

const refreshData = () => {
  getList()
}

const handleSearch = () => {
  pagination.current = 1
  getList()
}

const handleReset = () => {
  searchForm.userId = ''
  searchForm.questionFirstCategoryName = ''
  searchForm.questionSecondCategoryName = ''
  pagination.current = 1
  pagination.total = 0
  tableData.value = []
  hasSearched.value = false
  getList()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  if (hasSearched.value) {
    getList()
  }
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  if (hasSearched.value) {
    getList()
  }
}

const toRowKey = (row: AdminUserFirstCategoryQuestionBankPageVO): AdminUserFirstCategoryQuestionBankKey => ({
  userId: row.firstCategoryQuestionBankUserUserId,
  questionBankId: row.firstCategoryQuestionBankUserQuestionBankId
})

const handleView = async (row: AdminUserFirstCategoryQuestionBankPageVO) => {
  try {
    const response = await firstCategoryQuestionBankUserApi.getInfo(toRowKey(row))
    currentDetail.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取用户答题详情失败:', error)
  }
}

const handleDelete = async (row: AdminUserFirstCategoryQuestionBankPageVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 ${row.firstCategoryQuestionBankUserUserId} 的题目 ${row.firstCategoryQuestionBankUserQuestionBankId} 答题记录吗？`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await firstCategoryQuestionBankUserApi.remove(toRowKey(row))
    ElMessage.success('删除用户答题记录成功')

    if (tableData.value.length === 1 && pagination.current > 1) {
      pagination.current -= 1
    }

    getList()
  } catch (error) {
    if (error === 'cancel' || error === 'close') {
      return
    }
    console.error('删除用户答题记录失败:', error)
  }
}

getList()
</script>

<style scoped lang="scss">
.user-answer-management {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 24px;
    gap: 16px;
  }

  .header-content {
    .page-title {
      margin: 0 0 8px;
      font-size: 24px;
      font-weight: 700;
      color: #111827;
    }

    .page-description {
      margin: 0;
      color: #6b7280;
      font-size: 14px;
      line-height: 1.6;
    }
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    :deep(.el-card__body) {
      display: flex;
      flex-direction: column;
      gap: 20px;
    }
  }

  .pagination {
    justify-content: flex-end;
  }
}

@media (max-width: 768px) {
  .user-answer-management {
    .page-header {
      flex-direction: column;
      justify-content: stretch;
    }

    .header-actions {
      width: 100%;
      flex-direction: column;
    }

    .search-form {
      :deep(.el-form-item) {
        width: 100%;
        margin-right: 0;
      }

      :deep(.el-input) {
        width: 100% !important;
      }
    }
  }
}
</style>

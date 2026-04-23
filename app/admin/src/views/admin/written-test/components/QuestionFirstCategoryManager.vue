<template>
  <div class="question-first-category-management">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">题库大类管理</h2>
        <p class="page-description">维护笔试专项下的题库大类，作为后续题库小类与题库题目的业务入口。</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <el-icon><Plus /></el-icon>
          新增大类
        </el-button>
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="大类名称">
          <el-input
            v-model="searchForm.questionFirstCategoryName"
            placeholder="请输入题库大类名称"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="大类介绍">
          <el-input
            v-model="searchForm.questionFirstCategoryIntroduce"
            placeholder="请输入题库大类介绍"
            clearable
            style="width: 260px"
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
        empty-text="暂无题库大类数据"
      >
        <el-table-column prop="questionFirstCategoryId" label="题库大类ID" width="140" align="center" />
        <el-table-column prop="questionFirstCategoryName" label="大类名称" min-width="180" />
        <el-table-column prop="questionFirstCategoryIntroduce" label="大类介绍" min-width="260" show-overflow-tooltip />
        <el-table-column prop="questionFirstCategoryCreateTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.questionFirstCategoryCreateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="info" size="default" @click="handleView(row)">查看</el-button>
            <el-button type="primary" size="default" @click="handleEdit(row)">编辑</el-button>
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

    <el-dialog
      v-model="showCreateDialog"
      :title="editingQuestionFirstCategory ? '编辑题库大类' : '新增题库大类'"
      width="640px"
      @close="resetForm"
    >
      <el-form
        ref="questionFirstCategoryFormRef"
        :model="questionFirstCategoryForm"
        :rules="questionFirstCategoryRules"
        label-width="100px"
      >
        <el-form-item label="大类ID" v-if="editingQuestionFirstCategory">
          <el-input v-model="questionFirstCategoryForm.questionFirstCategoryId" disabled />
        </el-form-item>

        <el-form-item label="大类名称" prop="questionFirstCategoryName">
          <el-input
            v-model="questionFirstCategoryForm.questionFirstCategoryName"
            placeholder="请输入题库大类名称"
            maxlength="20"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="大类介绍" prop="questionFirstCategoryIntroduce">
          <el-input
            v-model="questionFirstCategoryForm.questionFirstCategoryIntroduce"
            type="textarea"
            :rows="4"
            placeholder="请输入题库大类介绍"
            maxlength="60"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showCreateDialog = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="showViewDialog" title="题库大类详情" width="640px">
      <div class="question-first-category-detail" v-if="currentViewQuestionFirstCategory">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="题库大类ID">
            <el-tag type="primary">{{ currentViewQuestionFirstCategory.questionFirstCategoryId }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="大类名称">
            {{ currentViewQuestionFirstCategory.questionFirstCategoryName }}
          </el-descriptions-item>
          <el-descriptions-item label="大类介绍">
            {{ currentViewQuestionFirstCategory.questionFirstCategoryIntroduce }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(currentViewQuestionFirstCategory.questionFirstCategoryCreateTime) }}
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
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { formatDate } from '@/utils'
import { questionFirstCategoryApi } from '@/api/admin'
import type {
  AdminQuestionFirstCategoryForm,
  AdminQuestionFirstCategoryInfoVO,
  AdminQuestionFirstCategoryPageVO,
  AdminQuestionFirstCategoryQuery
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showViewDialog = ref(false)
const editingQuestionFirstCategory = ref<AdminQuestionFirstCategoryPageVO | null>(null)
const currentViewQuestionFirstCategory = ref<AdminQuestionFirstCategoryInfoVO | null>(null)

const searchForm = reactive<AdminQuestionFirstCategoryQuery>({
  questionFirstCategoryName: '',
  questionFirstCategoryIntroduce: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<AdminQuestionFirstCategoryPageVO[]>([])

const questionFirstCategoryFormRef = ref<FormInstance>()
const questionFirstCategoryForm = reactive<AdminQuestionFirstCategoryForm>({
  questionFirstCategoryId: undefined,
  questionFirstCategoryName: '',
  questionFirstCategoryIntroduce: ''
})

const questionFirstCategoryRules = {
  questionFirstCategoryName: [
    { required: true, message: '请输入题库大类名称', trigger: 'blur' },
    { max: 20, message: '题库大类名称不能超过20个字符', trigger: 'blur' }
  ],
  questionFirstCategoryIntroduce: [
    { required: true, message: '请输入题库大类介绍', trigger: 'blur' },
    { max: 60, message: '题库大类介绍不能超过60个字符', trigger: 'blur' }
  ]
}

const getQuestionFirstCategoryList = async () => {
  loading.value = true
  try {
    const response = await questionFirstCategoryApi.getQuestionFirstCategoryPage(
      pagination.current,
      pagination.size,
      searchForm
    )

    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取题库大类列表失败:', error)
    ElMessage.error('加载题库大类数据失败')
  } finally {
    loading.value = false
  }
}

const openCreateDialog = () => {
  editingQuestionFirstCategory.value = null
  resetForm()
  showCreateDialog.value = true
}

const handleSearch = () => {
  pagination.current = 1
  getQuestionFirstCategoryList()
}

const handleReset = () => {
  searchForm.questionFirstCategoryName = ''
  searchForm.questionFirstCategoryIntroduce = ''
  pagination.current = 1
  getQuestionFirstCategoryList()
}

const refreshData = () => {
  getQuestionFirstCategoryList()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  getQuestionFirstCategoryList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getQuestionFirstCategoryList()
}

const handleView = async (row: AdminQuestionFirstCategoryPageVO) => {
  try {
    const response = await questionFirstCategoryApi.getQuestionFirstCategoryInfo(row.questionFirstCategoryId)
    currentViewQuestionFirstCategory.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取题库大类详情失败:', error)
    ElMessage.error('获取题库大类详情失败')
  }
}

const handleEdit = async (row: AdminQuestionFirstCategoryPageVO) => {
  try {
    const response = await questionFirstCategoryApi.getQuestionFirstCategoryInfo(row.questionFirstCategoryId)
    const detail = response.data

    editingQuestionFirstCategory.value = row
    Object.assign(questionFirstCategoryForm, {
      questionFirstCategoryId: detail.questionFirstCategoryId,
      questionFirstCategoryName: detail.questionFirstCategoryName,
      questionFirstCategoryIntroduce: detail.questionFirstCategoryIntroduce
    })
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取题库大类详情失败:', error)
    ElMessage.error('获取题库大类详情失败')
  }
}

const handleDelete = async (row: AdminQuestionFirstCategoryPageVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除题库大类“${row.questionFirstCategoryName}”吗？删除后将联动删除该大类下的小类及相关题目数据，请谨慎操作。`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await questionFirstCategoryApi.deleteQuestionFirstCategory(row.questionFirstCategoryId)
    ElMessage.success('删除题库大类成功')

    if (tableData.value.length === 1 && pagination.current > 1) {
      pagination.current -= 1
    }
    getQuestionFirstCategoryList()
  } catch (error) {
    if (error === 'cancel' || error === 'close') {
      return
    }
    console.error('删除题库大类失败:', error)
  }
}

const handleSubmit = async () => {
  if (!questionFirstCategoryFormRef.value) return

  try {
    await questionFirstCategoryFormRef.value.validate()
    submitting.value = true

    const payload: AdminQuestionFirstCategoryForm = {
      questionFirstCategoryId: questionFirstCategoryForm.questionFirstCategoryId,
      questionFirstCategoryName: questionFirstCategoryForm.questionFirstCategoryName.trim(),
      questionFirstCategoryIntroduce: questionFirstCategoryForm.questionFirstCategoryIntroduce.trim()
    }

    if (editingQuestionFirstCategory.value) {
      await questionFirstCategoryApi.updateQuestionFirstCategory(payload)
      ElMessage.success('修改题库大类成功')
    } else {
      await questionFirstCategoryApi.addQuestionFirstCategory({
        questionFirstCategoryName: payload.questionFirstCategoryName,
        questionFirstCategoryIntroduce: payload.questionFirstCategoryIntroduce
      })
      ElMessage.success('新增题库大类成功')
    }

    showCreateDialog.value = false
    getQuestionFirstCategoryList()
  } catch (error) {
    console.error('提交题库大类表单失败:', error)
  } finally {
    submitting.value = false
  }
}

const resetForm = () => {
  Object.assign(questionFirstCategoryForm, {
    questionFirstCategoryId: undefined,
    questionFirstCategoryName: '',
    questionFirstCategoryIntroduce: ''
  })
  questionFirstCategoryFormRef.value?.clearValidate()
}

onMounted(() => {
  getQuestionFirstCategoryList()
})

defineExpose({
  refreshData,
  openCreateDialog
})
</script>

<style scoped lang="scss">
.question-first-category-management {
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
  .question-first-category-management {
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

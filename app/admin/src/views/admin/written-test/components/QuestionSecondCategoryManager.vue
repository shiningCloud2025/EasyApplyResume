<template>
  <div class="question-second-category-management">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">题库小类管理</h2>
        <p class="page-description">维护题库小类，建立题目在业务分类中的归属关系。</p>
      </div>
      <div class="header-actions">
        <el-button v-if="canAddQuestionSecondCategory" type="primary" @click="openCreateDialog">
          <el-icon><Plus /></el-icon>
          新增小类
        </el-button>
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="所属大类ID">
          <el-input-number
            v-model="searchForm.questionFirstCategoryId"
            placeholder="请输入所属大类ID"
            :controls="false"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="小类名称">
          <el-input
            v-model="searchForm.questionSecondCategoryName"
            placeholder="请输入题库小类名称"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="小类介绍">
          <el-input
            v-model="searchForm.questionSecondCategoryIntroduce"
            placeholder="请输入题库小类介绍"
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
        empty-text="暂无题库小类数据"
      >
        <el-table-column prop="questionSecondCategoryId" label="题库小类ID" width="140" align="center" />
        <el-table-column prop="questionFirstCategoryId" label="所属大类ID" width="140" align="center" />
        <el-table-column prop="questionSecondCategoryName" label="小类名称" min-width="180" />
        <el-table-column prop="questionSecondCategoryIntroduce" label="小类介绍" min-width="260" show-overflow-tooltip />
        <el-table-column prop="questionSecondCategoryCreateTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.questionSecondCategoryCreateTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="showQuestionSecondCategoryActionColumn" label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canViewQuestionSecondCategoryInfo" type="info" size="default" @click="handleView(row)">查看</el-button>
            <el-button v-if="canUpdateQuestionSecondCategory" type="primary" size="default" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="canDeleteQuestionSecondCategory" type="danger" size="default" @click="handleDelete(row)">删除</el-button>
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
      :title="editingQuestionSecondCategory ? '编辑题库小类' : '新增题库小类'"
      width="640px"
      @close="resetForm"
    >
      <el-form
        ref="questionSecondCategoryFormRef"
        :model="questionSecondCategoryForm"
        :rules="questionSecondCategoryRules"
        label-width="100px"
      >
        <el-form-item label="小类ID" v-if="editingQuestionSecondCategory">
          <el-input v-model="questionSecondCategoryForm.questionSecondCategoryId" disabled />
        </el-form-item>

        <el-form-item label="所属大类" prop="questionFirstCategoryId">
          <el-select
            v-model="questionSecondCategoryForm.questionFirstCategoryId"
            placeholder="请选择所属大类"
            filterable
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in questionFirstCategoryOptions"
              :key="item.questionFirstCategoryId"
              :label="item.questionFirstCategoryName"
              :value="item.questionFirstCategoryId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="小类名称" prop="questionSecondCategoryName">
          <el-input
            v-model="questionSecondCategoryForm.questionSecondCategoryName"
            placeholder="请输入题库小类名称"
            maxlength="20"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="小类介绍" prop="questionSecondCategoryIntroduce">
          <el-input
            v-model="questionSecondCategoryForm.questionSecondCategoryIntroduce"
            type="textarea"
            :rows="4"
            placeholder="请输入题库小类介绍"
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

    <el-dialog v-model="showViewDialog" title="题库小类详情" width="640px">
      <div class="question-second-category-detail" v-if="currentViewQuestionSecondCategory">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="题库小类ID">
            <el-tag type="primary">{{ currentViewQuestionSecondCategory.questionSecondCategoryId }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="所属大类ID">
            <el-tag type="success">{{ currentViewQuestionSecondCategory.questionFirstCategoryId }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="小类名称">
            {{ currentViewQuestionSecondCategory.questionSecondCategoryName }}
          </el-descriptions-item>
          <el-descriptions-item label="小类介绍">
            {{ currentViewQuestionSecondCategory.questionSecondCategoryIntroduce }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(currentViewQuestionSecondCategory.questionSecondCategoryCreateTime) }}
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
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { formatDate } from '@/utils'
import { questionFirstCategoryApi, questionSecondCategoryApi } from '@/api/admin'
import { useAuthStore } from '@/store/auth'
import type {
  AdminQuestionFirstCategoryInfoVO,
  AdminQuestionSecondCategoryForm,
  AdminQuestionSecondCategoryInfoVO,
  AdminQuestionSecondCategoryPageVO,
  AdminQuestionSecondCategoryQuery
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

const authStore = useAuthStore()
const canAddQuestionSecondCategory = computed(() => authStore.hasPermission('/admin/questionSecondCategory/addQuestionSecondCategory'))
const canViewQuestionSecondCategoryInfo = computed(() => authStore.hasPermission('/admin/questionSecondCategory/findQuestionSecondCategoryById'))
const canUpdateQuestionSecondCategory = computed(() => authStore.hasPermission('/admin/questionSecondCategory/updateQuestionSecondCategory'))
const canDeleteQuestionSecondCategory = computed(() => authStore.hasPermission('/admin/questionSecondCategory/deleteQuestionSecondCategory'))
const showQuestionSecondCategoryActionColumn = computed(() => {
  return canViewQuestionSecondCategoryInfo.value || canUpdateQuestionSecondCategory.value || canDeleteQuestionSecondCategory.value
})

const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showViewDialog = ref(false)
const editingQuestionSecondCategory = ref<AdminQuestionSecondCategoryPageVO | null>(null)
const currentViewQuestionSecondCategory = ref<AdminQuestionSecondCategoryInfoVO | null>(null)

const searchForm = reactive<AdminQuestionSecondCategoryQuery>({
  questionFirstCategoryId: undefined,
  questionSecondCategoryName: '',
  questionSecondCategoryIntroduce: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<AdminQuestionSecondCategoryPageVO[]>([])
const questionFirstCategoryOptions = ref<AdminQuestionFirstCategoryInfoVO[]>([])

const questionSecondCategoryFormRef = ref<FormInstance>()
const questionSecondCategoryForm = reactive<AdminQuestionSecondCategoryForm>({
  questionSecondCategoryId: undefined,
  questionFirstCategoryId: undefined,
  questionSecondCategoryName: '',
  questionSecondCategoryIntroduce: ''
})

const questionSecondCategoryRules = {
  questionFirstCategoryId: [
    { required: true, message: '请选择所属大类', trigger: 'change' }
  ],
  questionSecondCategoryName: [
    { required: true, message: '请输入题库小类名称', trigger: 'blur' },
    { max: 20, message: '题库小类名称不能超过20个字符', trigger: 'blur' }
  ],
  questionSecondCategoryIntroduce: [
    { required: true, message: '请输入题库小类介绍', trigger: 'blur' },
    { max: 60, message: '题库小类介绍不能超过60个字符', trigger: 'blur' }
  ]
}

const getQuestionSecondCategoryList = async () => {
  loading.value = true
  try {
    const response = await questionSecondCategoryApi.getQuestionSecondCategoryPage(
      pagination.current,
      pagination.size,
      searchForm
    )

    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取题库小类列表失败:', error)
    ElMessage.error('加载题库小类数据失败')
  } finally {
    loading.value = false
  }
}

const getQuestionFirstCategoryOptions = async () => {
  try {
    const response = await questionFirstCategoryApi.getAllQuestionFirstCategory()
    questionFirstCategoryOptions.value = response.data || []
  } catch (error) {
    console.error('获取题库大类列表失败:', error)
    ElMessage.error('加载题库大类选项失败')
  }
}

const openCreateDialog = () => {
  editingQuestionSecondCategory.value = null
  resetForm()
  showCreateDialog.value = true
}

const handleSearch = () => {
  pagination.current = 1
  getQuestionSecondCategoryList()
}

const handleReset = () => {
  searchForm.questionFirstCategoryId = undefined
  searchForm.questionSecondCategoryName = ''
  searchForm.questionSecondCategoryIntroduce = ''
  pagination.current = 1
  getQuestionSecondCategoryList()
}

const refreshData = () => {
  getQuestionSecondCategoryList()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  getQuestionSecondCategoryList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getQuestionSecondCategoryList()
}

const handleView = async (row: AdminQuestionSecondCategoryPageVO) => {
  try {
    const response = await questionSecondCategoryApi.getQuestionSecondCategoryInfo(row.questionSecondCategoryId)
    currentViewQuestionSecondCategory.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取题库小类详情失败:', error)
    ElMessage.error('获取题库小类详情失败')
  }
}

const handleEdit = async (row: AdminQuestionSecondCategoryPageVO) => {
  try {
    const response = await questionSecondCategoryApi.getQuestionSecondCategoryInfo(row.questionSecondCategoryId)
    const detail = response.data

    editingQuestionSecondCategory.value = row
    Object.assign(questionSecondCategoryForm, {
      questionSecondCategoryId: detail.questionSecondCategoryId,
      questionFirstCategoryId: detail.questionFirstCategoryId,
      questionSecondCategoryName: detail.questionSecondCategoryName,
      questionSecondCategoryIntroduce: detail.questionSecondCategoryIntroduce
    })
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取题库小类详情失败:', error)
    ElMessage.error('获取题库小类详情失败')
  }
}

const handleDelete = async (row: AdminQuestionSecondCategoryPageVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除题库小类“${row.questionSecondCategoryName}”吗？删除后将联动删除该小类下的相关题目数据，请谨慎操作。`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await questionSecondCategoryApi.deleteQuestionSecondCategory(row.questionSecondCategoryId)
    ElMessage.success('删除题库小类成功')

    if (tableData.value.length === 1 && pagination.current > 1) {
      pagination.current -= 1
    }
    getQuestionSecondCategoryList()
  } catch (error) {
    if (error === 'cancel' || error === 'close') {
      return
    }
    console.error('删除题库小类失败:', error)
  }
}

const handleSubmit = async () => {
  if (!questionSecondCategoryFormRef.value) return

  try {
    await questionSecondCategoryFormRef.value.validate()
    submitting.value = true

    const payload: AdminQuestionSecondCategoryForm = {
      questionSecondCategoryId: questionSecondCategoryForm.questionSecondCategoryId,
      questionFirstCategoryId: questionSecondCategoryForm.questionFirstCategoryId,
      questionSecondCategoryName: questionSecondCategoryForm.questionSecondCategoryName.trim(),
      questionSecondCategoryIntroduce: questionSecondCategoryForm.questionSecondCategoryIntroduce.trim()
    }

    if (editingQuestionSecondCategory.value) {
      await questionSecondCategoryApi.updateQuestionSecondCategory(payload)
      ElMessage.success('修改题库小类成功')
    } else {
      await questionSecondCategoryApi.addQuestionSecondCategory({
        questionFirstCategoryId: payload.questionFirstCategoryId,
        questionSecondCategoryName: payload.questionSecondCategoryName,
        questionSecondCategoryIntroduce: payload.questionSecondCategoryIntroduce
      })
      ElMessage.success('新增题库小类成功')
    }

    showCreateDialog.value = false
    getQuestionSecondCategoryList()
  } catch (error) {
    console.error('提交题库小类表单失败:', error)
  } finally {
    submitting.value = false
  }
}

const resetForm = () => {
  Object.assign(questionSecondCategoryForm, {
    questionSecondCategoryId: undefined,
    questionFirstCategoryId: undefined,
    questionSecondCategoryName: '',
    questionSecondCategoryIntroduce: ''
  })
  questionSecondCategoryFormRef.value?.clearValidate()
}

onMounted(() => {
  getQuestionFirstCategoryOptions()
  getQuestionSecondCategoryList()
})

defineExpose({
  refreshData,
  openCreateDialog
})
</script>

<style scoped lang="scss">
.question-second-category-management {
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
  .question-second-category-management {
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

      :deep(.el-input),
      :deep(.el-input-number) {
        width: 100% !important;
      }
    }
  }
}
</style>

<template>
  <div class="score-training-data-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">训练数据管理</h1>
        <p class="page-description">管理简历评分训练数据，支持新增、查询、详情查看、删除和导出。</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData" :icon="Refresh">
          刷新
        </el-button>
        <el-button v-if="canExportScoreTrainingData" type="success" @click="handleExport" :loading="exporting">
          <el-icon><Download /></el-icon>
          导出训练数据
        </el-button>
        <el-button v-if="canAddScoreTrainingData" type="primary" @click="openCreateDialog" :icon="Plus">
          新增训练数据
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item label="简历名称">
          <el-input
            v-model="queryForm.scoreTrainingDataResumeName"
            placeholder="请输入简历名称"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="行业名称">
          <el-input
            v-model="queryForm.scoreTrainingDataIndustryName"
            placeholder="请输入行业名称"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="数据来源">
          <el-select
            v-model="queryForm.scoreTrainingDataDataSource"
            placeholder="请选择数据来源"
            clearable
            style="width: 180px"
          >
            <el-option label="人工" :value="0" />
            <el-option label="模型" :value="1" />
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

    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">训练数据列表</span>
        <span class="table-count">共 {{ pagination.total }} 条记录</span>
      </div>

      <el-table v-loading="loading" :data="tableData" style="width: 100%">
        <el-table-column prop="scoreTrainingDataId" label="训练数据ID" width="110" />
        <el-table-column prop="scoreTrainingDataResumeName" label="简历名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="scoreTrainingDataIndustryName" label="行业名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="scoreTrainingDataLabelScore" label="训练标签分数" width="140" />
        <el-table-column label="数据来源" width="120">
          <template #default="{ row }">
            <el-tag :type="row.scoreTrainingDataDataSource === 0 ? 'success' : 'warning'" size="small">
              {{ getDataSourceText(row.scoreTrainingDataDataSource) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.scoreTrainingDataCreateTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="showScoreTrainingDataActionColumn" label="操作" width="170" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canViewScoreTrainingDataDetail" type="info" size="default" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button v-if="canDeleteScoreTrainingData" type="danger" size="default" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <el-dialog
      v-model="dialogVisible"
      title="新增训练数据"
      width="760px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="简历名称" prop="scoreTrainingDataResumeName">
          <el-input v-model="form.scoreTrainingDataResumeName" placeholder="请输入简历名称" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="行业名称" prop="scoreTrainingDataIndustryName">
          <el-input v-model="form.scoreTrainingDataIndustryName" placeholder="请输入行业名称" maxlength="35" show-word-limit />
        </el-form-item>
        <el-form-item label="训练标签分数" prop="scoreTrainingDataLabelScore">
          <el-input-number
            v-model="form.scoreTrainingDataLabelScore"
            :min="0"
            :max="100"
            :precision="2"
            :step="0.5"
            controls-position="right"
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="数据来源" prop="scoreTrainingDataDataSource">
          <el-radio-group v-model="form.scoreTrainingDataDataSource">
            <el-radio :label="0">人工</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="简历内容" prop="scoreTrainingDataResumeContent">
          <el-input
            v-model="form.scoreTrainingDataResumeContent"
            type="textarea"
            :rows="12"
            placeholder="请输入简历内容（React代码/文本）"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="训练数据详情" width="900px">
      <div v-if="currentDetail" class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="训练数据ID">
            {{ currentDetail.scoreTrainingDataId }}
          </el-descriptions-item>
          <el-descriptions-item label="数据来源">
            <el-tag :type="currentDetail.scoreTrainingDataDataSource === 0 ? 'success' : 'warning'">
              {{ getDataSourceText(currentDetail.scoreTrainingDataDataSource) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="简历名称" :span="2">
            {{ currentDetail.scoreTrainingDataResumeName }}
          </el-descriptions-item>
          <el-descriptions-item label="行业名称">
            {{ currentDetail.scoreTrainingDataIndustryName }}
          </el-descriptions-item>
          <el-descriptions-item label="训练标签分数">
            {{ currentDetail.scoreTrainingDataLabelScore }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">
            {{ formatDateTime(currentDetail.scoreTrainingDataCreateTime) }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="resume-content-section">
          <div class="section-title">简历内容</div>
          <el-input
            :model-value="currentDetail.scoreTrainingDataResumeContent || ''"
            type="textarea"
            readonly
            :autosize="{ minRows: 12, maxRows: 20 }"
          />
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Download, Plus, Refresh, RefreshRight, Search } from '@element-plus/icons-vue'
import { scoreTrainingDataApi } from '@/api/admin'
import { useAuthStore } from '@/store/auth'
import { formatDateTime } from '@/utils'
import type {
  AdminScoreTrainingDataForm,
  AdminScoreTrainingDataInfoVO,
  AdminScoreTrainingDataPageVO,
  AdminScoreTrainingDataQuery
} from '@/types/admin'
import type { FormInstance, FormRules } from 'element-plus'

const authStore = useAuthStore()
const canAddScoreTrainingData = computed(() => authStore.hasPermission('/admin/scoreTrainingData/addScoreTrainingData'))
const canViewScoreTrainingDataDetail = computed(() => authStore.hasPermission('/admin/scoreTrainingData/findScoreTrainingDataById'))
const canDeleteScoreTrainingData = computed(() => authStore.hasPermission('/admin/scoreTrainingData/deleteScoreTrainingData'))
const canExportScoreTrainingData = computed(() => authStore.hasPermission('/admin/scoreTrainingData/exportScoreTrainingData'))
const showScoreTrainingDataActionColumn = computed(() => {
  return canViewScoreTrainingDataDetail.value || canDeleteScoreTrainingData.value
})

const loading = ref(false)
const submitting = ref(false)
const exporting = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const formRef = ref<FormInstance>()
const currentDetail = ref<AdminScoreTrainingDataInfoVO | null>(null)

const queryForm = reactive<AdminScoreTrainingDataQuery>({
  scoreTrainingDataResumeName: '',
  scoreTrainingDataIndustryName: '',
  scoreTrainingDataDataSource: undefined
})

const form = reactive<AdminScoreTrainingDataForm>({
  scoreTrainingDataResumeName: '',
  scoreTrainingDataIndustryName: '',
  scoreTrainingDataResumeContent: '',
  scoreTrainingDataLabelScore: null,
  scoreTrainingDataDataSource: 0
})

const rules: FormRules<AdminScoreTrainingDataForm> = {
  scoreTrainingDataResumeName: [
    { required: true, message: '请输入简历名称', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 25) {
          callback(new Error('简历名称长度不能超过25个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreTrainingDataIndustryName: [
    { required: true, message: '请输入行业名称', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 35) {
          callback(new Error('行业名称长度不能超过35个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreTrainingDataResumeContent: [{ required: true, message: '请输入简历内容', trigger: 'blur' }],
  scoreTrainingDataLabelScore: [
    { required: true, message: '请输入训练标签分数', trigger: 'change' },
    {
      validator: (_rule, value, callback) => {
        if (value != null && Number(value) < 0) {
          callback(new Error('训练标签分数不能为负数'))
          return
        }
        callback()
      },
      trigger: 'change'
    }
  ],
  scoreTrainingDataDataSource: [
    { required: true, message: '请选择数据来源', trigger: 'change' },
    {
      validator: (_rule, value, callback) => {
        if (value !== 0) {
          callback(new Error('新增训练数据时数据来源只能为人工'))
          return
        }
        callback()
      },
      trigger: 'change'
    }
  ]
}

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<AdminScoreTrainingDataPageVO[]>([])

const getDataSourceText = (dataSource?: number) => {
  if (dataSource === 0) return '人工'
  if (dataSource === 1) return '模型'
  return '-'
}

const getTableData = async () => {
  loading.value = true
  try {
    const response = await scoreTrainingDataApi.getScoreTrainingDataPage(
      pagination.current,
      pagination.size,
      queryForm
    )
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取训练数据列表失败:', error)
    ElMessage.error('加载训练数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  getTableData()
}

const resetSearch = () => {
  queryForm.scoreTrainingDataResumeName = ''
  queryForm.scoreTrainingDataIndustryName = ''
  queryForm.scoreTrainingDataDataSource = undefined
  pagination.current = 1
  getTableData()
}

const refreshData = () => {
  getTableData()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  getTableData()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getTableData()
}

const resetForm = () => {
  form.scoreTrainingDataId = undefined
  form.scoreTrainingDataResumeName = ''
  form.scoreTrainingDataIndustryName = ''
  form.scoreTrainingDataResumeContent = ''
  form.scoreTrainingDataLabelScore = null
  form.scoreTrainingDataDataSource = 0
  formRef.value?.clearValidate()
}

const openCreateDialog = () => {
  resetForm()
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const submitForm = {
      ...form,
      scoreTrainingDataResumeName: form.scoreTrainingDataResumeName.trim(),
      scoreTrainingDataIndustryName: form.scoreTrainingDataIndustryName.trim(),
      scoreTrainingDataResumeContent: form.scoreTrainingDataResumeContent.trim(),
      scoreTrainingDataLabelScore: Number(form.scoreTrainingDataLabelScore),
      scoreTrainingDataDataSource: 0
    }

    await scoreTrainingDataApi.addScoreTrainingData(submitForm)
    ElMessage.success('新增训练数据成功')
    dialogVisible.value = false
    resetForm()
    getTableData()
  } catch (error) {
    console.error('新增训练数据失败:', error)
    ElMessage.error('新增训练数据失败')
  } finally {
    submitting.value = false
  }
}

const handleViewDetail = async (row: AdminScoreTrainingDataPageVO) => {
  try {
    const response = await scoreTrainingDataApi.getScoreTrainingDataInfo(row.scoreTrainingDataId)
    currentDetail.value = response.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取训练数据详情失败:', error)
    ElMessage.error('获取训练数据详情失败')
  }
}

const handleDelete = async (row: AdminScoreTrainingDataPageVO) => {
  try {
    await ElMessageBox.confirm(
      `确定删除训练数据「${row.scoreTrainingDataResumeName}」吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await scoreTrainingDataApi.deleteScoreTrainingData(row.scoreTrainingDataId)
    ElMessage.success('删除训练数据成功')

    if (tableData.value.length === 1 && pagination.current > 1) {
      pagination.current -= 1
    }
    getTableData()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除训练数据失败:', error)
      ElMessage.error('删除训练数据失败')
    }
  }
}

const getFilenameFromDisposition = (disposition?: string) => {
  if (!disposition) return '简历评分训练数据.xlsx'
  const utf8Match = disposition.match(/filename\*=UTF-8''([^;]+)/i)
  if (utf8Match?.[1]) {
    return decodeURIComponent(utf8Match[1])
  }
  const normalMatch = disposition.match(/filename="?([^";]+)"?/i)
  if (normalMatch?.[1]) {
    return decodeURIComponent(normalMatch[1])
  }
  return '简历评分训练数据.xlsx'
}

const handleExport = async () => {
  exporting.value = true
  try {
    const response = await scoreTrainingDataApi.exportScoreTrainingData(queryForm)
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const disposition = response.headers?.['content-disposition']
    const filename = getFilenameFromDisposition(disposition)
    const downloadUrl = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = downloadUrl
    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(downloadUrl)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出训练数据失败:', error)
    ElMessage.error('导出训练数据失败')
  } finally {
    exporting.value = false
  }
}

onMounted(() => {
  getTableData()
})
</script>

<style scoped lang="scss">
.score-training-data-management {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.header-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

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

.search-card,
.table-card {
  :deep(.el-card__body) {
    padding: 20px;
  }
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  gap: 12px;
}

.table-title {
  font-size: 16px;
  font-weight: 600;
  color: #111827;
}

.table-count {
  color: #6b7280;
  font-size: 14px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.resume-content-section {
  .section-title {
    margin-bottom: 12px;
    font-size: 15px;
    font-weight: 600;
    color: #111827;
  }
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
  }

  .header-actions {
    width: 100%;

    :deep(.el-button) {
      flex: 1;
      min-width: 120px;
    }
  }

  .table-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .pagination-wrapper {
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>

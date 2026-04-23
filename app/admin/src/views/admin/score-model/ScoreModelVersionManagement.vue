<template>
  <div class="score-model-version-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">模型版本管理</h1>
        <p class="page-description">管理评分模型版本，支持新增、编辑、删除、详情查看和分页查询。</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData" :icon="Refresh">
          刷新
        </el-button>
        <el-button type="primary" @click="openCreateDialog" :icon="Plus">
          新增模型版本
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item label="模型名称">
          <el-input
            v-model="queryForm.scoreModelVersionModelName"
            placeholder="请输入模型名称"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="模型类型">
          <el-input
            v-model="queryForm.scoreModelVersionModelType"
            placeholder="请输入模型类型"
            clearable
            style="width: 220px"
          />
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
        <span class="table-title">模型版本列表</span>
        <span class="table-count">共 {{ pagination.total }} 条记录</span>
      </div>

      <el-table v-loading="loading" :data="tableData" style="width: 100%">
        <el-table-column prop="scoreModelVersionId" label="模型版本ID" width="120" />
        <el-table-column prop="scoreModelVersionModelName" label="模型名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="scoreModelVersionVersion" label="版本号" width="180" show-overflow-tooltip />
        <el-table-column prop="scoreModelVersionModelType" label="模型类型" width="140" show-overflow-tooltip />
        <el-table-column prop="scoreModelVersionEmbeddingModel" label="Embedding模型" min-width="160" show-overflow-tooltip />
        <el-table-column prop="scoreModelVersionSampleCount" label="样本数量" width="110" />
        <el-table-column label="是否启用" width="110">
          <template #default="{ row }">
            <el-tag :type="row.scoreModelVersionIsActive === 1 ? 'success' : 'info'" size="small">
              {{ row.scoreModelVersionIsActive === 1 ? '启用中' : '未启用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.scoreModelVersionCreateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="info" size="default" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button type="primary" size="default" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" size="default" @click="handleDelete(row)">
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
      :title="isEditMode ? '编辑模型版本' : '新增模型版本'"
      width="920px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="模型名称" prop="scoreModelVersionModelName">
          <el-input v-model="form.scoreModelVersionModelName" placeholder="请输入模型名称" maxlength="64" show-word-limit />
        </el-form-item>
        <el-form-item label="模型版本号" prop="scoreModelVersionVersion">
          <el-input v-model="form.scoreModelVersionVersion" placeholder="请输入模型版本号" maxlength="64" show-word-limit />
        </el-form-item>
        <el-form-item label="模型类型" prop="scoreModelVersionModelType">
          <el-input v-model="form.scoreModelVersionModelType" placeholder="请输入模型类型，如 xgboost" maxlength="32" show-word-limit />
        </el-form-item>
        <el-form-item label="Embedding模型" prop="scoreModelVersionEmbeddingModel">
          <el-input v-model="form.scoreModelVersionEmbeddingModel" placeholder="请输入Embedding模型" maxlength="64" show-word-limit />
        </el-form-item>
        <el-form-item label="训练样本数量" prop="scoreModelVersionSampleCount">
          <el-input-number
            v-model="form.scoreModelVersionSampleCount"
            :min="0"
            :precision="0"
            controls-position="right"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="训练耗时(毫秒)" prop="scoreModelVersionTrainCostMs">
          <el-input-number
            v-model="form.scoreModelVersionTrainCostMs"
            :min="0"
            :precision="0"
            controls-position="right"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="是否启用" prop="scoreModelVersionIsActive">
          <el-radio-group v-model="form.scoreModelVersionIsActive">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">不启用</el-radio>
          </el-radio-group>
          <div class="form-tip">
            同一时间只允许一个模型版本处于启用状态。
          </div>
        </el-form-item>
        <el-form-item label="评估指标JSON" prop="scoreModelVersionMetricJson">
          <el-input
            v-model="form.scoreModelVersionMetricJson"
            type="textarea"
            :rows="8"
            placeholder="请输入评估指标JSON"
          />
        </el-form-item>
        <el-form-item :label="isEditMode ? '模型文件' : '模型文件(必传)'" prop="modelFile">
          <el-upload
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleFileChange"
            accept=".json,.bin,.model,.pkl,.joblib,.onnx,.pt,.pth"
          >
            <el-button type="primary">
              选择模型文件
            </el-button>
          </el-upload>
          <div class="upload-info">
            <div v-if="selectedFileName">当前选择：{{ selectedFileName }}</div>
            <div v-else>{{ isEditMode ? '不重新选择则沿用原模型文件' : '请选择要上传的模型文件' }}</div>
          </div>
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

    <el-dialog v-model="detailDialogVisible" title="模型版本详情" width="980px">
      <div v-if="currentDetail" class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="模型版本ID">
            {{ currentDetail.scoreModelVersionId }}
          </el-descriptions-item>
          <el-descriptions-item label="是否启用">
            <el-tag :type="currentDetail.scoreModelVersionIsActive === 1 ? 'success' : 'info'">
              {{ currentDetail.scoreModelVersionIsActive === 1 ? '启用中' : '未启用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="模型名称" :span="2">
            {{ currentDetail.scoreModelVersionModelName }}
          </el-descriptions-item>
          <el-descriptions-item label="模型版本号">
            {{ currentDetail.scoreModelVersionVersion }}
          </el-descriptions-item>
          <el-descriptions-item label="模型类型">
            {{ currentDetail.scoreModelVersionModelType }}
          </el-descriptions-item>
          <el-descriptions-item label="Embedding模型" :span="2">
            {{ currentDetail.scoreModelVersionEmbeddingModel }}
          </el-descriptions-item>
          <el-descriptions-item label="模型地址" :span="2">
            {{ currentDetail.scoreModelVersionModelUrl }}
          </el-descriptions-item>
          <el-descriptions-item label="训练样本数量">
            {{ currentDetail.scoreModelVersionSampleCount }}
          </el-descriptions-item>
          <el-descriptions-item label="训练耗时(毫秒)">
            {{ currentDetail.scoreModelVersionTrainCostMs }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">
            {{ formatDateTime(currentDetail.scoreModelVersionCreateTime) }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="metric-json-section">
          <div class="section-title">评估指标JSON</div>
          <el-input
            :model-value="currentDetail.scoreModelVersionMetricJson || ''"
            type="textarea"
            readonly
            :autosize="{ minRows: 10, maxRows: 18 }"
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
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { UploadFile } from 'element-plus'
import { Plus, Refresh, RefreshRight, Search } from '@element-plus/icons-vue'
import { scoreModelVersionApi } from '@/api/admin'
import { formatDateTime } from '@/utils'
import type {
  AdminScoreModelVersionForm,
  AdminScoreModelVersionInfoVO,
  AdminScoreModelVersionPageVO,
  AdminScoreModelVersionQuery
} from '@/types/admin'
import type { FormInstance, FormRules } from 'element-plus'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const isEditMode = ref(false)
const formRef = ref<FormInstance>()
const currentDetail = ref<AdminScoreModelVersionInfoVO | null>(null)
const selectedFile = ref<File | null>(null)
const selectedFileName = ref('')

const queryForm = reactive<AdminScoreModelVersionQuery>({
  scoreModelVersionModelName: '',
  scoreModelVersionModelType: ''
})

const form = reactive<AdminScoreModelVersionForm>({
  scoreModelVersionId: undefined,
  scoreModelVersionModelName: '',
  scoreModelVersionVersion: '',
  scoreModelVersionModelType: '',
  scoreModelVersionEmbeddingModel: '',
  scoreModelVersionSampleCount: 0,
  scoreModelVersionTrainCostMs: 0,
  scoreModelVersionMetricJson: '',
  scoreModelVersionIsActive: 0
})

const rules: FormRules<AdminScoreModelVersionForm> = {
  scoreModelVersionModelName: [
    { required: true, message: '请输入模型名称', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 64) {
          callback(new Error('模型名称长度不能超过64个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreModelVersionVersion: [
    { required: true, message: '请输入模型版本号', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 64) {
          callback(new Error('模型版本号长度不能超过64个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreModelVersionModelType: [
    { required: true, message: '请输入模型类型', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        const trimmedValue = typeof value === 'string' ? value.trim() : ''
        if (trimmedValue.length > 32) {
          callback(new Error('模型类型长度不能超过32个字符'))
          return
        }
        if (trimmedValue && (trimmedValue === '.' || trimmedValue === '..' || !/^[A-Za-z0-9._-]+$/.test(trimmedValue))) {
          callback(new Error('模型类型格式不正确'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreModelVersionEmbeddingModel: [
    { required: true, message: '请输入Embedding模型', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 64) {
          callback(new Error('Embedding模型长度不能超过64个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreModelVersionSampleCount: [
    { required: true, message: '请输入训练样本数量', trigger: 'change' },
    {
      validator: (_rule, value, callback) => {
        if (value == null || Number(value) < 0) {
          callback(new Error('训练样本数量不能小于0'))
          return
        }
        callback()
      },
      trigger: 'change'
    }
  ],
  scoreModelVersionTrainCostMs: [
    { required: true, message: '请输入训练耗时毫秒', trigger: 'change' },
    {
      validator: (_rule, value, callback) => {
        if (value == null || Number(value) < 0) {
          callback(new Error('训练耗时毫秒不能小于0'))
          return
        }
        callback()
      },
      trigger: 'change'
    }
  ],
  scoreModelVersionMetricJson: [{ required: true, message: '请输入评估指标JSON', trigger: 'blur' }],
  scoreModelVersionIsActive: [{ required: true, message: '请选择是否启用', trigger: 'change' }]
}

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<AdminScoreModelVersionPageVO[]>([])

const getTableData = async () => {
  loading.value = true
  try {
    const response = await scoreModelVersionApi.getScoreModelVersionPage(
      pagination.current,
      pagination.size,
      queryForm
    )
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取模型版本列表失败:', error)
    ElMessage.error('加载模型版本失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  getTableData()
}

const resetSearch = () => {
  queryForm.scoreModelVersionModelName = ''
  queryForm.scoreModelVersionModelType = ''
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
  isEditMode.value = false
  form.scoreModelVersionId = undefined
  form.scoreModelVersionModelName = ''
  form.scoreModelVersionVersion = ''
  form.scoreModelVersionModelType = ''
  form.scoreModelVersionEmbeddingModel = ''
  form.scoreModelVersionSampleCount = 0
  form.scoreModelVersionTrainCostMs = 0
  form.scoreModelVersionMetricJson = ''
  form.scoreModelVersionIsActive = 0
  selectedFile.value = null
  selectedFileName.value = ''
  formRef.value?.clearValidate()
}

const openCreateDialog = () => {
  resetForm()
  dialogVisible.value = true
}

const handleFileChange = (uploadFile: UploadFile) => {
  selectedFile.value = uploadFile.raw || null
  selectedFileName.value = uploadFile.name || ''
}

const handleEdit = async (row: AdminScoreModelVersionPageVO) => {
  try {
    const response = await scoreModelVersionApi.getScoreModelVersionInfo(row.scoreModelVersionId)
    const detail = response.data
    form.scoreModelVersionId = detail.scoreModelVersionId
    form.scoreModelVersionModelName = detail.scoreModelVersionModelName || ''
    form.scoreModelVersionVersion = detail.scoreModelVersionVersion || ''
    form.scoreModelVersionModelType = detail.scoreModelVersionModelType || ''
    form.scoreModelVersionEmbeddingModel = detail.scoreModelVersionEmbeddingModel || ''
    form.scoreModelVersionSampleCount = detail.scoreModelVersionSampleCount ?? 0
    form.scoreModelVersionTrainCostMs = detail.scoreModelVersionTrainCostMs ?? 0
    form.scoreModelVersionMetricJson = detail.scoreModelVersionMetricJson || ''
    form.scoreModelVersionIsActive = detail.scoreModelVersionIsActive ?? 0
    selectedFile.value = null
    selectedFileName.value = ''
    isEditMode.value = true
    dialogVisible.value = true
    formRef.value?.clearValidate()
  } catch (error) {
    console.error('获取模型版本详情失败:', error)
    ElMessage.error('获取模型版本详情失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  if (!isEditMode.value && !selectedFile.value) {
    ElMessage.error('请先选择模型文件')
    return
  }

  submitting.value = true
  try {
    const submitForm: AdminScoreModelVersionForm = {
      ...(isEditMode.value ? { scoreModelVersionId: form.scoreModelVersionId } : {}),
      scoreModelVersionModelName: form.scoreModelVersionModelName.trim(),
      scoreModelVersionVersion: form.scoreModelVersionVersion.trim(),
      scoreModelVersionModelType: form.scoreModelVersionModelType.trim(),
      scoreModelVersionEmbeddingModel: form.scoreModelVersionEmbeddingModel.trim(),
      scoreModelVersionSampleCount: Number(form.scoreModelVersionSampleCount),
      scoreModelVersionTrainCostMs: Number(form.scoreModelVersionTrainCostMs),
      scoreModelVersionMetricJson: form.scoreModelVersionMetricJson.trim(),
      scoreModelVersionIsActive: Number(form.scoreModelVersionIsActive)
    }

    if (isEditMode.value) {
      await scoreModelVersionApi.updateScoreModelVersion(submitForm, selectedFile.value)
      ElMessage.success('修改模型版本成功')
    } else {
      await scoreModelVersionApi.addScoreModelVersion(submitForm, selectedFile.value as File)
      ElMessage.success('新增模型版本成功')
    }

    dialogVisible.value = false
    resetForm()
    getTableData()
  } catch (error) {
    console.error(isEditMode.value ? '修改模型版本失败:' : '新增模型版本失败:', error)
    ElMessage.error(isEditMode.value ? '修改模型版本失败' : '新增模型版本失败')
  } finally {
    submitting.value = false
  }
}

const handleViewDetail = async (row: AdminScoreModelVersionPageVO) => {
  try {
    const response = await scoreModelVersionApi.getScoreModelVersionInfo(row.scoreModelVersionId)
    currentDetail.value = response.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取模型版本详情失败:', error)
    ElMessage.error('获取模型版本详情失败')
  }
}

const handleDelete = async (row: AdminScoreModelVersionPageVO) => {
  try {
    await ElMessageBox.confirm(
      `确定删除模型版本「${row.scoreModelVersionModelName} ${row.scoreModelVersionVersion}」吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await scoreModelVersionApi.deleteScoreModelVersion(row.scoreModelVersionId)
    ElMessage.success('删除模型版本成功')

    if (tableData.value.length === 1 && pagination.current > 1) {
      pagination.current -= 1
    }
    getTableData()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除模型版本失败:', error)
      ElMessage.error('删除模型版本失败')
    }
  }
}

onMounted(() => {
  getTableData()
})
</script>

<style scoped lang="scss">
.score-model-version-management {
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

.metric-json-section {
  .section-title {
    margin-bottom: 12px;
    font-size: 15px;
    font-weight: 600;
    color: #111827;
  }
}

.form-tip,
.upload-info {
  margin-top: 8px;
  color: #6b7280;
  font-size: 12px;
  line-height: 1.6;
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

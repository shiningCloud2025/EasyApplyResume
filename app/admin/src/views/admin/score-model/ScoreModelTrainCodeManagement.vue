<template>
  <div class="score-model-train-code-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">训练代码管理</h1>
        <p class="page-description">管理评分模型训练代码，支持新增、编辑、查询、详情查看、删除和下载。</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData" :icon="Refresh">
          刷新
        </el-button>
        <el-button v-if="canAddScoreModelTrainCode" type="primary" @click="openCreateDialog" :icon="Plus">
          新增训练代码
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item label="代码名称">
          <el-input
            v-model="queryForm.scoreModelTrainCodeName"
            placeholder="请输入代码名称"
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="代码语言">
          <el-input
            v-model="queryForm.scoreModelTrainCodeLanguage"
            placeholder="请输入代码语言"
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
        <span class="table-title">训练代码列表</span>
        <span class="table-count">共 {{ pagination.total }} 条记录</span>
      </div>

      <el-table v-loading="loading" :data="tableData" style="width: 100%">
        <el-table-column prop="scoreModelTrainCodeId" label="训练代码ID" width="120" />
        <el-table-column prop="scoreModelTrainCodeName" label="代码名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="scoreModelTrainCodeVersion" label="版本" width="180" show-overflow-tooltip />
        <el-table-column prop="scoreModelTrainCodeLanguage" label="语言" width="140" show-overflow-tooltip />
        <el-table-column prop="scoreModelTrainCodeDesc" label="描述" min-width="220" show-overflow-tooltip />
        <el-table-column label="创建时间" min-width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.scoreModelTrainCodeCreateTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="showScoreModelTrainCodeActionColumn" label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canViewScoreModelTrainCodeDetail" type="info" size="default" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button v-if="canUpdateScoreModelTrainCode" type="primary" size="default" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button v-if="canDownloadScoreModelTrainCode" type="success" size="default" :loading="downloadingId === row.scoreModelTrainCodeId" @click="handleDownload(row)">
              下载
            </el-button>
            <el-button v-if="canDeleteScoreModelTrainCode" type="danger" size="default" @click="handleDelete(row)">
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
      :title="isEditMode ? '编辑训练代码' : '新增训练代码'"
      width="860px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="代码名称" prop="scoreModelTrainCodeName">
          <el-input v-model="form.scoreModelTrainCodeName" placeholder="请输入代码名称" maxlength="64" show-word-limit />
          <div class="form-tip">
            代码名称同时也是下载压缩包名称；单文件模式下，文件名默认等于代码名称，编辑时改代码名称也会同步改文件名。
          </div>
        </el-form-item>
        <el-form-item label="版本" prop="scoreModelTrainCodeVersion">
          <el-input v-model="form.scoreModelTrainCodeVersion" placeholder="请输入版本" maxlength="64" show-word-limit />
        </el-form-item>
        <el-form-item label="代码语言" prop="scoreModelTrainCodeLanguage">
          <el-input v-model="form.scoreModelTrainCodeLanguage" placeholder="请输入代码语言" maxlength="32" show-word-limit />
        </el-form-item>
        <el-form-item label="描述" prop="scoreModelTrainCodeDesc">
          <el-input
            v-model="form.scoreModelTrainCodeDesc"
            type="textarea"
            :rows="4"
            placeholder="请输入描述"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="训练代码内容" prop="scoreModelTrainCodeContent">
          <el-input
            v-model="form.scoreModelTrainCodeContent"
            type="textarea"
            :rows="14"
            placeholder="请输入训练代码内容；支持直接粘贴单文件代码，提交时会自动补成可下载格式"
          />
          <div class="form-tip">
            单文件代码可直接粘贴，提交时会自动生成文件块；如果你手动使用多文件块格式，则需自行维护每个“文件名:xxx”。
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

    <el-dialog v-model="detailDialogVisible" title="训练代码详情" width="960px">
      <div v-if="currentDetail" class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="训练代码ID">
            {{ currentDetail.scoreModelTrainCodeId }}
          </el-descriptions-item>
          <el-descriptions-item label="代码语言">
            {{ currentDetail.scoreModelTrainCodeLanguage }}
          </el-descriptions-item>
          <el-descriptions-item label="代码名称" :span="2">
            {{ currentDetail.scoreModelTrainCodeName }}
          </el-descriptions-item>
          <el-descriptions-item label="版本">
            {{ currentDetail.scoreModelTrainCodeVersion }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(currentDetail.scoreModelTrainCodeCreateTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">
            {{ currentDetail.scoreModelTrainCodeDesc }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="train-code-content-section">
          <div class="section-title">训练代码内容</div>
          <el-input
            :model-value="currentDetail.scoreModelTrainCodeContent || ''"
            type="textarea"
            readonly
            :autosize="{ minRows: 14, maxRows: 24 }"
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
import { Plus, Refresh, RefreshRight, Search } from '@element-plus/icons-vue'
import { scoreModelTrainCodeApi } from '@/api/admin'
import { useAuthStore } from '@/store/auth'
import { formatDateTime } from '@/utils'
import type {
  AdminScoreModelTrainCodeForm,
  AdminScoreModelTrainCodeInfoVO,
  AdminScoreModelTrainCodePageVO,
  AdminScoreModelTrainCodeQuery
} from '@/types/admin'
import type { FormInstance, FormRules } from 'element-plus'

const authStore = useAuthStore()
const canAddScoreModelTrainCode = computed(() => authStore.hasPermission('/admin/scoreModelTrainCode/addScoreModelTrainCode'))
const canViewScoreModelTrainCodeDetail = computed(() => authStore.hasPermission('/admin/scoreModelTrainCode/findScoreModelTrainCodeById'))
const canUpdateScoreModelTrainCode = computed(() => authStore.hasPermission('/admin/scoreModelTrainCode/updateScoreModelTrainCode'))
const canDeleteScoreModelTrainCode = computed(() => authStore.hasPermission('/admin/scoreModelTrainCode/deleteScoreModelTrainCode'))
const canDownloadScoreModelTrainCode = computed(() => authStore.hasPermission('/admin/scoreModelTrainCode/downloadScoreModelTrainCode'))
const showScoreModelTrainCodeActionColumn = computed(() => {
  return canViewScoreModelTrainCodeDetail.value || canUpdateScoreModelTrainCode.value || canDeleteScoreModelTrainCode.value || canDownloadScoreModelTrainCode.value
})

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const isEditMode = ref(false)
const downloadingId = ref<number | null>(null)
const formRef = ref<FormInstance>()
const currentDetail = ref<AdminScoreModelTrainCodeInfoVO | null>(null)

const queryForm = reactive<AdminScoreModelTrainCodeQuery>({
  scoreModelTrainCodeName: '',
  scoreModelTrainCodeLanguage: ''
})

const form = reactive<AdminScoreModelTrainCodeForm>({
  scoreModelTrainCodeId: undefined,
  scoreModelTrainCodeName: '',
  scoreModelTrainCodeVersion: '',
  scoreModelTrainCodeLanguage: '',
  scoreModelTrainCodeContent: '',
  scoreModelTrainCodeDesc: ''
})

const rules: FormRules<AdminScoreModelTrainCodeForm> = {
  scoreModelTrainCodeName: [
    { required: true, message: '请输入代码名称', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 64) {
          callback(new Error('代码名称长度不能超过64个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreModelTrainCodeVersion: [
    { required: true, message: '请输入版本', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 64) {
          callback(new Error('版本长度不能超过64个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreModelTrainCodeLanguage: [
    { required: true, message: '请输入代码语言', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 32) {
          callback(new Error('代码语言长度不能超过32个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  scoreModelTrainCodeContent: [{ required: true, message: '请输入训练代码内容', trigger: 'blur' }],
  scoreModelTrainCodeDesc: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (typeof value === 'string' && value.trim().length > 500) {
          callback(new Error('描述长度不能超过500个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ]
}

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<AdminScoreModelTrainCodePageVO[]>([])

const FILE_SEPARATOR = '++++???++++'
const FILE_NAME_PREFIX = '文件名:'

const getFilenameFromDisposition = (disposition?: string) => {
  if (!disposition) return '训练代码.zip'
  const utf8Match = disposition.match(/filename\*=UTF-8''([^;]+)/i)
  if (utf8Match?.[1]) {
    return decodeURIComponent(utf8Match[1])
  }
  const normalMatch = disposition.match(/filename="?([^";]+)"?/i)
  if (normalMatch?.[1]) {
    return decodeURIComponent(normalMatch[1])
  }
  return '训练代码.zip'
}

const isStructuredTrainCodeContent = (content: string) => {
  const normalizedContent = content.replace(/\r\n/g, '\n').trim()
  if (!normalizedContent.startsWith(FILE_SEPARATOR)) {
    return false
  }

  const blockList = normalizedContent
    .split(FILE_SEPARATOR)
    .map(block => block.trim())
    .filter(Boolean)

  return blockList.length > 0 && blockList.every(block => {
    const [firstLine] = block.split('\n')
    return firstLine?.trim().startsWith(FILE_NAME_PREFIX)
  })
}

const extractStructuredTrainCodeFileName = (content: string) => {
  const normalizedContent = content.replace(/\r\n/g, '\n').trim()
  if (!isStructuredTrainCodeContent(normalizedContent)) {
    return ''
  }

  const blockList = normalizedContent
    .split(FILE_SEPARATOR)
    .map(block => block.trim())
    .filter(Boolean)

  if (blockList.length !== 1) {
    return ''
  }

  const [firstLine] = blockList[0].split('\n')
  return firstLine?.trim().startsWith(FILE_NAME_PREFIX)
    ? firstLine.trim().slice(FILE_NAME_PREFIX.length).trim()
    : ''
}

const replaceStructuredTrainCodeFileName = (content: string, nextFileName: string) => {
  const normalizedContent = content.replace(/\r\n/g, '\n').trim()
  if (!isStructuredTrainCodeContent(normalizedContent)) {
    return content
  }

  const blockList = normalizedContent
    .split(FILE_SEPARATOR)
    .map(block => block.trim())
    .filter(Boolean)

  if (blockList.length !== 1) {
    return content
  }

  const lineList = blockList[0].split('\n')
  if (!lineList[0]?.trim().startsWith(FILE_NAME_PREFIX)) {
    return content
  }

  lineList[0] = `${FILE_NAME_PREFIX}${nextFileName}`
  return `${FILE_SEPARATOR}\n${lineList.join('\n')}`
}

const sanitizeSingleFileName = (fileName: string) => {
  const sanitizedName = fileName
    .trim()
    .replace(/[<>:"/\\|?*\x00-\x1f]+/g, '_')
    .replace(/\s+/g, '_')
    .replace(/_+/g, '_')
    .replace(/^[_./-]+|[_./-]+$/g, '')

  return sanitizedName || 'train_code.txt'
}

const inferSingleFileExtension = (language: string, codeName: string, content: string) => {
  const normalizedLanguage = language.trim().toLowerCase()
  const normalizedName = codeName.trim().toLowerCase()
  const normalizedContent = content.trim()

  if (normalizedLanguage.includes('python') || normalizedName.includes('python') || normalizedContent.startsWith('#!') || normalizedContent.includes('import argparse')) {
    return '.py'
  }
  if (normalizedLanguage.includes('java')) {
    return '.java'
  }
  if (normalizedLanguage.includes('javascript') || normalizedLanguage === 'js') {
    return '.js'
  }
  if (normalizedLanguage.includes('typescript') || normalizedLanguage === 'ts') {
    return '.ts'
  }
  if (normalizedLanguage.includes('vue')) {
    return '.vue'
  }
  if (normalizedLanguage.includes('shell') || normalizedLanguage.includes('bash') || normalizedLanguage === 'sh') {
    return '.sh'
  }
  if (normalizedLanguage.includes('go')) {
    return '.go'
  }
  if (normalizedLanguage.includes('c++') || normalizedLanguage.includes('cpp')) {
    return '.cpp'
  }
  if (normalizedLanguage.includes('c#') || normalizedLanguage.includes('csharp')) {
    return '.cs'
  }
  if (normalizedLanguage.includes('json')) {
    return '.json'
  }
  return '.txt'
}

const buildSingleFileTrainCodeContent = (content: string, language: string, codeName: string) => {
  const trimmedContent = content.trim()
  const normalizedName = codeName.trim()
  const hasExtension = /\.[A-Za-z0-9]+$/.test(normalizedName)
  const fileExtension = hasExtension ? '' : inferSingleFileExtension(language, codeName, trimmedContent)
  const fileName = sanitizeSingleFileName(`${normalizedName || 'train_code'}${fileExtension}`)
  return `${FILE_SEPARATOR}\n${FILE_NAME_PREFIX}${fileName}\n${trimmedContent}`
}

const isSingleFileStructuredTrainCodeContent = (content: string) => Boolean(extractStructuredTrainCodeFileName(content))

const syncSingleFileStructuredName = (content: string, language: string, codeName: string) => {
  const currentFileName = extractStructuredTrainCodeFileName(content)
  if (!currentFileName) {
    return content
  }

  const normalizedName = codeName.trim()
  const hasExtension = /\.[A-Za-z0-9]+$/.test(normalizedName)
  const nextFileName = sanitizeSingleFileName(
    `${normalizedName || 'train_code'}${hasExtension ? '' : inferSingleFileExtension(language, codeName, content)}`
  )

  return replaceStructuredTrainCodeFileName(content, nextFileName)
}

const normalizeTrainCodeContentForSubmit = (content: string, language: string, codeName: string) => {
  const trimmedContent = content.trim()
  if (!trimmedContent) {
    return trimmedContent
  }
  if (isSingleFileStructuredTrainCodeContent(trimmedContent)) {
    return syncSingleFileStructuredName(trimmedContent, language, codeName)
  }
  if (isStructuredTrainCodeContent(trimmedContent)) {
    return trimmedContent
  }
  return buildSingleFileTrainCodeContent(trimmedContent, language, codeName)
}

const getTableData = async () => {
  loading.value = true
  try {
    const response = await scoreModelTrainCodeApi.getScoreModelTrainCodePage(
      pagination.current,
      pagination.size,
      queryForm
    )
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取训练代码列表失败:', error)
    ElMessage.error('加载训练代码失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  getTableData()
}

const resetSearch = () => {
  queryForm.scoreModelTrainCodeName = ''
  queryForm.scoreModelTrainCodeLanguage = ''
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
  form.scoreModelTrainCodeId = undefined
  form.scoreModelTrainCodeName = ''
  form.scoreModelTrainCodeVersion = ''
  form.scoreModelTrainCodeLanguage = ''
  form.scoreModelTrainCodeContent = ''
  form.scoreModelTrainCodeDesc = ''
  formRef.value?.clearValidate()
}

const openCreateDialog = () => {
  resetForm()
  dialogVisible.value = true
}

const handleEdit = async (row: AdminScoreModelTrainCodePageVO) => {
  try {
    const response = await scoreModelTrainCodeApi.getScoreModelTrainCodeInfo(row.scoreModelTrainCodeId)
    const detail = response.data
    form.scoreModelTrainCodeId = detail.scoreModelTrainCodeId
    form.scoreModelTrainCodeName = detail.scoreModelTrainCodeName || ''
    form.scoreModelTrainCodeVersion = detail.scoreModelTrainCodeVersion || ''
    form.scoreModelTrainCodeLanguage = detail.scoreModelTrainCodeLanguage || ''
    form.scoreModelTrainCodeContent = detail.scoreModelTrainCodeContent || ''
    form.scoreModelTrainCodeDesc = detail.scoreModelTrainCodeDesc || ''
    isEditMode.value = true
    dialogVisible.value = true
    formRef.value?.clearValidate()
  } catch (error) {
    console.error('获取训练代码详情失败:', error)
    ElMessage.error('获取训练代码详情失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const submitForm: AdminScoreModelTrainCodeForm = {
      ...(isEditMode.value ? { scoreModelTrainCodeId: form.scoreModelTrainCodeId } : {}),
      scoreModelTrainCodeName: form.scoreModelTrainCodeName.trim(),
      scoreModelTrainCodeVersion: form.scoreModelTrainCodeVersion.trim(),
      scoreModelTrainCodeLanguage: form.scoreModelTrainCodeLanguage.trim(),
      scoreModelTrainCodeContent: normalizeTrainCodeContentForSubmit(
        form.scoreModelTrainCodeContent,
        form.scoreModelTrainCodeLanguage,
        form.scoreModelTrainCodeName
      ),
      scoreModelTrainCodeDesc: form.scoreModelTrainCodeDesc.trim()
    }

    if (isEditMode.value) {
      await scoreModelTrainCodeApi.updateScoreModelTrainCode(submitForm)
      ElMessage.success('修改训练代码成功')
    } else {
      await scoreModelTrainCodeApi.addScoreModelTrainCode(submitForm)
      ElMessage.success('新增训练代码成功')
    }

    dialogVisible.value = false
    resetForm()
    getTableData()
  } catch (error) {
    console.error(isEditMode.value ? '修改训练代码失败:' : '新增训练代码失败:', error)
    ElMessage.error(isEditMode.value ? '修改训练代码失败' : '新增训练代码失败')
  } finally {
    submitting.value = false
  }
}

const handleViewDetail = async (row: AdminScoreModelTrainCodePageVO) => {
  try {
    const response = await scoreModelTrainCodeApi.getScoreModelTrainCodeInfo(row.scoreModelTrainCodeId)
    currentDetail.value = response.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取训练代码详情失败:', error)
    ElMessage.error('获取训练代码详情失败')
  }
}

const handleDelete = async (row: AdminScoreModelTrainCodePageVO) => {
  try {
    await ElMessageBox.confirm(
      `确定删除训练代码「${row.scoreModelTrainCodeName}」吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await scoreModelTrainCodeApi.deleteScoreModelTrainCode(row.scoreModelTrainCodeId)
    ElMessage.success('删除训练代码成功')

    if (tableData.value.length === 1 && pagination.current > 1) {
      pagination.current -= 1
    }
    getTableData()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除训练代码失败:', error)
      ElMessage.error('删除训练代码失败')
    }
  }
}

const handleDownload = async (row: AdminScoreModelTrainCodePageVO) => {
  downloadingId.value = row.scoreModelTrainCodeId
  try {
    const response = await scoreModelTrainCodeApi.downloadScoreModelTrainCode(row.scoreModelTrainCodeId)
    const blob = new Blob([response.data], {
      type: 'application/zip'
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
    ElMessage.success('下载成功')
  } catch (error) {
    console.error('下载训练代码失败:', error)
    ElMessage.error('下载训练代码失败')
  } finally {
    downloadingId.value = null
  }
}

onMounted(() => {
  getTableData()
})
</script>

<style scoped lang="scss">
.score-model-train-code-management {
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

.train-code-content-section {
  .section-title {
    margin-bottom: 12px;
    font-size: 15px;
    font-weight: 600;
    color: #111827;
  }
}

.form-tip {
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

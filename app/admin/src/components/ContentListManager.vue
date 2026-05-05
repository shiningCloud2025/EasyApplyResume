<template>
  <div class="content-list-manager">
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="标题">
          <el-input
            v-model="searchForm.title"
            :placeholder="`请输入${moduleLabel}标题`"
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
            <el-icon><RefreshRight /></el-icon>
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
        empty-text="暂无数据"
      >
        <el-table-column label="标题" min-width="320">
          <template #default="{ row }">
            <div class="table-rich-title" v-html="normalizeRichTextHtml(row[titleField] || '') || '-'"></div>
          </template>
        </el-table-column>
        <el-table-column label="时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(getDisplayTime(row)) }}
          </template>
        </el-table-column>
        <el-table-column v-if="props.canView || props.canEdit || props.canDelete" label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button v-if="props.canView" type="info" @click="handleView(row)">查看</el-button>
            <el-button v-if="props.canEdit" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="props.canDelete" type="danger" @click="handleDelete(row)">删除</el-button>
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
      v-model="dialogVisible"
      :title="form.id != null ? `编辑${moduleLabel}` : `新增${moduleLabel}`"
      width="1280px"
      @close="resetForm"
    >
      <div v-loading="detailLoading">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" class="content-form">
          <div class="editor-preview-layout">
            <div class="editor-section">
              <el-form-item label="标题" prop="title">
                <MarkdownEditor
                  v-model="form.title"
                  :height="titleEditorHeight"
                  placeholder="请输入标题，支持富文本格式..."
                  :readonly="!canSubmitCurrentForm"
                />
              </el-form-item>
              <el-form-item label="内容" prop="content">
                <MarkdownEditor v-model="form.content" :height="editorHeight" :readonly="!canSubmitCurrentForm" />
              </el-form-item>
            </div>

            <div class="preview-section">
              <div class="preview-shell" :style="{ minHeight: previewMinHeight }">
                <div v-if="hasPreviewContent" class="preview-content">
                  <div v-if="previewTitle" class="preview-title" v-html="previewTitle"></div>
                  <div v-if="previewBody" class="preview-body" v-html="previewBody"></div>
                </div>
                <div v-else class="preview-placeholder">请输入后，这里会同步展示效果</div>
              </div>
            </div>
          </div>
        </el-form>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button v-if="canSubmitCurrentForm" type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" :title="`${moduleLabel}详情`" width="960px">
      <div v-loading="detailLoading" class="detail-wrapper">
        <template v-if="currentDetail">
          <el-descriptions :column="2" border class="detail-meta">
            <el-descriptions-item :label="detailIdLabel">
              {{ currentDetail[idField] ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ formatDateTime(currentDetail[createdField] || '') }}
            </el-descriptions-item>
            <el-descriptions-item label="修改时间">
              {{ formatDateTime(currentDetail[updatedField] || '') }}
            </el-descriptions-item>
            <el-descriptions-item label="标题" :span="2">
              <div class="detail-rich-title" v-html="normalizeRichTextHtml(currentDetail[titleField] || '') || '-'" />
            </el-descriptions-item>
            <el-descriptions-item label="内容" :span="2">
              <div class="preview-container">
                <div class="html-preview" v-html="normalizeRichTextHtml(currentDetail[contentField] || '') || '-'"></div>
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </template>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { RefreshRight, Search } from '@element-plus/icons-vue'
import MarkdownEditor from '@/components/MarkdownEditor.vue'
import { formatDateTime } from '@/utils'
import { extractRichTextPlainText, hasMeaningfulRichText, normalizeRichTextHtml } from '@/utils/html'

interface Props {
  moduleLabel: string
  idField: string
  titleField: string
  contentField: string
  searchField: string
  createdField: string
  updatedField: string
  getPage: (pageNum: number, pageSize: number, query: Record<string, any>) => Promise<any>
  getInfo: (id: number) => Promise<any>
  add: (data: Record<string, any>) => Promise<any>
  update: (data: Record<string, any>) => Promise<any>
  remove: (id: number) => Promise<any>
  editorHeight?: string
  titleEditorHeight?: string
  canView?: boolean
  canAdd?: boolean
  canEdit?: boolean
  canDelete?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  editorHeight: '420px',
  titleEditorHeight: '220px',
  canView: true,
  canAdd: true,
  canEdit: true,
  canDelete: true
})

const formRef = ref<FormInstance>()
const loading = ref(false)
const detailLoading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const tableData = ref<Record<string, any>[]>([])
const currentDetail = ref<Record<string, any> | null>(null)

const searchForm = reactive({
  title: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: undefined as number | undefined,
  title: '',
  content: ''
})

const previewTitle = computed(() => normalizeRichTextHtml(form.title))
const previewBody = computed(() => normalizeRichTextHtml(form.content))
const hasPreviewContent = computed(() => !!previewTitle.value || !!previewBody.value)
const previewMinHeight = computed(() => `calc(${props.titleEditorHeight} + ${props.editorHeight} + 56px)`)
const detailIdLabel = computed(() => `${props.moduleLabel}ID`)
const canSubmitCurrentForm = computed(() => (form.id != null ? props.canEdit : props.canAdd))

const validateRichTextField = (message: string) => {
  return (_rule: any, value: string, callback: (error?: Error) => void) => {
    if (hasMeaningfulRichText(value)) {
      callback()
      return
    }

    callback(new Error(message))
  }
}

const rules: FormRules = {
  title: [{ validator: validateRichTextField('请输入标题'), trigger: ['blur', 'change'] }],
  content: [{ validator: validateRichTextField('请输入内容'), trigger: ['blur', 'change'] }]
}

const queryPayload = computed(() => ({
  [props.searchField]: searchForm.title || undefined
}))

const buildPayload = () => {
  const payload: Record<string, any> = {
    [props.titleField]: normalizeRichTextHtml(form.title),
    [props.contentField]: normalizeRichTextHtml(form.content)
  }

  if (form.id != null) {
    payload[props.idField] = form.id
  }

  return payload
}

const getDisplayTime = (row: Record<string, any>) => {
  return row[props.updatedField] || row[props.createdField] || ''
}

const getList = async () => {
  loading.value = true
  try {
    const response = await props.getPage(pagination.current, pagination.size, queryPayload.value)
    tableData.value = response.data?.records || []
    pagination.total = response.data?.total || 0
  } catch (error) {
    console.error(`获取${props.moduleLabel}列表失败:`, error)
    ElMessage.error(`获取${props.moduleLabel}列表失败`)
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
  searchForm.title = ''
  pagination.current = 1
  getList()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  getList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getList()
}

const resetForm = () => {
  formRef.value?.clearValidate()
  form.id = undefined
  form.title = ''
  form.content = ''
}

const openCreateDialog = () => {
  resetForm()
  dialogVisible.value = true
}

const handleView = async (row: Record<string, any>) => {
  detailLoading.value = true
  viewDialogVisible.value = true
  try {
    const response = await props.getInfo(row[props.idField])
    currentDetail.value = response.data || row
  } catch (error) {
    console.error(`获取${props.moduleLabel}详情失败:`, error)
    ElMessage.error(`获取${props.moduleLabel}详情失败`)
    viewDialogVisible.value = false
  } finally {
    detailLoading.value = false
  }
}

const handleEdit = async (row: Record<string, any>) => {
  detailLoading.value = true
  try {
    const response = await props.getInfo(row[props.idField])
    const data = response.data || row
    form.id = data[props.idField]
    form.title = data[props.titleField] || ''
    form.content = data[props.contentField] || ''
    dialogVisible.value = true
  } catch (error) {
    console.error(`获取${props.moduleLabel}详情失败:`, error)
    ElMessage.error(`获取${props.moduleLabel}详情失败`)
  } finally {
    detailLoading.value = false
  }
}

const getDeleteTitle = (row: Record<string, any>) => {
  return extractRichTextPlainText(row[props.titleField] || '') || `${props.moduleLabel}内容`
}

const handleDelete = (row: Record<string, any>) => {
  ElMessageBox.confirm(`确定要删除“${getDeleteTitle(row)}”吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await props.remove(row[props.idField])
      ElMessage.success('删除成功')

      if (tableData.value.length === 1 && pagination.current > 1) {
        pagination.current -= 1
      }

      getList()
    } catch (error) {
      console.error(`删除${props.moduleLabel}失败:`, error)
      ElMessage.error(`删除${props.moduleLabel}失败`)
    }
  }).catch(() => undefined)
}

const handleSubmit = async () => {
  if (!formRef.value) {
    return
  }

  try {
    await formRef.value.validate()
    submitting.value = true

    if (form.id != null) {
      await props.update(buildPayload())
    } else {
      await props.add(buildPayload())
    }

    ElMessage.success(`${props.moduleLabel}${form.id != null ? '更新' : '创建'}成功`)
    dialogVisible.value = false
    getList()
  } catch (error) {
    console.error(`${props.moduleLabel}保存失败:`, error)
    ElMessage.error(`${props.moduleLabel}保存失败`)
  } finally {
    submitting.value = false
  }
}

defineExpose({
  refreshData,
  openCreateDialog
})

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.content-list-manager {
  .search-card {
    margin-bottom: 24px;
  }

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }

  .pagination {
    display: flex;
    justify-content: flex-end;
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid #f3f4f6;
  }

  .content-form {
    display: flex;
    flex-direction: column;
  }

  .editor-preview-layout {
    display: grid;
    grid-template-columns: minmax(0, 1.25fr) minmax(320px, 1fr);
    gap: 24px;
    align-items: start;
  }

  .preview-section {
    min-width: 0;
  }

  .preview-shell {
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    padding: 20px;
    background: #fafafa;
    overflow: auto;
  }

  .preview-content,
  .html-preview {
    color: #1f2937;
    line-height: 1.7;
    word-break: break-word;

    :deep(img) {
      max-width: 100%;
      height: auto;
    }

    :deep(table) {
      width: 100%;
      border-collapse: collapse;
    }

    :deep(th),
    :deep(td) {
      border: 1px solid #e5e7eb;
      padding: 8px 12px;
    }

    :deep(blockquote) {
      margin: 16px 0;
      padding-left: 12px;
      color: #6b7280;
      border-left: 4px solid #d1d5db;
    }
  }

  .preview-title {
    margin: 0 0 16px;
    color: #111827;
    line-height: 1.4;

    :deep(p) {
      margin: 0;
    }
  }

  .preview-placeholder {
    min-height: 160px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #9ca3af;
    font-size: 14px;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .detail-wrapper {
    min-height: 120px;
  }

  .detail-meta {
    margin-bottom: 0;
  }

  .preview-container {
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 16px;
    background: #fafafa;
  }
}

@media (max-width: 768px) {
  .content-list-manager {
    .editor-preview-layout {
      grid-template-columns: 1fr;
    }

    .search-card .el-form-item {
      display: block;
      margin-bottom: 16px;
    }
  }
}
</style>

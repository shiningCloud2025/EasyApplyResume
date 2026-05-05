<template>
  <div class="singleton-content-manager">
    <el-card class="content-card">
      <el-alert
        title="支持富文本编辑，体验更接近 Word"
        type="info"
        :closable="false"
        show-icon
        class="editor-tip"
      />

      <el-form
        ref="formRef"
        v-loading="loading"
        :model="form"
        :rules="rules"
        label-width="80px"
        class="content-form"
      >
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
              <MarkdownEditor
                v-model="form.content"
                :height="editorHeight"
                @html-change="handleHtmlChange"
                :readonly="!canSubmitCurrentForm"
              />
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

        <div class="form-footer">
          <div class="update-time">
            <span v-if="form.updatedTime">最近更新：{{ formatDateTime(form.updatedTime) }}</span>
          </div>
          <div class="actions">
            <el-button @click="loadInfo(true)" :loading="loading">刷新</el-button>
            <el-button v-if="canSubmitCurrentForm" type="primary" @click="handleSubmit" :loading="submitting">
              {{ submitButtonText }}
            </el-button>
          </div>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import MarkdownEditor from '@/components/MarkdownEditor.vue'
import { formatDateTime } from '@/utils'
import { hasMeaningfulRichText, normalizeRichTextHtml } from '@/utils/html'

interface Props {
  moduleLabel: string
  idField: string
  titleField: string
  contentField: string
  updatedField?: string
  getInfo: () => Promise<any>
  add: (data: Record<string, any>) => Promise<any>
  update: (data: Record<string, any>) => Promise<any>
  editorHeight?: string
  titleEditorHeight?: string
  canAdd?: boolean
  canUpdate?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  updatedField: '',
  editorHeight: '460px',
  titleEditorHeight: '220px',
  canAdd: true,
  canUpdate: true
})

const formRef = ref<FormInstance>()
const loading = ref(false)
const submitting = ref(false)

const form = reactive({
  id: undefined as number | undefined,
  title: '',
  content: '',
  updatedTime: '' as string | Date | undefined
})

const contentState = reactive({
  wrapperPrefix: '',
  wrapperSuffix: '',
  htmlContent: ''
})

const submitButtonText = computed(() => (form.id != null ? '更新' : '保存'))
const previewTitle = computed(() => normalizeRichTextHtml(form.title))
const previewBody = computed(() => normalizeRichTextHtml(contentState.htmlContent || form.content))
const hasPreviewContent = computed(() => !!previewTitle.value || !!previewBody.value)
const previewMinHeight = computed(() => `calc(${props.titleEditorHeight} + ${props.editorHeight} + 56px)`)
const canSubmitCurrentForm = computed(() => (form.id != null ? props.canUpdate : props.canAdd))

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

const unwrapPlatformContent = (value: string) => {
  let current = value.trim()
  let wrapperPrefix = ''
  let wrapperSuffix = ''
  let matched = true

  while (matched) {
    matched = false
    const match = current.match(/^\s*(<(react|vue|h5)(?:\s[^>]*)?>)([\s\S]*)(<\/\2>)\s*$/i)
    if (match) {
      wrapperPrefix += match[1]
      wrapperSuffix = `${match[4]}${wrapperSuffix}`
      current = match[3].trim()
      matched = true
    }
  }

  return {
    content: current,
    wrapperPrefix,
    wrapperSuffix
  }
}

const applyData = (data?: Record<string, any> | null) => {
  const rawContent = data?.[props.contentField] ?? ''
  const { content, wrapperPrefix, wrapperSuffix } = unwrapPlatformContent(rawContent)
  const editorContent = normalizeRichTextHtml(content)

  form.id = data?.[props.idField] ?? undefined
  form.title = normalizeRichTextHtml(data?.[props.titleField] ?? '')
  form.content = editorContent
  form.updatedTime = props.updatedField ? data?.[props.updatedField] ?? '' : ''

  contentState.wrapperPrefix = wrapperPrefix
  contentState.wrapperSuffix = wrapperSuffix
  contentState.htmlContent = editorContent
}

const handleHtmlChange = (html: string) => {
  contentState.htmlContent = normalizeRichTextHtml(html)
}

const buildPayload = () => {
  const htmlContent = contentState.htmlContent || normalizeRichTextHtml(form.content)
  const payload: Record<string, any> = {
    [props.titleField]: normalizeRichTextHtml(form.title),
    [props.contentField]: `${contentState.wrapperPrefix}${htmlContent}${contentState.wrapperSuffix}`
  }

  if (form.id != null) {
    payload[props.idField] = form.id
  }

  return payload
}

const loadInfo = async (showMessage = false) => {
  loading.value = true
  try {
    const response = await props.getInfo()
    applyData(response.data || null)
    if (showMessage) {
      ElMessage.success('刷新成功')
    }
  } catch (error) {
    console.error(`获取${props.moduleLabel}信息失败:`, error)
    ElMessage.error(`获取${props.moduleLabel}信息失败`)
    applyData(null)
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  if (!formRef.value) {
    return
  }

  const actionText = form.id != null ? '更新' : '保存'

  try {
    await formRef.value.validate()
    submitting.value = true

    const payload = buildPayload()
    if (form.id != null) {
      await props.update(payload)
    } else {
      await props.add(payload)
    }

    ElMessage.success(`${props.moduleLabel}${actionText}成功`)
    await loadInfo()
  } catch (error) {
    console.error(`${props.moduleLabel}${actionText}失败:`, error)
    ElMessage.error(`${props.moduleLabel}${actionText}失败`)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadInfo()
})
</script>

<style scoped lang="scss">
.singleton-content-manager {
  .editor-tip {
    margin-bottom: 20px;
  }

  .content-form {
    display: flex;
    flex-direction: column;
    gap: 20px;
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

  .preview-content {
    color: #1f2937;
    line-height: 1.7;
    word-break: break-word;
  }

  .preview-title {
    margin: 0 0 16px;
    color: #111827;
    line-height: 1.4;

    :deep(p) {
      margin: 0;
    }
  }

  .preview-body {
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

  .preview-placeholder {
    min-height: 160px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #9ca3af;
    font-size: 14px;
  }

  .form-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;
  }

  .update-time {
    color: #6b7280;
    font-size: 14px;
  }

  .actions {
    display: flex;
    gap: 12px;
    margin-left: auto;
  }
}

@media (max-width: 768px) {
  .singleton-content-manager {
    .editor-preview-layout {
      grid-template-columns: 1fr;
    }

    .form-footer {
      flex-direction: column;
      align-items: flex-start;
    }

    .actions {
      width: 100%;
      justify-content: flex-end;
    }
  }
}
</style>

<template>
  <div class="singleton-content-manager">
    <el-card class="content-card">
      <el-alert
        title="支持 Markdown 编辑与预览"
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
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="form.title"
            :placeholder="`请输入${moduleLabel}标题`"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <MarkdownEditor v-model="form.content" :height="editorHeight" />
        </el-form-item>

        <div class="form-footer">
          <div class="update-time">
            <span v-if="form.updatedTime">最近更新：{{ formatDateTime(form.updatedTime) }}</span>
          </div>
          <div class="actions">
            <el-button @click="loadInfo(true)" :loading="loading">刷新</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="submitting">
              保存
            </el-button>
          </div>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import MarkdownEditor from '@/components/MarkdownEditor.vue'
import { formatDateTime } from '@/utils'

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
}

const props = withDefaults(defineProps<Props>(), {
  updatedField: '',
  editorHeight: '460px'
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

const rules: FormRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const applyData = (data?: Record<string, any> | null) => {
  form.id = data?.[props.idField] ?? undefined
  form.title = data?.[props.titleField] ?? ''
  form.content = data?.[props.contentField] ?? ''
  form.updatedTime = props.updatedField ? data?.[props.updatedField] ?? '' : ''
}

const buildPayload = () => {
  const payload: Record<string, any> = {
    [props.titleField]: form.title,
    [props.contentField]: form.content
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

  try {
    await formRef.value.validate()
    submitting.value = true

    const payload = buildPayload()
    if (form.id != null) {
      await props.update(payload)
    } else {
      await props.add(payload)
    }

    ElMessage.success(`${props.moduleLabel}保存成功`)
    await loadInfo()
  } catch (error) {
    console.error(`${props.moduleLabel}保存失败:`, error)
    ElMessage.error(`${props.moduleLabel}保存失败`)
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

  .form-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;
    margin-top: 20px;

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
}

@media (max-width: 768px) {
  .singleton-content-manager {
    .form-footer {
      flex-direction: column;
      align-items: flex-start;

      .actions {
        width: 100%;
        justify-content: flex-end;
      }
    }
  }
}
</style>

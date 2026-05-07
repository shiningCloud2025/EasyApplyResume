<template>
  <div class="announcement-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>{{ isAdminType ? '管理端公告管理' : '用户端公告管理' }}</h2>
    </div>

    <!-- 端类型切换 -->
    <div class="type-tabs">
      <el-radio-group v-model="currentType" @change="handleTypeChange">
        <el-radio-button value="admin">管理端公告</el-radio-button>
        <el-radio-button value="user">用户端公告</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 公告编辑表单 -->
    <div class="announcement-form" v-loading="loading">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="公告标题" prop="title">
          <el-input
            v-model="formData.title"
            placeholder="请输入公告标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="公告内容" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            :rows="6"
            placeholder="请输入公告内容"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="公告类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择公告类型" style="width: 200px;">
            <el-option label="普通公告" value="normal" />
            <el-option label="重要公告" value="important" />
            <el-option label="紧急公告" value="urgent" />
          </el-select>
        </el-form-item>

        <el-form-item label="显示状态" prop="status">
          <el-switch
            v-model="formData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="显示"
            inactive-text="隐藏"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="saving">
            <el-icon><Check /></el-icon>
            {{ hasExisting ? '更新公告' : '创建公告' }}
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 当前公告预览 -->
    <div class="preview-section" v-if="formData.title || formData.content">
      <h3>公告预览</h3>
      <div class="preview-card" :class="formData.type">
        <div class="preview-header">
          <span class="preview-type-tag" :class="formData.type">
            {{ typeLabels[formData.type] || '普通公告' }}
          </span>
          <span class="preview-status" :class="{ active: formData.status === 1 }">
            {{ formData.status === 1 ? '显示中' : '已隐藏' }}
          </span>
        </div>
        <h4 class="preview-title">{{ formData.title || '暂无标题' }}</h4>
        <p class="preview-content">{{ formData.content || '暂无内容' }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Check, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { adminAnnouncementApi, userAnnouncementApi } from '@/api'

const route = useRoute()
const router = useRouter()

const currentType = ref<'admin' | 'user'>('admin')
const isAdminType = computed(() => currentType.value === 'admin')
const api = computed(() => isAdminType.value ? adminAnnouncementApi : userAnnouncementApi)

const loading = ref(false)
const saving = ref(false)
const hasExisting = ref(false)
const formRef = ref<FormInstance>()

const formData = reactive({
  id: null as number | null,
  title: '',
  content: '',
  type: 'normal',
  status: 1
})

const typeLabels: Record<string, string> = {
  normal: '普通公告',
  important: '重要公告',
  urgent: '紧急公告'
}

const formRules: FormRules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
}

const handleTypeChange = (type: 'admin' | 'user') => {
  router.replace({ query: { type } })
  loadData()
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await api.value.getInfo()
    if (res) {
      Object.assign(formData, res)
      hasExisting.value = true
    } else {
      resetForm()
      hasExisting.value = false
    }
  } catch (error) {
    console.error('加载公告失败', error)
    resetForm()
    hasExisting.value = false
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  formData.id = null
  formData.title = ''
  formData.content = ''
  formData.type = 'normal'
  formData.status = 1
}

const handleReset = () => {
  loadData()
}

const handleSave = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    saving.value = true

    if (hasExisting.value) {
      await api.value.update(formData)
      ElMessage.success('公告更新成功')
    } else {
      await api.value.add(formData)
      ElMessage.success('公告创建成功')
      hasExisting.value = true
    }

    loadData()
  } catch (error) {
    console.error('保存失败', error)
  } finally {
    saving.value = false
  }
}

watch(() => route.query.type, (type) => {
  if (type === 'admin' || type === 'user') {
    currentType.value = type
    loadData()
  }
}, { immediate: true })

onMounted(() => {
  const type = route.query.type as string
  if (type === 'admin' || type === 'user') {
    currentType.value = type
  }
  loadData()
})
</script>

<style scoped lang="scss">
.announcement-page {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.page-header {
  margin-bottom: 20px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0;
  }
}

.type-tabs {
  margin-bottom: 24px;
}

.announcement-form {
  max-width: 700px;
  padding: 24px;
  background: #f9fafb;
  border-radius: 8px;
  margin-bottom: 24px;
}

.preview-section {
  h3 {
    font-size: 15px;
    font-weight: 600;
    color: #374151;
    margin-bottom: 12px;
  }
}

.preview-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  border-left: 4px solid #10b981;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  max-width: 700px;

  &.important {
    border-left-color: #f59e0b;
  }

  &.urgent {
    border-left-color: #ef4444;
  }
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.preview-type-tag {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
  background: #d1fae5;
  color: #059669;

  &.important {
    background: #fef3c7;
    color: #d97706;
  }

  &.urgent {
    background: #fee2e2;
    color: #dc2626;
  }
}

.preview-status {
  font-size: 12px;
  color: #9ca3af;

  &.active {
    color: #10b981;
  }
}

.preview-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px 0;
}

.preview-content {
  font-size: 14px;
  color: #4b5563;
  line-height: 1.6;
  margin: 0;
  white-space: pre-wrap;
}
@media (max-width: 768px) {
  .announcement-page {
    padding: 16px;
  }

  .announcement-form,
  .preview-card {
    max-width: 100%;
    padding: 16px;
  }

  .preview-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>

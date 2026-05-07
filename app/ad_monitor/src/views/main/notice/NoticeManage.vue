<template>
  <div class="notice-page">
    <div class="page-header">
      <h2>{{ pageTitle }}</h2>
      <el-tag :type="tagType">{{ endpoint }}</el-tag>
    </div>

    <div class="notice-form" v-loading="loading">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="公告标题" prop="title">
          <el-input
            v-model="formData.title"
            placeholder="请输入公告标题"
            maxlength="35"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="公告内容" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            :rows="8"
            :placeholder="hasData ? '请输入公告内容' : '请输入公告内容（可空，默认显示“暂无公告”）'"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="saving">
            {{ hasData ? '更新公告' : '发布公告' }}
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 预览 -->
    <div class="preview-section" v-if="formData.title || formData.content">
      <h3>公告预览</h3>
      <div class="preview-card">
        <h4>{{ formData.title || '暂无标题' }}</h4>
        <p>{{ formData.content || '暂无内容' }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { adminAnnouncementApi, userAnnouncementApi, monitorAnnouncementApi } from '@/api'

const route = useRoute()

// 根据路由判断端类型
const endpoint = computed(() => {
  if (route.path.includes('/notice/admin')) return '管理端'
  if (route.path.includes('/notice/user')) return '用户端'
  return '监测端'
})

const pageTitle = computed(() => `${endpoint.value}公告管理`)

const tagType = computed(() => {
  if (endpoint.value === '管理端') return 'success'
  if (endpoint.value === '用户端') return 'primary'
  return 'warning'
})

// 获取对应的API
const api = computed(() => {
  if (endpoint.value === '用户端') return userAnnouncementApi
  if (endpoint.value === '监测端') return monitorAnnouncementApi
  return adminAnnouncementApi
})

const loading = ref(false)
const saving = ref(false)
const hasData = ref(false)
const formRef = ref<FormInstance>()

const formData = reactive({
  title: '',
  content: ''
})

const rules = computed<FormRules>(() => ({
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' },
    { max: 35, message: '标题最多35个字符', trigger: 'blur' }
  ],
  content: hasData.value
    ? [{ required: true, message: '修改时公告内容不能为空', trigger: 'blur' }]
    : [] // 新增时内容可空，后端默认“暂无公告”
}))

const loadData = async () => {
  loading.value = true
  try {
    const res = await api.value.getInfo()
    console.log('查询结果:', res)
    if (res && res.announcementTitle) {
      formData.title = res.announcementTitle
      formData.content = res.announcementContent || ''
      hasData.value = true
    } else {
      resetForm()
      hasData.value = false
    }
  } catch (e) {
    console.log('查询无数据，可新增')
    resetForm()
    hasData.value = false
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  formData.title = ''
  formData.content = ''
}

const handleReset = () => {
  loadData()
}

const handleSave = async () => {
  if (!formRef.value) return
  if (saving.value) return // 防止重复提交

  try {
    await formRef.value.validate()
    saving.value = true

    const data = {
      announcementTitle: formData.title.trim(),
      announcementContent: formData.content.trim() || undefined // 新增时不传空内容，由后端设默认值
    }
    console.log('发送数据:', data, 'hasData:', hasData.value)

    if (hasData.value) {
      // 修改时需要传announcementId，公告只有一条，固定传1
      await api.value.update({ ...data, announcementId: 1 })
      ElMessage.success('公告更新成功')
    } else {
      await api.value.add(data)
      ElMessage.success('公告发布成功')
      hasData.value = true
    }
  } catch (e) {
    console.error('保存失败', e)
  } finally {
    saving.value = false
  }
}

// 路由变化时重新加载
let currentPath = ''
watch(() => route.path, (path) => {
  if (path !== currentPath) {
    currentPath = path
    loadData()
  }
})

onMounted(() => {
  currentPath = route.path
  loadData()
})
</script>

<style scoped lang="scss">
.notice-page {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e5e7eb;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0;
  }
}

.notice-form {
  max-width: 700px;
  padding: 20px;
  background: #f9fafb;
  border-radius: 8px;
  margin-bottom: 24px;
}

.preview-section {
  h3 {
    font-size: 15px;
    font-weight: 600;
    color: #374151;
    margin: 0 0 12px 0;
  }
}

.preview-card {
  max-width: 700px;
  background: white;
  border-radius: 8px;
  padding: 20px;
  border-left: 4px solid #10b981;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  h4 {
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 8px 0;
  }

  p {
    font-size: 14px;
    color: #4b5563;
    line-height: 1.6;
    margin: 0;
    white-space: pre-wrap;
  }
}
@media (max-width: 768px) {
  .notice-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .notice-form,
  .preview-card {
    max-width: 100%;
    padding: 16px;
  }
}
</style>

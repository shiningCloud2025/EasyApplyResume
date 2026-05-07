<template>
  <div class="advertisement-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>{{ isAdminType ? '管理端广告管理' : '用户端广告管理' }}</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增广告
      </el-button>
    </div>

    <!-- 端类型切换 -->
    <div class="type-tabs">
      <el-radio-group v-model="currentType" @change="handleTypeChange">
        <el-radio-button value="admin">管理端广告</el-radio-button>
        <el-radio-button value="user">用户端广告</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 广告列表 -->
    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="广告标题" min-width="150" />
        <el-table-column prop="imageUrl" label="广告图片" width="120">
          <template #default="{ row }">
            <el-image
              v-if="row.imageUrl"
              :src="row.imageUrl"
              :preview-src-list="[row.imageUrl]"
              fit="cover"
              style="width: 80px; height: 50px; border-radius: 4px;"
            />
            <span v-else class="text-gray">暂无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="linkUrl" label="跳转链接" min-width="200">
          <template #default="{ row }">
            <el-link v-if="row.linkUrl" :href="row.linkUrl" target="_blank" type="primary">
              {{ row.linkUrl.length > 30 ? row.linkUrl.slice(0, 30) + '...' : row.linkUrl }}
            </el-link>
            <span v-else class="text-gray">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑广告' : '新增广告'"
      :width="dialogWidth"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="广告标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入广告标题" maxlength="50" show-word-limit />
        </el-form-item>

        <el-form-item label="广告图片" prop="imageUrl">
          <el-input v-model="formData.imageUrl" placeholder="请输入图片URL地址" />
          <div v-if="formData.imageUrl" class="image-preview">
            <el-image :src="formData.imageUrl" fit="contain" style="max-height: 100px;" />
          </div>
        </el-form-item>

        <el-form-item label="跳转链接" prop="linkUrl">
          <el-input v-model="formData.linkUrl" placeholder="请输入跳转链接URL" />
        </el-form-item>

        <el-form-item label="广告描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入广告描述"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" :max="9999" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="formData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { adminAdvertisementApi, userAdvertisementApi } from '@/api'

const route = useRoute()
const router = useRouter()

const currentType = ref<'admin' | 'user'>('admin')
const isAdminType = computed(() => currentType.value === 'admin')
const api = computed(() => isAdminType.value ? adminAdvertisementApi : userAdvertisementApi)

const loading = ref(false)
const tableData = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref<FormInstance>()
const dialogWidth = computed(() => (window.innerWidth <= 768 ? '94%' : '600px'))

const formData = reactive({
  id: null as number | null,
  title: '',
  imageUrl: '',
  linkUrl: '',
  description: '',
  sort: 0,
  status: 1
})

const formRules: FormRules = {
  title: [{ required: true, message: '请输入广告标题', trigger: 'blur' }]
}

const handleTypeChange = (type: 'admin' | 'user') => {
  router.replace({ query: { type } })
  pageNum.value = 1
  loadData()
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await api.value.getByPage(pageNum.value, pageSize.value)
    tableData.value = res?.records || res || []
    total.value = res?.total || tableData.value.length
  } catch (error) {
    console.error('加载广告列表失败', error)
    tableData.value = []
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  formData.id = null
  formData.title = ''
  formData.imageUrl = ''
  formData.linkUrl = ''
  formData.description = ''
  formData.sort = 0
  formData.status = 1
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该广告吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await api.value.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    // 用户取消或删除失败
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true

    if (isEdit.value) {
      await api.value.update(formData)
      ElMessage.success('更新成功')
    } else {
      await api.value.add(formData)
      ElMessage.success('创建成功')
    }

    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败', error)
  } finally {
    submitting.value = false
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
.advertisement-page {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0;
  }
}

.type-tabs {
  margin-bottom: 20px;
}

.table-container {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
}

.pagination-container {
  padding: 16px;
  display: flex;
  justify-content: flex-end;
  background: #f9fafb;
}

.text-gray {
  color: #9ca3af;
  font-size: 13px;
}

.image-preview {
  margin-top: 8px;
  padding: 8px;
  background: #f9fafb;
  border-radius: 4px;
  display: inline-block;
}
@media (max-width: 768px) {
  .advertisement-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .type-tabs {
    overflow-x: auto;
  }

  .pagination-container {
    justify-content: center;
  }
}
</style>

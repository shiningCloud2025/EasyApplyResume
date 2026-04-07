<template>
  <div class="ad-page">
    <div class="page-header">
      <div class="header-left">
        <h2>{{ pageTitle }}</h2>
        <el-tag :type="tagType">{{ endpoint }}</el-tag>
      </div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增广告
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchName"
        placeholder="请输入广告名称"
        clearable
        style="width: 240px"
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 广告列表 -->
    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="advertisementId" label="ID" width="70" />
        <el-table-column prop="advertisementName" label="广告名称" min-width="120" show-overflow-tooltip />
        <el-table-column label="广告图片" width="120">
          <template #default="{ row }">
            <el-image
              v-if="row.advertisementUrl"
              :src="row.advertisementUrl"
              :preview-src-list="[row.advertisementUrl]"
              fit="cover"
              style="width: 80px; height: 50px; border-radius: 4px;"
            />
            <span v-else class="text-gray">暂无</span>
          </template>
        </el-table-column>
        <el-table-column label="跳转链接" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-link v-if="row.advertisementLink" :href="row.advertisementLink" target="_blank" type="primary">
              {{ row.advertisementLink.length > 30 ? row.advertisementLink.slice(0, 30) + '...' : row.advertisementLink }}
            </el-link>
            <span v-else class="text-gray">-</span>
          </template>
        </el-table-column>
        <el-table-column label="开始时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.advertisementStartedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="结束时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.advertisementEndTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="info" link size="small" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="loadData"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑广告' : '新增广告'"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="广告名称" prop="advertisementName">
          <el-input
            v-model="formData.advertisementName"
            placeholder="请输入广告名称"
            maxlength="25"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="广告图片" prop="advertisementUrl">
          <el-upload
            class="ad-uploader"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :http-request="handleUpload"
            accept="image/*"
          >
            <el-image
              v-if="formData.advertisementUrl"
              :src="formData.advertisementUrl"
              fit="contain"
              style="width: 200px; height: 120px; border-radius: 4px;"
            />
            <div v-else class="upload-placeholder">
              <el-icon :size="28"><Plus /></el-icon>
              <span>点击上传广告图片</span>
            </div>
          </el-upload>
          <el-progress
            v-if="uploading"
            :percentage="100"
            status="success"
            :indeterminate="true"
            style="margin-top: 8px; width: 200px;"
          />
        </el-form-item>
        <el-form-item label="跳转链接" prop="advertisementLink">
          <el-input
            v-model="formData.advertisementLink"
            placeholder="请输入跳转链接"
            maxlength="3000"
            type="textarea"
            :rows="2"
          />
        </el-form-item>
        <el-form-item label="有效期" prop="timeRange">
          <el-date-picker
            v-model="formData.timeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
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

    <!-- 查看对话框 -->
    <el-dialog v-model="viewDialogVisible" title="查看广告" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="ID">{{ viewData.advertisementId }}</el-descriptions-item>
        <el-descriptions-item label="广告名称">{{ viewData.advertisementName }}</el-descriptions-item>
        <el-descriptions-item label="广告图片">
          <el-image
            v-if="viewData.advertisementUrl"
            :src="viewData.advertisementUrl"
            :preview-src-list="[viewData.advertisementUrl]"
            fit="contain"
            style="max-height: 150px;"
          />
          <span v-else class="text-gray">暂无</span>
        </el-descriptions-item>
        <el-descriptions-item label="跳转链接">
          <el-link v-if="viewData.advertisementLink" :href="viewData.advertisementLink" target="_blank" type="primary">
            {{ viewData.advertisementLink }}
          </el-link>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ formatDate(viewData.advertisementStartedTime) }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ formatDate(viewData.advertisementEndTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { adminAdvertisementApi, userAdvertisementApi, monitorAdvertisementApi, fileApi } from '@/api'

const route = useRoute()

const endpoint = computed(() => {
  if (route.path.includes('/ad/image/admin')) return '管理端'
  if (route.path.includes('/ad/image/user')) return '用户端'
  return '监测端'
})

const pageTitle = computed(() => `${endpoint.value}图片广告管理`)

const tagType = computed(() => {
  if (endpoint.value === '管理端') return 'success'
  if (endpoint.value === '用户端') return 'primary'
  return 'warning'
})

const api = computed(() => {
  if (endpoint.value === '用户端') return userAdvertisementApi
  if (endpoint.value === '监测端') return monitorAdvertisementApi
  return adminAdvertisementApi
})

const loading = ref(false)
const tableData = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchName = ref('')

const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const uploading = ref(false)
const formRef = ref<FormInstance>()

const viewData = reactive({
  advertisementId: null as number | null,
  advertisementName: '',
  advertisementUrl: '',
  advertisementLink: '',
  advertisementStartedTime: '',
  advertisementEndTime: ''
})

const formData = reactive({
  advertisementId: null as number | null,
  advertisementName: '',
  advertisementUrl: '',
  advertisementLink: '',
  timeRange: null as [string, string] | null
})

// 根据端点选择上传API
const getUploadApi = () => {
  if (endpoint.value === '管理端') return fileApi.uploadAdminAdImg
  if (endpoint.value === '用户端') return fileApi.uploadUserAdImg
  return fileApi.uploadAdmonitorAdImg
}

const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

const handleUpload = async (options: any) => {
  uploading.value = true
  try {
    const url = await getUploadApi()(options.file)
    formData.advertisementUrl = url
    ElMessage.success('上传成功')
  } catch (e) {
    ElMessage.error('上传失败')
  } finally {
    uploading.value = false
  }
}

const rules: FormRules = {
  advertisementName: [
    { required: true, message: '请输入广告名称', trigger: 'blur' },
    { max: 25, message: '广告名称最多25个字符', trigger: 'blur' }
  ],
  advertisementUrl: [
    { required: true, message: '请上传广告图片', trigger: 'change' }
  ],
  advertisementLink: [
    { required: true, message: '请输入跳转链接', trigger: 'blur' },
    { max: 3000, message: '跳转链接最多3000个字符', trigger: 'blur' }
  ],
  timeRange: [
    { required: true, message: '请选择有效期', trigger: 'change' }
  ]
}

const formatDate = (date: string) => {
  if (!date) return '-'
  return date.slice(0, 10)
}

const loadData = async () => {
  loading.value = true
  try {
    const query = searchName.value.trim()
      ? { advertisementName: searchName.value.trim() }
      : {}
    const res = await api.value.getByPage(pageNum.value, pageSize.value, query)
    tableData.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) {
    console.error('加载失败', e)
    tableData.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageNum.value = 1
  loadData()
}

const resetSearch = () => {
  searchName.value = ''
  pageNum.value = 1
  loadData()
}

const handleSizeChange = () => {
  pageNum.value = 1
  loadData()
}

const resetForm = () => {
  formData.advertisementId = null
  formData.advertisementName = ''
  formData.advertisementUrl = ''
  formData.advertisementLink = ''
  formData.timeRange = null
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleView = (row: any) => {
  viewData.advertisementId = row.advertisementId
  viewData.advertisementName = row.advertisementName || ''
  viewData.advertisementUrl = row.advertisementUrl || ''
  viewData.advertisementLink = row.advertisementLink || ''
  viewData.advertisementStartedTime = row.advertisementStartedTime || ''
  viewData.advertisementEndTime = row.advertisementEndTime || ''
  viewDialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  formData.advertisementId = row.advertisementId
  formData.advertisementName = row.advertisementName || ''
  formData.advertisementUrl = row.advertisementUrl || ''
  formData.advertisementLink = row.advertisementLink || ''
  // 设置时间范围
  if (row.advertisementStartedTime && row.advertisementEndTime) {
    formData.timeRange = [
      row.advertisementStartedTime.slice(0, 10),
      row.advertisementEndTime.slice(0, 10)
    ]
  } else {
    formData.timeRange = null
  }
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该广告吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await api.value.delete(row.advertisementId)
    ElMessage.success('删除成功')
    loadData()
  } catch {}
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitting.value = true

    const data = {
      advertisementName: formData.advertisementName.trim(),
      advertisementUrl: formData.advertisementUrl.trim(),
      advertisementLink: formData.advertisementLink.trim(),
      advertisementStartedTime: formData.timeRange![0],
      advertisementEndTime: formData.timeRange![1]
    }

    if (isEdit.value) {
      await api.value.update({ ...data, advertisementId: formData.advertisementId })
      ElMessage.success('更新成功')
    } else {
      await api.value.add(data)
      ElMessage.success('创建成功')
    }

    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error('提交失败', e)
  } finally {
    submitting.value = false
  }
}

watch(() => route.path, () => {
  pageNum.value = 1
  loadData()
})

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.ad-page {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0;
  }
}

.table-container {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.pagination {
  padding: 16px;
  display: flex;
  justify-content: flex-end;
  background: #f9fafb;
}

.text-gray {
  color: #9ca3af;
  font-size: 13px;
}

.ad-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    overflow: hidden;
    transition: border-color 0.3s;
    
    &:hover {
      border-color: #409eff;
    }
  }
}

.upload-placeholder {
  width: 200px;
  height: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #8c939d;
  gap: 8px;
  
  span {
    font-size: 12px;
  }
}
</style>

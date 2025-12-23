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
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
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
          @size-change="loadData"
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
        <el-form-item label="图片地址" prop="advertisementUrl">
          <el-input v-model="formData.advertisementUrl" placeholder="请输入图片URL" />
          <el-image 
            v-if="formData.advertisementUrl" 
            :src="formData.advertisementUrl" 
            fit="contain" 
            style="max-height: 80px; margin-top: 8px;" 
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
            v-model="timeRange"
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { adminAdvertisementApi, userAdvertisementApi, monitorAdvertisementApi } from '@/api'

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

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref<FormInstance>()
const timeRange = ref<[string, string] | null>(null)

const formData = reactive({
  advertisementId: null as number | null,
  advertisementName: '',
  advertisementUrl: '',
  advertisementLink: '',
  advertisementStartedTime: '',
  advertisementEndTime: ''
})

const rules: FormRules = {
  advertisementName: [
    { required: true, message: '请输入广告名称', trigger: 'blur' },
    { max: 25, message: '广告名称最多25个字符', trigger: 'blur' }
  ],
  advertisementUrl: [
    { required: true, message: '请输入图片地址', trigger: 'blur' }
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
  return date.replace('T', ' ').slice(0, 19)
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await api.value.getByPage(pageNum.value, pageSize.value, {})
    tableData.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) {
    console.error('加载失败', e)
    tableData.value = []
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  formData.advertisementId = null
  formData.advertisementName = ''
  formData.advertisementUrl = ''
  formData.advertisementLink = ''
  formData.advertisementStartedTime = ''
  formData.advertisementEndTime = ''
  timeRange.value = null
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  formData.advertisementId = row.advertisementId
  formData.advertisementName = row.advertisementName || ''
  formData.advertisementUrl = row.advertisementUrl || ''
  formData.advertisementLink = row.advertisementLink || ''
  formData.advertisementStartedTime = row.advertisementStartedTime || ''
  formData.advertisementEndTime = row.advertisementEndTime || ''
  // 设置时间范围
  if (row.advertisementStartedTime && row.advertisementEndTime) {
    timeRange.value = [
      formatDate(row.advertisementStartedTime),
      formatDate(row.advertisementEndTime)
    ]
  } else {
    timeRange.value = null
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

  // 校验时间范围
  if (!timeRange.value || timeRange.value.length !== 2) {
    return
  }

  try {
    await formRef.value.validate()
    submitting.value = true

    const data = {
      advertisementName: formData.advertisementName.trim(),
      advertisementUrl: formData.advertisementUrl.trim(),
      advertisementLink: formData.advertisementLink.trim(),
      advertisementStartedTime: timeRange.value[0],
      advertisementEndTime: timeRange.value[1]
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
</style>

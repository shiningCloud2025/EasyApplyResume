<template>
  <div class="server-page">
    <div class="page-header">
      <div class="header-left">
        <h2>设备管理</h2>
        <el-tag type="success">服务器管理</el-tag>
      </div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增服务器
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input 
        v-model="searchName" 
        placeholder="搜索服务器名称" 
        clearable 
        style="width: 240px;"
        @keyup.enter="loadData"
      />
      <el-button type="primary" @click="loadData">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 服务器列表 -->
    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="serviceMachineId" label="ID" width="70" />
        <el-table-column prop="serviceMachineName" label="服务器名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="serviceMachineHost" label="外网IP/域名" min-width="150" show-overflow-tooltip />
        <el-table-column prop="serviceMachinePort" label="SSH端口" width="100" />
        <el-table-column prop="serviceMachineUsername" label="登录账号" width="120" />
        <el-table-column label="密码" width="100">
          <template #default>
            <span class="text-gray">******</span>
          </template>
        </el-table-column>
        <el-table-column prop="serviceMachineRemark" label="备注" min-width="120" show-overflow-tooltip />
        <el-table-column label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.serviceMachineCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.serviceMachineUpdatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="success" link size="small" @click="handleTestConnect(row)">测试连接</el-button>
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
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑服务器' : '新增服务器'"
      :width="dialogWidth"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="服务器名称" prop="serviceMachineName">
          <el-input 
            v-model="formData.serviceMachineName" 
            placeholder="请输入服务器名称" 
            maxlength="25" 
            show-word-limit 
          />
        </el-form-item>
        <el-form-item label="外网IP/域名" prop="serviceMachineHost">
          <el-input 
            v-model="formData.serviceMachineHost" 
            placeholder="请输入服务器IP或域名" 
            maxlength="255"
          />
        </el-form-item>
        <el-form-item label="SSH端口" prop="serviceMachinePort">
          <el-input-number 
            v-model="formData.serviceMachinePort" 
            :min="1" 
            :max="65535" 
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="登录账号" prop="serviceMachineUsername">
          <el-input 
            v-model="formData.serviceMachineUsername" 
            placeholder="请输入登录账号" 
            maxlength="60"
          />
        </el-form-item>
        <el-form-item label="登录密码" prop="serviceMachinePassword">
          <el-input
            v-model="formData.serviceMachinePassword"
            type="password"
            placeholder="请输入登录密码"
            maxlength="60"
            show-password
          />
        </el-form-item>
        <el-form-item label="备注" prop="serviceMachineRemark">
          <el-input 
            v-model="formData.serviceMachineRemark" 
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息" 
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="连接测试">
          <div class="test-connect-area">
            <el-button 
              type="success" 
              @click="handleFormTestConnect" 
              :loading="testing"
              :disabled="!canTest"
            >
              测试连接
            </el-button>
            <el-tag v-if="connectTested" type="success" style="margin-left: 12px;">连接成功</el-tag>
            <span v-else class="test-hint">* 必须测试连接成功才能保存</span>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting" :disabled="!connectTested">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog v-model="viewDialogVisible" title="查看服务器" :width="dialogWidth">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="ID">{{ viewData.serviceMachineId }}</el-descriptions-item>
        <el-descriptions-item label="服务器名称">{{ viewData.serviceMachineName }}</el-descriptions-item>
        <el-descriptions-item label="外网IP/域名">{{ viewData.serviceMachineHost }}</el-descriptions-item>
        <el-descriptions-item label="SSH端口">{{ viewData.serviceMachinePort }}</el-descriptions-item>
        <el-descriptions-item label="登录账号">{{ viewData.serviceMachineUsername }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewData.serviceMachineRemark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(viewData.serviceMachineCreatedTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDate(viewData.serviceMachineUpdatedTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { serviceMachineManageApi } from '@/api'
import { useAuthStore, serviceMachineManagementPermissions } from '@/store/auth'

const authStore = useAuthStore()
const canViewPage = computed(() => authStore.canAccessRoute(serviceMachineManagementPermissions.getByPage))
const canViewDetail = computed(() => authStore.canAccessRoute(serviceMachineManagementPermissions.getInfo))
const canAdd = computed(() => authStore.canAccessRoute(serviceMachineManagementPermissions.add))
const canUpdate = computed(() => authStore.canAccessRoute(serviceMachineManagementPermissions.update))
const canDelete = computed(() => authStore.canAccessRoute(serviceMachineManagementPermissions.delete))

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
const testing = ref(false)
const connectTested = ref(false)
const formRef = ref<FormInstance>()
const dialogWidth = computed(() => (window.innerWidth <= 768 ? '94%' : '550px'))

// 是否可以测试连接（必填字段都填写了）
const canTest = computed(() => {
  return formData.serviceMachineHost && 
         formData.serviceMachinePort && 
         formData.serviceMachineUsername && 
         formData.serviceMachinePassword
})

const viewData = reactive({
  serviceMachineId: null as number | null,
  serviceMachineName: '',
  serviceMachineHost: '',
  serviceMachinePort: 22,
  serviceMachineUsername: '',
  serviceMachineRemark: '',
  serviceMachineCreatedTime: '',
  serviceMachineUpdatedTime: ''
})

const formData = reactive({
  serviceMachineId: null as number | null,
  serviceMachineName: '',
  serviceMachineHost: '',
  serviceMachinePort: 22,
  serviceMachineUsername: '',
  serviceMachinePassword: '',
  serviceMachineRemark: ''
})

const rules: FormRules = {
  serviceMachineName: [
    { required: true, message: '请输入服务器名称', trigger: 'blur' },
    { max: 25, message: '服务器名称最多25个字符', trigger: 'blur' }
  ],
  serviceMachineHost: [
    { required: true, message: '请输入外网IP/域名', trigger: 'blur' },
    { max: 255, message: '外网IP/域名最多255个字符', trigger: 'blur' }
  ],
  serviceMachinePort: [
    { required: true, message: '请输入SSH端口', trigger: 'blur' }
  ],
  serviceMachineUsername: [
    { required: true, message: '请输入登录账号', trigger: 'blur' },
    { max: 60, message: '登录账号最多60个字符', trigger: 'blur' }
  ],
  serviceMachinePassword: [
    { required: true, message: '请输入登录密码', trigger: 'blur' },
    { max: 60, message: '登录密码最多60个字符', trigger: 'blur' }
  ],
  serviceMachineRemark: [
    { max: 200, message: '备注最多200个字符', trigger: 'blur' }
  ]
}

const formatDate = (date: string) => {
  if (!date) return '-'
  return date.slice(0, 10)
}

const loadData = async () => {
  loading.value = true
  try {
    const query = searchName.value ? { serviceMachineName: searchName.value } : {}
    const res = await serviceMachineManageApi.getByPage(pageNum.value, pageSize.value, query)
    tableData.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) {
    console.error('加载失败', e)
    tableData.value = []
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchName.value = ''
  pageNum.value = 1
  loadData()
}

const resetForm = () => {
  formData.serviceMachineId = null
  formData.serviceMachineName = ''
  formData.serviceMachineHost = ''
  formData.serviceMachinePort = 22
  formData.serviceMachineUsername = ''
  formData.serviceMachinePassword = ''
  formData.serviceMachineRemark = ''
  connectTested.value = false
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 表单内测试连接
const handleFormTestConnect = async () => {
  testing.value = true
  try {
    await serviceMachineManageApi.testConnectForManage({
      serviceMachineHost: formData.serviceMachineHost,
      serviceMachinePort: formData.serviceMachinePort,
      serviceMachineUsername: formData.serviceMachineUsername,
      serviceMachinePassword: formData.serviceMachinePassword
    })
    connectTested.value = true
    ElMessage.success('连接成功！')
  } catch (e) {
    connectTested.value = false
    ElMessage.error('连接失败，请检查配置')
  } finally {
    testing.value = false
  }
}

const handleView = async (row: any) => {
  try {
    const res = await serviceMachineManageApi.getInfo(row.serviceMachineId)
    viewData.serviceMachineId = res?.serviceMachineId ?? row.serviceMachineId ?? null
    viewData.serviceMachineName = res?.serviceMachineName || ''
    viewData.serviceMachineHost = res?.serviceMachineHost || ''
    viewData.serviceMachinePort = res?.serviceMachinePort || 22
    viewData.serviceMachineUsername = res?.serviceMachineUsername || ''
    viewData.serviceMachineRemark = res?.serviceMachineRemark || ''
    viewData.serviceMachineCreatedTime = res?.serviceMachineCreatedTime || ''
    viewData.serviceMachineUpdatedTime = res?.serviceMachineUpdatedTime || ''
    viewDialogVisible.value = true
  } catch (e) {
    console.error('查看详情失败', e)
  }
}

const handleEdit = (row: any) => {
  isEdit.value = true
  formData.serviceMachineId = row.serviceMachineId
  formData.serviceMachineName = row.serviceMachineName || ''
  formData.serviceMachineHost = row.serviceMachineHost || ''
  formData.serviceMachinePort = row.serviceMachinePort || 22
  formData.serviceMachineUsername = row.serviceMachineUsername || ''
  formData.serviceMachinePassword = ''
  formData.serviceMachineRemark = row.serviceMachineRemark || ''
  connectTested.value = false  // 编辑时需要重新测试连接
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该服务器吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await serviceMachineManageApi.delete(row.serviceMachineId)
    ElMessage.success('删除成功')
    loadData()
  } catch {}
}

const handleTestConnect = async (row: any) => {
  try {
    ElMessage.info('正在测试连接...')
    await serviceMachineManageApi.testConnect({
      serviceMachineHost: row.serviceMachineHost,
      serviceMachinePort: row.serviceMachinePort,
      serviceMachineUsername: row.serviceMachineUsername,
      serviceMachinePassword: row.serviceMachinePassword
    })
    ElMessage.success('连接成功！')
  } catch (e) {
    ElMessage.error('连接失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitting.value = true

    const data: any = {
      serviceMachineName: formData.serviceMachineName.trim(),
      serviceMachineHost: formData.serviceMachineHost.trim(),
      serviceMachinePort: formData.serviceMachinePort,
      serviceMachineUsername: formData.serviceMachineUsername.trim(),
      serviceMachineRemark: formData.serviceMachineRemark?.trim() || '无'  // 后端必填，默认填"无"
    }

    // 密码字段：后端必填
    if (!formData.serviceMachinePassword) {
      ElMessage.error('请输入登录密码')
      return
    }
    data.serviceMachinePassword = formData.serviceMachinePassword

    if (isEdit.value) {
      data.serviceMachineId = formData.serviceMachineId
      await serviceMachineManageApi.update(data)
      ElMessage.success('更新成功')
    } else {
      await serviceMachineManageApi.add(data)
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

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.server-page {
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

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
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

.test-connect-area {
  display: flex;
  align-items: center;
}

.test-hint {
  margin-left: 12px;
  color: #f56c6c;
  font-size: 12px;
}
@media (max-width: 768px) {
  .server-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .search-bar {
    flex-direction: column;

    :deep(.el-input) {
      width: 100% !important;
    }
  }

  .test-connect-area {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .test-hint {
    margin-left: 0;
  }

  .pagination {
    justify-content: center;
  }
}
</style>

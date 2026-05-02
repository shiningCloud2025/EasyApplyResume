<template>
  <div class="permission-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">权限管理</h1>
        <p class="page-description">管理系统权限项，构建权限树结构</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
        <el-button v-if="canAddPermission" type="primary" @click="openCreateDialog">
          <el-icon><Plus /></el-icon>
          新增权限
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="权限名称">
          <el-input
            v-model="searchForm.permissionName"
            placeholder="请输入权限名称"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="权限URL">
          <el-input
            v-model="searchForm.permissionUrl"
            placeholder="请输入权限URL"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="权限简介">
          <el-input
            v-model="searchForm.permissionIntroduce"
            placeholder="请输入权限简介"
            clearable
            style="width: 240px"
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

    <!-- 权限表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="permissionId" label="权限ID" width="70" />
        <el-table-column prop="permissionName" label="权限名称" min-width="150" />
        <el-table-column prop="permissionUrl" label="权限URL" min-width="200" />
        <el-table-column prop="permissionIntroduce" label="权限简介" min-width="200" />
        <el-table-column v-if="showPermissionActionColumn" label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="canViewPermissionDetail"
              type="info"
              size="default"
              @click="handleViewDetail(row)"
            >
              查看
            </el-button>
            <el-button v-if="canUpdatePermission" type="primary" size="default" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button v-if="canDeletePermission" type="danger" size="default" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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

    <!-- 创建/编辑权限对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingPermission ? '编辑权限' : '新增权限'"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="permissionFormRef"
        :model="permissionForm"
        :rules="permissionRules"
        label-width="100px"
      >
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="permissionForm.permissionName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限URL" prop="permissionUrl">
          <el-input v-model="permissionForm.permissionUrl" placeholder="请输入权限URL" />
        </el-form-item>
        <el-form-item label="权限简介" prop="permissionIntroduce">
          <el-input
            v-model="permissionForm.permissionIntroduce"
            type="textarea"
            :rows="3"
            placeholder="请输入权限简介"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 权限详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="权限详情"
      width="600px"
    >
      <el-descriptions v-if="currentDetailPermission" :column="1" border>
        <el-descriptions-item label="权限ID">
          {{ currentDetailPermission.permissionId }}
        </el-descriptions-item>
        <el-descriptions-item label="权限名称">
          {{ currentDetailPermission.permissionName }}
        </el-descriptions-item>
        <el-descriptions-item label="权限URL">
          {{ currentDetailPermission.permissionUrl }}
        </el-descriptions-item>
        <el-descriptions-item label="权限简介">
          <div style="white-space: pre-wrap; word-break: break-all;">
            {{ currentDetailPermission.permissionIntroduce || '暂无简介' }}
          </div>
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Plus, Search, RefreshRight } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { permissionApi } from '@/api/admin'
import { useAuthStore } from '@/store/auth'
import type {
  PermissionPageVO,
  PermissionForm,
  PermissionPageQuery,
  PermissionInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

const authStore = useAuthStore()
const canAddPermission = computed(() => authStore.hasPermission('/admin/permission/add'))
const canUpdatePermission = computed(() => authStore.hasPermission('/admin/permission/update'))
const canDeletePermission = computed(() => authStore.hasPermission('/admin/permission/delete'))
const canViewPermissionDetail = computed(() => authStore.hasPermission('/admin/permission/findById'))
const showPermissionActionColumn = computed(() => {
  return canViewPermissionDetail.value || canUpdatePermission.value || canDeletePermission.value
})

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const editingPermission = ref<PermissionPageVO | null>(null)

// 搜索表单
const searchForm = reactive<PermissionPageQuery>({
  permissionName: '',
  permissionUrl: '',
  permissionIntroduce: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 表格数据
const tableData = ref<PermissionPageVO[]>([])

// 权限表单
const permissionFormRef = ref<FormInstance>()
const permissionForm = reactive<PermissionForm>({
  permissionId: undefined,
  permissionName: '',
  permissionUrl: '',
  permissionIntroduce: ''
})

// 表单校验规则
const permissionRules = {
  permissionName: [
    { required: true, message: '请输入权限名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  permissionUrl: [
    { required: true, message: '请输入权限URL', trigger: 'blur' },
    { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
  ],
  permissionIntroduce: [
    { max: 200, message: '简介长度不能超过200个字符', trigger: 'blur' }
  ]
}

// 获取权限列表
const getPermissionList = async () => {
  loading.value = true
  try {
    const response = await permissionApi.getPermissionPage(
      pagination.current,
      pagination.size,
      searchForm
    )
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取权限列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增权限
const openCreateDialog = () => {
  editingPermission.value = null
  resetForm()
  showCreateDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getPermissionList()
}

// 重置搜索
const handleReset = () => {
  searchForm.permissionName = ''
  searchForm.permissionUrl = ''
  searchForm.permissionIntroduce = ''
  pagination.current = 1
  getPermissionList()
}

// 刷新数据
const refreshData = () => {
  getPermissionList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getPermissionList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getPermissionList()
}

// 查看权限详情
const detailDialogVisible = ref(false)
const currentDetailPermission = ref<PermissionInfoVO | null>(null)

const handleViewDetail = async (row: PermissionPageVO) => {
  try {
    const response = await permissionApi.getPermissionInfo(row.permissionId)
    currentDetailPermission.value = response.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取权限详情失败:', error)
    ElMessage.error('获取权限详情失败')
  }
}

// 编辑权限
const handleEdit = (row: PermissionPageVO) => {
  editingPermission.value = row
  Object.assign(permissionForm, {
    permissionId: row.permissionId,
    permissionName: row.permissionName,
    permissionUrl: row.permissionUrl,
    permissionIntroduce: row.permissionIntroduce
  })
  showCreateDialog.value = true
}

// 删除权限
const handleDelete = (row: PermissionPageVO) => {
  ElMessageBox.confirm(`确定要删除权限"${row.permissionName}"吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await permissionApi.deletePermission(row.permissionId)
      ElMessage.success('删除成功')
      getPermissionList()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

// 获取请求方式标签类型
const getRequestTagType = (request: string) => {
  const typeMap: Record<string, string> = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger'
  }
  return typeMap[request] || 'info'
}

// 提交表单
const handleSubmit = async () => {
  if (!permissionFormRef.value) return
  
  try {
    await permissionFormRef.value.validate()
    submitting.value = true
    
    if (editingPermission.value) {
      await permissionApi.updatePermission(permissionForm)
    } else {
      await permissionApi.addPermission(permissionForm)
    }
    
    ElMessage.success(editingPermission.value ? '更新成功' : '创建成功')
    showCreateDialog.value = false
    getPermissionList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (permissionFormRef.value) {
    permissionFormRef.value.resetFields()
  }
  
  editingPermission.value = null
  Object.assign(permissionForm, {
    permissionId: undefined,
    permissionName: '',
    permissionUrl: '',
    permissionIntroduce: ''
  })
}

// 组件挂载
onMounted(() => {
  getPermissionList()
})
</script>

<style scoped lang="scss">
.permission-management {
  font-size: 16px;
  
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 24px;
  }

  .header-content {
    .page-title {
      font-size: 24px;
      font-weight: 700;
      color: #1f2937;
      margin-bottom: 8px;
    }

    .page-description {
      font-size: 14px;
      color: #6b7280;
      margin: 0;
    }
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }

  .search-card {
    margin-bottom: 24px;
  }

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }

  .table-card {
    .el-table {
      font-size: 16px;
    }
  }

  .pagination {
    display: flex;
    justify-content: flex-end;
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid #f3f4f6;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .danger {
    color: #ef4444;
    
    &:hover {
      color: #dc2626;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .permission-management {
    .page-header {
      flex-direction: column;
      gap: 16px;
    }
    
    .header-actions {
      width: 100%;
      justify-content: flex-start;
    }
    
    .search-card .el-form {
      .el-form-item {
        display: block;
        margin-bottom: 16px;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        .el-input,
        .el-select {
          width: 100%;
        }
      }
    }
    
    .el-table {
      font-size: 14px;
    }
  }
}
</style>
<template>
  <div class="role-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">角色管理</h1>
        <p class="page-description">管理系统角色权限，配置访问控制</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
        <el-button type="primary" @click="openCreateDialog">
          <el-icon><Plus /></el-icon>
          新增角色
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="角色名称">
          <el-input
            v-model="searchForm.roleName"
            placeholder="请输入角色名称"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="searchForm.roleIntroduce"
            placeholder="请输入角色描述"
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

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="roleId" label="角色ID" width="70" />
        <el-table-column prop="roleName" label="角色名称" min-width="150" />
        <el-table-column label="描述" width="100" align="center">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="default"
              @click="handleViewIntroduce(row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="roleCreatedTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.roleCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="default" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="warning" size="default" @click="handlePermission(row)">
              权限
            </el-button>
            <el-button type="danger" size="default" @click="handleDelete(row)">
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

    <!-- 创建/编辑角色对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingRole ? '编辑角色' : '新增角色'"
      width="500px"
      @close="resetForm"
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="80px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色描述" prop="roleIntroduce">
          <el-input
            v-model="roleForm.roleIntroduce"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
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

    <!-- 权限配置对话框 -->
    <el-dialog
      v-model="showPermissionDialog"
      title="权限配置"
      width="600px"
    >
      <div class="permission-setup">
        <div class="permission-tree">
          <el-tree
            ref="permissionTreeRef"
            :data="permissionTree"
            show-checkbox
            node-key="permissionId"
            :default-expanded-keys="[1, 2, 3]"
            :props="{ children: 'children', label: 'permissionName' }"
          />
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPermissionDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSavePermissions">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 描述详情弹窗 -->
    <el-dialog
      v-model="introduceDialogVisible"
      title="角色描述"
      width="600px"
    >
      <el-card v-if="currentIntroduceRole">
        <template #header>
          <div style="font-weight: 600; font-size: 16px;">{{ currentIntroduceRole.roleName }}</div>
        </template>
        <div style="padding: 16px; min-height: 100px; white-space: pre-wrap; word-break: break-all; line-height: 1.8;">
          {{ currentIntroduceRole.roleIntroduce || '该角色暂无描述' }}
        </div>
      </el-card>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="introduceDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Plus, Search, RefreshRight } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { roleApi, permissionApi } from '@/api/admin'
import type {
  RolePageVO,
  RoleForm,
  RolePageQuery,
  PermissionInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showPermissionDialog = ref(false)
const editingRole = ref<RolePageVO | null>(null)

// 搜索表单
const searchForm = reactive<RolePageQuery>({
  roleName: '',
  roleIntroduce: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 表格数据
const tableData = ref<RolePageVO[]>([])

// 角色表单
const roleFormRef = ref<FormInstance>()
const roleForm = reactive<RoleForm>({
  roleId: undefined,
  roleName: '',
  roleIntroduce: ''
})

// 表单校验规则
const roleRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  roleIntroduce: [
    { required: true, message: '请输入角色描述', trigger: 'blur' },
    { min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur' }
  ]
}

// 权限树数据
const permissionTree = ref<PermissionInfoVO[]>([])

// 加载所有权限
const loadPermissions = async () => {
  try {
    const response = await permissionApi.getAllPermissions()
    permissionTree.value = response.data
  } catch (error) {
    console.error('加载权限失败:', error)
  }
}

// 获取角色列表
const getRoleList = async () => {
  loading.value = true
  try {
    const response = await roleApi.getRolePage(
      pagination.current,
      pagination.size,
      searchForm
    )
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取角色列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增角色
const openCreateDialog = () => {
  editingRole.value = null
  resetForm()
  showCreateDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getRoleList()
}

// 重置搜索
const handleReset = () => {
  searchForm.roleName = ''
  searchForm.roleIntroduce = ''
  pagination.current = 1
  getRoleList()
}

// 刷新数据
const refreshData = () => {
  getRoleList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getRoleList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getRoleList()
}

// 编辑角色
const handleEdit = (row: RolePageVO) => {
  editingRole.value = row
  Object.assign(roleForm, {
    roleId: row.roleId,
    roleName: row.roleName,
    roleIntroduce: row.roleIntroduce
  })
  showCreateDialog.value = true
}

// 查看描述详情
const introduceDialogVisible = ref(false)
const currentIntroduceRole = ref<RolePageVO | null>(null)

const handleViewIntroduce = (row: RolePageVO) => {
  currentIntroduceRole.value = row
  introduceDialogVisible.value = true
}

// 配置权限
const handlePermission = async (row: RolePageVO) => {
  editingRole.value = row
  try {
    // 获取角色当前权限
    const response = await roleApi.getRolePermissions(row.roleId)
    // 设置权限树的选中状态
    // 这里需要根据实际返回的数据结构调整
  } catch (error) {
    console.error('获取角色权限失败:', error)
  }
  showPermissionDialog.value = true
}

// 保存权限
const handleSavePermissions = async () => {
  if (!editingRole.value) return
  
  try {
    const selectedPermissionIds = permissionTree.value.map(p => p.permissionId)
    await roleApi.assignPermissionToRole(
      editingRole.value.roleId,
      selectedPermissionIds
    )
    ElMessage.success('权限配置保存成功')
    showPermissionDialog.value = false
  } catch (error) {
    console.error('保存权限失败:', error)
    ElMessage.error('保存失败')
  }
}

// 删除角色
const handleDelete = (row: RolePageVO) => {
  ElMessageBox.confirm(`确定要删除角色"${row.roleName}"吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await roleApi.deleteRole(row.roleId)
      ElMessage.success('删除成功')
      getRoleList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!roleFormRef.value) return
  
  try {
    await roleFormRef.value.validate()
    submitting.value = true
    
    if (editingRole.value) {
      await roleApi.updateRole(roleForm)
    } else {
      await roleApi.addRole(roleForm)
    }
    
    ElMessage.success(editingRole.value ? '更新成功' : '创建成功')
    showCreateDialog.value = false
    getRoleList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (roleFormRef.value) {
    roleFormRef.value.resetFields()
  }
  
  editingRole.value = null
  Object.assign(roleForm, {
    roleId: undefined,
    roleName: '',
    roleIntroduce: ''
  })
}

// 组件挂载
onMounted(() => {
  getRoleList()
  loadPermissions()
})
</script>

<style scoped lang="scss">
.role-management {
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

  .permission-setup {
    max-height: 400px;
    overflow-y: auto;
  }

  .permission-tree {
    padding: 16px;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
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
  .role-management {
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
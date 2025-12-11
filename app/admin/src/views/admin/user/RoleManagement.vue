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
        <el-table-column prop="roleIntroduce" label="角色描述" min-width="200" />
        <el-table-column label="权限" width="100" align="center">
          <template #default="{ row }">
            <el-button
              type="info"
              size="default"
              @click="handleViewRoleDetail(row)"
            >
              查看权限
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button
              type="info"
              size="default"
              @click="handleViewRoleDetail(row)"
            >
              查看
            </el-button>
            <el-button type="primary" size="default" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="warning" size="default" @click="handlePermission(row)">
              分配权限
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

    <!-- 权限分配对话框 -->
    <el-dialog
      v-model="showPermissionDialog"
      title="分配权限"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="permission-info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="角色ID">
            {{ editingRole?.roleId }}
          </el-descriptions-item>
          <el-descriptions-item label="角色名称">
            {{ editingRole?.roleName }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <div class="permission-selection">
        <h4>选择权限</h4>
        <el-transfer
          v-model="selectedPermissionIds"
          :data="allPermissions"
          :titles="['可选权限', '已选权限']"
          :button-texts="['移除', '添加']"
          :format="{ noChecked: '${total}', hasChecked: '${checked}/${total}' }"
        />
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPermissionDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSavePermissions" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 角色详情对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="角色详情"
      width="700px"
    >
      <el-descriptions v-if="currentRoleDetail" :column="2" border>
        <el-descriptions-item label="角色ID">
          {{ currentRoleDetail.roleId }}
        </el-descriptions-item>
        <el-descriptions-item label="角色名称">
          {{ currentRoleDetail.roleName }}
        </el-descriptions-item>
        <el-descriptions-item label="角色描述" :span="2">
          <div style="white-space: pre-wrap; word-break: break-all;">
            {{ currentRoleDetail.roleIntroduce || '暂无描述' }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="权限" :span="2">
          <el-card>
            <template #header>
              <div style="display: flex; align-items: center; gap: 8px;">
                <i class="el-icon-key" style="color: #3b82f6;"></i>
                <span>权限列表</span>
              </div>
            </template>
            
            <div v-if="currentRoleDetail?.permissionInfoVOS && currentRoleDetail.permissionInfoVOS.length > 0" 
                 style="display: flex; flex-wrap: wrap; gap: 8px;">
              <el-tag 
                v-for="permission in currentRoleDetail.permissionInfoVOS" 
                :key="permission.permissionId" 
                type="info"
                size="default"
                style="display: flex; align-items: center; gap: 4px;"
              >
                <i class="el-icon-folder-opened" style="font-size: 14px;"></i>
                {{ permission.permissionName }}
              </el-tag>
            </div>
            
            <div v-else class="no-roles-card">
              <i class="el-icon-user"></i>
              <span>该角色暂无分配权限</span>
            </div>
          </el-card>
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">关闭</el-button>
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
  RoleInfoVO,
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
const allPermissions = ref<Array<{ key: number; label: string }>>([])
const selectedPermissionIds = ref<number[]>([])  

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

// 查看角色详情
const permissionDialogVisible = ref(false)
const currentRoleDetail = ref<RoleInfoVO | null>(null)

const handleViewRoleDetail = async (row: RolePageVO) => {
  try {
    // 调用查看角色详情接口
    const response = await roleApi.getRoleInfo(row.roleId)
    currentRoleDetail.value = response.data
    console.log('角色详情数据:', response.data)
    console.log('权限数据:', response.data?.permissionInfoVOS)
    console.log('权限数量:', response.data?.permissionInfoVOS?.length)
    permissionDialogVisible.value = true
  } catch (error) {
    console.error('获取角色详情失败:', error)
    ElMessage.error('获取角色详情失败')
  }
}

// 配置权限
const handlePermission = async (row: RolePageVO) => {
  editingRole.value = row
  showPermissionDialog.value = true
  
  try {
    // 获取角色当前权限
    const rolesPermissionsResponse = await roleApi.getRolePermissions(row.roleId)
    selectedPermissionIds.value = rolesPermissionsResponse.data
    
    // 获取所有权限
    const allPermissionsResponse = await permissionApi.getAllPermissions()
    // 修复：正确映射权限数据格式为 el-transfer 需要的格式
    allPermissions.value = allPermissionsResponse.data.map((permission: any) => ({
      key: permission.permissionId,
      label: permission.permissionName
    }))
  } catch (error) {
    console.error('获取权限信息失败:', error)
  }
}

// 保存权限
const handleSavePermissions = async () => {
  if (!editingRole.value) return
  
  try {
    submitting.value = true
    await roleApi.assignPermissionToRole(
      editingRole.value.roleId,
      selectedPermissionIds.value
    )
    ElMessage.success('权限分配成功')
    showPermissionDialog.value = false
  } catch (error) {
    console.error('保存权限失败:', error)
    ElMessage.error('保存失败')
  } finally {
    submitting.value = false
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
  
  // 无权限卡片样式
  .no-permissions-card {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 20px;
    background: white;
    border: 1px dashed #d1d5db;
    border-radius: 6px;
    color: #6b7280;
    
    i {
      font-size: 16px;
    }
  }
  
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

  .permission-selection {
    h4 {
      margin-bottom: 16px;
      color: #1f2937;
    }
  }
  
  .permission-info {
    margin-bottom: 24px;
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
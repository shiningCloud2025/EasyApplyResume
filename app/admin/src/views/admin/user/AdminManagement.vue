<template>
  <div class="admin-management">
    <!-- 页头 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">管理员管理</h1>
        <p class="page-description">管理系统管理员账号、角色分配和权限控制</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData" :icon="Refresh" type="default">
          刷新
        </el-button>
        <el-button @click="showAddDialog" type="primary" :icon="Plus">
          新增管理员
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item label="账号名称">
          <el-input
            v-model="queryForm.adminUsername"
            placeholder="请输入账号名称"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="queryForm.adminEmail"
            placeholder="请输入邮箱"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input
            v-model="queryForm.adminPhone"
            placeholder="请输入手机号"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryForm.adminState"
            placeholder="请选择状态"
            clearable
            style="width: 180px"
          >
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">管理员列表</span>
        <div class="table-actions">
          <el-button
            type="danger"
            :disabled="selectedIds.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column prop="adminId" label="ID" width="70" />
        
        <el-table-column label="头像" width="70">
          <template #default="{ row }">
            <el-avatar
              :size="40"
              :src="row.adminImage"
              :alt="row.adminUsername"
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="adminAccount" label="账号" min-width="120" />
        
        <el-table-column prop="adminUsername" label="姓名" min-width="120" />
        
        <el-table-column prop="adminEmail" label="邮箱" min-width="180" />
        
        <el-table-column prop="adminPhone" label="手机号" min-width="130" />
        
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag
              :type="row.adminState === 1 ? 'success' : 'danger'"
              size="small"
            >
              {{ row.adminState === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="adminLoginTime" label="最后登录" min-width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.adminLoginTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="角色" width="100" align="center">
          <template #default="{ row }">
            <el-button
              type="success"
              size="default"
              @click="handleViewRoles(row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
        
        <el-table-column label="介绍" width="100" align="center">
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
        
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button
              type="info"
              size="default"
              @click="handleViewDetail(row)"
            >
              查看
            </el-button>
            <el-button
              type="primary"
              size="default"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              type="warning"
              size="default"
              @click="handleAssignRole(row)"
            >
              分配角色
            </el-button>
            <el-button
              type="danger"
              size="default"
              :disabled="row.adminId === 1"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        size="default"
      >
        <el-form-item label="管理员账号" prop="adminAccount">
          <el-input
            v-model="form.adminAccount"
            placeholder="请输入管理员账号（7-10位）"
            :disabled="dialogType === 'edit'"
          />
        </el-form-item>
        
        <el-form-item label="管理员姓名" prop="adminUsername">
          <el-input v-model="form.adminUsername" placeholder="请输入管理员姓名" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="adminEmail">
          <el-input v-model="form.adminEmail" placeholder="请输入邮箱" />
        </el-form-item>
        
        <el-form-item label="手机号" prop="adminPhone">
          <el-input v-model="form.adminPhone" placeholder="请输入手机号" />
        </el-form-item>
        
        <el-form-item
          label="密码"
          prop="adminPassword"
          v-if="dialogType === 'add'"
        >
          <el-input
            v-model="form.adminPassword"
            type="password"
            placeholder="请输入密码（6-20位）"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="头像" prop="adminImage">
          <el-input v-model="form.adminImage" placeholder="请输入头像URL" />
        </el-form-item>
        
        <el-form-item label="介绍" prop="adminIntroduce">
          <el-input
            v-model="form.adminIntroduce"
            type="textarea"
            :rows="3"
            placeholder="请输入介绍"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="adminState">
          <el-radio-group v-model="form.adminState">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 角色详情弹窗 -->
    <el-dialog
      v-model="rolesDialogVisible"
      title="管理员角色"
      width="600px"
    >
      <el-card v-if="currentRolesAdmin">
        <template #header>
          <div style="display: flex; align-items: center; gap: 12px;">
            <el-avatar
              :size="50"
              :src="currentRolesAdmin.adminImage"
              :alt="currentRolesAdmin.adminUsername"
            />
            <div>
              <div style="font-weight: 600; font-size: 16px;">{{ currentRolesAdmin.adminUsername }}</div>
              <div style="color: #909399; font-size: 14px;">{{ currentRolesAdmin.adminAccount }}</div>
            </div>
          </div>
        </template>
        <div style="padding: 16px; min-height: 80px;">
          <div v-if="currentRolesAdmin.roles && currentRolesAdmin.roles.length > 0" style="display: flex; flex-wrap: wrap; gap: 10px;">
            <el-tag
              v-for="role in currentRolesAdmin.roles"
              :key="role.roleId"
              size="large"
              type="success"
            >
              {{ role.roleName }}
            </el-tag>
          </div>
          <div v-else style="color: #909399; text-align: center; padding: 20px;">
            该管理员暂无角色
          </div>
        </div>
      </el-card>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="rolesDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 介绍详情弹窗 -->
    <el-dialog
      v-model="introduceDialogVisible"
      title="管理员介绍"
      width="600px"
    >
      <el-card v-if="currentIntroduceAdmin">
        <template #header>
          <div style="display: flex; align-items: center; gap: 12px;">
            <el-avatar
              :size="50"
              :src="currentIntroduceAdmin.adminImage"
              :alt="currentIntroduceAdmin.adminUsername"
            />
            <div>
              <div style="font-weight: 600; font-size: 16px;">{{ currentIntroduceAdmin.adminUsername }}</div>
              <div style="color: #909399; font-size: 14px;">{{ currentIntroduceAdmin.adminAccount }}</div>
            </div>
          </div>
        </template>
        <div style="padding: 16px; min-height: 100px; white-space: pre-wrap; word-break: break-all; line-height: 1.8;">
          {{ currentIntroduceAdmin.adminIntroduce || '该管理员暂无介绍' }}
        </div>
      </el-card>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="introduceDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="管理员完整信息"
      width="700px"
    >
      <el-descriptions v-if="currentDetailAdmin" :column="2" border>
        <el-descriptions-item label="管理员ID">
          {{ currentDetailAdmin.adminId }}
        </el-descriptions-item>
        <el-descriptions-item label="账号">
          {{ currentDetailAdmin.adminAccount }}
        </el-descriptions-item>
        <el-descriptions-item label="姓名">
          {{ currentDetailAdmin.adminUsername }}
        </el-descriptions-item>
        <el-descriptions-item label="邮箱">
          {{ currentDetailAdmin.adminEmail }}
        </el-descriptions-item>
        <el-descriptions-item label="手机号">
          {{ currentDetailAdmin.adminPhone }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentDetailAdmin.adminState === 1 ? 'success' : 'danger'">
            {{ currentDetailAdmin.adminState === 1 ? '正常' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="头像" :span="2">
          <el-avatar
            :size="80"
            :src="currentDetailAdmin.adminImage"
            :alt="currentDetailAdmin.adminUsername"
          />
        </el-descriptions-item>
        <el-descriptions-item label="最后登录时间" :span="2">
          {{ formatDateTime(currentDetailAdmin.adminLoginTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="介绍" :span="2">
          <div style="white-space: pre-wrap; word-break: break-all;">
            {{ currentDetailAdmin.adminIntroduce || '暂无介绍' }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="角色" :span="2">
          <el-tag
            v-for="role in currentDetailAdmin.roles"
            :key="role.roleId"
            size="default"
            style="margin-right: 8px; margin-bottom: 8px;"
          >
            {{ role.roleName }}
          </el-tag>
          <span v-if="!currentDetailAdmin.roles || currentDetailAdmin.roles.length === 0" style="color: #909399;">暂无角色</span>
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 分配角色弹窗 -->
    <el-dialog
      v-model="roleDialogVisible"
      title="分配角色"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="role-info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="账号">
            {{ currentAdmin?.adminAccount }}
          </el-descriptions-item>
          <el-descriptions-item label="姓名">
            {{ currentAdmin?.adminUsername }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <div class="role-selection">
        <h4>选择角色</h4>
        <el-transfer
          v-model="selectedRoles"
          :data="allRoles"
          :titles="['可选角色', '已选角色']"
          :button-texts="['移除', '添加']"
          :format="{ noChecked: '${total}', hasChecked: '${checked}/${total}' }"
        />
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAssignRoleSubmit" :loading="submitting">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '@/api/admin'
import type {
  AdminPageVO,
  AdminPageQuery,
  AdminForm,
  AdminInfoVO,
  RoleInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'
import { formatDateTime } from '@/utils'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const tableData = ref<AdminPageVO[]>([])
const selectedIds = ref<number[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 查询表单
const queryForm = reactive<AdminPageQuery>({
  adminUsername: '',
  adminEmail: '',
  adminPhone: '',
  adminState: undefined
})

// 弹窗相关
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const dialogTitle = computed(() => dialogType.value === 'add' ? '新增管理员' : '编辑管理员')

// 表单数据
const formRef = ref<FormInstance>()
const form = reactive<AdminForm>({
  adminAccount: '',
  adminUsername: '',
  adminEmail: '',
  adminPhone: '',
  adminPassword: '',
  adminImage: '',
  adminIntroduce: '',
  adminState: 1
})

// 表单验证规则
const rules = {
  adminAccount: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' },
    { min: 7, max: 10, message: '账号长度为7-10位', trigger: 'blur' }
  ],
  adminUsername: [
    { required: true, message: '请输入管理员姓名', trigger: 'blur' },
    { max: 15, message: '姓名长度不能超过15位', trigger: 'blur' }
  ],
  adminEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
    { max: 25, message: '邮箱长度不能超过25位', trigger: 'blur' }
  ],
  adminPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  adminPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20位', trigger: 'blur' }
  ],
  adminIntroduce: [
    { max: 200, message: '介绍长度不能超过200位', trigger: 'blur' }
  ]
}

// 角色详情弹窗相关
const rolesDialogVisible = ref(false)
const currentRolesAdmin = ref<AdminPageVO | null>(null)

// 介绍详情弹窗相关
const introduceDialogVisible = ref(false)
const currentIntroduceAdmin = ref<AdminPageVO | null>(null)

// 查看详情弹窗相关
const detailDialogVisible = ref(false)
const currentDetailAdmin = ref<AdminPageVO | null>(null)

// 角色分配相关
const roleDialogVisible = ref(false)
const currentAdmin = ref<AdminPageVO | null>(null)
const allRoles = ref<Array<{ key: number; label: string }>>([])
const selectedRoles = ref<number[]>([])

// 获取管理员列表
const getAdminList = async () => {
  try {
    loading.value = true
    const response = await adminApi.getAdminPage(
      pagination.current,
      pagination.size,
      queryForm
    )
    
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取管理员列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getAdminList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(queryForm, {
    adminUsername: '',
    adminEmail: '',
    adminPhone: '',
    adminState: undefined
  })
  pagination.current = 1
  getAdminList()
}

// 刷新数据
const refreshData = () => {
  getAdminList()
}

// 表格选择
const handleSelectionChange = (selection: AdminPageVO[]) => {
  selectedIds.value = selection.map(item => item.adminId)
}

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.size = size
  getAdminList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getAdminList()
}

// 新增管理员
const showAddDialog = async () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  
  try {
    const response = await adminApi.generateRandomAccount()
    form.adminAccount = response.data
  } catch (error) {
    console.error('生成随机账号失败:', error)
  }
}

// 查看角色
const handleViewRoles = (row: AdminPageVO) => {
  currentRolesAdmin.value = row
  rolesDialogVisible.value = true
}

// 查看介绍
const handleViewIntroduce = (row: AdminPageVO) => {
  currentIntroduceAdmin.value = row
  introduceDialogVisible.value = true
}

// 查看完整详情
const handleViewDetail = (row: AdminPageVO) => {
  currentDetailAdmin.value = row
  detailDialogVisible.value = true
}

// 编辑管理员
const handleEdit = (row: AdminPageVO) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  
  // 填充表单数据
  Object.assign(form, {
    adminId: row.adminId,
    adminAccount: row.adminAccount,
    adminUsername: row.adminUsername,
    adminEmail: row.adminEmail,
    adminPhone: row.adminPhone,
    adminPassword: '', // 编辑时不显示密码
    adminImage: row.adminImage,
    adminIntroduce: row.adminIntroduce,
    adminState: row.adminState
  })
}

// 删除管理员
const handleDelete = async (row: AdminPageVO) => {
  if (row.adminId === 1) {
    ElMessage.warning('超级管理员不能删除')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除管理员"${row.adminUsername}"吗？`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await adminApi.deleteAdmin(row.adminId)
    ElMessage.success('删除成功')
    getAdminList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个管理员吗？`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    for (const id of selectedIds.value) {
      if (id !== 1) {
        await adminApi.deleteAdmin(id)
      }
    }
    
    ElMessage.success('批量删除成功')
    getAdminList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
    }
  }
}

// 分配角色
const handleAssignRole = async (row: AdminPageVO) => {
  currentAdmin.value = row
  roleDialogVisible.value = true
  
  try {
    // 获取管理员当前角色
    const rolesResponse = await adminApi.getAdminRoles(row.adminId)
    selectedRoles.value = rolesResponse.data.map((role: RoleInfoVO) => role.roleId)
    
    // 获取所有角色（这里需要从角色管理API获取，暂用模拟数据）
    allRoles.value = [
      { key: 1, label: '超级管理员' },
      { key: 2, label: '内容管理员' },
      { key: 3, label: '数据审核员' }
    ]
  } catch (error) {
    console.error('获取角色信息失败:', error)
  }
}

// 提交分配角色
const handleAssignRoleSubmit = async () => {
  if (!currentAdmin.value) return

  try {
    submitting.value = true
    await adminApi.assignRoleToAdmin(currentAdmin.value.adminId, selectedRoles.value)
    ElMessage.success('角色分配成功')
    roleDialogVisible.value = false
    getAdminList()
  } catch (error) {
    console.error('角色分配失败:', error)
  } finally {
    submitting.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitting.value = true

    if (dialogType.value === 'add') {
      await adminApi.addAdmin(form)
      ElMessage.success('新增成功')
    } else {
      await adminApi.updateAdmin(form)
      ElMessage.success('更新成功')
    }

    dialogVisible.value = false
    getAdminList()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  
  Object.assign(form, {
    adminAccount: '',
    adminUsername: '',
    adminEmail: '',
    adminPhone: '',
    adminPassword: '',
    adminImage: '',
    adminIntroduce: '',
    adminState: 1
  })
}

// 组件挂载
onMounted(() => {
  getAdminList()
})
</script>

<style scoped lang="scss">
.admin-management {
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
    
    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
    }

    .table-title {
      font-size: 16px;
      font-weight: 600;
      color: #1f2937;
    }

    .table-actions {
      display: flex;
      gap: 8px;
    }
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 24px;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .role-info {
    margin-bottom: 24px;
  }

  .role-selection {
    h4 {
      margin-bottom: 16px;
      color: #1f2937;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .admin-management {
    .page-header {
      flex-direction: column;
      gap: 16px;
    }

    .header-actions {
      width: 100%;
      justify-content: flex-start;
    }

    .search-form {
      .el-form-item {
        width: 100%;
        margin-bottom: 16px;
      }
    }
  }
}
</style>
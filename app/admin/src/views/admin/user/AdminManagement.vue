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
        
        <el-table-column prop="adminUsername" label="姓名" min-width="120" />
        <el-table-column prop="adminAccount" label="账号" min-width="150" />
        <el-table-column prop="adminEmail" label="邮箱" min-width="180" />
        <el-table-column prop="adminPhone" label="手机号" min-width="130" />
        
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.adminState === 1 ? 'success' : 'danger'" size="small">
              {{ row.adminState === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="角色" width="120">
          <template #default="{ row }">
            <el-button
              type="info"
              size="small"
              @click="handleViewRoles(row)"
            >
              查看角色
            </el-button>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="360" fixed="right">
          <template #default="{ row }">
            <el-button
              type="info"
              size="default"
              @click="handleViewDetail(row)"
            >
              查看
            </el-button>
            <el-button type="primary" size="default" @click="handleEdit(row)">
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
              type="success"
              size="default"
              @click="handleSendEmail(row)"
            >
              <el-icon><Message /></el-icon>
              发邮件
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

    <!-- 创建/编辑管理员对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '新增管理员' : '编辑管理员'"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="账号" prop="adminAccount">
          <el-input 
            v-model="form.adminAccount" 
            placeholder="请输入账号"
            :disabled="dialogType === 'edit'"
          />
        </el-form-item>
        
        <el-form-item label="姓名" prop="adminUsername">
          <el-input v-model="form.adminUsername" placeholder="请输入姓名" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="adminEmail">
          <el-input v-model="form.adminEmail" placeholder="请输入邮箱" />
        </el-form-item>
        
        <el-form-item label="手机号" prop="adminPhone">
          <el-input v-model="form.adminPhone" placeholder="请输入手机号" />
        </el-form-item>
        
        <el-form-item label="密码" prop="adminPassword" v-if="dialogType === 'create'">
          <el-input 
            v-model="form.adminPassword" 
            type="password" 
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="头像" prop="adminImage">
          <div style="display: flex; align-items: center; gap: 12px;">
            <el-avatar :size="60" :src="form.adminImage || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" />
            <div style="flex: 1;">
              <el-upload
                ref="uploadRef"
                :auto-upload="false"
                :show-file-list="false"
                :on-change="handleFileChange"
                accept="image/*"
              >
                <el-button type="primary" :loading="uploading">
                  <el-icon><Upload /></el-icon>
                  选择图片
                </el-button>
              </el-upload>
              <div style="font-size: 12px; color: #909399; margin-top: 8px;">
                支持 JPG/PNG/GIF 格式，文件大小不超过 5MB
              </div>
            </div>
          </div>
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

    <!-- 查看详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="管理员完整信息"
      width="500px"
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
        <el-descriptions-item label="介绍" :span="2">
          {{ currentDetailAdmin.adminIntroduce || '暂无介绍' }}
        </el-descriptions-item>
        
        <el-descriptions-item label="最后登录" :span="2" v-if="currentDetailAdmin.adminLoginTime">
          {{ formatDate(currentDetailAdmin.adminLoginTime) }}
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 发送邮件对话框 -->
    <el-dialog
      v-model="emailDialogVisible"
      title="发送邮件"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="emailFormRef"
        :model="emailForm"
        :rules="emailRules"
        label-width="100px"
      >
        <el-form-item label="收件人" prop="toEmail">
          <el-input v-model="emailForm.toEmail" disabled>
            <template #prepend>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="邮件主题" prop="subject">
          <el-input
            v-model="emailForm.subject"
            placeholder="请输入邮件主题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="邮件内容" prop="htmlContent">
          <div style="border: 1px solid #dcdfe6; border-radius: 4px;">
            <Toolbar
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              mode="default"
              style="border-bottom: 1px solid #dcdfe6"
            />
            <Editor
              v-model="emailForm.htmlContent"
              :defaultConfig="editorConfig"
              mode="default"
              style="height: 300px; overflow-y: hidden;"
              @onCreated="handleEditorCreated"
            />
          </div>
          <div style="font-size: 12px; color: #909399; margin-top: 4px;">
            支持富文本格式，内容将以HTML格式发送
          </div>
        </el-form-item>
        
        <el-alert
          title="提示：内容支持 HTML 格式，使用工具栏快速插入格式标签去参考管理员管理的发邮件"
          type="info"
          :closable="false"
          show-icon
        />
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="emailDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSendEmailSubmit" :loading="sendingEmail">
            <el-icon><Promotion /></el-icon>
            发送邮件
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 角色权限弹窗 -->
    <el-dialog
      v-model="rolesDialogVisible"
      title="管理员角色权限"
      width="700px"
    >
      <el-card>
        <template #header>
          <div style="font-weight: 600; font-size: 16px;">
            {{ currentRolesAdmin?.adminUsername }} - 角色权限详情
          </div>
        </template>
        
        <div v-if="currentRoles && currentRoles.length > 0">
          <div v-for="role in currentRoles" :key="role.roleId" class="role-card">
            <div class="role-header">
              <el-tag type="primary" size="large" class="role-tag">
                {{ role.roleName }}
              </el-tag>
              <el-tag type="info" size="small" class="role-desc-tag">
                {{ role.roleIntroduce }}
              </el-tag>
            </div>
            
            <!-- 权限显示优化 -->
            <div v-if="role.permissionInfoVOS && role.permissionInfoVOS.length > 0" class="permissions-section">
              <div class="permissions-header">
                <i class="el-icon-key"></i>
                <span>权限详情</span>
                <el-badge :value="role.permissionInfoVOS.length" class="permission-count" />
              </div>
              <div class="permissions-grid">
                <div v-for="permission in role.permissionInfoVOS" :key="permission.permissionId" class="permission-card">
                  <i class="el-icon-folder-opened"></i>
                  <span class="permission-name">{{ permission.permissionName }}</span>
                </div>
              </div>
            </div>
            
            <div v-else class="no-permissions-card">
              <i class="el-icon-warning-outline"></i>
              <span>暂无具体权限</span>
            </div>
          </div>
        </div>
        
        <div v-else class="no-roles-card">
          <i class="el-icon-user"></i>
          <span>该管理员暂无分配角色</span>
        </div>
      </el-card>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="rolesDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onBeforeUnmount, shallowRef, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Plus, Search, RefreshRight, Message, User, Promotion, Upload } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { adminApi, roleApi, emailApi } from '@/api/admin'
import { fileApi } from '@/api/file'
import { useAuthStore } from '@/store/auth'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import '@wangeditor/editor/dist/css/style.css'
import type {
  AdminPageVO,
  AdminPageQuery,
  AdminForm,
  AdminInfoVO,
  RoleInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

const authStore = useAuthStore()

// 日期格式化函数（只显示日期）
const formatDate = (dateStr: string) => {
  if (!dateStr) return '-'
  return dateStr.substring(0, 10)
}

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const uploading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref<'create' | 'edit'>('create')
const formRef = ref<FormInstance>()

// 邮件相关
const emailDialogVisible = ref(false)
const emailFormRef = ref<FormInstance>()
const sendingEmail = ref(false)

// 富文本编辑器相关
const editorRef = shallowRef()
const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入邮件内容，支持富文本格式...',
  MENU_CONF: {}
}
const toolbarConfig: Partial<IToolbarConfig> = {
  toolbarKeys: [
    'headerSelect',
    'bold',
    'italic',
    'underline',
    'color',
    'bgColor',
    '|',
    'fontSize',
    'fontFamily',
    '|',
    'bulletedList',
    'numberedList',
    '|',
    'justifyLeft',
    'justifyCenter',
    'justifyRight',
    '|',
    'emotion',
    'insertLink',
    '|',
    'undo',
    'redo'
  ]
}

const emailForm = reactive({
  fromEmail: '',
  toEmail: '',
  subject: '',
  htmlContent: ''
})

const emailRules = {
  toEmail: [
    { required: true, message: '请输入收件人邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  subject: [
    { required: true, message: '请输入邮件主题', trigger: 'blur' },
    { min: 1, max: 35, message: '主题长度在 1 到 35 个字符', trigger: 'blur' }
  ],
  htmlContent: [
    { required: true, message: '请输入邮件内容', trigger: 'blur' }
  ]
}

// 分页
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 表格数据
const tableData = ref<AdminPageVO[]>([])
const selectedIds = ref<number[]>([])

// 搜索表单
const queryForm = reactive<AdminPageQuery>({
  adminUsername: '',
  adminEmail: '',
  adminPhone: '',
  adminState: undefined
})

// 表单数据
const form = reactive<AdminForm>({
  adminId: undefined,
  adminAccount: '',
  adminUsername: '',
  adminEmail: '',
  adminPhone: '',
  adminPassword: '',
  adminImage: '',
  adminIntroduce: '',
  adminState: 1
})

// 表单校验规则
const rules = {
  adminAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  adminUsername: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  adminEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  adminPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  adminPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 介绍详情弹窗相关
const introduceDialogVisible = ref(false)
const currentIntroduceAdmin = ref<AdminPageVO | null>(null)

// 查看详情弹窗相关
const detailDialogVisible = ref(false)
const currentDetailAdmin = ref<AdminInfoVO | null>(null)

// 角色权限弹窗相关
const rolesDialogVisible = ref(false)
const currentRolesAdmin = ref<AdminPageVO | null>(null)
const currentRoles = ref<any[]>([])

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

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getAdminList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getAdminList()
}

// 多选
const handleSelectionChange = (selection: AdminPageVO[]) => {
  selectedIds.value = selection.map(item => item.adminId)
}

// 显示新增对话框
const showAddDialog = () => {
  dialogType.value = 'create'
  resetForm()
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  
  Object.assign(form, {
    adminId: undefined,
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

const handleFileChange = async (uploadFile: any) => {
  const file = uploadFile.raw
  if (!file) return

  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件')
    return
  }

  if (file.size / 1024 / 1024 > 5) {
    ElMessage.error('图片大小不能超过 5MB')
    return
  }

  uploading.value = true
  try {
    const user = authStore.user
    if (!user?.userId) {
      ElMessage.error('未获取到用户信息')
      return
    }

    console.log('📤 开始上传头像，文件:', file.name, '大小:', (file.size / 1024).toFixed(2) + 'KB')
    
    const adminId = dialogType.value === 'edit' ? form.adminId : user.userId
    const response = await fileApi.uploadAdminAvatar(file, adminId!)
    console.log('✅ 上传成功，URL:', response.data)
    
    form.adminImage = response.data
    ElMessage.success('上传成功')
  } catch (error: any) {
    console.error('❌ 上传失败:', error)
    ElMessage.error(error.message || '上传失败')
  } finally {
    uploading.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    if (dialogType.value === 'create') {
      await adminApi.addAdmin(form)
      ElMessage.success('新增成功')
    } else {
      await adminApi.updateAdmin(form)
      ElMessage.success('更新成功')
    }
    
    dialogVisible.value = false
    getAdminList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 编辑
const handleEdit = (row: AdminPageVO) => {
  dialogType.value = 'edit'
  Object.assign(form, {
    adminId: row.adminId,
    adminAccount: row.adminAccount,
    adminUsername: row.adminUsername,
    adminEmail: row.adminEmail,
    adminPhone: row.adminPhone,
    adminPassword: '', // 编辑时密码置空
    adminImage: row.adminImage,
    adminIntroduce: row.adminIntroduce,
    adminState: row.adminState
  })
  dialogVisible.value = true
}

// 删除
const handleDelete = (row: AdminPageVO) => {
  ElMessageBox.confirm(`确定要删除管理员"${row.adminUsername}"吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await adminApi.deleteAdmin(row.adminId)
      ElMessage.success('删除成功')
      getAdminList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个管理员吗？`, '确认批量删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await Promise.all(selectedIds.value.map(id => adminApi.deleteAdmin(id)))
      ElMessage.success('批量删除成功')
      getAdminList()
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

// 查看角色
const handleViewRoles = async (row: AdminPageVO) => {
  currentRolesAdmin.value = row
  rolesDialogVisible.value = true
  
  try {
    // 调用查看管理员拥有角色的接口
    const response = await adminApi.getAdminRoles(row.adminId)
    currentRoles.value = response.data
  } catch (error) {
    console.error('获取角色信息失败:', error)
    ElMessage.error('获取角色信息失败')
    currentRoles.value = []
  }
}

// 查看完整详情
const handleViewDetail = async (row: AdminPageVO) => {
  try {
    // 调用查看管理员详情接口
    const response = await adminApi.getAdminInfo(row.adminId)
    currentDetailAdmin.value = response.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取管理员详情失败:', error)
    ElMessage.error('获取管理员详情失败')
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
    
    // 获取所有角色
    const allRolesResponse = await roleApi.getAllRoles()
    allRoles.value = allRolesResponse.data.map((role: RoleInfoVO) => ({
      key: role.roleId,
      label: role.roleName
    }))
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

// 生成随机账号
const generateRandomAccount = async () => {
  try {
    const response = await adminApi.generateRandomAccount()
    form.adminAccount = response.data
    ElMessage.success('账号生成成功')
  } catch (error) {
    console.error('生成随机账号失败:', error)
  }
}

// 发送邮件
const handleSendEmail = async (row: AdminPageVO) => {
  console.log('📧 [发邮件] 目标管理员:', row)
  
  try {
    // 调用后端接口获取当前登录用户信息
    console.log('📧 [发邮件] 正在获取当前用户信息...')
    const response = await adminApi.getCurrentAdminInfo()
    console.log('📧 [发邮件] 当前用户信息:', response.data)
    
    const currentUserEmail = response.data.userEmail
    
    if (!currentUserEmail) {
    ElMessage.error('无法获取当前用户邮箱，请重新登录')
    return
  }
  
    console.log('📧 [发邮件] 发件人邮箱:', currentUserEmail)
    console.log('📧 [发邮件] 收件人邮箱:', row.adminEmail)
    
    // 设置邮件表单数据（使用默认发送者，不需要设置fromEmail）
    emailForm.toEmail = row.adminEmail
    emailForm.subject = ''
    emailForm.htmlContent = ''
  
    emailDialogVisible.value = true
  } catch (error: any) {
    console.error('❌ [发邮件] 获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败：' + (error.message || '请重新登录'))
  }
}

// 提交发送邮件
const handleSendEmailSubmit = async () => {
  if (!emailFormRef.value) return
  
  try {
    await emailFormRef.value.validate()
    
    ElMessageBox.confirm(
      `确定要向 ${emailForm.toEmail} 发送邮件吗？`,
      '确认发送',
      {
        type: 'warning',
        confirmButtonText: '确定发送',
        cancelButtonText: '取消'
      }
    ).then(async () => {
      try {
        sendingEmail.value = true
        console.log('📧 [发邮件] 发送参数:', emailForm)
        
        // 使用第四个接口：发送HTML邮件（使用默认发送者）
        await emailApi.sendHtmlEmailUsuallyDef(
          emailForm.toEmail,
          emailForm.subject,
          emailForm.htmlContent
        )
        
        ElMessage.success('邮件发送成功')
        emailDialogVisible.value = false
      } catch (error: any) {
        console.error('❌ [发邮件] 发送失败:', error)
        ElMessage.error('发送失败：' + (error.message || '请稍后重试'))
      } finally {
        sendingEmail.value = false
      }
    }).catch(() => {
      // 用户取消
    })
  } catch (error) {
    console.log('表单验证失败')
  }
}



// 富文本编辑器创建回调
const handleEditorCreated = (editor: any) => {
  editorRef.value = editor
  console.log('📝 富文本编辑器创建成功')
}

// 组件挂载
onMounted(() => {
  getAdminList()
})

// 组件卸载时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})
</script>

<style scoped lang="scss">
.admin-management {
  font-size: 16px;
  
  // 角色卡片样式
  .role-card {
    margin-bottom: 20px;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    overflow: hidden;
    background: white;
  }

  .role-header {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 16px 20px;
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    border-bottom: 1px solid #e4e7ed;
  }

  .role-tag {
    font-weight: 600;
  }

  .role-desc-tag {
    flex: 1;
    opacity: 0.9;
  }

  // 权限区域
  .permissions-section {
    padding: 20px;
    background: #fafbfc;
  }

  .permissions-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 16px;
    font-weight: 600;
    color: #1f2937;
    
    i {
      color: #3b82f6;
      font-size: 16px;
    }
  }

  .permission-count {
    margin-left: auto;
  }

  .permissions-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 12px;
  }

  .permission-card {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 16px;
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    transition: all 0.2s ease;
    
    &:hover {
      border-color: #3b82f6;
      box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
      transform: translateY(-1px);
    }
    
    i {
      color: #10b981;
      font-size: 14px;
    }
    
    .permission-name {
      font-size: 14px;
      color: #374151;
    }
  }

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

  .no-roles-card {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 24px;
    text-align: center;
    background: linear-gradient(135deg, #fef2f2 0%, #fee2e2 100%);
    border: 1px solid #fecaca;
    border-radius: 8px;
    color: #dc2626;
    font-weight: 500;
    
    i {
      font-size: 24px;
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
    gap: 12px;
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

  .role-selection {
    margin-top: 20px;
    
    h4 {
      margin-bottom: 16px;
      color: #1f2937;
      font-weight: 600;
    }
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
  .admin-management {
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
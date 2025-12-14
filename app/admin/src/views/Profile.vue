<template>
  <div class="profile-page" v-loading="loading">
    <!-- 个人中心头部 - 紧凑版 -->
    <el-card class="profile-card" shadow="hover">
    <div class="profile-header">
      <div class="avatar-section">
          <el-avatar :size="80" :src="adminInfo?.adminImage || defaultAvatar" />
          <div class="user-info">
            <h2>{{ adminInfo?.adminUsername || '加载中...' }}</h2>
            <p class="user-meta">@{{ adminInfo?.adminAccount }} | {{ adminInfo?.adminEmail }}</p>
            <div class="user-tags">
              <el-tag 
                v-for="role in adminInfo?.roleInfoVOS" 
                :key="role.roleId"
                type="primary"
                size="small"
              >
                {{ role.roleName }}
              </el-tag>
              <el-tag 
                :type="adminInfo?.adminState === 1 ? 'success' : 'danger'" 
                size="small"
              >
                {{ adminInfo?.adminState === 1 ? '正常' : '禁用' }}
              </el-tag>
        </div>
        </div>
      </div>
      <div class="header-actions">
          <el-button @click="getAdminInfo" :loading="loading">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
          <el-button type="primary" @click="openEditDialog">
            <el-icon><Edit /></el-icon>
          编辑资料
        </el-button>
      </div>
    </div>
    </el-card>

    <!-- 信息展示 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <span style="font-weight: 600;">基本信息</span>
          </template>
          <el-descriptions :column="2" border size="default">
            <el-descriptions-item label="管理员ID">{{ adminInfo?.adminId }}</el-descriptions-item>
            <el-descriptions-item label="账号">{{ adminInfo?.adminAccount }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ adminInfo?.adminUsername }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ adminInfo?.adminPhone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱" :span="2">{{ adminInfo?.adminEmail }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDateTime(adminInfo?.adminCreatedTime) }}</el-descriptions-item>
            <el-descriptions-item label="最后登录">{{ formatDateTime(adminInfo?.adminLoginTime) }}</el-descriptions-item>
            <el-descriptions-item label="个人简介" :span="2">
              {{ adminInfo?.adminIntroduce || '暂无简介' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span style="font-weight: 600;">我的角色</span>
          </template>
          <div class="roles-list">
            <div 
              v-for="role in adminInfo?.roleInfoVOS" 
              :key="role.roleId"
              class="role-item"
            >
              <el-icon color="#409eff"><Star /></el-icon>
              <div class="role-info">
                <div class="role-name">{{ role.roleName }}</div>
                <div class="role-desc">{{ role.roleIntroduce }}</div>
              </div>
            </div>
            <el-empty 
              v-if="!adminInfo?.roleInfoVOS || adminInfo.roleInfoVOS.length === 0" 
              description="暂无角色"
              :image-size="60"
            />
            </div>
          </el-card>
      </el-col>
    </el-row>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="showEditDialog"
      title="编辑个人资料"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
      >
        <el-form-item label="姓名" prop="adminUsername">
          <el-input v-model="editForm.adminUsername" maxlength="15" show-word-limit />
            </el-form-item>
        <el-form-item label="邮箱" prop="adminEmail">
          <el-input v-model="editForm.adminEmail" maxlength="25" show-word-limit />
            </el-form-item>
        <el-form-item label="手机号" prop="adminPhone">
          <el-input v-model="editForm.adminPhone" maxlength="11" />
            </el-form-item>
        <el-form-item label="头像URL" prop="adminImage">
          <el-input v-model="editForm.adminImage" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="个人简介" prop="adminIntroduce">
          <el-input v-model="editForm.adminIntroduce" type="textarea" :rows="3" maxlength="200" show-word-limit />
        </el-form-item>
        <el-form-item label="修改密码">
          <el-input v-model="editForm.adminPassword" type="password" placeholder="不修改请留空" show-password clearable />
          <div style="font-size: 12px; color: #909399; margin-top: 4px;">如不需要修改密码，请留空</div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { Edit, Refresh, Star } from '@element-plus/icons-vue'
import { adminApi } from '@/api/admin'
import type { AdminInfoVO, AdminForm } from '@/types/admin'
import { useAuthStore } from '@/store/auth'

const authStore = useAuthStore()
const loading = ref(false)
const showEditDialog = ref(false)
const submitting = ref(false)
const editFormRef = ref<FormInstance>()

const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
const adminInfo = ref<AdminInfoVO | null>(null)

const editForm = reactive<AdminForm>({
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

const editRules = {
  adminUsername: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
  ],
  adminEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  adminPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const formatDateTime = (dateStr: string | undefined) => {
  if (!dateStr) return '-'
  return dateStr.replace('T', ' ').substring(0, 16)
}

const getAdminInfo = async () => {
  loading.value = true
  try {
    // 先尝试刷新用户信息
    if (!authStore.user) {
      console.log('📥 [个人中心] authStore.user为空，尝试获取用户信息...')
      await authStore.getUserInfo()
    }
    
    const user = authStore.user
    if (!user?.userId) {
      ElMessage.error('未获取到用户信息，请重新登录')
      console.error('❌ [个人中心] authStore.user:', user)
      console.error('❌ [个人中心] 请检查登录状态或重新登录')
      return
    }
    
    console.log('📥 [个人中心] 当前用户:', user)
    console.log('📥 [个人中心] 调用 /admin/admin/findById，adminId:', user.userId)
    const response = await adminApi.getAdminInfo(user.userId)
    console.log('📥 [个人中心] API响应:', response)
    adminInfo.value = response.data
    console.log('✅ [个人中心] 管理员信息加载成功:', adminInfo.value)
  } catch (error) {
    console.error('❌ [个人中心] 获取失败:', error)
    ElMessage.error('获取个人信息失败，请重新登录')
  } finally {
    loading.value = false
  }
}

const openEditDialog = () => {
  if (!adminInfo.value) return
  
  Object.assign(editForm, {
    adminId: adminInfo.value.adminId,
    adminAccount: adminInfo.value.adminAccount,
    adminUsername: adminInfo.value.adminUsername,
    adminEmail: adminInfo.value.adminEmail,
    adminPhone: adminInfo.value.adminPhone,
    adminPassword: '',
    adminImage: adminInfo.value.adminImage,
    adminIntroduce: adminInfo.value.adminIntroduce,
    adminState: adminInfo.value.adminState
  })
  
  showEditDialog.value = true
}

const handleSubmit = async () => {
  if (!editFormRef.value) return
  
  try {
    await editFormRef.value.validate()
    
    submitting.value = true
    
    const submitData = { ...editForm }
    if (!submitData.adminPassword) {
      delete submitData.adminPassword
    }
    
    await adminApi.updateAdmin(submitData)
    ElMessage.success('修改成功')
    showEditDialog.value = false
    await getAdminInfo()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('❌ 修改失败:', error)
      ElMessage.error('修改失败')
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  getAdminInfo()
})
</script>

<style scoped lang="scss">
.profile-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.profile-card {
  margin-bottom: 20px;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

.avatar-section {
  display: flex;
  align-items: center;
    gap: 16px;
  }
  
  .user-info {
    h2 {
  margin: 0 0 8px 0;
      font-size: 20px;
  font-weight: 600;
  color: #1f2937;
}

    .user-meta {
      margin: 0 0 8px 0;
      font-size: 13px;
  color: #6b7280;
}

    .user-tags {
  display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }
  }
  
  .header-actions {
    display: flex;
    gap: 12px;
  }
}

.roles-list {
  min-height: 100px;
}

.role-item {
    display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  margin-bottom: 8px;
  background: #f5f7fa;
  border-radius: 8px;
  
  .role-info {
    flex: 1;
    
    .role-name {
      font-size: 14px;
      font-weight: 600;
      color: #1f2937;
      margin-bottom: 4px;
    }
    
    .role-desc {
      font-size: 12px;
      color: #6b7280;
    }
  }
}

@media (max-width: 768px) {
  .profile-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }
}
</style>

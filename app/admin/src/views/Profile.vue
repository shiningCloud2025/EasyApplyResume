<template>
  <div class="profile">
    <div class="profile-header">
      <div class="avatar-section">
        <div class="avatar-container">
          <el-avatar :size="80" :src="profile.avatar" />
          <el-button type="text" class="change-avatar" @click="changeAvatar">
            <i class="el-icon-camera"></i>
          </el-button>
        </div>
        <div class="avatar-info">
          <h2>{{ profile.username }}</h2>
          <p>{{ profile.role }}</p>
        </div>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="editProfile">
          <i class="el-icon-edit"></i>
          编辑资料
        </el-button>
      </div>
    </div>

    <div class="profile-content">
      <div class="profile-sidebar">
        <el-menu :default-active="activeTab" @select="handleTabSelect">
          <el-menu-item index="basic">
            <i class="el-icon-user"></i>
            <span>基本信息</span>
          </el-menu-item>
          <el-menu-item index="security">
            <i class="el-icon-lock"></i>
            <span>安全设置</span>
          </el-menu-item>
          <el-menu-item index="notifications">
            <i class="el-icon-bell"></i>
            <span>通知设置</span>
          </el-menu-item>
          <el-menu-item index="activity">
            <i class="el-icon-time"></i>
            <span>活动记录</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div class="profile-main">
        <!-- 基本信息 -->
        <div v-if="activeTab === 'basic'" class="tab-content">
          <h3>基本信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="用户名">{{ profile.username }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ profile.email }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ profile.phone }}</el-descriptions-item>
            <el-descriptions-item label="部门">{{ profile.department }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ profile.role }}</el-descriptions-item>
            <el-descriptions-item label="加入时间">{{ profile.joinTime }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 安全设置 -->
        <div v-if="activeTab === 'security'" class="tab-content">
          <h3>安全设置</h3>
          <el-card class="security-card">
            <div class="security-item">
              <div class="security-info">
                <h4>修改密码</h4>
                <p>定期更改密码可以保护账户安全</p>
              </div>
              <el-button @click="showPasswordDialog = true">修改密码</el-button>
            </div>
            <div class="security-item">
              <div class="security-info">
                <h4>绑定手机</h4>
                <p>已绑定：{{ profile.phone }}</p>
              </div>
              <el-button>更换手机</el-button>
            </div>
            <div class="security-item">
              <div class="security-info">
                <h4>登录验证</h4>
                <p>启用了短信验证码登录</p>
              </div>
              <el-switch v-model="securitySettings.smsLogin" />
            </div>
          </el-card>
        </div>

        <!-- 通知设置 -->
        <div v-if="activeTab === 'notifications'" class="tab-content">
          <h3>通知设置</h3>
          <el-form label-width="120px">
            <el-form-item label="邮件通知">
              <el-switch v-model="notificationSettings.email" />
            </el-form-item>
            <el-form-item label="系统通知">
              <el-switch v-model="notificationSettings.system" />
            </el-form-item>
            <el-form-item label="业务通知">
              <el-switch v-model="notificationSettings.business" />
            </el-form-item>
          </el-form>
        </div>

        <!-- 活动记录 -->
        <div v-if="activeTab === 'activity'" class="tab-content">
          <h3>活动记录</h3>
          <el-timeline>
            <el-timeline-item 
              v-for="activity in activities" 
              :key="activity.id"
              :timestamp="activity.time"
              :color="activity.color"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>

    <!-- 编辑资料对话框 -->
    <el-dialog v-model="showProfileDialog" title="编辑资料" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="editForm.department" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showProfileDialog = false">取消</el-button>
        <el-button type="primary" @click="saveProfile">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px">
      <el-form :model="passwordForm" label-width="80px">
        <el-form-item label="当前密码">
          <el-input type="password" v-model="passwordForm.currentPassword" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input type="password" v-model="passwordForm.newPassword" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input type="password" v-model="passwordForm.confirmPassword" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @clickchangePassword>确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('basic')
const showProfileDialog = ref(false)
const showPasswordDialog = ref(false)

const profile = reactive({
  username: 'admin',
  email: 'admin@example.com',
  phone: '138****8888',
  department: '技术部',
  role: '超级管理员',
  joinTime: '2024-01-15',
  avatar: ''
})

const editForm = reactive({ ...profile })
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const securitySettings = reactive({
  smsLogin: true
})

const notificationSettings = reactive({
  email: true,
  system: true,
  business: false
})

const activities = ref([
  {
    id: 1,
    content: '登录系统',
    time: '2024-03-15 10:30',
    color: '#67c23a'
  },
  {
    id: 2,
    content: '修改了简历模板',
    time: '2024-03-15 09:45',
    color: '#409eff'
  },
  {
    id: 3,
    content: '新增了招聘信息',
    time: '2024-03-14 16:20',
    color: '#409eff'
  }
])

const handleTabSelect = (index: string) => {
  activeTab.value = index
}

const editProfile = () => {
  Object.assign(editForm, profile)
  showProfileDialog.value = true
}

const saveProfile = () => {
  Object.assign(profile, editForm)
  showProfileDialog.value = false
  ElMessage.success('资料保存成功')
}

const changePassword = () => {
  // 实现修改密码逻辑
  showPasswordDialog.value = false
  ElMessage.success('密码修改成功')
}

const changeAvatar = () => {
  ElMessage.info('头像上传功能开发中')
}
</script>

<style scoped lang="scss">
.profile {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  padding: 24px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar-container {
  position: relative;
  
  .change-avatar {
    position: absolute;
    bottom: 0;
    right: 0;
    background: rgba(0, 0, 0, 0.5);
    color: white;
    border-radius: 50%;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.avatar-info h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
}

.avatar-info p {
  margin: 0;
  color: #6b7280;
}

.profile-content {
  display: flex;
  gap: 24px;
}

.profile-sidebar {
  width: 250px;
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.profile-main {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.tab-content h3 {
  margin: 0 0 24px 0;
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
}

.security-card {
  .security-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
    border-bottom: 1px solid #f3f4f6;
    
    &:last-child {
      border-bottom: none;
    }
    
    .security-info h4 {
      margin: 0 0 4px 0;
      font-size: 16px;
      font-weight: 500;
      color: #1f2937;
    }
    
    .security-info p {
      margin: 0;
      font-size: 14px;
      color: #6b7280;
    }
  }
}

@media (max-width: 768px) {
  .profile-content {
    flex-direction: column;
  }
  
  .profile-sidebar {
    width: 100%;
  }
  
  .profile-header {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
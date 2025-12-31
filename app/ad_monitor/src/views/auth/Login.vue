<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="decoration-circle decoration-1"></div>
      <div class="decoration-circle decoration-2"></div>
      <div class="decoration-circle decoration-3"></div>
    </div>

    <div class="login-card">
      <!-- Logo区域 -->
      <div class="logo-section">
        <div class="logo">
          <div class="logo-icon">
            <el-icon :size="24"><DataAnalysis /></el-icon>
          </div>
          <h1>易投简历 · 监测与广告端</h1>
        </div>
        <p>系统登录</p>
      </div>

      <!-- 登录表单 -->
      <el-form 
        ref="formRef" 
        :model="currentForm" 
        :rules="currentRules" 
        size="large"
        class="login-form"
      >
        <!-- 登录方式切换 -->
        <div class="login-tabs">
          <div 
            class="tab-item"
            :class="{ active: activeTab === 'account' }"
            @click="switchTab('account')"
          >
            账号密码
          </div>
          <div 
            class="tab-item"
            :class="{ active: activeTab === 'phone' }"
            @click="switchTab('phone')"
          >
            手机验证码
          </div>
          <div 
            class="tab-item"
            :class="{ active: activeTab === 'email' }"
            @click="switchTab('email')"
          >
            邮箱验证码
          </div>
        </div>

        <!-- 账号密码登录 -->
        <div v-show="activeTab === 'account'" class="form-section">
          <el-form-item prop="accountOrPhoneOrEmail">
            <el-input
              v-model="loginForm.accountOrPhoneOrEmail"
              placeholder="请输入账号/手机号/邮箱"
              :prefix-icon="User"
              clearable
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
              clearable
            />
          </el-form-item>
        </div>

        <!-- 手机验证码登录 -->
        <div v-show="activeTab === 'phone'" class="form-section">
          <el-form-item prop="phone">
            <el-input
              v-model="phoneLoginForm.phone"
              placeholder="请输入手机号"
              :prefix-icon="Iphone"
              clearable
            />
          </el-form-item>
          <el-form-item prop="messageCode">
            <div class="code-input-group">
              <el-input
                v-model="phoneLoginForm.messageCode"
                placeholder="请输入验证码"
                :prefix-icon="Message"
                clearable
              />
              <el-button
                @click="sendPhoneCode"
                :disabled="phoneCodeCountdown > 0"
                :loading="sendingPhoneCode"
                type="text"
                class="code-button"
              >
                {{ phoneCodeButtonText }}
              </el-button>
            </div>
          </el-form-item>
        </div>

        <!-- 邮箱验证码登录 -->
        <div v-show="activeTab === 'email'" class="form-section">
          <el-form-item prop="email">
            <el-input
              v-model="emailLoginForm.email"
              placeholder="请输入邮箱"
              :prefix-icon="Message"
              clearable
            />
          </el-form-item>
          <el-form-item prop="messageCode">
            <div class="code-input-group">
              <el-input
                v-model="emailLoginForm.messageCode"
                placeholder="请输入验证码"
                :prefix-icon="Key"
                clearable
              />
              <el-button
                @click="sendEmailCode"
                :disabled="emailCodeCountdown > 0"
                :loading="sendingEmailCode"
                type="text"
                class="code-button"
              >
                {{ emailCodeButtonText }}
              </el-button>
            </div>
          </el-form-item>
        </div>

        <!-- 登录按钮 -->
        <el-form-item class="submit-section">
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleLogin"
            class="login-button"
          >
            立即登录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 底部链接 -->
      <div class="footer-section">
        <span @click="showApplyTip" class="link-button apply-link">
          申请获取账号
        </span>
        <span class="divider">|</span>
        <span @click="goBack" class="link-button back-link">
          返回首页
        </span>
      </div>
    </div>

    <!-- 装饰性元素 -->
    <div class="floating-elements">
      <div class="element element-1"></div>
      <div class="element element-2"></div>
      <div class="element element-3"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import { monitorAnnouncementApi } from '@/api'
import { User, Lock, Iphone, Message, Key, DataAnalysis } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref<FormInstance>()
const loading = ref(false)
const activeTab = ref<'account' | 'phone' | 'email'>('account')

// 手机验证码倒计时
const phoneCodeCountdown = ref(0)
const sendingPhoneCode = ref(false)
const phoneCodeTimer = ref<ReturnType<typeof setInterval>>()

// 邮箱验证码倒计时
const emailCodeCountdown = ref(0)
const sendingEmailCode = ref(false)
const emailCodeTimer = ref<ReturnType<typeof setInterval>>()

// 表单数据
const loginForm = reactive({
  accountOrPhoneOrEmail: '',
  password: ''
})

const phoneLoginForm = reactive({
  phone: '',
  messageCode: ''
})

const emailLoginForm = reactive({
  email: '',
  messageCode: ''
})

// 动态表单
const currentForm = computed(() => {
  switch (activeTab.value) {
    case 'account': return loginForm
    case 'phone': return phoneLoginForm
    case 'email': return emailLoginForm
  }
})

// 校验规则
const isValidPhone = (phone: string) => /^1[3-9]\d{9}$/.test(phone)
const isValidEmail = (email: string) => /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(email)

const rules: FormRules = {
  accountOrPhoneOrEmail: [
    { required: true, message: '请输入账号/手机号/邮箱', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value && !isValidPhone(value)) {
        callback(new Error('请输入正确的手机号'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ],
  messageCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度为6位', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value && !isValidEmail(value)) {
        callback(new Error('请输入正确的邮箱格式'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ]
}

const currentRules = computed(() => {
  switch (activeTab.value) {
    case 'account': return { accountOrPhoneOrEmail: rules.accountOrPhoneOrEmail, password: rules.password }
    case 'phone': return { phone: rules.phone, messageCode: rules.messageCode }
    case 'email': return { email: rules.email, messageCode: rules.messageCode }
  }
})

// 验证码按钮文字
const phoneCodeButtonText = computed(() => phoneCodeCountdown.value > 0 ? `${phoneCodeCountdown.value}s` : '获取验证码')
const emailCodeButtonText = computed(() => emailCodeCountdown.value > 0 ? `${emailCodeCountdown.value}s` : '获取验证码')

// 切换Tab
const switchTab = (tab: 'account' | 'phone' | 'email') => {
  activeTab.value = tab
  formRef.value?.clearValidate()
}

// 发送手机验证码
const sendPhoneCode = async () => {
  if (!isValidPhone(phoneLoginForm.phone)) {
    ElMessage.error('请输入正确的手机号')
    return
  }
  try {
    sendingPhoneCode.value = true
    // 调用发送验证码API
    await authStore.sendPhoneCode(phoneLoginForm.phone)
    ElMessage.success('验证码已发送')
    startPhoneCountdown()
  } catch (error) {
    console.error('发送验证码失败:', error)
  } finally {
    sendingPhoneCode.value = false
  }
}

const startPhoneCountdown = () => {
  phoneCodeCountdown.value = 60
  phoneCodeTimer.value = setInterval(() => {
    phoneCodeCountdown.value--
    if (phoneCodeCountdown.value <= 0) clearInterval(phoneCodeTimer.value)
  }, 1000)
}

// 发送邮箱验证码
const sendEmailCode = async () => {
  if (!isValidEmail(emailLoginForm.email)) {
    ElMessage.error('请输入正确的邮箱格式')
    return
  }
  try {
    sendingEmailCode.value = true
    await authStore.sendEmailCode(emailLoginForm.email)
    ElMessage.success('验证码已发送')
    startEmailCountdown()
  } catch (error) {
    console.error('发送验证码失败:', error)
  } finally {
    sendingEmailCode.value = false
  }
}

const startEmailCountdown = () => {
  emailCodeCountdown.value = 60
  emailCodeTimer.value = setInterval(() => {
    emailCodeCountdown.value--
    if (emailCodeCountdown.value <= 0) clearInterval(emailCodeTimer.value)
  }, 1000)
}

// 登录
const handleLogin = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    loading.value = true

    switch (activeTab.value) {
      case 'account':
        await authStore.login(loginForm)
        break
      case 'phone':
        await authStore.loginByPhone(phoneLoginForm)
        break
      case 'email':
        await authStore.loginByEmail(emailLoginForm)
        break
    }

    if (authStore.isLoggedIn) {
      ElMessage.success('登录成功')
      router.replace('/main/home')
    }
  } catch (error: any) {
    console.error('登录失败:', error)
    ElMessage.error(error.message || '登录失败，请重试')
  } finally {
    loading.value = false
  }
}

const showApplyTip = () => {
  ElMessage.info('账号申请功能即将开放，敬请期待')
}

const goBack = () => {
  router.push('/')
}

onBeforeUnmount(() => {
  if (phoneCodeTimer.value) clearInterval(phoneCodeTimer.value)
  if (emailCodeTimer.value) clearInterval(emailCodeTimer.value)
})
</script>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #10b981 0%, #059669 50%, #047857 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}
.decoration-1 { width: 200px; height: 200px; top: -100px; left: -100px; }
.decoration-2 { width: 300px; height: 300px; bottom: -150px; right: -150px; }
.decoration-3 { width: 150px; height: 150px; top: 50%; right: -75px; }

.login-card {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 480px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  padding: 50px 45px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  display: inline-flex;
  align-items: center;
  margin-bottom: 12px;
}

.logo-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: white;
}

.logo h1 {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.logo-section > p {
  font-size: 16px;
  color: #6b7280;
  margin: 0;
}

.login-tabs {
  display: flex;  
  margin-bottom: 35px;
  background: #f3f4f6;
  border-radius: 10px;
  padding: 4px;
}

.tab-item {
  flex: 1;
  padding: 12px 16px;
  text-align: center;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tab-item.active {
  background: white;
  color: #10b981;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.form-section {
  margin-bottom: 24px;
}

.code-input-group {
  display: flex;
  gap: 10px;
}

.code-input-group .el-input {
  flex: 1;
}

.code-button {
  width: 110px;
  color: #10b981;
  font-size: 14px;
}

.submit-section {
  margin: 35px 0 20px;
}

.login-button {
  width: 100%;
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 10px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  transition: all 0.2s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.4);
}

.footer-section {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
}

.link-button {
  color: #6b7280;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.2s ease;
}

.link-button:hover {
  color: #10b981;
}

.divider {
  color: #d1d5db;
  font-size: 14px;
}

.floating-elements {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.element {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.08);
  animation: float 8s ease-in-out infinite;
}

.element-1 { width: 60px; height: 60px; top: 20%; left: 10%; animation-delay: 0s; }
.element-2 { width: 40px; height: 40px; top: 60%; right: 15%; animation-delay: 2s; }
.element-3 { width: 80px; height: 80px; bottom: 10%; left: 20%; animation-delay: 4s; }

@media (max-width: 480px) {
  .login-card { padding: 30px 20px; }
  .login-tabs { margin-bottom: 20px; }
  .tab-item { font-size: 12px; padding: 10px 8px; }
  .logo h1 { font-size: 18px; }
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}
</style>

<!-- 公告弹窗全局样式 -->
<style>
.announcement-dialog {
  min-width: 450px !important;
  max-width: 600px !important;
  border-radius: 16px !important;
  overflow: hidden;
}

.announcement-dialog .el-message-box__header {
  padding: 24px 24px 16px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.announcement-dialog .el-message-box__title {
  font-size: 20px !important;
  font-weight: 600 !important;
  color: white !important;
}

.announcement-dialog .el-message-box__headerbtn {
  top: 20px;
  right: 20px;
}

.announcement-dialog .el-message-box__headerbtn .el-message-box__close {
  color: white !important;
}

.announcement-dialog .el-message-box__content {
  padding: 24px !important;
  font-size: 15px !important;
  line-height: 1.8 !important;
  color: #374151 !important;
  min-height: 80px;
}

.announcement-dialog .el-message-box__status {
  display: none !important;
}

.announcement-dialog .el-message-box__btns {
  padding: 16px 24px 24px !important;
}

.announcement-dialog .el-message-box__btns .el-button--primary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%) !important;
  border: none !important;
  border-radius: 8px !important;
  padding: 12px 32px !important;
  font-size: 15px !important;
  font-weight: 500 !important;
}

.announcement-dialog .el-message-box__btns .el-button--primary:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
}
</style>

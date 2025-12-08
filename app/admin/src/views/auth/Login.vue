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
            <i class="el-icon-platform"></i>
          </div>
          <h1>易投简历</h1>
        </div>
        <p>管理平台登录</p>
      </div>

      <!-- 登录表单 -->
      <el-form 
        ref="formRef" 
        :model="loginForm" 
        :rules="rules" 
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
              prefix-icon="el-icon-user"
              clearable
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
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
              prefix-icon="el-icon-phone"
              clearable
            />
          </el-form-item>
          <el-form-item prop="messageCode">
            <div class="code-input-group">
              <el-input
                v-model="phoneLoginForm.messageCode"
                placeholder="请输入验证码"
                prefix-icon="el-icon-message"
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
              prefix-icon="el-icon-message"
              clearable
            />
          </el-form-item>
          <el-form-item prop="messageCode">
            <div class="code-input-group">
              <el-input
                v-model="emailLoginForm.messageCode"
                placeholder="请输入验证码"
                prefix-icon="el-icon-message"
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
        <span @click="showComingSoon" class="link-button apply-link">
          申请获取账号
        </span>
        <span class="divider">|</span>
        <el-button type="text" @click="goBack" class="link-button back-link">
          返回首页
        </el-button>
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
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import { authApi, smsApi } from '@/api/admin'
import { isValidPhone, isValidEmail } from '@/utils'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref<FormInstance>()
const loading = ref(false)
const activeTab = ref<'account' | 'phone' | 'email'>('account')

// 手机验证码倒计时
const phoneCodeCountdown = ref(0)
const sendingPhoneCode = ref(false)
const phoneCodeTimer = ref<NodeJS.Timeout>()

// 邮箱验证码倒计时
const emailCodeCountdown = ref(0)
const sendingEmailCode = ref(false)
const emailCodeTimer = ref<NodeJS.Timeout>()

// 账号密码表单
const loginForm = reactive({
  accountOrPhoneOrEmail: '',
  password: ''
})

// 手机验证码表单
const phoneLoginForm = reactive({
  phone: '',
  messageCode: ''
})

// 邮箱验证码表单
const emailLoginForm = reactive({
  email: '',
  messageCode: ''
})

// 表单验证规则
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

// 验证码按钮文字
const phoneCodeButtonText = computed(() => {
  return phoneCodeCountdown.value > 0 ? `${phoneCodeCountdown.value}s` : '获取验证码'
})

const emailCodeButtonText = computed(() => {
  return emailCodeCountdown.value > 0 ? `${emailCodeCountdown.value}s` : '获取验证码'
})

// 切换登录方式
const switchTab = (tab: 'account' | 'phone' | 'email') => {
  activeTab.value = tab
  formRef.value?.clearValidate()
}

// 发送手机验证码
const sendPhoneCode = async () => {
  try {
    // 验证手机号
    if (!isValidPhone(phoneLoginForm.phone)) {
      ElMessage.error('请输入正确的手机号')
      return
    }

    sendingPhoneCode.value = true
    await smsApi.sendPhoneCode(phoneLoginForm.phone)
    
    ElMessage.success('验证码已发送')
    startPhoneCountdown()
  } catch (error) {
    console.error('发送验证码失败:', error)
  } finally {
    sendingPhoneCode.value = false
  }
}

// 启动手机验证码倒计时
const startPhoneCountdown = () => {
  phoneCodeCountdown.value = 60
  phoneCodeTimer.value = setInterval(() => {
    phoneCodeCountdown.value--
    if (phoneCodeCountdown.value <= 0) {
      clearInterval(phoneCodeTimer.value)
    }
  }, 1000)
}

// 发送邮箱验证码
const sendEmailCode = async () => {
  try {
    // 验证邮箱
    if (!isValidEmail(emailLoginForm.email)) {
      ElMessage.error('请输入正确的邮箱格式')
      return
    }

    sendingEmailCode.value = true
    await authApi.sendEmailCode(emailLoginForm.email)
    
    ElMessage.success('验证码已发送')
    startEmailCountdown()
  } catch (error) {
    console.error('发送验证码失败:', error)
  } finally {
    sendingEmailCode.value = false
  }
}

// 启动邮箱验证码倒计时
const startEmailCountdown = () => {
  emailCodeCountdown.value = 60
  emailCodeTimer.value = setInterval(() => {
    emailCodeCountdown.value--
    if (emailCodeCountdown.value <= 0) {
      clearInterval(emailCodeTimer.value)
    }
  }, 1000)
}

// 登录处理
const handleLogin = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    
    loading.value = true
    let result: string

    switch (activeTab.value) {
      case 'account':
        result = await authStore.login(loginForm)
        break
      case 'phone':
        result = await authStore.loginByPhone(phoneLoginForm)
        break
      case 'email':
        result = await authStore.loginByEmail(emailLoginForm)
        break
    }

    if (result) {
      ElMessage.success('登录成功')
      router.push('/admin/dashboard')
    }
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}

// 跳转到注册页
const showComingSoon = () => {
  const customAlert = document.createElement('div')
  customAlert.style.cssText = `
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(249, 250, 251, 0.95) 100%);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.2);
    border-radius: 16px;
    padding: 32px;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
    z-index: 9999;
    min-width: 320px;
    text-align: center;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  `
  
  customAlert.innerHTML = `
    <div style="
      width: 48px;
      height: 48px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 50%;
      margin: 0 auto 16px;
      display: flex;
      align-items: center;
      justify-content: center;
    ">
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M12 8V12M12 16H12.01M22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12Z" 
          stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
    </div>
    <h3 style="color: #1f2937; margin: 0 0 8px 0; font-size: 18px; font-weight: 600;">功能未开发提示</h3>
    <p style="color: #6b7280; margin: 0 0 24px 0; font-size: 14px; line-height: 1.5;">该功能尚未完成，请等待！</p>
    <button id="loginCloseBtn" style="
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      border-radius: 8px;
      padding: 10px 24px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
    ">确定</button>
  `
  
  document.body.appendChild(customAlert)
  
  // 遮罩层
  const overlay = document.createElement('div')
  overlay.style.cssText = `
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(2px);
    z-index: 9998;
  `
  overlay.onclick = () => {
    customAlert.remove()
    overlay.remove()
    // 刷新页面
    window.location.reload()
  }
  
  // 为确定按钮添加点击事件
  const loginCloseBtn = document.getElementById('loginCloseBtn')
  loginCloseBtn.onclick = () => {
    customAlert.remove()
    overlay.remove()
    // 刷新页面
    window.location.reload()
  }
  
  document.body.appendChild(overlay)
}

// 返回首页
const goBack = () => {
  router.push('/')
}

// 清理定时器
onBeforeUnmount(() => {
  if (phoneCodeTimer.value) {
    clearInterval(phoneCodeTimer.value)
  }
  if (emailCodeTimer.value) {
    clearInterval(emailCodeTimer.value)
  }
})
</script>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
.decoration-1 {
  width: 200px;
  height: 200px;
  top: -100px;
  left: -100px;
}
.decoration-2 {
  width: 300px;
  height: 300px;
  bottom: -150px;
  right: -150px;
}
.decoration-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: -75px;
}

.login-card {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 480px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 12px;
  padding: 50px 45px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.logo-icon i {
  font-size: 20px;
  color: white;
}

.logo h1 {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.logo-section > p {
  font-size: 16px;
  color: #6b7280;
  margin: 0;
  font-weight: 400;
}

.login-tabs {
  display: flex;  
  margin-bottom: 35px;
  background: #f8f9fa;
  border-radius: 8px;
  padding: 3px;
}

.tab-item {
  flex: 1;
  padding: 12px 16px;
  text-align: center;
  border-radius: 6px;
  font-size: 15px;
  font-weight: 500;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tab-item.active {
  background: white;
  color: #667eea;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
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
  width: 100px;
  color: #3b82f6;
  font-size: 14px;
}

.submit-section {
  margin: 35px 0;
}

.login-button {
  width: 100%;
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.2s ease;
}

.login-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.footer-section {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 25px;
}

.link-button {
  color: #6b7280;
  font-size: 14px;
  padding: 0;
  font-weight: 400;
  cursor: pointer;
  transition: color 0.2s ease;
}

.link-button:hover {
  color: #667eea;
}

.apply-link {
  text-decoration: none;
}

.back-link {
  text-decoration: none;
}

.divider {
  color: #d1d5db;
  font-size: 14px;
  font-weight: 300;
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
  background: rgba(255, 255, 255, 0.05);
  animation: float 8s ease-in-out infinite;
}

.element-1 {
  width: 60px;
  height: 60px;
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.element-2 {
  width: 40px;
  height: 40px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.element-3 {
  width: 80px;
  height: 80px;
  bottom: 10%;
  left: 20%;
  animation-delay: 4s;
}

@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
    margin: 20px;
  }
  
  .login-tabs {
    margin-bottom: 20px;
  }
  
  .tab-item {
    font-size: 12px;
    padding: 8px;
  }
  
  .logo h1 {
    font-size: 20px;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}
</style>
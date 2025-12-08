<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <div class="logo-container">
          <div class="logo">
            <i class="el-icon-document-copy"></i>
          </div>
          <h1>易投简历</h1>
          <subtitle>管理端注册</subtitle>
        </div>
      </div>

      <div class="register-body">
        <el-tabs v-model="registerType" class="register-tabs">
          <el-tab-pane label="账号注册" name="account">
            <el-form
              ref="accountFormRef"
              :model="accountForm"
              :rules="accountRules"
              @keyup.enter="handleAccountRegister"
            >
              <el-form-item prop="username">
                <el-input
                  v-model="accountForm.username"
                  placeholder="用户名（4-20位字母数字下划线）"
                  size="large"
                  prefix-icon="el-icon-user"
                />
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model="accountForm.password"
                  type="password"
                  placeholder="密码（6-20位）"
                  size="large"
                  prefix-icon="el-icon-lock"
                  show-password
                />
              </el-form-item>
              <el-form-item prop="confirmPassword">
                <el-input
                  v-model="accountForm.confirmPassword"
                  type="password"
                  placeholder="确认密码"
                  size="large"
                  prefix-icon="el-icon-lock"
                  show-password
                />
              </el-form-item>
              <el-form-item prop="email">
                <el-input
                  v-model="accountForm.email"
                  placeholder="邮箱地址"
                  size="large"
                  prefix-icon="el-icon-message"
                />
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="手机注册" name="phone">
            <el-form
              ref="phoneFormRef"
              :model="phoneForm"
              :rules="phoneRules"
              @keyup.enter="handlePhoneRegister"
            >
              <el-form-item prop="phone">
                <el-input
                  v-model="phoneForm.phone"
                  placeholder="手机号码"
                  size="large"
                  prefix-icon="el-icon-phone"
                />
              </el-form-item>
              <el-form-item prop="code">
                <div class="code-input-group">
                  <el-input
                    v-model="phoneForm.code"
                    placeholder="短信验证码"
                    size="large"
                    prefix-icon="el-icon-key"
                  />
                  <el-button
                    :disabled="codeTimer > 0"
                    :loading="sendingCode"
                    @click="sendPhoneCode"
                    size="large"
                    class="code-button"
                  >
                    {{ codeTimer > 0 ? `${codeTimer}秒后重试` : '获取验证码' }}
                  </el-button>
                </div>
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model="phoneForm.password"
                  type="password"
                  placeholder="设置密码（6-20位）"
                  size="large"
                  prefix-icon="el-icon-lock"
                  show-password
                />
              </el-form-item>
              <el-form-item prop="confirmPassword">
                <el-input
                  v-model="phoneForm.confirmPassword"
                  type="password"
                  placeholder="确认密码"
                  size="large"
                  prefix-icon="el-icon-lock"
                  show-password
                />
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="邮箱注册" name="email">
            <el-form
              ref="emailFormRef"
              :model="emailForm"
              :rules="emailRules"
              @keyup.enter="handleEmailRegister"
            >
              <el-form-item prop="email">
                <el-input
                  v-model="emailForm.email"
                  placeholder="邮箱地址"
                  size="large"
                  prefix-icon="el-icon-message"
                />
              </el-form-item>
              <el-form-item prop="code">
                <div class="code-input-group">
                  <el-input
                    v-model="emailForm.code"
                    placeholder="邮箱验证码"
                    size="large"
                    prefix-icon="el-icon-key"
                  />
                  <el-button
                    :disabled="emailCodeTimer > 0"
                    :loading="sendingEmailCode"
                    @click="sendEmailCode"
                    size="large"
                    class="code-button"
                  >
                    {{ emailCodeTimer > 0 ? `${emailCodeTimer}秒后重试` : '获取验证码' }}
                  </el-button>
                </div>
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model="emailForm.password"
                  type="password"
                  placeholder="设置密码（6-20位）"
                  size="large"
                  prefix-icon="el-icon-lock"
                  show-password
                />
              </el-form-item>
              <el-form-item prop="confirmPassword">
                <el-input
                  v-model="emailForm.confirmPassword"
                  type="password"
                  placeholder="确认密码"
                  size="large"
                  prefix-icon="el-icon-lock"
                  show-password
                />
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>

        <div class="register-actions">
          <el-button
            type="primary"
            size="large"
            :loading="registering"
            @click="handleRegister"
            class="register-button"
          >
            {{ registering ? '注册中...' : '立即注册' }}
          </el-button>
        </div>

        <div class="register-footer">
          <div class="agreement">
            <el-checkbox v-model="agreeTerms">
              我已阅读并同意
              <a href="#" @click.prevent="showTerms">《用户协议》</a>
              和
              <a href="#" @click.prevent="showPrivacy">《隐私政策》</a>
            </el-checkbox>
          </div>
          <div class="login-link">
            已有账号？
            <router-link to="/login">立即登录</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const authStore = useAuthStore()

// 注册方式
const registerType = ref('account')

// 表单引用
const accountFormRef = ref(null)
const phoneFormRef = ref(null)
const emailFormRef = ref(null)

// 状态
const registering = ref(false)
const sendingCode = ref(false)
const sendingEmailCode = ref(false)
const codeTimer = ref(0)
const emailCodeTimer = ref(0)
const agreeTerms = ref(false)

// 账号注册表单
const accountForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: ''
})

// 手机注册表单
const phoneForm = reactive({
  phone: '',
  code: '',
  password: '',
  confirmPassword: ''
})

// 邮箱注册表单
const emailForm = reactive({
  email: '',
  code: '',
  password: '',
  confirmPassword: ''
})

// 校验规则
const validateConfirmPassword = (rule: any, value: string, callback: Function) => {
  const form = rule.field.includes('account') ? accountForm : rule.field.includes('phone') ? phoneForm : emailForm
  if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const accountRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度在 4 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const phoneRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度为6位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const emailRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度为6位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 发送手机验证码
const sendPhoneCode = async () => {
  if (!phoneForm.phone) {
    ElMessage.warning('请先输入手机号')
    return
  }
  
  if (!/^1[3-9]\d{9}$/.test(phoneForm.phone)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  
  sendingCode.value = true
  try {
    // await authApi.sendPhoneCode(phoneForm.phone)
    ElMessage.success('验证码已发送')
    
    // 开始倒计时
    codeTimer.value = 60
    const timer = setInterval(() => {
      codeTimer.value--
      if (codeTimer.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    ElMessage.error('发送验证码失败')
  } finally {
    sendingCode.value = false
  }
}

// 发送邮箱验证码
const sendEmailCode = async () => {
  if (!emailForm.email) {
    ElMessage.warning('请先输入邮箱')
    return
  }
  
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailForm.email)) {
    ElMessage.warning('请输入正确的邮箱格式')
    return
  }
  
  sendingEmailCode.value = true
  try {
    // await authApi.sendEmailCode(emailForm.email)
    ElMessage.success('验证码已发送')
    
    // 开始倒计时
    emailCodeTimer.value = 60
    const timer = setInterval(() => {
      emailCodeTimer.value--
      if (emailCodeTimer.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    ElMessage.error('发送验证码失败')
  } finally {
    sendingEmailCode.value = false
  }
}

// 注册处理
const handleRegister = async () => {
  if (!agreeTerms.value) {
    ElMessage.warning('请先同意用户协议和隐私政策')
    return
  }
  
  let formRef
  let formData
  
  if (registerType.value === 'account') {
    formRef = accountFormRef.value
    formData = {
      type: 'account',
      username: accountForm.username,
      password: accountForm.password,
      email: accountForm.email
    }
  } else if (registerType.value === 'phone') {
    formRef = phoneFormRef.value
    formData = {
      type: 'phone',
      phone: phoneForm.phone,
      code: phoneForm.code,
      password: phoneForm.password
    }
  } else {
    formRef = emailFormRef.value
    formData = {
      type: 'email',
      email: emailForm.email,
      code: emailForm.code,
      password: emailForm.password
    }
  }
  
  try {
    await formRef.validate()
  } catch {
    return
  }
  
  registering.value = true
  try {
    // await authStore.register(formData)
    ElMessage.success('注册成功！')
    router.push('/login')
  } catch (error: any) {
    ElMessage.error(error.message || '注册失败')
  } finally {
    registering.value = false
  }
}

// 显示用户协议
const showTerms = () => {
  ElMessageBox.alert('这里是用户协议内容...', '用户协议', {
    confirmButtonText: '确定'
  })
}

// 显示隐私政策
const showPrivacy = () => {
  ElMessageBox.alert('这里是隐私政策内容...', '隐私政策', {
    confirmButtonText: '确定'
  })
}
</script>

<style scoped lang="scss">
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-card {
  width: 100%;
  max-width: 460px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  animation: fadeInUp 0.6s ease;
}

.register-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px 30px;
  text-align: center;
}

.logo-container {
  .logo {
    width: 60px;
    height: 60px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 16px;
    font-size: 24px;
    backdrop-filter: blur(10px);
  }
  
  h1 {
    font-size: 28px;
    font-weight: 700;
    margin: 0 0 8px 0;
  }
  
  subtitle {
    font-size: 16px;
    opacity: 0.9;
  }
}

.register-body {
  padding: 40px 30px;
}

.register-tabs {
  margin-bottom: 30px;
  
  :deep(.el-tabs__header) {
    margin-bottom: 24px;
  }
  
  :deep(.el-tabs__nav-wrap) {
    after {
      display: none;
    }
  }
  
  :deep(.el-tabs__item) {
    font-weight: 500;
    color: #6b7280;
  }
  
  :deep(.el-tabs__item.is-active) {
    color: #667eea;
    font-weight: 600;
  }
  
  :deep(.el-tabs__active-bar) {
    background-color: #667eea;
    height: 3px;
    border-radius: 2px;
  }
}

.code-input-group {
  display: flex;
  gap: 12px;
  
  .el-input {
    flex: 1;
  }
  
  .code-button {
    flex-shrink: 0;
    min-width: 120px;
  }
}

.register-actions {
  margin-bottom: 24px;
}

.register-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
  }
}

.register-footer {
  text-align: center;
  
  .agreement {
    margin-bottom: 16px;
    
    :deep(.el-checkbox__label) {
      font-size: 14px;
      color: #6b7280;
    }
    
    a {
      color: #667eea;
      text-decoration: none;
      
      &:hover {
        text-decoration: underline;
      }
    }
  }
  
  .login-link {
    font-size: 14px;
    color: #6b7280;
    
    a {
      color: #667eea;
      text-decoration: none;
      font-weight: 500;
      
      &:hover {
        text-decoration: underline;
      }
    }
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 响应式设计
@media (max-width: 576px) {
  .register-container {
    padding: 16px;
  }
  
  .register-card {
    max-width: none;
  }
  
  .register-header {
    padding: 30px 20px;
  }
  
  .register-body {
    padding: 30px 20px;
  }
  
  .logo-container {
    .logo {
      width: 50px;
      height: 50px;
      font-size: 20px;
    }
    
    h1 {
      font-size: 24px;
    }
    
    subtitle {
      font-size: 14px;
    }
  }
  
  .code-input-group {
    flex-direction: column;
    gap: 12px;
    
    .code-button {
      min-width: auto;
    }
  }
}
</style>
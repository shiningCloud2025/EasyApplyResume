<template>
  <div class="welcome-container">
    <!-- 背景动画 -->
    <div class="background-animation">
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
        <div class="shape shape-4"></div>
        <div class="shape shape-5"></div>
      </div>
    </div>

    <!-- 主要内容 -->
    <div class="main-content">
      <!-- Logo区域 -->
      <div class="logo-section">
        <div class="logo">
          <div class="logo-icon">
            <i class="el-icon-document"></i>
          </div>
          <h1 class="logo-title">
            <span class="text-gradient">易投简历管理平台</span>
          </h1>
        </div>
        <p class="logo-subtitle">易投简历管理平台-管理端</p>
      </div>

      <!-- 欢迎信息 -->
      <div class="welcome-section">
        <h2 class="welcome-title">
          欢迎来到 <span class="text-gradient">易投简历管理平台</span>
        </h2>
        <p class="welcome-description">
          高效管理简历模板、职位信息、用户反馈的系统平台
        </p>
      </div>

      <!-- 特色功能 -->
      <div class="features-section">
        <div class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-user"></i>
          </div>
          <h3>权限管理</h3>
          <p>精细化的权限控制，系统安全有保障</p>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-document-remove"></i>
          </div>
          <h3>业务管理</h3>
          <p>丰富的管理模块，满足管理者不同需求</p>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
          <h3>工具管理</h3>
          <p>丰富的工具模块，助力管理员提效</p>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-cpu"></i>
          </div>
          <h3>AI智能</h3>
          <p>系统智能问答助手，提升管理员工作效率</p>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="action-section">
        <el-button 
          type="primary" 
          size="large"
          @click="goToLogin"
          class="action-button login-button"
        >
          <i class="el-icon-right"></i>
          立即登录
        </el-button>
        
        <el-button 
          size="large"
          @click="goToRegister"
          class="action-button register-button"
        >
          <i class="el-icon-plus"></i>
          申请获取账号
        </el-button>
      </div>

      <!-- 统计数据 -->
      <div class="stats-section">
        <div class="stat-item">
          <span class="stat-number">10+</span>
          <span class="stat-label">模块数量</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">100+</span>
          <span class="stat-label">注册用户</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">99.9%</span>
          <span class="stat-label">系统稳定性</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLogin = () => {
  router.push('/login')
}

const goToRegister = () => {
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
    <button id="closeBtn" style="
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
  
  // 关闭函数
  const closeDialog = () => {
    customAlert.remove()
    overlay.remove()
    // 刷新页面
    window.location.reload()
  }
  
  // 绑定关闭事件
  document.getElementById('closeBtn').onclick = closeDialog
  overlay.onclick = closeDialog
  
  document.body.appendChild(overlay)
}
</script>

<style scoped lang="scss">
.welcome-container {
  min-height: 100vh;
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.background-animation {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.floating-shapes {
  position: relative;
  width: 100%;
  height: 100%;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 80px;
  height: 80px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 120px;
  height: 120px;
  top: 20%;
  right: 10%;
  animation-delay: 2s;
}

.shape-3 {
  width: 60px;
  height: 60px;
  bottom: 30%;
  left: 20%;
  animation-delay: 1s;
}

.shape-4 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  right: 25%;
  animation-delay: 3s;
}

.shape-5 {
  width: 90px;
  height: 90px;
  top: 60%;
  left: 60%;
  animation-delay: 4s;
}

.main-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  width: 100%;
  padding: 0 20px;
  text-align: center;
}

.logo-section {
  margin-bottom: 3rem;
}

.logo {
  display: inline-flex;
  align-items: center;
  margin-bottom: 1rem;
}

.logo-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 1rem;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.logo-icon i {
  font-size: 24px;
  color: white;
}

.logo-title {
  font-size: 3rem;
  font-weight: 700;
  margin: 0;
  background: linear-gradient(135deg, #ffffff 0%, #e0e7ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo-subtitle {
  font-size: 1.125rem;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.welcome-section {
  margin-bottom: 4rem;
}

.welcome-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: white;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.welcome-description {
  font-size: 1.25rem;
  color: rgba(255, 255, 255, 0.7);
  max-width: 600px;
  margin: 0 auto;
}

.features-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 4rem;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 2rem;
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.15);
}

.feature-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
}

.feature-icon i {
  font-size: 20px;
  color: white;
}

.feature-card h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: white;
  margin-bottom: 0.5rem;
}

.feature-card p {
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
}

.action-section {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-bottom: 4rem;
}

.action-button {
  padding: 12px 32px;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login-button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 35px rgba(102, 126, 234, 0.5);
}

.register-button {
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  color: white;
}

.register-button:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.stats-section {
  display: flex;
  justify-content: center;
  gap: 4rem;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 700;
  color: white;
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.7);
}

@media (max-width: 768px) {
  .logo-title {
    font-size: 2rem;
  }
  
  .welcome-title {
    font-size: 1.75rem;
  }
  
  .features-section {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .action-section {
    flex-direction: column;
    align-items: center;
  }
  
  .stats-section {
    gap: 2rem;
  }
  
  .stat-number {
    font-size: 2rem;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}
</style>
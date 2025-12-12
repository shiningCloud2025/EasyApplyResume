<template>
  <div class="error-page">
    <div class="error-container">
      <!-- 错误图标 -->
      <div class="error-icon">
        <svg width="120" height="120" viewBox="0 0 120 120" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="60" cy="60" r="60" fill="#FEE2E2"/>
          <path d="M60 30V60M60 90H60.01" stroke="#DC2626" stroke-width="8" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>

      <!-- 错误信息 -->
      <div class="error-content">
        <h1 class="error-code">403</h1>
        <h2 class="error-title">权限不足</h2>
        <p class="error-description">
          抱歉，您没有权限访问该页面或执行该操作。<br>
          如需获得相关权限，请联系系统管理员。
        </p>
      </div>

      <!-- 操作按钮 -->
      <div class="error-actions">
        <el-button type="primary" @click="goBack" size="large">
          <i class="el-icon-back"></i>
          返回上一页
        </el-button>
        <el-button @click="goHome" size="large">
          <i class="el-icon-house"></i>
          返回首页
        </el-button>
      </div>

      <!-- 联系管理员卡片 -->
      <div class="contact-card">
        <div class="contact-header">
          <i class="el-icon-message"></i>
          <span>联系管理员</span>
        </div>
        <div class="contact-content">
          <div class="contact-item">
            <label>管理员邮箱：</label>
            <a href="mailto:admin@easyapplyresume.com" class="contact-link">admin@easyapplyresume.com</a>
          </div>
          <div class="contact-item">
            <label>申请权限：</label>
            <el-button 
              type="text" 
              class="apply-button"
              @click="showPermissionApplication"
            >
              点击填写权限申请
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 装饰性元素 -->
    <div class="decoration-elements">
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 返回上一页
const goBack = () => {
  if (window.history.length > 1) {
    router.go(-1)
  } else {
    router.push('/admin/dashboard')
  }
}

// 返回首页
const goHome = () => {
  router.push('/admin/dashboard')
}

// 显示权限申请弹窗
const showPermissionApplication = () => {
  const modal = document.createElement('div')
  modal.style.cssText = `
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: white;
    border-radius: 16px;
    padding: 32px;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
    z-index: 9999;
    min-width: 400px;
    max-width: 90%;
  `
  
  modal.innerHTML = `
    <h3 style="color: #1f2937; margin: 0 0 16px 0; font-size: 18px; font-weight: 600;">
      权限申请
    </h3>
    <p style="color: #6b7280; margin: 0 0 24px 0; font-size: 14px; line-height: 1.5;">
      请填写以下信息申请相关权限：
    </p>
    <form id="permissionForm">
      <div style="margin-bottom: 16px;">
        <label style="display: block; font-size: 14px; color: #374151; margin-bottom: 6px;">姓名</label>
        <input type="text" name="name" required style="width: 100%; padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px;">
      </div>
      <div style="margin-bottom: 16px;">
        <label style="display: block; font-size: 14px; color: #374151; margin-bottom: 6px;">邮箱</label>
        <input type="email" name="email" required style="width: 100%; padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px;">
      </div>
      <div style="margin-bottom: 16px;">
        <label style="display: block; font-size: 14px; color: #374151; margin-bottom: 6px;">申请权限</label>
        <textarea name="permission" required rows="3" style="width: 100%; padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; resize: vertical;" placeholder="请描述您需要的权限..."></textarea>
      </div>
      <div style="margin-bottom: 24px;">
        <label style="display: block; font-size: 14px; color: #374151; margin-bottom: 6px;">申请理由</label>
        <textarea name="reason" required rows="3" style="width: 100%; padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; resize: vertical;" placeholder="请说明申请该权限的理由..."></textarea>
      </div>
      <div style="display: flex; gap: 12px; justify-content: flex-end;">
        <button type="button" onclick="closePermissionModal()" style="padding: 8px 16px; border: 1px solid #d1d5db; background: white; color: #374151; border-radius: 6px; cursor: pointer; font-size: 14px;">
          取消
        </button>
        <button type="submit" style="padding: 8px 16px; background: #dc2626; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 14px;">
          提交申请
        </button>
      </div>
    </form>
  `
  
  // 遮罩层
  const overlay = document.createElement('div')
  overlay.style.cssText = `
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    z-index: 9998;
  `
  
  // 关闭弹窗函数
  ;(window as any).closePermissionModal = () => {
    modal.remove()
    overlay.remove()
    delete (window as any).closePermissionModal
    delete (window as any).submitPermissionForm
  }
  
  // 提交表单函数
  ;(window as any).submitPermissionForm = (e: Event) => {
    e.preventDefault()
    const formData = new FormData(e.target as HTMLFormElement)
    const data = Object.fromEntries(formData.entries())
    
    console.log('权限申请数据:', data)
    ElMessage.success('权限申请已提交，等待管理员审核')
    ;(window as any).closePermissionModal()
  }
  
  // 绑定表单提交事件
  const form = modal.querySelector('#permissionForm') as HTMLFormElement
  form.onsubmit = (window as any).submitPermissionForm
  
  overlay.onclick = () => (window as any).closePermissionModal()
  
  document.body.appendChild(overlay)
  document.body.appendChild(modal)
}
</script>

<style scoped lang="scss">
.error-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  position: relative;
  overflow: hidden;
}

.error-container {
  position: relative;
  z-index: 2;
  text-align: center;
  max-width: 600px;
  padding: 40px;
}

.error-icon {
  margin-bottom: 32px;
  opacity: 0.9;
}

.error-code {
  font-size: 72px;
  font-weight: 700;
  color: #dc2626;
  margin: 0 0 16px 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.error-title {
  font-size: 32px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 16px 0;
}

.error-description {
  font-size: 16px;
  color: #6b7280;
  line-height: 1.6;
  margin: 0 0 40px 0;
}

.error-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 40px;
}

.contact-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.contact-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
}

.contact-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
}

.contact-item label {
  min-width: 80px;
  color: #6b7280;
  font-weight: 500;
}

.contact-link {
  color: #3b82f6;
  text-decoration: none;
  transition: color 0.2s ease;
}

.contact-link:hover {
  color: #2563eb;
  text-decoration: underline;
}

.apply-button {
  color: #3b82f6;
  padding: 4px 0;
  font-size: 14px;
}

.apply-button:hover {
  color: #2563eb;
}

.decoration-elements {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.circle-1 {
  width: 200px;
  height: 200px;
  background: #dc2626;
  top: -100px;
  left: -100px;
}

.circle-2 {
  width: 150px;
  height: 150px;
  background: #3b82f6;
  bottom: -75px;
  right: -75px;
}

.circle-3 {
  width: 100px;
  height: 100px;
  background: #10b981;
  top: 20%;
  right: -50px;
}

@media (max-width: 768px) {
  .error-container {
    padding: 20px;
    margin: 20px;
  }
  
  .error-code {
    font-size: 48px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .error-actions {
    flex-direction: column;
    gap: 12px;
  }
  
  .error-actions .el-button {
    width: 100%;
  }
}
</style>
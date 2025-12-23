<template>
  <div class="coming-soon-page">
    <div class="page-header">
      <h1>{{ isAdmin ? '管理端' : '用户端' }}日志管理</h1>
      <el-tag type="warning" size="large">开发中</el-tag>
    </div>

    <div class="content-wrapper">
      <el-card class="main-card" shadow="hover">
        <div class="card-bg-decoration">
          <div class="bg-circle bg-circle-1"></div>
          <div class="bg-circle bg-circle-2"></div>
          <div class="bg-circle bg-circle-3"></div>
        </div>
        
        <div class="card-content">
          <div class="icon-wrapper">
            <div class="icon-bg">
              <el-icon :size="32"><Document /></el-icon>
            </div>
          </div>
          
          <h2>{{ isAdmin ? '管理端' : '用户端' }}日志管理</h2>
          <p class="subtitle">完善的日志采集、分析与追踪系统，敬请期待</p>
          
          <div class="features">
            <div class="feature-item">
              <div class="feature-icon">
                <el-icon><View /></el-icon>
              </div>
              <div class="feature-content">
                <span class="feature-title">访问日志</span>
                <span class="feature-desc">用户访问轨迹记录</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <el-icon><EditPen /></el-icon>
              </div>
              <div class="feature-content">
                <span class="feature-title">操作日志</span>
                <span class="feature-desc">系统操作行为追踪</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="feature-content">
                <span class="feature-title">异常日志</span>
                <span class="feature-desc">错误信息快速定位</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <el-icon><Search /></el-icon>
              </div>
              <div class="feature-content">
                <span class="feature-title">日志检索</span>
                <span class="feature-desc">多维度日志查询</span>
              </div>
            </div>
          </div>
          
          <div class="status-section">
            <div class="status-content">
              <div class="status-icon">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="status-text">
                <h3>功能开发中</h3>
                <p>日志采用 AOP + 文件存储方式，该功能正在开发中</p>
              </div>
            </div>
            <el-button size="large" type="info" disabled class="status-button">
              <el-icon><Bell /></el-icon>
              敬请期待
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Document, View, EditPen, Warning, Search, Clock, Bell } from '@element-plus/icons-vue'

const route = useRoute()
const isAdmin = computed(() => route.path.includes('admin-monitor'))
</script>

<style scoped lang="scss">
.coming-soon-page {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  
  h1 {
    margin: 0;
    font-size: 22px;
    font-weight: 600;
    color: #1a202c;
  }
}

.content-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.main-card {
  width: 100%;
  max-width: 800px;
  border-radius: 20px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  overflow: hidden;
  position: relative;
  
  :deep(.el-card__body) {
    padding: 0;
  }
}

.card-bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  pointer-events: none;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
  
  &.bg-circle-1 {
    width: 200px;
    height: 200px;
    top: -50px;
    right: -50px;
    animation-delay: 0s;
  }
  
  &.bg-circle-2 {
    width: 150px;
    height: 150px;
    bottom: -30px;
    left: -30px;
    animation-delay: 2s;
  }
  
  &.bg-circle-3 {
    width: 100px;
    height: 100px;
    top: 50%;
    left: 30%;
    animation-delay: 4s;
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0px) scale(1); }
  50% { transform: translateY(-15px) scale(1.05); }
}

.card-content {
  background: rgba(255, 255, 255, 0.98);
  padding: 40px;
  text-align: center;
  position: relative;
  z-index: 1;
}

.icon-wrapper {
  margin-bottom: 20px;
  
  .icon-bg {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 80px;
    height: 80px;
    border-radius: 24px;
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    box-shadow: 0 15px 35px rgba(16, 185, 129, 0.3);
    color: white;
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
      animation: shine 3s infinite;
    }
  }
}

@keyframes shine {
  0% { left: -100%; }
  100% { left: 100%; }
}

h2 {
  margin: 0 0 12px 0;
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  margin: 0 0 30px 0;
  color: #64748b;
  font-size: 16px;
  line-height: 1.6;
}

.features {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 30px;
  
  .feature-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px 16px;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-radius: 16px;
    border: 1px solid rgba(16, 185, 129, 0.1);
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 12px 30px rgba(16, 185, 129, 0.15);
      background: white;
    }
    
    .feature-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 48px;
      height: 48px;
      border-radius: 14px;
      background: linear-gradient(135deg, #10b981 0%, #059669 100%);
      margin-bottom: 12px;
      color: white;
      font-size: 20px;
    }
    
    .feature-content {
      text-align: center;
      
      .feature-title {
        display: block;
        font-size: 14px;
        font-weight: 600;
        color: #1e293b;
        margin-bottom: 4px;
      }
      
      .feature-desc {
        font-size: 12px;
        color: #64748b;
      }
    }
  }
}

.status-section {
  padding: 24px;
  background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
  border-radius: 16px;
  border: 1px solid rgba(245, 158, 11, 0.2);
  
  .status-content {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 16px;
    margin-bottom: 16px;
    
    .status-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 40px;
      border-radius: 12px;
      background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
      color: white;
      font-size: 18px;
    }
    
    .status-text {
      text-align: left;
      
      h3 {
        margin: 0 0 4px 0;
        font-size: 18px;
        font-weight: 600;
        color: #92400e;
      }
      
      p {
        margin: 0;
        font-size: 14px;
        color: #b45309;
      }
    }
  }
  
  .status-button {
    font-size: 16px;
    padding: 12px 32px;
    border-radius: 12px;
    background: #9ca3af;
    border: none;
  }
}

@media (max-width: 768px) {
  .features {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .card-content {
    padding: 24px;
  }
  
  h2 {
    font-size: 22px;
  }
}
</style>

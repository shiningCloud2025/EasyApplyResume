<template>
  <div class="coming-soon-page">
    <div class="page-header">
      <h1>MinIO 对象存储</h1>
      <el-tag type="success" size="large">可用</el-tag>
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
              <el-icon :size="32"><FolderOpened /></el-icon>
            </div>
          </div>
          
          <h2>MinIO 对象存储管理</h2>
          <p class="subtitle">高性能分布式对象存储系统，管理上传的图片、文件等资源</p>
          
          <div class="features">
            <div class="feature-item">
              <div class="feature-icon">
                <el-icon><Upload /></el-icon>
              </div>
              <div class="feature-content">
                <span class="feature-title">文件上传</span>
                <span class="feature-desc">支持大文件分片上传</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <el-icon><Folder /></el-icon>
              </div>
              <div class="feature-content">
                <span class="feature-title">Bucket管理</span>
                <span class="feature-desc">存储桶创建与配置</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <el-icon><Picture /></el-icon>
              </div>
              <div class="feature-content">
                <span class="feature-title">资源预览</span>
                <span class="feature-desc">在线预览图片文件</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <el-icon><Lock /></el-icon>
              </div>
              <div class="feature-content">
                <span class="feature-title">权限控制</span>
                <span class="feature-desc">访问权限策略管理</span>
              </div>
            </div>
          </div>
          
          <div class="action-section">
            <div class="action-content">
              <div class="action-icon">
                <el-icon><Right /></el-icon>
              </div>
              <div class="action-text">
                <h3>开始使用</h3>
                <p>点击下方按钮在新窗口中打开 MinIO 控制台</p>
              </div>
            </div>
            <el-button
              size="large"
              type="primary"
              @click="openMinio"
              class="action-button"
              :loading="loading"
              :disabled="!canOpenMinio"
            >
              <el-icon><TopRight /></el-icon>
              打开 MinIO 控制台
            </el-button>
          </div>
          
          <div class="quick-info">
            <div class="info-item">
              <el-icon><Link /></el-icon>
              <span>http://117.50.184.138:37214</span>
            </div>
            <div class="info-item">
              <el-icon><CircleCheck /></el-icon>
              <span>服务运行中</span>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { FolderOpened, Upload, Folder, Picture, Lock, Right, TopRight, Link, CircleCheck } from '@element-plus/icons-vue'
import { useAuthStore, middlewareManagementPermissions } from '@/store/auth'

const authStore = useAuthStore()
const loading = ref(false)
const canOpenMinio = computed(() => authStore.canAccessRoute(middlewareManagementPermissions.minio))

const openMinio = async () => {
  if (!canOpenMinio.value) {
    ElMessage.warning('暂无 MinIO 管理权限')
    return
  }

  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 500))
    window.open('http://117.50.184.138:37214', '_blank')
    ElMessage.success('正在打开 MinIO 控制台')
  } finally {
    loading.value = false
  }
}
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

.action-section {
  padding: 24px;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
  border-radius: 16px;
  border: 1px solid rgba(16, 185, 129, 0.2);
  margin-bottom: 20px;
  
  .action-content {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 16px;
    margin-bottom: 16px;
    
    .action-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 40px;
      border-radius: 12px;
      background: linear-gradient(135deg, #10b981 0%, #059669 100%);
      color: white;
      font-size: 18px;
    }
    
    .action-text {
      text-align: left;
      
      h3 {
        margin: 0 0 4px 0;
        font-size: 18px;
        font-weight: 600;
        color: #065f46;
      }
      
      p {
        margin: 0;
        font-size: 14px;
        color: #047857;
      }
    }
  }
  
  .action-button {
    font-size: 16px;
    padding: 12px 32px;
    border-radius: 12px;
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    border: none;
    font-weight: 600;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 25px rgba(16, 185, 129, 0.4);
    }
  }
}

.quick-info {
  display: flex;
  justify-content: center;
  gap: 30px;
  
  .info-item {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #64748b;
    font-size: 14px;
    
    .el-icon {
      color: #10b981;
    }
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
  
  .quick-info {
    flex-direction: column;
    gap: 12px;
  }
}
</style>

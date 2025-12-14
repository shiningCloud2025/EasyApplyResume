<template>
  <div class="portal-docs">
    <div class="docs-content">
      <div class="welcome-card">
        <div class="card-bg-decoration">
          <div class="bg-circle bg-circle-1"></div>
          <div class="bg-circle bg-circle-2"></div>
          <div class="bg-circle bg-circle-3"></div>
        </div>
        
        <el-card class="main-card" shadow="hover">
          <div class="welcome-content">
            <div class="icon-wrapper">
              <div class="icon-bg">
                <i class="el-icon-user-solid"></i>
              </div>
            </div>
            
            <h2>易投简历用户端</h2>
            <p class="subtitle">面向求职用户的简历制作与求职平台，提供完整的简历管理功能</p>
            
            <div class="features">
              <div class="feature-item">
                <div class="feature-icon">
                  <i class="el-icon-document"></i>
                </div>
                <div class="feature-content">
                  <span class="feature-title">简历制作</span>
                  <span class="feature-desc">在线制作精美简历</span>
                </div>
              </div>
              <div class="feature-item">
                <div class="feature-icon">
                  <i class="el-icon-search"></i>
                </div>
                <div class="feature-content">
                  <span class="feature-title">职位搜索</span>
                  <span class="feature-desc">智能匹配招聘信息</span>
                </div>
              </div>
              <div class="feature-item">
                <div class="feature-icon">
                  <i class="el-icon-cpu"></i>
                </div>
                <div class="feature-content">
                  <span class="feature-title">AI助手</span>
                  <span class="feature-desc">智能简历优化建议</span>
                </div>
              </div>
              <div class="feature-item">
                <div class="feature-icon">
                  <i class="el-icon-reading"></i>
                </div>
                <div class="feature-content">
                  <span class="feature-title">求职攻略</span>
                  <span class="feature-desc">海量求职技巧文章</span>
                </div>
              </div>
            </div>
            
            <div class="action-section">
              <div class="action-content">
                <div class="action-icon">
                  <i class="el-icon-right"></i>
                </div>
                <div class="action-text">
                  <h3>访问用户端</h3>
                  <p>点击下方按钮在新窗口中打开易投简历用户端系统</p>
                </div>
              </div>
              <el-button 
                size="large" 
                type="primary" 
                @click="openPortal"
                class="action-button"
                :loading="loading"
              >
                <i class="el-icon-top-right"></i>
                打开易投简历用户端
              </el-button>
            </div>
            
            <div class="quick-info">
              <div class="info-item">
                <i class="el-icon-link"></i>
                <span>{{ portalURL }}</span>
              </div>
              <div class="info-item">
                <i :class="statusIcon" :style="{ color: statusColor }"></i>
                <span :style="{ color: statusColor }">{{ statusText }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const portalURL = 'http://localhost:3001'

const statusIcon = computed(() => 'el-icon-success')
const statusColor = computed(() => '#10b981')
const statusText = computed(() => '服务运行中')

const openPortal = async () => {
  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 500))
    window.open(portalURL, '_blank')
    ElMessage.success('正在打开易投简历用户端')
  } catch (error) {
    ElMessage.error('打开失败，请检查服务是否启动')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.portal-docs {
  width: 100%;
  height: 100%;
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.docs-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.welcome-card {
  width: 100%;
  height: 100%;
  position: relative;
}

.card-bg-decoration {
  position: absolute;
  top: -50px;
  left: -50px;
  right: -50px;
  bottom: -50px;
  z-index: -1;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.05));
  animation: float 6s ease-in-out infinite;
  
  &.bg-circle-1 {
    width: 200px;
    height: 200px;
    top: 10%;
    left: 10%;
    animation-delay: 0s;
  }
  
  &.bg-circle-2 {
    width: 150px;
    height: 150px;
    top: 60%;
    right: 10%;
    animation-delay: 2s;
  }
  
  &.bg-circle-3 {
    width: 100px;
    height: 100px;
    bottom: 10%;
    left: 20%;
    animation-delay: 4s;
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

.main-card {
  border-radius: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  transition: all 0.3s ease;
  height: 100%;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 15px 35px rgba(102, 126, 234, 0.2);
  }
}

.welcome-content {
  text-align: center;
  padding: 20px 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: rgba(255, 255, 255, 0.98);
}
  
.icon-wrapper {
  margin-bottom: 15px;
  
  .icon-bg {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 60px;
    height: 60px;
    border-radius: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
      animation: shine 3s infinite;
    }
    
    i {
      font-size: 24px;
      color: white;
      z-index: 1;
      position: relative;
    }
  }
}

h2 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a202c;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  margin: 0 0 20px 0;
  color: #4a5568;
  font-size: 14px;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
  line-height: 1.5;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 12px;
  margin: 20px 0;
  
  .feature-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 12px;
    background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
    border-radius: 12px;
    transition: all 0.3s ease;
    border: 1px solid rgba(102, 126, 234, 0.1);
    
    &:hover {
      transform: translateY(-4px) scale(1.02);
      box-shadow: 0 12px 25px rgba(102, 126, 234, 0.2);
      background: linear-gradient(135deg, #ffffff 0%, #f7fafc 100%);
    }
    
    .feature-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 35px;
      height: 35px;
      border-radius: 10px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      margin-bottom: 8px;
      
      i {
        font-size: 14px;
        color: white;
      }
    }
    
    .feature-content {
      .feature-title {
        display: block;
        font-size: 12px;
        font-weight: 600;
        color: #1a202c;
        margin-bottom: 3px;
      }
      
      .feature-desc {
        font-size: 10px;
        color: #718096;
      }
    }
  }
}

.action-section {
  margin: 20px 0;
  padding: 18px;
  background: linear-gradient(135deg, #e6fffa 0%, #b2f5ea 100%);
  border-radius: 12px;
  border: 1px solid rgba(16, 185, 129, 0.2);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.1) 50%, transparent 70%);
    animation: shimmer 2s infinite;
  }
  
  .action-content {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    margin-bottom: 15px;
    
    .action-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 30px;
      height: 30px;
      border-radius: 8px;
      background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
      
      i {
        font-size: 12px;
        color: white;
      }
    }
    
    .action-text {
      text-align: left;
      
      h3 {
        margin: 0 0 4px 0;
        color: #1a202c;
        font-size: 16px;
        font-weight: 600;
      }
      
      p {
        margin: 0;
        color: #4a5568;
        font-size: 12px;
      }
    }
  }
  
  .action-button {
    font-size: 14px;
    padding: 8px 20px;
    border-radius: 8px;
    background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
    border: none;
    font-weight: 600;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 25px rgba(72, 187, 120, 0.4);
    }
    
    i {
      margin-right: 8px;
    }
  }
}

@keyframes shine {
  0% { left: -100%; }
  100% { left: 100%; }
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.quick-info {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 15px;
  
  .info-item {
    display: flex;
    align-items: center;
    gap: 5px;
    color: #718096;
    font-size: 10px;
    
    i {
      color: #667eea;
    }
  }
}

@media (max-width: 768px) {
  .portal-docs {
    padding: 16px;
  }
  
  .welcome-content {
    padding: 25px 20px;
    h2 {
      font-size: 20px;
    }
    
    .subtitle {
      font-size: 14px;
    }
  }
  
  .features {
    grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
    gap: 12px;
  }
  
  .action-section {
    padding: 20px 15px;
    
    .action-content {
      flex-direction: column;
      gap: 12px;
      text-align: center;
    }
  }
  
  .quick-info {
    flex-direction: column;
    gap: 12px;
  }
  
  .bg-circle-1 { width: 80px; height: 80px; }
  .bg-circle-2 { width: 60px; height: 60px; }
  .bg-circle-3 { width: 40px; height: 40px; }
}
</style>


<template>
  <div class="platform-page">
    <div class="content-wrapper">
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
                <el-icon><MagicStick /></el-icon>
              </div>
            </div>

            <h2>阿里云百炼平台</h2>
            <p class="subtitle">阿里云百炼是企业级AI开发平台，提供大模型服务和AI应用开发能力</p>

            <div class="features">
              <div class="feature-item">
                <div class="feature-icon">
                  <el-icon><Cpu /></el-icon>
                </div>
                <div class="feature-content">
                  <span class="feature-title">大模型服务</span>
                  <span class="feature-desc">通义千问等模型</span>
                </div>
              </div>
              <div class="feature-item">
                <div class="feature-icon">
                  <el-icon><Platform /></el-icon>
                </div>
                <div class="feature-content">
                  <span class="feature-title">AI应用开发</span>
                  <span class="feature-desc">快速构建AI应用</span>
                </div>
              </div>
              <div class="feature-item">
                <div class="feature-icon">
                  <el-icon><DataAnalysis /></el-icon>
                </div>
                <div class="feature-content">
                  <span class="feature-title">企业级能力</span>
                  <span class="feature-desc">安全可靠的服务</span>
                </div>
              </div>
              <div class="feature-item">
                <div class="feature-icon">
                  <el-icon><Connection /></el-icon>
                </div>
                <div class="feature-content">
                  <span class="feature-title">API接口</span>
                  <span class="feature-desc">灵活的调用方式</span>
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
                  <p>点击下方按钮在新窗口中访问阿里云百炼平台</p>
                </div>
              </div>
              <el-button
                size="large"
                type="primary"
                @click="openPlatform"
                class="action-button"
                :loading="loading"
              >
                <el-icon><TopRight /></el-icon>
                打开百炼平台
              </el-button>
            </div>

            <div class="quick-info">
              <div class="info-item">
                <el-icon><Link /></el-icon>
                <span>{{ platformURL }}</span>
              </div>
              <div class="info-item">
                <el-icon><InfoFilled /></el-icon>
                <span>阿里云官方平台</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  MagicStick,
  Cpu,
  Platform,
  DataAnalysis,
  Connection,
  Right,
  TopRight,
  Link,
  InfoFilled
} from '@element-plus/icons-vue'

const loading = ref(false)
const platformURL = 'https://bailian.console.aliyun.com/'

const openPlatform = async () => {
  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 500))
    window.open(platformURL, '_blank', 'noopener,noreferrer')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.platform-page {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content-wrapper {
  flex: 1;
  display: flex;
  align-items: stretch;
  justify-content: center;
}

.welcome-card {
  width: 100%;
  height: 100%;
  position: relative;
}

.card-bg-decoration {
  position: absolute;
  inset: -40px;
  z-index: 0;
  pointer-events: none;
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

.main-card {
  position: relative;
  z-index: 1;
  height: 100%;
  border-radius: 16px;
  background: linear-gradient(135deg, #ff6b00 0%, #ff8c00 100%);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  transition: all 0.3s ease;

  :deep(.el-card__body) {
    height: 100%;
    padding: 0;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 15px 35px rgba(255, 107, 0, 0.2);
  }
}

.welcome-content {
  text-align: center;
  padding: 24px;
  min-height: 100%;
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
    width: 72px;
    height: 72px;
    border-radius: 20px;
    background: linear-gradient(135deg, #ff6b00 0%, #ff8c00 100%);
    box-shadow: 0 10px 25px rgba(255, 107, 0, 0.3);
    position: relative;
    overflow: hidden;
    color: #fff;

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

    .el-icon {
      font-size: 30px;
      z-index: 1;
      position: relative;
    }
  }
}

h2 {
  margin: 0 0 8px;
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #ff6b00 0%, #ff8c00 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  margin: 0 0 20px;
  color: #4a5568;
  font-size: 14px;
  max-width: 420px;
  margin-left: auto;
  margin-right: auto;
  line-height: 1.6;
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
    border: 1px solid rgba(255, 107, 0, 0.1);

    &:hover {
      transform: translateY(-4px) scale(1.02);
      box-shadow: 0 12px 25px rgba(255, 107, 0, 0.2);
      background: linear-gradient(135deg, #ffffff 0%, #f7fafc 100%);
    }

    .feature-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 35px;
      height: 35px;
      border-radius: 10px;
      background: linear-gradient(135deg, #ff6b00 0%, #ff8c00 100%);
      margin-bottom: 8px;
      color: #fff;

      .el-icon {
        font-size: 14px;
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
  background: linear-gradient(135deg, #fff4e6 0%, #ffe0b2 100%);
  border-radius: 12px;
  border: 1px solid rgba(255, 107, 0, 0.2);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.1) 50%, transparent 70%);
    animation: shimmer 2s infinite;
  }

  .action-content {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    margin-bottom: 15px;
    position: relative;
    z-index: 1;

    .action-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 30px;
      height: 30px;
      border-radius: 8px;
      background: linear-gradient(135deg, #ff6b00 0%, #ff8c00 100%);
      color: #fff;

      .el-icon {
        font-size: 12px;
      }
    }

    .action-text {
      text-align: left;

      h3 {
        margin: 0 0 4px;
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
    position: relative;
    z-index: 1;
    font-size: 14px;
    padding: 8px 20px;
    border-radius: 8px;
    background: linear-gradient(135deg, #ff6b00 0%, #ff8c00 100%);
    border: none;
    font-weight: 600;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 25px rgba(255, 107, 0, 0.4);
    }
  }
}

.quick-info {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 15px;
  flex-wrap: wrap;

  .info-item {
    display: flex;
    align-items: center;
    gap: 5px;
    color: #718096;
    font-size: 10px;

    .el-icon {
      color: #ff6b00;
    }
  }
}

@keyframes shine {
  0% {
    left: -100%;
  }

  100% {
    left: 100%;
  }
}

@keyframes shimmer {
  0% {
    transform: translateX(-100%);
  }

  100% {
    transform: translateX(100%);
  }
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0) rotate(0deg);
  }

  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

@media (max-width: 768px) {
  .welcome-content {
    padding: 24px 20px;
  }

  .features {
    grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  }

  .action-section {
    padding: 20px 15px;

    .action-content {
      flex-direction: column;
      text-align: center;

      .action-text {
        text-align: center;
      }
    }
  }

  .quick-info {
    flex-direction: column;
    gap: 12px;
  }

  .bg-circle-1 {
    width: 80px;
    height: 80px;
  }

  .bg-circle-2 {
    width: 60px;
    height: 60px;
  }

  .bg-circle-3 {
    width: 40px;
    height: 40px;
  }
}
</style>

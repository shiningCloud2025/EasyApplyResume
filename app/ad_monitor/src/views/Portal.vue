<template>
  <div class="portal-container">
    <!-- 动态背景 -->
    <div class="animated-background">
      <div class="gradient-layer"></div>
      <div class="particles">
        <div v-for="i in 50" :key="i" class="particle" :style="getParticleStyle(i)"></div>
      </div>
      <div class="grid-overlay"></div>
    </div>

    <!-- 浮动装饰元素 -->
    <div class="floating-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
      <div class="shape shape-5"></div>
    </div>

    <!-- 主内容区 -->
    <div class="portal-content">
      <!-- Logo区域 -->
      <div class="logo-section">
        <div class="logo-icon-wrapper">
          <div class="logo-icon">
            <el-icon :size="48"><DataAnalysis /></el-icon>
          </div>
          <div class="logo-glow"></div>
        </div>
        <h1 class="main-title">
          <span class="title-line">易投简历</span>
          <span class="title-highlight">监测与广告端</span>
        </h1>
        <p class="subtitle">EasyApplyResume Monitor & Advertisement Platform</p>
      </div>

      <!-- 功能介绍 -->
      <div class="features-showcase">
        <div class="feature-item" v-for="(feature, index) in features" :key="index" :style="{ animationDelay: `${index * 0.15}s` }">
          <div class="feature-icon">
            <el-icon><component :is="feature.icon" /></el-icon>
          </div>
          <span>{{ feature.text }}</span>
        </div>
      </div>

      <!-- 按钮区域 -->
      <div class="action-buttons">
        <button class="btn-primary" @click="goToLogin">
          <el-icon><User /></el-icon>
          <span>立即登录</span>
          <div class="btn-shine"></div>
        </button>
        <button class="btn-secondary" @click="showApplyTip">
          <el-icon><Plus /></el-icon>
          <span>申请获取账号</span>
        </button>
      </div>

      <!-- 底部信息 -->
      <div class="footer-info">
        <span>© 2025 EasyApplyResume</span>
        <span class="divider">|</span>
        <span>Version 1.0.0</span>
      </div>
    </div>

    <!-- 科技线条装饰 -->
    <svg class="tech-lines" viewBox="0 0 100 100" preserveAspectRatio="none">
      <path class="line line-1" d="M0,50 Q25,30 50,50 T100,50" />
      <path class="line line-2" d="M0,60 Q25,40 50,60 T100,60" />
      <path class="line line-3" d="M0,70 Q25,50 50,70 T100,70" />
    </svg>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  DataAnalysis, User, Plus, Monitor, TrendCharts, 
  Bell, Picture, Setting, Connection 
} from '@element-plus/icons-vue'

const router = useRouter()

const features = ref([
  { icon: 'Monitor', text: '实时监测' },
  { icon: 'TrendCharts', text: '数据分析' },
  { icon: 'Bell', text: '公告管理' },
  { icon: 'Picture', text: '广告投放' },
  { icon: 'Setting', text: '系统配置' },
  { icon: 'Connection', text: '中间件监控' }
])

const getParticleStyle = (i: number) => {
  const size = Math.random() * 4 + 2
  const left = Math.random() * 100
  const animationDuration = Math.random() * 20 + 10
  const delay = Math.random() * 20
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${left}%`,
    animationDuration: `${animationDuration}s`,
    animationDelay: `${delay}s`
  }
}

const goToLogin = () => {
  router.push('/login')
}

const showApplyTip = () => {
  ElMessage.info('账号申请功能即将开放，敬请期待')
}
</script>

<style scoped lang="scss">
.portal-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: #0a0a0f;
}

// 动态背景
.animated-background {
  position: absolute;
  inset: 0;
  z-index: 1;
}

.gradient-layer {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(ellipse at 20% 20%, rgba(16, 185, 129, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 80%, rgba(6, 182, 212, 0.12) 0%, transparent 50%),
    radial-gradient(ellipse at 50% 50%, rgba(139, 92, 246, 0.08) 0%, transparent 60%),
    linear-gradient(180deg, #0a0a0f 0%, #111827 100%);
  animation: gradientShift 15s ease-in-out infinite;
}

@keyframes gradientShift {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.85; }
}

.particles {
  position: absolute;
  inset: 0;
}

.particle {
  position: absolute;
  background: rgba(16, 185, 129, 0.6);
  border-radius: 50%;
  bottom: -20px;
  animation: rise linear infinite;
  box-shadow: 0 0 6px rgba(16, 185, 129, 0.8);
}

@keyframes rise {
  0% {
    transform: translateY(0) scale(1);
    opacity: 0;
  }
  10% { opacity: 1; }
  90% { opacity: 1; }
  100% {
    transform: translateY(-100vh) scale(0.5);
    opacity: 0;
  }
}

.grid-overlay {
  position: absolute;
  inset: 0;
  background-image: 
    linear-gradient(rgba(16, 185, 129, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(16, 185, 129, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridMove 20s linear infinite;
}

@keyframes gridMove {
  0% { transform: perspective(500px) rotateX(60deg) translateY(0); }
  100% { transform: perspective(500px) rotateX(60deg) translateY(50px); }
}

// 浮动形状
.floating-shapes {
  position: absolute;
  inset: 0;
  z-index: 2;
  pointer-events: none;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.2), rgba(6, 182, 212, 0.1));
  backdrop-filter: blur(2px);
  animation: float 8s ease-in-out infinite;
}

.shape-1 { width: 300px; height: 300px; top: -100px; left: -100px; animation-delay: 0s; }
.shape-2 { width: 200px; height: 200px; top: 60%; right: -50px; animation-delay: 2s; }
.shape-3 { width: 150px; height: 150px; bottom: 10%; left: 10%; animation-delay: 4s; }
.shape-4 { width: 100px; height: 100px; top: 30%; right: 20%; animation-delay: 1s; }
.shape-5 { width: 80px; height: 80px; bottom: 30%; right: 30%; animation-delay: 3s; }

@keyframes float {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(10px, -10px) rotate(5deg); }
  50% { transform: translate(0, -20px) rotate(0deg); }
  75% { transform: translate(-10px, -10px) rotate(-5deg); }
}

// 主内容
.portal-content {
  position: relative;
  z-index: 10;
  text-align: center;
  padding: 40px;
}

// Logo区域
.logo-section {
  margin-bottom: 50px;
}

.logo-icon-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 24px;
}

.logo-icon {
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, #10b981 0%, #06b6d4 100%);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  position: relative;
  z-index: 2;
  box-shadow: 0 10px 40px rgba(16, 185, 129, 0.4);
  animation: iconPulse 3s ease-in-out infinite;
}

.logo-glow {
  position: absolute;
  inset: -10px;
  background: linear-gradient(135deg, #10b981, #06b6d4);
  border-radius: 28px;
  filter: blur(20px);
  opacity: 0.5;
  animation: glowPulse 3s ease-in-out infinite;
}

@keyframes iconPulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

@keyframes glowPulse {
  0%, 100% { opacity: 0.5; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.1); }
}

.main-title {
  font-size: 48px;
  font-weight: 800;
  margin: 0 0 16px 0;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.title-line {
  color: white;
  text-shadow: 0 0 30px rgba(255, 255, 255, 0.2);
}

.title-highlight {
  background: linear-gradient(135deg, #10b981, #06b6d4, #8b5cf6);
  background-size: 200% 200%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: gradientText 5s ease infinite;
}

@keyframes gradientText {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.5);
  letter-spacing: 2px;
  margin: 0;
}

// 功能展示
.features-showcase {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 50px;
  flex-wrap: wrap;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 30px;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  backdrop-filter: blur(10px);
  animation: fadeInUp 0.6s ease-out forwards;
  opacity: 0;
  transform: translateY(20px);
}

.feature-icon {
  color: #10b981;
  font-size: 18px;
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 按钮区域
.action-buttons {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 60px;
}

.btn-primary, .btn-secondary {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 36px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.btn-primary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  border: none;
  box-shadow: 0 8px 30px rgba(16, 185, 129, 0.4);

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 12px 40px rgba(16, 185, 129, 0.5);
  }

  .btn-shine {
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
    animation: shine 3s infinite;
  }
}

@keyframes shine {
  0% { left: -100%; }
  20% { left: 100%; }
  100% { left: 100%; }
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.05);
  color: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);

  &:hover {
    background: rgba(255, 255, 255, 0.1);
    border-color: rgba(16, 185, 129, 0.5);
    color: white;
  }
}

// 底部信息
.footer-info {
  color: rgba(255, 255, 255, 0.3);
  font-size: 13px;

  .divider {
    margin: 0 12px;
  }
}

// 科技线条
.tech-lines {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100px;
  z-index: 3;
  opacity: 0.3;
}

.line {
  fill: none;
  stroke: #10b981;
  stroke-width: 0.2;
  animation: lineFlow 3s ease-in-out infinite;
}

.line-1 { animation-delay: 0s; }
.line-2 { animation-delay: 0.5s; stroke: #06b6d4; }
.line-3 { animation-delay: 1s; stroke: #8b5cf6; }

@keyframes lineFlow {
  0%, 100% { stroke-dasharray: 10 5; stroke-dashoffset: 0; }
  50% { stroke-dasharray: 5 10; stroke-dashoffset: 15; }
}

// 响应式
@media (max-width: 768px) {
  .main-title { font-size: 32px; }
  .features-showcase { gap: 12px; }
  .feature-item { padding: 8px 14px; font-size: 12px; }
  .action-buttons { flex-direction: column; gap: 16px; }
  .btn-primary, .btn-secondary { width: 100%; justify-content: center; }
}
</style>

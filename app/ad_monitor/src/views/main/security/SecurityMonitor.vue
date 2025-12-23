<template>
  <div class="security-page">
    <div class="page-header">
      <h1>网站安全管理</h1>
      <p>系统监控与安全管理组件快速访问</p>
    </div>

    <div class="security-content">
      <div class="cards-grid">
        <!-- Spring Boot Admin -->
        <el-card class="link-card" @click="openLink('sba')" shadow="hover">
          <div class="card-inner">
            <div class="card-icon sba">
              <el-icon :size="32"><Monitor /></el-icon>
            </div>
            <div class="card-info">
              <h3>Spring Boot Admin</h3>
              <p>应用程序监控与管理平台，查看应用健康状态、指标、日志等信息</p>
              <div class="card-url">
                <el-icon><Link /></el-icon>
                <span>{{ links.sba }}</span>
              </div>
            </div>
            <div class="card-features">
              <span class="feature-tag">健康检查</span>
              <span class="feature-tag">指标监控</span>
              <span class="feature-tag">日志查看</span>
            </div>
            <div class="card-action">
              <el-button type="primary" size="small">
                打开控制台
                <el-icon class="el-icon--right"><Right /></el-icon>
              </el-button>
            </div>
          </div>
        </el-card>

        <!-- Prometheus -->
        <el-card class="link-card" @click="openLink('prometheus')" shadow="hover">
          <div class="card-inner">
            <div class="card-icon prometheus">
              <el-icon :size="32"><TrendCharts /></el-icon>
            </div>
            <div class="card-info">
              <h3>Prometheus</h3>
              <p>时序数据库与监控告警系统，收集和存储应用指标数据</p>
              <div class="card-url">
                <el-icon><Link /></el-icon>
                <span>{{ links.prometheus }}</span>
              </div>
            </div>
            <div class="card-features">
              <span class="feature-tag">指标采集</span>
              <span class="feature-tag">告警规则</span>
              <span class="feature-tag">PromQL查询</span>
            </div>
            <div class="card-action">
              <el-button type="primary" size="small">
                打开控制台
                <el-icon class="el-icon--right"><Right /></el-icon>
              </el-button>
            </div>
          </div>
        </el-card>

        <!-- Grafana -->
        <el-card class="link-card" @click="openLink('grafana')" shadow="hover">
          <div class="card-inner">
            <div class="card-icon grafana">
              <el-icon :size="32"><DataLine /></el-icon>
            </div>
            <div class="card-info">
              <h3>Grafana</h3>
              <p>数据可视化与仪表板平台，提供丰富的图表展示和告警功能</p>
              <div class="card-url">
                <el-icon><Link /></el-icon>
                <span>{{ links.grafana }}</span>
              </div>
            </div>
            <div class="card-features">
              <span class="feature-tag">可视化面板</span>
              <span class="feature-tag">仪表盘</span>
              <span class="feature-tag">告警通知</span>
            </div>
            <div class="card-action">
              <el-button type="primary" size="small">
                打开控制台
                <el-icon class="el-icon--right"><Right /></el-icon>
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Monitor, TrendCharts, DataLine, Link, Right } from '@element-plus/icons-vue'

const links = reactive({
  sba: 'http://localhost:8088',
  prometheus: 'http://localhost:9090',
  grafana: 'http://localhost:3000'
})

const openLink = (type: 'sba' | 'prometheus' | 'grafana') => {
  window.open(links[type], '_blank')
  const names: Record<string, string> = {
    sba: 'Spring Boot Admin',
    prometheus: 'Prometheus',
    grafana: 'Grafana'
  }
  ElMessage.success(`正在打开 ${names[type]}`)
}
</script>

<style scoped lang="scss">
.security-page {
  padding: 24px;
}

.page-header {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;

  h1 {
    font-size: 22px;
    font-weight: 700;
    color: #1f2937;
    margin: 0 0 8px 0;
  }

  p {
    font-size: 14px;
    color: #6b7280;
    margin: 0;
  }
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  gap: 20px;
}

.link-card {
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  overflow: hidden;

  &:hover {
    transform: translateY(-4px);
    border-color: #10b981;
    box-shadow: 0 12px 30px rgba(16, 185, 129, 0.15);
  }
}

.card-inner {
  padding: 8px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.card-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;

  &.sba { background: linear-gradient(135deg, #10b981, #059669); }
  &.prometheus { background: linear-gradient(135deg, #f59e0b, #d97706); }
  &.grafana { background: linear-gradient(135deg, #8b5cf6, #6d28d9); }
}

.card-info {
  h3 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 8px 0;
  }

  p {
    font-size: 13px;
    color: #6b7280;
    line-height: 1.5;
    margin: 0;
  }
}

.card-url {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 12px;
  padding: 10px 14px;
  background: #f3f4f6;
  border-radius: 8px;
  font-size: 13px;
  color: #4b5563;

  .el-icon {
    color: #10b981;
  }
}

.card-features {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.feature-tag {
  padding: 4px 12px;
  background: linear-gradient(135deg, #ecfdf5, #d1fae5);
  border-radius: 6px;
  font-size: 12px;
  color: #059669;
}

.card-action {
  padding-top: 12px;
  border-top: 1px solid #e5e7eb;
}

@media (max-width: 768px) {
  .cards-grid {
    grid-template-columns: 1fr;
  }
}
</style>

<template>
  <div class="system-links">
    <div class="page-header">
      <h1 class="page-title">系统链接</h1>
      <p class="page-description">易投简历相关系统快速访问</p>
    </div>

    <div class="links-grid">
      <el-card 
        v-for="link in systemLinks" 
        :key="link.id"
        class="link-card"
        @click="openSystem(link)"
      >
        <div class="link-icon" :style="{ background: link.color }">
          <i :class="link.icon"></i>
        </div>
        <div class="link-info">
          <h3>{{ link.name }}</h3>
          <p>{{ link.description }}</p>
          <div class="link-status" :class="link.status">
            {{ link.status === 'online' ? '在线' : link.status === 'offline' ? '离线' : '维护中' }}
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const systemLinks = ref([
  {
    id: 1,
    name: '易投简历用户端',
    description: '面向求职用户的简历制作平台',
    icon: 'el-icon-user-solid',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    status: 'online',
    url: 'http://localhost:3000'
  },
  {
    id: 2,
    name: '易投简历观测与广告端',
    description: '系统监控和广告管理平台',
    icon: 'el-icon-view',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    status: 'online',
    url: 'http://localhost:3100'
  },
  {
    id: 3,
    name: '账号申请管理',
    description: '管理员账号申请审核系统',
    icon: 'el-icon-document-checked',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    status: 'offline',
    url: ''
  },
  {
    id: 4,
    name: '账号申请记录',
    description: '查看所有账号申请的历史记录',
    icon: 'el-icon-notebook-2',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    status: 'maintenance',
    url: ''
  }
])

const openSystem = (link: any) => {
  if (link.status !== 'online') {
    ElMessage.warning(`系统${link.status === 'offline' ? '离线' : '维护中'}，暂无法访问`)
    return
  }
  
  if (link.url) {
    window.open(link.url, '_blank')
  } else {
    ElMessage.info('该功能正在开发中')
  }
}
</script>

<style scoped lang="scss">
.system-links {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 32px;
  
  h1 {
    font-size: 28px;
    font-weight: 700;
    color: #1f2937;
    margin-bottom: 8px;
  }
  
  p {
    color: #6b7280;
    margin: 0;
  }
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.link-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 25px rgba(0, 0, 0, 0.1);
    border-color: #3b82f6;
  }
  
  .el-card__body {
    padding: 24px;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 16px;
  }
}

.link-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 32px;
}

.link-info {
  flex: 1;
  width: 100%;
  
  h3 {
    font-size: 20px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 8px 0;
  }
  
  p {
    font-size: 14px;
    color: #6b7280;
    margin: 0 0 12px 0;
    line-height: 1.5;
  }
}

.link-status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  
  &.online {
    background: #d1fae5;
    color: #065f46;
  }
  
  &.offline {
    background: #fee2e2;
    color: #991b1b;
  }
  
  &.maintenance {
    background: #fef3c7;
    color: #92400e;
  }
}
</style>
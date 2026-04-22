<template>
  <div class="management-page">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">{{ currentModule.title }}</h2>
        <p class="page-description">{{ currentModule.description }}</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="goToCurrentRoute">
          <el-icon><RefreshRight /></el-icon>
          重新进入
        </el-button>
      </div>
    </div>

    <div class="content-card">
      <div class="module-intro">
        <el-tag type="success" effect="light">当前模块</el-tag>
        <h3>{{ currentModule.panelTitle }}</h3>
        <p>{{ currentModule.panelDescription }}</p>
      </div>

      <div class="module-list">
        <div
          v-for="item in modules"
          :key="item.key"
          class="module-item"
          :class="{ active: item.key === currentKey }"
          @click="router.push(item.path)"
        >
          <div class="module-item-header">
            <span class="module-name">{{ item.title }}</span>
            <el-tag size="small" :type="item.key === currentKey ? 'success' : 'info'">
              {{ item.key === currentKey ? '当前查看' : '点击进入' }}
            </el-tag>
          </div>
          <p class="module-text">{{ item.description }}</p>
        </div>
      </div>

      <el-alert
        type="success"
        :closable="false"
        show-icon
        title="评分模型管理入口已创建"
        description="当前已接入训练数据管理、训练代码管理和模型版本管理三个前端入口。后续如果你给我具体字段和交互要求，我可以继续只改前端，把表格、上传表单、详情抽屉和筛选区逐步补齐。"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { RefreshRight } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const modules = [
  {
    key: 'training-data',
    path: '/admin/score-model/training-data',
    title: '训练数据管理',
    description: '维护评分模型训练数据，后续可扩展列表查询、详情查看、导出和数据录入能力。'
  },
  {
    key: 'train-code',
    path: '/admin/score-model/train-code',
    title: '训练代码管理',
    description: '维护评分模型训练代码资源，后续可扩展上传、下载、详情和版本关联能力。'
  },
  {
    key: 'version',
    path: '/admin/score-model/version',
    title: '模型版本管理',
    description: '维护评分模型版本信息，后续可扩展版本发布、上传模型文件与状态展示能力。'
  }
] as const

const currentKey = computed(() => route.path.split('/').pop() || 'training-data')

const currentModule = computed(() => {
  const current = modules.find(item => item.key === currentKey.value) || modules[0]
  return {
    ...current,
    panelTitle: `${current.title}入口页`,
    panelDescription: `这里已经接入“${current.title}”的管理端菜单与路由入口，后续可以继续在当前页面扩展列表、查询、上传、编辑和详情能力。`
  }
})

const goToCurrentRoute = () => {
  router.push(route.path)
}
</script>

<style scoped lang="scss">
.management-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.page-title {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 700;
  color: #111827;
}

.page-description {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
  line-height: 1.6;
}

.content-card {
  padding: 24px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(15, 23, 42, 0.08);
}

.module-intro {
  margin-bottom: 20px;

  h3 {
    margin: 14px 0 8px;
    font-size: 20px;
    color: #111827;
  }

  p {
    margin: 0;
    font-size: 14px;
    line-height: 1.7;
    color: #4b5563;
  }
}

.module-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.module-item {
  padding: 18px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #f9fafb;

  &:hover {
    border-color: #67c23a;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(103, 194, 58, 0.12);
  }

  &.active {
    border-color: #67c23a;
    background: #f0f9eb;
  }
}

.module-item-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
}

.module-name {
  font-size: 16px;
  font-weight: 600;
  color: #111827;
}

.module-text {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
  color: #6b7280;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
  }

  .header-actions {
    width: 100%;

    :deep(.el-button) {
      width: 100%;
    }
  }

  .content-card {
    padding: 16px;
  }
}
</style>

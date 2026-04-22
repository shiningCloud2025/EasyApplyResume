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
        <el-tag type="primary" effect="light">当前模块</el-tag>
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
            <el-tag size="small" :type="item.key === currentKey ? 'primary' : 'info'">
              {{ item.key === currentKey ? '当前查看' : '点击进入' }}
            </el-tag>
          </div>
          <p class="module-text">{{ item.description }}</p>
        </div>
      </div>

      <el-alert
        type="info"
        :closable="false"
        show-icon
        title="前端入口已创建"
        description="当前已补齐管理端菜单、路由和页面入口。后续如果你确认页面字段和交互，我可以继续只改前端，把列表、表单、分页、上传等界面逐步补全。"
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
    key: 'first-category',
    path: '/admin/written-test/first-category',
    title: '题库大类管理',
    description: '维护笔试专项下的题库大类，作为后续题库小类与题库题目的业务入口。'
  },
  {
    key: 'second-category',
    path: '/admin/written-test/second-category',
    title: '题库小类管理',
    description: '维护题库小类，建立题目在业务分类中的归属关系。'
  },
  {
    key: 'question-bank',
    path: '/admin/written-test/question-bank',
    title: '题库题目管理',
    description: '维护笔试专项题库题目，用于后续列表、查询、编辑与展示扩展。'
  }
] as const

const currentKey = computed(() => route.path.split('/').pop() || 'first-category')

const currentModule = computed(() => {
  const current = modules.find(item => item.key === currentKey.value) || modules[0]
  return {
    ...current,
    panelTitle: `${current.title}入口页`,
    panelDescription: `这里已经接入“${current.title}”的管理端菜单与路由入口，后续可以继续在当前页面扩展列表、筛选、弹窗表单和详情能力。`
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
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
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
    border-color: #409eff;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(64, 158, 255, 0.12);
  }

  &.active {
    border-color: #409eff;
    background: #ecf5ff;
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

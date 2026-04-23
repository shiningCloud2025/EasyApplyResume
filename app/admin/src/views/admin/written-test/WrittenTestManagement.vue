<template>
  <QuestionFirstCategoryManager v-if="currentKey === 'first-category'" />
  <QuestionSecondCategoryManager v-else-if="currentKey === 'second-category'" />
  <div v-else class="written-test-placeholder">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">题库题目管理</h2>
        <p class="page-description">维护笔试专项题库题目，用于后续列表、查询、编辑与展示扩展。</p>
      </div>
    </div>

    <el-card class="placeholder-card">
      <el-alert
        type="info"
        :closable="false"
        show-icon
        title="题库题目管理待继续实现"
        description="当前先完成题库大类和题库小类管理，题库题目管理页后续再补分页、详情、表单和联动分类选择。"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import QuestionFirstCategoryManager from './components/QuestionFirstCategoryManager.vue'
import QuestionSecondCategoryManager from './components/QuestionSecondCategoryManager.vue'

const route = useRoute()

const currentKey = computed(() => route.path.split('/').pop() || 'first-category')
</script>

<style scoped lang="scss">
.written-test-placeholder {
  .page-header {
    margin-bottom: 24px;
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

  .placeholder-card {
    :deep(.el-card__body) {
      padding: 24px;
    }
  }
}
</style>

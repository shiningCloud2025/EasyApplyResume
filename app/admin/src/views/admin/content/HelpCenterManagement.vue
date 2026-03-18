<template>
  <div class="help-center-management">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">{{ currentModule.label }}</h2>
        <p class="page-description">集中维护帮助中心内容，支持 Markdown 编辑与分页管理。</p>
      </div>
    </div>

    <div class="content-wrapper">
      <ContentListManager
        v-if="currentModule.type === 'list'"
        :key="currentKey"
        v-bind="currentModule.config"
      />
      <SingletonContentManager
        v-else
        :key="currentKey"
        v-bind="currentModule.config"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import ContentListManager from '@/components/ContentListManager.vue'
import SingletonContentManager from '@/components/SingletonContentManager.vue'
import { customerServiceApi, faqApi, userGuideApi } from '@/api/admin'

const route = useRoute()

const moduleMap = {
  faq: {
    label: 'FAQ管理',
    type: 'list',
    config: {
      moduleLabel: 'FAQ',
      idField: 'faqId',
      titleField: 'faqTitle',
      contentField: 'faqContent',
      searchField: 'faqTitle',
      createdField: 'faqCreatedTime',
      updatedField: 'faqUpdatedTime',
      getPage: faqApi.getPage,
      getInfo: faqApi.getInfo,
      add: faqApi.add,
      update: faqApi.update,
      remove: faqApi.remove
    }
  },
  'customer-service': {
    label: '客服管理',
    type: 'singleton',
    config: {
      moduleLabel: '客服',
      idField: 'customerServiceId',
      titleField: 'customerServiceTitle',
      contentField: 'customerServiceContent',
      updatedField: 'customerServiceUpdatedTime',
      getInfo: customerServiceApi.getInfo,
      add: customerServiceApi.add,
      update: customerServiceApi.update
    }
  },
  'user-guide': {
    label: '使用指南管理',
    type: 'list',
    config: {
      moduleLabel: '使用指南',
      idField: 'userGuideId',
      titleField: 'userGuideTitle',
      contentField: 'userGuideContent',
      searchField: 'userGuideTitle',
      createdField: 'userGuideCreatedTime',
      updatedField: 'userGuideUpdatedTime',
      getPage: userGuideApi.getPage,
      getInfo: userGuideApi.getInfo,
      add: userGuideApi.add,
      update: userGuideApi.update,
      remove: userGuideApi.remove
    }
  }
} as const

const currentKey = computed(() => route.path.split('/').pop() || 'faq')

const currentModule = computed(() => {
  return moduleMap[currentKey.value as keyof typeof moduleMap] || moduleMap.faq
})
</script>

<style scoped lang="scss">
.help-center-management {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
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
  }

  .content-wrapper {
    padding: 20px 24px 24px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(15, 23, 42, 0.08);
  }
}

@media (max-width: 768px) {
  .help-center-management {
    .content-wrapper {
      padding: 16px;
    }
  }
}
</style>

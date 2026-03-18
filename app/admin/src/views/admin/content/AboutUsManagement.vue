<template>
  <div class="about-us-management">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">{{ currentModule.label }}</h2>
        <p class="page-description">统一维护关于我们模块内容，支持富文本编辑与实时保存。</p>
      </div>
    </div>

    <div class="content-wrapper">
      <SingletonContentManager :key="currentKey" v-bind="currentModule.config" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import SingletonContentManager from '@/components/SingletonContentManager.vue'
import {
  projectIntroduceApi,
  teamIntroduceApi,
  developHistoryApi,
  joinUsApi,
  partnerIntroduceApi,
  mediaReportApi
} from '@/api/admin'

const route = useRoute()

const moduleMap = {
  'project-introduce': {
    label: '项目介绍',
    config: {
      moduleLabel: '项目介绍',
      idField: 'projectIntroduceId',
      titleField: 'projectIntroduceTitle',
      contentField: 'projectIntroduceContent',
      updatedField: 'projectIntroduceUpdatedTime',
      getInfo: projectIntroduceApi.getInfo,
      add: projectIntroduceApi.add,
      update: projectIntroduceApi.update
    }
  },
  'team-introduce': {
    label: '团队介绍',
    config: {
      moduleLabel: '团队介绍',
      idField: 'teamIntroduceId',
      titleField: 'teamIntroduceTitle',
      contentField: 'teamIntroduceContent',
      updatedField: 'teamIntroduceUpdatedTime',
      getInfo: teamIntroduceApi.getInfo,
      add: teamIntroduceApi.add,
      update: teamIntroduceApi.update
    }
  },
  'develop-history': {
    label: '发展历程',
    config: {
      moduleLabel: '发展历程',
      idField: 'developHistoryId',
      titleField: 'developHistoryTitle',
      contentField: 'developHistoryContent',
      updatedField: 'developHistoryUpdatedTime',
      getInfo: developHistoryApi.getInfo,
      add: developHistoryApi.add,
      update: developHistoryApi.update
    }
  },
  'join-us': {
    label: '加入我们',
    config: {
      moduleLabel: '加入我们',
      idField: 'joinUsId',
      titleField: 'joinUsTitle',
      contentField: 'joinUsContent',
      updatedField: 'joinUsUpdatedTime',
      getInfo: joinUsApi.getInfo,
      add: joinUsApi.add,
      update: joinUsApi.update
    }
  },
  'partner-introduce': {
    label: '合作伙伴',
    config: {
      moduleLabel: '合作伙伴',
      idField: 'partnerIntroduceId',
      titleField: 'partnerIntroduceTitle',
      contentField: 'partnerIntroduceContent',
      updatedField: 'partnerIntroduceUpdatedTime',
      getInfo: partnerIntroduceApi.getInfo,
      add: partnerIntroduceApi.add,
      update: partnerIntroduceApi.update
    }
  },
  'media-report': {
    label: '媒体报道',
    config: {
      moduleLabel: '媒体报道',
      idField: 'mediaReportId',
      titleField: 'mediaReportTitle',
      contentField: 'mediaReportContent',
      updatedField: 'mediaReportUpdatedTime',
      getInfo: mediaReportApi.getInfo,
      add: mediaReportApi.add,
      update: mediaReportApi.update
    }
  }
} as const

const currentKey = computed(() => route.path.split('/').pop() || 'project-introduce')

const currentModule = computed(() => {
  return moduleMap[currentKey.value as keyof typeof moduleMap] || moduleMap['project-introduce']
})
</script>

<style scoped lang="scss">
.about-us-management {
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
  .about-us-management {
    .content-wrapper {
      padding: 16px;
    }
  }
}
</style>

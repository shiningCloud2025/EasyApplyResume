<template>
  <div class="portal-page">
    <div class="toolbar">
      <div class="toolbar-left">
        <h2>{{ currentConfig.title }}</h2>
        <p>{{ currentConfig.description }}</p>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="openPortal">
          <el-icon><TopRight /></el-icon>
          {{ currentConfig.buttonText }}
        </el-button>
      </div>
    </div>

    <div class="iframe-wrapper">
      <iframe
        :src="currentConfig.url"
        :title="currentConfig.title"
        class="portal-iframe"
        frameborder="0"
        referrerpolicy="strict-origin-when-cross-origin"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { TopRight } from '@element-plus/icons-vue'

type PortalConfig = {
  title: string
  description: string
  url: string
  buttonText: string
}

const route = useRoute()

const defaultConfig: PortalConfig = {
  title: '系统入口',
  description: '请选择左侧菜单访问对应的平台入口。',
  url: 'about:blank',
  buttonText: '新窗口打开'
}

const portalConfigMap: Record<string, PortalConfig> = {
  SystemUserPortal: {
    title: '易投简历用户端',
    description: '易投简历用户端面向求职用户，提供简历制作、职位搜索、AI优化与求职服务等一体化能力。',
    url: 'http://117.50.184.138:37222',
    buttonText: '新窗口打开'
  },
  SystemObservationPortal: {
    title: '易投简历监测与广告端',
    description: '监测与广告端用于查看系统运行状态、分析业务数据并处理广告运营相关工作。',
    url: 'http://117.50.184.138:37223',
    buttonText: '新窗口打开'
  },
  ExternalBailianPlatform: {
    title: '阿里云百炼平台',
    description: '阿里云百炼是企业级AI开发平台，提供大模型服务和AI应用开发能力',
    url: 'https://bailian.console.aliyun.com/',
    buttonText: '打开百炼平台'
  },
  ExternalSmsPlatform: {
    title: '阿里云短信平台',
    description: '阿里云短信平台提供验证码、通知和营销短信等能力，支持高并发发送、模板管理与全球触达。',
    url: 'https://www.aliyun.com/benefit',
    buttonText: '新窗口打开'
  },
  ExternalSearchAPIPlatform: {
    title: 'SearchAPI平台',
    description: '实时Google搜索API，提供便捷的SERP数据抓取服务',
    url: 'https://www.searchapi.io/',
    buttonText: '打开SearchAPI平台'
  },
  ExternalAmapPlatform: {
    title: '高德开放平台',
    description: '高德开放平台提供地图、定位、路径规划、地理编码等能力，适合位置服务和地图可视化场景。',
    url: 'https://lbs.amap.com/',
    buttonText: '新窗口打开'
  },
  YApiEmbed: {
    title: 'YApi测试平台',
    description: 'YApi 是一个可视化、可本地部署的 API 管理与测试平台，支持接口定义、调试、Mock 与团队协作。',
    url: 'http://120.48.177.183:3000/',
    buttonText: '新窗口打开'
  },
  ExternalAPIDocs: {
    title: 'API对外文档中心',
    description: '面向外部开发者的 API 接口文档，提供标准化的接口规范与联调入口。',
    url: 'http://117.50.184.138:37221/api/swagger-ui/index.html',
    buttonText: '新窗口打开'
  },
  InternalAPIDocs: {
    title: 'API对内文档中心',
    description: '内部系统 API 接口文档中心，包含管理端与业务侧接口说明。',
    url: 'http://117.50.184.138:37221/api/doc.html#/home',
    buttonText: '新窗口打开'
  }
}

const currentConfig = computed(() => {
  return portalConfigMap[String(route.name)] || defaultConfig
})

const openPortal = () => {
  window.open(currentConfig.value.url, '_blank', 'noopener,noreferrer')
}
</script>

<style scoped lang="scss">
.portal-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 14px 18px;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(15, 23, 42, 0.05);
}

.toolbar-left {
  h2 {
    margin: 0 0 4px;
    font-size: 20px;
    color: #1f2937;
  }

  p {
    margin: 0;
    color: #6b7280;
    font-size: 13px;
    line-height: 1.5;
  }
}

.iframe-wrapper {
  flex: 1;
  min-height: 0;
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(15, 23, 42, 0.06);
  border: 1px solid #e5e7eb;
}

.portal-iframe {
  width: 100%;
  height: 100%;
  min-height: 780px;
  border: none;
  background: #ffffff;
}

@media (max-width: 768px) {
  .toolbar {
    flex-direction: column;
    align-items: flex-start;
    padding: 14px;
  }

  .toolbar-right {
    width: 100%;

    :deep(.el-button) {
      width: 100%;
    }
  }

  .portal-iframe {
    min-height: 600px;
  }
}
</style>

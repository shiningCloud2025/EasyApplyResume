<template>
  <div class="platform-page">
    <div class="toolbar">
      <div class="toolbar-left">
        <h2>{{ title }}</h2>
        <p>{{ description }}</p>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="openPlatform">
          <i class="el-icon-top-right"></i>
          新窗口打开
        </el-button>
      </div>
    </div>

    <div v-if="mode === 'embed'" class="iframe-wrapper">
      <div v-if="loading" class="loading-mask">
        <el-icon class="loading-icon is-loading"><Loading /></el-icon>
        <span>正在尝试嵌入平台，请稍候...</span>
        <small>如果平台多次加载失败，将自动切换为打开模式</small>
      </div>

      <iframe
        :src="iframeSrc"
        class="platform-iframe"
        frameborder="0"
        referrerpolicy="strict-origin-when-cross-origin"
        @load="handleIframeLoad"
      />
    </div>

    <div v-else class="fallback-card">
      <el-result icon="warning" :title="`${title} 暂不支持嵌入访问`" sub-title="已为你自动切换为打开模式，你可以点击下方按钮在新窗口继续访问。">
        <template #extra>
          <el-button type="primary" size="large" @click="openPlatform">立即打开平台</el-button>
          <el-button @click="retryEmbed">再次尝试嵌入</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, ref, watch } from 'vue'
import { Loading } from '@element-plus/icons-vue'

interface Props {
  title: string
  description: string
  platformUrl: string
  retryCount?: number
  retryInterval?: number
}

const props = withDefaults(defineProps<Props>(), {
  retryCount: 4,
  retryInterval: 1800
})

const mode = ref<'embed' | 'fallback'>('embed')
const loading = ref(true)
const attempt = ref(0)
const iframeKey = ref(0)
const iframeLoaded = ref(false)
let fallbackTimer: number | null = null

const iframeSrc = computed(() => {
  return `${props.platformUrl}${props.platformUrl.includes('?') ? '&' : '?'}_embed_try=${iframeKey.value}`
})

const clearFallbackTimer = () => {
  if (fallbackTimer !== null) {
    window.clearTimeout(fallbackTimer)
    fallbackTimer = null
  }
}

const scheduleFallbackCheck = () => {
  clearFallbackTimer()

  fallbackTimer = window.setTimeout(() => {
    if (iframeLoaded.value) {
      loading.value = false
      return
    }

    if (attempt.value < props.retryCount) {
      attempt.value += 1
      iframeKey.value += 1
      scheduleFallbackCheck()
      return
    }

    loading.value = false
    mode.value = 'fallback'
  }, props.retryInterval)
}

const handleIframeLoad = () => {
  iframeLoaded.value = true
  loading.value = false
  clearFallbackTimer()
}

const openPlatform = () => {
  window.open(props.platformUrl, '_blank')
}

const startEmbedAttempt = () => {
  clearFallbackTimer()
  mode.value = 'embed'
  loading.value = true
  attempt.value = 1
  iframeLoaded.value = false
  iframeKey.value += 1
  scheduleFallbackCheck()
}

const retryEmbed = () => {
  startEmbedAttempt()
}

watch(
  () => props.platformUrl,
  () => {
    startEmbedAttempt()
  },
  { immediate: true }
)

onBeforeUnmount(() => {
  clearFallbackTimer()
})
</script>

<style scoped lang="scss">
.platform-page {
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

.iframe-wrapper,
.fallback-card {
  flex: 1;
  min-height: 0;
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(15, 23, 42, 0.06);
  border: 1px solid #e5e7eb;
}

.iframe-wrapper {
  position: relative;
}

.loading-mask {
  position: absolute;
  inset: 0;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.96);
  color: #4b5563;

  .loading-icon {
    font-size: 28px;
    color: #409eff;
  }

  span {
    font-size: 15px;
    font-weight: 500;
  }

  small {
    color: #9ca3af;
    font-size: 12px;
  }
}

.platform-iframe {
  width: 100%;
  height: 100%;
  min-height: 780px;
  border: none;
  background: #ffffff;
}

.fallback-card {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
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

  .platform-iframe {
    min-height: 600px;
  }
}
</style>

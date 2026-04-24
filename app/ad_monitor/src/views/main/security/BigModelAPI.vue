<template>
  <div class="platform-page">
    <div class="toolbar">
      <div class="toolbar-left">
        <h2>智谱开放平台</h2>
        <p>智谱开放平台提供 GLM 系列模型、智能体与大模型应用能力，适合通用问答、内容生成和 AI 能力接入场景。</p>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="openPlatform">
          <i class="el-icon-top-right"></i>
          新窗口打开
        </el-button>
      </div>
    </div>

    <div class="iframe-wrapper">
      <iframe
        :src="platformURL"
        class="platform-iframe"
        frameborder="0"
        referrerpolicy="strict-origin-when-cross-origin"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
const platformURL = 'https://open.bigmodel.cn/'

const openPlatform = () => {
  window.open(platformURL, '_blank')
}
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

.iframe-wrapper {
  flex: 1;
  min-height: 0;
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(15, 23, 42, 0.06);
  border: 1px solid #e5e7eb;
}

.platform-iframe {
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

  .platform-iframe {
    min-height: 600px;
  }
}
</style>

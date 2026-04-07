<template>
  <div class="portal-page">
    <div class="toolbar">
      <div class="toolbar-left">
        <h2>易投简历监测与广告端</h2>
        <p>监测与广告端用于查看系统运行状态、分析业务数据并处理广告运营相关工作。</p>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="openPortal">
          <i class="el-icon-top-right"></i>
          新窗口打开
        </el-button>
      </div>
    </div>

    <div class="iframe-wrapper">
      <iframe
        :src="portalURL"
        class="portal-iframe"
        frameborder="0"
        referrerpolicy="strict-origin-when-cross-origin"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
const portalURL = 'http://117.50.184.138:37223'

const openPortal = () => {
  window.open(portalURL, '_blank')
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

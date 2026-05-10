<template>
  <div class="portal-page">
    <div class="toolbar">
      <div class="toolbar-left">
        <h2>易投简历用户端</h2>
        <p>易投简历用户端面向求职用户，提供简历制作、职位搜索、AI优化与求职服务等一体化能力。</p>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="openPortal" :disabled="!canOpenPortal">
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
import { computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore, internalSystemPermissions } from '@/store/auth'

const authStore = useAuthStore()
const canOpenPortal = computed(() => authStore.canAccessRoute(internalSystemPermissions.userPortal))
const portalURL = 'http://117.50.184.138:37222'

const openPortal = () => {
  if (!canOpenPortal.value) {
    ElMessage.warning('暂无易投简历用户端权限')
    return
  }
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

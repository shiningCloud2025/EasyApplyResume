<template>
  <router-view />
</template>

<script setup lang="ts">
// App 根组件
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Element Plus 主题色覆盖 - 青绿色 */
:root {
  --el-color-primary: #10b981;
  --el-color-primary-light-3: #34d399;
  --el-color-primary-light-5: #6ee7b7;
  --el-color-primary-light-7: #a7f3d0;
  --el-color-primary-light-8: #d1fae5;
  --el-color-primary-light-9: #ecfdf5;
  --el-color-primary-dark-2: #059669;
}

/* 按钮悬停效果 */
.el-button--primary:hover {
  background-color: #059669;
  border-color: #059669;
}

@media (max-width: 768px) {
  .el-dialog {
    width: 94% !important;
    max-width: 94vw;
  }

  .el-dialog__body {
    max-height: 70vh;
    overflow-y: auto;
  }

  .el-pagination {
    justify-content: center;
    flex-wrap: wrap;
    gap: 8px;
  }

  .el-table {
    font-size: 14px;
  }

  .page-header {
    flex-direction: column !important;
    align-items: stretch !important;
    gap: 12px;
  }
}
</style>

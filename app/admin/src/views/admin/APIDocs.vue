<template>
  <div class="api-docs">
    <div class="docs-header">
      <h1>API文档中心</h1>
      <div class="header-actions">
        <el-button @click="exportDocs">
          <i class="el-icon-download"></i>
          导出文档
        </el-button>
      </div>
    </div>

    <div class="docs-content">
      <div class="docs-sidebar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索API..."
          prefix-icon="el-icon-search"
          class="search-input"
        />
        <div class="api-list">
          <div 
            v-for="api in filteredApis" 
            :key="api.id"
            class="api-item"
            :class="{ active: selectedApi?.id === api.id }"
            @click="selectApi(api)"
          >
            <div class="api-method" :class="api.method.toLowerCase()">
              {{ api.method }}
            </div>
            <div class="api-info">
              <div class="api-path">{{ api.path }}</div>
              <div class="api-name">{{ api.name }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="docs-main">
        <div v-if="selectedApi" class="api-detail">
          <div class="detail-header">
            <div class="method-tag" :class="selectedApi.method.toLowerCase()">
              {{ selectedApi.method }}
            </div>
            <h2>{{ selectedApi.path }}</h2>
            <p>{{ selectedApi.description }}</p>
          </div>

          <el-card class="detail-card">
            <template #header>请求参数</template>
            <el-table :data="selectedApi.parameters" border>
              <el-table-column prop="name" label="参数名" />
              <el-table-column prop="type" label="类型" />
              <el-table-column prop="required" label="必填">
                <template #default="{ row }">
                  <el-tag :type="row.required ? 'danger' : 'info'">
                    {{ row.required ? '是' : '否' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="说明" />
            </el-table>
          </el-card>

          <el-card class="detail-card">
            <template #header>响应示例</template>
            <pre>{{ JSON.stringify(selectedApi.responseExample, null, 2) }}</pre>
          </el-card>
        </div>

        <div v-else class="welcome-message">
          <i class="el-icon-document"></i>
          <p>请从左侧选择API查看详情</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

const searchKeyword = ref('')
const selectedApi = ref(null)

const apis = ref([
  {
    id: 1,
    method: 'GET',
    path: '/api/admin/list',
    name: '获取管理员列表',
    description: '获取管理员列表，支持分页和筛选',
    parameters: [
      { name: 'page', type: 'int', required: false, description: '页码' },
      { name: 'size', type: 'int', required: false, description: '每页数量' }
    ],
    responseExample: { code: 200, data: [], message: 'success' }
  },
  {
    id: 2,
    method: 'POST',
    path: '/api/admin/create',
    name: '创建管理员',
    description: '创建新的管理员账户',
    parameters: [
      { name: 'username', type: 'string', required: true, description: '用户名' },
      { name: 'password', type: 'string', required: true, description: '密码' }
    ],
    responseExample: { code: 200, data: { id: 1 }, message: 'success' }
  }
])

const filteredApis = computed(() => {
  return apis.value.filter(api => 
    api.name.includes(searchKeyword.value) || 
    api.path.includes(searchKeyword.value)
  )
})

const selectApi = (api: any) => {
  selectedApi.value = api
}

const exportDocs = () => {
  ElMessage.success('文档导出功能开发中')
}
</script>

<style scoped lang="scss">
.api-docs {
  height: calc(100vh - 120px);
  padding: 24px;
}

.docs-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.docs-content {
  display: flex;
  gap: 24px;
  height: calc(100% - 60px);
}

.docs-sidebar {
  width: 350px;
  background: white;
  border-radius: 8px;
  padding: 16px;
  overflow-y: auto;
}

.search-input {
  margin-bottom: 16px;
}

.api-list {
  .api-item {
    display: flex;
    gap: 12px;
    padding: 12px;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.2s;
    
    &:hover {
      background: #f5f5f5;
    }
    
    &.active {
      background: #eff6ff;
    }
  }
}

.api-method {
  width: 60px;
  height: 24px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: white;
  
  &.get { background: #67c23a; }
  &.post { background: #409eff; }
  &.put { background: #e6a23c; }
  &.delete { background: #f56c6c; }
}

.docs-main {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 24px;
  overflow-y: auto;
}

.detail-header {
  margin-bottom: 24px;
  
  .method-tag {
    display: inline-block;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 600;
    color: white;
    margin-bottom: 12px;
    
    &.get { background: #67c23a; }
    &.post { background: #409eff; }
    &.put { background: #e6a23c; }
    &.delete { background: #f56c6c; }
  }
}

.detail-card {
  margin-bottom: 20px;
}

.welcome-message {
  text-align: center;
  margin-top: 100px;
  color: #999;
  
  i {
    font-size: 64px;
    margin-bottom: 16px;
  }
}

pre {
  background: #f5f5f5;
  padding: 16px;
  border-radius: 4px;
  overflow-x: auto;
}
</style>
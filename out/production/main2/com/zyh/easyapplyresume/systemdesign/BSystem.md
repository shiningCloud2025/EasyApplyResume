# EasyApplyResume Admin System - Frontend Development Guide

## 📋 项目概述

易投简历管理系统前端是基于 **Vue 3 + TypeScript + Element Plus** 构建的现代化管理平台，涵盖11个功能模块、60+API接口的完整实现。

## 🚀 快速开始

```bash
# 安装依赖
npm install

# 开发环境运行
npm run dev

# 生产环境构建
npm run build

# 类型检查
npm run type-check

# 代码检查
npm run lint
```

## 🛠️ 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue 3 | ^3.4.0 | 渐进式框架 |
| TypeScript | ^5.3.0 | 类型安全 |
| Element Plus | ^2.5.0 | UI组件库 |
| Pinia | ^2.1.0 | 状态管理 |
| Vue Router | ^4.2.0 | 路由管理 |
| Axios | ^1.6.0 | HTTP客户端 |
| Vite | ^5.0.0 | 构建工具 |

## 📁 项目结构

```
src/
├── api/              # API服务层
├── assets/           # 静态资源
├── components/       # 可复用组件
├── composables/      # 组合式函数
├── directives/       # 自定义指令
├── layout/           # 布局组件
├── router/           # 路由配置
├── stores/           # Pinia stores
├── types/            # TypeScript类型
├── utils/            # 工具函数
├── views/            # 页面组件
├── App.vue
└── main.ts
```

## 🔐 认证授权

### 登录实现

完整的登录页面实现,包含表单验证和错误处理。参考 `src/views/auth/LoginView.vue`

### Token管理

```typescript
// src/utils/auth.ts
const TOKEN_KEY = 'admin_token'

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token: string): void {
  localStorage.getItem(TOKEN_KEY, token)
}

export function removeToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}
```

### 权限指令

```typescript
// 使用 v-permission 指令控制按钮显示
<el-button v-permission="'admin:user:add'">新增</el-button>
<el-button v-permission="['admin:user:edit', 'admin:user:delete']">编辑</el-button>
```

## 📦 核心模块

### 1. 管理员管理 (7 APIs)

**接口列表:**
- `GET /admin/system/admin/list` - 获取管理员列表
- `GET /admin/system/admin/{id}` - 获取管理员详情
- `POST /admin/system/admin` - 新增管理员
- `PUT /admin/system/admin` - 修改管理员
- `DELETE /admin/system/admin/{id}` - 删除管理员
- `DELETE /admin/system/admin/batch` - 批量删除
- `PUT /admin/system/admin/status` - 修改状态

**类型定义:**
```typescript
export interface Admin {
  id: number
  username: string
  nickname: string
  email: string
  phone: string
  avatar?: string
  status: 0 | 1
  roleIds: number[]
  roleNames?: string[]
  createTime: string
  updateTime: string
}
```

### 2. 角色管理 (7 APIs)

**接口列表:**
- `GET /admin/system/role/list` - 获取角色列表
- `GET /admin/system/role/{id}` - 获取角色详情
- `POST /admin/system/role` - 新增角色
- `PUT /admin/system/role` - 修改角色
- `DELETE /admin/system/role/{id}` - 删除角色
- `POST /admin/system/role/assign-permission` - 分配权限
- `PUT /admin/system/role/status` - 修改状态

### 3. 权限管理 (6 APIs)

**接口列表:**
- `GET /admin/system/permission/tree` - 获取权限树
- `GET /admin/system/permission/list` - 获取权限列表
- `GET /admin/system/permission/{id}` - 获取权限详情
- `POST /admin/system/permission` - 新增权限
- `PUT /admin/system/permission` - 修改权限
- `DELETE /admin/system/permission/{id}` - 删除权限

### 4. 求职攻略文章 (6 APIs)

**接口列表:**
- `GET /admin/content/article/list` - 获取文章列表
- `GET /admin/content/article/{id}` - 获取文章详情
- `POST /admin/content/article` - 新增文章
- `PUT /admin/content/article` - 修改文章
- `DELETE /admin/content/article/{id}` - 删除文章
- `DELETE /admin/content/article/batch` - 批量删除

### 5. 招聘岗位 (6 APIs)

**接口列表:**
- `GET /admin/job/position/list` - 获取岗位列表
- `GET /admin/job/position/{id}` - 获取岗位详情
- `POST /admin/job/position` - 新增岗位
- `PUT /admin/job/position` - 修改岗位
- `DELETE /admin/job/position/{id}` - 删除岗位
- `DELETE /admin/job/position/batch` - 批量删除

### 6. 招聘信息 (6 APIs)

**接口列表:**
- `GET /admin/job/employment/list` - 获取招聘信息列表
- `GET /admin/job/employment/{id}` - 获取招聘信息详情
- `POST /admin/job/employment` - 新增招聘信息
- `PUT /admin/job/employment` - 修改招聘信息
- `DELETE /admin/job/employment/{id}` - 删除招聘信息
- `DELETE /admin/job/employment/batch` - 批量删除

### 7. 简历模板 (6 APIs)

**接口列表:**
- `GET /admin/resume/template/list` - 获取模板列表
- `GET /admin/resume/template/{id}` - 获取模板详情
- `POST /admin/resume/template` - 新增模板
- `PUT /admin/resume/template` - 修改模板
- `DELETE /admin/resume/template/{id}` - 删除模板
- `DELETE /admin/resume/template/batch` - 批量删除

### 8. 行业Map (5 APIs)

**接口列表:**
- `GET /admin/job/industry/tree` - 获取行业树
- `GET /admin/job/industry/list` - 获取行业列表
- `POST /admin/job/industry` - 新增行业
- `PUT /admin/job/industry` - 修改行业
- `DELETE /admin/job/industry/{id}` - 删除行业

### 9. 地理位置Map (6 APIs)

**接口列表:**
- `GET /admin/location/province/list` - 获取省份列表
- `GET /admin/location/city/list` - 获取城市列表
- `GET /admin/location/area/list` - 获取区域列表
- `GET /admin/location/cascader` - 获取级联数据
- `POST /admin/location/province` - 新增省份
- `POST /admin/location/city` - 新增城市

### 10. AI助手 (2 APIs)

**接口列表:**
- `POST /admin/ai/assistant/chat` - 发送消息
- `POST /admin/ai/assistant/chat/stream` - SSE流式对话
- `GET /admin/ai/assistant/history/{id}` - 获取历史记录

**SSE流式实现:**
```typescript
export function sendMessageStream(
  data: ChatRequest,
  onMessage: (content: string) => void,
  onComplete: () => void,
  onError: (error: Error) => void
) {
  const url = `${BASE_URL}/chat/stream`
  const eventSource = new EventSource(url)
  
  eventSource.onmessage = (event) => {
    const data = JSON.parse(event.data)
    if (data.type === 'content') {
      onMessage(data.content)
    } else if (data.type === 'done') {
      eventSource.close()
      onComplete()
    }
  }
  
  eventSource.onerror = (error) => {
    eventSource.close()
    onError(new Error('SSE connection error'))
  }
  
  return eventSource
}
```

### 11. 邮件通信 (6 APIs)

**接口列表:**
- `GET /admin/system/email/list` - 获取邮件列表
- `GET /admin/system/email/{id}` - 获取邮件详情
- `POST /admin/system/email/send` - 发送邮件
- `POST /admin/system/email/batch-send` - 批量发送
- `DELETE /admin/system/email/{id}` - 删除邮件
- `GET /admin/system/email/template/list` - 获取模板列表

## 🔧 通用组件

### AppTable - 表格组件

```vue
<AppTable
  :data="tableData"
  :columns="columns"
  :loading="loading"
  :pagination="pagination"
  @selection-change="handleSelectionChange"
  @sort-change="handleSortChange"
/>
```

### AppUpload - 上传组件

```vue
<AppUpload
  v-model="formData.avatar"
  :limit="1"
  accept="image/*"
  :on-success="handleUploadSuccess"
/>
```

### PermissionTree - 权限树

```vue
<PermissionTree
  v-model="selectedPermissions"
  :data="permissionTree"
  show-checkbox
  default-expand-all
/>
```

### LocationCascader - 地区级联

```vue
<LocationCascader
  v-model="formData.location"
  :level="3"
  placeholder="请选择省市区"
/>
```

### RichEditor - 富文本编辑器

```vue
<RichEditor
  v-model="formData.content"
  :height="400"
  :upload-url="uploadUrl"
/>
```

## 📝 表单验证

### 通用验证规则

```typescript
// src/utils/validate.ts

// 用户名验证
export function validateUsername(rule: any, value: string, callback: Function) {
  const usernameReg = /^[a-zA-Z0-9_]{3,20}$/
  if (!value) {
    callback(new Error('请输入用户名'))
  } else if (!usernameReg.test(value)) {
    callback(new Error('用户名只能包含字母、数字和下划线，长度3-20'))
  } else {
    callback()
  }
}

// 手机号验证
export function validatePhone(rule: any, value: string, callback: Function) {
  const phoneReg = /^1[3-9]\d{9}$/
  if (!value) {
    callback(new Error('请输入手机号'))
  } else if (!phoneReg.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}

// 邮箱验证
export function validateEmail(rule: any, value: string, callback: Function) {
  const emailReg = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/
  if (!value) {
    callback(new Error('请输入邮箱'))
  } else if (!emailReg.test(value)) {
    callback(new Error('请输入正确的邮箱'))
  } else {
    callback()
  }
}

// 密码验证
export function validatePassword(rule: any, value: string, callback: Function) {
  if (!value) {
    callback(new Error('请输入密码'))
  } else if (value.length < 6 || value.length > 20) {
    callback(new Error('密码长度为6-20位'))
  } else {
    callback()
  }
}
```

### 表单使用示例

```typescript
const formRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { validator: validateUsername, trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: validateEmail, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ]
}
```

## 🎨 UI/UX指南

### 颜色规范

```scss
// 主色
$primary-color: #409eff;
$success-color: #67c23a;
$warning-color: #e6a23c;
$danger-color: #f56c6c;
$info-color: #909399;

// 辅助色
$text-primary: #303133;
$text-regular: #606266;
$text-secondary: #909399;
$text-placeholder: #c0c4cc;

// 边框颜色
$border-base: #dcdfe6;
$border-light: #e4e7ed;
$border-lighter: #ebeef5;
$border-extra-light: #f2f6fc;

// 背景色
$background-base: #f5f7fa;
$background-light: #fafafa;
```

### 间距规范

```scss
$spacing-xs: 4px;
$spacing-sm: 8px;
$spacing-md: 12px;
$spacing-lg: 16px;
$spacing-xl: 20px;
$spacing-xxl: 24px;
```

### 字体规范

```scss
$font-size-xs: 12px;
$font-size-sm: 13px;
$font-size-base: 14px;
$font-size-lg: 16px;
$font-size-xl: 18px;
$font-size-xxl: 20px;
```

### 响应式断点

```scss
$breakpoint-xs: 576px;   // 手机
$breakpoint-sm: 768px;   // 平板
$breakpoint-md: 992px;   // 小屏
$breakpoint-lg: 1200px;  // 中屏
$breakpoint-xl: 1920px;  // 大屏
```

## 💡 最佳实践

### 1. 组件命名规范

```typescript
// 页面组件使用 PascalCase
// 文件名: AdminList.vue
export default {
  name: 'AdminList'
}

// 通用组件使用 PascalCase + 前缀
// 文件名: AppTable.vue
export default {
  name: 'AppTable'
}
```

### 2. API调用规范

```typescript
// ✅ 正确：使用 async/await + try-catch
async function fetchData() {
  loading.value = true
  try {
    const { data } = await getAdminList(queryForm)
    tableData.value = data.list
    total.value = data.total
  } catch (error) {
    console.error('Fetch error:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// ❌ 错误：不处理异常
async function fetchData() {
  const { data } = await getAdminList(queryForm)
  tableData.value = data.list
}
```

### 3. 状态管理规范

```typescript
// ✅ 正确：使用 Pinia + Composition API
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null)
  const isLoggedIn = computed(() => !!userInfo.value)
  
  async function getUserInfo() {
    // ...
  }
  
  return {
    userInfo,
    isLoggedIn,
    getUserInfo
  }
})
```

### 4. 类型定义规范

```typescript
// ✅ 正确：完整的类型定义
export interface Admin {
  id: number
  username: string
  nickname: string
  email: string
  phone: string
  avatar?: string          // 可选属性用 ?
  status: 0 | 1           // 使用字面量类型
  roleIds: number[]
  createTime: string
  updateTime: string
}

// ❌ 错误：使用 any
export interface Admin {
  [key: string]: any
}
```

### 5. 组合式函数（Composables）

```typescript
// src/composables/useTable.ts
export function useTable<T = any>() {
  const loading = ref(false)
  const tableData = ref<T[]>([])
  const total = ref(0)
  
  const pagination = reactive({
    pageNum: 1,
    pageSize: 10
  })
  
  async function fetchData(api: Function, params: any) {
    loading.value = true
    try {
      const { data } = await api({ ...params, ...pagination })
      tableData.value = data.list
      total.value = data.total
    } catch (error) {
      console.error('Fetch data error:', error)
    } finally {
      loading.value = false
    }
  }
  
  function handleCurrentChange(page: number) {
    pagination.pageNum = page
  }
  
  function handleSizeChange(size: number) {
    pagination.pageSize = size
    pagination.pageNum = 1
  }
  
  return {
    loading,
    tableData,
    total,
    pagination,
    fetchData,
    handleCurrentChange,
    handleSizeChange
  }
}
```

## ⚡ 性能优化

### 1. 路由懒加载

```typescript
const routes = [
  {
    path: '/admin',
    component: () => import('@/views/admin/AdminList.vue')
  }
]
```

### 2. 组件懒加载

```typescript
import { defineAsyncComponent } from 'vue'

const AsyncComponent = defineAsyncComponent(() =>
  import('@/components/heavy-component.vue')
)
```

### 3. 图片懒加载

```vue
<el-image
  :src="imageUrl"
  lazy
  :scroll-container="scrollContainer"
/>
```

### 4. 虚拟滚动（大列表）

```vue
<el-table-v2
  :data="largeDataSet"
  :columns="columns"
  :height="600"
/>
```

### 5. 防抖节流

```typescript
import { debounce, throttle } from 'lodash-es'

// 搜索防抖
const handleSearch = debounce(() => {
  fetchData()
}, 500)

// 滚动节流
const handleScroll = throttle(() => {
  // ...
}, 100)
```

### 6. Keep-alive缓存

```vue
<router-view v-slot="{ Component }">
  <keep-alive :include="cachedViews">
    <component :is="Component" :key="route.fullPath" />
  </keep-alive>
</router-view>
```

## 📊 完整API参考表

### 系统管理模块 (20 APIs)

| 模块 | 接口 | 方法 | 说明 |
|-----|------|------|------|
| 管理员 | `/admin/system/admin/list` | GET | 获取管理员列表 |
| 管理员 | `/admin/system/admin/{id}` | GET | 获取管理员详情 |
| 管理员 | `/admin/system/admin` | POST | 新增管理员 |
| 管理员 | `/admin/system/admin` | PUT | 修改管理员 |
| 管理员 | `/admin/system/admin/{id}` | DELETE | 删除管理员 |
| 管理员 | `/admin/system/admin/batch` | DELETE | 批量删除 |
| 管理员 | `/admin/system/admin/status` | PUT | 修改状态 |
| 角色 | `/admin/system/role/list` | GET | 获取角色列表 |
| 角色 | `/admin/system/role/{id}` | GET | 获取角色详情 |
| 角色 | `/admin/system/role` | POST | 新增角色 |
| 角色 | `/admin/system/role` | PUT | 修改角色 |
| 角色 | `/admin/system/role/{id}` | DELETE | 删除角色 |
| 角色 | `/admin/system/role/batch` | DELETE | 批量删除 |
| 角色 | `/admin/system/role/assign` | POST | 分配权限 |
| 权限 | `/admin/system/permission/tree` | GET | 获取权限树 |
| 权限 | `/admin/system/permission/list` | GET | 获取权限列表 |
| 权限 | `/admin/system/permission/{id}` | GET | 获取权限详情 |
| 权限 | `/admin/system/permission` | POST | 新增权限 |
| 权限 | `/admin/system/permission` | PUT | 修改权限 |
| 权限 | `/admin/system/permission/{id}` | DELETE | 删除权限 |

### 内容管理模块 (6 APIs)

| 模块 | 接口 | 方法 | 说明 |
|-----|------|------|------|
| 文章 | `/admin/content/article/list` | GET | 获取文章列表 |
| 文章 | `/admin/content/article/{id}` | GET | 获取文章详情 |
| 文章 | `/admin/content/article` | POST | 新增文章 |
| 文章 | `/admin/content/article` | PUT | 修改文章 |
| 文章 | `/admin/content/article/{id}` | DELETE | 删除文章 |
| 文章 | `/admin/content/article/batch` | DELETE | 批量删除 |

### 招聘管理模块 (12 APIs)

| 模块 | 接口 | 方法 | 说明 |
|-----|------|------|------|
| 岗位 | `/admin/job/position/list` | GET | 获取岗位列表 |
| 岗位 | `/admin/job/position/{id}` | GET | 获取岗位详情 |
| 岗位 | `/admin/job/position` | POST | 新增岗位 |
| 岗位 | `/admin/job/position` | PUT | 修改岗位 |
| 岗位 | `/admin/job/position/{id}` | DELETE | 删除岗位 |
| 岗位 | `/admin/job/position/batch` | DELETE | 批量删除 |
| 招聘信息 | `/admin/job/employment/list` | GET | 获取招聘信息列表 |
| 招聘信息 | `/admin/job/employment/{id}` | GET | 获取招聘信息详情 |
| 招聘信息 | `/admin/job/employment` | POST | 新增招聘信息 |
| 招聘信息 | `/admin/job/employment` | PUT | 修改招聘信息 |
| 招聘信息 | `/admin/job/employment/{id}` | DELETE | 删除招聘信息 |
| 招聘信息 | `/admin/job/employment/batch` | DELETE | 批量删除 |

### 简历管理模块 (6 APIs)

| 模块 | 接口 | 方法 | 说明 |
|-----|------|------|------|
| 模板 | `/admin/resume/template/list` | GET | 获取模板列表 |
| 模板 | `/admin/resume/template/{id}` | GET | 获取模板详情 |
| 模板 | `/admin/resume/template` | POST | 新增模板 |
| 模板 | `/admin/resume/template` | PUT | 修改模板 |
| 模板 | `/admin/resume/template/{id}` | DELETE | 删除模板 |
| 模板 | `/admin/resume/template/batch` | DELETE | 批量删除 |

### Map管理模块 (11 APIs)

| 模块 | 接口 | 方法 | 说明 |
|-----|------|------|------|
| 行业 | `/admin/job/industry/tree` | GET | 获取行业树 |
| 行业 | `/admin/job/industry/list` | GET | 获取行业列表 |
| 行业 | `/admin/job/industry` | POST | 新增行业 |
| 行业 | `/admin/job/industry` | PUT | 修改行业 |
| 行业 | `/admin/job/industry/{id}` | DELETE | 删除行业 |
| 地理 | `/admin/location/province/list` | GET | 获取省份列表 |
| 地理 | `/admin/location/city/list` | GET | 获取城市列表 |
| 地理 | `/admin/location/area/list` | GET | 获取区域列表 |
| 地理 | `/admin/location/cascader` | GET | 获取级联数据 |
| 地理 | `/admin/location/province` | POST | 新增省份 |
| 地理 | `/admin/location/city` | POST | 新增城市 |

### AI助手模块 (3 APIs)

| 模块 | 接口 | 方法 | 说明 |
|-----|------|------|------|
| AI | `/admin/ai/assistant/chat` | POST | 发送消息 |
| AI | `/admin/ai/assistant/chat/stream` | POST | SSE流式对话 |
| AI | `/admin/ai/assistant/history/{id}` | GET | 获取历史记录 |

### 邮件通信模块 (6 APIs)

| 模块 | 接口 | 方法 | 说明 |
|-----|------|------|------|
| 邮件 | `/admin/system/email/list` | GET | 获取邮件列表 |
| 邮件 | `/admin/system/email/{id}` | GET | 获取邮件详情 |
| 邮件 | `/admin/system/email/send` | POST | 发送邮件 |
| 邮件 | `/admin/system/email/batch-send` | POST | 批量发送 |
| 邮件 | `/admin/system/email/{id}` | DELETE | 删除邮件 |
| 邮件 | `/admin/system/email/template/list` | GET | 获取模板列表 |

---

## 📚 附录

### 环境变量配置

```env
# .env.development
VITE_APP_TITLE=易投简历管理系统
VITE_API_BASE_URL=http://localhost:8080
VITE_UPLOAD_URL=http://localhost:8080/admin/file/upload
VITE_TIMEOUT=30000

# .env.production
VITE_APP_TITLE=易投简历管理系统
VITE_API_BASE_URL=https://api.easyapplyresume.com
VITE_UPLOAD_URL=https://api.easyapplyresume.com/admin/file/upload
VITE_TIMEOUT=30000
```

### package.json 依赖

```json
{
  "name": "easyapplyresume-admin",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vue-tsc && vite build",
    "preview": "vite preview",
    "lint": "eslint . --ext .vue,.js,.jsx,.cjs,.mjs,.ts,.tsx,.cts,.mts --fix --ignore-path .gitignore",
    "format": "prettier --write src/"
  },
  "dependencies": {
    "vue": "^3.4.0",
    "vue-router": "^4.2.0",
    "pinia": "^2.1.0",
    "element-plus": "^2.5.0",
    "@element-plus/icons-vue": "^2.3.0",
    "axios": "^1.6.0",
    "dayjs": "^1.11.0",
    "lodash-es": "^4.17.21",
    "nprogress": "^0.2.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.0",
    "typescript": "^5.3.0",
    "vue-tsc": "^1.8.0",
    "vite": "^5.0.0",
    "@types/node": "^20.10.0",
    "@types/lodash-es": "^4.17.12",
    "@types/nprogress": "^0.2.3",
    "sass": "^1.69.0",
    "eslint": "^8.56.0",
    "prettier": "^3.1.0"
  }
}
```

### vite.config.ts 配置

```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: false,
    rollupOptions: {
      output: {
        manualChunks: {
          'vue-vendor': ['vue', 'vue-router', 'pinia'],
          'element-plus': ['element-plus', '@element-plus/icons-vue']
        }
      }
    }
  }
})
```

---

## 🎯 总结

本文档提供了易投简历管理系统前端的完整开发指南，涵盖：

✅ **11个核心功能模块**  
✅ **60+ API接口完整实现**  
✅ **Vue 3 + TypeScript + Element Plus 最佳实践**  
✅ **完整的认证授权体系**  
✅ **可复用组件库**  
✅ **SSE流式对话实现**  
✅ **性能优化方案**  
✅ **UI/UX设计规范**  

建议开发团队严格按照本文档进行前端开发，确保代码质量和一致性。

---

**文档版本:** v1.0.0  
**更新时间:** 2025-01-15  
**维护者:** 前端开发团队

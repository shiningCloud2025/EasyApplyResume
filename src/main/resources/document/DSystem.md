# D端系统 - 易投简历观测与广告端设计文档

## 1. 系统概述

### 1.1 系统定位
D端（Data & Display）系统是易投简历平台的观测与广告管理端，主要用于：
- 平台数据监控与可视化
- 广告投放与管理
- 系统公告发布
- 用户行为分析

### 1.2 技术架构
- **前端框架**: Vue 3 + TypeScript
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router 4
- **HTTP客户端**: Axios
- **图表库**: ECharts
- **构建工具**: Vite

---

## 2. 功能模块

### 2.1 登录认证
| 功能 | 接口路径 | 请求方式 | 说明 |
|------|---------|---------|------|
| 账号密码登录 | `/admin/login/formal` | POST | 管理员账号密码登录 |
| 手机验证码登录 | `/admin/login/phone` | POST | 手机号+验证码登录 |
| 邮箱验证码登录 | `/admin/login/email` | POST | 邮箱+验证码登录 |
| 发送手机验证码 | `/admin/sms/sendPhoneCode` | GET | 发送登录验证码 |
| 发送邮箱验证码 | `/admin/email/sendEmailCode` | GET | 发送邮箱验证码 |

### 2.2 数据监控

#### 2.2.1 管理端数据
| 功能 | 接口路径 | 请求方式 | 说明 |
|------|---------|---------|------|
| 计算某天访问量 | `/admonitor/admin/dailyVisitNum/calculateAdmonitorAdminDailyVisitNum` | GET | 参数: time |
| 总访问量 | `/admonitor/admin/dailyVisitNum/calculateAdmonitorAdminDailyVisitNumTotal` | GET | - |
| 今日新增访问量 | `/admonitor/admin/dailyVisitNum/calculateDayIncreaseAdmonitorAdminDailyVisitNum` | GET | - |
| 查询时间段访问量 | `/admonitor/admin/dailyVisitTotalNum/findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum` | GET | 参数: fromDate, endDate |
| 查询时间段管理员数量 | `/admonitor/adminDaliyAdminNum/findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum` | GET | 参数: fromDate, endDate |

#### 2.2.2 用户端数据
| 功能 | 接口路径 | 请求方式 | 说明 |
|------|---------|---------|------|
| 计算某天访问量 | `/admonitor/user/dailyVisitNum/calculateAdmonitorUserDailyVisitNum` | GET | 参数: time |
| 总访问量 | `/admonitor/user/dailyVisitNum/calculateAdmonitorUserDailyVisitNumTotal` | GET | - |
| 今日新增访问量 | `/admonitor/user/dailyVisitNum/calculateDayIncreaseAdmonitorUserDailyVisitNum` | GET | - |
| 查询时间段访问量 | `/admonitor/user/dailyVisitTotalNum/findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum` | GET | 参数: fromDate, endDate |
| 查询时间段用户数量 | `/admonitor/user/daliyUserNum/findFromTimeToEndTimeAdmonitorUserDaliyUserNum` | GET | 参数: fromDate, endDate |

### 2.3 广告管理

#### 2.3.1 管理端广告
| 功能 | 接口路径 | 请求方式 | 说明 |
|------|---------|---------|------|
| 添加广告 | `/admonitor/admin/advertisement/addAdmonitorAdminAdvertisement` | POST | - |
| 更新广告 | `/admonitor/admin/advertisement/updateAdmonitorAdminAdvertisement` | PUT | - |
| 删除广告 | `/admonitor/admin/advertisement/deleteAdmonitorAdminAdvertisement` | DELETE | 参数: id |
| 查询广告详情 | `/admonitor/admin/advertisement/findAdmonitorAdminAdvertisementById` | GET | 参数: id |
| 分页查询广告 | `/admonitor/admin/advertisement/findAdmonitorAdminAdvertisementByPage` | POST | 参数: pageNum, pageSize |
| 查询所有广告 | `/admonitor/admin/advertisement/findAllAdmonitorAdminAdvertisement` | GET | - |

#### 2.3.2 用户端广告
| 功能 | 接口路径 | 请求方式 | 说明 |
|------|---------|---------|------|
| 添加广告 | `/admonitor/user/advertisement/addAdmonitorUserAdvertisement` | POST | - |
| 更新广告 | `/admonitor/user/advertisement/updateAdmonitorUserAdvertisement` | PUT | - |
| 删除广告 | `/admonitor/user/advertisement/deleteAdmonitorUserAdvertisement` | DELETE | 参数: id |
| 查询广告详情 | `/admonitor/user/advertisement/findAdmonitorUserAdvertisementById` | GET | 参数: id |
| 分页查询广告 | `/admonitor/user/advertisement/findAdmonitorUserAdvertisementByPage` | POST | 参数: pageNum, pageSize |
| 查询所有广告 | `/admonitor/user/advertisement/findAllAdmonitorUserAdvertisement` | GET | - |

### 2.4 公告管理

#### 2.4.1 管理端公告
| 功能 | 接口路径 | 请求方式 | 说明 |
|------|---------|---------|------|
| 添加公告 | `/admonitor/admin/announcement/add` | POST | - |
| 修改公告 | `/admonitor/admin/announcement/update` | PUT | - |
| 获取公告信息 | `/admonitor/admin/announcement/getInfo` | GET | - |

#### 2.4.2 用户端公告
| 功能 | 接口路径 | 请求方式 | 说明 |
|------|---------|---------|------|
| 添加公告 | `/admonitor/user/announcement/add` | POST | - |
| 修改公告 | `/admonitor/user/announcement/update` | PUT | - |
| 获取公告信息 | `/admonitor/user/announcement/getInfo` | GET | - |

---

## 3. 页面设计

### 3.1 页面结构
```
app/ad_monitor/
├── src/
│   ├── api/                 # API接口
│   │   └── index.ts
│   ├── layout/              # 布局组件
│   │   └── MonitorLayout.vue
│   ├── router/              # 路由配置
│   │   └── index.ts
│   ├── store/               # 状态管理
│   │   └── auth.ts
│   ├── styles/              # 全局样式
│   │   └── index.scss
│   ├── utils/               # 工具函数
│   │   └── index.ts
│   ├── views/               # 页面组件
│   │   ├── auth/
│   │   │   └── Login.vue    # 登录页
│   │   └── monitor/
│   │       ├── Dashboard.vue      # 仪表盘首页
│   │       ├── Advertisement.vue  # 广告管理
│   │       ├── Announcement.vue   # 公告管理
│   │       └── Statistics.vue     # 数据统计
│   ├── App.vue
│   └── main.ts
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

### 3.2 页面功能说明

#### 3.2.1 登录页 (Login.vue)
- 支持三种登录方式：账号密码、手机验证码、邮箱验证码
- 复用管理端登录样式，修改主题色为青绿色系
- 登录成功后跳转到仪表盘

#### 3.2.2 仪表盘首页 (Dashboard.vue)
- 显示关键指标卡片：管理端总访问量、用户端总访问量、今日新增、管理员数量、用户数量
- 访问量趋势折线图（管理端 vs 用户端）
- 快捷功能入口

#### 3.2.3 广告管理 (Advertisement.vue)
- 广告列表（分页、搜索、筛选）
- 广告CRUD操作
- 支持管理端广告和用户端广告切换

#### 3.2.4 公告管理 (Announcement.vue)
- 管理端公告编辑
- 用户端公告编辑
- 富文本编辑器支持

#### 3.2.5 数据统计 (Statistics.vue)
- 时间范围选择器
- 访问量趋势图表
- 用户/管理员数量趋势图表
- 数据导出功能

---

## 4. UI/UX设计

### 4.1 主题色
- 主色调: `#10b981` (翠绿色)
- 渐变色: `linear-gradient(135deg, #10b981 0%, #059669 100%)`
- 辅助色: `#34d399`, `#6ee7b7`

### 4.2 布局设计
- 左侧固定侧边栏（可折叠）
- 顶部导航栏
- 面包屑导航
- 响应式设计

---

## 5. 接口对接注意事项

### 5.1 请求基础配置
- 基础URL: `/api`
- Token请求头: `Authorization: Bearer {token}`
- Token存储: `localStorage.getItem('monitor_token')`

### 5.2 统一响应格式
```typescript
interface BaseResult<T> {
  code: number;      // 状态码，200为成功
  message: string;   // 提示信息
  data: T;          // 响应数据
}
```

### 5.3 日期格式
- 前端传参格式: `yyyy-MM-dd` 或时间戳
- 后端返回格式: ISO 8601

---

## 6. 开发规范

### 6.1 命名规范
- 组件文件: PascalCase (如 `Dashboard.vue`)
- 工具函数: camelCase (如 `formatDateTime`)
- 常量: UPPER_SNAKE_CASE (如 `API_BASE_URL`)

### 6.2 代码风格
- 使用 TypeScript 进行类型检查
- 使用 Composition API (`<script setup>`)
- 使用 SCSS 进行样式管理

---

## 7. 部署说明

### 7.1 开发环境
```bash
cd app/ad_monitor
npm install
npm run dev
```

### 7.2 生产构建
```bash
npm run build
```

### 7.3 端口配置
- 开发服务器: `http://localhost:5175`
- API代理: `/api` -> `http://localhost:8080/api`

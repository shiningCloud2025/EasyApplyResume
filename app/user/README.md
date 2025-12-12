# EasyApplyResume 用户端前端

基于 React + TypeScript + Ant Design 的现代化求职简历平台。

## 快速开始

```bash
# 安装依赖
npm install

# 开发环境运行
npm run dev

# 生产构建
npm run build

# 代码检查
npm run lint
```

## 技术栈

- **React 18** - 渐进式框架
- **TypeScript** - 类型安全
- **Ant Design** - UI组件库
- **React Router** - 路由管理
- **Axios** - HTTP客户端
- **Zustand** - 状态管理
- **Vite** - 构建工具

## 功能模块

### ✅ 已完成

- 🔐 **认证模块**: 支持账号密码/手机/邮箱3种登录方式
- 🏠 **门户首页**: 响应式设计，核心功能展示
- 👤 **个人中心**: 用户信息管理
- 📄 **简历管理**: 模板市场、我的简历、在线编辑器
- 💼 **职位信息**: 职位列表、筛选搜索
- 🤖 **AI助手**: 智能对话，求职建议

### 🚧 开发中

- 📚 求职攻略
- 💭 用户反馈

## 项目结构

```
src/
├── api/              # API接口层
├── components/       # 通用组件
├── views/            # 页面组件
├── stores/           # 状态管理
├── types/            # TypeScript类型
├── utils/            # 工具函数
└── styles/           # 样式文件
```

## 开发说明

1. 项目端口：3001
2. 后端API代理：8080
3. 支持响应式设计，移动端友好
4. 完整的错误处理和加载状态
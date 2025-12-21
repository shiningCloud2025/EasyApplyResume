# EasyApplyResume User System - Frontend Development Guide

## 📋 项目概述

易投简历用户端是基于 **Vue 3 + TypeScript + Element Plus** 构建的现代化求职简历平台，为求职者提供智能简历制作、职位搜索、AI求职助手等核心功能。

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
│   ├── auth.ts       # 认证相关API
│   ├── resume.ts     # 简历相关API
│   ├── job.ts        # 职位相关API
│   └── ai.ts         # AI助手API
├── assets/           # 静态资源
├── components/       # 可复用组件
│   ├── common/       # 通用组件
│   ├── resume/       # 简历组件
│   └── ai/           # AI组件
├── composables/      # 组合式函数
├── layout/           # 布局组件
├── router/           # 路由配置
├── stores/           # Pinia stores
│   ├── user.ts       # 用户状态
│   ├── resume.ts     # 简历状态
│   └── job.ts        # 职位状态
├── types/            # TypeScript类型
├── utils/            # 工具函数
├── views/            # 页面组件
│   ├── auth/         # 认证页面
│   ├── resume/       # 简历页面
│   ├── job/          # 职位页面
│   └── ai/           # AI助手页面
├── App.vue
└── main.ts
```

## 🎯 核心功能模块

### 1️⃣ 用户认证模块

#### 1.1 登录注册页面（Auth Page）

##### 📍 页面路由
- 登录页: `/auth/login`
- 注册页: `/auth/register`

##### 🔧 功能流程

**A. 普通登录流程**
```
用户输入账号密码 → 点击登录 → 调用登录接口 → 保存Token → 跳转首页
```

**B. 手机验证码登录流程**
```
用户输入手机号 → 点击获取验证码 → 调用发送验证码接口 → 用户输入验证码 → 调用手机登录接口 → 保存Token → 跳转首页
```

**C. 邮箱验证码登录流程**
```
用户输入邮箱 → 点击获取验证码 → 调用发送验证码接口 → 用户输入验证码 → 调用邮箱登录接口 → 保存Token → 跳转首页
```

**D. 注册流程**
```
用户填写注册信息 → 获取手机验证码 → 获取邮箱验证码 → 提交注册 → 自动登录 → 跳转首页
```

**E. 随机账号生成流程**
```
用户点击"随机生成账号" → 调用生成接口 → 获取账号密码 → 显示给用户 → 用户使用生成的账号登录
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/auth/formalLogin` | POST | 用户点击"普通登录"按钮 | 账号密码登录 |
| 2 | `/user/auth/phoneLogin` | POST | 用户输入手机验证码后点击"登录" | 手机验证码登录 |
| 3 | `/user/auth/emailLogin` | POST | 用户输入邮箱验证码后点击"登录" | 邮箱验证码登录 |
| 4 | `/user/auth/formalRegister` | POST | 用户填写完注册信息点击"注册" | 用户注册 |
| 5 | `/user/auth/generateRandomAccount` | POST | 用户点击"随机生成账号"按钮 | 生成随机账号 |
| 6 | `/user/sms/send` | POST | 登录页或注册页点击"获取手机验证码" | 发送手机验证码 |
| 7 | `/user/sms/check` | POST | 用户输入手机验证码后(可选校验) | 校验手机验证码 |
| 8 | `/user/email/loginandregister/send` | POST | 登录页或注册页点击"获取邮箱验证码" | 发送邮箱验证码 |
| 9 | `/user/email/loginandregister/check` | POST | 用户输入邮箱验证码后(可选校验) | 校验邮箱验证码 |

##### 📝 接口详情

**1. 普通登录**
```typescript
// POST /user/auth/formalLogin
interface FormalLoginForm {
  accountOrPhoneOrEmail: string  // 用户账号/手机号/邮箱号
  password: string               // 用户密码
}

interface Response {
  code: number
  message: string
  data: string  // JWT Token
}
```

**2. 手机验证码登录**
```typescript
// POST /user/auth/phoneLogin
interface PhoneLoginForm {
  phone: string       // 手机号
  verifyCode: string  // 验证码
}

interface Response {
  code: number
  message: string
  data: string  // JWT Token
}
```

**3. 邮箱验证码登录**
```typescript
// POST /user/auth/emailLogin
interface EmailLoginForm {
  email: string       // 邮箱
  verifyCode: string  // 验证码
}

interface Response {
  code: number
  message: string
  data: string  // JWT Token
}
```

**4. 用户注册**
```typescript
// POST /user/auth/formalRegister
interface FormalRegisterForm {
  userAccount: string              // 用户账号 (必填)
  userUsername: string             // 用户名称 (必填)
  userEmail: string                // 用户邮箱 (必填)
  userPhone: string                // 用户手机 (必填)
  userPassword: string             // 用户密码 (必填)
  userImage?: string               // 用户头像
  userIntroduce?: string           // 用户介绍
  userDreamPosition?: number       // 用户目标岗位
  userDreamMinMonthSalary?: number // 最低月薪
  userDreamMaxMonthSalary?: number // 最高月薪
  userDreamWeekWorkDayNum?: number // 每周工作天数
  userDreamGoodWelfare?: string    // 福利待遇
  userRecruitLocationFirst?: string // 省级地址
  userRecruitLocationSecond?: string // 市级地址
  userUniversityCode?: number      // 大学编码
  phoneMessageCode: string         // 手机验证码 (必填)
  emailMessageCode: string         // 邮箱验证码 (必填)
}

interface Response {
  code: number
  message: string
  data: string  // 注册成功信息或Token
}
```

**5. 生成随机账号**
```typescript
// POST /user/auth/generateRandomAccount
interface Response {
  code: number
  message: string
  data: string  // 生成的随机账号(格式: "账号:xxx,密码:xxx")
}
```

**6. 发送手机验证码**
```typescript
// POST /user/sms/send
interface Request {
  phone: string  // 手机号
}

interface Response {
  code: number
  message: string
  data: null
}
```

**7. 校验手机验证码**
```typescript
// POST /user/sms/check
interface Request {
  phone: string  // 手机号
  code: string   // 验证码
}

interface Response {
  code: number
  message: string
  data: null
}
```

**8. 发送邮箱验证码**
```typescript
// POST /user/email/loginandregister/send
interface Request {
  email: string  // 邮箱地址
}

interface Response {
  code: number
  message: string
  data: null
}
```

**9. 校验邮箱验证码**
```typescript
// POST /user/email/loginandregister/check
interface Request {
  email: string  // 邮箱地址
  code: string   // 验证码
}

interface Response {
  code: number
  message: string
  data: null
}
```

##### 💡 实现建议

1. **验证码倒计时**: 发送验证码后显示60秒倒计时,防止频繁请求
2. **表单验证**: 使用Element Plus的表单验证规则
3. **错误处理**: 使用全局错误拦截器统一处理错误提示
4. **Token管理**: 登录成功后将Token存储在localStorage,并设置axios请求头
5. **自动跳转**: 登录成功后根据用户类型跳转到对应页面

---

#### 1.2 个人中心页面（Profile Page）

##### 📍 页面路由
- 个人中心: `/profile`
- 编辑资料: `/profile/edit`

##### 🔧 功能流程

**A. 查看个人信息流程**
```
进入个人中心页面 → 自动调用获取用户信息接口 → 显示用户详细信息
```

**B. 编辑个人信息流程**
```
点击"编辑资料" → 进入编辑页面 → 修改信息 → 点击保存 → 调用更新接口 → 刷新用户信息
```

**C. 退出登录流程**
```
点击"退出登录" → 调用退出接口 → 清除Token → 跳转登录页
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/auth/getUserInfo` | POST | 进入个人中心页面时 | 获取当前登录用户信息 |
| 2 | `/user/user/getUserByUserId` | GET | 查看用户详情时 | 根据用户ID查询用户信息 |
| 3 | `/user/user/updateUser` | POST | 用户点击"保存"按钮 | 更新用户信息 |
| 4 | `/user/auth/logout` | POST | 用户点击"退出登录"按钮 | 退出登录 |

##### 📝 接口详情

**1. 获取用户信息(通过Token)**
```typescript
// POST /user/auth/getUserInfo
// 注意: 需要在请求头中携带Token
// Header: { Authorization: Bearer <token> }

interface Response {
  code: number
  message: string
  data: {
    userId: number
    username: string
    authorities: string[]  // 用户权限列表
    // ... 其他SecurityUser字段
  }
}
```

**2. 根据用户ID查询用户信息**
```typescript
// GET /user/user/getUserByUserId?userId={userId}
interface Request {
  userId: string  // 用户ID
}

interface UserInfoVO {
  userId: number
  userAccount: string
  userUsername: string
  userEmail: string
  userPhone: string
  userPassword: string
  userImage?: string
  userIntroduce?: string
  userCreatedTime: Date
  userLoginTime: Date
  userDreamPosition: number
  userDreamMinMonthSalary: number
  userDreamMaxMonthSalary: number
  userDreamWeekWorkDayNum: number
  userDreamGoodWelfare: string
}

interface Response {
  code: number
  message: string
  data: UserInfoVO
}
```

**3. 更新用户信息**
```typescript
// POST /user/user/updateUser
interface UserUpdateForm {
  userId: number                    // 用户ID (必填)
  userUsername?: string             // 用户名称
  userEmail?: string                // 用户邮箱
  userPhone?: string                // 用户手机
  userPassword?: string             // 用户密码
  userImage?: string                // 用户头像
  userIntroduce?: string            // 用户介绍
  userDreamPosition?: number        // 目标岗位
  userDreamMinMonthSalary?: number  // 最低月薪
  userDreamMaxMonthSalary?: number  // 最高月薪
  userDreamWeekWorkDayNum?: number  // 每周工作天数
  userDreamGoodWelfare?: string     // 福利待遇
  userRecruitLocationFirst?: string // 省级地址
  userRecruitLocationSecond?: string // 市级地址
  userUniversityCode?: number       // 大学编码
}

interface Response {
  code: number
  message: string
  data: null
}
```

**4. 退出登录**
```typescript
// POST /user/auth/logout
interface Request {
  userId: number  // 用户ID
}

interface Response {
  code: number
  message: string
  data: null
}
```

##### 💡 实现建议

1. **头像上传**: 集成图片上传组件,支持裁剪和预览
2. **表单验证**: 邮箱、手机号需要格式验证
3. **级联选择**: 地址选择使用级联选择器
4. **薪资范围**: 最低月薪不能大于最高月薪
5. **退出确认**: 退出登录前弹出确认对话框

---

### 2️⃣ 简历管理模块

#### 2.1 简历模板市场页面（Template Market）

##### 📍 页面路由
- 模板市场: `/resume/templates`
- 模板详情: `/resume/template/:id`

##### 🔧 功能流程

**A. 浏览模板流程**
```
进入模板市场 → 调用分页查询接口 → 展示模板列表 → 支持筛选、排序、分页
```

**B. 查看模板详情流程**
```
点击模板卡片 → 调用模板详情接口 → 显示模板预览 → 显示模板信息
```

**C. 收藏模板流程**
```
点击"收藏"按钮 → 调用收藏接口 → 更新收藏状态 → 提示收藏成功
```

**D. 取消收藏流程**
```
点击"已收藏"按钮 → 调用取消收藏接口 → 更新收藏状态 → 提示取消成功
```

**E. 使用模板创建简历流程**
```
点击"使用此模板" → 调用第一次保存简历接口 → 跳转到简历编辑页
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/resumeTemplate/findResumeTemplateByPage` | GET | 进入模板市场页面或筛选条件变化时 | 分页查询简历模板 |
| 2 | `/user/resumeTemplate/findResumeTemplateById` | GET | 点击模板查看详情时 | 查询模板详情 |
| 3 | `/user/userCollections/isUserCollectResumeTemplate` | GET | 进入模板详情页时 | 检查是否已收藏 |
| 4 | `/user/userCollections/saveResumeTemplateByUserId` | GET | 点击收藏/取消收藏按钮时 | 收藏或取消收藏模板 |
| 5 | `/user/saveResume/saveUserSaveResumeInfoFirst` | POST | 点击"使用此模板"按钮时 | 通过模板创建简历 |

##### 📝 接口详情

**1. 分页查询简历模板**
```typescript
// GET /user/resumeTemplate/findResumeTemplateByPage
// ?pageNum={pageNum}&pageSize={pageSize}
// Body: ResumeTemplateQuery

interface ResumeTemplateQuery {
  resumeTemplateName?: string        // 模板名称(模糊查询)
  resumeTemplateIndustry?: number    // 模板行业
  resumeTemplateStyle?: string       // 模板风格
  // ... 其他筛选条件
}

interface ResumeTemplatePageVO {
  resumeTemplateId: number
  resumeTemplateName: string
  resumeTemplateIndustry: number
  resumeTemplateStyle: string
  resumeTemplatePreviewImage: string  // 预览图
  resumeTemplateDescription: string
  resumeTemplateCreatedTime: Date
  resumeTemplateUpdatedTime: Date
}

interface Response {
  code: number
  message: string
  data: {
    records: ResumeTemplatePageVO[]
    total: number
    size: number
    current: number
    pages: number
  }
}
```

**2. 查询模板详情**
```typescript
// GET /user/resumeTemplate/findResumeTemplateById
// ?resumeTemplateId={resumeTemplateId}

interface ResumeTemplateInfoVO {
  resumeTemplateId: number
  resumeTemplateName: string
  resumeTemplateIndustry: number
  resumeTemplateStyle: string
  resumeTemplateReactCode: string      // React组件代码
  resumeTemplatePreviewImage: string
  resumeTemplateDescription: string
  resumeTemplateCreatedTime: Date
  resumeTemplateUpdatedTime: Date
}

interface Response {
  code: number
  message: string
  data: ResumeTemplateInfoVO
}
```

**3. 检查是否已收藏模板**
```typescript
// GET /user/userCollections/isUserCollectResumeTemplate
// ?userId={userId}&rtid={rtid}

interface Request {
  userId: number  // 用户ID
  rtid: number    // 简历模板ID
}

interface Response {
  code: number
  message: string
  data: boolean  // true-已收藏, false-未收藏
}
```

**4. 收藏或取消收藏模板**
```typescript
// GET /user/userCollections/saveResumeTemplateByUserId
// ?userId={userId}&rtid={rtid}&isCollect={isCollect}

interface Request {
  userId: number     // 用户ID
  rtid: number       // 简历模板ID
  isCollect: boolean // true-收藏, false-取消收藏
}

interface Response {
  code: number
  message: string
  data: null
}
```

**5. 通过模板创建简历(第一次)**
```typescript
// POST /user/saveResume/saveUserSaveResumeInfoFirst
// ?userId={userId}
// Body: ResumeTemplateInfoVO

interface Request {
  userId: number
  resumeTemplateInfo: ResumeTemplateInfoVO  // 模板详情
}

interface Response {
  code: number
  message: string
  data: string  // 成功信息
}
```

##### 💡 实现建议

1. **卡片布局**: 使用Grid布局展示模板,响应式设计
2. **图片懒加载**: 模板预览图使用懒加载提升性能
3. **筛选器**: 支持按行业、风格、创建时间筛选
4. **搜索功能**: 支持模板名称搜索
5. **收藏状态**: 使用心形图标区分收藏和未收藏状态
6. **预览功能**: 点击模板可预览完整效果

---

#### 2.2 我的简历页面（My Resumes）

##### 📍 页面路由
- 我的简历列表: `/resume/my-resumes`
- 简历编辑器: `/resume/edit/:sortedNum`
- 简历回收站: `/resume/trash`

##### 🔧 功能流程

**A. 查看简历列表流程**
```
进入我的简历页面 → 调用查询用户所有简历接口 → 展示简历列表
```

**B. 查看单个简历流程**
```
点击简历卡片 → 调用查询单个简历接口 → 展示简历详情
```

**C. 编辑简历流程**
```
点击"编辑"按钮 → 进入编辑器 → 修改内容 → 点击保存 → 调用保存接口 → 提示保存成功
```

**D. 删除简历流程**
```
点击"删除"按钮 → 确认删除 → 调用删除接口 → 简历移入回收站 → 刷新列表
```

**E. 恢复简历流程**
```
进入回收站 → 点击"恢复"按钮 → 调用恢复接口 → 简历恢复到我的简历 → 刷新列表
```

**F. 清空回收站流程**
```
进入回收站 → 点击"清空回收站"按钮 → 确认清空 → 调用清空接口 → 提示清空成功
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/saveResume/getUserSaveResumeInfoByUserId` | GET | 进入我的简历页面时 | 查询用户所有简历 |
| 2 | `/user/saveResume/getUserSaveResumeInfoByUserIdAndResumeId` | GET | 进入编辑器时 | 查询指定简历 |
| 3 | `/user/saveResume/saveUserSaveResumeInfo` | POST | 点击"保存"按钮时 | 保存/更新简历 |
| 4 | `/user/saveResume/deleteUserSaveResumeInfoByUserIdAndResumeId` | DELETE | 点击"删除"按钮时 | 删除简历(移入回收站) |
| 5 | `/user/deleteResume/getUserDeleteResumeInfoByUserId` | GET | 进入回收站页面时 | 查询回收站所有简历 |
| 6 | `/user/deleteResume/getUserDeleteResumeInfoByUserIdAndResumeSortedNum` | GET | 查看回收站单个简历时 | 查询回收站指定简历 |
| 7 | `/user/deleteResume/addUserDeleteResumeToUserSaveResume` | POST | 点击"恢复"按钮时 | 从回收站恢复简历 |
| 8 | `/user/deleteResume/clearUserAllDeleteResume` | DELETE | 点击"清空回收站"按钮时 | 清空回收站 |
| 9 | `/user/deleteResume/clearExpiredResume` | POST | 定时任务或手动触发 | 清理过期简历 |

##### 💡 实现建议

1. **简历排序**: 支持按创建时间、更新时间排序
2. **简历卡片**: 显示缩略图、名称、行业、最后更新时间
3. **批量操作**: 支持批量删除简历
4. **自动保存**: 编辑器支持自动保存,避免数据丢失
5. **版本控制**: 可考虑实现简历版本历史记录
6. **过期提醒**: 回收站简历超过30天提醒用户

---

### 3️⃣ 职位信息模块

#### 3.1 职位搜索页面（Job Search）

##### 📍 页面路由
- 职位列表: `/jobs`
- 职位详情: `/job/:id`

##### 🔧 功能流程

**A. 浏览职位流程**
```
进入职位列表页 → 调用分页查询接口 → 展示职位列表 → 支持筛选、排序
```

**B. 查看职位详情流程**
```
点击职位卡片 → 调用职位详情接口 → 显示职位详细信息
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/employmentInformation/getEmploymentInformationPage` | GET | 进入职位列表或筛选时 | 分页查询招聘信息 |
| 2 | `/user/employmentInformation/getEmploymentInformationInfo` | GET | 点击查看职位详情时 | 获取招聘信息详情 |

##### 💡 实现建议

1. **高级筛选**: 支持多维度筛选(行业、地点、薪资、经验)
2. **关键词搜索**: 支持职位名称、公司名称搜索
3. **排序功能**: 按薪资、发布时间排序
4. **收藏功能**: 用户可收藏感兴趣的职位
5. **申请记录**: 记录用户已申请的职位

---

#### 3.2 求职攻略页面（Job Advice）

##### 📍 页面路由
- 攻略列表: `/advice`
- 攻略详情: `/advice/:id`

##### 🔧 功能流程

**A. 浏览攻略流程**
```
进入攻略列表页 → 调用分页查询接口 → 展示攻略列表
```

**B. 查看攻略详情流程**
```
点击攻略卡片 → 调用攻略详情接口 → 显示攻略内容
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/jobAdviceArticle/getJobAdviceArticlePage` | GET | 进入攻略列表或筛选时 | 分页查询求职攻略 |
| 2 | `/user/jobAdviceArticle/getJobAdviceArticleInfo` | GET | 点击查看攻略详情时 | 获取攻略详情 |

##### 💡 实现建议

1. **分类导航**: 按应届生、社招、面试技巧等分类
2. **标签筛选**: 支持按标签筛选文章
3. **富文本渲染**: 支持Markdown或HTML格式
4. **点赞收藏**: 支持点赞和收藏功能
5. **相关推荐**: 显示相关文章推荐

---

### 4️⃣ AI助手模块

#### 4.1 AI求职助手页面（AI Assistant）

##### 📍 页面路由
- AI助手: `/ai/assistant`

##### 🔧 功能流程

**A. AI应用对话流程**
```
用户输入问题 → 点击发送 → 调用AI应用对话接口 → 流式接收回复 → 显示在对话框
```

**B. AI Agent对话流程**
```
用户输入复杂任务 → 点击发送 → 调用AI Agent接口 → SSE流式接收 → 显示执行过程和结果
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/aiResumeAssistant/application/chat` | POST | 用户发送普通问题时 | AI应用对话(流式) |
| 2 | `/user/aiResumeAssistant/agent/chat` | POST | 用户发送复杂任务时 | AI Agent对话(SSE流式) |

##### 📝 接口详情

**1. AI应用对话(流式)**
```typescript
// POST /user/aiResumeAssistant/application/chat
// ?chatId={chatId}
// Content-Type: text/event-stream

interface Request {
  message: string   // 用户消息
  chatId?: string   // 对话ID(可选,不传则自动生成)
}

interface Response {
  code: number
  message: string
  data: Flux<string>  // 流式返回,逐字输出
}

// 前端接收流式数据示例
const eventSource = new EventSource(`/user/aiResumeAssistant/application/chat?chatId=${chatId}`)
eventSource.onmessage = (event) => {
  const chunk = event.data
  // 将chunk追加到对话框
}
```

**2. AI Agent对话(SSE流式)**
```typescript
// POST /user/aiResumeAssistant/agent/chat
// ?chatId={chatId}
// Content-Type: text/event-stream

interface Response {
  code: number
  message: string
  data: SseEmitter  // SSE流式返回
}
```

##### 💡 实现建议

1. **流式渲染**: 使用SSE或WebSocket实现流式对话
2. **对话历史**: 保存对话历史,支持上下文理解
3. **思维链展示**: 显示AI的思考过程和执行步骤
4. **工具调用**: 展示AI调用的工具(文件操作、网络搜索等)
5. **错误处理**: 处理流式连接中断和错误
6. **打字机效果**: 逐字显示AI回复,提升体验

---

### 5️⃣ 辅助功能模块

#### 5.1 大学选择组件（University Selector）

##### 🔧 功能流程

**A. 获取所有大学流程**
```
组件挂载时 → 调用获取所有大学接口 → 填充下拉列表
```

**B. 搜索大学流程**
```
用户输入大学名称 → 调用模糊查询接口 → 显示匹配结果
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/universityMap/getAllUniversityMap` | GET | 组件初始化时 | 获取所有大学 |
| 2 | `/user/universityMap/getAllUniversityMapByName` | GET | 用户输入搜索关键词时 | 模糊查询大学 |

##### 💡 实现建议

1. **远程搜索**: 使用Element Plus的远程搜索Select
2. **防抖处理**: 搜索时添加防抖,避免频繁请求
3. **分组显示**: 按985/211/普通本科分组
4. **热门推荐**: 显示热门大学

---

#### 5.2 用户反馈模块（Feedback）

##### 📍 页面路由
- 提交反馈: `/feedback/submit`
- 我的反馈: `/feedback/my`
- 反馈详情: `/feedback/:id`

##### 🔧 功能流程

**A. 提交反馈流程**
```
用户填写反馈信息 → 点击提交 → 调用添加反馈接口 → 提示提交成功
```

**B. 查看反馈列表流程**
```
进入我的反馈页面 → 调用分页查询接口 → 展示反馈列表
```

**C. 查看反馈详情流程**
```
点击反馈项 → 调用查询详情接口 → 显示反馈内容和处理记录
```

##### 📡 接口调用列表

| 序号 | 接口地址 | 方法 | 调用时机 | 说明 |
|-----|---------|------|---------|------|
| 1 | `/user/feedback/addFeedback` | POST | 用户点击"提交反馈"按钮 | 添加反馈 |
| 2 | `/user/feedback/getFeedbackPage` | POST | 进入我的反馈页面或分页时 | 分页查询反馈 |
| 3 | `/user/feedback/findFeedbackById` | GET | 点击查看反馈详情时 | 查询反馈详情 |
| 4 | `/user/feedback/updateFeedbackStep` | POST | 用户或管理员更新反馈时 | 更新反馈阶段 |

##### 💡 实现建议

1. **反馈分类**: 明确的反馈类型选择
2. **图片上传**: 支持上传截图
3. **实时通知**: 反馈有新回复时推送通知
4. **处理进度**: 显示反馈处理的时间轴
5. **评价系统**: 处理完成后用户可评价

---

🎯 **文档总结**

本文档提供了完整的用户端系统前端开发指南：

✅ **5大核心功能模块** (用户认证、简历管理、职位信息、AI助手、辅助功能)  
✅ **39+ API接口完整覆盖**  
✅ **页面维度**的接口调用流程  
✅ **Vue 3 + TypeScript最佳实践**  
✅ **完整的类型定义**和接口文档  
✅ **流式对话实现方案**  
✅ **性能优化建议**

---

**文档版本:** v1.1.0  
**更新时间:** 2025-01-15  
**维护者:** 前端开发团队
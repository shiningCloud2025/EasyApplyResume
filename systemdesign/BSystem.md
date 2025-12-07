# 管理端系统接口文档 (B System API Documentation)

> **版本**: v1.7.0  
> **更新时间**: 2025-12-07  
> **基础路径**: `/admin`  
> **作者**: shiningCloud2025

---

## 📋 目录

1. [登录认证](#1-登录认证)
2. [权限管理](#2-权限管理)
3. [内容管理](#3-内容管理)
4. [数据配置](#4-数据配置)
5. [通信工具](#5-通信工具)
6. [AI助手](#6-ai助手)

---

## 1. 登录认证

### 1.1 登录流程

#### 1.1.1 账号密码登录
**接口**: `POST /admin/auth/formalLogin`

**请求参数** (`AdminFormalLoginForm`):
```json
{
  "accountOrPhoneOrEmail": "string",  // 账号/手机号/邮箱
  "password": "string"                 // 密码
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."  // JWT Token
}
```

---

#### 1.1.2 手机验证码登录
**接口**: `POST /admin/auth/phoneLogin`

**请求参数** (`AdminPhoneLoginForm`):
```json
{
  "phone": "string",       // 手机号
  "messageCode": "string"  // 短信验证码
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."  // JWT Token
}
```

---

#### 1.1.3 邮箱验证码登录
**接口**: `POST /admin/auth/emailLogin`

**请求参数** (`EmailLoginForm`):
```json
{
  "email": "string",       // 邮箱
  "messageCode": "string"  // 邮箱验证码
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."  // JWT Token
}
```

---

### 1.2 验证码获取

#### 1.2.1 发送邮箱验证码
**接口**: `POST /admin/email/loginandregister/send`

**请求参数**:
- `email` (required): string - 邮箱地址

**响应示例**:
```json
{
  "code": 200,
  "message": "OK"
}
```

---

#### 1.2.2 校验邮箱验证码
**接口**: `POST /admin/email/loginandregister/check`

**请求参数**:
- `email` (required): string - 邮箱地址
- `code` (required): string - 验证码

**响应示例**:
```json
{
  "code": 200,
  "message": "OK"
}
```

---

#### 1.2.3 发送短信验证码
**接口**: `POST /user/sms/send`

**请求参数**:
- `phone` (required): string - 手机号

**响应示例**:
```json
{
  "code": 200,
  "message": "OK"
}
```

---

#### 1.2.4 校验短信验证码
**接口**: `POST /user/sms/check`

**请求参数**:
- `phone` (required): string - 手机号
- `code` (required): string - 验证码

**响应示例**:
```json
{
  "code": 200,
  "message": "OK"
}
```

---

## 2. 权限管理

### 2.1 管理员管理

#### 2.1.1 新增管理员
**接口**: `POST /admin/admin/add`

**请求体** (`AdminForm`):
```json
{
  "adminAccount": "string",      // 管理员账号 (必填, 7-10位)
  "adminUsername": "string",     // 管理员姓名 (必填, 最长15字符)
  "adminEmail": "string",        // 管理员邮箱 (必填, 最长25字符)
  "adminPhone": "string",        // 管理员手机 (必填, 11位)
  "adminPassword": "string",     // 管理员密码 (选填, 默认123456, 6-20位)
  "adminImage": "string",        // 管理员头像 (选填, 默认头像URL)
  "adminIntroduce": "string",    // 管理员介绍 (选填, 默认"这个人很懒...", 最长200字符)
  "adminState": 1                // 管理员状态 (选填, 默认1正常, 0禁用)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1  // 影响行数
}
```

---

#### 2.1.2 修改管理员
**接口**: `POST /admin/admin/update`

**请求体** (`AdminForm`):
```json
{
  "adminId": 2,                  // 管理员ID (必填)
  "adminAccount": "string",      // 管理员账号 (必填, 7-10位)
  "adminUsername": "string",     // 管理员姓名 (必填)
  "adminEmail": "string",        // 管理员邮箱 (必填)
  "adminPhone": "string",        // 管理员手机 (必填)
  "adminPassword": "string",     // 管理员密码 (必填)
  "adminImage": "string",        // 管理员头像 (必填)
  "adminIntroduce": "string",    // 管理员介绍 (必填)
  "adminState": 1                // 管理员状态 (必填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

**注意事项**:
- 超级管理员(ID=1)不能被修改
- 修改时所有字段均为必填

---

#### 2.1.3 删除管理员
**接口**: `DELETE /admin/admin/delete`

**请求参数**:
- `adminId` (required): Integer - 管理员ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

**注意事项**:
- 超级管理员(ID=1)不能被删除
- 删除操作为逻辑删除（软删除）

---

#### 2.1.4 查询管理员详情
**接口**: `GET /admin/admin/findById`

**请求参数**:
- `adminId` (required): Integer - 管理员ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "adminId": 1,
    "adminAccount": "admin001",
    "adminUsername": "超级管理员",
    "adminEmail": "admin@example.com",
    "adminPhone": "13800138000",
    "adminImage": "https://...",
    "adminIntroduce": "系统超级管理员",
    "adminState": 1,
    "adminLoginTime": "2025-12-07T10:00:00",
    "adminCreatedTime": "2025-01-01T00:00:00",
    "roles": [
      {
        "roleId": 1,
        "roleName": "超级管理员",
        "roleIntroduce": "拥有所有权限"
      }
    ]
  }
}
```

---

#### 2.1.5 分页查询管理员
**接口**: `POST /admin/admin/findByPage`

**请求参数**:
- `pageNum` (optional, default=1): Integer - 页码
- `pageSize` (optional, default=10): Integer - 每页条数

**请求体** (`AdminPageQuery`):
```json
{
  "adminUsername": "string",  // 管理员姓名 (模糊查询)
  "adminEmail": "string",     // 管理员邮箱 (模糊查询)
  "adminPhone": "string",     // 管理员手机 (模糊查询)
  "adminState": 1             // 管理员状态 (精确查询: 0禁用/1正常)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "records": [
      {
        "adminId": 2,
        "adminAccount": "admin002",
        "adminUsername": "张三",
        "adminEmail": "zhangsan@example.com",
        "adminPhone": "13900139000",
        "adminImage": "https://...",
        "adminIntroduce": "测试管理员",
        "adminState": 1,
        "adminLoginTime": "2025-12-07T09:00:00",
        "adminCreatedTime": "2025-06-01T00:00:00"
      }
    ],
    "total": 50,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

---

#### 2.1.6 查看管理员拥有的角色
**接口**: `GET /admin/admin/findRoleByAdmin`

**请求参数**:
- `adminId` (required): Integer - 管理员ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "roleId": 2,
      "roleName": "内容管理员",
      "roleIntroduce": "管理内容相关模块"
    },
    {
      "roleId": 3,
      "roleName": "数据审核员",
      "roleIntroduce": "审核数据提交"
    }
  ]
}
```

---

#### 2.1.7 为管理员分配角色
**接口**: `POST /admin/admin/assignRoleToAdmin`

**请求参数**:
- `adminId` (required): Integer - 管理员ID
- `roleIds` (required): Integer[] - 角色ID数组

**示例**: `/admin/admin/assignRoleToAdmin?adminId=2&roleIds=2&roleIds=3`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 2  // 分配成功的角色数量
}
```

---

#### 2.1.8 生成随机账号
**接口**: `GET /admin/admin/generateRandomAccount`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": "1234567890"  // 7-10位随机账号
}
```

**说明**: 自动生成7-10位不重复的随机账号，供新增管理员时使用

---

#### 2.1.9 获取当前登录管理员信息
**接口**: `POST /admin/admin/getAdminInfo`

**请求头**:
```
Authorization: Bearer <JWT Token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "userId": 1,
    "username": "超级管理员",
    "authorities": ["ROLE_SUPER_ADMIN"]
  }
}
```

---

### 2.2 角色管理

#### 2.2.1 新增角色
**接口**: `POST /admin/role/add`

**请求体** (`RoleForm`):
```json
{
  "roleName": "string",       // 角色名称 (必填, 最长12字符)
  "roleIntroduce": "string"   // 角色简介 (选填, 最长30字符)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 2.2.2 修改角色
**接口**: `POST /admin/role/update`

**请求体** (`RoleForm`):
```json
{
  "roleId": 2,                // 角色ID (必填)
  "roleName": "string",       // 角色名称 (必填)
  "roleIntroduce": "string"   // 角色简介 (必填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

**注意事项**:
- 超级管理员角色(ID=1)不能被修改

---

#### 2.2.3 删除角色
**接口**: `DELETE /admin/role/delete`

**请求参数**:
- `roleId` (required): Integer - 角色ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

**注意事项**:
- 超级管理员角色(ID=1)不能被删除

---

#### 2.2.4 查询角色详情
**接口**: `GET /admin/role/findById`

**请求参数**:
- `roleId` (required): Integer - 角色ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "roleId": 2,
    "roleName": "内容管理员",
    "roleIntroduce": "负责管理简历模板、求职攻略等内容模块",
    "permissions": [
      {
        "permissionId": 5,
        "permissionName": "简历模板管理",
        "permissionUrl": "/admin/resumeTemplate/**"
      }
    ]
  }
}
```

---

#### 2.2.5 分页查询角色
**接口**: `POST /admin/role/findByPage`

**请求参数**:
- `pageNum` (optional, default=1): Integer - 页码
- `pageSize` (optional, default=10): Integer - 每页条数

**请求体** (`RolePageQuery`):
```json
{
  "roleName": "string",       // 角色名称 (模糊查询)
  "roleIntroduce": "string"   // 角色简介 (模糊查询)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "records": [
      {
        "roleId": 2,
        "roleName": "内容管理员",
        "roleIntroduce": "负责管理简历模板、求职攻略等内容模块"
      }
    ],
    "total": 10,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

---

#### 2.2.6 查看角色拥有的权限
**接口**: `GET /admin/role/findPermissionByRole`

**请求参数**:
- `roleId` (required): Integer - 角色ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "permissionId": 5,
      "permissionName": "简历模板管理",
      "permissionUrl": "/admin/resumeTemplate/**",
      "permissionIntroduce": "管理简历模板的增删改查"
    },
    {
      "permissionId": 6,
      "permissionName": "求职攻略管理",
      "permissionUrl": "/admin/jobAdviceArticle/**",
      "permissionIntroduce": "管理求职攻略文章"
    }
  ]
}
```

---

#### 2.2.7 为角色分配权限
**接口**: `POST /admin/role/assignPermissionToRole`

**请求参数**:
- `roleId` (required): Integer - 角色ID
- `permissionIds` (required): Integer[] - 权限ID数组

**示例**: `/admin/role/assignPermissionToRole?roleId=2&permissionIds=5&permissionIds=6`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 2  // 分配成功的权限数量
}
```

---

### 2.3 权限管理

#### 2.3.1 新增权限
**接口**: `POST /admin/permission/add`

**请求体** (`PermissionForm`):
```json
{
  "permissionName": "string",       // 权限名称 (必填, 最长20字符)
  "permissionUrl": "string",        // 权限URL (必填, 最长50字符)
  "permissionIntroduce": "string"   // 权限简介 (选填, 最长30字符)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 2.3.2 修改权限
**接口**: `POST /admin/permission/update`

**请求体** (`PermissionForm`):
```json
{
  "permissionId": 10,               // 权限ID (必填)
  "permissionName": "string",       // 权限名称 (必填)
  "permissionUrl": "string",        // 权限URL (必填)
  "permissionIntroduce": "string"   // 权限简介 (必填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 2.3.3 删除权限
**接口**: `DELETE /admin/permission/delete`

**请求参数**:
- `permissionId` (required): Integer - 权限ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 2.3.4 查询权限详情
**接口**: `GET /admin/permission/findById`

**请求参数**:
- `permissionId` (required): Integer - 权限ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "permissionId": 10,
    "permissionName": "招聘信息管理",
    "permissionUrl": "/admin/employmentInformation/**",
    "permissionIntroduce": "管理企业招聘信息的增删改查"
  }
}
```

---

#### 2.3.5 分页查询权限
**接口**: `POST /admin/permission/findByPage`

**请求参数**:
- `pageNum` (optional, default=1): Integer - 页码
- `pageSize` (optional, default=10): Integer - 每页条数

**请求体** (`PermissionPageQuery`):
```json
{
  "permissionName": "string",       // 权限名称 (模糊查询)
  "permissionUrl": "string",        // 权限URL (模糊查询)
  "permissionIntroduce": "string"   // 权限简介 (模糊查询)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "records": [
      {
        "permissionId": 10,
        "permissionName": "招聘信息管理",
        "permissionUrl": "/admin/employmentInformation/**",
        "permissionIntroduce": "管理企业招聘信息的增删改查"
      }
    ],
    "total": 20,
    "size": 10,
    "current": 1,
    "pages": 2
  }
}
```

---

#### 2.3.6 查询所有权限
**接口**: `GET /admin/permission/findAll`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "permissionId": 1,
      "permissionName": "管理员管理",
      "permissionUrl": "/admin/admin/**",
      "permissionIntroduce": "管理员增删改查"
    },
    {
      "permissionId": 2,
      "permissionName": "角色管理",
      "permissionUrl": "/admin/role/**",
      "permissionIntroduce": "角色增删改查"
    }
  ]
}
```

---

## 3. 内容管理

### 3.1 简历模板管理

#### 3.1.1 新增简历模板
**接口**: `POST /admin/resumeTemplate/addResumeTemplate`

**请求体** (`ResumeTemplateForm`):
```json
{
  "resumeTemplateName": "string",       // 模板名称 (必填, 最长25字符)
  "resumeTemplateReactCode": "string",  // React代码 (必填)
  "resumeTemplateIndustry": 1,          // 行业代码 (必填)
  "resumeTemplateIsActive": 1           // 是否激活 (选填, 0禁用/1启用)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.1.2 修改简历模板
**接口**: `POST /admin/resumeTemplate/updateResumeTemplate`

**请求体** (`ResumeTemplateForm`):
```json
{
  "resumeTemplateId": 5,                // 模板ID (必填)
  "resumeTemplateName": "string",       // 模板名称 (必填)
  "resumeTemplateReactCode": "string",  // React代码 (必填)
  "resumeTemplateIndustry": 1,          // 行业代码 (必填)
  "resumeTemplateIsActive": 1           // 是否激活 (必填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.1.3 删除简历模板
**接口**: `DELETE /admin/resumeTemplate/deleteResumeTemplate`

**请求参数**:
- `resumeTemplateId` (required): Integer - 模板ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.1.4 查询简历模板详情
**接口**: `GET /admin/resumeTemplate/findResumeTemplateById`

**请求参数**:
- `resumeTemplateId` (required): Integer - 模板ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "resumeTemplateId": 5,
    "resumeTemplateName": "互联网产品经理模板",
    "resumeTemplateReactCode": "import React from 'react'...",
    "resumeTemplateIndustry": 101,
    "resumeTemplateIndustryName": "互联网",
    "resumeTemplateIsActive": 1
  }
}
```

---

#### 3.1.5 分页查询简历模板
**接口**: `POST /admin/resumeTemplate/findResumeTemplateByPage`

**请求参数**:
- `pageNum` (required, default=1): Integer - 页码
- `pageSize` (required, default=10): Integer - 每页条数

**请求体** (`ResumeTemplateQuery`):
```json
{
  "resumeTemplateName": "string",  // 模板名称 (模糊查询)
  "resumeTemplateIndustry": 101    // 行业代码 (精确查询, 必填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "records": [
      {
        "resumeTemplateId": 5,
        "resumeTemplateName": "互联网产品经理模板",
        "resumeTemplateIndustry": 101,
        "resumeTemplateIndustryName": "互联网",
        "resumeTemplateIsActive": 1
      }
    ],
    "total": 25,
    "size": 10,
    "current": 1,
    "pages": 3
  }
}
```

---

#### 3.1.6 查询所有简历模板
**接口**: `GET /admin/resumeTemplate/findAllResumeTemplate`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "resumeTemplateId": 1,
      "resumeTemplateName": "经典商务模板",
      "resumeTemplateIndustry": 101,
      "resumeTemplateIndustryName": "互联网",
      "resumeTemplateIsActive": 1
    }
  ]
}
```

---

### 3.2 求职攻略管理

#### 3.2.1 新增求职攻略
**接口**: `POST /admin/jobAdviceArticle/addJobAdviceArticle`

**请求体** (`JobAdviceArticleForm`):
```json
{
  "jobAdviceArticleTitle": "string",            // 文章标题 (必填, 最长30字符)
  "jobAdviceArticleContent": "string",          // 文章内容 (选填)
  "jobAdviceArticleCategory": "string",         // 文章分类 (选填, 最长30字符)
  "jobAdviceArticleTags": "string",             // 文章标签 (选填, 最长30字符)
  "jobAdviceArticleAuthorName": "string",       // 作者名称 (选填, 最长30字符)
  "jobAdviceArticlePublishedStatus": 1          // 发布状态 (选填, 0草稿/1已发布)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.2.2 修改求职攻略
**接口**: `POST /admin/jobAdviceArticle/updateJobAdviceArticle`

**请求体** (`JobAdviceArticleForm`):
```json
{
  "jobAdviceArticleId": 10,                     // 文章ID (必填)
  "jobAdviceArticleTitle": "string",            // 文章标题 (必填)
  "jobAdviceArticleContent": "string",          // 文章内容 (选填)
  "jobAdviceArticleCategory": "string",         // 文章分类 (选填)
  "jobAdviceArticleTags": "string",             // 文章标签 (选填)
  "jobAdviceArticleAuthorName": "string",       // 作者名称 (选填)
  "jobAdviceArticlePublishedStatus": 1          // 发布状态 (选填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.2.3 删除求职攻略
**接口**: `DELETE /admin/jobAdviceArticle/deleteJobAdviceArticle`

**请求参数**:
- `jobAdviceArticleId` (required): Integer - 文章ID

**响应示例**:
```json
1  // 直接返回Integer
```

---

#### 3.2.4 查询求职攻略详情
**接口**: `GET /admin/jobAdviceArticle/getJobAdviceArticleInfo`

**请求参数**:
- `jobAdviceArticleId` (required): Integer - 文章ID

**响应示例**:
```json
{
  "jobAdviceArticleId": 10,
  "jobAdviceArticleTitle": "如何准备技术面试",
  "jobAdviceArticleContent": "本文介绍技术面试的准备方法...",
  "jobAdviceArticleCategory": "面试技巧",
  "jobAdviceArticleTags": "面试,技术,准备",
  "jobAdviceArticleAuthorName": "李老师",
  "jobAdviceArticlePublishedStatus": 1,
  "jobAdviceArticleCreatedTime": "2025-11-01T10:00:00",
  "jobAdviceArticleUpdatedTime": "2025-12-01T15:00:00"
}
```

---

#### 3.2.5 分页查询求职攻略
**接口**: `POST /admin/jobAdviceArticle/getJobAdviceArticlePage`

**请求参数**:
- `size` (optional, default=10): int - 每页条数
- `page` (optional, default=1): int - 页码

**请求体** (`JobAdviceArticleQuery`):
```json
{
  "jobAdviceArticleTitle": "string",        // 文章标题 (必填, 模糊查询)
  "jobAdviceArticleContent": "string",      // 文章内容 (模糊查询)
  "jobAdviceArticleCategory": "string",     // 文章分类 (模糊查询)
  "jobAdviceArticleTags": "string",         // 文章标签 (模糊查询)
  "jobAdviceArticleAuthorName": "string"    // 作者名称 (模糊查询)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "jobAdviceArticleId": 10,
      "jobAdviceArticleTitle": "如何准备技术面试",
      "jobAdviceArticleCategory": "面试技巧",
      "jobAdviceArticleTags": "面试,技术",
      "jobAdviceArticleAuthorName": "李老师",
      "jobAdviceArticlePublishedStatus": 1,
      "jobAdviceArticleCreatedTime": "2025-11-01T10:00:00"
    }
  ]
}
```

---

#### 3.2.6 查询所有求职攻略
**接口**: `GET /admin/jobAdviceArticle/getAllJobAdviceArticle`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "jobAdviceArticleId": 1,
      "jobAdviceArticleTitle": "简历撰写技巧",
      "jobAdviceArticleCategory": "简历优化",
      "jobAdviceArticlePublishedStatus": 1
    }
  ]
}
```

---

### 3.3 招聘岗位管理

#### 3.3.1 新增招聘岗位
**接口**: `POST /admin/recruitPosition/addRecruitPosition`

**请求体** (`RecruitPositionForm`):
```json
{
  "recruitPositionName": "string",          // 岗位名称 (必填, 最长30字符)
  "recruitPositionIndustryCode": 101,       // 行业代码 (必填)
  "minMonthSalary": 8000.00,                // 最低月薪 (必填, 单位:元)
  "maxMonthSalary": 15000.00,               // 最高月薪 (必填, 单位:元)
  "weekWorkDayNum": 5,                      // 每周工作天数 (必填)
  "goodWelfare": "string"                   // 福利待遇 (选填, 最长200字符)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.3.2 修改招聘岗位
**接口**: `POST /admin/recruitPosition/updateRecruitPosition`

**请求体** (`RecruitPositionForm`):
```json
{
  "recruitPositionId": 15,                  // 岗位ID (必填)
  "recruitPositionName": "string",          // 岗位名称 (必填)
  "recruitPositionIndustryCode": 101,       // 行业代码 (必填)
  "minMonthSalary": 8000.00,                // 最低月薪 (必填)
  "maxMonthSalary": 15000.00,               // 最高月薪 (必填)
  "weekWorkDayNum": 5,                      // 每周工作天数 (必填)
  "goodWelfare": "string"                   // 福利待遇 (选填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.3.3 删除招聘岗位
**接口**: `DELETE /admin/recruitPosition/deleteRecruitPosition`

**请求参数**:
- `recruitPositionId` (required): Integer - 岗位ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

**注意事项**:
- 如果该岗位正在被招聘信息使用，将无法删除

---

#### 3.3.4 查询招聘岗位详情
**接口**: `GET /admin/recruitPosition/queryRecruitPosition`

**请求参数**:
- `recruitPositionId` (required): Integer - 岗位ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "recruitPositionId": 15,
    "recruitPositionName": "Java开发工程师",
    "recruitPositionIndustryCode": 101,
    "recruitPositionIndustryName": "互联网",
    "minMonthSalary": 10000.00,
    "maxMonthSalary": 18000.00,
    "weekWorkDayNum": 5,
    "goodWelfare": "五险一金,年终奖,带薪年假"
  }
}
```

---

#### 3.3.5 分页查询招聘岗位
**接口**: `POST /admin/recruitPosition/queryRecruitPositionPage`

**请求参数**:
- `pageNum` (optional, default=1): Integer - 页码
- `pageSize` (optional, default=10): Integer - 每页条数

**请求体** (`RecruitPositionQuery`):
```json
{
  "recruitPositionName": "string",          // 岗位名称 (模糊查询)
  "recruitPositionIndustryCode": 101,       // 行业代码 (精确查询)
  "minMonthSalary": 8000.00,                // 最低月薪 (范围查询)
  "maxMonthSalary": 15000.00,               // 最高月薪 (范围查询)
  "weekWorkDayNum": 5                       // 每周工作天数 (精确查询)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "records": [
      {
        "recruitPositionId": 15,
        "recruitPositionName": "Java开发工程师",
        "recruitPositionIndustryCode": 101,
        "recruitPositionIndustryName": "互联网",
        "minMonthSalary": 10000.00,
        "maxMonthSalary": 18000.00,
        "weekWorkDayNum": 5,
        "goodWelfare": "五险一金,年终奖"
      }
    ],
    "total": 50,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

---

#### 3.3.6 查询所有招聘岗位
**接口**: `GET /admin/recruitPosition/queryAllRecruitPositionPage`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "recruitPositionId": 1,
      "recruitPositionName": "Java开发工程师",
      "recruitPositionIndustryName": "互联网",
      "minMonthSalary": 10000.00,
      "maxMonthSalary": 18000.00
    }
  ]
}
```

---

### 3.4 招聘信息管理

#### 3.4.1 新增招聘信息
**接口**: `POST /admin/employmentInformation/addEmploymentInformation`

**请求体** (`EmploymentInformationForm`):
```json
{
  "employmentInformationCompanyName": "string",                     // 公司名称 (必填, 最长30字符)
  "employmentInformationIndustryCategories": 101,                   // 行业大类 (必填)
  "employmentInformationCompanyType": 1,                            // 企业性质 (必填)
  "employmentInformationBatch": 2025,                               // 招聘批次 (必填)
  "employmentInformationRecruitPosition": 15,                       // 招聘岗位ID (必填)
  "employmentInformationRecruitObject": 1,                          // 招聘对象 (必填)
  "employmentInformationRecruitLocationFirst": [11, 31],            // 招聘地址(省级) (必填, List<Integer>)
  "employmentInformationRecruitLocationSecond": [1101, 3101],       // 招聘地址(市级) (必填, List<Integer>)
  "employmentInformationRecruitLocationDetail": "string",           // 详细地址 (选填, 最长255字符)
  "employmentInformationStopTime": "2025-12-31T23:59:59",           // 截止时间 (必填)
  "employmentInformationOnlineApplicationStatus": "string",         // 网申状态 (必填, 最长30字符)
  "employmentInformationOfficialAnnouncement": "string",            // 官方公告 (选填, 最长1024字符)
  "employmentInformationSubmissionWay": "string",                   // 投递方式 (必填, 最长1024字符)
  "employmentInformationEmployeeReferralCode": "string"             // 内推码 (选填, 最长255字符)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

**注意事项**:
- `employmentInformationRecruitLocationFirst` 和 `employmentInformationRecruitLocationSecond` 长度必须一致
- 省级和市级地址需一一对应

---

#### 3.4.2 修改招聘信息
**接口**: `POST /admin/employmentInformation/updateEmploymentInformation`

**请求体** (`EmploymentInformationForm`):
```json
{
  "employmentInformationId": 20,                                    // 招聘信息ID (必填)
  "employmentInformationCompanyName": "string",                     // 公司名称 (必填)
  "employmentInformationIndustryCategories": 101,                   // 行业大类 (必填)
  "employmentInformationCompanyType": 1,                            // 企业性质 (必填)
  "employmentInformationBatch": 2025,                               // 招聘批次 (必填)
  "employmentInformationRecruitPosition": 15,                       // 招聘岗位ID (必填)
  "employmentInformationRecruitObject": 1,                          // 招聘对象 (必填)
  "employmentInformationRecruitLocationFirst": [11, 31],            // 招聘地址(省级) (必填)
  "employmentInformationRecruitLocationSecond": [1101, 3101],       // 招聘地址(市级) (必填)
  "employmentInformationRecruitLocationDetail": "string",           // 详细地址 (选填)
  "employmentInformationStopTime": "2025-12-31T23:59:59",           // 截止时间 (必填)
  "employmentInformationOnlineApplicationStatus": "string",         // 网申状态 (必填)
  "employmentInformationOfficialAnnouncement": "string",            // 官方公告 (选填)
  "employmentInformationSubmissionWay": "string",                   // 投递方式 (必填)
  "employmentInformationEmployeeReferralCode": "string"             // 内推码 (选填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.4.3 删除招聘信息
**接口**: `DELETE /admin/employmentInformation/deleteEmploymentInformation`

**请求体** (`EmploymentInformationForm`):
```json
{
  "employmentInformationId": 20  // 招聘信息ID
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 3.4.4 查询招聘信息详情
**接口**: `GET /admin/employmentInformation/getEmploymentInformationInfo`

**请求参数**:
- `employmentInformationId` (required): Integer - 招聘信息ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "employmentInformationId": 20,
    "employmentInformationCode": 20250001,
    "employmentInformationCompanyName": "阿里巴巴",
    "employmentInformationIndustryCategories": 101,
    "employmentInformationIndustryCategoriesName": "互联网",
    "employmentInformationCompanyType": 1,
    "employmentInformationCompanyTypeName": "国有企业",
    "employmentInformationBatch": 2025,
    "employmentInformationRecruitPosition": 15,
    "employmentInformationRecruitPositionName": "Java开发工程师",
    "employmentInformationRecruitObject": 1,
    "employmentInformationRecruitObjectName": "应届生",
    "employmentInformationRecruitLocationFirst": [11, 31],
    "employmentInformationRecruitLocationFirstNames": ["北京市", "上海市"],
    "employmentInformationRecruitLocationSecond": [1101, 3101],
    "employmentInformationRecruitLocationSecondNames": ["北京市", "上海市"],
    "employmentInformationRecruitLocationDetail": "朝阳区望京SOHO",
    "employmentInformationStopTime": "2025-12-31T23:59:59",
    "employmentInformationOnlineApplicationStatus": "开放中",
    "employmentInformationOfficialAnnouncement": "欢迎投递简历...",
    "employmentInformationSubmissionWay": "官网投递",
    "employmentInformationEmployeeReferralCode": "REF2025001"
  }
}
```

---

#### 3.4.5 分页查询招聘信息
**接口**: `POST /admin/employmentInformation/getEmploymentInformationPage`

**请求参数**:
- `pageNum` (required): Integer - 页码
- `pageSize` (required): Integer - 每页条数

**请求体** (`EmploymentInformationQuery`):
```json
{
  "employmentInformationCompanyName": "string",                     // 公司名称 (模糊查询)
  "employmentInformationIndustryCategories": 101,                   // 行业大类 (精确查询)
  "employmentInformationCompanyType": 1,                            // 企业性质 (精确查询)
  "employmentInformationBatch": 2025,                               // 招聘批次 (精确查询)
  "employmentInformationRecruitPosition": 15,                       // 招聘岗位 (精确查询)
  "employmentInformationRecruitObject": 1,                          // 招聘对象 (精确查询)
  "employmentInformationRecruitLocationFirst": 11,                  // 招聘地址(省级) (精确查询)
  "employmentInformationRecruitLocationSecond": 1101,               // 招聘地址(市级) (精确查询)
  "employmentInformationRecruitLocationDetail": "string",           // 详细地址 (模糊查询)
  "employmentInformationStopTime": "2025-12-31T23:59:59",           // 截止时间 (小于等于查询)
  "employmentInformationOnlineApplicationStatus": "string",         // 网申状态 (模糊查询)
  "employmentInformationOfficialAnnouncement": "string",            // 官方公告 (模糊查询)
  "employmentInformationSubmissionWay": "string",                   // 投递方式 (模糊查询)
  "employmentInformationEmployeeReferralCode": "string"             // 内推码 (模糊查询)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "records": [
      {
        "employmentInformationId": 20,
        "employmentInformationCompanyName": "阿里巴巴",
        "employmentInformationIndustryCategoriesName": "互联网",
        "employmentInformationRecruitPositionName": "Java开发工程师",
        "employmentInformationRecruitLocationFirstNames": ["北京市"],
        "employmentInformationStopTime": "2025-12-31T23:59:59",
        "employmentInformationOnlineApplicationStatus": "开放中"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

---

#### 3.4.6 查询所有招聘信息
**接口**: `GET /admin/employmentInformation/getAllEmploymentInformation`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "employmentInformationId": 1,
      "employmentInformationCompanyName": "阿里巴巴",
      "employmentInformationRecruitPositionName": "Java开发工程师",
      "employmentInformationStopTime": "2025-12-31T23:59:59"
    }
  ]
}
```

---

## 4. 数据配置

### 4.1 行业配置

#### 4.1.1 新增行业
**接口**: `POST /admin/industryMap/addIndustryMap`

**请求体** (`IndustryMapForm`):
```json
{
  "industryMapIndustryCode": 201,       // 行业代码 (主键)
  "industryMapIndustryName": "string"   // 行业名称 (必填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 4.1.2 修改行业
**接口**: `POST /admin/industryMap/updateIndustryMap`

**请求体** (`IndustryMapForm`):
```json
{
  "industryMapIndustryCode": 201,       // 行业代码 (必填)
  "industryMapIndustryName": "string"   // 行业名称 (必填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": 1
}
```

---

#### 4.1.3 查询行业详情
**接口**: `GET /admin/industryMap/findIndustryMapById`

**请求参数**:
- `industryMapId` (required): Integer - 行业ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "industryMapIndustryCode": 201,
    "industryMapIndustryName": "金融"
  }
}
```

---

#### 4.1.4 分页查询行业
**接口**: `POST /admin/industryMap/findIndustryMapByPage`

**请求参数**:
- `pageNum` (optional, default=1): Integer - 页码
- `pageSize` (optional, default=10): Integer - 每页条数

**请求体** (`IndustryMapQuery`):
```json
{
  "industryMapIndustryCode": 201,       // 行业代码 (精确查询)
  "industryMapIndustryName": "string"   // 行业名称 (模糊查询)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "records": [
      {
        "industryMapIndustryCode": 201,
        "industryMapIndustryName": "金融"
      }
    ],
    "total": 20,
    "size": 10,
    "current": 1,
    "pages": 2
  }
}
```

---

#### 4.1.5 查询所有行业
**接口**: `GET /admin/industryMap/findAllIndustryMap`

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "industryMapIndustryCode": 101,
      "industryMapIndustryName": "互联网"
    },
    {
      "industryMapIndustryCode": 201,
      "industryMapIndustryName": "金融"
    }
  ]
}
```

---

### 4.2 地区配置

#### 4.2.1 查询所有省份
**接口**: `GET /admin/provinceMap/getAllProvince`

**响应示例**:
```json
[
  {
    "provinceMapId": 11,
    "provinceMapName": "北京市"
  },
  {
    "provinceMapId": 31,
    "provinceMapName": "上海市"
  }
]
```

---

#### 4.2.2 根据省份查询城市
**接口**: `GET /admin/provinceMap/getCityByProvinceId`

**请求参数**:
- `provinceMapId` (required): Integer - 省份ID

**响应示例**:
```json
[
  {
    "cityMapId": 1101,
    "cityMapName": "市辖区",
    "cityMapProvinceId": 11
  }
]
```

---

## 5. 通信工具

### 5.1 沟通邮件发送

#### 5.1.1 发送纯文本邮件（指定发送者）
**接口**: `POST /admin/email/communication/selfde/sendText`

**请求参数**:
- `fromEmail` (optional): string - 发送者邮箱
- `toEmail` (required): string - 接收者邮箱
- `subject` (required): string - 邮件主题
- `content` (required): string - 邮件内容

**响应**: 无返回值 (void)

---

#### 5.1.2 发送纯文本邮件（使用默认发送者）
**接口**: `POST /admin/email/communication/usallyde/sendText`

**请求参数**:
- `toEmail` (required): string - 接收者邮箱
- `subject` (required): string - 邮件主题
- `content` (required): string - 邮件内容

**响应**: 无返回值 (void)

---

#### 5.1.3 发送HTML邮件（指定发送者）
**接口**: `POST /admin/email/communication/selfde/sendHtml`

**请求参数**:
- `fromEmail` (optional): string - 发送者邮箱
- `toEmail` (required): string - 接收者邮箱
- `subject` (required): string - 邮件主题
- `htmlContent` (required): string - HTML内容

**响应**: 无返回值 (void)

---

#### 5.1.4 发送HTML邮件（使用默认发送者）
**接口**: `POST /admin/email/communication/usallyde/sendHtml`

**请求参数**:
- `toEmail` (required): string - 接收者邮箱
- `subject` (required): string - 邮件主题
- `htmlContent` (required): string - HTML内容

**响应**: 无返回值 (void)

---

## 6. AI助手

### 6.1 AI系统管理助手

#### 6.1.1 应用对话（流式）
**接口**: `POST /admin/aiSystemManagerAssistant/application/chat`

**Content-Type**: `text/event-stream` (SSE流式响应)

**请求体**:
```json
"string"  // 用户消息内容
```

**请求参数**:
- `chatId` (optional): string - 会话ID（如不提供会自动生成UUID）

**响应示例** (SSE流):
```
data: {"code":200,"message":"OK","data":"Flux<String>"}
```

---

#### 6.1.2 Agent对话（流式）
**接口**: `POST /admin/aiSystemManagerAssistant/agent/chat`

**请求体**:
```json
"string"  // 用户消息内容
```

**请求参数**:
- `chatId` (optional): string - 会话ID（如不提供会自动生成UUID）

**响应示例** (SseEmitter):
```json
{
  "code": 200,
  "message": "OK",
  "data": "SseEmitter对象"
}
```

---

## 7. 反馈管理

### 7.1 管理员反馈

#### 7.1.1 添加反馈
**接口**: `POST /admin/feedback/addFeedback`

**请求体** (`AdminFeedbackForm`):
```json
{
  "adminFeedbackTitle": "string",       // 反馈标题 (最长35字符)
  "adminFeedbackContent": "string",     // 反馈内容
  "adminFeedbackAdminId": 5             // 提交反馈的管理员ID
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK"
}
```

---

#### 7.1.2 更新反馈阶段
**接口**: `PUT /admin/feedback/updateFeedbackStep`

**请求参数**:
- `feedbackId` (required): Integer - 反馈ID
- `OperationCode` (required): Integer - 操作码
  - 0: 接受反馈
  - 1: 忽视反馈
  - 2: 回复反馈
  - 3: 拒绝回复
- `operationPersonId` (required): Integer - 操作人ID

**请求体** (`AdminUpdateFeedbackForm`):
```json
{
  "title": "string",    // 回复标题 (操作码2时使用)
  "content": "string"   // 回复内容 (操作码2时使用)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK"
}
```

**流程说明**:
- 操作码0: 接受反馈 → 发送邮件通知用户 → 状态变更为"待回复"
- 操作码1: 忽视反馈 → 状态变更为"已忽视"
- 操作码2: 回复反馈 → 发送HTML邮件 → 状态变更为"已回复"
- 操作码3: 拒绝回复 → 状态变更为"拒绝回复"

---

#### 7.1.3 查询反馈详情
**接口**: `GET /admin/feedback/findFeedbackById`

**请求参数**:
- `feedbackId` (required): Integer - 反馈ID

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "adminFeedbackId": 10,
    "adminFeedbackTitle": "系统建议",
    "adminFeedbackContent": "建议增加批量导入功能...",
    "adminFeedbackTime": "2025-12-01T10:00:00",
    "adminFeedbackRecentTime": "2025-12-07T15:00:00",
    "adminFeedbackCurStep": "已回复",
    "adminFeedbackAdminId": 5,
    "adminFeedbackAdminName": "张三"
  }
}
```

---

#### 7.1.4 分页查询反馈
**接口**: `POST /admin/feedback/getFeedbackPage`

**请求参数**:
- `size` (required): Integer - 每页条数
- `page` (required): Integer - 页码

**请求体** (`AdminFeedbackQuery`):
```json
{
  "adminFeedbackTitle": "string",       // 反馈标题 (模糊查询)
  "adminFeedbackContent": "string"      // 反馈内容 (模糊查询)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "records": [
      {
        "adminFeedbackId": 10,
        "adminFeedbackTitle": "系统建议",
        "adminFeedbackContent": "建议增加批量导入功能...",
        "adminFeedbackTime": "2025-12-01T10:00:00",
        "adminFeedbackRecentTime": "2025-12-07T15:00:00",
        "adminFeedbackCurStep": "已回复",
        "adminFeedbackAdminId": 5,
        "adminFeedbackAdminName": "张三"
      }
    ],
    "total": 45,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

---

## 附录

### A. 统一响应格式

所有接口均采用统一的响应格式（除特殊说明外）：

```json
{
  "code": 200,           // 状态码: 200成功, 其他失败
  "message": "OK",       // 消息描述
  "data": {}             // 响应数据
}
```

---

### B. 分页参数说明

- **pageNum**: 页码，从1开始
- **pageSize**: 每页条数，默认10条

**分页响应格式**:
```json
{
  "records": [],    // 当前页数据列表
  "total": 100,     // 总记录数
  "size": 10,       // 每页条数
  "current": 1,     // 当前页
  "pages": 10       // 总页数
}
```

---

### C. 日期时间格式

所有日期时间字段统一采用ISO 8601格式：`yyyy-MM-ddTHH:mm:ss`

示例：`2025-12-07T15:30:00`

---

### D. 错误码说明

详见 `AdminCodeEnum` 枚举类，主要错误码范围：

- **603-629**: 管理员相关错误
- **630-639**: 角色相关错误
- **640-650**: 权限相关错误
- **655-659**: 简历模板相关错误
- **670-677**: 求职攻略相关错误
- **685-693**: 招聘岗位相关错误
- **705-727**: 招聘信息相关错误
- **750-759**: 邮箱验证相关错误
- **770-774**: 短信验证相关错误
- **785-793**: 登录认证相关错误

---

### E. 接口调用流程图

#### E.1 登录流程
```
1. 前端选择登录方式
   ├─ 账号密码登录 → POST /admin/auth/formalLogin
   ├─ 手机验证码登录
   │  ├─ 发送验证码 → POST /user/sms/send
   │  ├─ 校验验证码 → POST /user/sms/check
   │  └─ 登录 → POST /admin/auth/phoneLogin
   └─ 邮箱验证码登录
      ├─ 发送验证码 → POST /admin/email/loginandregister/send
      ├─ 校验验证码 → POST /admin/email/loginandregister/check
      └─ 登录 → POST /admin/auth/emailLogin

2. 后端返回JWT Token
3. 前端存储Token，后续请求携带Token
4. 获取当前用户信息 → POST /admin/admin/getAdminInfo
```

#### E.2 权限管理流程
```
1. 管理员管理
   ├─ 查询管理员列表 → POST /admin/admin/findByPage
   ├─ 新增管理员
   │  ├─ 生成随机账号 → GET /admin/admin/generateRandomAccount
   │  └─ 提交新增 → POST /admin/admin/add
   ├─ 修改管理员 → POST /admin/admin/update
   ├─ 查看管理员角色 → GET /admin/admin/findRoleByAdmin
   ├─ 分配角色 → POST /admin/admin/assignRoleToAdmin
   └─ 删除管理员 → DELETE /admin/admin/delete

2. 角色管理
   ├─ 查询角色列表 → POST /admin/role/findByPage
   ├─ 新增角色 → POST /admin/role/add
   ├─ 修改角色 → POST /admin/role/update
   ├─ 查看角色权限 → GET /admin/role/findPermissionByRole
   ├─ 分配权限 → POST /admin/role/assignPermissionToRole
   └─ 删除角色 → DELETE /admin/role/delete

3. 权限管理
   ├─ 查询所有权限 → GET /admin/permission/findAll
   ├─ 新增权限 → POST /admin/permission/add
   ├─ 修改权限 → POST /admin/permission/update
   └─ 删除权限 → DELETE /admin/permission/delete
```

#### E.3 内容管理流程
```
1. 简历模板
   ├─ 查询模板列表 → POST /admin/resumeTemplate/findResumeTemplateByPage
   ├─ 新增模板 → POST /admin/resumeTemplate/addResumeTemplate
   ├─ 修改模板 → POST /admin/resumeTemplate/updateResumeTemplate
   └─ 删除模板 → DELETE /admin/resumeTemplate/deleteResumeTemplate

2. 求职攻略
   ├─ 查询文章列表 → POST /admin/jobAdviceArticle/getJobAdviceArticlePage
   ├─ 新增文章 → POST /admin/jobAdviceArticle/addJobAdviceArticle
   ├─ 修改文章 → POST /admin/jobAdviceArticle/updateJobAdviceArticle
   └─ 删除文章 → DELETE /admin/jobAdviceArticle/deleteJobAdviceArticle

3. 招聘岗位
   ├─ 查询岗位列表 → POST /admin/recruitPosition/queryRecruitPositionPage
   ├─ 新增岗位 → POST /admin/recruitPosition/addRecruitPosition
   ├─ 修改岗位 → POST /admin/recruitPosition/updateRecruitPosition
   └─ 删除岗位 → DELETE /admin/recruitPosition/deleteRecruitPosition

4. 招聘信息
   ├─ 查询招聘信息列表 → POST /admin/employmentInformation/getEmploymentInformationPage
   ├─ 获取行业列表 → GET /admin/industryMap/findAllIndustryMap
   ├─ 获取省份列表 → GET /admin/provinceMap/getAllProvince
   ├─ 根据省份获取城市 → GET /admin/provinceMap/getCityByProvinceId
   ├─ 新增招聘信息 → POST /admin/employmentInformation/addEmploymentInformation
   ├─ 修改招聘信息 → POST /admin/employmentInformation/updateEmploymentInformation
   └─ 删除招聘信息 → DELETE /admin/employmentInformation/deleteEmploymentInformation
```

#### E.4 反馈处理流程
```
1. 管理员提交反馈 → POST /admin/feedback/addFeedback
2. 查询反馈列表 → POST /admin/feedback/getFeedbackPage
3. 查看反馈详情 → GET /admin/feedback/findFeedbackById
4. 处理反馈
   ├─ 接受反馈 → PUT /admin/feedback/updateFeedbackStep?OperationCode=0
   ├─ 忽视反馈 → PUT /admin/feedback/updateFeedbackStep?OperationCode=1
   ├─ 回复反馈 → PUT /admin/feedback/updateFeedbackStep?OperationCode=2
   └─ 拒绝回复 → PUT /admin/feedback/updateFeedbackStep?OperationCode=3
```

---

### F. 前端对接建议

#### F.1 Token管理
```javascript
// 登录后存储Token
localStorage.setItem('admin_token', response.data);

// 请求拦截器添加Token
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('admin_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

#### F.2 错误处理
```javascript
// 响应拦截器统一处理错误
axios.interceptors.response.use(
  response => response.data,
  error => {
    const { code, message } = error.response.data;
    // 根据错误码提示用户
    if (code === 785) {
      alert('账号或密码错误');
    }
    return Promise.reject(error);
  }
);
```

#### F.3 分页组件参数映射
```javascript
// Element UI Pagination组件
<el-pagination
  :current-page="pageNum"
  :page-size="pageSize"
  :total="total"
  @current-change="handlePageChange"
/>

// Ant Design Pagination组件
<a-pagination
  :current="pageNum"
  :pageSize="pageSize"
  :total="total"
  @change="handlePageChange"
/>
```

---

### G. 接口总览表

| 模块 | 接口数量 | 说明 |
|------|---------|------|
| 登录认证 | 7 | 账号/手机/邮箱登录 + 验证码发送/校验 |
| 管理员管理 | 9 | 管理员CRUD + 角色分配 + 账号生成 |
| 角色管理 | 7 | 角色CRUD + 权限分配 |
| 权限管理 | 6 | 权限CRUD + 查询所有 |
| 简历模板 | 6 | 模板CRUD + 分页查询 + 查询所有 |
| 求职攻略 | 6 | 文章CRUD + 分页查询 + 查询所有 |
| 招聘岗位 | 6 | 岗位CRUD + 分页查询 + 查询所有 |
| 招聘信息 | 6 | 招聘信息CRUD + 分页查询 + 查询所有 |
| 行业配置 | 5 | 行业CRUD + 分页查询 + 查询所有 |
| 地区配置 | 2 | 查询省份 + 根据省份查询城市 |
| 邮件发送 | 4 | 文本/HTML邮件发送（指定/默认发送者）|
| AI助手 | 2 | 应用对话 + Agent对话 |
| 反馈管理 | 4 | 反馈添加 + 状态更新 + 查询详情 + 分页查询 |
| **总计** | **70** | **完整管理端接口** |

---

## 更新日志

### v1.7.0 (2025-12-07)
- ✅ 完成管理端所有接口文档编写
- ✅ 新增管理员账号字段（7-10位）
- ✅ 新增管理员反馈管理模块
- ✅ 优化接口分类和流程说明
- ✅ 新增前端对接建议和错误处理示例

---

**文档编写**: Claude (Anthropic AI)  
**技术支持**: shiningCloud2025  
**最后更新**: 2025-12-07 16:00:00

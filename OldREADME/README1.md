<p align="center">
  <h1 align="center">🚀 EasyApplyResume 易投简历</h1>
  <p align="center">一站式智能简历管理与投递平台</p>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk" alt="Java">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=spring-boot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-3.x-4FC08D?style=flat-square&logo=vue.js" alt="Vue">
  <img src="https://img.shields.io/badge/React-18.x-61DAFB?style=flat-square&logo=react" alt="React">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Redis-7.2-DC382D?style=flat-square&logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/License-MIT-blue?style=flat-square" alt="License">
</p>

---

## 📖 目录

- [项目介绍](#-项目介绍)
- [技术架构](#-技术架构)
- [功能特性](#-功能特性)
- [系统截图](#-系统截图)
- [快速开始](#-快速开始)
- [项目结构](#-项目结构)
- [开发进度](#-开发进度)
- [参与贡献](#-参与贡献)
- [开源协议](#-开源协议)

---

## 🎯 项目介绍

**EasyApplyResume（易投简历）** 是一个面向求职者的智能简历管理与投递平台，采用前后端分离的主流架构设计。

本项目是作者的毕业设计作品，同时也是作者一直想做的开源项目。项目包含三个子系统：

| 子系统 | 面向用户 | 核心功能 |
|--------|----------|----------|
| **用户端** | 求职者 | 简历模板、制作简历、投递简历、AI求职助手 |
| **管理端** | 管理员 | 用户管理、简历审核、文章管理、反馈处理 |
| **监测与广告端** | 运维人员 | 数据统计、广告管理、服务器监控 |

---

## 🛠 技术架构

### 整体架构

```
┌─────────────────────────────────────────────────────────────────┐
│                          前端层                                  │
│   用户端(React)    │    管理端(Vue3)    │    监测端(Vue3)        │
└─────────────────────────────┬───────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────────┐
│                        Nginx 网关层                              │
│                    路由分发 · 负载均衡 · 静态资源                  │
└─────────────────────────────┬───────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────────┐
│                      Spring Boot 3 后端                          │
│     Spring Security + JWT · MyBatis-Plus · Spring AI            │
└─────────────────────────────┬───────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────────┐
│                         数据层                                   │
│       MySQL 8.0  ·  Redis 7.2  ·  MinIO/七牛云  ·  PGVector     │
└─────────────────────────────────────────────────────────────────┘
```

### 技术栈详情

| 分类 | 技术 | 说明 |
|------|------|------|
| **后端框架** | Spring Boot 3 + JDK 21 | 核心业务框架 |
| **安全认证** | Spring Security + JWT | 双Token认证鉴权 |
| **ORM框架** | MyBatis-Plus | 简化数据库操作 |
| **AI能力** | Spring AI + Coze | 智能简历助手 |
| **数据库** | MySQL 8.0 | 业务数据存储 |
| **缓存** | Redis 7.2 | Token缓存与数据同步 |
| **向量数据库** | PGVector 16 | AI向量检索 |
| **对象存储** | KODO / MinIO | 文件存储服务 |
| **前端框架** | Vue 3 / React 18 | 用户界面 |
| **网关** | Nginx | 路由与负载均衡 |
| **监控** | Actuator + Prometheus + Grafana | 系统监控 |
| **部署** | Docker + Docker Compose | 容器化部署 |

---

## ✨ 功能特性

### 用户端

- 🎨 **简历模板** - 多种精美模板，一键套用
- ✏️ **在线编辑** - 可视化简历编辑器
- 🤖 **AI助手** - 智能问答 + 智能体，简历优化建议
- 📤 **简历投递** - 一键投递，自动匹配岗位
- 💼 **招聘信息** - 海量职位，精准推荐
- 📚 **求职攻略** - 面试技巧，职场干货

### 管理端

- 👥 **用户管理** - 管理员、角色、权限管理
- 📝 **内容管理** - 文章、简历模板管理
- 💼 **招聘管理** - 岗位、招聘信息管理
- 💬 **反馈管理** - 用户反馈处理与追踪
- 🤖 **AI助手** - 智能问答 + 智能体助手

### 监测与广告端

- 📊 **数据统计** - 用户访问量、注册量可视化
- 📢 **广告管理** - 三端广告统一管理
- 📣 **公告管理** - 三端公告统一管理
- 🖥️ **服务器管理** - 设备信息管理
- 📈 **服务器监控** - 实时监控CPU/内存/硬盘/负载
- 🔒 **安全监控** - SpringBootAdmin + Prometheus + Grafana

---

## 📸 系统截图

<details>
<summary><b>👤 用户端截图（点击展开）</b></summary>

| 功能 | 截图 |
|------|------|
| 门户首页 | ![门户页](../ReadMeImages/SystemPicture/img.png) |
| 我的简历 | ![我的简历](../ReadMeImages/SystemPicture/img_1.png) |
| 简历模板 | ![简历模板](../ReadMeImages/SystemPicture/img_2.png) |
| 招聘信息 | ![招聘信息](../ReadMeImages/SystemPicture/img_3.png) |
| 求职攻略 | ![求职攻略](../ReadMeImages/SystemPicture/img_4.png) |
| AI智能问答 | ![AI问答](../ReadMeImages/SystemPicture/img_5.png) |
| AI智能体 | ![AI智能体](../ReadMeImages/SystemPicture/img_6.png) |

</details>

<details>
<summary><b>🔧 管理端截图（点击展开）</b></summary>

| 功能 | 截图 |
|------|------|
| 管理员管理 | ![管理员管理](../ReadMeImages/SystemPicture/img_11.png) |
| 角色管理 | ![角色管理](../ReadMeImages/SystemPicture/img_12.png) |
| 权限管理 | ![权限管理](../ReadMeImages/SystemPicture/img_13.png) |
| 简历模板管理 | ![简历模板](../ReadMeImages/SystemPicture/img_31.png) |
| AI智能体助手 | ![AI助手](../ReadMeImages/SystemPicture/img_21.png) |
| 反馈管理 | ![反馈管理](../ReadMeImages/SystemPicture/img_22.png) |

</details>

<details>
<summary><b>📊 监测与广告端截图（点击展开）</b></summary>

| 功能 | 截图 |
|------|------|
| 设备管理 | ![设备管理](../ReadMeImages/SystemPicture/img_38.png) |
| 设备监控 | ![设备监控](../ReadMeImages/SystemPicture/img_39.png) |
| SpringBootAdmin | ![SBA](../ReadMeImages/SystemPicture/img_40.png) |
| Prometheus | ![Prometheus](../ReadMeImages/SystemPicture/img_41.png) |
| Grafana | ![Grafana](../ReadMeImages/SystemPicture/img_42.png) |

</details>

---

## 🚀 快速开始

### 环境要求

- JDK 21+
- Node.js 18+
- MySQL 8.0+
- Redis 7.0+
- Docker & Docker Compose（可选）

### 本地开发

```bash
# 1. 克隆项目
git clone https://github.com/your-username/EasyApplyResume.git
cd EasyApplyResume

# 2. 启动后端
# 配置 application.yml 中的数据库连接
mvn spring-boot:run

# 3. 启动前端（管理端）
cd app/admin
npm install
npm run dev

# 4. 启动前端（用户端）
cd app/user
npm install
npm run dev

# 5. 启动前端（监测端）
cd app/ad_monitor
npm install
npm run dev
```

### Docker 部署

```bash
cd docker
docker-compose up -d
```

### 访问地址

| 服务 | 地址 |
|------|------|
| 用户端 | http://localhost:3001 |
| 管理端 | http://localhost:3000 |
| 监测端 | http://localhost:3002 |
| 后端API | http://localhost:8080 |

---

## 📁 项目结构

```
EasyApplyResume/
├── app/                          # 前端项目
│   ├── admin/                    # 管理端 (Vue3)
│   ├── user/                     # 用户端 (React)
│   └── ad_monitor/               # 监测端 (Vue3)
├── src/                          # 后端源码
│   └── main/java/com/zyh/easyapplyresume/
│       ├── controller/           # 控制器层
│       │   ├── user/             # 用户端接口
│       │   ├── admin/            # 管理端接口
│       │   └── ad_monitor/       # 监测端接口
│       ├── service/              # 业务逻辑层
│       ├── mapper/               # 数据访问层
│       ├── model/                # 实体模型
│       │   ├── pojo/             # 数据库实体
│       │   ├── vo/               # 视图对象
│       │   ├── form/             # 表单对象
│       │   └── query/            # 查询对象
│       ├── config/               # 配置类
│       ├── security/             # 安全认证
│       ├── filter/               # 过滤器
│       └── utils/                # 工具类
├── docker/                       # Docker配置
│   ├── docker-compose.yml
│   └── nginx/
└── README.md
```

---

## 📋 开发进度

### ✅ 已完成

- [x] 用户端核心业务（简历管理、模板、投递）
- [x] 管理端核心业务（用户管理、内容管理、反馈管理）
- [x] 监测端核心业务（数据统计、广告公告、服务器监控）
- [x] AI智能问答助手
- [x] AI智能体助手
- [x] JWT双Token认证
- [x] Docker容器化部署

### 🚧 开发中

- [ ] AI简历自动生成
- [ ] 简历评分与关键字提取
- [ ] RPA + Agent-Flow 自动投递

### 📝 规划中

- [ ] 远程服务器终端（类Xshell）
- [ ] MySQL/Redis可视化管理
- [ ] 视频广告管理

---

## 🤝 参与贡献

欢迎任何对项目感兴趣的开发者参与贡献！

### 贡献类型

- 🔧 **代码类**：新增功能、修复Bug、性能优化
- 📝 **文档类**：完善README、API文档、翻译
- 🧪 **测试类**：单元测试、集成测试

### 贡献流程

1. **提交 Issue** - 描述需求或Bug
2. **Fork 项目** - 创建你的分支
3. **开发代码** - 遵循代码规范
4. **提交 PR** - 关联Issue，说明改动

### 分支命名

```
feature/Issue编号-功能描述    # 新功能
fix/Issue编号-问题描述        # Bug修复
docs/Issue编号-文档描述       # 文档更新
```

---

## 📄 开源协议

本项目采用 [MIT License](../LICENSE) 开源协议，可自由使用和二次开发。

---

## 👨‍💻 作者

**赵云翰 (shiningCloud2025)**

- 📧 Email: your-email@example.com
- 🐙 GitHub: [@shiningCloud2025](https://github.com/shiningCloud2025)

---

<p align="center">
  如果这个项目对你有帮助，请给一个 ⭐ Star 支持一下！
</p>

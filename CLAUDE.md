# EasyApplyResume 项目信息

## 项目概述
EasyApplyResume(易投简历)是一个基于 Spring Boot 3 + Spring AI 的智能简历管理系统，包含用户端、管理端和监控广告端。

## 技术栈
- **后端**: Spring Boot 3.3.5 + JDK 21
- **数据库**: MySQL 5.7, Redis 7.2, PGVector 16
- **AI框架**: Spring AI, LangChain4j, 阿里云DashScope
- **ORM**: MyBatis-Plus 3.5.7
- **文档**: Knife4j 4.4.0
- **存储**: MinIO
- **工具**: Hutool, Lombok, Jsoup

## 常用命令

### Maven 命令
```bash
mvn clean package
mvn spring-boot:run
mvn test
mvn compile
```

### 测试命令
```bash
mvn test
mvn test -Dtest=类名
```

### 运行项目
```bash
java -jar target/EasyApplyResume-0.0.1-SNAPSHOT.jar
```

## 项目结构
- `app/`: 应用模块（ad_monitor, admin, user）
- `src/main/java/com/zyh/easyapplyresume/`: 主代码
  - `controller/`: 控制器层
  - `service/`: 服务层
  - `mapper/`: 数据访问层
  - `model/`: 数据模型（pojo, form, query, vo）
  - `demo/`: AI功能示例（RAG, 对话记忆, 工具调用）
  - `config/`: 配置类
  - `utils/`: 工具类
- `src/main/resources/`: 配置文件和文档
- `Image-Search-Mcp/`: 图片搜索 MCP 服务

## 开发规范
- 遵循阿里巴巴 Java 开发规范
- Git 提交格式: `[类型] 描述（关联#Issue编号）`
- 分支命名: `类型/Issue编号-简短描述`

## 端口和环境
- 默认端口: 8080
- 配置文件: application-dev.yaml, application-prod.yaml
- API 文档: http://localhost:8080/doc.html

## 核心功能模块
1. **用户端**: 简历模板、简历制作、职位搜索、AI求职助手
2. **管理端**: 权限管理、模板管理、职位管理、用户管理
3. **监控广告端**: 广告管理、访问监控

## 数据库配置
- MySQL: 主数据库
- PGVector: 向量存储（RAG功能）
- Redis: 缓存和会话

## AI 功能
- RAG文档问答
- 多轮对话记忆（MySQL持久化）
- 工具调用（文件操作、PDF生成、网络搜索等）
- MCP集成（图片搜索）

## 注意事项
- 使用 JDK 21 及 `--enable-preview` 特性
- PGVector 表名默认小写，大写需使用双引号
- 多数据源配置时使用 `jdbc-url` 而非 `url`



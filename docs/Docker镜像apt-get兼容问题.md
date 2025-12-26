# Docker 镜像 apt-get 兼容问题

## 问题现象

构建 Docker 镜像时报错：

```
/bin/sh: apt-get: not found
```

完整错误：
```
ERROR: failed to solve: process "/bin/sh -c apt-get update && apt-get install -y ..." 
did not complete successfully: exit code: 127
```

---

## 出错原因

Dockerfile 中使用了 `apt-get` 命令：

```dockerfile
RUN apt-get update && apt-get install -y --no-install-recommends tzdata \
    && ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apt-get clean && rm -rf /var/lib/apt/lists/*
```

但基础镜像是 **Alpine Linux**（如 `khipu/openjdk21-alpine`），Alpine 使用 `apk` 作为包管理器，不支持 `apt-get`。

### Linux 发行版与包管理器对照

| 发行版 | 包管理器 | 镜像特点 |
|--------|----------|----------|
| Debian/Ubuntu | `apt-get` | 完整，体积较大 |
| Alpine | `apk` | 轻量，体积小 |
| CentOS/RHEL | `yum` / `dnf` | 企业级 |

---

## 解决方案

### 方案一：修改 Dockerfile 适配 Alpine（推荐）

将 `apt-get` 改成 `apk`：

```dockerfile
# 原来的（Debian）
RUN apt-get update && apt-get install -y --no-install-recommends tzdata \
    && ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# 改成（Alpine）
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata
```

**Alpine 完整 Dockerfile 示例**：

```dockerfile
FROM openjdk:21-jdk-slim

LABEL maintainer="your-email@example.com"

# Alpine 时区设置
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

WORKDIR /app
RUN mkdir -p /app/logs
ENV LOG_DIR=/app/logs
ENV JVM_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"
ENV SPRING_PROFILES_ACTIVE="prod"

COPY your-app.jar app.jar
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
```

---

### 方案二：使用 Debian 基础镜像

如果不想修改 Dockerfile，可以换用 Debian 基础的 JDK 镜像：

```bash
# 拉取 Debian 基础的 JDK 镜像（以 eclipse-temurin 为例）
docker pull eclipse-temurin:21-jdk

# 或使用华为云镜像源
docker pull swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/library/eclipse-temurin:21-jdk

# 重命名为 Dockerfile 中使用的名字
docker tag eclipse-temurin:21-jdk openjdk:21-jdk-slim
```

这样原来的 `apt-get` 命令就能正常执行了。

---

## 如何判断镜像是 Alpine 还是 Debian

```bash
# 进入容器查看
docker run -it --rm <镜像名> sh

# 查看系统版本
cat /etc/os-release
```

- 如果看到 `Alpine Linux` → 用 `apk`
- 如果看到 `Debian` / `Ubuntu` → 用 `apt-get`

---

## 总结

| 场景 | 推荐方案 |
|------|----------|
| 追求镜像体积小 | 用 Alpine + `apk` |
| 兼容现有 Dockerfile | 用 Debian 镜像 |
| 快速解决 | 简化 Dockerfile，去掉复杂配置 |

---

## 参考

- [Alpine Linux 包管理](https://wiki.alpinelinux.org/wiki/Alpine_Package_Keeper)
- [Docker Hub - eclipse-temurin](https://hub.docker.com/_/eclipse-temurin)

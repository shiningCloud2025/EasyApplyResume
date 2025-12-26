# EasyApplyResume Docker 部署流程

## 架构概览

```
外部访问
    │
    ├── :37221 → EasyApplyResume-admin (管理端)
    ├── :37222 → EasyApplyResume-user (用户端)
    └── :37223 → EasyApplyResume-admonitor (监控端)
                    │
                    ▼ /api/ 请求
            nginx-gateway (API网关)
                    │
                    ▼
            backend-1 (后端服务)
                    │
                    ▼
            MySQL / Redis / PgVector / MinIO
```

## 服务端口规划

| 服务 | 端口 | 说明 |
|------|------|------|
| MySQL | 37210 | 数据库 |
| Redis | 37211 | 缓存 |
| PgVector | 37212 | 向量数据库 |
| MinIO | 37213/37214 | 对象存储 |
| Nacos | 37215-37217 | 配置中心（可选） |
| Prometheus | 37218 | 监控 |
| Grafana | 37219 | 可视化 |
| 后端 | 8080 (内部) | Spring Boot |
| nginx-gateway | 80 (内部) | API 网关 |
| 管理端 | 37221 | Vue3 前端 |
| 用户端 | 37222 | React 前端 |
| 监控端 | 37223 | Vue3 前端 |

---

## 一、后端部署

### 1. 本地打包

```bash
# Windows PowerShell
cd C:\Users\shini\IdeaProjects\EasyApplyResume
mvn clean package -DskipTests
```

生成：`target/EasyApplyResume-0.0.1-SNAPSHOT.jar`

### 2. 准备 Dockerfile

```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY EasyApplyResume-0.0.1-SNAPSHOT.jar app.jar

ENV TZ=Asia/Shanghai
ENV JVM_OPTS="-Xms512m -Xmx1024m"

RUN mkdir -p /app/logs && chmod 777 /app/logs

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar app.jar"]
```

### 3. 上传到服务器

上传 `jar` 和 `Dockerfile` 到 `/EasyApplyResume/`

### 4. 构建镜像

```bash
cd /EasyApplyResume
docker build -t easyapply-backend:latest .
```

### 5. 添加到 docker-compose.yml

```yaml
backend-1:
  image: easyapply-backend:latest
  container_name: EasyApplyResume-backend-1
  restart: always
  networks:
    - app-network
  environment:
    - SPRING_PROFILES_ACTIVE=dev
    - TZ=Asia/Shanghai
  volumes:
    - ./backend/logs:/app/logs
  depends_on:
    - mysql
    - redis
```

### 6. 启动

```bash
mkdir -p /EasyApplyResume/backend/logs
chmod 777 /EasyApplyResume/backend/logs
docker compose up -d backend-1
```

---

## 二、Nginx 网关部署

### 1. 创建配置目录

```bash
mkdir -p /EasyApplyResume/nginx
```

### 2. 创建 nginx.conf

```nginx
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
    worker_connections 1024;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    sendfile on;
    keepalive_timeout 65;
    client_max_body_size 50M;

    # Docker 内置 DNS（动态解析）
    resolver 127.0.0.11 valid=10s ipv6=off;

    server {
        listen 80;

        location /api/ {
            set $backend "EasyApplyResume-backend-1:8080";
            proxy_pass http://$backend/api/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }

        location /health {
            return 200 'OK';
        }
    }
}
```

### 3. 添加到 docker-compose.yml

```yaml
nginx-gateway:
  image: nginx:alpine
  container_name: EasyApplyResume-nginx-gateway
  restart: always
  networks:
    - app-network
  volumes:
    - ${PWD}/nginx/nginx.conf:/etc/nginx/nginx.conf:ro
    - ${PWD}/nginx/logs:/var/log/nginx
```

### 4. 启动

```bash
docker compose up -d nginx-gateway
```

---

## 三、前端部署

### 1. 本地打包（3个前端）

```powershell
# admin
cd C:\Users\shini\IdeaProjects\EasyApplyResume\app\admin
npm run build

# user
cd C:\Users\shini\IdeaProjects\EasyApplyResume\app\user
npm run build

# ad_monitor
cd C:\Users\shini\IdeaProjects\EasyApplyResume\app\ad_monitor
npm run build
```

### 2. 准备 Dockerfile（每个前端相同）

```dockerfile
FROM nginx:alpine
COPY dist/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### 3. 准备 nginx.conf（每个前端相同）

```nginx
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # SPA 路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API 代理到网关
    location /api/ {
        proxy_pass http://EasyApplyResume-nginx-gateway:80/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

### 4. 上传到服务器

每个前端上传：
- `dist/` 目录
- `Dockerfile`
- `nginx.conf`

目录结构：
```
/EasyApplyResume/app/
├── admin/
│   ├── dist/
│   ├── Dockerfile
│   └── nginx.conf
├── user/
│   ├── dist/
│   ├── Dockerfile
│   └── nginx.conf
└── ad_monitor/
    ├── dist/
    ├── Dockerfile
    └── nginx.conf
```

### 5. 构建镜像

```bash
cd /EasyApplyResume/app/admin
docker build -t easyapply-admin:latest .

cd /EasyApplyResume/app/user
docker build -t easyapply-user:latest .

cd /EasyApplyResume/app/ad_monitor
docker build -t easyapply-admonitor:latest .
```

### 6. 添加到 docker-compose.yml

```yaml
frontend-admin:
  image: easyapply-admin:latest
  container_name: EasyApplyResume-admin
  restart: always
  networks:
    - app-network
  ports:
    - "37221:80"

frontend-user:
  image: easyapply-user:latest
  container_name: EasyApplyResume-user
  restart: always
  networks:
    - app-network
  ports:
    - "37222:80"

frontend-admonitor:
  image: easyapply-admonitor:latest
  container_name: EasyApplyResume-admonitor
  restart: always
  networks:
    - app-network
  ports:
    - "37223:80"
```

### 7. 启动

```bash
cd /EasyApplyResume
docker compose up -d frontend-admin frontend-user frontend-admonitor
```

---

## 四、常用命令

```bash
# 查看所有容器状态
docker ps

# 查看日志
docker logs -f <容器名>

# 重启服务
docker restart <容器名>

# 停止所有服务
docker compose down

# 启动所有服务
docker compose up -d

# 查看资源占用
docker stats
```

---

## 五、常见问题

### 1. Nginx 上游服务解析失败

**错误**：`host not found in upstream`

**原因**：后端服务还没启动

**解决**：使用动态 DNS 解析（resolver 127.0.0.11）

### 2. 后端日志权限问题

**错误**：`Permission denied: logs/my-application.log`

**解决**：
```bash
mkdir -p /EasyApplyResume/backend/logs
chmod 777 /EasyApplyResume/backend/logs
```

### 3. Docker 镜像拉取失败

**错误**：`net/http: request canceled`

**解决**：使用华为云 SWR 镜像源
```bash
docker pull swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/library/nginx:alpine
docker tag swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/library/nginx:alpine nginx:alpine
```

### 4. Alpine 镜像 apt-get 不存在

**错误**：`apt-get: not found`

**解决**：Alpine 使用 `apk` 包管理器，或换用 Debian 基础镜像

---

## 六、访问地址

- **管理端**：`http://<服务器IP>:37221`
- **用户端**：`http://<服务器IP>:37222`
- **监控端**：`http://<服务器IP>:37223`
- **Grafana**：`http://<服务器IP>:37219`
- **Nacos**：`http://<服务器IP>:37215/nacos`

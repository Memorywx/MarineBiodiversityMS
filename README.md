# 海洋生物多样性信息管理系统

基于 Spring Boot + Vue 3 + MySQL 的海洋生物多样性信息管理平台。

## 技术栈

- **后端**：Spring Boot 3.1 + MyBatis-Plus + JWT + MySQL 8.0
- **前端**：Vue 3 + Vite + Element Plus + ECharts + Leaflet
- **部署**：Docker + Docker Compose

## 快速启动

### 前置要求

- Docker Desktop 已安装并运行
- 本地端口 **8080**（应用）和 **3306**（MySQL）未被占用

### 首次部署（完整流程）

```bash
# 1. 安装前端依赖并打包
cd frontend
npm install
npm run build
cd ..

# 2. 把前端产物复制到后端静态资源目录
copy-Item -Recurse frontend\dist src\main\resources\static

# 3. 打包后端（生成 jar）
mvn clean package -DskipTests

# 4. 启动 Docker
docker-compose up -d
```

> 如果本地没有 Maven，可以用 IDEA 的 Maven 面板执行 `clean` + `package`。

### 后续启动（已有 jar）

```bash
docker-compose up -d
```

访问：http://localhost:8080

### 默认账号

| 账号 | 密码 | 角色 |
|------|------|------|
| admin | 123456 | 管理员 |
| researcher | 123456 | 科研人员 |

## 端口冲突处理

### 情况一：8080 端口被占用

修改 `docker-compose.yml`：

```yaml
app:
  ports:
    - "8081:8080"   # 把左侧改成其他端口，如 8081、9000 等
```

然后重启：
```bash
docker-compose up -d
```

访问时改用新端口，如 http://localhost:8081

### 情况二：3306 端口被占用

修改 `docker-compose.yml`：

```yaml
mysql:
  ports:
    - "3307:3306"   # 把左侧改成其他端口，如 3307
```

> **注意**：Docker 部署时**不需要改 `application.yml`**。因为容器内部 Spring Boot 通过 `SPRING_DATASOURCE_URL` 环境变量直接连接 MySQL 容器（`mysql:3306`），不受宿主机端口映射影响。只有本地 IDEA 直接运行后端时才需要改 `application.yml` 中的 `localhost:3306`。

然后重新打包部署：
```bash
mvn clean package -DskipTests
docker-compose up -d --build
```

### 查看端口占用（Windows）

```powershell
netstat -ano | findstr :8080
netstat -ano | findstr :3306
```

## 常用命令

| 命令 | 说明 |
|------|------|
| `docker-compose up -d` | 启动服务（后台运行） |
| `docker-compose up -d --build` | 重新构建并启动（代码变更后使用） |
| `docker-compose down` | 停止服务 |
| `docker-compose down -v` | 停止服务并清空数据库（会丢失所有数据） |
| `docker logs marine-app` | 查看后端日志 |
| `docker logs marine-mysql` | 查看 MySQL 日志 |

## 开发模式

### 后端开发（本地调试）

```bash
# 只启动 MySQL
docker-compose up -d mysql

# 本地启动 Spring Boot（IDEA 运行 MarineBiodiversityApplication）
# 会自动连接 localhost:3306 的数据库
```

### 前端开发（热更新）

```bash
cd frontend
npm install
npm run dev
```

前端运行在 http://localhost:5173，通过 Vite 代理自动转发 `/api` 到 localhost:8080。

## 项目结构

```
.
├── src/main/java/           # 后端 Java 源码
├── src/main/resources/      # 配置文件、静态资源（打包后的前端）
├── frontend/                # Vue 3 前端源码
├── db/
│   ├── init.sql             # 数据库表结构
│   └── seed.sql             # 初始演示数据
├── docker-compose.yml       # Docker 编排配置
├── Dockerfile               # 后端镜像构建
├── pom.xml                  # Maven 构建配置
└── README.md                # 本文件
```

## 更新部署流程

### 只改前端

```bash
cd frontend
npm run build
cd ..
copy-Item -Recurse frontend\dist src\main\resources\static
mvn clean package -DskipTests
docker-compose up -d --build
```

### 只改后端

```bash
mvn clean package -DskipTests
docker-compose up -d --build
```

### 全量重置（清空数据）

```bash
docker-compose down -v
docker-compose up -d --build
```

## 注意事项

- MySQL 初始化脚本（`db/init.sql` 和 `db/seed.sql`）**只在数据库首次创建时执行一次**。如需重新执行，必须先 `docker-compose down -v` 删除数据卷。
- 数据库中文编码已配置为 `utf8mb4`，所有表和连接均支持中文。
- 生产环境部署前，请修改 `application.yml` 中的 `jwt.secret` 为随机强密码。

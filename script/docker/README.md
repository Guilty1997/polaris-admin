# Docker 使用说明（Mac 本地开发 + 本地部署）

本目录已重构为两套编排：

- `compose.local.yml`：仅启动本地开发依赖（MySQL / Redis / MinIO）
- `compose.deploy.yml`：本地部署链路（MySQL / Polaris Server）

## 1) 前置要求（macOS）

- Docker Desktop for Mac（建议最新版）
- 已开启 Docker Compose v2（`docker compose version`）

## 2) 固定配置说明

当前编排已按本地开发写死默认参数（MySQL/MinIO/应用端口等），无需 `.env`。

## 3) 本地开发调试（推荐）

启动依赖：

```bash
docker compose -f compose.local.yml up -d
```

MySQL 在首次初始化时会自动执行 `script/sql/compose-init` 目录下的脚本（按文件名顺序）。

默认脚本名已调整为：

- `10_polaris_admin_mysql_core.sql`
- `20_polaris_admin_mysql_workflow.sql`
- `30_polaris_admin_mysql_job.sql`

历史 SQL 已集中放到 `script/sql/backup` 作为备份。
如果你需要切换初始化方案，把对应 SQL 从 `backup` 复制到 `script/sql/compose-init` 后再执行 compose 即可。

如需重新初始化数据库，请先删除 `script/docker/volumes/mysql/data` 后再启动。

然后在 IDE 启动后端（`polaris-server`）。

后端默认读取 `application-dev.yml`，本地调试按默认端口即可连接（MySQL `3306`，Redis `6379`）。

## 4) 本地部署验证（容器化）

在项目根目录先构建 jar（确保 `polaris-server/target` 下有产物）：

```bash
mvn clean package -DskipTests
```

再回到本目录启动：

```bash
docker compose -f compose.deploy.yml up -d --build
```

访问：

- 后端：`http://localhost:8080`

## 5) 停止服务

停止开发依赖：

```bash
docker compose -f compose.local.yml down
```

停止部署栈：

```bash
docker compose -f compose.deploy.yml down
```

## 6) 目录约定

- 持久化目录：`./volumes/*`
不再使用 `network_mode: host` 与系统绝对路径挂载，避免在 Mac 上出现网络和权限兼容问题。

# compose-init 目录说明

该目录用于 MySQL 容器初始化脚本投放，`compose.local.yml` 会将本目录挂载到：

- `/docker-entrypoint-initdb.d`

MySQL 仅在数据目录为空时，按文件名顺序执行该目录下脚本。

## 当前默认脚本

- `10_polaris_admin_mysql_core.sql`
- `20_polaris_admin_mysql_workflow.sql`
- `30_polaris_admin_mysql_job.sql`

## 切换数据库初始化方案

如果你要切换初始化内容（例如不同数据库版本或定制脚本）：

1. 清空本目录现有 `.sql` 文件
2. 从 `script/sql/backup` 复制你需要执行的脚本到本目录（建议按 `10_`, `20_`, `30_` 命名控制顺序）
3. 删除 `script/docker/volumes/mysql/data` 后重新 `docker compose up`

> 注意：本目录脚本是给 MySQL 容器执行的；若切换到 PostgreSQL/Oracle/SQLServer，请使用对应数据库镜像与初始化机制。

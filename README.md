# Polaris Admin

基于 **RuoYi-Vue-Plus** 技术路线改造的后台底座，用于个人或团队快速搭建 **多租户管理后台**。后端统一品牌与模块为 `polaris-*`，在保留原有能力（Sa-Token、MyBatis-Plus、工作流、任务调度等）的基础上，可按业务继续扩展。

| 项目 | 地址 |
| --- | --- |
| 后端（本仓库） | [github.com/Guilty1997/polaris-admin](https://github.com/Guilty1997/polaris-admin) |
| 前端 | [github.com/Guilty1997/polaris-admin-ui](https://github.com/Guilty1997/polaris-admin-ui) |

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](./LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)

**当前版本**：`1.0.0-SNAPSHOT`（见根 `pom.xml` 中 `revision`）  
**建议分支**：日常开发使用 `dev`，稳定发布以仓库默认主分支为准。

---

## 技术栈概要

- **运行时**：Spring Boot 3.5.x、Java 17（可按需使用21）
- **Web**：Undertow、SpringDoc（OpenAPI）
- **安全**：Sa-Token、多租户、数据权限等（与上游 Plus 体系一致）
- **数据**：MyBatis-Plus、动态数据源、Redis（Redisson）等
- **可选能力**：工作流（Warm Flow）、分布式任务（Snail Job）、代码生成、AI 扩展模块等

更完整的演进与 AI 规划见仓库内 **[AI_APP_PLAN.md](./AI_APP_PLAN.md)**。

---

## 工程结构（后端）

```
polaris-admin/
├── polaris-server/          # 启动入口（Web 服务）
├── polaris-modules/         # 业务模块
│   ├── polaris-system/      # 系统管理
│   ├── polaris-auth/        # 认证授权
│   ├── polaris-generator/   # 代码生成
│   ├── polaris-job/         # 定时任务
│   ├── polaris-workflow/    # 工作流
│   ├── polaris-demo/        # 示例
│   └── polaris-ai/          # AI 相关扩展（见 AI_APP_PLAN.md）
├── polaris-common/          # 公共组件（多子模块）
└── script/                  # SQL 初始化与更新、Docker 等
```

主启动类：`polaris-server` 下的 `com.polaris.PolarisAdminApplication`。

---

## 本地运行（简要）

1. **环境**：JDK 17+、Maven 3.8+、MySQL（或其它已配置驱动）、Redis、Node（仅前端需要）。
2. **数据库**：在 `script/sql` 中选择与数据库类型匹配的脚本初始化；版本升级脚本见 `script/sql/update`。
3. **配置**：修改 `polaris-server/src/main/resources/application-dev.yml`（或对应 profile）中的数据源、Redis 等。
4. **构建与启动**：
   ```bash
   mvn clean install
   cd polaris-server
   mvn spring-boot:run
   ```
   或在 IDE 中直接运行 `PolarisAdminApplication`。

5. **前端**：克隆 [polaris-admin-ui](https://github.com/Guilty1997/polaris-admin-ui)，按该仓库 `README` 安装依赖并启动；将接口代理指向本后端地址。

详细部署、多环境、Docker 等请以本仓库 `script/docker` 与配置文件为准逐步完善（可参考上游 [RuoYi-Vue-Plus 文档](https://plus-doc.dromara.org) 中与 Spring Boot 3 / Sa-Token / 多租户相关的通用说明）。

---

## 与上游的关系

本仓库在 **RuoYi-Vue-Plus** 能力集上做了品牌化与结构调整（`polaris-*` 模块）。**协议**：MIT，使用或分发时请保留仓库中的 [LICENSE](./LICENSE) 及其中原有版权声明。若向他人分发衍生作品，建议在文档中说明基于 Polaris / RuoYi-Vue-Plus 的演进关系。

---

## 文档索引

| 文档 | 说明 |
| --- | --- |
| [AI_APP_PLAN.md](./AI_APP_PLAN.md) | Polaris AI 能力规划与架构设想 |
| [LICENSE](./LICENSE) | MIT 许可 |

---

## 贡献与问题

欢迎通过 Issue / PR 在本仓库讨论。若涉及上游框架通用问题，也可对照 [dromara/RuoYi-Vue-Plus](https://github.com/dromara/RuoYi-Vue-Plus) 与 [plus-doc](https://plus-doc.dromara.org) 排查。

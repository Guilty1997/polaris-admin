# Polaris AI 应用规划

## 1. 背景与目标
Polaris 目前以 RuoYi-Vue-Plus 为底座，后端采用 Spring Boot 3.5.7 + MyBatis-Plus + Sa-Token + 多租户插件，前端为 Vue3 + Element Plus。系统具备用户/租户/流程/任务等丰富的业务能力，但缺少面向租户的智能助手。目标是引入基于 Langchain4j 的 AI 能力，打造 “Polaris AI Copilot”，提供以下价值：

- 降低学习与运维门槛：支持面向租户的系统使用问答、流程配置指导、日志/告警解读。
- 提升流程与任务效率：生成审批模板、优化 Warm Flow 流程、Snail Job 异常诊断。
- 形成知识资产：沉淀多租户知识库，实现私有化的 Retrieval-Augmented Generation (RAG)。

## 2. 现有技术栈速览

| 分层 | 关键技术 | 说明 |
| --- | --- | --- |
| 网关/服务端 | Undertow、Sa-Token、多租户、Lock4J、Redisson、Warm Flow、Snail Job | `polaris-server` 提供统一配置，`polaris-modules` 负责业务实现。|
| 数据层 | MyBatis-Plus、动态数据源、HikariCP、Redis、OSS、SMS4J | 支持多数据库、多 Redis、异步任务、文件存储等。
| 前端 | Vue3、TypeScript、Element Plus、Pinia、VXE Table、SSE/WebSocket | `polaris-admin-ui` 构建后台管理 UI，可扩展新视图和实时推送。
| 公共能力 | Excel 导入导出、API 加解密、国际化、数据权限、租户隔离 | AI 模块应复用这些横向能力。

## 3. AI 应用定位

1. **知识问答 Copilot**：面向租户/管理员的系统使用指引、政策制度问答、产品 FAQ。支持引用知识源、权限控制、多轮上下文。
2. **流程/表单助手**：结合 Warm Flow 元数据和租户业务参数，为审批流程提供节点建议、字段说明、自动生成流程草稿。
3. **任务与运维智能**：对 Snail Job 日志、监控数据进行总结，输出异常原因、修复建议；对系统操作日志生成可读报告。
4. **工单自动化**：根据用户描述自动生成工单或配置脚本，引导用户一步步完成（可调用现有 API 作为 Langchain Tool）。

## 4. Langchain4j 技术选型

- **模块划分**：新增 `polaris-modules/polaris-ai`，对外暴露 REST/SSE API。引入 `langchain4j-spring-boot-starter`、选定 LLM 驱动（OpenAI、阿里通义、火山方舟、自建模型等），以及 `langchain4j-embeddings`、`langchain4j-pgvector` 等依赖。
- **模型与部署**：优先使用云端大模型，提供 API Key/Proxy 配置；后续可接入私有化模型。通过 Spring `@ConfigurationProperties` 管理多模型参数（温度、最大 tokens、超时、租户限额）。
- **向量存储**：
  - 方案 A：PostgreSQL + pgvector（私有部署、事务支持、方便多租户隔离）。
  - 方案 B：Milvus/Chroma/Elastic/Redis Stack（若已有基础设施）。
  - 方案 C：混合模式，短期先使用 Redis Vector，后续迁移。
- **文件解析**：接入 Apache Tika + PDFBox + Excel 解析器，统一抽象 `DocumentIngestService`，支持文本、PDF、Word、Excel、Markdown 等。
- **会话管理**：Langchain4j 提供 `ChatMemoryStore`，结合 Redis 或数据库保存历史；SSE 流式输出连接现有 `sse.enabled` 能力。

## 5. 目标架构

```
┌───────────────────────────┐
│ Polaris Admin UI          │
│  - AI Copilot Chat        │
│  - 知识库管理 / 上传        │
│  - 流程/任务助手入口        │
└───────────────▲───────────┘
                │HTTP(S)/SSE
┌───────────────┴───────────┐
│ polaris-server           │
│  - polaris-ai 模块       │
│    · ChatController      │
│    · KnowledgeController │
│    · Langchain4j Chains  │
│    · Tool 集成 Warm Flow │
│  - Snail Job Worker      │
└───────▲───────────┬──────┘
        │            │
        │            └──► 文件处理/嵌入任务 (Snail Job + OSS)
        │
        └──► 向量库 (pgvector / Milvus)
               + 原业务数据库 / Redis
```

## 6. 功能规划（迭代）

### 阶段 0：底座搭建
- 新建 `polaris-ai` 模块，配置 Maven 依赖、Spring Boot 自动配置、配置文件（`application-*.yml`）中的模型参数、密钥、限流策略。
- 基于 `Sa-Token` + `TenantHelper` + `DataPermissionHelper` 打底，所有 AI API 默认需要认证且携带租户上下文。
- 定义通用 DTO：`ChatMessage`, `ChatSession`, `KnowledgeDocument`, `IngestTask` 等。

### 阶段 1：知识库与向量化
- 设计数据库表：
  - `ai_corpus`（知识库维度，租户隔离、可共享）。
  - `ai_document`（文件记录、OSS 路径、状态）。
  - `ai_chunk`（分块元数据、序号、哈希）。
  - `ai_embedding`（向量、维度、索引、租户 ID）。
- 文件上传 -> OSS 存储 -> Snail Job 任务触发 -> Worker 提取文本、分块、调用 Langchain4j Embeddings -> 写入向量库。
- 前端提供知识库列表、文件上传、状态查询、重新刷新嵌入。

### 阶段 2：对话与 SSE 推送
- 实现 `ChatController`：
  - `/ai/chat/session` 创建/切换会话。
  - `/ai/chat/stream` SSE 推送（流式 token）。
  - `/ai/chat/history` 返回会话历史（分页）。
- 集成 Langchain4j `ConversationalRetrievalChain`，注入 RAG 检索器（按租户过滤向量），加入系统提示与引用来源元数据。
- 前端聊天界面：多会话切换、Markdown 渲染、引用折叠、复制/反馈、权限标签。

### 阶段 3：Agent & 业务融合
- 以 Langchain4j Tool/AiServices 形式暴露：
  - Warm Flow 接口（查询/创建流程、获取节点配置）。
  - Snail Job 接口（任务状态、日志摘要）。
  - 系统查询接口（用户、租户、菜单、操作日志等）。
- 通过 `Tool` + `Function Calling` 让模型完成“流程草拟”“任务异常诊断”等操作，必要时写操作需二次确认（前端弹窗/审批）。
- 在流程设计器、任务列表右侧加“AI 建议”面板，调用相同 API 但带上上下文（表单结构、任务日志 ID）。

### 阶段 4：治理与商业化
- 引入配额计费（按租户/角色限制调用次数、上下文长度）。
- 提供审计日志、提示词模板配置、模型热切换能力。
- 支持私有部署模型或混合路由（参考 Langchain4j Router）。

## 7. 多租户、安全与合规

- **租户隔离**：所有知识库与会话表均包含 `tenant_id`，Langchain4j 检索器/ChatMemory 均需在 DAO 层注入租户过滤；超级管理员可指定是否读取“全局知识库”。
- **权限控制**：结合 Sa-Token 权限编码，如 `ai:chat:use`, `ai:kb:manage`, `ai:agent:workflow`。
- **数据加解密**：沿用现有 `api-decrypt` 和 HTTPS，模型密钥存储在系统参数表，使用 `ApiEncrypt` 注解保护上传接口。
- **审计与监控**：利用现有操作日志 + Snail Job 记录 + 监控端点，记录提示词、引用文档、调用结果。若涉及敏感数据，增加脱敏策略。

## 8. 实施里程碑

| 阶段 | 交付物 | 预计周期 |
| --- | --- | --- |
| 0 | `polaris-ai` 模块、配置项、原型 API、模型连通性验证 | 1~2 周 |
| 1 | 知识库 CRUD、文件上传 & 向量化任务、前端管理界面 | 2~3 周 |
| 2 | RAG 对话、SSE 流式响应、多会话 UI、权限控制 | 2 周 |
| 3 | Agent 与业务融合（流程/任务助手）、UI 集成 | 3~4 周 |
| 4 | 配额、审计、模型治理、性能/成本优化 | 2 周 |

> 实际时间视模型接入 & 基础设施准备情况而定，可并行推进向量库部署与 UI/产品设计。

## 9. 风险与应对

- **模型供应风险**：提前确认供应商 SLA、网络出口与费用；保留开源模型备选方案。
- **多租户数据泄露**：在数据链路（上传、分块、存储、检索）强制校验租户 ID，定期做穿透测试。
- **成本不可控**：引入配额与限流，支持低成本模型 fallback；对知识库做去重压缩，避免冗余向量。
- **链路性能**：优化 Snail Job 异步处理、使用缓存/分片检索；必要时开启流式响应 + 分段引用。
- **合规要求**：对敏感字段做脱敏/加密，提供可审计日志；若涉及外传数据，需获得客户授权。

## 10. 下一步动作

1. 确定首选模型 & 向量库方案，准备访问密钥与网络环境。
2. 新建 `polaris-ai` 模块并完成最小可运行样例（简单对话 + SSE）。
3. 设计知识库数据结构与 Snail Job 流程，搭建 PoC 并验证多租户隔离。
4. 与产品/业务确认首批 AI 场景范围，完善提示词模板与 UX 交互。

---
如需进一步细化某个阶段（例如知识库字段设计、向量库部署脚本、Langchain4j 具体配置），可继续在此文档补充或拆分子文档。

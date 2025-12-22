package com.polaris.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * PgVector 存储配置。
 */
@Data
@ConfigurationProperties(prefix = "ai.pgvector")
public class PgVectorProperties {

    /** 是否启用 pgvector 向量存储 */
    private boolean enabled = true;
    /** 数据库主机 */
    private String host = "127.0.0.1";
    /** 数据库端口 */
    private int port = 5432;
    /** 数据库名称 */
    private String database = "polaris_ai";
    /** 数据库用户 */
    private String username = "postgres";
    /** 数据库密码 */
    private String password = "postgres";
    /** Schema 名称 */
    private String schema = "public";
    /** 表名 */
    private String table = "ai_embeddings";
    /** 向量维度，需要与嵌入模型保持一致 */
    private int dimension = 1536;
}

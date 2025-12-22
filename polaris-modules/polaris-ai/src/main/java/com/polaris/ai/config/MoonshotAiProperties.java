package com.polaris.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Moonshot(月之暗面)模型配置。
 */
@Data
@ConfigurationProperties(prefix = "ai.moonshot")
public class MoonshotAiProperties {

    /** 是否启用 Moonshot 聊天模型 */
    private boolean enabled = true;

    /** API 地址，默认 Moonshot OpenAI 兼容网关 */
    private String baseUrl = "https://api.moonshot.cn/v1";

    /** 访问令牌 */
    private String apiKey;

    /** 聊天模型名称 */
    private String model = "moonshot-v1-8k";

    /** 采样温度 */
    private double temperature = 0.3;

    /** 最大输出 tokens */
    private Integer maxTokens = 2048;

    /** 请求超时时间 */
    private Duration timeout = Duration.ofSeconds(60);

    /**
     * 嵌入模型配置，用于向量化。
     */
    private Embedding embedding = new Embedding();

    @Data
    public static class Embedding {
        /** 是否启用嵌入模型 */
        private boolean enabled = true;
        /** 嵌入 API 地址，不配置时沿用主配置 */
        private String baseUrl;
        /** 嵌入 API Key，不配置时沿用主配置 */
        private String apiKey;
        /** 模型名称，默认使用 OpenAI 兼容 embedding */
        private String model = "text-embedding-3-small";
        /** 向量维度，对应 pgvector 表结构 */
        private int dimension = 1536;
    }
}

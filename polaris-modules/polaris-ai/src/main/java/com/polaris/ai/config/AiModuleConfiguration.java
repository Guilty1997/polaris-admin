package com.polaris.ai.config;

import com.polaris.common.core.utils.StringUtils;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * AI 模块核心配置。
 */
@Configuration
@EnableConfigurationProperties({MoonshotAiProperties.class, PgVectorProperties.class})
@RequiredArgsConstructor
public class AiModuleConfiguration {

    private final MoonshotAiProperties moonshotProperties;
    private final PgVectorProperties pgVectorProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ai.moonshot", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ChatLanguageModel moonshotChatLanguageModel() {
        Assert.hasText(moonshotProperties.getApiKey(), "Moonshot apiKey 未配置");
        OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
            .apiKey(moonshotProperties.getApiKey())
            .baseUrl(moonshotProperties.getBaseUrl())
            .modelName(moonshotProperties.getModel())
            .temperature(moonshotProperties.getTemperature())
            .timeout(moonshotProperties.getTimeout());
        if (moonshotProperties.getMaxTokens() != null) {
            builder.maxTokens(moonshotProperties.getMaxTokens());
        }
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ai.moonshot.embedding", name = "enabled", havingValue = "true", matchIfMissing = true)
    public EmbeddingModel moonshotEmbeddingModel() {
        MoonshotAiProperties.Embedding embedding = moonshotProperties.getEmbedding();
        Assert.notNull(embedding, "Moonshot 嵌入配置为空");
        String apiKey = StringUtils.defaultIfBlank(embedding.getApiKey(), moonshotProperties.getApiKey());
        Assert.hasText(apiKey, "Embedding apiKey 未配置");
        String baseUrl = StringUtils.defaultIfBlank(embedding.getBaseUrl(), moonshotProperties.getBaseUrl());
        OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder builder = OpenAiEmbeddingModel.builder()
            .apiKey(apiKey)
            .baseUrl(baseUrl)
            .modelName(embedding.getModel())
            .timeout(moonshotProperties.getTimeout());
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ai.pgvector", name = "enabled", havingValue = "true", matchIfMissing = true)
    public EmbeddingStore<TextSegment> pgVectorEmbeddingStore() {
        String table = pgVectorProperties.getTable();
        if (StringUtils.isNotBlank(pgVectorProperties.getSchema())) {
            table = pgVectorProperties.getSchema() + "." + table;
        }
        return PgVectorEmbeddingStore.builder()
            .host(pgVectorProperties.getHost())
            .port(pgVectorProperties.getPort())
            .database(pgVectorProperties.getDatabase())
            .user(pgVectorProperties.getUsername())
            .password(pgVectorProperties.getPassword())
            .table(table)
            .dimension(pgVectorProperties.getDimension())
            .build();
    }
}

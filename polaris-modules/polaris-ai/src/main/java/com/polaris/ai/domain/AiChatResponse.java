package com.polaris.ai.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 对话响应。
 */
@Data
public class AiChatResponse {

    private String sessionId;

    private String model;

    private String content;

    /** 被引用的知识片段 */
    private List<String> references = new ArrayList<>();
}

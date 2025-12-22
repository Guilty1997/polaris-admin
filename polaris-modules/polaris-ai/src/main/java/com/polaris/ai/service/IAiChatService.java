package com.polaris.ai.service;

import com.polaris.ai.domain.AiChatRequest;
import com.polaris.ai.domain.AiChatResponse;

/**
 * AI 对话服务接口。
 */
public interface IAiChatService {

    /**
     * 根据提示词生成回复。
     *
     * @param request 对话请求
     * @return 回复内容
     */
    AiChatResponse chat(AiChatRequest request);
}

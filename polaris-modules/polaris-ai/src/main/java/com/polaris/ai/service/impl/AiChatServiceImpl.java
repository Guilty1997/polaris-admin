package com.polaris.ai.service.impl;

import com.polaris.ai.config.MoonshotAiProperties;
import com.polaris.ai.domain.AiChatRequest;
import com.polaris.ai.domain.AiChatResponse;
import com.polaris.ai.service.IAiChatService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 默认 AI 对话实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements IAiChatService {

    private final ChatLanguageModel chatLanguageModel;
    private final MoonshotAiProperties moonshotAiProperties;

    @Override
    public AiChatResponse chat(AiChatRequest request) {
        String prompt = buildPrompt(request);
        String answer = chatLanguageModel.generate(prompt);
        AiChatResponse response = new AiChatResponse();
        response.setSessionId(request.getSessionId());
        response.setModel(moonshotAiProperties.getModel());
        response.setContent(answer);
        response.setReferences(new ArrayList<>(request.getContext() == null ? Collections.emptyList() : request.getContext()));
        return response;
    }

    private String buildPrompt(AiChatRequest request) {
        StringBuilder builder = new StringBuilder();
        if (request.getContext() != null && !request.getContext().isEmpty()) {
            builder.append("以下是补充上下文，供你回答时参考：\n");
            for (String ctx : request.getContext()) {
                builder.append("- ").append(ctx).append('\n');
            }
            builder.append("\n");
        }
        builder.append(request.getPrompt());
        return builder.toString();
    }
}

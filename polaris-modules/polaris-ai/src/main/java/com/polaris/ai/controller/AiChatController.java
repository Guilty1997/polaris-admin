package com.polaris.ai.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.polaris.ai.domain.AiChatRequest;
import com.polaris.ai.domain.AiChatResponse;
import com.polaris.ai.service.IAiChatService;
import com.polaris.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 对话接口。
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {

    private final IAiChatService aiChatService;

    @SaCheckPermission("ai:chat:generate")
    @PostMapping("/generate")
    public R<AiChatResponse> generate(@Validated @RequestBody AiChatRequest request) {
        return R.ok(aiChatService.chat(request));
    }
}

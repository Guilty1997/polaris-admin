package com.polaris.ai.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 对话请求。
 */
@Data
public class AiChatRequest {

    /** 会话 ID，用于前端管理上下文 */
    private String sessionId;

    /** 主提示词 */
    @NotBlank(message = "提示词不能为空")
    private String prompt;

    /** 额外上下文（例如业务字段、用户补充信息） */
    private List<String> context = new ArrayList<>();

    /** 是否允许模型结合知识库 */
    private boolean useKnowledge = true;
}

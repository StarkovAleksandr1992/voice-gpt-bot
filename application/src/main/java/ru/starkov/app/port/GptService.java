package ru.starkov.app.port;

import ru.starkov.app.dto.in.gpt_response.GptResponseInfo;
import ru.starkov.app.dto.out.gpt_request.GptChatRequest;
import ru.starkov.app.dto.out.gpt_request.GptPromptRequest;
import ru.starkov.app.dto.out.gpt_request.GptSummarizationRequest;

public interface GptService {

    GptResponseInfo summarize(GptSummarizationRequest request);

    GptResponseInfo sendGptChatRequest(GptChatRequest request);

    GptResponseInfo sendGptPromptRequest(GptPromptRequest request);
}

package ru.starkov.struct.yandex.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.starkov.app.dto.in.gpt_response.GptResponseInfo;
import ru.starkov.app.dto.out.gpt_request.GptChatRequest;
import ru.starkov.app.dto.out.gpt_request.GptPromptRequest;
import ru.starkov.app.dto.out.gpt_request.GptSummarizationRequest;
import ru.starkov.app.port.GptService;
import ru.starkov.struct.yandex.gpt.dto.request.DefaultGptPromptRequest;
import ru.starkov.struct.yandex.gpt.dto.request.DefaultGtpSummarizationRequest;
import ru.starkov.struct.yandex.gpt.dto.request.GptRequest;
import ru.starkov.struct.yandex.gpt.dto.response.GptResponse;
import ru.starkov.struct.yandex.gpt.dto.request.DefaultGptChatRequest;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YandexGptService implements GptService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String GPT_MODEL_URL = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion";
    private final RestTemplate restTemplate;
    @Value("${infrastructure.yandex.speech-kit.api-key}")
    private String apiKey;

    @Override
    public GptResponseInfo summarize(GptSummarizationRequest request) {
        var requestHttpEntity = createHttpEntity(new DefaultGtpSummarizationRequest(request.request()));
        return processGptRequest(requestHttpEntity);
    }

    @Override
    public GptResponseInfo sendGptChatRequest(GptChatRequest request) {
        var requestHttpEntity = createHttpEntity(new DefaultGptChatRequest(request.chat(), request.gptModelUrl()));
        return processGptRequest(requestHttpEntity);
    }

    @Override
    public GptResponseInfo sendGptPromptRequest(GptPromptRequest request) {
        var requestHttpEntity = createHttpEntity(new DefaultGptPromptRequest(request.request(), request.prompt(), request.gptModelUrl()));
        return processGptRequest(requestHttpEntity);
    }

    private <T extends GptRequest> HttpEntity<T> createHttpEntity(T request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(AUTHORIZATION_HEADER, "Api-Key " + apiKey);
        return new HttpEntity<>(request, httpHeaders);
    }

    private GptResponseInfo processGptRequest(HttpEntity<? extends GptRequest> requestHttpEntity) {
        try {
            ResponseEntity<GptResponse> exchange = restTemplate
                    .exchange(GPT_MODEL_URL, HttpMethod.POST, requestHttpEntity, GptResponse.class);
            return handleResponse(exchange.getBody());
        } catch (HttpClientErrorException e) {
            return handleError(e);
        }
    }

    private GptResponseInfo handleResponse(GptResponse response) {
        Objects.requireNonNull(response);
        var gptTextResponse =  response.getResult().getAlternatives().stream()
                .map(alternative -> alternative.getMessage().getText())
                .collect(Collectors.joining());
        var totalTokens = new BigInteger(response.getResult().getUsage().getTotalTokens(), 10);
        return new GptResponseInfo(totalTokens, gptTextResponse);
    }

    private GptResponseInfo handleError(HttpClientErrorException e) {
        if (isBadRequestOrCannotGenerateAnswer(e)) {
            String errorMessage = "Извините, ответ на данную тему не может быть сгенерирован";
            return new GptResponseInfo(BigInteger.ZERO, errorMessage);
        } else {
            throw new RuntimeException("Error occurred while sending request: " + e.getMessage());
        }
    }

    private boolean isBadRequestOrCannotGenerateAnswer(HttpClientErrorException e) {
        String statusText = e.getStatusText();
        String responseBodyAsString = e.getResponseBodyAsString();
        return statusText.equalsIgnoreCase("bad request")
                || responseBodyAsString.contains("An answer to a given topic cannot be generated");
    }
}
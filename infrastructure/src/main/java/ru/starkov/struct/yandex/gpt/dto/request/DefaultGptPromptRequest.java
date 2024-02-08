package ru.starkov.struct.yandex.gpt.dto.request;

import java.util.List;

public class DefaultGptPromptRequest extends GptRequest {

    public DefaultGptPromptRequest(String requestData, String prompt, String gptModelUrl) {
        super(gptModelUrl, defaultCompletionOptions(), defaultMessage(requestData, prompt));
    }

    private static CompletionOptions defaultCompletionOptions() {
        var completionOptions = new GptRequest.CompletionOptions();
        completionOptions.setStream(false);
        completionOptions.setMaxTokens(2000);
        completionOptions.setTemperature(0.0);
        return completionOptions;
    }

    private static List<Message> defaultMessage(String requestData, String prompt) {
        var systemMessage = new GptRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setText(prompt);
        var userMessage = new GptRequest.Message();
        userMessage.setRole("user");
        userMessage.setText(requestData);
        return List.of(systemMessage, userMessage);
    }
}

package ru.starkov.struct.yandex.gpt.dto.request;

import java.util.List;

public class DefaultGtpSummarizationRequest extends GptRequest{

    private static final String Y_GPT_SUMMARIZATION_MODEL = "gpt://b1gqvj9jdr0pru8t82tr/summarization";

    public DefaultGtpSummarizationRequest(String requestData) {
        super(Y_GPT_SUMMARIZATION_MODEL, defaultCompletionOptions(), defaultMessage(requestData));
    }

    private static CompletionOptions defaultCompletionOptions() {
        var completionOptions = new GptRequest.CompletionOptions();
        completionOptions.setStream(false);
        completionOptions.setMaxTokens(2000);
        completionOptions.setTemperature(0.3);
        return completionOptions;
    }

    private static List<Message> defaultMessage(String requestData) {
        var systemMessage = new GptRequest.Message();
        systemMessage.setRole("system");
//        "Вы персональный ассистент собеседника. Вы предоставляете краткую структурированную информацию на основе сообщения собеседника."
        systemMessage.setText("кратко");

        var userMessage = new GptRequest.Message();
        userMessage.setRole("user");
        userMessage.setText(requestData);
        return List.of(systemMessage, userMessage);
    }
}

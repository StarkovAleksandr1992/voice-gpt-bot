package ru.starkov.struct.yandex.gpt.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultGptChatRequest extends GptRequest {

    public DefaultGptChatRequest(List<Map<String, String>> chatHistory, String gptModelUrl) {
        super(gptModelUrl, defaultCompletionOptions(), defaultMessage(chatHistory));
    }

    private static CompletionOptions defaultCompletionOptions() {
        var completionOptions = new GptRequest.CompletionOptions();
        completionOptions.setStream(false);
        completionOptions.setMaxTokens(2000);
        completionOptions.setTemperature(0.0);
        return completionOptions;
    }

    private static List<Message> defaultMessage(List<Map<String, String>> chatHistory) {
        List<Message> messages = new ArrayList<>();

        for (var record : chatHistory) {
            for (var entry : record.entrySet()) {
                var role = entry.getKey();
                var text = entry.getValue();
                var message = new GptRequest.Message();
                message.setRole(role);
                message.setText(text);
                messages.add(message);
            }
        }
        return messages;
    }
}

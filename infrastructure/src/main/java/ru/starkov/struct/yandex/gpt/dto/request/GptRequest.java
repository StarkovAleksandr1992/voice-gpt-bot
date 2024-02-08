package ru.starkov.struct.yandex.gpt.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptRequest {
    @JsonProperty("modelUri")
    private String modelUri;

    @JsonProperty("completionOptions")
    private CompletionOptions completionOptions;

    @JsonProperty("messages")
    private List<Message> messages;

    @Data
    public static class CompletionOptions {
        @JsonProperty("stream")
        private Boolean stream;

        @JsonProperty("temperature")
        private Double temperature;

        @JsonProperty("maxTokens")
        private Integer maxTokens;
    }

    @Data
    public static class Message {
        @JsonProperty("role")
        private String role;

        @JsonProperty("text")
        private String text;
    }
}

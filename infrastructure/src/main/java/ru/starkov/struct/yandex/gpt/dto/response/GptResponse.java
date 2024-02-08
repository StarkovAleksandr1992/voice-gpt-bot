package ru.starkov.struct.yandex.gpt.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GptResponse {
    private Result result;

    @Data
    public static class Result {
        private List<Alternative> alternatives;
        private Usage usage;
        private String modelVersion;

        @Data
        public static class Alternative {
            private Message message;
            private String status;

        }

        @Data
        public static class Message {
            @JsonProperty("role")
            private String role;
            @JsonProperty("text")
            private String text;

        }

        @Data
        public static class Usage {
            @JsonProperty("inputTextTokens")
            private String inputTextTokens;

            @JsonProperty("completionTokens")
            private String completionTokens;

            @JsonProperty("totalTokens")
            private String totalTokens;
        }
    }
}
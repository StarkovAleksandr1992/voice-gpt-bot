package ru.starkov.dom.entity;

import lombok.*;
import ru.starkov.dom.entity.identifier.RequestDataId;
import ru.starkov.dom.entity.identifier.RequestDataIdGenerator;
import ru.starkov.common.mark.Entity;

import java.util.Objects;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public final class RequestData<T> implements Entity {

    private final RequestDataId requestDataId;
    private final T data;
    private final RequestDataType dataType;

    private String prompt;
    private String recognizedVoiceRequest;
    private Integer voiceRequestDurationInSeconds;


    public static RequestData<?> createTextRequestData(
            @NonNull RequestDataIdGenerator requestDataIdGenerator,
            @NonNull String textData,
            @NonNull RequestDataType dataType) {

        Objects.requireNonNull(requestDataIdGenerator, "RequestDataIdGenerator cannot be null");
        Objects.requireNonNull(textData, "Text data cannot be null");
        Objects.requireNonNull(dataType, "Data type cannot be null");

        return RequestData.builder()
                .requestDataId(requestDataIdGenerator.generate())
                .data(textData)
                .dataType(dataType)
                .build();
    }

    public static RequestData<?> createTextRequestDataWithPrompt(
            @NonNull RequestDataIdGenerator requestDataIdGenerator,
            @NonNull String textData,
            @NonNull RequestDataType dataType,
            @NonNull String prompt) {

        Objects.requireNonNull(requestDataIdGenerator, "RequestDataIdGenerator cannot be null");
        Objects.requireNonNull(textData, "Text data cannot be null");
        Objects.requireNonNull(dataType, "Data type cannot be null");
        Objects.requireNonNull(prompt, "Prompt cannot be null");

        return RequestData.builder()
                .requestDataId(requestDataIdGenerator.generate())
                .data(textData)
                .dataType(dataType)
                .prompt(prompt)
                .build();
    }

    public static RequestData<?> createVoiceRequestData(
            @NonNull RequestDataIdGenerator requestDataIdGenerator,
            byte[] voiceData,
            @NonNull RequestDataType dataType,
            @NonNull Integer voiceRequestDuration) {

        Objects.requireNonNull(requestDataIdGenerator, "RequestDataIdGenerator cannot be null");
        if (voiceData == null || voiceData.length == 0) {
            throw new IllegalArgumentException("Voice data cannot be empty or null");
        }
        Objects.requireNonNull(dataType, "Data type cannot be null");
        Objects.requireNonNull(voiceRequestDuration, "Voice request duration cannot be null");

        return RequestData.builder()
                .requestDataId(requestDataIdGenerator.generate())
                .data(voiceData)
                .dataType(dataType)
                .voiceRequestDurationInSeconds(voiceRequestDuration)
                .build();
    }

    // used by mapper
    public static RequestData<?> createNewInstance(
            @NonNull RequestDataId id,
            @NonNull Object data,
            @NonNull RequestDataType dataType,
            String prompt,
            String recognizedVoiceRequest,
            Integer voiceRequestDuration) {

        Objects.requireNonNull(id, "RequestDataId cannot be null.");
        Objects.requireNonNull(data, "Data object cannot be null.");
        Objects.requireNonNull(dataType, "RequestDataType cannot be null.");

        return RequestData.builder()
                .requestDataId(id)
                .data(data)
                .dataType(dataType)
                .prompt(prompt)
                .recognizedVoiceRequest(recognizedVoiceRequest)
                .voiceRequestDurationInSeconds(voiceRequestDuration)
                .build();
    }

    public enum RequestDataType {
        TEXT,
        VOICE
    }
}

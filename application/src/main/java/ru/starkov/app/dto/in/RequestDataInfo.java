package ru.starkov.app.dto.in;

public record RequestDataInfo<T>(Long customerTelegramId, RequestDataInfo.DataType dataType, T data, String prompt, Integer voiceRequestDuration) {

    public enum DataType {
        TEXT,
        VOICE
    }
}

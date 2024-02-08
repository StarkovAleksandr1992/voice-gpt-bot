package ru.starkov.app.dto.in;


public record CustomerSettingsGptModelInfo(Long customerTelegramId, GptModel gptModel) {
    public enum GptModel{
        YANDEX_GPT,
        YANDEX_GPT_LITE
    }
}

package ru.starkov.app.dto.out;

public record SummarizationVoiceRequestResultInfo(Long customerTelegramId, String recognizedText, String summarizedText) {
}

package ru.starkov.app.dto.in;

public record CustomerSettingsActionTypeInfo(Long customerTelegramId, ActionType actionType) {

    public enum ActionType{
        RECOGNIZE,
        SUMMARIZE,
        ASK_GPT,
        ASK_GPT_WITH_PROMPT
    }
}

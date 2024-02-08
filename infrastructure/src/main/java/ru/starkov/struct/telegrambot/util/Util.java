package ru.starkov.struct.telegrambot.util;

import lombok.Getter;

public class Util {

    public static final String HELP = "help";
    public static final String SETTINGS = "settings";
    public static final String REGISTRATION = "registration";
    public static final String CHATS = "chats";
    public static final String ACTION = "action";
    public static final String MODEL = "model";


    @Getter
    public enum BotCommands {
        START("/start", "Начало работы с ботом"),
        HELP("/help", "Описание режимов работы бота"),
        SETTINGS("/settings", "Изменить режим работы бота"),
        CHATS("/chats", "Управление чатами.");
        private final String value;
        private final String description;

        BotCommands(String value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    @Getter
    public enum BotActionType {
        RECOGNIZE("recognize", "Расшифровка голосовых сообщений"),
        SUMMARIZE("summarize", "Выделение основных тезисов сообщения"),
        ASK_GPT("chat", "Режим чата с YandexGPT"),
        ASK_GPT_WITH_PROMPT("prompt", "Промпт режим с YandexGPT");

        private final String value;
        private final String description;
        BotActionType(String value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    @Getter
    public enum BotModelType {
        YANDEX_GPT("default"),
        YANDEX_GPT_LITE("lite");
        private final String value;

        BotModelType(String value) {
            this.value = value;
        }
    }
}

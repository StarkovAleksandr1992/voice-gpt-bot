package ru.starkov.dom.entity;

import lombok.Data;
import ru.starkov.dom.entity.identifier.ChatId;
import ru.starkov.dom.entity.identifier.ChatIdGenerator;
import ru.starkov.dom.value.CustomerMessage;
import ru.starkov.dom.value.GptMessage;
import ru.starkov.dom.value.Message;
import ru.starkov.dom.value.SystemMessage;

import java.util.ArrayList;
import java.util.List;

@Data
public class Chat {

    private static final String SYSTEM_MESSAGE = "Ты ассистент, который отвечает на вопросы пользователя";
    private final ChatId chatId;

    private Status status;
    private List<Message> chatHistory;


    public static Chat create(ChatIdGenerator chatIdGenerator) {
        Chat chat = new Chat(chatIdGenerator.generate());
        chat.setStatus(Status.ACTIVE);
        return chat;
    }

    public void addMessage(String message) {
        if (chatHistory == null || chatHistory.isEmpty()) {
            chatHistory = new ArrayList<>();
            chatHistory.add(new SystemMessage(SYSTEM_MESSAGE));
            chatHistory.add(new CustomerMessage(message));
        } else if (chatHistory.size() % 2 == 0) {
            chatHistory.add(new GptMessage(message));
        } else {
            chatHistory.add(new CustomerMessage(message));
        }
    }

    public enum Status {
        ACTIVE,
        CLOSED
    }
}

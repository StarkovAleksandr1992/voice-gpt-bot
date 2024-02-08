package ru.starkov.app.dto.out.gpt_request;

import ru.starkov.dom.entity.Chat;
import ru.starkov.dom.value.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record GptChatRequest(List<Map<String, String>> chat, String gptModelUrl) {
    public static GptChatRequest create(Chat chat, String gptModelUrl) {
        List<Map<String, String>> chatList = new ArrayList<>();
        for (Message message : chat.getChatHistory()) {
            var senderName = message.getChatRole().getSenderName();
            var chatMessage = message.getChatMessage();
            chatList.add(Map.of(senderName, chatMessage));
        }
        return new GptChatRequest(chatList, gptModelUrl);
    }
}

package ru.starkov.struct.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.starkov.app.dto.in.NewChatRequestInfo;
import ru.starkov.struct.telegrambot.MessageFactory;
import ru.starkov.struct.telegrambot.TelegramBot;
import ru.starkov.struct.telegrambot.util.Util;
import ru.starkov.app.usecase.CreateNewChat;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatController {
    private final TelegramBot telegramBot;
    private final CreateNewChat createNewChat;
    private final MessageFactory factory;

    @SneakyThrows
    public void sendChatsOptions(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        if (data.endsWith("new")) {
            createNewChat.execute(new NewChatRequestInfo(callbackQuery.getFrom().getId()));
            var editMessage = new EditMessageReplyMarkup();
            editMessage.setMessageId(callbackQuery.getMessage().getMessageId());
            editMessage.setChatId(callbackQuery.getFrom().getId());
            editMessage.setReplyMarkup(factory.createInlineKeyboard(List.of(factory.createButton("Успешно!", Util.CHATS, "successful"))));
            telegramBot.execute(editMessage);
        }
    }

    @SneakyThrows
    public void sendChatsOptions(Long customerTelegramId) {
        String chatsInfoMessage = "В этом разделе вы управляете своими чатами";
        var message = factory.createSendMessage(customerTelegramId, chatsInfoMessage);
        var keyboard = factory.createInlineKeyboard(List.of(factory.createButton("Создать новый чат", Util.CHATS, "new")));
        message.setReplyMarkup(keyboard);
        telegramBot.execute(message);
    }
}

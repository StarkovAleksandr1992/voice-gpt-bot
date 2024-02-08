package ru.starkov.struct.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.starkov.app.dto.in.CustomerInfo;
import ru.starkov.struct.telegrambot.MessageFactory;
import ru.starkov.struct.telegrambot.TelegramBot;
import ru.starkov.app.usecase.CreateCustomer;

import java.util.List;

import static ru.starkov.struct.telegrambot.util.Util.*;

@Component
@RequiredArgsConstructor
public class RegistrationController {
    private final CreateCustomer createCustomer;
    private final TelegramBot telegramBot;
    private final MessageFactory factory;

    public void processRegistration(CustomerInfo customerInfo) {
        createCustomer.execute(customerInfo);
    }

    @SneakyThrows
    public void removeRegistrationButton(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();
        var data = callbackQuery.getData();
        if (data.endsWith("successful")) {
            EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
            editMessageReplyMarkup.setChatId(chatId);
            editMessageReplyMarkup.setMessageId(messageId);
            telegramBot.execute(editMessageReplyMarkup);
        } else {
            var keyboard = factory.createInlineKeyboard(List.of(
                    factory.createButton("Успешно!", REGISTRATION, "successful")));
            EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
            editMessageReplyMarkup.setChatId(chatId);
            editMessageReplyMarkup.setMessageId(messageId);
            editMessageReplyMarkup.setReplyMarkup(keyboard);
            telegramBot.execute(editMessageReplyMarkup);
        }
    }

    @SneakyThrows
    public void alreadyRegistered(CustomerInfo customerInfo) {
        telegramBot.execute(factory.createSendMessage(customerInfo.customerTelegramId(), "Вы уже зарегистрированы!"));
    }
}

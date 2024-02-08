package ru.starkov.struct.telegrambot.handler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.starkov.app.dto.in.CustomerInfo;
import ru.starkov.struct.telegrambot.controller.*;
import ru.starkov.struct.telegrambot.util.Util;
import ru.starkov.app.usecase.CheckCustomerState;

@Component
@RequiredArgsConstructor
public class UpdateHandler {

    private final CheckCustomerState checkCustomerState;

    private final GreetingController greetingController;
    private final RegistrationController registrationController;
    private final CustomerSettingsController customerSettingsController;
    private final CustomerRequestController customerRequestController;
    private final HelpController helpController;
    private final ChatController chatController;


    @SneakyThrows
    public void handleUpdate(Update update) {
        var customerInfo = createCustomerInfo(update);
        boolean isRegistered = checkCustomerState.checkCustomerRegistrationStatus(customerInfo);
        if (update.hasMessage()) {
            handleMessageUpdate(update.getMessage(), customerInfo, isRegistered);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQueryUpdate(update.getCallbackQuery(), customerInfo, isRegistered);
        }
    }

    private void handleMessageUpdate(Message message, CustomerInfo customerInfo, boolean isRegistered) {
        if (message.hasText()) {
            handleTextMessage(message, customerInfo, isRegistered);
        } else if (message.hasVoice() || message.hasAudio()) {
            if (!isRegistered) {
                greetingController.sendGreetingMessage(customerInfo);
            } else {
                customerRequestController.processRequest(message);
            }
        }
    }

    private void handleTextMessage(Message message, CustomerInfo customerInfo, boolean isRegistered) {
        var text = message.getText();
        if (text.equals(Util.BotCommands.START.getValue()) || !isRegistered) {
            if (isRegistered) {
                registrationController.alreadyRegistered(customerInfo);
            } else {
                greetingController.sendGreetingMessage(customerInfo);
            }
        } else if (text.equals(Util.BotCommands.SETTINGS.getValue())) {
            customerSettingsController.sendSettingsOptions(customerInfo.customerTelegramId());
        } else if (text.equals(Util.BotCommands.HELP.getValue())) {
            helpController.help(customerInfo);
        } else if (text.equals(Util.BotCommands.CHATS.getValue())) {
            chatController.sendChatsOptions(customerInfo.customerTelegramId());
        } else {
            customerRequestController.processRequest(message);
        }
    }

    private void handleCallbackQueryUpdate(CallbackQuery callbackQuery, CustomerInfo customerInfo, boolean isRegistered) {
        String data = callbackQuery.getData();
        if (data.startsWith(Util.REGISTRATION)) {
            if (!isRegistered) {
                registrationController.processRegistration(customerInfo);
            }
            registrationController.removeRegistrationButton(callbackQuery);
        } else if (data.startsWith(Util.SETTINGS)) {
            customerSettingsController.sendSettingsOptions(callbackQuery);
        } else if (data.startsWith(Util.HELP)) {
            helpController.help(callbackQuery);
        } else if (data.startsWith(Util.CHATS)) {
            chatController.sendChatsOptions(callbackQuery);
        }
    }


    private CustomerInfo createCustomerInfo(Update update) {
        User user = getUserFromUpdate(update);
        var chatId = user.getId();
        var firstName = user.getFirstName();
        var lastName = user.getLastName();
        var languageCode = user.getLanguageCode();
        return new CustomerInfo(chatId, firstName, lastName, languageCode);
    }


    private User getUserFromUpdate(Update update) {
        User user;
        if (update.hasCallbackQuery()) {
            user = update.getCallbackQuery().getFrom();
        } else if (update.hasMessage()) {
            user = update.getMessage().getFrom();
        } else if (update.hasMyChatMember()) {
            user = update.getMyChatMember().getFrom();
        } else {
            throw new RuntimeException("Unhandled update type");
        }
        return user;
    }
}

package ru.starkov.struct.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.starkov.app.dto.in.CustomerSettingsActionTypeInfo;
import ru.starkov.app.dto.in.CustomerSettingsGptModelInfo;
import ru.starkov.struct.telegrambot.MessageFactory;
import ru.starkov.struct.telegrambot.TelegramBot;
import ru.starkov.struct.telegrambot.util.Util.BotActionType;
import ru.starkov.app.usecase.ChangeCustomerSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ru.starkov.struct.telegrambot.util.Util.*;

@Component
@RequiredArgsConstructor
public class CustomerSettingsController {
    private final ChangeCustomerSettings changeCustomerSettings;
    private final TelegramBot telegramBot;
    private final MessageFactory factory;
    private final Map<String, CustomerSettingsActionTypeInfo.ActionType> actionTypeMap = Map.of(
            BotActionType.RECOGNIZE.getValue(), CustomerSettingsActionTypeInfo.ActionType.RECOGNIZE,
            BotActionType.SUMMARIZE.getValue(), CustomerSettingsActionTypeInfo.ActionType.SUMMARIZE,
            BotActionType.ASK_GPT.getValue(), CustomerSettingsActionTypeInfo.ActionType.ASK_GPT,
            BotActionType.ASK_GPT_WITH_PROMPT.getValue(), CustomerSettingsActionTypeInfo.ActionType.ASK_GPT_WITH_PROMPT
    );

    private final Map<String, CustomerSettingsGptModelInfo.GptModel> gptModelMap = Map.of(
            BotModelType.YANDEX_GPT.getValue(), CustomerSettingsGptModelInfo.GptModel.YANDEX_GPT,
            BotModelType.YANDEX_GPT_LITE.getValue(), CustomerSettingsGptModelInfo.GptModel.YANDEX_GPT_LITE
    );

    @SneakyThrows
    public void sendSettingsOptions(Long customerTelegramId) {
        var settingsOptions = "Выберите опции которые хотите изменить";
        var keyboardMarkup = factory.createInlineKeyboard(List.of(
                factory.createButton("Режим работы бота", SETTINGS, ACTION),
                factory.createButton("Языковая модель", SETTINGS, MODEL)));
        var message = factory.createSendMessage(customerTelegramId, settingsOptions);
        message.setReplyMarkup(keyboardMarkup);
        telegramBot.execute(message);
    }

    @SneakyThrows
    public void sendSettingsOptions(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData();
        if (data.contains(ACTION)) {
            var action = Arrays.stream(data.split(" ")).toList().getLast();
            if (actionTypeMap.containsKey(action)) {
                changeCustomerSettings.changeDefaultActionType(new CustomerSettingsActionTypeInfo(callbackQuery.getFrom().getId(), actionTypeMap.get(action)));
            } else {
                var keyboard = factory.createInlineKeyboard(List.of(
                        factory.createButton(BotActionType.RECOGNIZE.getDescription(), SETTINGS, ACTION, BotActionType.RECOGNIZE.getValue()),
                        factory.createButton(BotActionType.SUMMARIZE.getDescription(), SETTINGS, ACTION, BotActionType.SUMMARIZE.getValue()),
                        factory.createButton(BotActionType.ASK_GPT.getDescription(), SETTINGS, ACTION, BotActionType.ASK_GPT.getValue()),
                        factory.createButton(BotActionType.ASK_GPT_WITH_PROMPT.getDescription(), SETTINGS, ACTION, BotActionType.ASK_GPT_WITH_PROMPT.getValue())));
                var message = new EditMessageText();
                message.setChatId(callbackQuery.getFrom().getId());
                message.setMessageId(callbackQuery.getMessage().getMessageId());
                message.setText("Выберите действие по умолчанию");
                message.setReplyMarkup(keyboard);
                telegramBot.execute(message);
            }
        } else if (data.contains(MODEL)) {
            var model = Arrays.stream(data.split(" ")).toList().getLast();
            if (gptModelMap.containsKey(model)) {
                changeCustomerSettings.changeDefaultGptModel(new CustomerSettingsGptModelInfo(callbackQuery.getFrom().getId(), gptModelMap.get(model)));
            } else {
                var keyboard = factory.createInlineKeyboard(List.of(
                        factory.createButton(BotModelType.YANDEX_GPT.name(), SETTINGS, MODEL, BotModelType.YANDEX_GPT.getValue()),
                        factory.createButton(BotModelType.YANDEX_GPT_LITE.name(), SETTINGS, MODEL, BotModelType.YANDEX_GPT_LITE.getValue())));
                var message = new EditMessageText();
                message.setChatId(callbackQuery.getFrom().getId());
                message.setMessageId(callbackQuery.getMessage().getMessageId());
                message.setText("Выберите модель по умолчанию");
                message.setReplyMarkup(keyboard);
                telegramBot.execute(message);
            }
        }
    }
}


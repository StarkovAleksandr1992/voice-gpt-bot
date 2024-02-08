package ru.starkov.struct.telegrambot.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.starkov.app.dto.in.CustomerSettingsActionTypeInfo;
import ru.starkov.app.dto.in.CustomerSettingsGptModelInfo;
import ru.starkov.app.dto.in.NewChatRequestInfo;
import ru.starkov.app.dto.out.*;
import ru.starkov.struct.telegrambot.MessageFactory;

import java.util.List;

import static ru.starkov.struct.telegrambot.util.Util.*;

@Component
@RequiredArgsConstructor
public class ApplicationDtoToTelegramSendMessageMapper {

    private final MessageFactory factory;


    public List<SendMessage> summarizationVoiceRequestResultToSendMessage(SummarizationVoiceRequestResultInfo resultInfo) {
        var customerTelegramId = resultInfo.customerTelegramId();
        var summarizedText = "*Кратко:* \n" + resultInfo.summarizedText();
        var recognizedText = "*Полная расшифровка:* \n" + resultInfo.recognizedText();

        var fullMessage = createMessage(customerTelegramId, summarizedText + "\n" + "\n" + recognizedText);
        var lengthOfFullMessage = fullMessage.getText().length();

        if (lengthOfFullMessage <= MessageFactory.MAX_LENGTH_OF_TELEGRAM_MESSAGE) {
            return List.of(fullMessage);
        } else {
            var shortMessage = createMessage(customerTelegramId, summarizedText);
            var longMessage = createMessage(customerTelegramId, recognizedText);

            return List.of(shortMessage, longMessage);
        }
    }

    public SendMessage summarizationTextRequestResultToSendMessage(SummarizationTextRequestResultInfo resultInfo) {
        var customerTelegramId = resultInfo.customerTelegramId();
        var summarizedText = "*Кратко:* \n" + resultInfo.summarizedText();
        return createMessage(customerTelegramId, summarizedText);
    }

    public SendMessage gptResponseRequestResultToSendMessage(GptRequestResultInfo resultInfo) {
        var customerTelegramId = resultInfo.customerTelegramId();
        var gptResponse = resultInfo.gptResponse();
        return createMessage(customerTelegramId, gptResponse);
    }

    public List<SendMessage> successfulRegistrationDataToSendMessage(SuccessfulRegistrationInfo data) {
        var customerTelegramId = data.customerTelegramId();
        var helloMessage = """
                Вы успешно зарегистрировались!
                Ваш баланс токенов пополнен на 10 000.
                По умолчанию бот работает в режиме чата с моделью YandexGPT.
                В меню можно изменить режим работы бота и языковую модель.
                Предложения по улучшению работы бота можно отправить на адрес электронной почты: voice-gpt-bot@yandex.com.""";
        var message = createMessage(customerTelegramId, helloMessage);
        var keyboard = factory.createInlineKeyboard(List.of(
                factory.createButton("Подробнее о режимах работы", "help modes"),
                factory.createButton("Подробнее о токенах", "help tokens")));
        message.setReplyMarkup(keyboard);
        return List.of(message);
    }

    public SendMessage recognitionRequestResultToSendMessage(RecognitionRequestResultInfo resultInfo) {
        var customerTelegramId = resultInfo.customerTelegramId();
        var recognizedText = resultInfo.recognizedText();
        return createMessage(customerTelegramId, recognizedText);
    }

    public SendMessage customerSettingsInfoToSendMessage(CustomerSettingsActionTypeInfo actionTypeInfo) {
        var prefix = "Настройки успешно изменены. Действие по умолчанию: ";
        var customerTelegramId = actionTypeInfo.customerTelegramId();
        var actionType = actionTypeInfo.actionType();
        return switch (actionType) {
            case RECOGNIZE -> createMessage(customerTelegramId, prefix + BotActionType.RECOGNIZE.getDescription().toLowerCase());
            case SUMMARIZE -> createMessage(customerTelegramId, prefix + BotActionType.SUMMARIZE.getDescription().toLowerCase());
            case ASK_GPT -> createMessage(customerTelegramId, prefix + BotActionType.ASK_GPT.getDescription().toLowerCase());
            case ASK_GPT_WITH_PROMPT -> createMessage(customerTelegramId, prefix + BotActionType.ASK_GPT_WITH_PROMPT.getDescription().toLowerCase());
        };
    }

    public SendMessage customerSettingsGptModelInfoToSendMessage(CustomerSettingsGptModelInfo modelInfo) {
        var prefix = "Настройки успешно изменены. модель по умолчанию: ";
        var customerTelegramId = modelInfo.customerTelegramId();
        var gptModel = modelInfo.gptModel();
        return switch (gptModel) {
            case YANDEX_GPT -> createMessage(customerTelegramId, prefix + "YandexGPT");
            case YANDEX_GPT_LITE -> createMessage(customerTelegramId, prefix + "YandexGPT Lite");
        };
    }

    public SendMessage newChatRequestInfoToSendMessage(NewChatRequestInfo info) {
        var customerTelegramId = info.customerTelegramId();
        var message = "Новый чат успешно создан";
        return createMessage(customerTelegramId, message);
    }

    public SendMessage insufficientBalanceInfoToSendMessage(InsufficientBalanceInfo insufficientBalanceInfo) {
        var text = String.format("""
                Для выполнения запроса недостаточно токенов.
                Текущий баланс: %s
                """, insufficientBalanceInfo.balance());

        var message = createMessage(insufficientBalanceInfo.customerTelegramId(), text);
        var inlineKeyboard = factory.createInlineKeyboard(List.of(factory.createButton("Пополнить баланс", "balance")));
        message.setReplyMarkup(inlineKeyboard);
        return message;
    }

    private SendMessage createMessage(Long chatId, String text) {
        return factory.createSendMessage(chatId, text);
    }
}

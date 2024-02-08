package ru.starkov.struct.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.starkov.app.dto.in.CustomerInfo;
import ru.starkov.struct.telegrambot.MessageFactory;
import ru.starkov.struct.telegrambot.TelegramBot;
import ru.starkov.struct.telegrambot.util.Util;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GreetingController {

    private static final String GREETINGS = """
            Здравствуйте!
            Данный бот предоставляет доступ к языковым генеративным моделям Yandex.
            Бот работает с текстовыми и голосовыми сообщениями длительностью до 5 минут.
            У бота четыре режима работы бота, которые можно переключать в меню:
            1. Расшифровка голосовых сообщений.
            2. Выделение основной информации из голосовых и текстовых сообщений.
            3. Режим чата с языковой моделью.
            4. Промпт режим с языковой моделью.
            Нажимая кнопку "Зарегистрироваться", вы соглашаетесь с условиями, согласно которым запрещено отправлять боту конфиденциальную информацию, такую как персональные идентификационные данные, финансовую информацию, медицинские данные и прочее.
            """;
    private final TelegramBot telegramBot;
    private final MessageFactory factory;


    @SneakyThrows
    public void sendGreetingMessage(CustomerInfo customerInfo) {
        telegramBot.execute(createGreetingMessage(customerInfo));
    }

    private SendMessage createGreetingMessage(CustomerInfo info) {
        var customerTelegramId = info.customerTelegramId();

        var keyboard = factory.createInlineKeyboard(List.of(
                factory.createButton("Зарегистрироваться", Util.REGISTRATION),
                factory.createButton("Подробнее о режимах работы", Util.HELP, "modes")));

        return factory.createSendMessageWithKeyboard(customerTelegramId, GREETINGS, keyboard);
    }
}

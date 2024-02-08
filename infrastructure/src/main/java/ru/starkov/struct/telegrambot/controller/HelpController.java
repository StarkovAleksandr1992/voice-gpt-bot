package ru.starkov.struct.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.starkov.app.dto.in.CustomerInfo;
import ru.starkov.struct.telegrambot.MessageFactory;
import ru.starkov.struct.telegrambot.TelegramBot;
import ru.starkov.struct.telegrambot.util.Util;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HelpController {

    private static final String RECOGNIZE_HELP_MESSAGE = """
            *Расшифровка голосовых сообщений:*
            В этом режиме боту можно отправить голосовое сообщение продолжительностью *до 5 минут*.
            В ответ бот предоставит вам полную расшифровку переданного сообщения.
            """;

    private static final String SUMMARIZE_HELP_MESSAGE = """
            *Выделение основных тезисов сообщения:*
            В этом режиме боту можно передать голосовое сообщение (до 5 минут) или отправить текстовое.
            При отправке голосового сообщения бот вернет ответ, выделяя основные тезисы сообщения, а также предоставит полную расшифровку.
            Если отправлено текстовое сообщение, бот вернет ответ с основными тезисами.
            """;

    private static final String ASK_GPT_HELP_MESSAGE = """
            *Режим чата с YandexGPT:*
            В этом режиме боту можно отправлять текстовые и голосовые сообщения.
            Бот будет отвечать в режиме чата, учитывая контекст предыдущих сообщений.
            Если нужно обновить контекст беседы, в меню бота во вкладке чаты, выберите создать новый чат.
            """;

    private static final String ASK_GPT_WITH_PROMPT_HELP_MESSAGE = """
            *Промпт режим с YandexGPT:*
            В этом режиме боту можно отправлять только текстовые сообщения заданного формата.
            Первая часть сообщения - контекст для языковой модели, вторая часть - сам запрос.
            Разделяйте части сообщения с помощью @@@.
            *Пример:*
            Ты поэт эпохи возрождения, возможно, ты знаком с Данте Алигьери. @@@ Дополни стихотворение:
            "Муха села на варенье, вот и все стихотворенье".
            """;
    private static final String TOKENS_DESCRIPTION = """
            Токены представляют собой единицы измерения, используемые для определения стоимости генерации текста. Они охватывают как токены запроса (промпта), так и токены ответа.
                
            Расчет стоимости зависит от следующих параметров запроса к YandexGPT API:
                
            *Модель:*
            Указывает на конкретную модель, используемую для генерации текста. Разные модели могут иметь различные тарифы за токены.
            *Режим работы модели:*
            Учитывает режим, в котором модель работает. Разные режимы могут влиять на стоимость токенов.
                
            Структура запроса включает в себя промпт (входной текст) и соответствующий ответ. Количество токенов в промпте и ответе может варьироваться для различных моделей.
                
            Количество тарифицирующих юнитов рассчитывается на основе общего числа токенов в промпте и ответе, округленного вверх с применением соответствующего коэффициента. Этот метод обеспечивает точный и справедливый расчет стоимости генерации текста в зависимости от выбранной модели и ее режима работы.
                
            1000 токенов примерно соответствуют 750 словам для YandexGPT Lite и 250 словам для YandexGPT.
            В режиме чата расход токенов максимален, так как для сохранения контекста диалога в запросе к модели передается история чата.
            """;

    private final Map<String, String> helpMessages = Map.of(
            Util.BotActionType.RECOGNIZE.getValue(), RECOGNIZE_HELP_MESSAGE,
            Util.BotActionType.SUMMARIZE.getValue(), SUMMARIZE_HELP_MESSAGE,
            Util.BotActionType.ASK_GPT.getValue(), ASK_GPT_HELP_MESSAGE,
            Util.BotActionType.ASK_GPT_WITH_PROMPT.getValue(), ASK_GPT_WITH_PROMPT_HELP_MESSAGE);

    private final TelegramBot telegramBot;
    private final MessageFactory factory;


    @SneakyThrows
    public void help(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();

        if (data.contains("modes")) {
            var botAction = helpMessages.entrySet().stream()
                    .filter(entry -> data.endsWith(entry.getKey()))
                    .findFirst();

            if (botAction.isPresent()) {
                telegramBot.execute(factory.createSendMessage(callbackQuery.getFrom().getId(), botAction.get().getValue()));
            } else {
                displayModeOptions(callbackQuery.getFrom().getId());
            }
        } else if (data.contains("tokens")) {
            telegramBot.execute(factory.createSendMessage(callbackQuery.getFrom().getId(), TOKENS_DESCRIPTION));
        }
    }

    @SneakyThrows
    public void help(CustomerInfo customerInfo) {
        var keyboard = factory.createInlineKeyboard(List.of(factory.createButton("Описание режимов работы", Util.HELP, "modes"),
                factory.createButton("Описание токенов", Util.HELP, "tokens")));
        var message = factory.createSendMessageWithKeyboard(customerInfo.customerTelegramId(), "С чем вам нужна помощь?", keyboard);
        telegramBot.execute(message);
    }

    private void displayModeOptions(Long customerId) throws TelegramApiException {
        var modeDetails = "О каком режиме работы вы хотите узнать подробнее?";
        var keyboard = createInlineKeyboardWithButtons();
        var messageWithKeyboard = factory.createSendMessageWithKeyboard(customerId, modeDetails, keyboard);
        telegramBot.execute(messageWithKeyboard);
    }

    private InlineKeyboardMarkup createInlineKeyboardWithButtons() {
        var keyboardButtons = List.of(
                createButtonForHelp(Util.BotActionType.RECOGNIZE),
                createButtonForHelp(Util.BotActionType.SUMMARIZE),
                createButtonForHelp(Util.BotActionType.ASK_GPT),
                createButtonForHelp(Util.BotActionType.ASK_GPT_WITH_PROMPT));
        return factory.createInlineKeyboard(keyboardButtons);
    }

    private List<InlineKeyboardButton> createButtonForHelp(Util.BotActionType botActionType) {
        return factory.createButton(botActionType.getDescription(), Util.HELP, "modes", botActionType.getValue());
    }
}

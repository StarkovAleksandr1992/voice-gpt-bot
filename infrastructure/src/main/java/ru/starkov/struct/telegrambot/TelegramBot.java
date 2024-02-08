package ru.starkov.struct.telegrambot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.starkov.struct.telegrambot.handler.UpdateHandler;

import java.util.List;

import static ru.starkov.struct.telegrambot.util.Util.BotCommands.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final UpdateHandler updateHandler;
    private final List<BotCommand> BOT_MENU_COMMANDS = List.of(
            new BotCommand(HELP.getValue(), HELP.getDescription()),
            new BotCommand(SETTINGS.getValue(), SETTINGS.getDescription()),
            new BotCommand(CHATS.getValue(), CHATS.getDescription()));

    @Value("${infrastructure.telegram.bot.name}")
    private String botName;


    public TelegramBot(@Value("${infrastructure.telegram.bot.api-key}") String botToken,
                       @Lazy UpdateHandler updateHandler) {
        super(botToken);
        this.updateHandler = updateHandler;
    }

    @PostConstruct
    private void registerBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            this.execute(new SetMyCommands(BOT_MENU_COMMANDS, new BotCommandScopeDefault(), "ru"));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}

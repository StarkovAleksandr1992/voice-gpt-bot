package ru.starkov.struct.telegrambot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class MessageFactory {
    public static final int MAX_LENGTH_OF_TELEGRAM_MESSAGE = 4095;

    public SendMessage createSendMessage(Long chatId, String text) {
        var message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chatId);
        message.setText(trimMessageIfTooLong(text));
        return message;
    }

    public SendMessage createSendMessageWithKeyboard(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        var message = createSendMessage(chatId, text);
        message.setReplyMarkup(keyboard);
        return message;
    }

    private String trimMessageIfTooLong(String text) {
        var lengthOfMessage = text.length();
        if (lengthOfMessage > MAX_LENGTH_OF_TELEGRAM_MESSAGE) {
            return text.substring(0, MAX_LENGTH_OF_TELEGRAM_MESSAGE);
        }
        return text;
    }

    public InlineKeyboardMarkup createInlineKeyboard(List<List<InlineKeyboardButton>> keyboardButtons) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardButtons);
        return keyboardMarkup;
    }

    public List<InlineKeyboardButton> createButton(String buttonText, String...callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton(buttonText);
        button.setCallbackData(String.join(" ", callbackData));
        return List.of(button);
    }
}

package ru.starkov.struct.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Voice;
import ru.starkov.app.dto.in.RequestDataInfo;
import ru.starkov.struct.telegrambot.TelegramBot;
import ru.starkov.app.usecase.CreateCustomerRequest;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class CustomerRequestController {

    private final TelegramBot telegramBot;
    private final CreateCustomerRequest createCustomerRequest;


    public void processRequest(Message message) {
        createCustomerRequest.execute(createRequestDataInfo(message));
    }

    public RequestDataInfo<?> createRequestDataInfo(Message message) {
        Long chatId = message.getFrom().getId();
        if (message.hasVoice()) {
            return new RequestDataInfo<>(chatId, RequestDataInfo.DataType.VOICE, getVoiceMessage(message), null, message.getVoice().getDuration());
        } else if (message.hasText()) {
            String messageText = message.getText();
            if (messageText.contains("@@@")) {
                String[] promptAndRequest = messageText.split("@@@");
                return new RequestDataInfo<>(chatId, RequestDataInfo.DataType.TEXT, promptAndRequest[1], promptAndRequest[0], null);
            } else {
                return new RequestDataInfo<>(chatId, RequestDataInfo.DataType.TEXT, messageText, null, null);
            }
        } else {
            throw new RuntimeException(String.format("Unknown data type: '%s'", message));
        }
    }

    @SneakyThrows
    @SuppressWarnings("all")
    private byte[] getVoiceMessage(Message message) {
        Voice voice = message.getVoice();
        GetFile getFile = new GetFile();
        getFile.setFileId(voice.getFileId());
        File execute = telegramBot.execute(getFile);
        URL fileUrl = new URI(execute.getFileUrl(telegramBot.getBotToken())).toURL();
        try (InputStream inputStream = fileUrl.openStream()) {
            return inputStream.readAllBytes();
        }
    }
}

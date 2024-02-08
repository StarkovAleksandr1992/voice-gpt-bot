package ru.starkov.struct.telegrambot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.starkov.app.dto.in.CustomerSettingsActionTypeInfo;
import ru.starkov.app.dto.in.CustomerSettingsGptModelInfo;
import ru.starkov.app.dto.in.NewChatRequestInfo;
import ru.starkov.app.dto.in.RequestDataInfo;
import ru.starkov.app.dto.out.*;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.struct.telegrambot.mapper.ApplicationDtoToTelegramSendMessageMapper;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements CustomerNotificationService {

    private final TelegramBot telegramBot;
    private final ApplicationDtoToTelegramSendMessageMapper mapper;


    @Override
    @SneakyThrows
    public void notifyCustomerAboutSuccessfulRegistration(SuccessfulRegistrationInfo successfulRegistrationInfo) {
        for (var message : mapper.successfulRegistrationDataToSendMessage(successfulRegistrationInfo)) {
            telegramBot.execute(message);
        }
    }

    @SneakyThrows
    @Override
    public void notifyAboutRequestReceived(RequestDataInfo<?> data) {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(data.customerTelegramId());
        sendChatAction.setAction(ActionType.TYPING);
        telegramBot.execute(sendChatAction);
    }

    @SneakyThrows
    @Override
    public void sendRecognizedText(RecognitionRequestResultInfo resultInfo) {
        var message = mapper.recognitionRequestResultToSendMessage(resultInfo);
        telegramBot.execute(message);
    }

    @SneakyThrows
    @Override
    public void sendVoiceRequestSummary(SummarizationVoiceRequestResultInfo resultInfo) {
        for (var message : mapper.summarizationVoiceRequestResultToSendMessage(resultInfo)) {
            telegramBot.execute(message);
        }
    }

    @SneakyThrows
    @Override
    public void sendTextRequestSummary(SummarizationTextRequestResultInfo resultInfo) {
        var message = mapper.summarizationTextRequestResultToSendMessage(resultInfo);
        telegramBot.execute(message);
    }

    @SneakyThrows
    @Override
    public void sendGptChatResponse(GptRequestResultInfo resultInfo) {
        var message = mapper.gptResponseRequestResultToSendMessage(resultInfo);
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            message.enableMarkdown(false);
            telegramBot.execute(message);
        }
    }

    @SneakyThrows
    @Override
    public void notifyAboutSuccessfulSettingsActionTypeChange(CustomerSettingsActionTypeInfo actionTypeInfo) {
        var message = mapper.customerSettingsInfoToSendMessage(actionTypeInfo);
        telegramBot.execute(message);
    }

    @SneakyThrows
    @Override
    public void notifyAboutNewChatCreatedSuccessfully(NewChatRequestInfo info) {
        var message = mapper.newChatRequestInfoToSendMessage(info);
        telegramBot.execute(message);
    }

    @SneakyThrows
    @Override
    public void notifyAboutInsufficientBalance(InsufficientBalanceInfo insufficientBalanceInfo) {
       var message = mapper.insufficientBalanceInfoToSendMessage(insufficientBalanceInfo);
       telegramBot.execute(message);
    }

    @SneakyThrows
    @Override
    public void notifyAboutSuccessfulSettingsGptModelChange(CustomerSettingsGptModelInfo modelInfo) {
        var message = mapper.customerSettingsGptModelInfoToSendMessage(modelInfo);
        telegramBot.execute(message);
    }
}

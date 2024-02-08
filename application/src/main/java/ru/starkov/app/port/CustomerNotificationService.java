package ru.starkov.app.port;


import ru.starkov.app.dto.in.CustomerSettingsActionTypeInfo;
import ru.starkov.app.dto.in.CustomerSettingsGptModelInfo;
import ru.starkov.app.dto.in.RequestDataInfo;
import ru.starkov.app.dto.out.*;
import ru.starkov.app.dto.in.NewChatRequestInfo;


public interface CustomerNotificationService {

    void notifyCustomerAboutSuccessfulRegistration(SuccessfulRegistrationInfo successfulRegistrationInfo);

    void notifyAboutRequestReceived(RequestDataInfo<?> data);

    void sendRecognizedText(RecognitionRequestResultInfo resultInfo);

    void sendVoiceRequestSummary(SummarizationVoiceRequestResultInfo resultInfo);

    void sendTextRequestSummary(SummarizationTextRequestResultInfo resultInfo);

    void sendGptChatResponse(GptRequestResultInfo resultInfo);

    void notifyAboutSuccessfulSettingsActionTypeChange(CustomerSettingsActionTypeInfo actionTypeInfo);

    void notifyAboutNewChatCreatedSuccessfully(NewChatRequestInfo info);

    void notifyAboutInsufficientBalance(InsufficientBalanceInfo insufficientBalanceInfo);

    void notifyAboutSuccessfulSettingsGptModelChange(CustomerSettingsGptModelInfo modelInfo);
}
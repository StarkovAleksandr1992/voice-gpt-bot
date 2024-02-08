package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.dto.in.gpt_response.GptResponseInfo;
import ru.starkov.app.dto.out.gpt_request.GptChatRequest;
import ru.starkov.app.dto.out.gpt_request.GptPromptRequest;
import ru.starkov.app.dto.out.gpt_request.GptSummarizationRequest;
import ru.starkov.app.port.CustomerRequestGateWay;
import ru.starkov.app.port.EventPublisher;
import ru.starkov.app.port.GptService;
import ru.starkov.app.port.SpeechToTextService;
import ru.starkov.app.usecase.HandleCustomerRequestByGpt;
import ru.starkov.dom.entity.Chat;
import ru.starkov.dom.entity.CustomerRequest;
import ru.starkov.dom.entity.RequestData;
import ru.starkov.dom.entity.identifier.ChatIdGenerator;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.dom.value.CustomerSettings;


@Service
@RequiredArgsConstructor
public class HandleCustomerRequestByGptImpl implements HandleCustomerRequestByGpt {
    private final CustomerRequestGateWay customerRequestGateWay;
    private final SpeechToTextService speechToTextService;
    private final GptService gptService;
    private final ChatIdGenerator chatIdGenerator;
    private final EventPublisher eventPublisher;

    @Override
    public void execute(CustomerRequestId customerRequestId) {
        var customerRequest = customerRequestGateWay.findById(customerRequestId)
                .orElseThrow(() -> new RuntimeException("No customer request found with given ID: " + customerRequestId.toString()));
        var gptResponse = handleByActionType(customerRequest);
        var result = customerRequest.handleByGpt(gptResponse.gptResponse(), gptResponse.totalTokens());
        customerRequestGateWay.save(customerRequest);
        eventPublisher.publish(result.getEvent());
    }

    private GptResponseInfo handleByActionType(CustomerRequest customerRequest) {
        var customerSettings = customerRequest.getCustomer().getCustomerSettings();
        String textToHandleByGpt = getTextToHandleByGpt(customerRequest.getRequestData());
        GptResponseInfo gptResponse;
        if (customerSettings.getDefaultActionType() == CustomerSettings.ActionType.SUMMARIZE) {
            gptResponse = gptService.summarize(new GptSummarizationRequest(textToHandleByGpt));
        } else if (customerSettings.getDefaultActionType() == CustomerSettings.ActionType.ASK_GPT) {
            gptResponse = handleGptChat(customerRequest, textToHandleByGpt, customerSettings.getDefaultGptModel().getModelUrl());
        } else if (customerSettings.getDefaultActionType() == CustomerSettings.ActionType.ASK_GPT_WITH_PROMPT) {
            gptResponse = gptService.sendGptPromptRequest(
                    new GptPromptRequest(textToHandleByGpt,
                            customerRequest.getRequestData().getPrompt(),
                            customerSettings.getDefaultGptModel().getModelUrl()));
        } else {
            throw new RuntimeException("Unsupported action type encountered: " + customerSettings.getDefaultActionType());
        }
        return gptResponse;
    }

    private GptResponseInfo handleGptChat(CustomerRequest customerRequest, String textToHandleByGpt, String gptModelUrl) {
        var activeChat = customerRequest
                .getCustomer()
                .getChats()
                .stream()
                .filter(chat -> chat.getStatus() == Chat.Status.ACTIVE)
                .findFirst()
                .orElseGet(() -> customerRequest.getCustomer().createNewChat(chatIdGenerator));
        activeChat.addMessage(textToHandleByGpt);
        var gptChatResponse = gptService.sendGptChatRequest(GptChatRequest.create(activeChat, gptModelUrl));
        activeChat.addMessage(gptChatResponse.gptResponse());
        return gptChatResponse;
    }

    private String getTextToHandleByGpt(RequestData<?> requestData) {
        String textToHandleByGpt;
        if (requestData.getDataType() == RequestData.RequestDataType.VOICE) {
            textToHandleByGpt = speechToTextService.recognize((byte[]) requestData.getData());
            requestData.setRecognizedVoiceRequest(textToHandleByGpt);
        } else if (requestData.getDataType() == RequestData.RequestDataType.TEXT) {
            textToHandleByGpt = (String) requestData.getData();
        } else {
            throw new RuntimeException("Unsupported RequestDataType: " + requestData.getDataType());
        }
        return textToHandleByGpt;
    }
}
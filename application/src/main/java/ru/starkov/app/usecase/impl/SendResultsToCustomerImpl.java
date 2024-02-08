package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.dto.out.GptRequestResultInfo;
import ru.starkov.app.dto.out.RecognitionRequestResultInfo;
import ru.starkov.app.dto.out.SummarizationTextRequestResultInfo;
import ru.starkov.app.dto.out.SummarizationVoiceRequestResultInfo;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.app.port.CustomerRequestGateWay;
import ru.starkov.app.port.EventPublisher;
import ru.starkov.dom.entity.Customer;
import ru.starkov.dom.entity.CustomerRequest;
import ru.starkov.dom.value.CustomerSettings;
import ru.starkov.dom.entity.RequestData;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.common.event.ResultWithEvent;
import ru.starkov.dom.event.ResultsSentToCustomerEvent;
import ru.starkov.app.usecase.SendResultsToCustomer;

@Service
@RequiredArgsConstructor
public class SendResultsToCustomerImpl implements SendResultsToCustomer {

    private final CustomerRequestGateWay customerRequestGateWay;

    private final CustomerNotificationService customerNotificationService;

    private final EventPublisher eventPublisher;


    @Override
    public void execute(CustomerRequestId customerRequestId) {
        var customerRequestOptional = customerRequestGateWay.findById(customerRequestId);
        if (customerRequestOptional.isEmpty()) {
            throw new RuntimeException("No customer request found with given id.");
        }

        var customerRequest = customerRequestOptional.get();
        var requestData = customerRequest.getRequestData();
        var resultData = customerRequest.getResultData().data();
        var customer = customerRequest.getCustomer();
        var customerSettings = customer.getCustomerSettings();

        ResultWithEvent<CustomerRequest, ResultsSentToCustomerEvent> result;

        if (isRecognizeAction(customerSettings, requestData)) {
            result = handleRecognizeAction(customer, resultData, customerRequest);
        } else if (isAskGptAction(customerSettings)) {
            result = handleAskGptAction(customer, resultData, customerRequest);
        } else if (isSummarizeAction(customerSettings)) {
            result = handleSummarizationAction(requestData, customer, resultData, customerRequest);
        } else {
            throw new RuntimeException("Invalid action type encountered.");
        }
        customerRequestGateWay.save(result.getValue());
        eventPublisher.publish(result.getEvent());
    }

    private ResultWithEvent<CustomerRequest, ResultsSentToCustomerEvent> handleSummarizationAction(
            RequestData<?> requestData,
            Customer customer,
            String resultData,
            CustomerRequest customerRequest) {
        if (requestData.getDataType() == RequestData.RequestDataType.VOICE) {
            customerNotificationService
                    .sendVoiceRequestSummary(new SummarizationVoiceRequestResultInfo(
                            customer.getTelegramId(),
                            requestData.getRecognizedVoiceRequest(),
                            resultData));
            return customerRequest.sendResultsToCustomer();

        } else if (requestData.getDataType() == RequestData.RequestDataType.TEXT) {
            customerNotificationService
                    .sendTextRequestSummary(new SummarizationTextRequestResultInfo(
                            customer.getTelegramId(),
                            resultData));
            return customerRequest.sendResultsToCustomer();
        } else {
            throw new RuntimeException("Invalid data type for Summarization Action.");
        }
    }

    private ResultWithEvent<CustomerRequest, ResultsSentToCustomerEvent> handleAskGptAction(
            Customer customer,
            String resultData,
            CustomerRequest customerRequest) {
        customerNotificationService
                .sendGptChatResponse(new GptRequestResultInfo(customer.getTelegramId(), resultData));
        return customerRequest.sendResultsToCustomer();
    }

    private ResultWithEvent<CustomerRequest, ResultsSentToCustomerEvent> handleRecognizeAction(
            Customer customer,
            String resultData,
            CustomerRequest customerRequest) {
        customerNotificationService.sendRecognizedText(
                new RecognitionRequestResultInfo(customer.getTelegramId(), resultData));
        return customerRequest.sendResultsToCustomer();
    }


    private static boolean isAskGptAction(CustomerSettings customerSettings) {
        return customerSettings.getDefaultActionType() == CustomerSettings.ActionType.ASK_GPT ||
                customerSettings.getDefaultActionType() == CustomerSettings.ActionType.ASK_GPT_WITH_PROMPT;
    }

    private static boolean isSummarizeAction(CustomerSettings customerSettings) {
        return customerSettings.getDefaultActionType() == CustomerSettings.ActionType.SUMMARIZE;
    }

    private static boolean isRecognizeAction(CustomerSettings customerSettings, RequestData<?> requestData) {
        return customerSettings.getDefaultActionType() == CustomerSettings.ActionType.RECOGNIZE
                && requestData.getDataType() == RequestData.RequestDataType.VOICE;
    }
}

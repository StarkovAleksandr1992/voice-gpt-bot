package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import ru.starkov.app.dto.in.RequestDataInfo;
import ru.starkov.app.dto.out.InsufficientBalanceInfo;
import ru.starkov.app.port.CustomerGateway;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.app.port.CustomerRequestGateWay;
import ru.starkov.app.port.EventPublisher;
import ru.starkov.dom.entity.Customer;
import ru.starkov.dom.entity.RequestData;
import ru.starkov.dom.entity.identifier.CustomerRequestIdGenerator;
import ru.starkov.dom.entity.identifier.RequestDataIdGenerator;
import ru.starkov.dom.entity.CustomerRequest;
import org.springframework.stereotype.Service;
import ru.starkov.app.usecase.CreateCustomerRequest;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class CreateCustomerRequestImpl implements CreateCustomerRequest {
    private final CustomerGateway customerGateway;
    private final CustomerRequestGateWay customerRequestGateWay;
    private final CustomerRequestIdGenerator customerRequestIdGenerator;
    private final RequestDataIdGenerator requestDataIdGenerator;
    private final CustomerNotificationService customerNotificationService;
    private final EventPublisher eventPublisher;

    @Override
    public void execute(RequestDataInfo<?> data) {
        var customer = customerGateway.findCustomerByTelegramId(data.customerTelegramId())
                .orElseThrow(() -> new RuntimeException("Could not find a customer with the provided Telegram ID"));

        if (hasSufficientBalance(customer)) {
            processRequestData(data, customer);
        } else {
            notifyInsufficientBalance(customer);
        }
    }


    private boolean hasSufficientBalance(Customer customer) {
        return customer.getTokenAccount().getBalance().compareTo(BigInteger.ZERO) > 0;
    }

    private void processRequestData(RequestDataInfo<?> data, Customer customer) {
        var requestData = createRequestData(data);
        customerNotificationService.notifyAboutRequestReceived(data);
        var resultWithEvent = CustomerRequest.create(customerRequestIdGenerator, customer, requestData);
        customerRequestGateWay.save(resultWithEvent.getValue());
        eventPublisher.publish(resultWithEvent.getEvent());
    }

    private RequestData<?> createRequestData(RequestDataInfo<?> data) {
        return switch (data.dataType()) {
            case TEXT -> createTextRequestData(data);
            case VOICE -> createVoiceRequestData(data);
            default -> throw new RuntimeException("Invalid data type. Only text and voice types are supported.");
        };
    }

    private RequestData<?> createTextRequestData(RequestDataInfo<?> data) {
        return data.prompt() != null ?
                RequestData.createTextRequestDataWithPrompt(requestDataIdGenerator, (String) data.data(), RequestData.RequestDataType.TEXT, data.prompt()) :
                RequestData.createTextRequestData(requestDataIdGenerator, (String) data.data(), RequestData.RequestDataType.TEXT);
    }

    private RequestData<?> createVoiceRequestData(RequestDataInfo<?> data) {
        return RequestData.createVoiceRequestData(requestDataIdGenerator, (byte[]) data.data(), RequestData.RequestDataType.VOICE, data.voiceRequestDuration());
    }

    private void notifyInsufficientBalance(Customer customer) {
        var balance = customer.getTokenAccount().getBalance();
        customerNotificationService.notifyAboutInsufficientBalance(new InsufficientBalanceInfo(customer.getTelegramId(), balance));
    }
}

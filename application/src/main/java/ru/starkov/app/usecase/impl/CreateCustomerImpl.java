package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.dto.out.SuccessfulRegistrationInfo;
import ru.starkov.app.port.CustomerGateway;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.app.port.EventPublisher;
import ru.starkov.dom.entity.Customer;
import ru.starkov.dom.entity.identifier.CustomerIdGenerator;
import ru.starkov.app.dto.in.CustomerInfo;
import ru.starkov.dom.entity.identifier.TokenAccountIdGenerator;
import ru.starkov.app.usecase.CreateCustomer;

@Service
@RequiredArgsConstructor
public class CreateCustomerImpl implements CreateCustomer {

    private final CustomerGateway gateway;

    private final CustomerIdGenerator customerIdGenerator;

    private final TokenAccountIdGenerator tokenAccountIdGenerator;

    private final CustomerNotificationService customerNotificationService;

    private final EventPublisher eventPublisher;

    @Override
    public void execute(CustomerInfo customerInfo) {
        var customerCreatedEvent = Customer.create(
                customerIdGenerator,
                customerInfo.customerTelegramId(),
                customerInfo.firstName(),
                customerInfo.lastName(),
                customerInfo.languageCode(),
                tokenAccountIdGenerator);
        gateway.save(customerCreatedEvent.getValue());
        eventPublisher.publish(customerCreatedEvent.getEvent());
        customerNotificationService
                .notifyCustomerAboutSuccessfulRegistration(
                        new SuccessfulRegistrationInfo(
                                customerInfo.customerTelegramId(),
                                customerInfo.firstName()));
    }
}
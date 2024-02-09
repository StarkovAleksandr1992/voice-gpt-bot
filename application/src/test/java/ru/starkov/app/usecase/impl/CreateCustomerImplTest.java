package ru.starkov.app.usecase.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starkov.app.dto.in.CustomerInfo;
import ru.starkov.app.dto.out.SuccessfulRegistrationInfo;
import ru.starkov.app.port.CustomerGateway;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.app.port.EventPublisher;
import ru.starkov.dom.entity.Customer;
import ru.starkov.dom.entity.identifier.CustomerId;
import ru.starkov.dom.entity.identifier.CustomerIdGenerator;
import ru.starkov.dom.entity.identifier.TokenAccountId;
import ru.starkov.dom.entity.identifier.TokenAccountIdGenerator;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerImplTest {
    @Spy
    private CustomerGateway gateway;

    @Spy
    private CustomerIdGenerator customerIdGenerator;

    @Spy
    private TokenAccountIdGenerator tokenAccountIdGenerator;

    @Spy
    private CustomerNotificationService customerNotificationService;

    @Spy
    private EventPublisher eventPublisher;

    @InjectMocks
    private CreateCustomerImpl createCustomer;


    @Test
    public void testExecute() {
        // Arrange
        CustomerInfo customerInfo = new CustomerInfo(123L, "John", "Doe", "en");
        when(customerIdGenerator.generate()).thenReturn(new CustomerId(1L));
        when(tokenAccountIdGenerator.generate()).thenReturn(new TokenAccountId(1L));
        var customerCreationResult = Customer.create(
                customerIdGenerator,
                customerInfo.customerTelegramId(),
                customerInfo.firstName(),
                customerInfo.lastName(),
                customerInfo.languageCode(),
                tokenAccountIdGenerator);

        // Act
        createCustomer.execute(customerInfo);

        // Assert
        verify(gateway, times(1)).save(eq(customerCreationResult.getValue()));
        verify(eventPublisher, times(1)).publish(eq(customerCreationResult.getEvent()));
        verify(customerNotificationService, times(1)).notifyCustomerAboutSuccessfulRegistration(any(SuccessfulRegistrationInfo.class));
    }
}
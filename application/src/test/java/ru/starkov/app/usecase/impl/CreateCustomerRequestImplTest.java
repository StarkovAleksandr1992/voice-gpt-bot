package ru.starkov.app.usecase.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starkov.app.dto.in.RequestDataInfo;
import ru.starkov.app.dto.out.InsufficientBalanceInfo;
import ru.starkov.app.port.CustomerGateway;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.app.port.CustomerRequestGateWay;
import ru.starkov.app.port.EventPublisher;
import ru.starkov.dom.entity.Customer;
import ru.starkov.dom.entity.CustomerRequest;
import ru.starkov.dom.entity.TokenAccount;
import ru.starkov.dom.entity.identifier.CustomerRequestIdGenerator;
import ru.starkov.dom.entity.identifier.RequestDataIdGenerator;
import ru.starkov.dom.event.CustomerRequestCreatedEvent;

import java.math.BigInteger;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
class CreateCustomerRequestImplTest {
    @Mock
    private CustomerGateway customerGateway;
    @Mock
    private CustomerRequestGateWay customerRequestGateWay;
    @Mock
    private CustomerRequestIdGenerator customerRequestIdGenerator;
    @Mock
    private RequestDataIdGenerator requestDataIdGenerator;
    @Mock
    private CustomerNotificationService customerNotificationService;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private CreateCustomerRequestImpl createCustomerRequest;


    @Test
    public void testExecute_success() {
        //Arrange
        var requestDataInfo = createRequestDataInfo();
        var customer = Mockito.mock(Customer.class);
        Result result = new Result(requestDataInfo, customer);
        when(result.customer().getTokenAccount()).thenReturn(Mockito.mock(TokenAccount.class));
        when(result.customer().getTokenAccount().getBalance()).thenReturn(BigInteger.ONE);
        when(result.customer().getState()).thenReturn(Customer.State.ACTIVE);
        when(customerGateway.findCustomerByTelegramId(1L)).thenReturn(Optional.of(result.customer()));

        //Act
        createCustomerRequest.execute(result.requestDataInfo());

        //Assert
        verify(customerGateway, times(1)).findCustomerByTelegramId(anyLong());
        verify(customerRequestGateWay, times(1)).save(any(CustomerRequest.class));
        verify(customerNotificationService, times(1)).notifyAboutRequestReceived(any(RequestDataInfo.class));
        verify(eventPublisher, times(1)).publish(any(CustomerRequestCreatedEvent.class));
    }

    private record Result(RequestDataInfo<String> requestDataInfo, Customer customer) {

    }
    @Test
    public void testExecute_insufficientBalance() {
        //Arrange
        var requestDataInfo = createRequestDataInfo();
        var customer = Mockito.mock(Customer.class);
        when(customer.getTokenAccount()).thenReturn(Mockito.mock(TokenAccount.class));
        when(customer.getTokenAccount().getBalance()).thenReturn(BigInteger.ZERO);
        when(customerGateway.findCustomerByTelegramId(1L)).thenReturn(Optional.of(customer));

        //Act
        createCustomerRequest.execute(requestDataInfo);

        //Assert
        verify(customerGateway, times(1)).findCustomerByTelegramId(anyLong());
        verify(customerNotificationService, times(1)).notifyAboutInsufficientBalance(any(InsufficientBalanceInfo.class));
    }

    private static RequestDataInfo<String> createRequestDataInfo() {
        return new RequestDataInfo<>(
                1L,
                RequestDataInfo.DataType.TEXT,
                "Hello world!",
                null,
                null);
    }
}
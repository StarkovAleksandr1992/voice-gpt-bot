package ru.starkov.app.usecase.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starkov.app.port.CustomerRequestGateWay;
import ru.starkov.app.usecase.HandleCustomerRequestByGpt;
import ru.starkov.app.usecase.RecognizeCustomerRequestToText;
import ru.starkov.dom.entity.Customer;
import ru.starkov.dom.entity.CustomerRequest;
import ru.starkov.dom.entity.RequestData;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.dom.value.CustomerSettings;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistributeCustomerRequestImplTest {
    @Mock
    private CustomerRequestGateWay customerRequestGateWay;
    @Mock
    private RecognizeCustomerRequestToText recognizeCustomerRequestToText;
    @Mock
    private HandleCustomerRequestByGpt handleCustomerRequestByGpt;
    @InjectMocks
    private DistributeCustomerRequestImpl distributeCustomerRequest;


    @Test
    void distribute_shouldThrowsRuntimeExceptionWhenCustomerRequestNotFound() {
        //Arrange
        var customerRequestId = new CustomerRequestId(1L);
        when(customerRequestGateWay.findById(customerRequestId)).thenReturn(Optional.empty());

        //Assert
        assertThrows(RuntimeException.class, () -> distributeCustomerRequest.distribute(customerRequestId));
    }

    @Test
    @SuppressWarnings("unchecked")
    void distribute_withFoundCustomerAndDefaultActionRecognizeAndDataTypeVoice_executesRecognizeRequest() {
        //Arrange
        var customerRequestId = new CustomerRequestId(1L);
        var customerRequest = mock(CustomerRequest.class);
        when(customerRequestGateWay.findById(customerRequestId)).thenReturn(Optional.of(customerRequest));
        when(customerRequest.getCustomer()).thenReturn(mock(Customer.class));
        when(customerRequest.getCustomer().getCustomerSettings()).thenReturn(mock(CustomerSettings.class));
        when(customerRequest.getCustomer().getCustomerSettings().getDefaultActionType()).thenReturn(CustomerSettings.ActionType.RECOGNIZE);
        when(customerRequest.getRequestData()).thenReturn(mock(RequestData.class));
        when(customerRequest.getRequestData().getDataType()).thenReturn(RequestData.RequestDataType.VOICE);

        //Act
        distributeCustomerRequest.distribute(customerRequestId);

        //Assert
        verify(recognizeCustomerRequestToText, times(1)).execute(customerRequestId);
    }

    @Test
    void distribute_withFoundCustomerRequestButDefaultActionNotRecognizeOrDataTypeNotVoice_executesHandleByGptRequest() {
        //Arrange
        var customerRequestId = new CustomerRequestId(1L);
        var customerRequest = mock(CustomerRequest.class);
        when(customerRequestGateWay.findById(customerRequestId)).thenReturn(Optional.of(customerRequest));
        when(customerRequest.getCustomer()).thenReturn(mock(Customer.class));
        when(customerRequest.getCustomer().getCustomerSettings()).thenReturn(mock(CustomerSettings.class));
        when(customerRequest.getCustomer().getCustomerSettings().getDefaultActionType()).thenReturn(CustomerSettings.ActionType.ASK_GPT);

        //Act
        distributeCustomerRequest.distribute(customerRequestId);

        //Assert
        verify(handleCustomerRequestByGpt, times(1)).execute(customerRequestId);
    }
}
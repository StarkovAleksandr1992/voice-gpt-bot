package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.port.CustomerRequestGateWay;
import ru.starkov.app.usecase.DistributeCustomerRequest;
import ru.starkov.dom.value.CustomerSettings;
import ru.starkov.dom.entity.RequestData;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.app.usecase.HandleCustomerRequestByGpt;
import ru.starkov.app.usecase.RecognizeCustomerRequestToText;

@Service
@RequiredArgsConstructor
public class DistributeCustomerRequestImpl implements DistributeCustomerRequest {

    private final CustomerRequestGateWay customerRequestGateWay;
    private final RecognizeCustomerRequestToText recognizeCustomerRequestToText;
    private final HandleCustomerRequestByGpt handleCustomerRequestByGpt;

    @Override
    public void distribute(CustomerRequestId customerRequestId) {
        var customerRequestOptional = customerRequestGateWay.findById(customerRequestId);
        if (customerRequestOptional.isEmpty()) {
            throw new RuntimeException("No customer request found with given ID: " + customerRequestId.toString());
        }
        var customerRequest = customerRequestOptional.get();
        var customerSettings = customerRequest.getCustomer().getCustomerSettings();

        if (customerSettings.getDefaultActionType() == CustomerSettings.ActionType.RECOGNIZE &&
                customerRequest.getRequestData().getDataType() == RequestData.RequestDataType.VOICE) {
            recognizeCustomerRequestToText.execute(customerRequestId);
        } else {
            handleCustomerRequestByGpt.execute(customerRequestId);
        }
    }
}

package ru.starkov.app.usecase;

import ru.starkov.dom.entity.identifier.CustomerRequestId;

public interface DistributeCustomerRequest {

    void distribute(CustomerRequestId customerRequestId);
    
}

package ru.starkov.app.usecase;

import ru.starkov.dom.entity.identifier.CustomerRequestId;

public interface HandleCustomerRequestByGpt {
    void execute(CustomerRequestId customerRequestId);
}

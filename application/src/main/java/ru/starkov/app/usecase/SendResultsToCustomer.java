package ru.starkov.app.usecase;

import ru.starkov.dom.entity.identifier.CustomerRequestId;

public interface SendResultsToCustomer {

    void execute(CustomerRequestId customerRequestId);
}

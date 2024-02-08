package ru.starkov.app.usecase;

import ru.starkov.dom.entity.identifier.CustomerRequestId;

public interface RecognizeCustomerRequestToText {
    void execute(CustomerRequestId customerRequestId);
}

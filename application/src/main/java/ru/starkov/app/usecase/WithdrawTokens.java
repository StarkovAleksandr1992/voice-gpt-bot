package ru.starkov.app.usecase;

import ru.starkov.dom.entity.identifier.CustomerRequestId;

public interface WithdrawTokens {

    void withdrawTokens(CustomerRequestId id);
}

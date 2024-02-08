package ru.starkov.app.usecase;

import ru.starkov.app.dto.in.CustomerInfo;

public interface CreateCustomer {
    void execute(CustomerInfo customerInfo);
}

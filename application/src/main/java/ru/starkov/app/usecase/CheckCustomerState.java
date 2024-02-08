package ru.starkov.app.usecase;

import ru.starkov.app.dto.in.CustomerInfo;

public interface CheckCustomerState {
    boolean checkCustomerRegistrationStatus(CustomerInfo customerInfo);
}

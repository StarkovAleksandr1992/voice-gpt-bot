package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.dto.in.CustomerInfo;
import ru.starkov.dom.entity.Customer;
import ru.starkov.app.port.CustomerGateway;
import ru.starkov.app.usecase.CheckCustomerState;


@Service
@RequiredArgsConstructor
public class CheckCustomerStateImpl implements CheckCustomerState {

    private final CustomerGateway customerGateway;

    @Override
    public boolean checkCustomerRegistrationStatus(CustomerInfo customerInfo) {
        return customerGateway.findCustomerByTelegramId(customerInfo.customerTelegramId())
                .filter(customer -> customer.getState() == Customer.State.ACTIVE)
                .isPresent();
    }
}

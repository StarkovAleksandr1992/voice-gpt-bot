package ru.starkov.app.port;

import ru.starkov.dom.entity.Customer;
import lombok.NonNull;

import java.util.Optional;

public interface CustomerGateway {
    void save(@NonNull Customer value);

    Optional<Customer> findCustomerByTelegramId(Long customerTelegramId);
}

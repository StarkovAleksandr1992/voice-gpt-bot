package ru.starkov.app.port;


import lombok.NonNull;
import ru.starkov.dom.entity.CustomerRequest;
import ru.starkov.dom.entity.identifier.CustomerRequestId;

import java.util.Optional;

public interface CustomerRequestGateWay {
    void save(@NonNull CustomerRequest value);

    Optional<CustomerRequest> findById(CustomerRequestId id);


}

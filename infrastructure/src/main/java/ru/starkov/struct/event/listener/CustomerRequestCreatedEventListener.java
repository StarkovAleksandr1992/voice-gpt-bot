package ru.starkov.struct.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.starkov.dom.event.CustomerRequestCreatedEvent;
import ru.starkov.app.usecase.DistributeCustomerRequest;

@Component
@RequiredArgsConstructor
public class CustomerRequestCreatedEventListener {

    private final DistributeCustomerRequest distributeCustomerRequest;

    @EventListener
    public void onCustomerRequestCreated(CustomerRequestCreatedEvent event) {
        distributeCustomerRequest.distribute(event.customerRequestId());
    }
}

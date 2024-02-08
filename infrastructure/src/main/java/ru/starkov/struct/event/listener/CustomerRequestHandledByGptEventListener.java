package ru.starkov.struct.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.starkov.dom.event.CustomerRequestHandledByGptEvent;
import ru.starkov.app.usecase.SendResultsToCustomer;

@Component
@RequiredArgsConstructor
public class CustomerRequestHandledByGptEventListener {
    private final SendResultsToCustomer sendResultsToCustomer;

    @EventListener(value = CustomerRequestHandledByGptEvent.class)
    public void onCustomerRequestHandledByGptEvent(CustomerRequestHandledByGptEvent event) {
        sendResultsToCustomer.execute(event.customerRequestId());
    }
}

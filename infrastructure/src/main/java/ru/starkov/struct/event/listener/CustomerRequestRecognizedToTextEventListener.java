package ru.starkov.struct.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.starkov.dom.event.CustomerRequestRecognizedToTextEvent;
import ru.starkov.app.usecase.SendResultsToCustomer;

@Component
@RequiredArgsConstructor
public class CustomerRequestRecognizedToTextEventListener {
    private final SendResultsToCustomer sendResultsToCustomer;

    @EventListener
    public void onAudioMessageRecognizedEvent(CustomerRequestRecognizedToTextEvent event) {
        sendResultsToCustomer.execute(event.customerRequestId());
    }
}

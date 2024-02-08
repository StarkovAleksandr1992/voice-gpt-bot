package ru.starkov.struct.event.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.starkov.common.mark.DomainEvent;
import ru.starkov.app.port.EventPublisher;

@Component
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public <T extends DomainEvent> void publish(T event) {
        eventPublisher.publishEvent(event);
    }
}

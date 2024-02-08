package ru.starkov.app.port;

import ru.starkov.common.mark.DomainEvent;

@FunctionalInterface
public interface EventPublisher {
    <T extends DomainEvent> void publish(T event);
}

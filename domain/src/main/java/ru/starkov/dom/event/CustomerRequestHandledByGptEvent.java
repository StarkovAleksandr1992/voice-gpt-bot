package ru.starkov.dom.event;

import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.common.mark.DomainEvent;

public record CustomerRequestHandledByGptEvent(CustomerRequestId customerRequestId) implements DomainEvent {
}
